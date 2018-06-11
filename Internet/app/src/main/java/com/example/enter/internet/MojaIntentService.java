package com.example.enter.internet;

import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.InterruptedByTimeoutException;

class MojaIntentService extends IntentService{
    //constants required to send info to separate class and methods
    private static final String DOWNLOAD = "downloading_task";
    public final static String D_INFO =
            "com.example.intent_service.odbiornik";
    public final static String Size_info = "Download_info";
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */


    //creating progressInfo instance
    ProgresInfo progresInfo = new ProgresInfo();

    int file_length=0;
    long downloaded =0;
    public MojaIntentService() {
        super("MojaIntentService");
    }

    //main method to download a file
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("OHI","start");
        if(intent!=null) {
            final String action = intent.getAction();
              if (DOWNLOAD.equals(action)) {
                Log.d("OHI", "intent!=null");
                String addres = intent.getStringExtra("url");
                HttpURLConnection urlConnection = null;
                try {
                    //conectig to url
                    Log.d("DT", "Start downloading");
                    URL url = new URL(addres);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.connect();
                    //receive a file length
                    file_length = urlConnection.getContentLength();

                    //creaating download temporary file
                    File downlading_file = new File(url.getFile());
                    File input_file = new File(Environment.getExternalStorageDirectory() +
                            File.separator + downlading_file.getName());

                    //if the same file exist- delete it
                    if (input_file.exists()) {
                        input_file.delete();
                        Log.d("DT", "deleting existing file");
                    }

                    //downloading file
                    DataInputStream reader = new DataInputStream(urlConnection.getInputStream());
                    FileOutputStream outputStream = new FileOutputStream(input_file.getPath());
                    byte[] data = new byte[1024];
                    int total = 0;
                    int count = reader.read(data, 0, 1024);
                    while (count != -1) {
                        outputStream.write(data, 0, count);
                        //changing download field
                        downloaded += count;
                        count = reader.read(data, 0, 1024);
                        send_count();//sending broadcast to update progress bar and downloaded bytes
                    }
                    //close downloading
                    if(reader!=null){
                        reader.close();
                    }
                    if(outputStream!=null)
                    {
                        outputStream.flush();
                        outputStream.close();
                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    //close connection
                    if (urlConnection != null) urlConnection.disconnect();
                }


            }
        }
        else{
            Log.d("DT", "error");
        }

    }

    //get info from main class, start downloading service
    public static void download(Context context, String s) {
        Log.d("download", "download function");
        Intent task = new Intent(context, MojaIntentService.class);
        task.setAction(DOWNLOAD);
        task.putExtra("url", s);
        context.startService(task);
    }//broadcast info about progress
    void send_count(){
        Log.d("SC","send_count");
        Intent zamiar = new Intent(D_INFO);
        progresInfo.Progres=(int) (downloaded*100/file_length);
        progresInfo.Downloaded = downloaded;
        zamiar.putExtra(Size_info,progresInfo);
        sendBroadcast(zamiar);

    }
}
