package com.test.omdb.ui.components.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.test.omdb.R;
import com.test.omdb.ui.detail.DetailActivity;
import com.test.omdb.data.models.Search;

public class SeriesItem extends MaterialCardView implements BindableSearchItem {

    private SeriesItem parent;
    private ImageView series_poster;
    private TextView series_title;

    private Search search;
    private boolean isFromSimilarSection;

    public SeriesItem(Context context) {
        super(context);
    }

    public SeriesItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SeriesItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        parent = findViewById(R.id.parent_view);
        series_poster = findViewById(R.id.series_poster);
        series_title = findViewById(R.id.series_title);

    }

    @Override
    public void unbind() {}

    @Override
    public void bind(@NonNull Search search, boolean isFromSimilarSection) {
        this.search = search;
        this.isFromSimilarSection = isFromSimilarSection;

        setItemData(search);
        handleActions(search);
    }

    private void setItemData(Search search){
        Glide.with(this)
                .load(search.getPoster())
                .error(R.drawable.show_fallback_image)
                .fallback(R.drawable.show_fallback_image)
                .into(series_poster);

        series_title.setText(search.getTitle());
    }

    private void handleActions(Search search){
        if (isFromSimilarSection) {
            parent.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra("search_type", search.getType());
                intent.putExtra("search_id", search.getImdbID());
                intent.putExtra("search_title", search.getTitle());
                intent.putExtra("is_from_similar", true);

                Pair<View, String> posterTrans = Pair.create(series_poster, "poster");
                Pair<View, String> titleTrans = Pair.create(series_title, "title");

                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) getContext(), posterTrans, titleTrans);

                getContext().startActivity(intent, optionsCompat.toBundle());
            });
        } else {
            parent.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra("search_type", search.getType());
                intent.putExtra("search_id", search.getImdbID());
                intent.putExtra("search_title", search.getTitle());

                Pair<View, String> posterTrans = Pair.create(series_poster, "poster");
                Pair<View, String> titleTrans = Pair.create(series_title, "title");

                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) getContext(), posterTrans, titleTrans);

                getContext().startActivity(intent, optionsCompat.toBundle());
            });
        }
    }


    @NonNull
    @Override
    public Search getSearches() {
        return search;
    }

}
