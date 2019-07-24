var AppodealPrime = exports;

var exec = require('cordova/exec');
var cordova = require('cordova');

/**
 * @ignore
 */
function execute(method, args) {
    return new Promise((resolve, reject) => {
        exec(resolve, reject, 'AppodealPrime', method, [args])
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
    execute('ready', {});
};

AppodealPrime.showBanner = function() {
    execute('banner_show', adConfig('BANNER_BOTTOM'));
};

AppodealPrime.hideBanner = function() {
    execute('banner_hide', adConfig('BANNER_BOTTOM'));
}

AppodealPrime.loadNative = function() {
    execute('native_load', adConfig('NATIVE'));
}

AppodealPrime.showNative = function(data) {
    execute('native_show', nativeConfig(data));
}

AppodealPrime.showInterstitial = function() {
    execute('interstitial_show', adConfig('INTERSTITIAL'));
}

AppodealPrime.showRewardVideo = function() {
    execute('reward_video_show', adConfig('REWARD_VIDEO'));
}
