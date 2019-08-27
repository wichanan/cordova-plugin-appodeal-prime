package com.appodealprime.ads;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.BannerView;
import com.appodeal.ads.Native;
import com.appodeal.ads.NativeAd;
import com.appodeal.ads.NativeAdView;
import com.appodeal.ads.NativeCallbacks;
import com.appodeal.ads.NativeMediaView;
import com.appodeal.ads.native_ad.views.NativeAdViewContentStream;
import com.appodealprime.Action;
import com.appodealprime.AppodealPrime;

public class APNativeAd extends AdBase {
    private static final String TAG = "AP::NativeADs";
    private NativeAd nativeAd;
    private NativeAdView nativeAdView;
    private ViewGroup parentView;
    private JSONObject position;

    private List<com.appodeal.ads.NativeAd> loadedNativeads;

    APNativeAd(int id) {
        super(id);
        Appodeal.cache(plugin.cordova.getActivity(), Appodeal.NATIVE);
    }

    public static boolean executeNativeLoadAction(Action action, CallbackContext callbackContext) {
        plugin.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                APNativeAd nativeAd = (APNativeAd) action.getAd();
                if (nativeAd == null) {
                    nativeAd = new APNativeAd(
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

    public static boolean executeNativeShowAction(Action action, CallbackContext callbackContext) {
        plugin.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                APNativeAd nativeAd = (APNativeAd) action.getAd();
                if (nativeAd == null) {
                    nativeAd = new APNativeAd(
                            action.optId()
                    );
                }
                nativeAd.position = action.optPosition();
                nativeAd.show();
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
                APNativeAd APNativeAd = (APNativeAd) action.getAd();
                if (APNativeAd != null) {
                    APNativeAd.hide();
                }

                PluginResult result = new PluginResult(PluginResult.Status.OK, "");
                callbackContext.sendPluginResult(result);
            }
        });

        return true;
    }

    public void hide() {
        if (nativeAdView != null) {
            nativeAdView.unregisterViewForInteraction();
            nativeAdView.destroy();

            nativeAd.destroy();
            View view = plugin.webView.getView();
            ViewGroup wvParentView = (ViewGroup) view.getParent();
            int countwv = wvParentView.getChildCount();
            for (int i = 0; i< countwv; i++) {
                View v = wvParentView.getChildAt(i);
                if (v instanceof NativeAdView) {
                    wvParentView.removeView(v);
                }
            }
        }
    }

    public void load() {
        Appodeal.setRequiredNativeMediaAssetType(Native.MediaAssetType.ALL);
        Appodeal.setNativeAdType(Native.NativeAdType.Auto);
        Appodeal.initialize(
                plugin.cordova.getActivity(),
                AppodealPrime.apiKey,
                Appodeal.NATIVE,
                true);
        Appodeal.setNativeCallbacks(new NativeCallbacks() {
            @Override
            public void onNativeLoaded() {
                Log.d(TAG, "Native loaded");
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

        Appodeal.cache(plugin.cordova.getActivity(), Appodeal.NATIVE, 2);
    }

    public void show() {
        loadedNativeads = Appodeal.getNativeAds(Appodeal.getAvailableNativeAdsCount());
        if (loadedNativeads.isEmpty()) {
            return;
        }
        if (nativeAdView == null) {
            nativeAd = loadedNativeads.get(new Random().nextInt(loadedNativeads.size()));
        } else {
            View view = plugin.webView.getView();
            ViewGroup wvParentView = (ViewGroup) view.getParent();
            if (parentView != wvParentView) {
                parentView.removeAllViews();
                if (parentView.getParent() != null) {
                    ((ViewGroup)parentView.getParent()).removeView(parentView);
                }
            }
            nativeAd = loadedNativeads.get(new Random().nextInt(loadedNativeads.size()));

        }

        addNativeView(nativeAd);
    }

    private void addNativeView(NativeAd nativeAd) {
        nativeAdView = new NativeAdViewContentStream(plugin.cordova.getActivity(), nativeAd);
        nativeAdView.registerView(nativeAd);;
        nativeAdView.setVisibility(View.VISIBLE);
        int adHeight = (int)AdBase.pxFromDp(plugin.webView.getContext(), 320f);

        View view = plugin.webView.getView();
        ViewGroup wvParentView = (ViewGroup) view.getParent();
        if (parentView == null) {
            parentView = new FrameLayout(plugin.webView.getContext());
        }

        BannerView banner = getShownBannerView();
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, adHeight
        );
        if (wvParentView != null && wvParentView != parentView) {
            wvParentView.removeView(view);

            parentView.addView(view);
            wvParentView.addView(parentView);
        }

        params.setMargins(0, (int) position.optDouble("top"), 0, 0);
        if (banner != null) {
            parentView.addView(banner);
        }

        nativeAdView.setLayoutParams(params);

        parentView.addView(nativeAdView);
        parentView.bringToFront();
        parentView.requestLayout();
        parentView.requestFocus();
        nativeAdView.bringToFront();
    }

    private BannerView getShownBannerView() {
        View view = plugin.webView.getView();
        ViewGroup wvParentView = (ViewGroup) view.getParent();
        BannerView bannerView = null;

        int count = wvParentView.getChildCount();
        ArrayList<View> views = new ArrayList<View>();
        for (int i = 0; i<count; i++) {
            View v = wvParentView.getChildAt(i);
            if (v instanceof BannerView) {
                bannerView = (BannerView) v;
                wvParentView.removeView(v);
            }
            if (v == null) {
                wvParentView.removeView(v);
            }
        }

        return bannerView;
    }
}
