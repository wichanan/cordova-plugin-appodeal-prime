package com.appodeal.ads;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeAdView;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONObject;


import java.util.ArrayList;

import com.appodeal.Action;
import com.appodeal.Events;

public class NativeAd extends AdBase {
    private static final String TAG = "AppodealPrime::NativeADs";
    private NativeAd nativeAd;
    private View nativeAdView;
    private ViewGroup parentView;
    private JSONObject position;

    FBNativeAd(int id, String placementID, JSONObject position) {
        super(id, placementID);

        this.position = position;
    }

    public static boolean executeNativeLoadAction(Action action, CallbackContext callbackContext) {
        plugin.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                FBNativeAd fbNativeAd = (FBNativeAd) action.getAd();
                if (fbNativeAd == null) {
                    fbNativeAd = new FBNativeAd(
                            action.optId(),
                            action.getPlacementID(),
                            action.optPosition()
                    );
                }
                fbNativeAd.load();
                PluginResult result = new PluginResult(PluginResult.Status.OK, "");
                callbackContext.sendPluginResult(result);
            }
        });

        return true;
    }

    public static boolean executeNativeShowAction(Action action, CallbackContext callbackContext) {
        plugin.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                FBNativeAd fbNativeAd = (FBNativeAd) action.getAd();
                if (fbNativeAd == null) {
                    fbNativeAd = new FBNativeAd(
                            action.optId(),
                            action.getPlacementID(),
                            action.optPosition()
                    );
                }
                fbNativeAd.show();
                PluginResult result = new PluginResult(PluginResult.Status.OK, "");
                callbackContext.sendPluginResult(result);
            }
        });

        return true;
    }

    public static boolean executeNativeHideAction(Action action, CallbackContext callbackContext) {
        plugin.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FBNativeAd fbNativeAd = (FBNativeAd) action.getAd();
                if (fbNativeAd != null) {
                    fbNativeAd.hide();
                }

                PluginResult result = new PluginResult(PluginResult.Status.OK, "");
                callbackContext.sendPluginResult(result);
            }
        });

        return true;
    }

    public static boolean executeNativeHideAllAction(Action action, CallbackContext callbackContext) {
        plugin.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FBNativeAd fbNativeAd = (FBNativeAd) action.getAd();
                if (fbNativeAd == null) {
                    fbNativeAd = new FBNativeAd(
                            action.optId(),
                            "",
                            new JSONObject()
                    );
                }
                fbNativeAd.hideAll();
                PluginResult result = new PluginResult(PluginResult.Status.OK, "");
                callbackContext.sendPluginResult(result);
            }
        });

        return true;
    }

    public void hide() {
        if (nativeAdView != null) {
            View view = plugin.webView.getView();
            nativeAd.destroy();

            if (view.getParent() != null) {
                ((ViewGroup)view.getParent()).removeView(view);
            }
            parentView.addView(view);

            parentView.removeView(nativeAdView);

            parentView.bringToFront();
            parentView.requestLayout();
            parentView.requestFocus();
        }
    }

    public void hideAll() {
        View view = plugin.webView.getView();
        ViewGroup wvParentView = (ViewGroup) view.getParent();
        int countwv = wvParentView.getChildCount();
        for (int i = 0; i<countwv; i++) {
            View v = wvParentView.getChildAt(i);
            if (v instanceof NativeAdLayout) {
                wvParentView.removeView(v);
            }
        }
    }

    public void load() {
        if (nativeAd == null) {
            nativeAd = new NativeAd(plugin.webView.getContext(), placementID);
            if (!nativeAd.isAdLoaded()) {
                nativeAd.loadAd();
            }
        }
    }

    public void show() {
        if (nativeAdView == null) {
            nativeAd = new NativeAd(plugin.webView.getContext(), placementID);
            addNativeView(nativeAd);
        } else {
            View view = plugin.webView.getView();
            ViewGroup wvParentView = (ViewGroup) view.getParent();
            if (parentView != wvParentView) {
                parentView.removeAllViews();
                if (parentView.getParent() != null) {
                    ((ViewGroup)parentView.getParent()).removeView(parentView);
                }
            }
            nativeAd = new NativeAd(plugin.webView.getContext(), placementID);
            addNativeView(nativeAd);
        }

        nativeAd.setAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
                Log.d(TAG, "Media loaded");
                plugin.emit(Events.NATIVE_MEDIA_LOAD);
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                Log.d(TAG, "Error loading ad with" + adError.getErrorMessage());
                plugin.emit(Events.NATIVE_LOAD_FAIL);
            }

            @Override
            public void onAdLoaded(Ad ad) {
                plugin.emit(Events.NATIVE_LOAD);
            }

            @Override
            public void onAdClicked(Ad ad) {
                plugin.emit(Events.NATIVE_CLICK);
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                plugin.emit(Events.NATIVE_IMPRESSION);
            }
        });
    }

    private void addNativeView(NativeAd nativeAd) {
        Log.d(TAG, "Trying native show");
        nativeAdView = NativeAdView.render(plugin.webView.getContext(), nativeAd);
        int adHeight = (int)AdBase.pxFromDp(plugin.webView.getContext(), 290f);

        View view = plugin.webView.getView();
        ViewGroup wvParentView = (ViewGroup) view.getParent();
        if (parentView == null) {
            parentView = new FrameLayout(plugin.webView.getContext());
        }
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, adHeight
        );
        if (wvParentView != null && wvParentView != parentView) {
            wvParentView.removeView(view);

            parentView.addView(view);
            wvParentView.addView(parentView);
        }

        params.setMargins(0, (int) position.optDouble("top"), 0, 0);

        nativeAdView.setLayoutParams(params);

        parentView.addView(nativeAdView);
        parentView.bringToFront();
        parentView.requestLayout();
        parentView.requestFocus();
    }
}
