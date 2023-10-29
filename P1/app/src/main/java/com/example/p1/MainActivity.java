package com.example.p1;

import androidx.appcompat.app.AppCompatActivity;
import android.view.SurfaceView;
import android.os.Bundle;
import android.content.res.AssetManager;

import com.example.libengineandroid.EngineAndroid;
import com.example.logiclib.GameScene;
import com.example.aninterface.Scene;
import com.example.logiclib.InitialScene;

public class MainActivity extends AppCompatActivity {
    private EngineAndroid _engineAndroid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SurfaceView surfaceView = new SurfaceView(this);
        surfaceView.getLayoutParams();

        setContentView(surfaceView);

        float aspectRatio = 2f / 3f;
        int height = 720;
        _engineAndroid = new EngineAndroid(surfaceView, (int) (height * aspectRatio), height);

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