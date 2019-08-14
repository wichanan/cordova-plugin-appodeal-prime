package com.appodealprime.ads;

import android.util.Log;


import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.RewardedVideoCallbacks;
import com.appodealprime.Action;
import com.appodealprime.Events;

public class RewardVideo extends AdBase  {
    private static final String TAG = "AP::RewardVideo";

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

    public void show() {
        Appodeal.show(plugin.cordova.getActivity(), Appodeal.REWARDED_VIDEO);

        Appodeal.setRewardedVideoCallbacks(new RewardedVideoCallbacks() {
            @Override
            public void onRewardedVideoLoaded(boolean b) {
                Log.d(TAG, "on rewared video loaded");
            }

            @Override
            public void onRewardedVideoFailedToLoad() {
                Log.d(TAG, "on rewared video failed to load");
            }

            @Override
            public void onRewardedVideoShown() {
                Log.d(TAG, "on rewared video shown");
            }

            @Override
            public void onRewardedVideoFinished(double v, String s) {
                Log.d(TAG, "on rewared video finished");
            }

            @Override
            public void onRewardedVideoClosed(boolean b) {
                Log.d(TAG, "on rewared video closed");
            }

            @Override
            public void onRewardedVideoExpired() {
                Log.d(TAG, "on rewared video expired");
            }

            @Override
            public void onRewardedVideoClicked() {
                Log.d(TAG, "on rewared video clicked");
            }
        });
    }
}
