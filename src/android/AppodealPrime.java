package com.appodealprime;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.AppodealNetworks;
import com.appodeal.ads.Native;
import com.appodeal.ads.NativeAd;
import com.appodeal.ads.NativeCallbacks;
import com.appodealprime.ads.APNativeAd;
import com.appodealprime.ads.AdBase;
import com.appodealprime.ads.BannerAd;
import com.appodealprime.ads.InterstitialAd;
import com.appodealprime.ads.RewardVideo;

public class AppodealPrime extends CordovaPlugin {

    private static final String TAG = "AppodealPrime()";

    private CallbackContext readyCallbackContext = null;

    private ArrayList<PluginResult> waitingForReadyCallbackContextResults = new ArrayList<PluginResult>();
    public static String apiKey;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        // Appodeal.disableNetwork(cordova.getActivity(), AppodealNetworks.ADMOB);
        // Appodeal.setLogLevel(com.appodeal.ads.utils.Log.LogLevel.debug);
        apiKey = this.getApiKey();
        Appodeal.initialize(
                cordova.getActivity(),
                apiKey,
                Appodeal.BANNER | Appodeal.INTERSTITIAL | Appodeal.REWARDED_VIDEO,
                true);
        AdBase.initialize(this);
    }

    @Override
    public boolean execute(String actionKey, JSONArray args, CallbackContext callbackContext) {
        Action action = new Action(args);
        if (Actions.READY.equals(actionKey)) {
            if (waitingForReadyCallbackContextResults == null) {
                return false;
            }
            readyCallbackContext = callbackContext;
            for (PluginResult result : waitingForReadyCallbackContextResults) {
                readyCallbackContext.sendPluginResult(result);
            }
            waitingForReadyCallbackContextResults = null;
            JSONObject data = new JSONObject();
            try {
                data.put("platform", "android");
                data.put("SDK Version", "5.4.1");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            emit(Events.READY, data);
            return true;
        } else if (Actions.BANNER_SHOW.equals(actionKey)) {
            return BannerAd.executeShowAction(action, callbackContext);
        } else if (Actions.BANNER_HIDE.equals(actionKey)) {
            Log.d(TAG, "Hide banner acrion");
            return BannerAd.executeHideAction(action, callbackContext);
        } else if (Actions.INTERSTITIAL_SHOW.equals(actionKey)) {
            return InterstitialAd.executeInterstitialShowAction(action, callbackContext);
        } else if (Actions.REWARD_VIDEO_SHOW.equals(actionKey)) {
            return RewardVideo.executeShowAction(action, callbackContext);
        } else if (Actions.NATIVE_LOAD.equals(actionKey)) {
            return APNativeAd.executeNativeLoadAction(action, callbackContext);
        } else if (Actions.NATIVE_HIDE.equals(actionKey)) {
            Log.d(TAG, "Hide Native acrion");
            return APNativeAd.executeNativeHideAction(action, callbackContext);
        } else if (Actions.NATIVE_SHOW.equals(actionKey)) {
            return APNativeAd.executeNativeShowAction(action, callbackContext);
        }

        return false;
    }

    @Override
    public void onDestroy() {
        readyCallbackContext = null;

        super.onDestroy();
    }

    @Override
    public void onResume(boolean multitasking) {
        super.onResume(multitasking);
        Log.d(TAG, "onResume");
        Appodeal.onResume(cordova.getActivity(), Appodeal.BANNER_VIEW);
    }

    public void emit(String eventType) {
        emit(eventType, false);
    }

    public void emit(String eventType, Object data) {
        JSONObject event = new JSONObject();
        try {
            event.put("type", eventType);
            event.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        PluginResult result = new PluginResult(PluginResult.Status.OK, event);
        result.setKeepCallback(true);
        if (readyCallbackContext == null) {
            waitingForReadyCallbackContextResults.add(result);
        } else {
            readyCallbackContext.sendPluginResult(result);
        }
    }

    private String getApiKey() {
        try {
            ApplicationInfo ai = this.cordova.getActivity().getApplicationContext().getPackageManager().getApplicationInfo(cordova.getActivity().getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            return bundle.getString("com.appodeal.ads.appodealprime.APP_KEY");
        } catch (Exception e) {
            Log.e(TAG, "Forget to configure <meta-data android:name=\"com.appodeal.ads.appodealprime.APP_KEY\" android:value=\"XXX\"/> in your AndroidManifest.xml file.");
        }
        return "";
    }
}
