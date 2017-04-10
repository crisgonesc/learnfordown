package com.uvigo.learnfordown.learnfordown;


/**
 * Created by Susana on 04/04/2017.
 */


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import java.io.InputStream;


public class GifView extends View {

    private InputStream mInputStream;
    private Movie mMovie;
    private int mWidth, mHeight;
    private long mStart;
    private Context mContext;
    writegame_level1_screen writeL1 = new writegame_level1_screen();


    public GifView(Context context) {
        super(context);
        this.mContext = context;
    }

    public GifView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GifView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        if (attrs.getAttributeName(1).equals("background")) {
            int id = Integer.parseInt(attrs.getAttributeValue(1).substring(1));
            setGifImageResource(id);
        }
    }


    private void init() {
        setFocusable(true);
        mMovie = Movie.decodeStream(mInputStream);
        mWidth = mMovie.width();
        mHeight = mMovie.height();

        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        super.onDraw(canvas);

       long now = SystemClock.uptimeMillis();

        if (mStart == 0) {
            mStart = (int) now;
        }

        if (mMovie != null) {
            int relTime = (int) (now - mStart);

            mMovie.setTime(relTime < mMovie.duration() ? relTime : mMovie.duration());
            mMovie.draw(canvas, 0, 0);
            invalidate();

            if (relTime >= mMovie.duration()) {
                writeL1.getBorrar().performClick();
            }

        }
    }

    public void setGifImageResource(int id) {
        mInputStream = mContext.getResources().openRawResource(id);
        init();
    }

}

