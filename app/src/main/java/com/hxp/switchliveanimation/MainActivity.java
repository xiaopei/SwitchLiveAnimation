package com.hxp.switchliveanimation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private PageWidget mPageWidget;
    private static final String LIVE_ID = "live_id";
    private int liveId;
    private TextView liveName;
    private boolean inited;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        liveName = (TextView)findViewById(R.id.live_name);
        initLive();
        mPageWidget = new PageWidget(MainActivity.this);
        mPageWidget.setBitmapsRes( R.mipmap.scroll_down,R.mipmap.scroll_up);
        mPageWidget.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                return mPageWidget.doTouchEvent(e);
            }
        });
        mPageWidget.setBottomPaneListener(new PageWidget.OnScrolledListener() {
            @Override
            public void onScrolled() {
                loadNextLive();
            }
        });
        mPageWidget.setAnimationEndListener(new PageWidget.OnScrolledListener() {
            @Override
            public void onScrolled() {
                changeLive();
                mPageWidget.dismissPane();
            }
        });
        mPageWidget.setTopPaneListener(new PageWidget.OnScrolledListener() {
            @Override
            public void onScrolled() {
                loadPreLive();
            }
        });

        ((RelativeLayout)findViewById(R.id.activity_main)).addView(mPageWidget);
    }

    public void loadPreLive(){
        liveId--;
        //TODO:request last live id
        inited = false;
    }

    public void loadNextLive(){
        liveId++;
        //TODO:request next live id
        inited = false;
    }

    private void initLive() {
        liveName.setText("直播"+(liveId%10+1));
        //// TODO: init the live info
        inited = true;
    }

    private void changeLive() {
        if(!inited) {
            initLive();
        }
    }

}
