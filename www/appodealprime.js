var AppodealPrime = exports;

var exec = require('cordova/exec');
var cordova = require('cordova');

/**
 * @ignore
 */
function execute(method, args) {
    return new Promise((resolve, reject) => {
        exec(resolve, reject, 'FBANFree', method, [args])
    });
}

var nextId = 100
var adUnits = {}

function getAdUnitId(adUnitId) {
    if (adUnits[adUnitId]) {
      return adUnits[adUnitId]
    }
    adUnits[adUnitId] = nextId
    nextId += 1
    return adUnits[adUnitId]
}

function bannerConfig(placementID) {
    return {
        placementID: placementID,
        adSize: 1,
        position: 'bottom',
        id: getAdUnitId(placementID)
    }
}

function adConfig(placementID) {
    return {
        placementID: placementID,
        id: getAdUnitId(placementID)
    }
}

function nativeConfig(data) {
    return {
        position: data.position,
        placementID: data.placementID,
        id: getAdUnitId(data.placementID)
    }
}

AppodealPrime.INTERSTITIAL = 3;
AppodealPrime.BANNER = 4;
AppodealPrime.BANNER_BOTTOM = 8;
AppodealPrime.BANNER_TOP = 16;
AppodealPrime.REWARDED_VIDEO = 128;
AppodealPrime.NON_SKIPPABLE_VIDEO = 256;

AppodealPrime.BANNER_X_SMART = 0;
AppodealPrime.BANNER_X_CENTER = 1;
AppodealPrime.BANNER_X_LEFT = 2;
AppodealPrime.BANNER_X_RIGHT = 3;

AppodealPrime.LogLevel = {
    NONE: 0,
    DEBUG: 1,
    VERBOSE: 2
};

AppodealPrime.Gender = {
    OTHER: 0,
    MALE: 1,
    FEMALE: 2
};

AppodealPrime.pluginVersion = '0.0.1';

AppodealPrime.initialize = function(appKey, adType) {
    exec(null, null, "AppodealPlugin", "setPluginVersion", [AppodealPrime.pluginVersion]);
    exec(null, null, "AppodealPlugin", "initialize", [appKey, adType]);
};

AppodealPrime.show = function(adType, callback) {
    exec(callback, null, "AppodealPlugin", "show", [adType]);
};

AppodealPrime.showWithPlacement = function(adType, placement, callback) {
    exec(callback, null, "AppodealPlugin", "showWithPlacement", [adType, placement]);
};

AppodealPrime.showBannerView = function(xAxis, yAxis, placement) {
    exec(null, null, "AppodealPlugin", "showBannerView", [xAxis, yAxis, placement]);
};

AppodealPrime.isLoaded = function(adType, callback) {
    exec(callback, null, "AppodealPlugin", "isLoaded", [adType]);
};

AppodealPrime.cache = function(adType) {
    exec(null, null, "AppodealPlugin", "cache", [adType]);
};

AppodealPrime.hide = function(adType) {
    exec(null, null, "AppodealPlugin", "hide", [adType]);
};

AppodealPrime.destroy = function(adType) {
    exec(null, null, "AppodealPlugin", "destroy", [adType]);
}

AppodealPrime.setAutoCache = function(adType, autoCache) {
    exec(null, null, "AppodealPlugin", "setAutoCache", [adType, autoCache]);
};

AppodealPrime.isPrecache = function(adType, callback) {
    exec(callback, null, "AppodealPlugin", "isPrecache", [adType]);
};

AppodealPrime.setBannerBackground = function(value) {
    exec(null, null, "AppodealPlugin", "setBannerBackground", [value]);
};

AppodealPrime.setBannerAnimation = function(value) {
    exec(null, null, "AppodealPlugin", "setBannerAnimation", [value]);
};

AppodealPrime.setSmartBanners = function(value) {
    exec(null, null, "AppodealPlugin", "setSmartBanners", [value]);
};

AppodealPrime.set728x90Banners = function(value) {
    exec(null, null, "AppodealPlugin", "set728x90Banners", [value]);
};

AppodealPrime.setBannerOverLap = function(value) {
    exec(null, null, "AppodealPlugin", "setBannerOverLap", [value]);
};

AppodealPrime.setTesting = function(testing) {
    exec(null, null, "AppodealPlugin", "setTesting", [testing]);
};

