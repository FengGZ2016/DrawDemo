package example.com.drawdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //initView();
        init();


    }

    private void init() {
        drawingView= (DrawingView) findViewById(R.id.drawing_view);
        btn_save= (Button) findViewById(R.id.btn_save);
        btn_redo= (Button) findViewById(R.id.btn_redo);
        btn_undo= (Button) findViewById(R.id.btn_undo);
        btn_remove= (Button) findViewById(R.id.btn_remove);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.saveBitmap();
            }
        });

        btn_undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.undo();
            }
        });

        btn_redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.redo();
            }
        });

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
