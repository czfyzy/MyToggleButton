package com.example.mytogglebutton;

import com.example.mytogglebutton.utils.DensityUtils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

public class MyToggleButton extends View implements OnClickListener{

	/**
	 * 做为背景的图片
	 */
	private Bitmap backgroundBitmap;
	/**
	 * 可以滑动的图片
	 */
	private Bitmap slideBtn;
	private Paint paint;
	private int slideLeft;
	private boolean open = false;
	private boolean isDrag;
	
	public MyToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public MyToggleButton(Context context, AttributeSet set) {
		super(context, set);
		// TODO Auto-generated constructor stub
		TypedArray ta = context.obtainStyledAttributes(set, R.styleable.MyToggleButton);

		int len = ta.length();
		
		for(int i = 0; i < len; i++) {
			int id = ta.getIndex(i);
			switch (id) {
			case R.styleable.MyToggleButton_my_background:
				int backgroundId = ta.getResourceId(id, -1);
				if(backgroundId == -1) {
					throw new RuntimeException("请设置背景图片");
				}
				backgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundId);
				break;

			case R.styleable.MyToggleButton_my_slide_btn:
				int slideResId = ta.getResourceId(id, -1);
				if(slideResId == -1) {
					throw new RuntimeException("请设置按钮图片");
				}
				slideBtn = BitmapFactory.decodeResource(getResources(), slideResId);
				break;
			case R.styleable.MyToggleButton_curr_state:
				open  = ta.getBoolean(id, false);
				setSlideLeft();
				break;
			}
		}
		
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
//		backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.switch_background);
//		
//		slideBtn = BitmapFactory.decodeResource(getResources(), R.drawable.slide_button);
		
		paint = new Paint();
		
		paint.setAntiAlias(true);
		
		setOnClickListener(this);
	}

	public MyToggleButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		
		canvas.drawBitmap(backgroundBitmap, 0, 0, paint);
		canvas.drawBitmap(slideBtn, slideLeft, 0, paint);
		
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		
		/**
		 * 设置当前view的大小
		 * width  :view的宽度
		 * height :view的高度   （单位：像素）
		 */
		setMeasuredDimension(backgroundBitmap.getWidth(),backgroundBitmap.getHeight());
	}

	private void changeState() {
		setSlideLeft();
		
		reDraw();
	}

	private void setSlideLeft() {
		if(open) {
			slideLeft = backgroundBitmap.getWidth() - slideBtn.getWidth();
		} else {
			slideLeft = 0;
		}
	}
	
	private void reDraw(){
		int maxLeft =  backgroundBitmap.getWidth() - slideBtn.getWidth();
		if(slideLeft > 0) {
			
			slideLeft = slideLeft > maxLeft?maxLeft:slideLeft;
		} else {
			slideLeft = 0;
		}
		invalidate();
		
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(!isDrag) {
			open = !open;
			changeState();
		}
	}
	private int lastX;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		super.onTouchEvent(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isDrag = false;
			lastX = (int) event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			int dis = (int) event.getX() - lastX;
			if(Math.abs(dis) > 5) {
				isDrag = true;
			}
			lastX = (int) event.getX();
			slideLeft += dis;
			reDraw();
			break;
		case MotionEvent.ACTION_UP:
			if(isDrag) {
				int maxLeft = backgroundBitmap.getWidth() - slideBtn.getWidth();
				open = slideLeft > maxLeft/2;
			}
			changeState();
			break;
		}
		
		 return true;
	}
}
