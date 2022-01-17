package com.test.omdb.data.search.repository;

import androidx.lifecycle.MutableLiveData;
import com.test.omdb.data.dependencies.OmdbDependencies;
import com.test.omdb.data.models.Search;
import com.test.omdb.data.network.NetworkServiceManager;
import com.test.omdb.data.network.exceptions.NetworkFailureReason;
import com.test.omdb.utils.AsynchronousCallback;
import com.test.omdb.utils.JsonUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchRepository {

    private static SearchRepository instance;
    private NetworkServiceManager networkServiceManager;

    private MutableLiveData<List<Search>> list;

    public static SearchRepository getInstance(){
        if (instance == null){
            instance = new SearchRepository();
        }
        return instance;
    }

    public SearchRepository(){
        this.list = new MutableLiveData<>();
        this.networkServiceManager = OmdbDependencies.getNetworkServiceManager();
    }

    public MutableLiveData<List<Search>> getSearchList() {
        return list;
    }

    public void searchOmdb(String query, AsynchronousCallback.WorkerThread<String, NetworkFailureReason> callback){
        try{
            JSONObject jsonObject = new JSONObject(networkServiceManager.searchOmdb(query, 1));
            JSONArray searchList = jsonObject.getJSONArray("Search");

            List<Search> searches = new ArrayList(Arrays.asList(JsonUtil.fromJson(searchList.toString(), Search[].class)));

            getSearchList().postValue(searches);

            callback.onComplete("Fetched results properly");
        } catch (JSONException | IOException e) {
            callback.onError(NetworkFailureReason.fromException(e));
        }
    }

    public void searchOmdbWithType(String query, String search_type, AsynchronousCallback.WorkerThread<String, NetworkFailureReason> callback){
        try{
            JSONObject jsonObject = new JSONObject(networkServiceManager.searchOmdbWithType(query, search_type, 1));
            JSONArray searchList = jsonObject.getJSONArray("Search");

            List<Search> searches = new ArrayList(Arrays.asList(JsonUtil.fromJson(searchList.toString(), Search[].class)));

            list.postValue(searches);

            callback.onComplete("Fetched results properly");
        } catch (JSONException | IOException e) {
            callback.onError(NetworkFailureReason.fromException(e));
            list.postValue(null);
        }
    }

}
