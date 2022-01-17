package com.test.omdb.ui.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.test.omdb.R;

public class ExpandableLayout extends RelativeLayout {

    private boolean isAnimationRunning = false;
    private boolean isOpened = false;
    private int duration;
    private int targetHeight, initialHeight;

    private FrameLayout contentLayout;
    private FrameLayout headerLayout;
    private Animation animation;
    private TypedArray typedArray;

    private View headerView, contentView;

    public ExpandableLayout(Context context)
    {
        super(context);
    }

    public ExpandableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ExpandableLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(final Context context, AttributeSet attrs) {
        View rootView = View.inflate(context, R.layout.view_expandable, this);

        headerLayout = rootView.findViewById(R.id.expandable_headerlayout_view);
        contentLayout = rootView.findViewById(R.id.expandable_contentLayout_view);

        typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableLayout);
        int headerID = typedArray.getResourceId(R.styleable.ExpandableLayout_headerLayout, -1);
        int contentID = typedArray.getResourceId(R.styleable.ExpandableLayout_contentLayout, -1);

        if (headerID == -1 || contentID == -1){
            throw new IllegalArgumentException("HeaderLayout and ContentLayout cannot be null!");
        }

        if (isInEditMode()){
            return;
        }

        duration = typedArray.getInt(R.styleable.ExpandableLayout_duration, getContext().getResources().getInteger(android.R.integer.config_shortAnimTime));

        headerView = View.inflate(context, headerID, null);

        headerView.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        headerLayout.addView(headerView);

        contentView = View.inflate(context, contentID, null);
        contentView.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        contentLayout.addView(contentView);
        contentLayout.setVisibility(GONE);
        headerLayout.setOnClickListener(v -> {
            if (!isAnimationRunning) {
                if (contentLayout.getVisibility() == VISIBLE){
                    collapse(contentLayout);
                } else {
                    expand(contentLayout);
                }

                isAnimationRunning = true;
                new Handler().postDelayed(() -> isAnimationRunning = false, duration);
            }
        });

        typedArray.recycle();
    }

    private void expand(final View v) {
        v.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        targetHeight = v.getMeasuredHeight();
        v.getLayoutParams().height = 0;
        v.setVisibility(VISIBLE);

        animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1){
                    isOpened = true;
                }
                v.getLayoutParams().height = (interpolatedTime == 1) ? LayoutParams.WRAP_CONTENT : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }


            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        animation.setDuration(duration);
        v.startAnimation(animation);
    }

    private void collapse(final View v) {
        initialHeight = v.getMeasuredHeight();
        animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                    isOpened = false;
                } else {
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        animation.setDuration(duration);
        v.startAnimation(animation);
    }

    @Override
    public void setLayoutAnimationListener(Animation.AnimationListener animationListener) {
        animation.setAnimationListener(animationListener);
    }
}
