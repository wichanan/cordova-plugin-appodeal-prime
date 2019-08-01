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
import com.appodealprime.Action;
import com.appodealprime.Events;

public class BannerAd extends AdBase {
    private static final String TAG = "AppodealPrime::BannerAD";
    private ViewGroup parentView;
    private BannerView bannerView;

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
                Log.d(TAG, "hide action triggered");
                BannerAd bannerAd = (BannerAd) action.getAd();
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
        Appodeal.hide(plugin.cordova.getActivity(), Appodeal.BANNER_BOTTOM);
    }

    public void show() {
//        bannerView = Appodeal.getBannerView(plugin.cordova.getActivity());
        Appodeal.show(plugin.cordova.getActivity(), Appodeal.BANNER_BOTTOM);
        adjustViewHeight();

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
    
    private void adjustViewHeight() {
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

            parentView.addView(view);
            wvParentView.addView(parentView);
        }
        view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, adPosition));
    }
}
