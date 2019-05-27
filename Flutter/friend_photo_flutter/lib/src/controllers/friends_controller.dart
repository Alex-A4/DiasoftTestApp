import 'dart:convert';

import 'package:friend_photo_flutter/src/models/friend.dart';

/// Controller that contains list of friends and can restore it from json string
class FriendsController {
  static final JsonCodec _codec = JsonCodec();
  final List<Friend> friends = [];

  /// Check is [friends] already downloaded
  bool isLoaded() => friends.length != 0;

  /// Set up friends list from input [jsonString]
  void setFriends(String jsonString) {
    print(jsonString);
    friends.clear();

    try {
      Map<String, dynamic> json = _codec.decode(jsonString)['response'];
      List list = json['items'];
      list.forEach((friend) => friends.add(new Friend.fromJson(friend)));
    } catch (e) {
      print(e);
      friends.clear();
    }
  }

  /// Set up friends list from DB
  void setFromDb(List<Friend> newFriends) {
    friends.addAll(newFriends);
  }

  /// Free memory
  void dispose() {
    friends.clear();
  }
}
