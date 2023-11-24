package com.example.p1;

import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aninterface.Scene;
import com.example.libengineandroid.EngineAndroid;
import com.example.logiclib.Button;
import com.example.logiclib.InitialScene;
//Auncios
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class MainActivity extends AppCompatActivity {
    private EngineAndroid _engineAndroid;
    //Anuncios
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SurfaceView sf = new SurfaceView(this);


        _engineAndroid = new EngineAndroid(sf);

        Scene firstScene = new InitialScene(_engineAndroid);
        _engineAndroid.setCurrentScene(firstScene);
        _engineAndroid.resume();


        //Anuncios Inicializado
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        //Creacion de un layout relativo para introducir el anuncio
        RelativeLayout layout = new RelativeLayout(this);

        // Se añade el surfaceView a este layout
        layout.addView(sf);
        initialisedownBanner();
        layout.addView(mAdView);


        //Esto es para hacer que se vea
        setContentView(layout);

        createAdRequest();

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
    protected void createAdRequest()
    {
        //Request de anuncios
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    };
    protected void initialisedownBanner()
    {
        mAdView = new AdView(this);
        mAdView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setEnabled(true);
        mAdView.setVisibility(View.VISIBLE);

        //Hacemos que se situe en la parte de abajo
        RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                AdSize.AUTO_HEIGHT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mAdView.setLayoutParams(adParams);
    }
    @Override
    protected void onStop() {
        super.onStop();


    }

    protected void onDestroy() {
        super.onDestroy();


    }
}

