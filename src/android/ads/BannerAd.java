package com.appodealprime.ads;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import java.util.ArrayList;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.BannerCallbacks;
import com.appodeal.ads.BannerView;
import com.appodeal.ads.NativeAdView;
import com.appodealprime.Action;
import com.appodealprime.Events;

public class BannerAd extends AdBase {
    private static final String TAG = "AppodealPrime::BannerAD";
    private ViewGroup parentView;
    private BannerView bannerView;
    public static Boolean isBannerShowing = false;

    BannerAd(int id) {
        super(id);

    }

    public static boolean executeShowAction(Action action, CallbackContext callbackContext) {
        plugin.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                BannerAd bannerAd = (BannerAd) action.getAd();
                if (bannerAd == null) {
                    bannerAd = new BannerAd(
                            action.optId()
                    );
                }
                isBannerShowing = true;
                bannerAd.show();
                PluginResult result = new PluginResult(PluginResult.Status.OK, "");
                callbackContext.sendPluginResult(result);
            }
        });

        return true;
    }

    public static boolean executeHideAction(Action action, CallbackContext callbackContext) {
        plugin.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BannerAd bannerAd = (BannerAd) action.getAd();
                isBannerShowing = false;
                if (bannerAd != null) {
                    bannerAd.hide(true);
                }
                PluginResult result = new PluginResult(PluginResult.Status.OK, "");
                callbackContext.sendPluginResult(result);
            }
        });

        return true;
    }

    public void hide(boolean withParam) {
        if (bannerView != null) {
            Appodeal.hide(plugin.cordova.getActivity(), Appodeal.BANNER_VIEW);
            ((ViewGroup)bannerView.getParent()).removeView(bannerView);
            bannerView.removeAllViews();
            View view = plugin.webView.getView();
            if (view.getParent() != null) {
                ((ViewGroup)view.getParent()).removeView(view);
            }
            FrameLayout.LayoutParams webViewParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    parentView.getHeight()
            );

            if (withParam) {
                view.setLayoutParams(webViewParams);
            }

            parentView.addView(view);
            parentView.bringToFront();
            parentView.requestLayout();
            parentView.requestFocus();
        }
    }

    public void show() {
        if (bannerView == null) {
            bannerView = Appodeal.getBannerView(plugin.cordova.getActivity());
        }
        addBannerView();

        Appodeal.setBannerCallbacks(new BannerCallbacks() {
            @Override
            public void onBannerLoaded(int i, boolean b) {
                plugin.emit(Events.BANNER_LOAD);
            }

            @Override
            public void onBannerFailedToLoad() {
                Log.d(TAG, "Error loading banner");
                plugin.emit(Events.BANNER_LOAD_FAIL);
            }

            @Override
            public void onBannerShown() {
                Log.d(TAG, "Banner shown");
            }

            @Override
            public void onBannerClicked() {
                plugin.emit(Events.BANNER_CLICK);
            }

            @Override
            public void onBannerExpired() {
                Log.d(TAG, "Banner Expired");
            }
        });

    }

    @Override
    public void destroy() {
        Appodeal.destroy(Appodeal.BANNER_VIEW);

        super.destroy();
    }

    private void addBannerView() {
        View view = plugin.webView.getView();
        ViewGroup wvParentView = (ViewGroup) view.getParent();
        if (parentView == null) {
            parentView = new FrameLayout(plugin.webView.getContext());
        }
        float dip = 50f;
        float px = AdBase.pxFromDp(plugin.webView.getContext(), dip);
        int adPosition = wvParentView.getHeight() - (int)Math.floor(px);

        if (wvParentView != null && wvParentView != parentView) {
            wvParentView.removeView(view);
            parentView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            if (parentView.getParent() != null) {
                ((ViewGroup)parentView.getParent()).removeView(parentView);
            }
            parentView.addView(view);
            wvParentView.addView(parentView);
        }
        view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, adPosition));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, adPosition, 0, 0);
        bannerView.setLayoutParams(params);
        parentView.addView(bannerView);
        parentView.bringToFront();
        parentView.requestLayout();
        parentView.requestFocus();

        Appodeal.show(plugin.cordova.getActivity(), Appodeal.BANNER_VIEW);
    }
}
