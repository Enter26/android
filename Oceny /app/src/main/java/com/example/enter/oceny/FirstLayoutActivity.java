package com.example.enter.oceny;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class FirstLayoutActivity extends AppCompatActivity {

    //deklaracje zmiennych EditText
    private EditText name;
    private  EditText surname;
    private EditText number;
    boolean a1 =false, a2 = false, a3 = false; //zmienna sprawdzające poprawność
    //a1- name, a2- surname, a3-number

    //deklaracja zmiennej button
    private Button button;
    private Button button2;//drugi przycisk (do celów testowych)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_layout);

        //definicja referencji do przycisku
        final Button button = (Button) findViewById(R.id.button);

        //definicja referencji do EditText
        name = (EditText) findViewById(R.id.name);
        surname = (EditText) findViewById(R.id.surname);
        number = (EditText) findViewById(R.id.number);



        //imie - ustawienie focus listenera
        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                //pobranie wartości z pola textEdit
                String userName = name.getText().toString();
                // sprawdzenie poprawności
                if(!userName.matches("^[A-ZŁ]{1}[a-ząęóśłćńżź-]{1,15}\\s?$") && !b){
                    //stworzenie tosta
                    Toast wrongName = Toast.makeText(FirstLayoutActivity.this, "Podaj imie", Toast.LENGTH_SHORT);
                    //wyswietlenie tosta
                    wrongName.show();
                }
            }
        });
        //nazwisko - ustawienie focus listenera
        surname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String userSurname = surname.getText().toString();
                if(!userSurname.matches("^[A-ZŁ]{1}[a-ząęóśłćńżź-]{1,25}\\s?$") && !b){
                    Toast wrongSurname = Toast.makeText(FirstLayoutActivity.this, "Podaj nazwisko", Toast.LENGTH_SHORT);
                    wrongSurname.show();
                }

            }
        });
        //liczba ocen - ustawienie focus listenera
        number.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                //próba pobrania wartości int
                try{
                    int userNumber = Integer.parseInt((number.getText().toString()));
                    //sprawdzenie poprawności wprowadzonej liczby
                    if(userNumber < 5 || userNumber > 15 && !b){
                        Toast wrongNumber = Toast.makeText(FirstLayoutActivity.this, "Podaj liczbe z przedziału 5-15", Toast.LENGTH_SHORT);
                        wrongNumber.show();

                    }

                }//obsługa w przypadku niepowodzenia pobrania liczby
                catch(NumberFormatException e){
                    if(R.id.number == view.getId() && !b){
                        Toast wrongNumber = Toast.makeText(FirstLayoutActivity.this, "Podaj liczbe", Toast.LENGTH_SHORT);
                        wrongNumber.show();
                    }

                }


            }
        });

        //imie- ustawienie textChange listenera
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String userName = name.getText().toString();
                if(!userName.matches("^[A-ZŁ]{1}[a-ząęóśłćńżź-]{1,15}\\s?$")){
                    button.setVisibility(View.INVISIBLE);
                    a1=false;
                }
                else
                {
                    a1=true;
                }
                if(a1&&a2&&a3)button.setVisibility(View.VISIBLE);

            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });


        //nazwisko- ustawienie textChange listenera
        surname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String surname1 = surname.getText().toString();
                if(!surname1.matches("^[A-Z]{1}[a-ząęłńśćźżó-]{2,20}\\s?$")) {
                    button.setVisibility(View.INVISIBLE);
                    a2=false;


                }
                else
                    {
                         a2=true;
                    }
                if(a1&&a2&&a3)button.setVisibility(View.VISIBLE);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });




        //liczba ocen - ustawienie textChange listenera
        number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {

                    int marks = Integer.parseInt(number.getText().toString());
                    if (marks < 5 || marks > 15) {
                        button.setVisibility(View.INVISIBLE);
                        a3 = false;
                    } else
                        {
                            a3=true;
                        }
                    if(a1&&a2&&a3)button.setVisibility(View.VISIBLE);
                }catch(NumberFormatException e) {
                    button.setVisibility(View.INVISIBLE);

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
        //przejście do kolejnej aktywności z przesłaniem ilości ocen
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent zamiar = new Intent(FirstLayoutActivity.this, Second.class);
                zamiar.putExtra("num", number.getText().toString());
                startActivityForResult(zamiar,0);
            }

        });

    }
    //powrót z drugiej aktywnosci
    protected void onActivityResult(int kodZadania, int kodWyniku, Intent dane){
        super.onActivityResult(kodZadania, kodWyniku, dane);
        if(kodWyniku==RESULT_OK){
            //pobranie danych przeslanych z drugiej aktywnosci
            Bundle box2 = dane.getExtras();
            double average = box2.getDouble("average");
            //wyswietlenie textu ze srednia ocen
            TextView view =(TextView) findViewById(R.id.text_view);
            view.setText("Twoja średnia to: "+String.format("%.2f", average));
            button = (Button) findViewById(R.id.button);

           // button2 =(Button)findViewById(R.id.button2); //próba na drugim przycisku
            //jeśli średnia pozytyna, komunikat i zmiana przycisku
            if(average>=3.0){
                button.setText("Super :)");
                //po naciśnieciu zamknięcie i toast
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(),"Gratulacje! Otrzymujesz zaliczenie",Toast.LENGTH_LONG).show();
                        finish();
                    }
                });


            }else
                //jeśli średnia negatywna, komunikat i zmiana przycisku
            {
                button.setText("Tym razem Ci nie poszło");
                //po naciśnieciu zamknięcie i toast
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(), "Wysyłam podanie o zaliczenie warunkowe.", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
            }

        }

    }
}