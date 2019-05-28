import 'package:flutter/material.dart';
import 'package:flutter_advanced_networkimage/provider.dart';
import 'package:flutter_advanced_networkimage/transition.dart';
import 'package:flutter_advanced_networkimage/zoomable.dart';

class FullImageView extends StatefulWidget {
  final String url;
  final String text;

  const FullImageView({Key key, this.url, this.text}) : super(key: key);

  @override
  _FullImageViewState createState() => _FullImageViewState();
}

class _FullImageViewState extends State<FullImageView> {
  bool isTextVisible = true;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.black,
      body: Stack(
        children: <Widget>[
          ZoomableWidget(
            autoCenter: true,
            bounceBackBoundary: true,
            child: TransitionToImage(
              image: AdvancedNetworkImage(
                widget.url,
                useDiskCache: true,
                cacheRule: CacheRule(maxAge: Duration(days: 7)),
              ),
              fit: BoxFit.contain,
              placeholder: CircularProgressIndicator(),
            ),
            maxScale: 3.0,
            minScale: 1.0,
            zoomSteps: 2,
            onTap: () => setState(() => isTextVisible = !isTextVisible),
          ),
          if (isTextVisible)
            Container(
              alignment: Alignment.topLeft,
              height: 80.0,
              width: double.infinity,
              color: Color(0x5F000000),
              padding:
                  const EdgeInsets.only(left: 8.0, top: 25.0),
              child: IconButton(
                icon: Icon(Icons.arrow_back, color: Colors.white, size: 35.0),
                onPressed: () => Navigator.of(context).pop(),
              ),
            ),
          if (isTextVisible)
            Align(
              alignment: Alignment.bottomCenter,
              child: Container(
                color: Color(0x5F000000),
                padding: const EdgeInsets.all(16.0),
                width: double.infinity,
                child: Text(
                  widget.text,
                  maxLines: 3,
                  textAlign: TextAlign.center,
                  style: TextStyle(color: Colors.white, fontSize: 20.0),
                ),
              ),
            ),
        ],
      ),
    );
  }
}
