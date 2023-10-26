package com.example.p1;

import androidx.appcompat.app.AppCompatActivity;
import android.view.SurfaceView;
import android.os.Bundle;
import android.content.res.AssetManager;

import com.example.libengineandroid.EngineAndroid;
import com.example.logiclib.GameScene;
import com.example.aninterface.Scene;

public class MainActivity extends AppCompatActivity {
    private SurfaceView _surfaceView;
    private AssetManager _assetManager;
    private EngineAndroid _engineAndroid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _surfaceView = new SurfaceView(this);
        _surfaceView.getLayoutParams();

        setContentView(_surfaceView);
        _assetManager = getAssets();

        float aspectRatio = 2f / 3f;
        int height = 720;
        _engineAndroid = new EngineAndroid(_surfaceView, (int) (height * aspectRatio), height);

        //InitialScene scene = new InitialScene(_engineAndroid);
        Scene scene = new GameScene(_engineAndroid, 5, 6, 10);
        _engineAndroid.setCurrentScene(scene);
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