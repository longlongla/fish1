package com.example.object;

import java.util.ArrayList;
import java.util.List;
import com.example.factory.GameObjectFactory;
import com.example.interfaces.IMyPlane;
import com.example.mybeatplane.R;
import com.example.view.MainView;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
/*��ҷɻ�����*/
public class MyPlane extends GameObject implements IMyPlane{
	private float middle_x;			 // �ɻ�����������
	private float middle_y;
	private long startTime;	 	 	 // ��ʼ��ʱ��
	private long endTime;	 	 	 // ������ʱ��
	public Bitmap myplane;			 // �ɻ�����ʱ��ͼƬ
	private MainView mainView;
	private GameObjectFactory factory;
	public MyPlane(Resources resources) {
		super(resources);
		// TODO Auto-generated constructor stub
		initBitmap();
		this.speed = 8;
		factory = new GameObjectFactory();
	}
	public void setMainView(MainView mainView) {
		this.mainView = mainView;
	}
	// ������Ļ��Ⱥ͸߶�
	@Override
	public void setScreenWH(float screen_width, float screen_height) {
		super.setScreenWH(screen_width, screen_height);
		object_x = screen_width/2 - object_width/2;
		object_y = screen_height - object_height;
		middle_x = object_x + object_width/2;
		middle_y = object_y + object_height/2;
	}
	// ��ʼ��ͼƬ��Դ��
	@Override
	public void initBitmap() {
		// TODO Auto-generated method stub
		myplane = BitmapFactory.decodeResource(resources, R.drawable.myfish2);
		object_width = myplane.getWidth(); // ���ÿһ֡λͼ�Ŀ�
		object_height = myplane.getHeight(); 	// ���ÿһ֡λͼ�ĸ�
	}
	// ����Ļ�ͼ����
	@Override
	public void drawSelf(Canvas canvas) {
		// TODO Auto-generated method stub
		if(isAlive){
			int x = (int) (currentFrame * object_width); // ��õ�ǰ֡�����λͼ��X����
			canvas.save();
			canvas.clipRect(object_x, object_y, object_x + object_width, object_y + object_height);
			canvas.drawBitmap(myplane, object_x - x, object_y, paint);
			canvas.restore();
			/*
			currentFrame++;
			if (currentFrame >= 2) {
				currentFrame = 0;
			}
			*/
		}
	}
	// �ͷ���Դ�ķ���
	@Override
	public void release() {
		// TODO Auto-generated method stub
		if(!myplane.isRecycled()){
			myplane.recycle();
		}
	}
	//getter��setter����
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	@Override
	public float getMiddle_x() {
		return middle_x;
	}
	@Override
	public void setMiddle_x(float middle_x) {
		this.middle_x = middle_x;
		this.object_x = middle_x - object_width/2;
	}
	@Override
	public float getMiddle_y() {
		return middle_y;
	}
	@Override
	public void setMiddle_y(float middle_y) {
		this.middle_y = middle_y;
		this.object_y = middle_y - object_height/2;
	}	
}
