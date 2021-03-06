package com.example.pr_idi.mydatabaseexample;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pr_idi.mydatabaseexample.Class.Film;
import com.example.pr_idi.mydatabaseexample.Class.FilmData;

/**
 * Created by SigmundFreud on 10/01/2017.
 */

public class EditFilmActivity extends AppCompatActivity
{
    FilmData filmData;

    EditText title, director, protagonist, year, country;
    TextView rate;
    RatingBar ratingBar;
    Button submit, cancel;

    String sTitle, sDirector, sProtagonist, sCountry;
    int iYear, iRate;
    long mId;

    Film oldFilm;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_film_view);

        getSupportActionBar().setTitle("Edit film");

        //Old values
        mId = getIntent().getLongExtra("FILM_ID",0);
        filmData = new FilmData(EditFilmActivity.this);
        filmData.open();
        Film aux = filmData.getFilm(mId);
        sTitle = aux.getTitle();
        sDirector = aux.getDirector();
        sProtagonist = aux.getProtagonist();
        sCountry = aux.getCountry();
        iYear = aux.getYear();
        iRate = aux.getCritics_rate();



        title = (EditText) findViewById(R.id.title_edit);
        director = (EditText) findViewById(R.id.director_edit);
        protagonist = (EditText) findViewById(R.id.protagonist_edit);
        year = (EditText) findViewById(R.id.year_edit);
        country = (EditText) findViewById(R.id.country_edit);

        rate = (TextView) findViewById(R.id.ratingText_edit);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar_edit);

        title.setText(sTitle);
        director.setText(sDirector);
        protagonist.setText(sProtagonist);
        country.setText(sCountry);
        year.setText(String.valueOf(iYear));

        rate.setText(String.valueOf(iRate));
        float fRate = (float) (iRate);
        ratingBar.setRating(fRate / 2);

        filmData = new FilmData(this);
        filmData.open();

        oldFilm = new Film();
        oldFilm.setId(mId);

        listenerForOKButton();
        listenerForCancelButton();
        listenerForRatingBar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Controlar els clicks de la action bar aqui.
        int id = item.getItemId();

        if (id == R.id.deleteAction)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditFilmActivity.this);
            builder.setTitle("Do you want to delete this film?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id)
                {

                    filmData.deleteFilm(oldFilm);
                    filmData.close();
                    Toast.makeText(EditFilmActivity.this,"Film deleted",Toast.LENGTH_SHORT).show();
                    /**
                     * TODO: Return to the MainActivity
                     */
                    Intent intent = new Intent(EditFilmActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id)
                {
                    /*
                    deleteConfirm.dismiss();
                    */
                }
            });
            builder.create().show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    public void listenerForOKButton()
    {
        submit = (Button) findViewById(R.id.submitButton_edit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean bTitle = true;
                boolean bDirector = true;
                boolean bCountry = true;
                boolean bYear = true;
                boolean bProtagonist = true;

                title = (EditText) findViewById(R.id.title_edit);
                director = (EditText) findViewById(R.id.director_edit);
                protagonist = (EditText) findViewById(R.id.protagonist_edit);
                year = (EditText) findViewById(R.id.year_edit);
                country = (EditText) findViewById(R.id.country_edit);

                rate = (TextView) findViewById(R.id.ratingText_edit);

                if (title.length() == 0) {
                    bTitle = false;
                    title.setError("Put a name");
                }
                if (director.length() == 0) {
                    bDirector = false;
                    director.setError("Put a director");
                }
                if (country.length() == 0) {
                    bCountry = false;
                    country.setError("Put a country");
                }
                if (year.length() == 0) {
                    bYear = false;
                    year.setError("Put a year");
                }
                if (protagonist.length() == 0) {
                    bProtagonist = false;
                    protagonist.setError("Put a protagonist");
                }

                if (bTitle && bDirector && bCountry && bYear && bProtagonist) {
                    Film film = new Film();
                    film.setId(mId);
                    film.setTitle(title.getText().toString().trim());
                    film.setDirector(director.getText().toString().trim());
                    film.setCountry(country.getText().toString().trim());
                    film.setYear(Integer.parseInt(year.getText().toString()));
                    film.setProtagonist(protagonist.getText().toString().trim());
                    film.setCritics_rate(Integer.parseInt(rate.getText().toString()));
                    filmData.modify(film);
                    filmData.close();

                    Toast.makeText(EditFilmActivity.this,"Film edited",Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });
    }


    public void listenerForCancelButton()
    {
        cancel = (Button) findViewById(R.id.cancelButton_edit);

        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                filmData.close();
                finish();
            }
        });
    }





    public void listenerForRatingBar()
    {
        rate = (TextView) findViewById(R.id.ratingText_edit);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar_edit);

        ratingBar.setOnRatingBarChangeListener(
                new RatingBar.OnRatingBarChangeListener()
                {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float v, boolean b)
                    {
                        rate.setText(String.valueOf(Math.round(v * 2.0)));
                    }
                }
        );
    }

}
