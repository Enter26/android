package com.example.enter.myapplication;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private SimpleCursorAdapter KursorAdapter;
    private ListView Lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //utworzenie listy
        Lista=(ListView)findViewById(R.id.lista_wartosci);
        startLoader();
        Lista.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        Lista.setMultiChoiceModeListener(MultipleChoice());
        Lista.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        Intent zamiar = new Intent(MainActivity.this, Second.class);
                        zamiar.putExtra(DBHelper.ID,id);
                        startActivityForResult(zamiar, 0);
                    }
                }
        );
    }

    public void addNew(){
        Intent zamiar=new Intent(MainActivity.this,Second.class);
        zamiar.putExtra(DBHelper.ID,(long) -1);
        startActivityForResult(zamiar, 0);
    }

    public void startLoader(){
        getLoaderManager().initLoader(0,null, this);
        String[] mapujZ=new String[]{DBHelper.BRAND,DBHelper.MODEL};
        int[] mapujDo=new int[]{R.id.Brand,R.id.Model};
        KursorAdapter=new SimpleCursorAdapter(this,R.layout.item,null,mapujZ,mapujDo,0);
        Lista.setAdapter(KursorAdapter);
    }


    public Loader<Cursor> onCreateLoader(int id, Bundle args){
        String[] projekcja={DBHelper.ID, DBHelper.BRAND, DBHelper.MODEL};
        CursorLoader loaderKursora = new CursorLoader(this,Provider.URI_CONTENT,projekcja,null,null,null);
        return loaderKursora;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor dane) {
        KursorAdapter.swapCursor(dane);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        KursorAdapter.swapCursor(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dane)
    {
        super.onActivityResult(requestCode, resultCode, dane);
        getLoaderManager().restartLoader(0, null, this);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manu_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.add){
           addNew();
        }
        return super.onOptionsItemSelected(item);
    }


    private AbsListView.MultiChoiceModeListener MultipleChoice()
    {
        return new AbsListView.MultiChoiceModeListener()
        {
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu)
            {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {}

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {}

            //dodanie menu
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu)
            {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.list_bar, menu);
                return true;
            }


            // Metoda wykonuje się gdy zostanie kliknięty jakiś przycisk w menu

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item)
            {
                if(item.getItemId() == R.id.delete) //Instrukcja wykona się gdy zostanie wciśnięty przycisk "USUŃ"
                {
                    long[] zaznaczone = Lista.getCheckedItemIds();

                    for(int i = 0; i < zaznaczone.length; i++)
                    {
                        getContentResolver().delete(ContentUris.withAppendedId(Provider.URI_CONTENT, zaznaczone[i]), null, null);
                    }

                    return true;
                }

                return false;
            }
        };
    }

}
