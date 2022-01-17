package com.test.omdb.ui.search.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.test.omdb.data.models.Search;
import com.test.omdb.data.network.exceptions.NetworkFailureReason;
import com.test.omdb.data.search.repository.SearchRepository;
import com.test.omdb.utils.AsynchronousCallback;

import java.util.List;
import java.util.Objects;

public class SearchViewModel extends ViewModel{

    private SearchRepository searchRepository;

    public SearchViewModel(){
        this.searchRepository = SearchRepository.getInstance();
    }

    public LiveData<List<Search>> getSearches(){
        return searchRepository.getSearchList();
    }

    public void searchOmdb(String query, AsynchronousCallback.WorkerThread<String, NetworkFailureReason> callback){
        this.searchRepository.searchOmdb(query, callback);
    }

    public void searchOmdbWithType(String query, String search_type, AsynchronousCallback.WorkerThread<String, NetworkFailureReason> callback){
        this.searchRepository.searchOmdbWithType(query, search_type, callback);
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        @Override
        public @NonNull
        <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return Objects.requireNonNull(modelClass.cast(new SearchViewModel()));
        }
    }
}
