package com.example.view;

import java.util.ArrayList;
import java.util.List;

import com.example.constant.ConstantUtil;
import com.example.factory.GameObjectFactory;
import com.example.mybeatplane.R;
import com.example.object.BigPlane;
import com.example.object.EnemyPlane;
import com.example.object.GameObject;
import com.example.object.MiddlePlane;
import com.example.object.MyPlane;
import com.example.object.SmallPlane;
import com.example.sounds.GameSoundPool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
/*��Ϸ���е�������*/
public class MainView extends BaseView{
	private int middlePlaneScore;	// ���͵л��Ļ���
	private int bigPlaneScore;		// ���͵л��Ļ���
	private int speedTime;			// ��Ϸ�ٶȵı���
	private int sum = 0;
	private float bg_y;				// ͼƬ������
	private float bg_y2;
	private float play_bt_w;
	private float play_bt_h;	 
	private float missile_bt_y;		 	
	private boolean isPlay;			// �����Ϸ����״̬
	private boolean isTouchPlane;	// �ж�����Ƿ�����Ļ
	private Bitmap background; 		// ����ͼƬ
	private Bitmap background2; 	// ����ͼƬ
	private Bitmap playButton; 		// ��ʼ/��ͣ��Ϸ�İ�ťͼƬ
	private MyPlane myPlane;		// ��ҵķɻ�
	private List<EnemyPlane> enemyPlanes;
	private GameObjectFactory factory;
	
	private Matrix matrix = new Matrix();
	float scale = 1.1f;
	
