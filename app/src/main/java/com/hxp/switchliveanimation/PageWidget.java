package com.hxp.switchliveanimation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;


public abstract class PageWidget extends View {
	private int nWidth = 1080;
	private int nHeight = 1920;
	public float downX = 0;
	public float downY = 0;
	PointF nTouch = new PointF();
	Scroller nScroller;
	Bitmap mLastPageBitmap, mNextPageBitmap;
	Paint mPaint;
	Context context;
	final int TOP = 1;
	final int BOTTOM = -1;
	final int NONE = 0;
	int direction = 0;
	boolean touchable;
	long time;
	int type ;

	public PageWidget(Context context) {
		super(context);
		init(context);
	}

	public abstract void onTopPaneMoved();
	public abstract void onBottomPaneMoved();
	public abstract void onAnimationEnd();

	private void init(Context context) {
		this.context = context;
		mPaint = new Paint();
		mPaint.setStyle(Paint.Style.FILL);
		touchable = true;
		nScroller = new Scroller(getContext());
		nTouch.x = 0.01f; // 不让x,y为0,否则在点计算时会有问题
		nTouch.y = 0.01f;
		setScreen(getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
	}

	public PageWidget(Context context,AttributeSet attrs) {
		super(context,attrs);
		init(context);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return doTouchEvent(event);
	}

	public void setTouchable(boolean touchable) {
		this.touchable = touchable;
	}

	public void dismissPane(){
		direction = NONE;
		postInvalidate();
	}

	// 控件的touch事件
	public boolean doTouchEvent(MotionEvent event) {
		nTouch.x = event.getX();
		nTouch.y = event.getY();
		if(!touchable || Math.abs(nTouch.x - downX) > Math.abs(nTouch.y - downY)) {
			switch (direction) {
				case TOP:
					startAnimation(2000);
					this.postInvalidate();
					break;
				case BOTTOM:
					startAnimation(2000);
					this.postInvalidate();
					break;
				case NONE:
					break;
			}
			return false;
		}
		switch (event.getAction()) {
			case MotionEvent.ACTION_MOVE:
				if(Math.abs(nTouch.y - downY) <= Math.abs(nTouch.x - downX)){
					return false;
				}
				switch (direction) {
					case NONE:
						if (nTouch.y > downY + 5) {
							direction = BOTTOM;
							onTopPaneMoved();
						}
						if (nTouch.y < downY - 5) {
							direction = TOP;
							onBottomPaneMoved();
						}
						break;
					case TOP:
					case BOTTOM:
						this.postInvalidate();
						break;
				}
				break;
			case MotionEvent.ACTION_DOWN:
				downX = event.getX();
				downY = event.getY();
				direction = NONE;
				time = System.currentTimeMillis();
			case MotionEvent.ACTION_UP:
				switch (direction) {
					case TOP:
					case BOTTOM:
						startAnimation(1000);
						this.postInvalidate();
						break;
				}
				break;
		}
		return true;
	}

	public void setBitmaps(Bitmap bm1, Bitmap bm2) {
		mLastPageBitmap = bm1;
		mNextPageBitmap = bm2;
	}

	public void setBitmapsRes(int res1, int res2) {
		mLastPageBitmap = BitmapFactory.decodeResource(getResources(),res1);
		mNextPageBitmap = BitmapFactory.decodeResource(getResources(),res2);
	}

	public void setScreen(int w, int h) {
		nWidth = w;
		nHeight = h;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		switch (direction){
			case TOP:
				Rect src = new Rect(0,0,mNextPageBitmap.getWidth(),mNextPageBitmap.getHeight());
				RectF rect = new RectF(0,nHeight+nTouch.y-downY,nWidth,nHeight+nTouch.y-downY+nHeight);
				canvas.drawBitmap(mNextPageBitmap, src, rect, null);
				break;
			case BOTTOM:
				Rect src1 = new Rect(0,0,mNextPageBitmap.getWidth(),mNextPageBitmap.getHeight());
				RectF rect1 = new RectF(0,nTouch.y-downY-nHeight,nWidth,nTouch.y-downY);
				canvas.drawBitmap(mLastPageBitmap, src1, rect1, null);
				break;
		}
	}

	public void computeScroll() {
		super.computeScroll();
		if (nScroller.computeScrollOffset()) {
			float x = nScroller.getCurrX();
			float y = nScroller.getCurrY();
			nTouch.x = x;
			nTouch.y = y;
			postInvalidate();
		}
	}

	private void startAnimation(int delayMillis) {
		float ndy = 0;
		switch (direction){
			case BOTTOM:
				if(nTouch.y - downY > nHeight / 5){
					ndy = nHeight - nTouch.y + downY;
					type = 1;
				}else{
					ndy = downY - nTouch.y-10;
					type = 2;
				}
				break;
			case TOP:
				if(downY - nTouch.y > nHeight / 5){
					ndy = downY - nTouch.y - nHeight;
					type = 3;
				}else{
					ndy = downY - nTouch.y ;
					type = 4;
				}
				break;
		}
		nScroller.startScroll((int) nTouch.x, (int) nTouch.y, (int) nTouch.x, (int)ndy, delayMillis);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				switch (type){
					case 4:
					case 2:
						break;
					case 3:
					case 1:
						onAnimationEnd();
						break;
				}
				direction = NONE;
			}
		},delayMillis+100);
	}

	public void abortAnimation() {
		if (!nScroller.isFinished()) {
			nScroller.abortAnimation();
		}
	}
}
