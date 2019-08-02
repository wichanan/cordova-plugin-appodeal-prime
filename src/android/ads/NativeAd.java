package com.appodealprime.ads;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.Native;
import com.appodeal.ads.NativeCallbacks;
import com.appodealprime.Action;
import com.appodealprime.Events;

public class NativeAd extends AdBase {
    private static final String TAG = "AP::NativeADs";
    private NativeAd nativeAd;
    private View nativeAdView;
    private ViewGroup parentView;
    private JSONObject position;

    private List<com.appodeal.ads.NativeAd> loadedNativeads;

    NativeAd(int id) {
        super(id);
        Appodeal.cache(plugin.cordova.getActivity(), Appodeal.NATIVE);
    }

    public static boolean executeNativeLoadAction(Action action, CallbackContext callbackContext) {
        plugin.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                NativeAd nativeAd = (NativeAd) action.getAd();
                if (nativeAd == null) {
                    nativeAd = new NativeAd(
                            action.optId()
                    );
                }
                nativeAd.load();
                PluginResult result = new PluginResult(PluginResult.Status.OK, "");
                callbackContext.sendPluginResult(result);
            }
        });

        return true;
    }

//    public static boolean executeNativeShowAction(Action action, CallbackContext callbackContext) {
//        plugin.cordova.getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//
//                FBNativeAd fbNativeAd = (FBNativeAd) action.getAd();
//                if (fbNativeAd == null) {
//                    fbNativeAd = new FBNativeAd(
//                            action.optId(),
//                            action.optPosition()
//                    );
//                }
//                this.position = action.optPosition()
//                fbNativeAd.show();
//                PluginResult result = new PluginResult(PluginResult.Status.OK, "");
//                callbackContext.sendPluginResult(result);
//            }
//        });
//
//        return true;
//    }
//
//    public static boolean executeNativeHideAction(Action action, CallbackContext callbackContext) {
//        plugin.cordova.getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                FBNativeAd fbNativeAd = (FBNativeAd) action.getAd();
//                if (fbNativeAd != null) {
//                    fbNativeAd.hide();
//                }
//
//                PluginResult result = new PluginResult(PluginResult.Status.OK, "");
//                callbackContext.sendPluginResult(result);
//            }
//        });
//
//        return true;
//    }
//
//    public static boolean executeNativeHideAllAction(Action action, CallbackContext callbackContext) {
//        plugin.cordova.getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                FBNativeAd fbNativeAd = (FBNativeAd) action.getAd();
//                if (fbNativeAd == null) {
//                    fbNativeAd = new FBNativeAd(
//                            action.optId(),
//                            "",
//                            new JSONObject()
//                    );
//                }
//                fbNativeAd.hideAll();
//                PluginResult result = new PluginResult(PluginResult.Status.OK, "");
//                callbackContext.sendPluginResult(result);
//            }
//        });
//
//        return true;
//    }
//
    public void hide() {
        
    }
//
//    public void hideAll() {
//        View view = plugin.webView.getView();
//        ViewGroup wvParentView = (ViewGroup) view.getParent();
//        int countwv = wvParentView.getChildCount();
//        for (int i = 0; i<countwv; i++) {
//            View v = wvParentView.getChildAt(i);
//            if (v instanceof NativeAdLayout) {
//                wvParentView.removeView(v);
//            }
//        }
//    }
//
    public void load() {
        Appodeal.setRequiredNativeMediaAssetType(Native.MediaAssetType.ALL);
        Appodeal.setNativeCallbacks(new NativeCallbacks() {
            @Override
            public void onNativeLoaded() {
                Log.d(TAG, "Native loaded");
                loadedNativeads = Appodeal.getNativeAds(1);
            }

            @Override
            public void onNativeFailedToLoad() {
                Log.d(TAG, "Native fail to load");
            }

            @Override
            public void onNativeShown(com.appodeal.ads.NativeAd nativeAd) {
                Log.d(TAG, "Native shown");
            }

            @Override
            public void onNativeClicked(com.appodeal.ads.NativeAd nativeAd) {
                Log.d(TAG, "Native clicked");
            }

            @Override
            public void onNativeExpired() {
                Log.d(TAG, "Native expired");
            }
        });
    }

//    public void show(JSONObject position) {
//        if (nativeAdView == null) {
//            nativeAd = new NativeAd(plugin.webView.getContext(), placementID);
//            addNativeView(nativeAd);
//        } else {
//            View view = plugin.webView.getView();
//            ViewGroup wvParentView = (ViewGroup) view.getParent();
//            if (parentView != wvParentView) {
//                parentView.removeAllViews();
//                if (parentView.getParent() != null) {
//                    ((ViewGroup)parentView.getParent()).removeView(parentView);
//                }
//            }
//            nativeAd = new NativeAd(plugin.webView.getContext(), placementID);
//            addNativeView(nativeAd);
//        }
//
//    }
//
//    private void addNativeView(NativeAd nativeAd) {
//        Log.d(TAG, "Trying native show");
//        nativeAdView = NativeAdView.render(plugin.webView.getContext(), nativeAd);
//        int adHeight = (int)AdBase.pxFromDp(plugin.webView.getContext(), 290f);
//
//        View view = plugin.webView.getView();
//        ViewGroup wvParentView = (ViewGroup) view.getParent();
//        if (parentView == null) {
//            parentView = new FrameLayout(plugin.webView.getContext());
//        }
//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, adHeight
//        );
//        if (wvParentView != null && wvParentView != parentView) {
//            wvParentView.removeView(view);
//
//            parentView.addView(view);
//            wvParentView.addView(parentView);
//        }
//
//        params.setMargins(0, (int) position.optDouble("top"), 0, 0);
//
//        nativeAdView.setLayoutParams(params);
//
//        parentView.addView(nativeAdView);
//        parentView.bringToFront();
//        parentView.requestLayout();
//        parentView.requestFocus();
//    }
}
