package com.appodeal;

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

import com.appodeal.ads.AdBase;
import com.appodeal.ads.BannerAd;
import com.appodeal.ads.FBInterstitialAd;
import com.appodeal.ads.FBNativeAd;

public class AppodealPrime extends CordovaPlugin {

    private static final String TAG = "AppodealPrime()";

    private CallbackContext readyCallbackContext = null;

    private ArrayList<PluginResult> waitingForReadyCallbackContextResults = new ArrayList<PluginResult>();

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        AudienceNetworkAds.initialize(cordova.getActivity());
        AdBase.initialize(this);
        AdSettings.addTestDevice("ea9f55ac-cbc1-40d7-8cd0-4adddee3331f");
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
        } else if (Actions.BANNER_HIDE.equals(actionKey)) {
            return BannerAd.executeHideAction(action, callbackContext);
        } else if (Actions.BANNER_SHOW.equals(actionKey)) {
            return BannerAd.executeShowAction(action, callbackContext);
        } else if (Actions.NATIVE_LOAD.equals(actionKey)) {
            return FBNativeAd.executeNativeLoadAction(action, callbackContext);
        } else if (Actions.NATIVE_SHOW.equals(actionKey)) {
            return FBNativeAd.executeNativeShowAction(action, callbackContext);
        } else if (Actions.NATIVE_HIDE.equals(actionKey)) {
            return FBNativeAd.executeNativeHideAction(action, callbackContext);
        }else if (Actions.NATIVE_HIDE_ALL.equals(actionKey)) {
            try {
                action.opts.put("id", 9999);
                return FBNativeAd.executeNativeHideAllAction(action, callbackContext);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (Actions.INTERSTITIAL_SHOW.equals(actionKey)) {
            return FBInterstitialAd.executeInterstitialShowAction(action, callbackContext);
        } else if (Actions.INTERSTITIAL_LOAD.equals(actionKey)) {
            return FBInterstitialAd.executeInterstitialLoadAction(action, callbackContext);
        }

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
}
