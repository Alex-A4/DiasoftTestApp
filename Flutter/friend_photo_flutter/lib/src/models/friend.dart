/// Basic model that contains information about one friend
class Friend {
  // Default values of friend
  final int id;
  final String name;
  final String lastName;
  final String photoUrl; // url of profile photo
  final String status;

  ///Default constructor.
  ///Uses to restore from DB
  const Friend({this.id, this.name, this.lastName, this.photoUrl, this.status});

  /// Constructor to restore data from JSON
  factory Friend.fromJson(Map<String, dynamic> json) {
    return Friend(
      id: json['id'],
      name: json['first_name'],
      lastName: json['last_name'],
      photoUrl: json['photo_100'],
      status: json['status'],
    );
  }

  /// Convert friend object to Json. Needs to set to DB
  Map<String, dynamic> toJson() => {
        'id': id,
        'first_name': name,
        'last_name': lastName,
        'photo_100': photoUrl,
        'status': status
      };
}
