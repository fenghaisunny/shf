package com.baoji.jiuguijiu.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.baoji.jiuguijiu.R;

public class PathMenuViewTwo extends FrameLayout {
	/**
	 * 中间的圆形菜单.
	 */
	private ImageView mHome;
	/**
	 * 上下文.
	 */
	private Context mContext;

	/**
	 * 图标列表
	 */
	private Bitmap[] icons = new Bitmap[PONIT_NUM];
	/**
	 * point列表
	 */
	private Point[] points;

	/**
	 * 数目
	 */
	private static final int PONIT_NUM = 4;

	/**
	 * 圆心坐标
	 */
	private int mPointX = 0, mPointY = 0;

	/**
	 * 半径
	 */
	private int mRadius;

	/**
	 * 每两个点间隔的角度
	 */
	private int mDegreeDelta;

	/**
	 * 屏幕宽高
	 */
	private int screenW, screenH;
	/**
	 * 每次转动的角度差
	 */
	private int tempDegree = 0;
	/**
	 * 选中的图标标识 999：未选中任何图标
	 */
	private int chooseBtn = 999;

	private boolean hasMeasured = false;

	private OnTouchStatusListener onTurnplateListener;

	public PathMenuViewTwo(Context context) {
		super(context);
		setupViews();
	}

	public PathMenuViewTwo(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupViews();
	}

	/**
	 * 方法名：setupViews 功能 : 设置view
	 */
	private void setupViews() {
		mContext = getContext();
		// 得到屏幕宽高
		getDensity();
		mHome = new ImageView(mContext);
		// 中心的图标
		mHome.setImageResource(R.drawable.touming_640);
		addView(mHome);

		mHome.getViewTreeObserver().addOnPreDrawListener(
				new ViewTreeObserver.OnPreDrawListener() {
					// 在视图被绘制前调用
					public boolean onPreDraw() {
						if (hasMeasured == false) {// 保证只调用一次
							hasMeasured = true;
							mPointX = screenW / 2;
							
							Bitmap bm = BitmapFactory.decodeResource(
									getResources(), R.drawable.bg_header);
							// 减去标题栏高度  此处是为了适应不同屏幕
							mPointY = (mHome.getDrawable().getMinimumHeight())
									/ 2 - bm.getHeight() / 3 + 20 * screenH
									/ 1280;
							loadIcons();
							initPoints();
							computeCoordinates();
						}
						return true;
					}
				});
	}

	private void getDensity() {
		DisplayMetrics dm = new DisplayMetrics();
		dm = mContext.getApplicationContext().getResources()
				.getDisplayMetrics();
		this.screenH = dm.heightPixels;
		this.screenW = dm.widthPixels;
		this.mRadius = dm.widthPixels / 3 - 10;
	}

	/**
	 * 
	 * 方法名：loadIcons 功能：获取所有图片
	 */
	public void loadIcons() {
		for (int i = 0; i < PONIT_NUM; i++) {
			//首先进行缩放
			Bitmap bm=BitmapFactory.decodeResource(
					getResources(),
					getResources().getIdentifier("bg" + (i + 1),
							"drawable", mContext.getPackageName()));
			int width=bm.getWidth();
			int height=bm.getHeight();
			float scaleHeight=(float)height/(102*1.5f);
			float scaleWidth=(float)width/(96*1.5f);
			Matrix matrix=new Matrix();
			matrix.postScale(scaleWidth,scaleHeight);
			icons[i] =Bitmap.createBitmap(bm,0,0,width,height,matrix,true);
		}
	}

	/**
	 * 
	 * 方法名：initPoints 功能：初始化每个点 参数：
	 */

	private void initPoints() {
		points = new Point[PONIT_NUM];
		Point point;
		int angle = 0;
		mDegreeDelta = 360 / PONIT_NUM;
		for (int index = 0; index < PONIT_NUM; index++) {
			point = new Point();
			point.angle = angle;
			angle += mDegreeDelta;
			point.bitmap = icons[index];
			point.flag = index;
			points[index] = point;
		}
	}

