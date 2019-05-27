/// List of states that app can take

/// Basic general state
abstract class State {}

/// State means that app was launched and helpful data is downloading
class StateLaunching extends State {}

/// State means that user was not authenticated
class StateNotAuthenticated extends State {}

/// State means that user authenticated
class StateAuthenticated extends State {}
