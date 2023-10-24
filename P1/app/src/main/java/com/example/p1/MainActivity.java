package com.example.p1;

import androidx.appcompat.app.AppCompatActivity;
import android.view.SurfaceView;
import android.os.Bundle;
import android.content.res.AssetManager;

import com.example.libengineandroid.EngineAndroid;
import com.example.logiclib.DifficultyScene;
import com.example.logiclib.GameScene;
import com.example.logiclib.InitialScene;


public class MainActivity extends AppCompatActivity {

    private SurfaceView rView;
    private AssetManager manager;

    private EngineAndroid engineAndroid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.rView = new SurfaceView(this);
        setContentView(this.rView);
        this.manager = getAssets();
        this.engineAndroid = new EngineAndroid(this.rView, 1080,2400);
        DifficultyScene scene = new DifficultyScene(engineAndroid);
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