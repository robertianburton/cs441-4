package com.example.cs441_4;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * TODO: document your custom view class.
 */
public class MyView extends View implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    private String mExampleString = "lol"; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;
    Paint graphPaint = new Paint();
    Bitmap frame = Bitmap.createBitmap(1080,1920,Bitmap.Config.ARGB_8888);

    DisplayMetrics displayMetrics;
    int ImageHeight;
    int ImageWidth;
    int[][] results;
    int MaxIterations = 500;
    int[] colors;
    double ZoomLevel = 1;
    double OffsetX = 0.0;
    double OffsetY = 0.0;
    String DEBUG_TAG = "whatever";
    int ButtonSize = 200;



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
        graphPaint.setColor(Color.argb(100,255,255,255));
        for(int i = 0; i < ImageHeight; i++) {
            for(int j = 0; j < ImageWidth; j++) {
                results[i][j] = 0;
            }
        }

        displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        ImageHeight = displayMetrics.heightPixels;
        ImageWidth = displayMetrics.widthPixels;
        results = new int[ImageWidth][ImageHeight];
        ButtonSize = ImageHeight/5;

        colors = new int[MaxIterations];
        float[] hsv = new float[]{0f,1.0f,1.0f};

        for(int i = 0; i < MaxIterations; i++) {
            hsv[0] = ((float)Math.cbrt(((float)i) / (float)MaxIterations) * 3600.0f)%360 ;

            colors[i] = Color.HSVToColor(hsv);
        }

        createBitmap();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(frame, 0, 0, null);
        canvas.drawRect(0,ImageHeight-ButtonSize, ButtonSize, ImageHeight, graphPaint);
        canvas.drawRect(ImageWidth-ButtonSize,ImageHeight-ButtonSize, ImageWidth, ImageHeight, graphPaint);

        Paint textPaint = new Paint();
        textPaint.setColor(Color.argb(170,0,0,0));
        textPaint.setTextSize(ButtonSize);
        canvas.drawText("+", ImageWidth-(int)(ButtonSize*0.75), ImageHeight-(int)(ButtonSize*0.18), textPaint);
        canvas.drawText("-", (int)(ButtonSize*0.35), ImageHeight-(int)(ButtonSize*0.18), textPaint);

        Log.d("Test","More Test");
    }



    @Override
    public boolean onDown(MotionEvent event) {
        Log.d(DEBUG_TAG,"onDown: " + event.toString());
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        Log.d(DEBUG_TAG, "onFling: " + event1.toString() + event2.toString());
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        Log.d(DEBUG_TAG, "onLongPress: " + event.toString());
    }

    @Override
    public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX,
                            float distanceY) {
        Log.d(DEBUG_TAG, "onScroll: " + event1.toString() + event2.toString());
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
        Log.d(DEBUG_TAG, "onShowPress: " + event.toString());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        Log.d(DEBUG_TAG, "onSingleTapUp: " + event.toString());
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        Log.d(DEBUG_TAG, "onDoubleTap: " + event.toString());
        System.out.println("Double tappp");
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        Log.d(DEBUG_TAG, "onDoubleTapEvent: " + event.toString());
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        Log.d(DEBUG_TAG, "onSingleTapConfirmed: " + event.toString());
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int si = 6;
        int px = (int) event.getX();
        int py = (int) event.getY();
        int e = event.getAction();
        if(event.getAction() == MotionEvent.ACTION_UP) {
            if(px<ButtonSize && py>ImageHeight-ButtonSize) {
                System.out.println("ZOOM OUT");
                ZoomLevel--;
            } else if (px > ImageWidth-ButtonSize && py > ImageHeight-ButtonSize) {
                System.out.println("ZOOM IN");
                ZoomLevel++;
            } else {
                System.out.println("Touch at " + px + "," + py);
                OffsetX += 8 / Math.pow(2, ZoomLevel) * ((px - ImageWidth / 2.0) / ImageWidth);
                OffsetY += 8 / Math.pow(2, ZoomLevel) * ((py - ImageHeight / 2.0) / ImageHeight);
            }
        } else {
            return true;
        }

        createBitmap();

        System.out.println("Calculations Ending");

        invalidate();
        return true;
    }


    public boolean createBitmap() {
        double ScaleFactor = 2.0/Math.pow(2,ZoomLevel-1);
        double MinRe = (-16.0/9.0)*ScaleFactor + (16.0/9.0)*OffsetX;
        double MaxRe = (16.0/9.0)*ScaleFactor + (16.0/9.0)*OffsetX;
        double MinIm = (-1.0)*ScaleFactor + OffsetY;
        double MaxIm = (1.0)*ScaleFactor + OffsetY;
        //double MaxIm = MinIm+(MaxRe-MinRe)*ImageHeight/ImageWidth;
        double Re_factor = (MaxRe-MinRe)/(ImageWidth-1);
        double Im_factor = (MaxIm-MinIm)/(ImageHeight-1);

        int[] pixels = new int[ImageWidth*ImageHeight];


        System.out.println("Calculations Starting");
        frame = Bitmap.createBitmap(ImageWidth,ImageHeight,Bitmap.Config.ARGB_8888);

        double c_re, c_im, Z_re, Z_im, Z_re2, Z_im2;
        boolean isInside;
        int myblack = 0xFF000000;
        int x, y, n;
        for(x=0; x<ImageWidth; ++x)
        {
            c_re = MaxRe - (ImageWidth-x)*Re_factor;
            //double c_im = MaxIm - x*Im_factor;
            for(y=0; y<ImageHeight; ++y)
            {
                //double c_re = MinRe + y*Re_factor;
                c_im = MinIm + y*Im_factor;

                Z_re = c_re;
                Z_im = c_im;
                isInside = true;
                for(n=0; n<MaxIterations; ++n)
                {
                    Z_re2 = Z_re*Z_re;
                    Z_im2 = Z_im*Z_im;
                    if(Z_re2 + Z_im2 > 4)
                    {
                        results[x][y] = n;
                        isInside = false;
                        break;
                    }
                    Z_im = 2*Z_re*Z_im + c_im;
                    Z_re = Z_re2 - Z_im2 + c_re;
                }
                if(isInside) {
                    /*results[x][y]=MaxIterations-1;
                    frame.setPixel(x,y, Color.BLACK);*/
                    pixels[x + ImageWidth*y] = myblack;
                } else {
                    /*frame.setPixel(x,y, colors.get(results[x][y]));*/
                    pixels[x + ImageWidth*y] = colors[results[x][y]];
                }
            }
        }
        frame.setPixels(pixels, 0, ImageWidth, 0, 0, ImageWidth, ImageHeight);
        return true;
    }
}
