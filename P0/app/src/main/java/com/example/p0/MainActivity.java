package com.example.p0;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.content.Intent;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "Llamada a metodo onCreate");

        surfaceView = findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "Llamada a metodo onStart");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "Llamada a metodo onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "Llamada a metodo onPause");
    }

    @Override
    protected void onStop() {
        // call the superclass method first
        super.onStop();
        Log.i(TAG, "Llamada a metodo onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Llamada a metodo onDestroy");
    }

//    public void sendMessage(View view) {
//        Intent intent = new Intent(this, DisplayMessageActivity.class);
//        EditText editText = (EditText) findViewById(R.id.editTextText2);
//        String message = editText.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, message);
//        startActivity(intent);
//    }

    public void draw(View view){
        Paint paint = new Paint();
        paint.setColor(Color.RED);

        Canvas canvas = new Canvas();
        canvas = surfaceHolder.lockCanvas();
        int radius = 20;
        canvas.drawCircle(canvas.getWidth() / 2 - radius, canvas.getHeight() / 2 - radius, radius, paint);
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    public void clear(View view){
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);

        Canvas canvas = new Canvas();
        canvas = surfaceHolder.lockCanvas();
        int radius = 20;
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);
        surfaceHolder.unlockCanvasAndPost(canvas);
    }
}