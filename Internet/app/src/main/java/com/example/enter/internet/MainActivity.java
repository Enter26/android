package com.example.enter.internet;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import android.annotation.SuppressLint;


public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    //creating handle to progress acrion
    ProgressBar progressBar;
    TextView text_pobrano;
    long progres_info=0;

 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

     //creating and attach buttons, edit_text and progresBar
     Button b_info = (Button) findViewById(R.id.b_info);
     Button b_pobierz = (Button) findViewById(R.id.b_pobierz);
     final EditText url = (EditText) findViewById(R.id.e_adres);
     progressBar=findViewById(R.id.progressBar);


     //method to dowload info about the file while press button 1
     b_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            ShowTask show = new ShowTask();
            show.execute(url.getText().toString());
          }
        });
     //method to dowload the file while press button 2
        b_pobierz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

    Log.d("DButton","press button");
               MojaIntentService.download(MainActivity.this,url.getText().toString());

             }
        });
    }
    //asynch task class to dowload info about file
    private class ShowTask extends AsyncTask<String ,Integer,String > {


        int file_length=0;
        String file_typ="Sprawdź poprawność adresu";
        final TextView text_rozm = (TextView) findViewById(R.id.text_rozm);
        final TextView text_typ = (TextView) findViewById(R.id.text_typ);

        @Override
        protected String doInBackground(String... params) {
            Log.d("ST", "run");
            String path = params[0];
            HttpURLConnection urlConnection= null; //http connection
            try{
                URL url = new URL(path); //url read handler
                urlConnection = (HttpURLConnection) url.openConnection(); //open connection
                urlConnection.connect();
                Log.d("ST", "connected");
                //download file artibutes
                file_length = urlConnection.getContentLength();
                file_typ = urlConnection.getContentType();
                //logcat info
                Log.d("ST", "size: " + urlConnection.getContentLength());
                Log.d("ST", "typ: " + urlConnection.getContentType());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(urlConnection!=null)
                {
                    Log.d("ST", "disconected");
                    urlConnection.disconnect();
                }
            }

            return "Download Info Complete";
        }
               @Override
        protected void onProgressUpdate(Integer... values) {
           super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {

            //updating interface
            text_rozm.setText(Integer.toString(file_length));
            text_typ.setText(file_typ);
            super.onPostExecute(result);
        }
    }

    private static final int REQUEST_WRITE_PERMISSION = 786;

    //required permission to download
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        }
    }
    //private Handler handler;

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
        }
    }


      private BroadcastReceiver mOdbiorcaRozgloszen = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        //receive info about downloading
        public void onReceive(Context context, Intent intent) {
      //      handler = new Handler();
            //get info from prgresInfo
            Bundle tobolek = intent.getExtras();
            ProgresInfo progresInfo = tobolek.getParcelable(MojaIntentService.Size_info);

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
            text_pobrano=findViewById(R.id.text_pobrano);
            //set progresBar visible
            progressBar.setVisibility(View.VISIBLE);
            progres_info=progresInfo.Downloaded;
            progressBar.setMax(100);
            //changing progress bar color
            progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#0099cc"), android.graphics.PorterDuff.Mode.SRC_IN);
            //set progress
            progressBar.setProgress(progresInfo.Progres);
            text_pobrano.setText(Long.toString(progres_info));
/*      //progress dialog
        progressDialog.setTitle("Download in progress...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);

        handler.post(new Runnable() {
            @Override
            public void run() {
                progressDialog.show();
            }
        });
        progressDialog.setProgress(progresInfo.Progres);

        Log.d("Slider","progresssssss");

        if(progresInfo.Progres>=100)progressDialog.hide();*/

        }
    };
    @Override //register receiver
    protected void onResume() {
        super.onResume();
        registerReceiver(mOdbiorcaRozgloszen, new IntentFilter(
                MojaIntentService.D_INFO));
    }
    @Override //unregister reciver
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mOdbiorcaRozgloszen);
    }

}
