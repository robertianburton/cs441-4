package com.example.cs441_4;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class MyView extends View {
    private String mExampleString = "lol"; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;
    Paint graphPaint = new Paint();


    DisplayMetrics displayMetrics;
    int ImageHeight;
    int ImageWidth;
    float[][] results;


    public MyView(Context context) {
        super(context);
        init(null, 0);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        graphPaint.setAntiAlias(true);
        graphPaint.setStrokeWidth(1.0f);
        for(int i = 0; i < ImageHeight; i++) {
            for(int j = 0; j < ImageWidth; j++) {
                results[i][j] = 0.4f;
            }
        }

        displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        ImageHeight = displayMetrics.heightPixels;
        ImageWidth = displayMetrics.widthPixels;
        results = new float[ImageHeight][ImageWidth];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.argb(255,0,255,0));
        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        for(int i = 0; i < ImageHeight; i++) {
            for(int j = 0; j < ImageWidth; j++) {
                //float[] hsv = new float[]{(float)i/1080.0f*360,(float)j/1920.0f,1.0f};
                float[] hsv = new float[]{results[i][j]*330,1.0f,1.0f};
                /*if(results[i][j]==1f) {
                    hsv = new float[]{results[i][j]*330,1.0f,1f};
                }*/
                graphPaint.setColor(Color.HSVToColor(hsv));
                canvas.drawPoint(i,j,graphPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int px = (int) event.getX();
        int py = (int) event.getY();

        if(event.getAction() == MotionEvent.ACTION_UP) {
            System.out.println("Touch at "+px+","+py);
        }

        Point point = new Point();
        point.x = (int)event.getX();
        point.y = (int)event.getY();

        double MinRe = -2.0;
        double MaxRe = 1.0;
        double MinIm = -1.2;
        double MaxIm = MinIm+(MaxRe-MinRe)*ImageHeight/ImageWidth;
        double Re_factor = (MaxRe-MinRe)/(ImageWidth-1);
        double Im_factor = (MaxIm-MinIm)/(ImageHeight-1);
        int MaxIterations = 200;

        System.out.println("Calculations Starting");
        for(int y=0; y<ImageWidth; ++y)
        {
            double c_im = MaxIm - y*Im_factor;
            for(int x=0; x<ImageHeight; ++x)
            {
                double c_re = MinRe + x*Re_factor;

                double Z_re = c_re, Z_im = c_im;
                boolean isInside = true;
                for(int n=0; n<MaxIterations; ++n)
                {
                    double Z_re2 = Z_re*Z_re, Z_im2 = Z_im*Z_im;
                    if(Z_re2 + Z_im2 > 4)
                    {
                        results[x][y] = (float)Math.sqrt((float)n/(float)MaxIterations);
                        isInside = false;
                        break;
                    }
                    Z_im = 2*Z_re*Z_im + c_im;
                    Z_re = Z_re2 - Z_im2 + c_re;
                }
                if(isInside) {
                    results[x][y]=1f;
                }
            }
        }
        System.out.println("Calculations Ending");
        invalidate();
        return true;
    }


}
