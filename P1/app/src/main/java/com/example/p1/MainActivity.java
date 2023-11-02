package com.example.p1;

import android.os.Bundle;
import android.view.SurfaceView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aninterface.Scene;
import com.example.libengineandroid.EngineAndroid;
import com.example.logiclib.InitialScene;

public class MainActivity extends AppCompatActivity {
    private EngineAndroid _engineAndroid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SurfaceView surfaceView = new SurfaceView(this);
        surfaceView.getLayoutParams();

        setContentView(surfaceView);

        _engineAndroid = new EngineAndroid(surfaceView);

        Scene firstScene = new InitialScene(_engineAndroid);
        _engineAndroid.setCurrentScene(firstScene);
        _engineAndroid.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        _engineAndroid.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        _engineAndroid.resume();
    }
}