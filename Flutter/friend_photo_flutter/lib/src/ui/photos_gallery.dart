import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_advanced_networkimage/provider.dart';
import 'package:flutter_advanced_networkimage/transition.dart';
import 'package:friend_photo_flutter/src/bloc/bloc.dart';
import 'package:friend_photo_flutter/src/bloc/bloc_events.dart';
import 'package:friend_photo_flutter/src/models/photo.dart';
import 'package:provider/provider.dart';

import 'error_screen.dart';
import 'full_image.dart';

class PhotosGallery extends StatelessWidget {
  /// Initials of friend
  final String name;
  final String lastName;
  final int friendId;

  const PhotosGallery({Key key, this.name, this.lastName, this.friendId})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    var bloc = Provider.of<Bloc>(context);

    return Scaffold(
      appBar: AppBar(
        title: Text('$name $lastName'),
        centerTitle: true,
        leading: IconButton(
          icon: Icon(Icons.arrow_back),
          onPressed: () => bloc.dispatch(EventFriends()),
        ),
      ),
      body: FutureBuilder(
        future: bloc.repository.getPhotosList(friendId),
        builder: (context, snapshot) {
          if (snapshot.hasError)
            return ErrorScreen(
                text: 'Unable to download photos of $name $lastName');

          if (snapshot.hasData) {
            var data = snapshot.data;
            if (data.length != 0)
              return getPhotoList(snapshot.data, context);
            else
              return ErrorScreen(text: '$name has no photos');
          }
          return Center(child: CircularProgressIndicator());
        },
      ),
    );
  }

  Widget getPhotoList(List<Photo> data, context) {
    return GridView.builder(
      padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 8.0),
      gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
        crossAxisCount: 2,
        crossAxisSpacing: 10.0,
        mainAxisSpacing: 10.0,
      ),
      itemCount: data.length,
      itemBuilder: (_, index) {
        var photo = data[index];
        return InkWell(
          onTap: () => Navigator.of(context).push(
                new MaterialPageRoute(
                    builder: (context) => FullImageView(
                          url: photo.bigSize,
                          text: photo.text,
                        )),
              ),
          child: TransitionToImage(
            image: AdvancedNetworkImage(
              photo.smallSize,
              useDiskCache: true,
              cacheRule: CacheRule(maxAge: Duration(days: 7)),
            ),
            fit: BoxFit.cover,
            height: 70.0,
            placeholder: CircularProgressIndicator(),
          ),
        );
      },
    );
  }
}
