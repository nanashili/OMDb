package com.test.omdb.data.detail.repository;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import com.test.omdb.data.dependencies.OmdbDependencies;
import com.test.omdb.data.models.Movie;
import com.test.omdb.data.models.Search;
import com.test.omdb.data.network.NetworkServiceManager;
import com.test.omdb.data.network.exceptions.NetworkFailureReason;
import com.test.omdb.utils.AsynchronousCallback;
import com.test.omdb.utils.JsonUtil;
import com.test.omdb.utils.concurrent.ThreadExecutors;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DetailRepository {

    private static DetailRepository instance;
    private NetworkServiceManager networkServiceManager;

    private MutableLiveData<Movie> movieData;
    private MutableLiveData<List<Search>> similarShowsData;

    public static DetailRepository getInstance(){
        if (instance == null){
            instance = new DetailRepository();
        }
        return instance;
    }

    public DetailRepository(){
        this.movieData = new MutableLiveData<>();
        this.similarShowsData = new MutableLiveData<>();
        this.networkServiceManager = OmdbDependencies.getNetworkServiceManager();
    }

    public MutableLiveData<Movie> getMovieData() {
        return movieData;
    }

    public MutableLiveData<List<Search>> getSimilarShowsData() {
        return similarShowsData;
    }

    public void getData(String value){
        ThreadExecutors.BOUNDED.execute(() -> networkServiceManager.getTitle(value, new AsynchronousCallback.WorkerThread<String, NetworkFailureReason>() {
            @Override
            public void onComplete(@Nullable String result) {
                try {
                    Movie movie = JsonUtil.fromJson(result, Movie.class);
                    movieData.postValue(movie);
                } catch (IOException e) {
                    e.printStackTrace();
                    movieData.postValue(null);
                }
            }

            @Override
            public void onError(@Nullable NetworkFailureReason error) {
                movieData.postValue(null);
            }
        }));
    }

    public void getSimilarShows(String value){
        ThreadExecutors.BOUNDED.execute(() -> networkServiceManager.getTitle(value, new AsynchronousCallback.WorkerThread<String, NetworkFailureReason>() {
            @Override
            public void onComplete(@Nullable String result) {
                try {
                    JSONObject jsonObject = new JSONObject(networkServiceManager.searchOmdb(value, 2));
                    JSONArray similar = jsonObject.getJSONArray("Search");

                    List<Search> similarList = new ArrayList(Arrays.asList(JsonUtil.fromJson(similar.toString(), Search[].class)));

                    similarShowsData.postValue(similarList);
                } catch (IOException e) {
                    e.printStackTrace();
                    similarShowsData.postValue(null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(@Nullable NetworkFailureReason error) {
                similarShowsData.postValue(null);
            }
        }));
    }
}
