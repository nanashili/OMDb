package com.test.omdb.ui.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.button.MaterialButton;
import com.test.omdb.ui.MainActivity;
import com.test.omdb.R;
import com.test.omdb.ui.components.ViewMoreTextView;
import com.test.omdb.ui.components.details.MovieContentDetails;
import com.test.omdb.ui.detail.viewmodel.DetailViewModel;
import com.test.omdb.data.models.Movie;
import com.test.omdb.data.models.Search;
import com.test.omdb.ui.search.SearchAdapter;
import com.test.omdb.utils.OmdbPreferences;
import jp.wasabeef.glide.transformations.BlurTransformation;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getName();

    private ImageView close, movie_poster, background_image, share;
    private MaterialButton website;
    private ViewMoreTextView movie_plot;
    private MovieContentDetails movieContentDetails;
    private RecyclerView similar_shows;
    private TextView seasons, movie_title, rating, genre, release, view_more_text;

    private DetailViewModel detailViewModel;
    private SearchAdapter searchAdapter;

    private String search_type, search_id, search_title;
    private boolean isFromSimilar;
    private List<Search> similarShowList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        close = findViewById(R.id.close);
        movie_poster = findViewById(R.id.movie_poster);
        website = findViewById(R.id.website);
        movie_plot = findViewById(R.id.movie_plot);
        similar_shows = findViewById(R.id.similar_shows);
        background_image = findViewById(R.id.background_image);
        seasons = findViewById(R.id.seasons);
        movieContentDetails = findViewById(R.id.movieContentDetails);
        movie_title = findViewById(R.id.movie_title);
        rating = findViewById(R.id.rating);
        genre = findViewById(R.id.genre);
        release = findViewById(R.id.release);
        view_more_text = findViewById(R.id.view_more_text);
        share = findViewById(R.id.share);

        search_type = getIntent().getExtras().getString("search_type");
        search_id = getIntent().getExtras().getString("search_id");
        search_title = getIntent().getExtras().getString("search_title");
        isFromSimilar = getIntent().getExtras().getBoolean("is_from_similar");

        similarShowList = new ArrayList<>();

        initializeViewModel();
        initializeResources();
    }

    private void initializeResources(){

        /**
         * We check if there is previous activity before closing and if there is not we make
         * an Intent call to the MainActivity.
         */
        close.setOnClickListener(v -> {
            if (isFromSimilar || isTaskRoot()) {
                OmdbPreferences.saveState(MainActivity.class.getName(), "", "", "");
                detailViewModel.clearViewModel();

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else if (!isTaskRoot()){
                OmdbPreferences.saveState(MainActivity.class.getName(), "", "", "");
                super.onBackPressed();
            }
        });

        if (search_type.equals("series")){
            view_more_text.setText("View more about this show");
        } else {
            view_more_text.setText("View more about this movie");
        }

        similar_shows.setHasFixedSize(true);
        searchAdapter = new SearchAdapter(this, similarShowList, true);
        similar_shows.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        similar_shows.setItemViewCacheSize(10);
        similar_shows.setAdapter(searchAdapter);

    }

    private void initializeViewModel(){
        detailViewModel = ViewModelProviders.of(this, new DetailViewModel.Factory()).get(DetailViewModel.class);

        detailViewModel.loadData(search_id);
        detailViewModel.loadSimilarShowsData(search_title);
        detailViewModel.getMovieInformation().observe(this, movie -> populateViews(movie));
        detailViewModel.loadSimilarShows().observe(this, searches -> searchAdapter.setSearchedPosts(searches));
    }

    private void populateViews(Movie movie){
        Glide.with(getApplicationContext())
                .load(movie.getPoster())
                .error(R.drawable.show_fallback_image)
                .fallback(R.drawable.show_fallback_image)
                .into(movie_poster);

        Glide.with(this)
                .load(movie.getPoster())
                .error(R.drawable.show_fallback_image)
                .fallback(R.drawable.show_fallback_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(45, 5)))
                .into(background_image);

        movie_title.setText(movie.getTitle());
        movie_plot.setText(movie.getPlot());
        rating.setText("Rated " + movie.getRated());
        genre.setText(movie.getGenre());
        release.setText(movie.getReleased());

        if (movie.getTotalSeasons() == 1){
            seasons.setText(String.format("%d Season", movie.getTotalSeasons()));
        } else if (movie.getTotalSeasons() > 1){
            seasons.setText(String.format("%d Seasons", movie.getTotalSeasons()));
        } else {
            seasons.setText(movie.getRuntime());
        }

        if (movie.getWebsite() == null || movie.getWebsite().equals("N/A")){
            website.setVisibility(View.GONE);
        } else {
            website.setVisibility(View.VISIBLE);
        }

        movieContentDetails.populateData(movie);

        handleActions(movie);
    }

    private void handleActions(Movie movie){

        share.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, "Let's watch this show together some time:  https://www.imdb.com/title/" + search_id);
            intent.setType("text/plain");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(Intent.createChooser(intent, "Invite Friends"));
            } else {
                Toast.makeText(DetailActivity.this, "No apps to share to", Toast.LENGTH_SHORT).show();
            }
        });

        website.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(movie.getWebsite()));
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        OmdbPreferences.saveState("DetailActivity", search_type, search_id, search_title);
    }

    @Override
    protected void onResume() {
        super.onResume();
        OmdbPreferences.saveState("DetailActivity", search_type, search_id, search_title);
    }
}
