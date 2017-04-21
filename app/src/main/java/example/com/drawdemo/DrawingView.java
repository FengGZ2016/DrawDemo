package example.com.drawdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by 国富小哥 on 2017/4/20.
 */

public class DrawingView extends View{
    private Canvas mCanvas;
    private Path mPath;
    private Paint mPaint;
    private Paint mBitmapPaint;
    private Bitmap mBitmap;
    private ArrayList<DrawPath> savePath;//保存的路径
    private ArrayList<DrawPath> deletePath;//删除的路径
    private int bitmapWidth;//屏幕分辨率
    private int bitmapHeight;
    private DrawPath drawPath;
    private float mX, mY;


    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
       getWindomMetrics();//获取屏幕分辨率
        initCanvas();//初始化画布
        initChangePath();

    }

    private void initChangePath() {
        savePath=new ArrayList<>();
        deletePath=new ArrayList<>();

    }



    private void initCanvas() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFF00FF00);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(10);

        mBitmapPaint=new Paint(Paint.ANTI_ALIAS_FLAG);

        mBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        mCanvas=new Canvas(mBitmap);
        mCanvas.drawColor(Color.WHITE);
        mPath=new Path();
    }

    private void getWindomMetrics() {
        //得到屏幕的分辨率
        DisplayMetrics metrics=getResources().getDisplayMetrics();
        bitmapWidth=metrics.widthPixels;
        bitmapHeight=metrics.heightPixels-2*45;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);//显示旧的画布      
        if (mPath != null) {
          // 实时的显示
           canvas.drawPath(mPath, mPaint);
          }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
            switch (event.getAction()) {
               case MotionEvent.ACTION_DOWN:
                    mPath = new Path();
                    drawPath = new DrawPath();
                   drawPath.path = mPath;
                   drawPath.paint = mPaint;
                    touch_start(x, y);
                    invalidate(); //清屏
                    break;
                case MotionEvent.ACTION_MOVE:
                   touch_move(x, y);
                   invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                  touch_up();
                    invalidate();
                    break;
                }

        return true;
    }


    private void touch_up() {
        mPath.lineTo(mX, mY);
        mCanvas.drawPath(mPath, mPaint);
        savePath.add(drawPath);
        mPath = null;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= 4 || dy >= 4) {
            //mPath.quadTo(mX, mY, x, y);
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);//源代码是这样写的，可是我没有弄明白，为什么要这样？
            mX = x;
            mY = y;
        }
    }

    private void touch_start(float x, float y) {
        mPath.reset();//清空path
        mPath.moveTo(x, y);
        mX = x;
        mY = y;

    }


            /**
             * 清空的主要思想就是初始化画布
             * 将保存路径的两个List清空
             * */
            public void removeAllPaint(){
        //调用初始化画布函数以清空画布
                initCanvas();
                invalidate();//刷新
                savePath.clear();
                deletePath.clear();
        }


    /**
     *撤销的核心思想就是将画布清空，
      将保存下来的Path路径最后一个移除掉，
      重新将路径画在画布上面。
     *
     * */
    public void undo(){
        if(savePath != null && savePath.size() > 0){
            //调用初始化画布函数以清空画布
            initCanvas();
            //将路径保存列表中的最后一个元素删除 ,并将其保存在路径删除列表中
            DrawPath drawPath = savePath.get(savePath.size() - 1);
            deletePath.add(drawPath);
            savePath.remove(savePath.size() - 1);
            //将路径保存列表中的路径重绘在画布上
            Iterator<DrawPath> iter = savePath.iterator();//重复保存
            while (iter.hasNext()) {
                DrawPath dp = iter.next();
                mCanvas.drawPath(dp.path, dp.paint);
            }
            invalidate();// 刷新
            }
        }




    /**
     * 恢复
     * */
    public void redo(){
        if(deletePath.size() > 0){
            //将删除的路径列表中的最后一个，也就是最顶端路径取出（栈）,并加入路径保存列表中
            DrawPath dp = deletePath.get(deletePath.size() - 1);
            savePath.add(dp);
            //将取出的路径重绘在画布上
            mCanvas.drawPath(dp.path, dp.paint);
            //将该路径从删除的路径列表中去除
            deletePath.remove(deletePath.size() - 1);
            invalidate();
        }
        }


    /**
     * 保存
     * */
    public String saveBitmap(){
        //获得系统当前时间，并以该时间作为文件名
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        //获取当前时间
        Date curDate=new Date(System.currentTimeMillis());

        String str=formatter.format(curDate);
        String paintPath = "";
        str = str + "paint.png";
        File dir = new File("/sdcard/notes/");
        File file = new File("/sdcard/notes/",str);
        if (!dir.exists()) {
            dir.mkdir();
            }
        else{
            if(file.exists()){
               file.delete();
             }
            }
        try {
            FileOutputStream out = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            //保存绘图文件路径
            paintPath = "/sdcard/notes/" + str;
            } catch (FileNotFoundException e) {
            e.printStackTrace();
            } catch (IOException e) {
            e.printStackTrace();
            }
        return paintPath;
    }


    }
