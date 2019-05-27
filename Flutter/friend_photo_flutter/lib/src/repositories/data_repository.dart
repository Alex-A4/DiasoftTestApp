import 'package:friend_photo_flutter/src/controllers/friends_controller.dart';
import 'package:friend_photo_flutter/src/controllers/photos_controller.dart';
import 'package:friend_photo_flutter/src/models/user.dart';
import 'package:friend_photo_flutter/src/repositories/storage_repo.dart';

/// Repository that provide full data about friends, photos and user
class DataRepository {
  final FriendsController _friends = FriendsController();
  final PhotosController _photos = PhotosController();
  final StorageRepository _storage = StorageRepository();
  User _user;

  /// Check is user authenticated
  /// If true, then initialize [_user] with data
  Future<bool> checkAuthentication() async {
    if (await _storage.isUserAuthenticated()) {
      _user = await _storage.getUserData();

      // TODO: add reading friends from DB
      return true;
    }
    return false;
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
