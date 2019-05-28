import 'package:friend_photo_flutter/src/controllers/db_controller.dart';
import 'package:friend_photo_flutter/src/controllers/friends_controller.dart';
import 'package:friend_photo_flutter/src/controllers/photos_controller.dart';
import 'package:friend_photo_flutter/src/models/friend.dart';
import 'package:friend_photo_flutter/src/models/photo.dart';
import 'package:friend_photo_flutter/src/models/user.dart';
import 'package:friend_photo_flutter/src/repositories/storage_repo.dart';
import 'package:http/http.dart' as http;

/// Repository that provide full data about friends, photos and user
class DataRepository {
  final FriendsController _friends = FriendsController();
  final PhotosController _photos = PhotosController();
  final StorageRepository _storage = StorageRepository();
  final DbController _db = DbController();
  User _user;

  /// Check is user authenticated
  /// If true, then initialize [_user] with data
  Future<bool> checkAuthentication() async {
    if (await _storage.isUserAuthenticated()) {
      _user = await _storage.getUserData();

      // Read friends list
      var list = await _db.getFriendsList();
      _friends.setFromDb(list);

      return true;
    }
    return false;
  }

  /// Get list of friends
  /// If they were in DB then return momentally
  /// else download them through API
  Future<List<Friend>> getFriendsList() async {
    if (_friends.friends.length != 0) return _friends.friends;

    var response =
        await http.get('https://api.vk.com/method/friends.get?fields=photo_100,'
            'status&v=5.95&access_token=${_user.accessToken}');
    if (response.statusCode == 200) {
      _friends.setFriends(response.body);

      // Save friends to DB
      await _db.setFriendsList(_friends.friends);

      return _friends.friends;
    } else
      throw Exception();
  }

  /// Get list of photos by specified [friendId]
  /// If that photos alredy downloaded, then return them
  Future<List<Photo>> getPhotosList(int friendId) async {
    if (_photos.isIdential(friendId)) return _photos.photos;

    var response =
        await http.get('https://api.vk.com/method/photos.get?owner_id='
            '$friendId&album_id=wall&v=5.95&access_token=${_user.accessToken}');

    // If everything is OK, then return new list
    if (response.statusCode == 200) {
      _photos.ownerId = friendId;
      _photos.setPhotos(response.body);
      return _photos.photos;
    } else
      throw Exception();
  }

  /// Authenticate user from input [url]
  /// There can be error if authentication attempt is wrong
  /// or can be access_token and user_id if everything is ok
  bool authenticateUser(String url) {
    print(url);
    List<String> data = url.split('&');
    // If this is wrong authentication
    if (data[0].contains('error')) return false;

    _user = new User(
        accessToken: data[0].replaceAll('access_token=', ''),
        id: int.parse(data[2].replaceAll('user_id=', '')));
    _storage.saveUserData(_user);

    return true;
  }

  /// Free memory
  void dispose() {
    _friends.dispose();
    _photos.dispose();
  }
}
