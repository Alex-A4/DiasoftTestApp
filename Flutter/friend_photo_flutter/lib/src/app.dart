import 'package:flutter/material.dart';
import 'package:friend_photo_flutter/src/bloc/bloc.dart';
import 'package:provider/provider.dart';

import 'ui/main_screen.dart';

/// Widget that initialize provider and starting screen
class App extends StatelessWidget {
  final Bloc bloc = Bloc();

  @override
  Widget build(BuildContext context) {
    return Provider(
      builder: (_) => bloc,
      child: MaterialApp(
        theme: theme,
        home: MainScreen(),
      ),
    );
  }
}

/// Default theme of app
final theme = ThemeData(
  primaryColor: Color(0xFFB0283C),
  accentColor: Color(0xFF80A000),
);
