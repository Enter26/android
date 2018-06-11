package com.example.enter.myapplication;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

public class Provider extends ContentProvider {
    private DBHelper _db_helper;//identyfikator dbhelpera
    //stałe do rozpoznawania URI
    public static final Uri URI_CONTENT = Uri.parse("content://com.example.enter.myapplication.Provider/" + DBHelper.TABLE_NAME);
    private static final UriMatcher _Uri = new UriMatcher(UriMatcher.NO_MATCH);

    static {
//dodanie rozpoznawanych URI
        _Uri.addURI("com.example.enter.myapplication.Provider", DBHelper.TABLE_NAME, 1);
        _Uri.addURI("com.example.enter.myapplication.Provider", DBHelper.TABLE_NAME + "/#", 2);
    }


    //tworzenie pomocnika bazy danych
    @Override
    public boolean onCreate() {
        _db_helper = new DBHelper(getContext());
        return false;
    }

    //funkcja do obsługi kasowania rekordów
    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int typUri = _Uri.match(uri);
//otwieranie bazy danych
        SQLiteDatabase baza=_db_helper.getWritableDatabase();
        int liczbaUsunietych = 0;
        switch (typUri) {
            case 1: //cała tabela
                liczbaUsunietych = baza.delete(DBHelper.TABLE_NAME, selection, selectionArgs);
                break;
            case 2: //wybrany wiersz
                liczbaUsunietych =baza.delete(DBHelper.TABLE_NAME, addToGroup(selection, uri), selectionArgs);
                break;
                //jeśli nie zadziała prawidłowo
            default:
                throw new IllegalArgumentException("Nieznane URI: " + uri);
        }
//powiadomienie
        getContext().getContentResolver().notifyChange(uri, null);
        return liczbaUsunietych;
    }
//funkcja do obsługi dodania rekordu
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        //czy wiersz czy cała tabela i otworzenie bazy
        int typUri = _Uri.match(uri);
        SQLiteDatabase baza = _db_helper.getWritableDatabase();    //otwieranie bazy danych
        long idDodanego = 0;
        switch (typUri) {
            case 1: //cała tabela
                idDodanego=baza.insert(DBHelper.TABLE_NAME,null,values);   //!!!
                break;
            //jeśli nie zadziała prawidłowo
            default:
                throw new IllegalArgumentException("Nieznane URI: " + uri);
        }
        //powiadomienie
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(DBHelper.TABLE_NAME + "/" + idDodanego);
    }


    @Override
    public String getType(Uri uri) {
        return null;
    }

    //kursor
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int typUri = _Uri.match(uri);
        SQLiteDatabase baza=_db_helper.getWritableDatabase();  //otwieranie bazy danych
        Cursor kursor = null;
        switch (typUri) {
            case 1://umieszczenie danych w kursorze //cała tabela
                kursor =baza.query(false, DBHelper.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder, null, null);
                break;
            case 2://umieszczenie danych w kursorze //wybrane wiersze
                kursor = baza.query(false, DBHelper.TABLE_NAME, projection, addToGroup(selection, uri), selectionArgs, null, null, sortOrder, null, null);

                break;
                //jeśli nie zadziała
            default:
                throw new IllegalArgumentException("Nieznane URI: " + uri);
        }
       //Obserwator zmiany danych
        kursor.setNotificationUri(getContext().getContentResolver(), uri);
        return kursor;
    }
    //funkcja do obsłgi zaznaczenia wielu elementów z listy
    private String addToGroup(String group, Uri uri)
    {
        if(group != null && !group.equals(""))
        {
            group = group + " and " + DBHelper.ID + "=" + uri.getLastPathSegment();
        }

        else
        {
            group = DBHelper.ID + "=" + uri.getLastPathSegment();
        }

        return group;
    }



    //aktualizacja rekordu
    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int typUri = _Uri.match(uri);
//otwieranie bzy danych
        SQLiteDatabase baza=_db_helper.getWritableDatabase();
        int liczbaZaktualizowanych = 0;
        switch (typUri) {
            case 1:// cała tabela
                liczbaZaktualizowanych = baza.update(DBHelper.TABLE_NAME,values,selection,selectionArgs);
                break;
            case 2://wybrane rekordy
                liczbaZaktualizowanych = baza.update(DBHelper.TABLE_NAME,values,addToGroup(selection,uri),selectionArgs);
                break;
                //jeśli coś nie zadziała
            default:
                throw new IllegalArgumentException("Nieznane URI: " + uri);
        }
//powiadomienie
        getContext().getContentResolver().notifyChange(uri, null);
        return liczbaZaktualizowanych;
    }
}
