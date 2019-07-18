package com.appodeal.ads;

import android.util.Log;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import com.appodeal.Action;
import com.appodeal.Events;

public class InterstitialAd extends AdBase {
    private static final String TAG = "AppodealPrime::InterstitialAd";
    private InterstitialAd interstitialAd;

    FBInterstitialAd(int id, String placementID) {
        super(id, placementID);
    }

    public static boolean executeInterstitialLoadAction(Action action, CallbackContext callbackContext) {
        plugin.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                FBInterstitialAd fbInterstitialAd = (FBInterstitialAd) action.getAd();
                if (fbInterstitialAd == null) {
                    fbInterstitialAd = new FBInterstitialAd(
                            action.optId(),
                            action.getPlacementID()
                    );
                }
                fbInterstitialAd.load();
                PluginResult result = new PluginResult(PluginResult.Status.OK, "");
                callbackContext.sendPluginResult(result);
            }
        });

        return true;
    }

    public static boolean executeInterstitialShowAction(Action action, CallbackContext callbackContext) {
        plugin.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                FBInterstitialAd fbInterstitialAd = (FBInterstitialAd) action.getAd();
                if (fbInterstitialAd == null) {
                    fbInterstitialAd = new FBInterstitialAd(
                            action.optId(),
                            action.getPlacementID()
                    );
                }
                fbInterstitialAd.show();
                PluginResult result = new PluginResult(PluginResult.Status.OK, "");
                callbackContext.sendPluginResult(result);
            }
        });

        return true;
    }

    public void show() {
        if (interstitialAd != null) {
            interstitialAd.show();
        }
    }

    public void load() {
        if (interstitialAd == null) {
            interstitialAd = new InterstitialAd(plugin.webView.getContext(), placementID);
        }
        interstitialAd.loadAd();

        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                plugin.emit(Events.INTERSTITIAL_DISPLAYED);
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                plugin.emit(Events.INTERSTITIAL_CLOSE);
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                Log.d(TAG, "Error loading ad with" + adError.getErrorMessage());
                plugin.emit(Events.INTERSTITIAL_LOAD_FAIL);
            }

            @Override
            public void onAdLoaded(Ad ad) {
                plugin.emit(Events.INTERSTITIAL_LOAD);
            }

            @Override
            public void onAdClicked(Ad ad) {
                plugin.emit(Events.INTERSTITIAL_CLICK);
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                plugin.emit(Events.INTERSTITIAL_IMPRESSION);
            }
        });
    }
}
