package com.baoji.jinlinggongshang.pulltorefresh;

import com.baoji.jinlinggongshang.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class GridMap extends LinearLayout {
	// 表格的行数和列数
	private int row = 3, col = 3;
	// 表格定位的左上角X和右上角Y
	private final static int STARTX = 0;
	private final static int STARTY = 0;
	// 表格的宽度
	private static float gridWidth;
	private static float gridHeight;
	
	 

	public GridMap(Context context) {
		super(context);
		if (this.row == 0 || this.col == 0) {
			assert false : "行数和列数为0，不符合";
		}
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
	}
	
	public GridMap(Context context, int row, int col) {
		super(context);
		this.row = row;
		this.col = col;
	}
	
	public GridMap(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		gridWidth = (w - 50) / (this.col * 1.0f);
		if (this.row > this.col) {
			gridHeight = (h - 100) / (this.row * 1.0f);
		} else {
			gridHeight = gridWidth;
		}
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		gridWidth = FragmentPage6.mapWith/3;
		gridHeight = FragmentPage6.mapHeight/3;
		Paint paintColor = new Paint();
		paintColor.setStyle(Style.FILL);
		paintColor.setColor(getResources().getColor(R.color.toumingse));
		canvas.drawRect(STARTX, STARTY, STARTX + gridWidth * this.col, STARTY
				+ gridHeight * this.row, paintColor);
		paintColor.setColor(getResources().getColor(R.color.toumingse));
		for (int i = 0; i < this.row; i++) {
			if ((i + 1) % 2 == 1) {
				canvas.drawRect(STARTX, i * gridHeight + STARTY, STARTX
						+ this.col * gridWidth, STARTY + (i + 1) * gridHeight,
						paintColor);
			}
		}

		// 画表格最外层边框
		Paint paintRect = new Paint();
		/*paintRect.setColor(Color.rgb(79, 129, 189));
		paintRect.setStrokeWidth(2);
		paintRect.setStyle(Style.STROKE);
		canvas.drawRect(STARTX, STARTY, STARTX + gridWidth * this.col, STARTY
				+ gridHeight * this.row, paintRect);*/
		// 画表格的行和列,先画行后画列
		paintRect.setStrokeWidth(1);
		for (int i = 0; i < this.row - 1; i++) {
			canvas.drawLine(STARTX, STARTY + (i + 1) * gridHeight, STARTX
					+ this.col * gridWidth, STARTY + (i + 1) * gridHeight,
					paintRect);
		}
		for (int j = 0; j < this.col - 1; j++) {
			canvas.drawLine(STARTX + (j + 1) * gridWidth, STARTY, STARTX
					+ (j + 1) * gridWidth, STARTY + this.row * gridHeight,
					paintRect);
		}

		if (this.row <= 50 && this.col <= 30) {
			Paint paint = new Paint();
			paint.setColor(getResources().getColor(R.color.toumingse));
			paint.setStyle(Style.STROKE);
			paint.setTextAlign(Align.CENTER);
			if (this.row > 40 || this.col > 25) {
				paint.setTextSize(7);
			} else if (this.row > 30 || this.col > 20) {
				paint.setTextSize(8);
			} else if (this.row > 20 || this.col > 15) {
				paint.setTextSize(9);
			} else if (this.row > 10 || this.col > 10) {
				paint.setTextSize(10);
			}

			FontMetrics fontMetrics = paint.getFontMetrics();
			float fontHeight = fontMetrics.bottom - fontMetrics.top;
			int text = 0;
			for (int i = 0; i < this.row; i++) {
				for (int j = 0; j < this.col; j++) {
					float mLeft = j * gridWidth + STARTX;
					float mTop = i * gridHeight + STARTY;
					float mRight = mLeft + gridWidth;
					text++;
					float textBaseY = (int) (gridHeight + fontHeight) >> 1;
					canvas.drawText(text + "", (int) (mLeft + mRight) >> 1,
							textBaseY + mTop, paint);
				}
			}
		}
	}

}
