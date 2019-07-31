var AppodealPrime = exports;

var exec = require('cordova/exec');
var cordova = require('cordova');

/**
 * @ignore
 */
function execute(method, args) {
    return new Promise((resolve, reject) => {
        exec(function(event) {
            resolve(event);
        }, function(err) {
            reject(err);
        }, 'AppodealPrime', method, [args]);
    });
}

function fireDocumentEvent(eventName, data) {
    if (data === void 0) { data = null; }
    var event = new CustomEvent(eventName, { detail: data });
    document.dispatchEvent(event);
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

function adConfig(adType) {
    return {
        id: getAdUnitId(adType)
    }
}

function nativeConfig(data) {
    return {
        position: data.position,
        id: getAdUnitId('NATIVE')
    }
}

AppodealPrime.pluginVersion = '0.0.1';

AppodealPrime.ready = function() {
    return new Promise((resolve, reject) => {
        exec(function(event) {
            fireDocumentEvent(event.type, event.data);
            resolve(event.data);
        }, function(err) {
            reject(err);
        }, 'AppodealPrime', 'ready');
    });
};

AppodealPrime.showBanner = function() {
    return execute('banner_show', adConfig('BANNER_BOTTOM'));
};

AppodealPrime.hideBanner = function() {
    return execute('banner_hide', adConfig('BANNER_BOTTOM'));
}

AppodealPrime.loadNative = function() {
    return execute('native_load', adConfig('NATIVE'));
}

AppodealPrime.showNative = function(data) {
    return execute('native_show', nativeConfig(data));
}

AppodealPrime.hideNative = function() {
    return execute('native_hide', adConfig('NATIVE'));
}

AppodealPrime.showInterstitial = function() {
    return execute('interstitial_show', adConfig('INTERSTITIAL'));
}

AppodealPrime.showRewardVideo = function() {
    return execute('reward_video_show', adConfig('REWARD_VIDEO'));
}