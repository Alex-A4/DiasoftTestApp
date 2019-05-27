import 'package:flutter/material.dart';
import 'package:friend_photo_flutter/src/bloc/bloc.dart';
import 'package:friend_photo_flutter/src/bloc/bloc_states.dart';
import 'package:provider/provider.dart';

import 'auth_screen.dart';
import 'friends_screen.dart';
import 'launch_screen.dart';

class MainScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    var bloc = Provider.of<Bloc>(context);

    return StreamBuilder(
      stream: bloc.state,
      builder: (context, snapshot) {
        if (snapshot.hasData) {
          if (snapshot.data is StateLaunching) return LaunchScreen();
          if (snapshot.data is StateNotAuthenticated) return AuthScreen();
          if (snapshot.data is StateAuthenticated) return FriendsScreen();
        }

        return Container();
      },
    );
  }
}
