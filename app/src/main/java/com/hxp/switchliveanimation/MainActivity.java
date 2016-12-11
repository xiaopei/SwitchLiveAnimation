package com.hxp.switchliveanimation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private PageWidget mPageWidget;
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
        mPageWidget = new PageWidget(MainActivity.this){

            @Override
            public void onTopPaneMoved() {
                loadPreLive();
            }

            @Override
            public void onBottomPaneMoved() {
                loadNextLive();
            }

            @Override
            public void onAnimationEnd() {
                changeLive();
                mPageWidget.dismissPane();
            }
        };
        mPageWidget.setBitmapsRes( R.mipmap.scroll_down,R.mipmap.scroll_up);
        ((RelativeLayout)findViewById(R.id.activity_main)).addView(mPageWidget);
    }

    public void loadPreLive(){
        liveId--;
        Log.e("live","loadPreLive"+liveId);
        //TODO:request last live id
        inited = false;
    }

    public void loadNextLive(){
        liveId++;
        Log.e("live","loadNextLive"+liveId);
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
