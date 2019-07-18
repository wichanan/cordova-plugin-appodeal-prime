package com.appodeal;

import com.appodeal.ads.AdBase;

import org.json.JSONArray;
import org.json.JSONObject;

public class Action {
    public JSONObject opts;

    Action(JSONArray args) {
        this.opts = args.optJSONObject(0);
    }

    public int optId() {
        return this.opts.optInt("id");
    }

    public JSONObject optPosition() {
        return this.opts.optJSONObject("position");
    }

    public AdBase getAd() {
        return AdBase.getAd(optId());
    }

    public String getPlacementID() {
        return this.opts.optString("placementID");
    }
}
