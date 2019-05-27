import 'dart:async';

import 'package:flutter/material.dart';
import 'package:friend_photo_flutter/src/bloc/bloc.dart';
import 'package:friend_photo_flutter/src/bloc/bloc_events.dart';
import 'package:provider/provider.dart';
import 'package:flutter_webview_plugin/flutter_webview_plugin.dart';

class AuthScreen extends StatefulWidget {
  @override
  _AuthScreenState createState() => _AuthScreenState();
}

class _AuthScreenState extends State<AuthScreen> {
  final webViewPlugin = FlutterWebviewPlugin();
  final String startUrl = 'https://oauth.vk.com/authorize?client_id=6994466&' +
      'display=page&scope=65538&response_type=token&v=5.95';
  StreamSubscription<String> _onUrlChgd;

  @override
  void dispose() {
    _onUrlChgd.cancel();
    webViewPlugin.stopLoading();
    webViewPlugin.close();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final bloc = Provider.of<Bloc>(context);
    _onUrlChgd = webViewPlugin.onUrlChanged.listen((url) {
      print(url);
      Uri uri = Uri.parse(url);
      if ('${uri.host}${uri.path}' == 'oauth.vk.com/blank.html') {
        webViewPlugin.stopLoading();
        webViewPlugin.close();
        bloc.dispatch(EventAuthenticate(uri.fragment));
      }
    });

    Future.delayed(
        Duration(seconds: 1),
        () => webViewPlugin.launch(
              startUrl,
              rect: Rect.fromLTWH(0.0, 20.0, MediaQuery.of(context).size.width,
                  MediaQuery.of(context).size.height - 80),
            ));

    return Scaffold();
  }
}
