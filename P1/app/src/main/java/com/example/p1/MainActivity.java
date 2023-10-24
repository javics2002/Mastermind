package com.example.p1;

import androidx.appcompat.app.AppCompatActivity;
import android.view.SurfaceView;
import android.os.Bundle;
import android.content.res.AssetManager;

import com.example.libengineandroid.EngineAndroid;
import com.example.libengineandroid.InputAndroid;
import com.example.logiclib.DifficultyScene;
import com.example.logiclib.GameScene;
import com.example.logiclib.InitialScene;
import android.view.View;
import android.view.Window;


public class MainActivity extends AppCompatActivity {
    private SurfaceView rView;
    private AssetManager manager;

    private EngineAndroid engineAndroid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.rView = new SurfaceView(this);
        this.rView.getLayoutParams();


        setContentView(this.rView);
        this.manager = getAssets();
        this.engineAndroid = new EngineAndroid(this.rView, 1080,2400);
        InitialScene scene = new InitialScene(engineAndroid);
        engineAndroid.setCurrentScene(scene);
        engineAndroid.resume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        this.engineAndroid.pause();
    }
    @Override
    protected void onResume() {
        super.onResume();
        this.engineAndroid.resume();
    }

}