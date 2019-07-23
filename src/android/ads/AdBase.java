package com.appodealprime.ads;

import android.content.Context;
import android.util.SparseArray;

import org.json.JSONException;
import org.json.JSONObject;

import com.appodealprime.AppodealPrime;


public abstract class AdBase {
    protected static AppodealPrime plugin;

    final int id;

    private static SparseArray<AdBase> ads = new SparseArray<AdBase>();


    AdBase(int id) {
        this.id = id;

        ads.put(id, this);
    }

    public static void initialize(AppodealPrime plugin) {
        AdBase.plugin = plugin;
    }

    public static AdBase getAd(Integer id) {
        return ads.get(id);
    }

    JSONObject buildErrorPayload(int errorCode) {
        JSONObject data = new JSONObject();
        try {
            data.put("errorCode", errorCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    public void destroy() {
        ads.remove(id);
    }

    protected static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
