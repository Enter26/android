package com.example.enter.myapplication;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    //makra
    private Context _Kontekst;
    public final static int DB_VERSION = 1;
    public final static String ID = "_id";
    public final static String DB_NAME = "base";
    public final static String TABLE_NAME = "telephones";
    public final static String BRAND = "marka";
    public final static String MODEL = "model";
    public final static String ANDROID = "android";
    public final static String WWW = "www";

    //polecenie do tworzenia bazy
    public final static String DB_CREATE = "CREATE TABLE " + TABLE_NAME + "("+ID+" integer primary key autoincrement, " + BRAND+" text not null,"+ MODEL+" text not null,"+ ANDROID+" text not null, "+WWW+" text not null);";
    //polecenie do skasowania bazy
    private static final String DB_DELETE = "DROP TABLE IF EXISTS "+TABLE_NAME;
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        _Kontekst=context;
    }

    //tworzenie bazy
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DB_CREATE);
    }
    //update bazy (skasowanie porzedniej i stowrzenie nowej)
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DB_DELETE);
        onCreate(sqLiteDatabase);
    }
}
