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
                plugin.emit(Events.REWARD_VIDEO_LOAD);
            }

            @Override
            public void onRewardedVideoFailedToLoad() {
                Log.d(TAG, "on rewared video failed to load");
                plugin.emit(Events.REWARD_VIDEO_LOAD_FAIL);
            }

            @Override
            public void onRewardedVideoShown() {
                Log.d(TAG, "on rewared video shown");
                plugin.emit(Events.REWARD_VIDEO_SHOW);
            }

            @Override
            public void onRewardedVideoFinished(double v, String s) {
                Log.d(TAG, "on rewared video finished");
                plugin.emit(Events.REWARD_VIDEO_COMPLETE);
            }

            @Override
            public void onRewardedVideoClosed(boolean b) {
                Log.d(TAG, "on rewared video closed");
                plugin.emit(Events.REWARD_VIDEO_CLOSE);
            }

            @Override
            public void onRewardedVideoExpired() {
                Log.d(TAG, "on rewared video expired");
            }

            @Override
            public void onRewardedVideoClicked() {
                Log.d(TAG, "on rewared video clicked");
                plugin.emit(Events.REWARD_VIDEO_CLICK);
            }
        });
    }
}
