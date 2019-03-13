package com.example.cs441_4;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.ArrayList;

public class ZoomActivity extends Activity {
    private View mContentView;
    ArrayList<Point> points = new ArrayList<Point>();
    Paint paint = new Paint();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        setContentView(R.layout.activity_zoom);
        setContentView(new MyView(this));
        //Intent intent = getIntent();
        //String value = intent.getStringExtra("key");
        //Log.d("thisTag",value);


    }

    /*@Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        if(event.getAction() == MotionEvent.ACTION_UP) {
            System.out.println("Touch at "+x+","+y);
        }

        Point point = new Point();
        point.x = (int)event.getX();
        point.y = (int)event.getY();
        //invalidate();
        return true;
    }*/



}


