AppodealPrime.setLogLevel = function(loglevel) {
    exec(null, null, "AppodealPlugin", "setLogLevel", [loglevel]);
};

AppodealPrime.setChildDirectedTreatment = function(value) {
    exec(null, null, "AppodealPlugin", "setChildDirectedTreatment", [value]);
};

AppodealPrime.setTriggerOnLoadedOnPrecache = function(set) {
    exec(null, null, "AppodealPlugin", "setOnLoadedTriggerBoth", [set]);
};

AppodealPrime.disableNetwork = function(network, adType) {
    exec(null, null, "AppodealPlugin", "disableNetwork", [network]);
};

AppodealPrime.disableNetworkType = function(network, adType) {
    exec(null, null, "AppodealPlugin", "disableNetworkType", [network, adType]);
};

AppodealPrime.disableLocationPermissionCheck = function() {
    exec(null, null, "AppodealPlugin", "disableLocationPermissionCheck", []);
};

AppodealPrime.disableWriteExternalStoragePermissionCheck = function() {
    exec(null, null, "AppodealPlugin", "disableWriteExternalStoragePermissionCheck", []);
};

AppodealPrime.muteVideosIfCallsMuted = function(value) {
    exec(null, null, "AppodealPlugin", "muteVideosIfCallsMuted", [value]);
};

AppodealPrime.showTestScreen = function(value) {
    exec(null, null, "AppodealPlugin", "showTestScreen", []);
};

AppodealPrime.getVersion = function(callback) {
    exec(callback, null, "AppodealPlugin", "getVersion", []);
};

AppodealPrime.getPluginVersion = function(){
    return AppodealPrime.pluginVersion;
};

AppodealPrime.isInitialized = function(callback) {
    exec(callback, null, "AppodealPlugin", "isInitalized", []);
};

AppodealPrime.canShow = function(adType, callback) {
    exec(callback, null, "AppodealPlugin", "canShow", [adType]);
};

AppodealPrime.canShowWithPlacement = function(adType, placement, callback) {
    exec(callback, null, "AppodealPlugin", "canShowWithPlacement", [adType, placement]);
};

AppodealPrime.setCustomBooleanRule = function(name, rule) {
    exec(null, null, "AppodealPlugin", "setCustomBooleanRule", [name, rule]);
};

AppodealPrime.setCustomIntegerRule = function(name, rule) {
    exec(null, null, "AppodealPlugin", "setCustomIntegerRule", [name, rule]);
};

AppodealPrime.setCustomDoubleRule = function(name, rule) {
    exec(null, null, "AppodealPlugin", "setCustomDoubleRule", [name, rule]);
};

AppodealPrime.setCustomStringRule = function(name, rule) {
    exec(null, null, "AppodealPlugin", "setCustomStringRule", [name, rule]);
};

AppodealPrime.getRewardParameters = function(callback) {
    exec(callback, null, "AppodealPlugin", "getRewardParameters", []);
};

AppodealPrime.getRewardParametersForPlacement = function(placement, callback) {
    exec(callback, null, "AppodealPlugin", "getRewardParametersForPlacement", [placement]);
};

AppodealPrime.setAge = function(age) {
    exec(null, null, "AppodealPlugin", "setAge", [age]);
};

AppodealPrime.setGender = function(gender) {
    exec(null, null, "AppodealPlugin", "setGender", [gender]);
};

AppodealPrime.setUserId = function(userid){
    exec(null, null, "AppodealPlugin", "setUserId", [userid]);
};

AppodealPrime.trackInAppPurchase = function(amount, currency){
    exec(null, null, "AppodealPlugin", "trackInAppPurchase", [amount, currency]);
};

AppodealPrime.setInterstitialCallbacks = function(callback) {
    exec(callback, null, "AppodealPlugin", "setInterstitialCallbacks", [])
};

AppodealPrime.setNonSkippableVideoCallbacks = function(callbacks) {
    exec(callbacks, null, "AppodealPlugin", "setNonSkippableVideoCallbacks", []);
};

AppodealPrime.setRewardedVideoCallbacks = function(callbacks) {
    exec(callbacks, null, "AppodealPlugin", "setRewardedVideoCallbacks", []);
};

AppodealPrime.setBannerCallbacks = function(callbacks) {
    exec(callbacks, null, "AppodealPlugin", "setBannerCallbacks", []);
};