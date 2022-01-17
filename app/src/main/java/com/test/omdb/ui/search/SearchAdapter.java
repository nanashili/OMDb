package com.test.omdb.ui.search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.test.omdb.R;
import com.test.omdb.ui.components.search.BindableSearchItem;
import com.test.omdb.data.models.Search;

import java.util.*;

public class SearchAdapter extends ListAdapter<Search, RecyclerView.ViewHolder> {

    private static final String TAG = SearchAdapter.class.getName();

    private static final int SEARCH_TYPE_SERIES = 0;
    private static final int SEARCH_TYPE_MOVIE = 1;

    private Context context;

    private List<Search> searchList;
    private boolean isFromSimilarSection;

    public SearchAdapter(@NonNull Context context, List<Search> searches, boolean isFromSimilarSection) {
        super(new DiffUtil.ItemCallback<Search>() {
            @Override
            public boolean areItemsTheSame(@NonNull Search oldItem, @NonNull Search newItem) {
                return Objects.equals(oldItem.getImdbID(), newItem.getImdbID());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Search oldItem, @NonNull Search newItem) {
                return Objects.equals(oldItem.getImdbID(), newItem.getImdbID());
            }
        });

        this.context = context;
        this.searchList = searches;
        this.isFromSimilarSection = isFromSimilarSection;
    }

    @Override
    public int getItemViewType(int position) {
        Search search = getItem(position);

        if ("series".equals(search.getType())) {
            return SEARCH_TYPE_SERIES;
        }
        return SEARCH_TYPE_MOVIE;
    }

    @Override
    public @NonNull
    RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case SEARCH_TYPE_SERIES:
                View series = LayoutInflater.from(context).inflate(getLayoutForViewType(viewType), parent, false);
                return new SearchAdapter.SeriesViewHolder(series);
            case SEARCH_TYPE_MOVIE:
                View movie = LayoutInflater.from(context).inflate(getLayoutForViewType(viewType), parent, false);
                return new SearchAdapter.MovieViewHolder(movie);
            default:
                throw new IllegalStateException("Cannot create viewholder for type: " + viewType);
        }
    }

    @Override
    @SuppressLint("RecyclerView")
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case SEARCH_TYPE_SERIES:
                SearchAdapter.SeriesViewHolder seriesViewHolder = (SearchAdapter.SeriesViewHolder) holder;
                Search series = getItem(position);
                seriesViewHolder.getBindable().bind(series, isFromSimilarSection);
                break;
            case SEARCH_TYPE_MOVIE:
                SearchAdapter.MovieViewHolder movieViewHolder = (SearchAdapter.MovieViewHolder) holder;
                Search movies = getItem(position);
                movieViewHolder.getBindable().bind(movies, isFromSimilarSection);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        if (holder instanceof SearchAdapter.SeriesViewHolder) {
            ((SearchAdapter.SeriesViewHolder) holder).getBindable().unbind();
        } else if (holder instanceof SearchAdapter.MovieViewHolder) {
            ((SearchAdapter.MovieViewHolder) holder).getBindable().unbind();
        }
    }

    public void setSearchedPosts(List<Search> searchList){
        this.searchList = searchList;
        notifyDataSetChanged();
    }

    public @Nullable
    Search getItem(int position) {
        if (position == -1) {
            return null;
        } else if (position < searchList.size()) {
            return searchList.get(position);
        } else {
            int correctedPosition = position - searchList.size();

            if (correctedPosition < getItemCount()) {
                return super.getItem(correctedPosition);
            } else {
                Log.d(TAG, "Could not access corrected position " + correctedPosition + " as it is out of bounds.");
                return null;
            }
        }
    }

    private static @LayoutRes
    int getLayoutForViewType(int viewType) {
        switch (viewType) {
            case SEARCH_TYPE_SERIES:
                return R.layout.item_search_series;
            case SEARCH_TYPE_MOVIE:
                return R.layout.item_search_movies;
            default:
                throw new IllegalArgumentException("Unknown type!");
        }
    }

    final static class SeriesViewHolder extends RecyclerView.ViewHolder {
        public SeriesViewHolder(final @NonNull View itemView) {
            super(itemView);
        }

        public BindableSearchItem getBindable() {
            return (BindableSearchItem) itemView;
        }
    }

    final static class MovieViewHolder extends RecyclerView.ViewHolder {
        public MovieViewHolder(final @NonNull View itemView) {
            super(itemView);
        }

        public BindableSearchItem getBindable() {
            return (BindableSearchItem) itemView;
        }
    }

}
