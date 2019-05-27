import 'package:friend_photo_flutter/src/models/user.dart';
import 'package:shared_preferences/shared_preferences.dart';

/// Repository that can communicate with SharedPreferences
/// and r/w info about user
class StorageRepository {
  /// Default constructor
  StorageRepository();

  /// Check is user authenticated
  Future<bool> isUserAuthenticated() async {
    var prefs = await SharedPreferences.getInstance();
    return prefs.getBool('isAuthenticated') ?? false;
  }

  /// Get data about user and return its reference
  /// if there is not some data, then throw error
  Future<User> getUserData() async {
    var prefs = await SharedPreferences.getInstance();
    int id = prefs.getInt('user_id') ?? 0;
    String token = prefs.getString('access_token') ?? null;
    if (id == 0 || token == null) {
      print('Incorrect data');
      throw new Exception('Wrong data in storage');
    }
    print('Authentication success: $id\n$token');
    return User(accessToken: token, id: id);
  }

  Future<void> saveUserData(User user) async {
    var prefs = await SharedPreferences.getInstance();

    await prefs.setString('access_token', user.accessToken);
    await prefs.setInt('user_id', user.id);
    await prefs.setBool('isAuthenticated', true);

    print('User data saved: ${user.id}\n${user.accessToken}');
  }
}
