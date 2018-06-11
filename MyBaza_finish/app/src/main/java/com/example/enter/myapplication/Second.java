package com.example.enter.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Second extends AppCompatActivity {

    //utworzenie zmiennych
    private EditText brand;
    private EditText model;
    private EditText android;
    private EditText www;
    private Button save;
    private Button cancel;
    private Button wwwButton;
    private long _IdWiersza;

    //do sprawdzenia poprawności wpisanych danych
    boolean isBrandFill=false;
    boolean isModelFill=false;
    boolean isAndroidFill=false;
    boolean isWwwFill=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //utworzenie dołączeń do elementów layoutu
        setContentView(R.layout.activity_second);
        brand=(EditText)findViewById(R.id.Brand);
        model=(EditText)findViewById(R.id.Model);
        android=(EditText)findViewById(R.id.Android);
        www=(EditText)findViewById(R.id.WWW);
        save=(Button)findViewById(R.id.save);
        save.setEnabled(false);
        cancel=(Button)findViewById(R.id.cancel);
        wwwButton=(Button)findViewById(R.id.wwwButton);
        wwwButton.setEnabled(false);

        //dodanie listenerów
        brand.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(!brand.getText().toString().equals("")){
                           isBrandFill=true;
                            check();
                        }
                    }
                }
        );
        model.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(!model.getText().toString().equals("")){
                            isModelFill=true;
                            check();
                        }
                    }
                }
        );
        android.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(!android.getText().toString().equals("")){
                            isAndroidFill=true;
                            check();
                        }
                    }
                }
        );
        www.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(!www.getText().toString().equals("")){
                           isWwwFill=true;
                            wwwButton.setEnabled(true);
                            check();
                        }
                    }
                }
        );

        _IdWiersza = -1;
        if(savedInstanceState != null){

            _IdWiersza = savedInstanceState.getLong(DBHelper.ID);
        }
        else{
            Bundle tobolek = getIntent().getExtras();
            if(tobolek != null){
                _IdWiersza = tobolek.getLong(DBHelper.ID);
            }
        }
        if(_IdWiersza != -1){

            fill();
        }


        save.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addItem();
                        returnToMain();
                    }
                }
        );

        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancel();
                    }
                }
        );


    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putLong(DBHelper.ID, _IdWiersza);
    }

    public void cancel(){
        setResult(RESULT_CANCELED);
        finish();
    }

    public void returnToMain(){
        Intent powrotDoMain=new Intent();
        setResult(RESULT_OK, powrotDoMain);
        finish();
    }


    public void addItem(){
        ContentValues values = new ContentValues();

        values.put(DBHelper.BRAND,brand.getText().toString());
        values.put(DBHelper.MODEL,model.getText().toString());
        values.put(DBHelper.ANDROID,android.getText().toString());
        values.put(DBHelper.WWW,www.getText().toString());
        if(_IdWiersza==-1) {
            Uri uriNowego = getContentResolver().insert(Provider.URI_CONTENT,  values);
            _IdWiersza = Integer.parseInt(uriNowego.getLastPathSegment());
        }
        else{
            getContentResolver().update(ContentUris.withAppendedId(Provider.URI_CONTENT,_IdWiersza), values,null,null);
        }
    }

    //sprawdzenie czy wszystkie pola są wypełnione
    public void check(){
        if(isBrandFill & isModelFill & isAndroidFill & isWwwFill){
            save.setEnabled(true);

        }
    }
    //funkcja uzupełniająca

    public void fill(){
        String[] projekcja={DBHelper.BRAND,DBHelper.MODEL,DBHelper.ANDROID,DBHelper.WWW};
        Cursor kursor=getContentResolver().query(ContentUris.withAppendedId(Provider.URI_CONTENT,_IdWiersza),projekcja,null,null,null);
        kursor.moveToFirst();
        int indeks=kursor.getColumnIndexOrThrow(DBHelper.BRAND);
        String wartosc=kursor.getString(indeks);
        brand.setText(wartosc);
        model.setText(kursor.getString(kursor.getColumnIndexOrThrow(DBHelper.MODEL)));
        android.setText(kursor.getString(kursor.getColumnIndexOrThrow(DBHelper.ANDROID)));
        www.setText(kursor.getString(kursor.getColumnIndexOrThrow(DBHelper.WWW)));
        kursor.close();
    }

    //funkcja do obsługi przeglądarki
    public void openWWW(View view){
        String adres=www.getText().toString();
        if(!adres.startsWith("http://")){
            adres="http://"+adres;
        }
        Intent zamiarPrzegladarki = new Intent("android.intent.action.VIEW",Uri.parse(adres));
        startActivity(zamiarPrzegladarki);

    }
    //menu z przyciskiem anuluj
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu2, menu);
        return true;
    }
    //menu wysuwane z przyciskiem anuluj
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);

        switch(item.getItemId()) {
            case R.id.cancel:


                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
