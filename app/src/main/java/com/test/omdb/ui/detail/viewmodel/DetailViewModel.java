package com.test.omdb.ui.detail.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.test.omdb.data.detail.repository.DetailRepository;
import com.test.omdb.data.models.Movie;
import com.test.omdb.data.models.Search;

import java.util.List;
import java.util.Objects;

public class DetailViewModel extends ViewModel {

    private DetailRepository detailRepository;

    public DetailViewModel(){
        this.detailRepository = DetailRepository.getInstance();
    }

    public LiveData<Movie> getMovieInformation(){
        return detailRepository.getMovieData();
    }

    public void loadData(String value){
        detailRepository.getData(value);
    }

    public void loadSimilarShowsData(String value){
        detailRepository.getSimilarShows(value);
    }

    public LiveData<List<Search>> loadSimilarShows(){
        return detailRepository.getSimilarShowsData();
    }

    public void clearViewModel(){
        onCleared();
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        @Override
        public @NonNull
        <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return Objects.requireNonNull(modelClass.cast(new DetailViewModel()));
        }
    }

}
