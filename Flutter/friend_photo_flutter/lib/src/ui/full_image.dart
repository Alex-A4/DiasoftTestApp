import 'package:flutter/material.dart';
import 'package:flutter_advanced_networkimage/provider.dart';
import 'package:flutter_advanced_networkimage/transition.dart';
import 'package:flutter_advanced_networkimage/zoomable.dart';

class FullImageView extends StatelessWidget {
  final String url;
  final String text;

  const FullImageView({Key key, this.url, this.text}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(backgroundColor: Colors.transparent),
      backgroundColor: Colors.black,
      body: ZoomableWidget(
        autoCenter: true,
        bounceBackBoundary: true,
        child: TransitionToImage(
          image: AdvancedNetworkImage(
            url,
            useDiskCache: true,
            cacheRule: CacheRule(maxAge: Duration(days: 7)),
          ),
          fit: BoxFit.contain,
          placeholder: CircularProgressIndicator(),
        ),
        maxScale: 3.0,
        minScale: 1.0,
        zoomSteps: 2,
      ),
    );
  }
}
