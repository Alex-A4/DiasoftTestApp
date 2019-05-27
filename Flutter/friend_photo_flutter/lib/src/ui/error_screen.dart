import 'package:flutter/material.dart';

import '../app.dart';

class ErrorScreen extends StatelessWidget {
  final String text;
  const ErrorScreen({Key key, this.text}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: <Widget>[
          Icon(
            Icons.sentiment_dissatisfied,
            size: 60.0,
            color: theme.primaryColor,
          ),
          Text(text),
        ],
      ),
    );
  }
}
