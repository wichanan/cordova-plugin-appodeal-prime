package com.appodealprime.ads;

import android.util.Log;


import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import com.appodealprime.Action;
import com.appodealprime.Events;

public class RewardVideo extends AdBase  {
    private static final String TAG = "AppodealPrime::RewardVideo";

    RewardVideo(int id) {
        super(id);
    }

    public static boolean executeShowAction(Action action, CallbackContext callbackContext) {
        plugin.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                RewardVideo rewardVideo = (RewardVideo) action.getAd();
                if (rewardVideo == null) {
                    rewardVideo = new RewardVideo(
                            action.optId()
                    );
                }
//                rewardVideo.show();
                PluginResult result = new PluginResult(PluginResult.Status.OK, "");
                callbackContext.sendPluginResult(result);
            }
        });

        return true;
    }

//    public void show() {
//        if (rewardedVideoAd == null) {
//            rewardedVideoAd = new RewardedVideoAd(plugin.webView.getContext(), placementID);
//        }
//        rewardedVideoAd.loadAd();
//
//        rewardedVideoAd.setAdListener(new RewardedVideoAdListener() {
//            @Override
//            public void onRewardedVideoCompleted() {
//                plugin.emit(Events.REWARD_VIDEO_COMPLETE);
//            }
//
//            @Override
//            public void onLoggingImpression(Ad ad) {
//                plugin.emit(Events.REWARD_VIDEO_IMPRESSION);
//            }
//
//            @Override
//            public void onRewardedVideoClosed() {
//                plugin.emit(Events.REWARD_VIDEO_CLOSE);
//            }
//
//            @Override
//            public void onError(Ad ad, AdError adError) {
//                Log.d(TAG, "Error loading ad with" + adError.getErrorMessage());
//                plugin.emit(Events.REWARD_VIDEO_LOAD_FAIL);
//            }
//
//            @Override
//            public void onAdLoaded(Ad ad) {
//                rewardedVideoAd.loadAd();
//                plugin.emit(Events.REWARD_VIDEO_LOAD);
//            }
//
//            @Override
//            public void onAdClicked(Ad ad) {
//                plugin.emit(Events.REWARD_VIDEO_CLICK);
//            }
//        });
//    }
}
