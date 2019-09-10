package com.appodealprime.ads;

import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.InterstitialCallbacks;
import com.appodealprime.Action;
import com.appodealprime.Events;

public class InterstitialAd extends AdBase {
    private static final String TAG = "AP::InterstitialAd";

    InterstitialAd(int id) {
        super(id);
    }

    public static boolean executeInterstitialShowAction(Action action, CallbackContext callbackContext) {
        plugin.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                InterstitialAd interstitialAd = (InterstitialAd) action.getAd();
                if (interstitialAd == null) {
                    interstitialAd = new InterstitialAd(
                            action.optId()
                    );
                }
                interstitialAd.show();
                PluginResult result = new PluginResult(PluginResult.Status.OK, "");
                callbackContext.sendPluginResult(result);
            }
        });

        return true;
    }

    public void show() {
        Appodeal.show(plugin.cordova.getActivity(), Appodeal.INTERSTITIAL);

        Appodeal.setInterstitialCallbacks(new InterstitialCallbacks() {
            @Override
            public void onInterstitialLoaded(boolean b) {
                Log.d(TAG, "on Interstitial loaded");
                plugin.emit(Events.INTERSTITIAL_LOAD);
            }

            @Override
            public void onInterstitialFailedToLoad() {
                Log.d(TAG, "Interstitial Failed to load");
                plugin.emit(Events.INTERSTITIAL_LOAD_FAIL);
            }

            @Override
            public void onInterstitialShown() {
                Log.d(TAG, "Interstitial shown");
                plugin.emit(Events.INTERSTITIAL_SHOW);
            }

            @Override
            public void onInterstitialClicked() {
                Log.d(TAG, "Interstitial clicked");
                plugin.emit(Events.INTERSTITIAL_CLICK);
            }

            @Override
            public void onInterstitialClosed() {
                Log.d(TAG, "Interstitial closed");
                plugin.emit(Events.INTERSTITIAL_CLOSE);
            }

            @Override
            public void onInterstitialExpired() {
                Log.d(TAG, "Interstitial expired");
            }
        });
    }
}
