package com.test.omdb.ui.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.test.omdb.R;

@SuppressLint("AppCompatCustomView")
public class ViewMoreTextView extends TextView implements View.OnClickListener {

    private static final int MAX_LINES = 4;
    private int currentMaxLines = Integer.MAX_VALUE;

    public ViewMoreTextView(Context context) {
        super(context);
        setOnClickListener(this);
    }
    public ViewMoreTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOnClickListener(this);
    }

    public ViewMoreTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {

        // If text longer than MAX_LINES set DrawableBottom - I'm using '...' icon
        post(() -> {
            if (getLineCount() > MAX_LINES)
                setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_more_horizontal);
            else
                setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

            setMaxLines(MAX_LINES);
        });
    }


    @Override
    public void setMaxLines(int maxLines) {
        currentMaxLines = maxLines;
        super.setMaxLines(maxLines);
    }

    public int getMyMaxLines() {
        return currentMaxLines;
    }

    @Override
    public void onClick(View v) {

        // Toggle between expanded collapsed states
        if (getMyMaxLines() == Integer.MAX_VALUE)
            setMaxLines(MAX_LINES);
        else
            setMaxLines(Integer.MAX_VALUE);
    }
}
