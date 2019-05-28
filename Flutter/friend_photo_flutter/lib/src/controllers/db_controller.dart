import 'dart:io';

import 'package:friend_photo_flutter/src/models/friend.dart';
import 'package:path/path.dart';
import 'package:path_provider/path_provider.dart';
import 'package:sqflite/sqflite.dart';

/// Controller that provide access to DB
class DbController {
  /// Default constructor
  DbController();

  final String _tableName = 'Friends';

  /// Database to store information about friends
  Database _databaseInst;

  /// Get database
  Future<Database> get _database async {
    if (_databaseInst == null) _databaseInst = await _getDbInstance();
    return _databaseInst;
  }

  /// Get instance of db after initializing
  Future<Database> _getDbInstance() async {
    Directory directory = await getApplicationDocumentsDirectory();
    String path = join(directory.path, 'friends.db');
    return await openDatabase(path, version: 1,
        onCreate: (Database db, int vers) async {
      await db.execute(
        '''
        CREATE TABLE $_tableName (
          id INTEGER PRIMARY KEY,
          first_name TEXT,
          last_name TEXT,
          status TEXT,
          photo_100 TEXT
        )
        ''',
      );
    });
  }

  Future<List<Friend>> getFriendsList() async {
    final db = await _database;
    var response = await db.query(_tableName);

    return response.isEmpty
        ? []
        : response.map((json) => Friend.fromJson(json)).toList();
  }

  /// Set list of friends to DB
  Future<void> setFriendsList(List<Friend> friends) async {
    final db = await _database;
    await Future.wait(friends
        .map((friend) => db.insert(
              _tableName,
              friend.toJson(),
              conflictAlgorithm: ConflictAlgorithm.replace,
            ))
        .toList());
    return;
  }
}
