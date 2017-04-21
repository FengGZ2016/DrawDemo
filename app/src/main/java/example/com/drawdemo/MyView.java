package example.com.drawdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by 国富小哥 on 2017/4/20.
 */

public class MyView extends View{
    private Path mPath;//路径
    private Paint mPaint;//画笔
    private int cachaBitmapWidth;
    private int cachaBitmapHeight;
    private Bitmap cachaBitmap;
    private Canvas cacheCanvas;//缓冲画布
    private Bitmap dogBitmap;
    private int dogLeft =0;
    private int dogTop =0;
    private float addx=2;
    private float addy=2;
    private boolean isrun=false;


    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
       // drawPath();
        drawBitmap();
    }

    public void setIsRun(boolean isrun){
        this.isrun=isrun;
        //开启线程
        staetThread();
    }

    private void drawBitmap() {
        mPaint = new Paint(Paint.DITHER_FLAG);
        //设置画笔的颜色
        mPaint.setColor(Color.RED);
        //设置画笔的风格
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        //反锯齿
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        //狗的图片
        dogBitmap= BitmapFactory.decodeResource(getResources(),R.mipmap.dog);
        //得到屏幕的分辨率
        DisplayMetrics metrics=getResources().getDisplayMetrics();
        cachaBitmapWidth=metrics.widthPixels;
        cachaBitmapHeight=metrics.heightPixels-2*45;
        //
        cachaBitmap = Bitmap.createBitmap(cachaBitmapWidth, cachaBitmapHeight, Bitmap.Config.ARGB_8888);
        cacheCanvas = new Canvas();
        cacheCanvas.setBitmap(cachaBitmap);

        //开启线程
       // staetThread();

    }

    private void staetThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isrun) {
                    try {
                        Thread.sleep(10);
                        cacheCanvas.drawColor(Color.WHITE);
                        cacheCanvas.drawBitmap(dogBitmap,dogLeft,dogTop,mPaint);
                        dogLeft+=addx;
                        dogTop+=addy;

                        if(dogLeft<0){
                            addx=Math.abs(addx);
                        }
                        if(dogTop<0){
                            addy=Math.abs(addy);
                        }
                        if(dogLeft>getWidth()){
                            addx=-Math.abs(addx);
                        }
                        if(dogTop>getHeight()){
                            addy=-Math.abs(addy);
                        }

                        postInvalidate();
                    } catch (Exception e) {

                    }
                }
            }
        }).start();

    }




//    private void drawPath() {
//        mPath=new Path();
//        mPaint = new Paint(Paint.DITHER_FLAG);
//        //设置画笔的颜色
//        mPaint.setColor(Color.RED);
//        //设置画笔的风格
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setStrokeWidth(5);
//        //反锯齿
//        mPaint.setAntiAlias(true);
//        mPaint.setDither(true);
//    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mPaint!=null){
            //画布颜色
            canvas.drawColor(Color.WHITE);
            canvas.drawBitmap(cachaBitmap, 0, 0, mPaint);
            //canvas.drawPath(mPath,mPaint);
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            //按下
            case MotionEvent.ACTION_DOWN:
                float dx = event.getX();
                float dy = event.getY();
                mPath.moveTo(dx, dy);
                break;
            //移动
            case MotionEvent.ACTION_MOVE:
                float mx = event.getX();
                float my = event.getY();
                mPath.lineTo(mx,my);
                invalidate();
                break;
            //离开
            case MotionEvent.ACTION_UP:
                float ux = event.getX();
                float uy = event.getY();
                mPath.lineTo(ux,uy);
                invalidate();
                break;
        }


        return true;
    }
}
