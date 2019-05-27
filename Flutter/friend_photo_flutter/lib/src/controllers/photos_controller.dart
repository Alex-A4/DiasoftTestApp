import 'dart:convert';

import 'package:friend_photo_flutter/src/models/photo.dart';

/// Controller that contains list of photos of one friend
class PhotosController {
  static final JsonCodec _codec = JsonCodec();

  final List<Photo> photos = [];
  int _ownerId = 0;

  /// Set up photos list from input [jsonString]
  void setPhotos(String jsonString) {
    photos.clear();
    try {
      Map<String, dynamic> json = _codec.decode(jsonString)['response'];
      List list = json['items'];
      list.forEach((photo) => photos.add(new Photo.fromJson(photo)));
    } catch (e) {
      print(e);
      photos.clear();
    }
  }

  /// Check is otherId and this id is equals
  bool isIdential(int otherId) => _ownerId == otherId;

  /// Default setter for id
  set ownerId(int id) => _ownerId = id;

  /// Free memory
  void dispose() {
    photos.clear();
  }
}
