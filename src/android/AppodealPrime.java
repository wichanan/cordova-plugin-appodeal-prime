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
import com.appodealprime.ads.AdBase;
import com.appodealprime.ads.BannerAd;
import com.appodealprime.ads.InterstitialAd;
import com.appodealprime.ads.NativeAd;

public class AppodealPrime extends CordovaPlugin {

    private static final String TAG = "AppodealPrime()";

    private CallbackContext readyCallbackContext = null;

    private ArrayList<PluginResult> waitingForReadyCallbackContextResults = new ArrayList<PluginResult>();

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        Appodeal.setTesting(true);
        Appodeal.setLogLevel(com.appodeal.ads.utils.Log.LogLevel.verbose);

        Appodeal.initialize(
                cordova.getActivity(),
                this.getApiKey(),
                Appodeal.BANNER | Appodeal.INTERSTITIAL | Appodeal.REWARDED_VIDEO | Appodeal.NATIVE,
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
//        } else if (Actions.BANNER_HIDE.equals(actionKey)) {
//            return BannerAd.executeHideAction(action, callbackContext);
//        }
        }
        else if (Actions.BANNER_SHOW.equals(actionKey)) {
            return BannerAd.executeShowAction(action, callbackContext);
        }
//        else if (Actions.NATIVE_LOAD.equals(actionKey)) {
//            return NativeAd.executeNativeLoadAction(action, callbackContext);
//        } else if (Actions.NATIVE_SHOW.equals(actionKey)) {
//            return NativeAd.executeNativeShowAction(action, callbackContext);
//        } else if (Actions.NATIVE_HIDE.equals(actionKey)) {
//            return NativeAd.executeNativeHideAction(action, callbackContext);
//        } else if (Actions.INTERSTITIAL_SHOW.equals(actionKey)) {
//            return InterstitialAd.executeInterstitialShowAction(action, callbackContext);
//        } else if (Actions.REWARD_VIDEO_SHOW.equals(actionKey)) {
//            return InterstitialAd.executeInterstitialShowAction(action, callbackContext);
//        }

        return false;
    }

    @Override
    public void onDestroy() {
        readyCallbackContext = null;

        super.onDestroy();
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
            ApplicationInfo ai = cordova.getActivity().getApplicationContext().getPackageManager().getApplicationInfo(cordova.getActivity().getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            return bundle.getString("com.appodeal.ads.appodealprime.APP_KEY");
        } catch (Exception e) {
            Log.e(TAG, "Forget to configure <meta-data android:name=\"com.appodeal.ads.appodealprime.APP_KEY\" android:value=\"XXX\"/> in your AndroidManifest.xml file.");
        }
        return "";
    }
}
