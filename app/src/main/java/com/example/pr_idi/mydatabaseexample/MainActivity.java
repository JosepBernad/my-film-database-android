package com.example.pr_idi.mydatabaseexample;


import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pr_idi.mydatabaseexample.Adapters.FilmsAdapter;
import com.example.pr_idi.mydatabaseexample.Class.Film;
import com.example.pr_idi.mydatabaseexample.Class.FilmData;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.example.pr_idi.mydatabaseexample.R.layout.main;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public ListView mainListView;

    private FilmData filmData;
    private ArrayList<Film> filmArray;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;

    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;

    private String sortBy = "director";

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(main);
        mainListView = (ListView) findViewById(R.id.my_list);
        getSupportActionBar().setTitle("My Films");

        /** (+) Add button */
        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.addButton);
        addButton.setOnClickListener(this);


        filmData = new FilmData(this);
        filmData.open();

        filmArray = new ArrayList<>(filmData.getAllFilms());
        sortArrayList();


        /**
         // use the SimpleCursorAdapter to show the
         // elements in a ListView
         ArrayAdapter<Film> adapter = new ArrayAdapter<>(this,
         android.R.layout.simple_list_item_1, values);
         setListAdapter(adapter); */


        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new FilmsAdapter(filmArray);
        recyclerView.setAdapter(adapter);


        ////// Navigation Drawer/////////

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        mDrawerList = (ListView) findViewById(R.id.navList);
        addDrawerItems();

        setupDrawer();

        /////End Navigaion Drawer//////

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    // Will be called via the onClick attribute
    // of the buttons in main.xml

    public void onClick(View view) {
        @SuppressWarnings("unchecked")
        ArrayAdapter<Film> adapter = (ArrayAdapter<Film>) mainListView.getAdapter();
        Film film;
        switch (view.getId())
        {
            case R.id.addButton:
                filmData.close();
                Intent i = new Intent(MainActivity.this, NewFilmActivity.class);
                startActivity(i);
                break;

            case R.id.sortButton:
                sortBy = "year";
                sortArrayList();

                recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
                layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(layoutManager);

                //adapter = new FilmsAdapter(filmArray);
                //recyclerView.setAdapter(adapter);

                break;

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        filmData.open();
        filmArray = new ArrayList<>(filmData.getAllFilms());
        sortArrayList();
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        //adapter = new FilmsAdapter(filmArray);
        //recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        filmData.close();
        super.onPause();
    }


    private void addDrawerItems() {
        String[] filmArray = {"Add Film", "Delete Film", "Help", "About"};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, filmArray);
        mDrawerList.setAdapter(mAdapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: //cas Add Film
                        filmData.close();
                        Intent i = new Intent(MainActivity.this, NewFilmActivity.class);
                        startActivity(i);
                        break;
                    case 1: //cas Delete Film
                        Toast.makeText(MainActivity.this, "No se borrar pelis OK?", Toast.LENGTH_SHORT).show();
                        break;
                    case 2: //cas Help
                        Toast.makeText(MainActivity.this, "Help encara s'ha de implementar XD", Toast.LENGTH_SHORT).show();
                        break;
                    case 3: //cas About
                        Toast.makeText(MainActivity.this, "About encara s'ha de implementar XD", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Es crida quan el drawer esta completament obert */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Options");
                invalidateOptionsMenu();
            }

            /** Es crida quan el drawer esta completament tancat */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle("My Films");
                invalidateOptionsMenu();
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Controlar els clicks de la action bar aqui.
        int id = item.getItemId();

        /*
        if (id == R.id.action_settings) {
            return true;
        }
        */
        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }



    void sortArrayList()
    {
        Collections.sort(filmArray, new Comparator<Film>(){
            public int compare(Film film1, Film film2)
            {
                // ## Ascending order
                switch (sortBy)
                {
                    case "title":
                        return film1.getTitle().compareToIgnoreCase(film2.getTitle());
                    case "director":
                        return film1.getDirector().compareToIgnoreCase(film2.getDirector());
                    case "year":
                        return Integer.valueOf(film1.getYear()).compareTo(film2.getYear());
                    default:
                        return film1.getTitle().compareToIgnoreCase(film2.getTitle());

                    /*
                    ## Ascending order
                    return film1.getFirstName().compareToIgnoreCase(film2.getFirstName()); // To compare string values
                    return Integer.valueOf(film1.getId()).compareTo(film2.getId()); // To compare integer values

                    ## Descending order
                    return film2.getFirstName().compareToIgnoreCase(film1.getFirstName()); // To compare string values
                    return Integer.valueOf(film2.getId()).compareTo(film1.getId()); // To compare integer values
                     */
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }
}