package com.test.omdb.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.test.omdb.R;
import com.test.omdb.data.dependencies.OmdbDependencies;
import com.test.omdb.data.models.Search;
import com.test.omdb.data.network.NetworkServiceManager;
import com.test.omdb.data.network.exceptions.NetworkFailureReason;
import com.test.omdb.ui.search.SearchAdapter;
import com.test.omdb.ui.search.viewmodel.SearchViewModel;
import com.test.omdb.utils.AsynchronousCallback;
import com.test.omdb.utils.OmdbPreferences;
import com.test.omdb.utils.ThreadUtil;
import com.test.omdb.utils.ViewUtil;
import com.test.omdb.utils.concurrent.ThreadExecutors;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText search_query;
    private ImageView search;
    private RecyclerView recycler_view;
    private ChipGroup filters;
    private View search_now, search_empty;
    private ProgressBar loading_data;

    private SearchViewModel searchViewModel;
    private NetworkServiceManager networkServiceManager;
    private SearchAdapter searchAdapter;

    private List<Search> searchList;

    private String searchType = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search_query = findViewById(R.id.search_query);
        search = findViewById(R.id.search);
        recycler_view = findViewById(R.id.recycler_view);
        filters = findViewById(R.id.filters);
        search_now = findViewById(R.id.search_now);
        search_empty = findViewById(R.id.search_empty);
        loading_data = findViewById(R.id.loading_data);

        networkServiceManager = OmdbDependencies.getNetworkServiceManager();

        searchList = new ArrayList<>();

        initializeViewModel();
        initializeResources();
        handleFiltering();
    }

    private void initializeResources(){

        recycler_view.setHasFixedSize(true);
        searchAdapter = new SearchAdapter(this, searchList, false);
        recycler_view.setLayoutManager(new GridLayoutManager(this, 3));
        recycler_view.setItemViewCacheSize(10);
        recycler_view.setAdapter(searchAdapter);
    }

    private void initializeViewModel(){
        searchViewModel = ViewModelProviders.of(this, new SearchViewModel.Factory()).get(SearchViewModel.class);
        search.setOnClickListener(v -> ThreadExecutors.BOUNDED.execute(() -> {
            ViewUtil.hideSoftKeyboard(this);

            ThreadUtil.runOnMain(() -> {
                loading_data.setVisibility(View.VISIBLE);
                search_now.setVisibility(View.GONE);
                search_empty.setVisibility(View.GONE);
                recycler_view.setVisibility(View.GONE);
            });

            if (searchType.equals("") || searchType.isEmpty()){
                searchViewModel.searchOmdb(search_query.getText().toString(), new AsynchronousCallback.WorkerThread<String, NetworkFailureReason>() {
                    @Override
                    public void onComplete(@Nullable String result) {
                        ThreadUtil.runOnMain(() -> {
                            recycler_view.setVisibility(View.VISIBLE);
                            searchViewModel.getSearches().observe(MainActivity.this, searches -> searchAdapter.setSearchedPosts(searches));
                            loading_data.setVisibility(View.GONE);
                        });
                    }

                    @Override
                    public void onError(@Nullable NetworkFailureReason error) {
                        ThreadUtil.runOnMain(() -> {
                            loading_data.setVisibility(View.GONE);
                            search_empty.setVisibility(View.VISIBLE);
                            recycler_view.setVisibility(View.GONE);
                        });
                    }
                });
            } else {
                searchViewModel.searchOmdbWithType(search_query.getText().toString(), searchType, new AsynchronousCallback.WorkerThread<String, NetworkFailureReason>() {
                    @Override
                    public void onComplete(@Nullable String result) {
                        ThreadUtil.runOnMain(() -> {
                            recycler_view.setVisibility(View.VISIBLE);
                            searchViewModel.getSearches().observe(MainActivity.this, searches -> searchAdapter.setSearchedPosts(searches));
                            loading_data.setVisibility(View.GONE);
                        });
                    }

                    @Override
                    public void onError(@Nullable NetworkFailureReason error) {
                        ThreadUtil.runOnMain(() -> {
                            recycler_view.setVisibility(View.GONE);
                            loading_data.setVisibility(View.GONE);
                            search_empty.setVisibility(View.VISIBLE);
                        });
                    }
                });
            }
        }));

    }

    private void handleFiltering(){
        Chip any = (Chip) filters.getChildAt(0);
        any.setOnClickListener(v -> {
            searchType = "";
        });

        Chip movies = (Chip) filters.getChildAt(1);
        movies.setOnClickListener(v -> {
            searchType = "movie";
        });

        Chip series = (Chip) filters.getChildAt(2);
        series.setOnClickListener(v -> {
            searchType = "series";
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        OmdbPreferences.saveState("MainActivity", "", "", "");
    }

    @Override
    public void onBackPressed() {
        if (!isTaskRoot()) {
            super.onBackPressed();
        } else {
            finish();
            System.exit(0);
        }
    }

}
