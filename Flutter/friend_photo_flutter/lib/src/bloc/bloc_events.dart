/// List of events that app can send

/// Basic general event
abstract class Event {}

/// Event means that app launchind and need to download data
class EventLaunch extends Event {}

/// Event means that user authenticated and send [url] that
/// contains token and id or error
class EventAuthenticate extends Event {
  final String url;

  EventAuthenticate(this.url);
}
