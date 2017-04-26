package example.com.drawdemo;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button btn_run;
    private Button btn_stop;
    private MyView mMyView;
    //activity_main2
    private Button btn_save;
    private Button btn_undo;
    private Button btn_redo;
    private Button btn_remove;
    private DrawingView drawingView;
    private AlertDialog.Builder widthDialog;//设置画笔粗细的对话框
    private View dialogView;//对话框的view
    private TextView showWidth;//显示画笔粗细的textview
    private SeekBar widthSeekBar;//拖动画笔粗细的sb
    private int paintWidth;//记录画笔的粗细

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉应用标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main2);
        //initView();
        init();
        initDialog();


    }

    private void initDialog() {
        dialogView = getLayoutInflater().inflate(R.layout.dialog_width_set, null);
        showWidth = (TextView) dialogView.findViewById(R.id.textView1);
        widthSeekBar = (SeekBar) dialogView.findViewById(R.id.seekBar1);
        //给SeekBar设置点击事件
        widthSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                showWidth.setText("当前选中宽度：" + (progress + 1));
                paintWidth = progress + 1;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //对话框
        widthDialog = new AlertDialog.Builder(MainActivity.this);
        widthDialog.setIcon(R.mipmap.ic_launcher);
        widthDialog.setTitle("设置画笔粗细");
        widthDialog.setView(dialogView);
        widthDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //在这里调用DrawingView的设置画笔粗细方法
                    drawingView.setPaintWidth(paintWidth);
            }
        });
        widthDialog.setNegativeButton("取消", null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SubMenu colorSubMenu = menu.addSubMenu(1, 1, 1, "选择画笔颜色");
        colorSubMenu.add(2, 200, 200, "红色");
        colorSubMenu.add(2, 210, 210, "绿色");
        colorSubMenu.add(2, 220, 220, "蓝色");
        colorSubMenu.add(2, 230, 230, "紫色");
        colorSubMenu.add(2, 240, 240, "黄色");
        colorSubMenu.add(2, 250, 250, "黑色");
        menu.add(1, 2, 2, "设置画笔粗细");
        SubMenu stypeSubMenu = menu.addSubMenu(1, 3, 3, "选择画笔样式");
        stypeSubMenu.add(3, 300, 300, "线状画笔");
        stypeSubMenu.add(3, 301, 301, "填充画笔");
       stypeSubMenu.add(3,302,302,"橡皮擦");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int index = item.getItemId();
        switch (index) {
            case 200:
                drawingView.setColor(Color.RED);
                break;
            case 210:
                drawingView.setColor(Color.GREEN);
                break;
            case 220:
                drawingView.setColor(Color.BLUE);
                break;
            case 230:
                drawingView.setColor(Color.MAGENTA);
                break;
            case 240:
                drawingView.setColor(Color.YELLOW);
                break;
            case 250:
                drawingView.setColor(Color.BLACK);
                break;
            case 2:
                widthDialog.show();
                break;
            case 300:
                drawingView.setStyle(DrawingView.PEN);
                break;
            case 301:
                drawingView.setStyle(DrawingView.PAIL);
                break;
            case 302:
                drawingView.setStyle(DrawingView.PAIL);
                drawingView.setColor(Color.WHITE);
                break;
        }
            return true;
        }


    private void init() {
        drawingView= (DrawingView) findViewById(R.id.drawing_view);
        btn_save= (Button) findViewById(R.id.btn_save);
        btn_redo= (Button) findViewById(R.id.btn_redo);
        btn_undo= (Button) findViewById(R.id.btn_undo);
        btn_remove= (Button) findViewById(R.id.btn_remove);

        /**
         * 保存
         * */
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.saveBitmap();
            }
        });

        /**
         * 撤销
         * */
        btn_undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.undo();
            }
        });

        /**
         * 恢复
         * */
        btn_redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.redo();
            }
        });

        /**
         * 清空
         * */
        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.removeAllPaint();
            }
        });

    }

    private void initView() {
        mMyView= (MyView) findViewById(R.id.myView);
        btn_run= (Button) findViewById(R.id.btn_run);
        btn_stop= (Button) findViewById(R.id.btn_stop);


        /**
         * 开跑
         * */
        btn_run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyView.setIsRun(true);
            }
        });

        /**
         * 听跑
         * */
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyView.setIsRun(false);
            }
        });

    }


}
