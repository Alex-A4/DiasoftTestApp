/// Basic model that contains information about one photo
class Photo {
  // Different sizes of image to show in different places
  final String bigSize;
  final String smallSize;
  // Text of photo
  final String text;

  // Private default constructor
  Photo._({this.bigSize, this.smallSize, this.text});

  // Constructor to restore from json
  factory Photo.fromJson(Map<String, dynamic> json) {
    List<dynamic> array = json['sizes'];

    return Photo._(
      text: json['text'],
      bigSize: array[array.length - 1]['url'],
      smallSize: array[(array.length - 1) ~/ 2]['url'],
    );

    // json['text'];
    // array[array.length - 1]['url'];
    // array[((array.length - 1) / 2).toInt()]['url'];

    // JSONArray array = json.getJSONArray("sizes");
    // mBigSize = array.getJSONObject(array.length() - 1).getString("url");
    // mSmallSize = array.getJSONObject((array.length() - 1) / 2).getString("url");
  }
}
