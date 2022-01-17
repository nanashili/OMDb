package com.test.omdb.ui.components.details;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.test.omdb.R;
import com.test.omdb.data.models.Movie;

public class MovieContentDetails extends LinearLayout {

    private TextView director, writer, actors, language, awards, dvd;

    public MovieContentDetails(Context context) {
        super(context);
    }

    public MovieContentDetails(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MovieContentDetails(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        director = findViewById(R.id.director);
        writer = findViewById(R.id.writer);
        actors = findViewById(R.id.actors);
        language = findViewById(R.id.language);
        awards = findViewById(R.id.awards);
        dvd = findViewById(R.id.dvd);

    }

    public void populateData(Movie movie){
        director.setText(String.format("Directors: %s", movie.getDirector()));
        writer.setText(String.format("Writers: %s", movie.getWriter()));
        actors.setText(String.format("Actors: %s", movie.getActors()));
        awards.setText(String.format("Awards: %s", movie.getAwards()));

        if (movie.getType().equals("series")){
            language.setText(String.format("Series Language: %s", movie.getLanguage()));
            dvd.setVisibility(GONE);
        } else {
            language.setText(String.format("Movie Language: %s", movie.getLanguage()));

            if (movie.getDvd() == "N/A" || movie.getDvd() == null){
                dvd.setText(String.format("DVD Release Date: Not Available"));
                dvd.setVisibility(VISIBLE);
            } else {
                dvd.setText(String.format("DVD Release Date: %s", movie.getDvd()));
                dvd.setVisibility(VISIBLE);
            }
        }

    }

}