	public MainView(Context context,GameSoundPool sounds) {
		super(context,sounds);
		// TODO Auto-generated constructor stub
		isPlay = true;
		speedTime = 1;
		factory = new GameObjectFactory();						  //������
		enemyPlanes = new ArrayList<EnemyPlane>();
		myPlane = (MyPlane) factory.createMyPlane(getResources());//������ҵķɻ�
		myPlane.setMainView(this);
		for(int i = 0;i < SmallPlane.sumCount;i++){
			//����С�͵л�
			SmallPlane smallPlane = (SmallPlane) factory.createSmallPlane(getResources());
			enemyPlanes.add(smallPlane);
		}
		for(int i = 0;i < MiddlePlane.sumCount;i++){
			//�������͵л�
			MiddlePlane middlePlane = (MiddlePlane) factory.createMiddlePlane(getResources());
			enemyPlanes.add(middlePlane);
		}
		for(int i = 0;i < BigPlane.sumCount;i++){
			//�������͵л�
			BigPlane bigPlane = (BigPlane) factory.createBigPlane(getResources());
			enemyPlanes.add(bigPlane);
		}
		thread = new Thread(this);	
	}
	// ��ͼ�ı�ķ���
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		super.surfaceChanged(arg0, arg1, arg2, arg3);
	}
	// ��ͼ�����ķ���
	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		super.surfaceCreated(arg0);
		initBitmap(); // ��ʼ��ͼƬ��Դ
		for(GameObject obj:enemyPlanes){			
			obj.setScreenWH(screen_width,screen_height);
		}
		myPlane.setScreenWH(screen_width,screen_height);
		myPlane.setAlive(true);
		if(thread.isAlive()){
			thread.start();
		}
		else{
			thread = new Thread(this);
			thread.start();
		}
	}
	// ��ͼ���ٵķ���
	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		super.surfaceDestroyed(arg0);
		release();
	}
	// ��Ӧ�����¼��ķ���
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_UP){
			isTouchPlane = false;
			return true;
		}
		else if(event.getAction() == MotionEvent.ACTION_DOWN){
			float x = event.getX();
			float y = event.getY();
			if(x > 10 && x < 10 + play_bt_w && y > 10 && y < 10 + play_bt_h){
				if(isPlay){
					isPlay = false;
				}		
				else{
					isPlay = true;	
					synchronized(thread){
						thread.notify();
					}
				}
				return true;
			}
			//�ж���ҷɻ��Ƿ񱻰���
			else if(x > myPlane.getObject_x() && x < myPlane.getObject_x() + myPlane.getObject_width() 
					&& y > myPlane.getObject_y() && y < myPlane.getObject_y() + myPlane.getObject_height()){
				if(isPlay){
					isTouchPlane = true;
				}
				return true;
			}
		}
		//��Ӧ��ָ����Ļ�ƶ����¼�
		else if(event.getAction() == MotionEvent.ACTION_MOVE && event.getPointerCount() == 1){
			//�жϴ������Ƿ�Ϊ��ҵķɻ�
			if(isTouchPlane){
				float x = event.getX();
				float y = event.getY();
				if(x > myPlane.getMiddle_x() + 20){
					if(myPlane.getMiddle_x() + myPlane.getSpeed() <= screen_width){
						myPlane.setMiddle_x(myPlane.getMiddle_x() + myPlane.getSpeed());
					}					
				}
				else if(x < myPlane.getMiddle_x() - 20){
					if(myPlane.getMiddle_x() - myPlane.getSpeed() >= 0){
						matrix.postScale(1,-1);
						myPlane.myplane = Bitmap.createBitmap(myPlane.myplane,0,0
								,(int)myPlane.getObject_width()
								,(int)myPlane.getObject_height()
								,matrix,true);
						myPlane.setMiddle_x(myPlane.getMiddle_x() - myPlane.getSpeed());
					}				
				}
				if(y > myPlane.getMiddle_y() + 20){
					if(myPlane.getMiddle_y() + myPlane.getSpeed() <= screen_height){
						myPlane.setMiddle_y(myPlane.getMiddle_y() + myPlane.getSpeed());
					}		
				}
				else if(y < myPlane.getMiddle_y() - 20){
					if(myPlane.getMiddle_y() - myPlane.getSpeed() >= 0){
						matrix.postScale(1,-1);
						myPlane.myplane = Bitmap.createBitmap(myPlane.myplane,0,0
								,(int)myPlane.getObject_width()
								,(int)myPlane.getObject_height()
								,matrix,true);
						myPlane.setMiddle_y(myPlane.getMiddle_y() - myPlane.getSpeed());
					}
				}
				return true;
			}	
		}
		return false;
	}
	// ��ʼ��ͼƬ��Դ����
	@Override
	public void initBitmap() {
		// TODO Auto-generated method stub
		playButton = BitmapFactory.decodeResource(getResources(),R.drawable.play);
		background = BitmapFactory.decodeResource(getResources(), R.drawable.back);
		background2 = BitmapFactory.decodeResource(getResources(), R.drawable.back2);
		scalex = screen_width / background.getWidth();
		scaley = screen_height / background.getHeight();
		play_bt_w = playButton.getWidth();
		play_bt_h = playButton.getHeight()/2;
		bg_y = 0;
		bg_y2 = bg_y - screen_height;
	}
	//��ʼ����Ϸ����
	public void initObject(){
		for(EnemyPlane obj:enemyPlanes){	
			//��ʼ��С�͵л�	
			if(obj instanceof SmallPlane){
				if(!obj.isAlive()){
					obj.initial(speedTime,0,0);
					break;
				}	
			}
			//��ʼ�����͵л�
			else if(obj instanceof MiddlePlane){
				//if(middlePlaneScore > 10000){
					if(!obj.isAlive()){
						obj.initial(speedTime,0,0);
						break;
					}	
				//}
			}
			//��ʼ�����͵л�
			else if(obj instanceof BigPlane){
				//if(bigPlaneScore >= 25000){
					if(!obj.isAlive()){
						obj.initial(speedTime,0,0);
						break;
					}	
				//}
			}
		}
		//�����ȼ�			speedTime++;	
	}
	// �ͷ�ͼƬ��Դ�ķ���
	@Override
	public void release() {
		// TODO Auto-generated method stub
		for(GameObject obj:enemyPlanes){			
			obj.release();
		}
		myPlane.release();
		if(!playButton.isRecycled()){
			playButton.recycle();
		}
		if(!background.isRecycled()){
			background.recycle();
		}
		if(!background2.isRecycled()){
			background2.recycle();
		}
	}
	// ��ͼ����
	@Override
	public void drawSelf() {
		// TODO Auto-generated method stub
		try {
			canvas = sfh.lockCanvas();
			canvas.drawColor(Color.BLACK); // ���Ʊ���ɫ
			canvas.save();
			// ���㱳��ͼƬ����Ļ�ı���
			canvas.scale(scalex, scaley, 0, 0);
			canvas.drawBitmap(background, 0, bg_y, paint);   // ���Ʊ���ͼ
			canvas.drawBitmap(background2, 0, bg_y2, paint); // ���Ʊ���ͼ
			canvas.restore();	
			//���ư�ť
			canvas.save();
			canvas.clipRect(10, 10, 10 + play_bt_w,10 + play_bt_h);
			if(isPlay){
				canvas.drawBitmap(playButton, 10, 10, paint);			 
			}
			else{
				canvas.drawBitmap(playButton, 10, 10 - play_bt_h, paint);
			}
			canvas.restore();
			//���Ƶл�
			for(EnemyPlane obj:enemyPlanes){		
				if(obj.isAlive()){
					obj.drawSelf(canvas);					
					//���л��Ƿ�����ҵķɻ���ײ					
					if(obj.isCanCollide() && myPlane.isAlive()){		
						if(obj.isCollide(myPlane)){		
							if(obj.getObject_height()<=myPlane.getObject_height())//���˱Ƚ�С,ֻ���߶�
							{
								sum++;
								obj.setAlive(false);
								matrix.setScale(scale,scale);
								myPlane.myplane = Bitmap.createBitmap(myPlane.myplane,0,0
										,(int)myPlane.getObject_width()
										,(int)myPlane.getObject_height()
										,matrix,true);
								myPlane.drawSelf(canvas);
								myPlane.setObject_height(myPlane.getObject_height()*1.1f);
								myPlane.setObject_width(myPlane.getObject_width()*1.1f);
						}
						else{
							myPlane.setAlive(false);
						}
					}
				}	
			}
			}
			if(!myPlane.isAlive()){
				threadFlag = false;
				sounds.playSound(4, 0);			//�ɻ�ը�ٵ���Ч
			}
			myPlane.drawSelf(canvas);	//������ҵķɻ�
			
			paint.setTextSize(30);
			paint.setColor(Color.rgb(235, 1, 1));
			canvas.drawText("Grade: "+String.valueOf(sum), screen_width - 150, 40, paint); //��������
		} catch (Exception err) {
			err.printStackTrace();
		} finally {
			if (canvas != null)
				sfh.unlockCanvasAndPost(canvas);
		}
	}
	// �����ƶ����߼�����
	public void viewLogic(){
		if(bg_y > bg_y2){
			bg_y += 10;											
			bg_y2 = bg_y - background.getHeight();
		}
		else{
			bg_y2 += 10;											
			bg_y = bg_y2 - background.getHeight();
		}
		if(bg_y >= background.getHeight()){
			bg_y = bg_y2 - background.getHeight();
		}
		else if(bg_y2 >= background.getHeight()){
			bg_y2 = bg_y - background.getHeight();
		}
	}
	// ������Ϸ�����ķ��� 
	public void addGameScore(int score){
		middlePlaneScore += score;	// ���͵л��Ļ���
		bigPlaneScore += score;		// ���͵л��Ļ���
	}
	// ������Ч
	public void playSound(int key){
		sounds.playSound(key, 0);
	}
	// �߳����еķ���
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (threadFlag) {	
			long startTime = System.currentTimeMillis();
			initObject();
			drawSelf();	
			viewLogic();		//�����ƶ����߼�	
			long endTime = System.currentTimeMillis();	
			if(!isPlay){
				synchronized (thread) {  
				    try {  
				    	thread.wait();  
				    } catch (InterruptedException e) {  
				        e.printStackTrace();  
				    }  
				}  		
			}	
			try {
				if (endTime - startTime < 100)
					Thread.sleep(100 - (endTime - startTime));
			} catch (InterruptedException err) {
				err.printStackTrace();
			}
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Message message = new Message();   
		message.what = 	ConstantUtil.TO_END_VIEW;
		mainActivity.getHandler().sendMessage(message);
	}
}
