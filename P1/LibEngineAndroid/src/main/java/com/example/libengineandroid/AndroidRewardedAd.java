package com.example.libengineandroid;
import android.util.Log;

import com.example.aninterface.Scene;
import com.example.logiclib.GameAttributes;
import com.example.logiclib.GameOverScene;
import com.example.logiclib.GameScene;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import androidx.annotation.NonNull;

public class AndroidRewardedAd {

    private RewardedAd rewardedAd;
    private int rewardedAttemps =2;
    private final OnUserEarnedRewardListener rewardCallback;
    private final EngineAndroid engine;
    private final AdRequest adRequest;

    public AndroidRewardedAd(EngineAndroid engine, OnUserEarnedRewardListener earned) {


        this.engine = engine;
        this.rewardCallback = earned;

        // Request de un anuncio como el banner
        adRequest = new AdRequest.Builder().build();

        tryLoadAd();


    }

    //Intenta cargar el anuncio
    private void tryLoadAd() {
        engine.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RewardedAd.load(engine.getActivity(), "ca-app-pub-3940256099942544/5224354917",
                        adRequest, new RewardedAdLoadCallback() {
                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                super.onAdFailedToLoad(loadAdError);
                                rewardedAd = null;
                            }

                            @Override
                            public void onAdLoaded(@NonNull RewardedAd rewarded) {
                                super.onAdLoaded(rewarded);

                                rewardedAd = rewarded;

                                //Cuando el anuncio sea mostrado, cargamos el siguiente
                                rewarded.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        super.onAdShowedFullScreenContent();
                                        tryLoadAd();
                                    }
                                    //En este metodo es donde realmente se quita el anuncio
                                    //Recompensamos con intentos al jugador usando game attributes
                                    //Diferenciamos entre si hay mundos o si de lo contrario es una partida rapida
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Este método se llama cuando el usuario cierra el anuncio
                                        if (engine.getScene() instanceof GameOverScene) {
                                            GameOverScene gameOverScene = (GameOverScene) engine.getScene();
                                            GameAttributes _gameAttributes= gameOverScene.getGameAttributtes();

                                            if(_gameAttributes!=null) {
                                                Scene scene = new GameScene(engine, rewardedAttemps, rewardedAttemps, _gameAttributes.combinationLength,
                                                        _gameAttributes.colorNumber, _gameAttributes.repeatedColors, _gameAttributes.returnScene,
                                                        _gameAttributes.backGroundSkinId, _gameAttributes.skin, _gameAttributes.selectedWorld, _gameAttributes.resultCombination);
                                                engine.setCurrentScene(scene);
                                            }
                                            else Log.d("AD","GAME ATTRIBUTES INITIALISATION ERROR IN REWARDED AD CALLBACK");
                                        } else Log.d("AD","CASTING SCENE ERROR IN REWARDED AD CALLBACK");
                                    }
                                });
                            }
                        });
            }
        });
    }

    public void show() {
        engine.getActivity().runOnUiThread(() -> {
            // Si se ha creado correctamente
            if (rewardedAd != null) {
                rewardedAd.show(engine.getActivity(), rewardCallback);
            }
            else {
                //Si no, se intenta cargar de nuevo
                tryLoadAd();
            }
        });
    }

}
