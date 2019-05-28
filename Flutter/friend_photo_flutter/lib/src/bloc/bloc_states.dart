/// List of states that app can take

/// Basic general state
abstract class State {}

/// State means that app was launched and helpful data is downloading
class StateLaunching extends State {}

/// State means that user was not authenticated
class StateNotAuthenticated extends State {}

/// State means that user authenticated and need to show friends
class StateFriends extends State {}

/// State means that user selected some friend
class StateGallery extends State {
  final String name;
  final String lastName;
  final int id;

  StateGallery(this.name, this.lastName, this.id);
}
