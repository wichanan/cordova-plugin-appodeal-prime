package com.appodeal.ads;

import android.util.Log;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.RewardedVideoAd;
import com.facebook.ads.RewardedVideoAdListener;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import com.appodeal.Action;
import com.appodeal.Events;

public class RewardVideo extends AdBase  {
    private static final String TAG = "AppodealPrime::RewardVideo";
    private RewardedVideoAd rewardedVideoAd;

    FBRewardVideo(int id, String placementID) {
        super(id, placementID);
    }

    public static boolean executeShowAction(Action action, CallbackContext callbackContext) {
        plugin.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                FBRewardVideo fbRewardVideo = (FBRewardVideo) action.getAd();
                if (fbRewardVideo == null) {
                    fbRewardVideo = new FBRewardVideo(
                            action.optId(),
                            action.getPlacementID()
                    );
                }
                fbRewardVideo.show();
                PluginResult result = new PluginResult(PluginResult.Status.OK, "");
                callbackContext.sendPluginResult(result);
            }
        });

        return true;
    }

    public void show() {
        if (rewardedVideoAd == null) {
            rewardedVideoAd = new RewardedVideoAd(plugin.webView.getContext(), placementID);
        }
        rewardedVideoAd.loadAd();

        rewardedVideoAd.setAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoCompleted() {
                plugin.emit(Events.REWARD_VIDEO_COMPLETE);
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                plugin.emit(Events.REWARD_VIDEO_IMPRESSION);
            }

            @Override
            public void onRewardedVideoClosed() {
                plugin.emit(Events.REWARD_VIDEO_CLOSE);
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                Log.d(TAG, "Error loading ad with" + adError.getErrorMessage());
                plugin.emit(Events.REWARD_VIDEO_LOAD_FAIL);
            }

            @Override
            public void onAdLoaded(Ad ad) {
                rewardedVideoAd.loadAd();
                plugin.emit(Events.REWARD_VIDEO_LOAD);
            }

            @Override
            public void onAdClicked(Ad ad) {
                plugin.emit(Events.REWARD_VIDEO_CLICK);
            }
        });
    }
}
