import 'package:flutter/material.dart';

class LaunchScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        mainAxisSize: MainAxisSize.max,
        children: <Widget>[
          Expanded(flex: 1, child: Container()),
          Expanded(
            flex: 2,
            child: FittedBox(
              child: Container(
                decoration: BoxDecoration(
                  image: DecorationImage(
                    image: AssetImage('assets/smile.png'),
                  ),
                  shape: BoxShape.circle,
                ),
                child: CircularProgressIndicator(strokeWidth: 1.0),
              ),
            ),
          ),
          Expanded(
            flex: 1,
            child: Text('Preparing to work'),
          ),
        ],
      ),
    );
  }
}
