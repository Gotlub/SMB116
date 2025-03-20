package com.smb116.myapplication.modele;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

public class UserContentProvider extends ContentProvider {
    public UserContentProvider() {
    }

    // defining authority so that other application can access it
    static final String PROVIDER_NAME = "com.smb116.myapplication.provider";

    // defining content URI
    static final String URL = "content://" + PROVIDER_NAME + "/users";

    // parsing the content URI
    public static final Uri CONTENT_URI = Uri.parse(URL);

    static final int All_USER = 1;
    static final int SINGLE_USER = 2;

    public static final String CONTENT_TYPE = "text/plain";
    public static final String CONTENT_ITEM_TYPE = "text/plain";
    static final UriMatcher uriMatcher;
    private DatabaseHelper dbHelper;

    static {

        // to match the content URI
        // every time user access table under content provider
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        // to access whole table
        uriMatcher.addURI(PROVIDER_NAME, "users", All_USER);

        // to access a particular row
        // of the table
        uriMatcher.addURI(PROVIDER_NAME, "users/#", SINGLE_USER);
    }

    private static HashMap<String, String> values;

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case All_USER:
                return "vnd.android.cursor.dir/vnd.com.smb116.myapplication.provider.users";
            case SINGLE_USER:
                return "vnd.android.cursor.item/vnd.com.smb116.myapplication.provider.users";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    // creating the database
    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        if (db != null) {
            return true;
        }
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case All_USER:
                //do nothing
                break;
            case SINGLE_USER:
                String id = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(NOM_COLONNE_ID + "=" + id);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        return cursor;
    }

    // adding data to the database
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch (uriMatcher.match(uri)) {
            case All_USER:
                //do nothing
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        Log.d("logD inser", values.toString());
        long id = db.insert(TABLE_NAME, null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(CONTENT_URI + "/" + id);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case All_USER:
                break;
            case SINGLE_USER:
                String id = uri.getPathSegments().get(1);
                selection = NOM_COLONNE_ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        int updateCount = db.update(TABLE_NAME, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return updateCount;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case All_USER:
                break;
            case SINGLE_USER:
                String id = uri.getPathSegments().get(1);
                selection = NOM_COLONNE_ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        int updateCount = db.delete(TABLE_NAME, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return updateCount;
    }

    public void setUp() {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
    }


    // creating object of database
    // to perform query
    private SQLiteDatabase db;

    // declaring name of the database
    static final String DATABASE_NAME = "UserDB";

    // declaring table name of the database
    static final String TABLE_NAME = "Users";

    // declaring version of the database
    static final int DATABASE_VERSION = 1;

    public static final String NOM_COLONNE_ID = "Id";
    public static final String NOM_COLONNE_NOM = "Nom";
    public static final String NOM_COLONNE_PRENOM = "Prenom";
    public static final String NOM_COLONNE_COURRIEL= "Courriel";

    // sql query to create the table
    static final String CREATE_DB_TABLE = " CREATE TABLE " + TABLE_NAME + " (" +
            NOM_COLONNE_ID + " integer primary key autoincrement, " +
            NOM_COLONNE_NOM + " text not null, " +
            NOM_COLONNE_PRENOM + " text not null, " +
            NOM_COLONNE_COURRIEL + " text not null);";


    // creating a database
    private static class DatabaseHelper extends SQLiteOpenHelper {


        public static DatabaseHelper getInstance() {
            return instance;
        }

        static DatabaseHelper instance = null;
        // defining a constructor
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            instance = this;
        }

        // creating a table in the database
        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d("log ContentProvider", "onCreate");
            db.execSQL(CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            // sql query to drop a table
            // having similar name
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}