package com.example.libengineandroid;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class AndroidRewardedAd {

	private RewardedAd rewardedAd;
	private final OnUserEarnedRewardListener _rewardCallback;
	private final EngineAndroid _engine;
	private final AdRequest _adRequest;

	public AndroidRewardedAd(EngineAndroid engine, OnUserEarnedRewardListener earned) {

		this._engine = engine;
		this._rewardCallback = earned;

		// Request de un anuncio como el banner
		_adRequest = new AdRequest.Builder().build();

		tryLoadAd();

	}

	//Intenta cargar el anuncio
	private void tryLoadAd() {
		_engine.getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				RewardedAd.load(_engine.getActivity(), "ca-app-pub-3940256099942544/5224354917",
						_adRequest, new RewardedAdLoadCallback() {
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
										_engine.getScene().recieveADMSG();
									}

								});
							}
						});
			}
		});
	}

	public void show() {
		_engine.getActivity().runOnUiThread(() -> {
			// Si se ha creado correctamente
			if (rewardedAd != null) {
				rewardedAd.show(_engine.getActivity(), _rewardCallback);
			} else {
				//Si no, se intenta cargar de nuevo
				tryLoadAd();
			}
		});
	}

}
