package com.example.object;

import java.util.Random;
import com.example.mybeatplane.R;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
/*大型敌机的类*/
public class BigPlane extends EnemyPlane{
	private static int currentCount = 0;	 //	对象当前的数量
	public static int sumCount = 2;	 	 	 //	对象总的数量
	private Bitmap bigPlane; // 对象图片
	public BigPlane(Resources resources) {
		super(resources);
		// TODO Auto-generated constructor stub
		this.score = 3000;		// 为对象设置分数
	}
	//初始化数据
	@Override
	public void initial(int arg0,float arg1,float arg2){
		isAlive = true;
		Random ran = new Random();
		speed = ran.nextInt(2) + 4 * arg0;	
		object_x = ran.nextInt((int)(screen_width - object_width));
		object_y = -object_height * (currentCount*2 + 1);
		currentCount++;
		if(currentCount >= sumCount){
			currentCount = 0;
		}
	}
	// 初始化图片资源	
	@Override
	public void initBitmap() {
		// TODO Auto-generated method stub
		Random ran = new Random();
		num = ran.nextFloat();
		bigPlane = BitmapFactory.decodeResource(resources, R.drawable.bigenemy);
		bigPlane= Bitmap.createBitmap(bigPlane,0,0
				,(int)bigPlane.getWidth()
				,(int)bigPlane.getHeight()
				,matrix,true);
		object_width = bigPlane.getWidth();			//获得每一帧位图的宽
		object_height = bigPlane.getHeight();		//获得每一帧位图的高
	}
	// 对象的绘图函数
	@Override
	public void drawSelf(Canvas canvas) {
		// TODO Auto-generated method stub
		if(isAlive){
			if(!isExplosion){
				if(isVisible){
					canvas.save();
					canvas.clipRect(object_x,object_y,object_x + object_width,object_y + object_height);
					canvas.drawBitmap(bigPlane, object_x, object_y,paint);
					canvas.restore();
				}	
				logic();
			}
		}
	}
	// 释放资源
	@Override
	public void release() {
		// TODO Auto-generated method stub
		if(!bigPlane.isRecycled()){
			bigPlane.recycle();
		}
	}
}
