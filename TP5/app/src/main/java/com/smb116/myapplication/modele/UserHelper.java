package com.smb116.myapplication.modele;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class UserHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "userDBTP5";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_USERS = "UserTP5";
    public static final String NOM_COLONNE_ID = "Id";
    public static final String NOM_COLONNE_NOM = "Nom";
    public static final String NOM_COLONNE_PRENOM = "Prenom";
    public static final String NOM_COLONNE_COURRIEL= "Courriel";

    public final String[] ALL = new
            String[]{NOM_COLONNE_ID,NOM_COLONNE_NOM,NOM_COLONNE_PRENOM,NOM_COLONNE_COURRIEL};
    public UserHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String CREATION_DB = "CREATE TABLE " + TABLE_USERS + " (" +
            NOM_COLONNE_ID + " integer primary key autoincrement, " +
            NOM_COLONNE_NOM + " text not null, " +
            NOM_COLONNE_PRENOM + " text not null, " +
            NOM_COLONNE_COURRIEL + " text not null);";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d("log Helper", "onCreat");
        sqLiteDatabase.execSQL(CREATION_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
// Simplest implementation is to drop all old tables and recreate them
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS + ";");
        onCreate(sqLiteDatabase);
    }

    public void setUp() {
        SQLiteDatabase bd = this.getWritableDatabase();
        bd.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS + ";");
        onCreate(bd);
    }

    public void ajout(User user) {
        SQLiteDatabase bd = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NOM_COLONNE_NOM, user.getNom());
        values.put(NOM_COLONNE_PRENOM, user.getPrenom());
        values.put(NOM_COLONNE_COURRIEL, user.getCourriel());
        bd.insert(TABLE_USERS, null, values);
        bd.close();
    }

    public List<User> recupUsers() {
        List<User> Users = new ArrayList<User>();
        SQLiteDatabase bd = this.getReadableDatabase();
        Cursor curseur = bd.query(true, TABLE_USERS, ALL,null,null,null,null,null,null );
        if(curseur.moveToFirst()) do {
            String id = curseur.getString(curseur.getColumnIndexOrThrow(NOM_COLONNE_ID));
            String nom = curseur.getString(curseur.getColumnIndexOrThrow(NOM_COLONNE_NOM));
            String prenom = curseur.getString(curseur.getColumnIndexOrThrow(NOM_COLONNE_PRENOM));
            String courriel = curseur.getString(curseur.getColumnIndexOrThrow(NOM_COLONNE_COURRIEL));
            User user = new User(nom, prenom, courriel);
            user.setId(id);
            Users.add(user);
        } while (curseur.moveToNext());
        curseur.close();
        bd.close();
        return Users;
    }
}

