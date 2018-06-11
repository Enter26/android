package com.example.enter.oceny;

import android.content.Intent;
import android.support.v4.widget.ListViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Second extends AppCompatActivity {

    private List marks = new ArrayList<MarksModel>();
    private int marksAmount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        //TextView text = (TextView) findViewById(R.id.number_second);
        try {
            Bundle box = getIntent().getExtras();
            //  text.setText(box.getString("num")); //tymczasowy test działąnia
            marksAmount = Integer.parseInt(box.getString("num"));

            for (int i = 1; i <= marksAmount; i++) {
                marks.add(new MarksModel("ocena" + i));
            }
            //tworzenie nowego adaptera
            MarksAdapter marksAdapter = new MarksAdapter(this, marks);
            ListView listView = (ListView) findViewById(R.id.listView);
            //dołączenie adaptera do listy
            listView.setAdapter(marksAdapter);
        }


        catch (Exception e){
            //text.setText("Obsługa try nie zadziałała");
        }


    }
    //funkcja zwracająca wyniki wpisania ocen - przejście do first layout
    public void finish (View view){
        if(MarksModel.allChecked(marks)){
            Bundle marksData = new Bundle();
            marksData.putDouble("average", MarksModel.average(marks));
            Intent returnMain = new Intent();
            returnMain.putExtras(marksData);
            setResult(RESULT_OK, returnMain);
            finish();

        }
        else
        {
            Toast.makeText(Second.this, "Wypełnij wszystkie pola!",Toast.LENGTH_SHORT).show();

        }

    }
}
