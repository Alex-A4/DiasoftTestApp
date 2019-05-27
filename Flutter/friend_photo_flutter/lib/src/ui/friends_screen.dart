import 'package:flutter/material.dart';
import 'package:flutter_advanced_networkimage/provider.dart';
import 'package:flutter_advanced_networkimage/transition.dart';
import 'package:friend_photo_flutter/src/bloc/bloc.dart';
import 'package:friend_photo_flutter/src/models/friend.dart';
import 'package:provider/provider.dart';

import 'error_screen.dart';

class FriendsScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    var bloc = Provider.of<Bloc>(context);

    return Scaffold(
      appBar: AppBar(
        title: Text('Friends'),
        centerTitle: true,
      ),
      body: FutureBuilder(
        future: bloc.repository.getFriendsList(),
        builder: (context, snapshot) {
          if (snapshot.hasError) {
            return ErrorScreen(text: 'Unable to load friends list');
          }

          if (snapshot.hasData) {
            return getFriendsList(snapshot.data);
          }

          return Center(child: CircularProgressIndicator());
        },
      ),
    );
  }

  /// Get the list of friends
  Widget getFriendsList(List<Friend> friends) {
    return ListView.builder(
      padding: const EdgeInsets.symmetric(horizontal: 16.0),
      itemCount: friends.length,
      itemBuilder: (_, index) {
        return Container(
          padding: const EdgeInsets.symmetric(vertical: 8.0),
          child: InkWell(
            child: Row(
              children: <Widget>[
                TransitionToImage(
                  image: AdvancedNetworkImage(
                    friends[index].photoUrl,
                    useDiskCache: true,
                    cacheRule: CacheRule(maxAge: const Duration(days: 7)),
                  ),
                  fit: BoxFit.fill,
                  borderRadius: BorderRadius.circular(50.0),
                  width: 60.0,
                  height: 60.0,
                  placeholder: CircularProgressIndicator(),
                ),
                SizedBox(width: 20.0),
                Expanded(
                  child: Text(
                    '${friends[index].name} ${friends[index].lastName}\n${friends[index].status}',
                    maxLines: 3,
                  ),
                ),
              ],
            ),
            onTap: () {},
          ),
        );
      },
    );
  }
}
