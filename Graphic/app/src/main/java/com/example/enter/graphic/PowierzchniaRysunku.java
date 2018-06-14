package com.example.enter.graphic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.graphics.Paint;
import android.graphics.Path;


public class PowierzchniaRysunku extends SurfaceView implements SurfaceHolder.Callback,
Runnable{

    //drawing path
    private Path mSciezka=null;
    private Paint mFarba = new Paint();

    //zmienne do obsługi współrzędnych
    private float X_move = -1;
    private float Y_move = -1;
    private float Xstart = -1;
    private float Ystart = -1;
    private float XmoveOld = -1;
    private float YmoveOld = -1;

    private boolean shouldErase = false;

    public void erase() {
        shouldErase = true;
    }
    private Bitmap mTempBitmap;
    //funkcje do obsługi bitmapy
    public void setBitmap(Bitmap bitmap) {
        mTempBitmap = bitmap;
    }
    public Bitmap getBitmap() {
        return mBitmapa;
    }


    // pola klasy
    private Bitmap mBitmapa = null;
    // pozwala kontrolować i monitorować powierzchnię
    private Canvas mKanwa = null;
    private SurfaceHolder mPojemnik;
    // wątek, który odświeża kanwę
    private Thread mWatekRysujacy;
    // flaga logiczna do kontrolowania pracy watku
    private boolean mWatekPracuje = false;
    // obiekt do tworzenia sekcji krytycznych
    private Object mBlokada=new Object();

    public PowierzchniaRysunku(Context context, AttributeSet attrs) {
        super(context, attrs);
// Pojemnik powierzchni - pozwala kontrolować i monitorować powierzchnię
        mPojemnik = getHolder();
        mPojemnik.addCallback(this);
        pauzujRysowanie();
        wznowRysowanie();
//inicjalizacja innych elementów...
    }
    public void wznowRysowanie() {
// uruchomienie wątku rysującego
        mWatekRysujacy = new Thread(this);
        mWatekPracuje = true;
        mWatekRysujacy.start();
    }
    public void pauzujRysowanie() {
        mWatekPracuje = false;
    }
    //obsługa dotknięcia ekranu
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        performClick();
        //sekcja krytyczna – modyfikacja rysunku na wyłączność
        synchronized (mBlokada) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pauzujRysowanie();
                    wznowRysowanie();
                    //ustawienie współrzędnych początkowych
                    Xstart = event.getX();
                    Ystart = event.getY();
                    //ustawienie koloru
                    mFarba.setColor(MainActivity.color);
                    //ustawienie szerokości lini
                    mFarba.setStrokeWidth(4);
                    //rysowanie
                    mKanwa.drawCircle(event.getX(), event.getY(), 8, mFarba);
                    //ustawienie współrzędnych końcowych
                    XmoveOld = Xstart;
                    YmoveOld = Ystart;

                  case MotionEvent.ACTION_MOVE:
                      //pobranie współrzędnych początkowych
                      X_move = event.getX();
                      Y_move = event.getY();
                      //rysowanie
                      mKanwa.drawLine(XmoveOld, YmoveOld, X_move, Y_move, mFarba);
                      //ustawienie współrzędnych końcowych
                      XmoveOld = X_move;
                      YmoveOld = Y_move;
                      return true;

                case MotionEvent.ACTION_UP:
//rysowanie...
                    mKanwa.drawCircle(event.getX(), event.getY(), 8, mFarba);
                    break;
            }
        }
        return true;
    }
    //żeby lint nie wyświetlał ostrzeżeń - onTouchEvent i performClick trzeba
    //implementować razem
    public boolean performClick()
    {
        return super.performClick();
    }
    @Override
    public void run() {
        while (mWatekPracuje) {
            Canvas kanwa = null;

            try {
// sekcja krytyczna - żaden inny wątek nie może używać pojemnika
                synchronized (mPojemnik) {
                    // czy powierzchnia jest prawidłowa
                    if (!mPojemnik.getSurface().isValid()) continue;
                    // zwraca kanwę, na której można rysować, każdy piksel
                    // kanwy w prostokącie przekazanym jako parametr musi być
                    // narysowany od nowa inaczej: rozpoczęcie edycji
                    // zawartości kanwy
                    kanwa = mPojemnik.lockCanvas(null);
                    //sekcja krytyczna – dostęp do rysunku na wyłączność
                    synchronized (mBlokada) {
                        if (mWatekPracuje) {
                            //rysowanie na lokalnej kanwie...
                            if(MainActivity.clear ==true){
                                //zamalowanie kanwy na biało
                                mKanwa.drawARGB(255, 255, 255, 255);
                                MainActivity.clear =false;
                            }
                            //wyswietlenie kanwy na ekranie
                            kanwa.drawBitmap(mBitmapa,0,0,null);
                        }
                    }
                }
            } finally {
                // w bloku finally - gdyby wystąpił wyjątek w powyższym
                // powierzchnia zostanie zostawiona w spójnym stanie
                if (kanwa != null) {
                    // koniec edycji kanwy i wyświetlenie rysunku na ekranie
                    mPojemnik.unlockCanvasAndPost(kanwa);
                }
            }
            try {
                Thread.sleep(1000 / 25); // 25
            } catch (InterruptedException e) {
            }
        }
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
// inicjalizacja...
       mBitmapa = Bitmap.createBitmap(getWidth(), getHeight(),
                Bitmap.Config.ARGB_8888);
        mKanwa = new Canvas(mBitmapa); //tworzenie kanwy do bitmapy
// zmalowanie na biało

        mKanwa.drawARGB(255, 255, 255, 255);
        //mKanwa.setBitmap(mBitmapa);

        if(mTempBitmap !=null)
        {

            mKanwa.drawBitmap(mTempBitmap, 0,0,null);
            mTempBitmap.recycle();
        }
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
// zatrzymanie rysowania
        mWatekPracuje = false;
        //w przypadku zminimalizowania, czyszczenie bufora tymczasowego
        mTempBitmap=null;
    }
}