	private int degree = 0;

	/**
	 * 
	 * 方法名：resetPointAngle 功能：重新计算每个点的角度 参数：
	 * 
	 * @param x
	 * @param y
	 * 
	 */
	private void resetPointAngle(float x, float y) {
		degree = computeMigrationAngle(x, y);
		for (int index = 0; index < PONIT_NUM; index++) {
			points[index].angle += degree;
			if (points[index].angle > 360) {
				points[index].angle -= 360;
			} else if (points[index].angle < 0) {
				points[index].angle += 360;
			}
		}
	}
	/**
	 * 
	 * 方法名：computeCoordinates 功能：计算每个点的坐标 参数
	 */
	private void computeCoordinates() {
		Point point;
		for (int index = 0; index < PONIT_NUM; index++) {
			point = points[index];
			point.x = mPointX
					+ (float) (mRadius * Math.cos(point.angle * Math.PI / 180));
			point.y = mPointY
					+ (float) (mRadius * Math.sin(point.angle * Math.PI / 180));
		}
	}

	/**
	 * 
	 * 方法名：computeMigrationAngle 功能：计算偏移角度 参数：
	 * 
	 * @param x
	 * @param y
	 * 
	 */
	private int computeMigrationAngle(float x, float y) {
		int a = 0;
		float distance = (float) Math
				.sqrt(((x - mPointX) * (x - mPointX) + (y - mPointY)
						* (y - mPointY)));
		int degree = (int) (Math.acos((x - mPointX) / distance) * 180 / Math.PI);
		if (y < mPointY) {
			degree = -degree;
		}
		if (tempDegree != 0) {
			a = degree - tempDegree;
		}
		tempDegree = degree;
		return a;
	}
	
	/**
	 * 
	 * 方法名：computeCurrentDistance 功能：计算触摸的位置与各个元点的距离 参数：
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private void computeCurrentDistance(float x, float y) {
		for (Point point : points) {
			float distance = (float) Math
					.sqrt(((x - point.x) * (x - point.x) + (y - point.y)
							* (y - point.y)));
			if (distance < 31) {
				chooseBtn = point.flag;
				break;
			} else {
				chooseBtn = 999;
			}
		}
	}

	private void switchScreen(MotionEvent event) {
		computeCurrentDistance(event.getX(), event.getY());
		if (chooseBtn != 999) {
			onTurnplateListener.onStatuTouch(chooseBtn);
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_MOVE:
			resetPointAngle(event.getX(), event.getY());
			computeCoordinates();
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			switchScreen(event);
			tempDegree = 0;
			invalidate();
			break;
		}
		return true;
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (int index = 0; index < PONIT_NUM; index++) {
			drawInCenter(canvas, points[index].bitmap, points[index].x,
					points[index].y);
		}
	}
	/**
	 * 
	 * 方法名：drawInCenter 功能：把点放到图片中心处 参数：
	 * 
	 * @param canvas
	 * @param bitmap
	 * @param left
	 * @param top
	 * 
	 */
	void drawInCenter(Canvas canvas, Bitmap bitmap, float left, float top) {
		canvas.drawBitmap(bitmap, left - bitmap.getWidth() / 2,
				top - bitmap.getHeight() / 2, null);
	}

	class Point {

		/**
		 * 位置标识
		 */
		int flag;
		/**
		 * 图片
		 */
		Bitmap bitmap;
		/**
		 * /** 角度
		 */
		int angle;
		/**
		 * x坐标
		 */
		float x;

		/**
		 * y坐标
		 */
		float y;

	}

	public static interface OnTouchStatusListener {

		public void onStatuTouch(int statu);
	}

	public void setOnTurnplateListener(OnTouchStatusListener onTurnplateListener) {
		this.onTurnplateListener = onTurnplateListener;
	}
}
