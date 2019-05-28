import 'dart:async';

import 'package:friend_photo_flutter/src/repositories/data_repository.dart';
import 'package:rxdart/rxdart.dart';

import 'bloc_events.dart';
import 'bloc_states.dart';

/// Main class that provides whole information for the app
class Bloc {
  /// Subject of states
  final BehaviorSubject<State> _statesSubject =
      BehaviorSubject.seeded(StateLaunching());

  /// Subject of events
  final BehaviorSubject<Event> _eventsSubject =
      BehaviorSubject.seeded(EventLaunch());
  final DataRepository repository = DataRepository();

  /// Get stream of states
  Stream<State> get state => _statesSubject.stream;

  Bloc() {
    _bindSubjects();
  }

  /// Bind dynamic converting of events to State
  void _bindSubjects() {
    _eventsSubject.asyncExpand(eventToState).forEach(_statesSubject.sink.add);
  }

  /// Asynchronous convertering of events to states
  Stream<State> eventToState(Event event) async* {
    /// If there is launching then read some helpful data
    /// If data had been initialized then send state that user
    /// authenticated else not
    if (event is EventLaunch) {
      /// Delay process of work
      await Future.delayed(Duration(seconds: 2));
      if (await repository.checkAuthentication())
        yield StateFriends();
      else
        yield StateNotAuthenticated();
    }

    /// Try to authenticate user with input data
    if (event is EventAuthenticate) {
      if (repository.authenticateUser(event.url))
        yield StateFriends();
      else
        yield StateNotAuthenticated();
    }

    if (event is EventFriends) yield StateFriends();

    if (event is EventGallery) {
      yield StateGallery(event.name, event.lastName, event.id);
    }
  }

  /// Dispatch new event to bloc
  void dispatch(Event event) {
    if (event != _eventsSubject.value) _eventsSubject.sink.add(event);
  }

  void dispose() {
    repository.dispose();
    _statesSubject.close();
    _eventsSubject.close();
  }
}
