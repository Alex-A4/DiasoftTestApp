package alexa4.friendphoto.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import alexa4.friendphoto.models.Friend;


/**
 * Controller that helps to r/w database information about friends
 */
public class DbController extends SQLiteOpenHelper {
    private static final String TAG = "DbController";

    public DbController(Context context) {
        super(context, "friendPhotoDb", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Creating DB");
        db.execSQL("create table friendsDB ("
                + "id integer primary key,"
                + "name text,"
                + "lastName text,"
                + "status text,"
                + "photo text"
                + ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    /**
     * Read list of friends from DB
     */
    public ArrayList<Friend> readFriends() {
        ArrayList<Friend> friends = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("friendsDB", null, null,
                null, null, null, null);

        if (cursor.moveToFirst()) {
            int idCol = cursor.getColumnIndex("id");
            int nameCol = cursor.getColumnIndex("name");
            int lastNameCol = cursor.getColumnIndex("lastName");
            int statusCol = cursor.getColumnIndex("status");
            int photoCol = cursor.getColumnIndex("photo");

            do {
                int id = cursor.getInt(idCol);
                String name = cursor.getString(nameCol);
                String lastName = cursor.getString(lastNameCol);
                String status = cursor.getString(statusCol);
                String photo = cursor.getString(photoCol);

                friends.add(new Friend(id, name, lastName, photo, status));
            } while (cursor.moveToNext());
        }

        cursor.close();

        Log.d(TAG, "Reading completed, read " + friends.size() + " items");

        return friends;
    }

    public void saveFriends(ArrayList<Friend> friends) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        for(Friend friend : friends) {
            values.put("id", friend.mId);
            values.put("name", friend.mName);
            values.put("lastName", friend.mLastName);
            values.put("status", friend.mStatus);
            values.put("photo", friend.mPhotoUrl);

            db.insert("friendsDB", null, values);
        }

        Log.d(TAG, "Writing completed");
    }
}
