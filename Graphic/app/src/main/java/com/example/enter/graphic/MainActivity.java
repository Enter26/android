package com.example.enter.graphic;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {

    //ustawienie początkowego koloru na niebieski
    public static int  color =Color.BLUE;
    //flaga wciśnięcia przycisku "X"
    public static boolean clear = false;
    private PowierzchniaRysunku mKanwa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //sprawdzenie czy nie ma zapisanego obrazu w cashu
        mKanwa=findViewById(R.id.powierzchnia_rysunku);
        if (savedInstanceState != null) {
            loadBitmapFromCache(savedInstanceState);
        }
    }

    //obsługa przycisków
    //funkcje do zmiany koloru rysowania, oraz krótkiej animacji
    public void f_red(View view){color = Color.RED;
        Animation animation = new AlphaAnimation(1.0f,0.0f);
        animation.setDuration(1000);
        findViewById(R.id.b_red).startAnimation(animation);
    }
    public void f_yellow(View view){color = Color.YELLOW;
        Animation animation = new AlphaAnimation(1.0f,0.0f);
        animation.setDuration(1000);
        findViewById(R.id.b_white).startAnimation(animation);
    }
    public void f_green(View view){color = Color.GREEN;
        Animation animation = new AlphaAnimation(1.0f,0.0f);
        animation.setDuration(1000);
        findViewById(R.id.b_green).startAnimation(animation);
    }
    public void f_blue(View view){color = Color.BLUE;
        Animation animation = new AlphaAnimation(1.0f,0.0f);
        animation.setDuration(1000);
        findViewById(R.id.b_blue).startAnimation(animation);}
    //funkcja czyszcząca ekran
    public void f_clear(View view){clear = true; mKanwa.erase();
        Animation animation = new AlphaAnimation(1.0f,0.0f);
        animation.setDuration(2000);
        findViewById(R.id.b_clear).startAnimation(animation);}


    //zapisanie bitmapy do bundla cz1
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveBitmapToCache(outState);
    }

    //załadowanie bitmapy z bundla
    private void loadBitmapFromCache(Bundle savedInstanceState) {
        String savedUri = savedInstanceState.getString("bitmap_uri");
        if (savedUri != null) {
            try {
                URI uri = new URI(savedUri);
                Bitmap bitmap = BitmapUtil.loadBitmap(uri);
                mKanwa.setBitmap(bitmap);
            } catch (URISyntaxException e) {

            } catch (IOException e) { }
        }
    }

    //zapisanie bitmapy do bundla cz2
    private void saveBitmapToCache(Bundle outState) {
        Bitmap bitmap = mKanwa.getBitmap();
        try {
            URI uri = BitmapUtil.saveBitmap(this, bitmap, "cached_bitmap.png", Bitmap.CompressFormat.PNG);
            outState.putString("bitmap_uri", uri.toString());
        } catch (IOException e) { }
    }
}
