package com.test.omdb.ui.components.search;

import androidx.annotation.NonNull;
import com.test.omdb.data.models.Search;

public interface BindableSearchItem extends Unbindable {
    void bind(@NonNull Search search, boolean isFromSimilarSection);

    @NonNull
    Search getSearches();
}
