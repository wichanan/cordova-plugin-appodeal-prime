# Cordova Plugin Appodeal Prime

## Warning the iOS podfiles of Appodeal has the size of 1.2GB please use a decent internet connection to install this plugin

## Installation

```
cordova plugin add cordova-appodeal-prime --variable ANDROID_APP_KEY=xxxx-your-android-api-key-xxxx --variable IOS_APP_KEY=xxxx-your-ios-api-key-xxxx
```

## Prerequisite(iOS)
This Plugin uses `cordova-plugin-cocoapod-support` to generate the Podfile to match the min ios version in pod file.
You must check at `yourprojectDir/config.xml` before using this plugin.

add this

```xml
<platform name="ios">
    ...
    <preference name="pods_ios_min_version" value="11.0"/>
    ...
</platform>
```

## Prerequisite(Android)

Minimum Android SdkVersion=21

The Appodeal SDK requires the `com.android.support:recyclerview-v7` suppoort library. Before the installation 
please check the version of your project `com.android.support libraries` version and add another `--variable` to the `cordova plugin add`

The default uses `27+`


```
cordova plugin add cordova-plugin-fbanfree --variable ANDROID_SUPPORT_RECYCLERVIEW_VERSION=20+ --variable ANDROID_APP_KEY=xxxx --variable IOS_APP_KEY=xxxx
```

And just like the iOS Version the Android version needs the `config.xml` modification as well.
The plugin uses `cordova-custom-config` to add the `http://schemas.android.com/tools` to the Manifest file, to do that
you will need to add the following line to your app `config.xml`.


```xml
<platform name="android">
    ...
    <custom-preference name="android-manifest/@xmlns:tools" value="http://schemas.android.com/tools" />
    ...
</platform>
```


## Usage


```javascript
// initialize the Appodeal prime after the cordova device ready (do not use the ready function in the document.ready.
// because the app initialization time will disgustingly increase).
onDeviceReady: function() {
    this.receivedEvent('deviceready');
    ...
    appodealprime.ready();
}


// show banner
appodealprime.showBanner()
then(res => {
    console.log('Banner shown');
})
.catch(err => {
    console.log('Error showing banner');
});

// show native ad (Can only add top position modification and can only show 1 native ad right now).

// the load native ad can be at anywhere in the app but should be in the same page as the show native.
// if the ads got loaded too much without showing the Appodeal ad bidding will decrease the ad loading for your app.
appodealprime.loadNative()
then(res => {
    console.log('Native loaded');
})
.catch(err => {
    console.log('error loading native ad');
});

const data = {
    position: {
        top: 220
    }
}
appodealprime.showNative(data)
then(res => {
    console.log('Native shown');
})
.catch(err => {
    console.log('error showing native');
});

// show Interstitial (No need to load the interstitial because 
// the Appodeal will do all the loading in ready() function).
appodealprime.showInterstitial('YOUR_PLACEMENT_ID')
.then(res => {
   console.log('interstitial show success:');
})
.catch(err => {
   console.log('error showing interstitial:');
});

// show Rewarded video ad.
appodealprime.showRewardedVideo('YOUR_PLACEMENT_ID')
.then(res => {
   console.log('rewarded video show success:');
})
.catch(err => {
   console.log('error showing rewarded video:');
});

// To get the Ad Event Listener you must implement the document.addEventListener('event name', () =>{}) like this

document.addEventListener('appodeal.interstitial.close', () => {
  console.log('interstitial close listener');
  // your code here
});

// here is the list of events for appodeal ads
"appodeal.banner.click"
"appodeal.banner.load_fail"
"appodeal.banner.load"
"appodeal.banner.show"
"appodeal.interstitial.click"
"appodeal.interstitial.show"
"appodeal.interstitial.close"
"appodeal.interstitial.show_fail"
"appodeal.interstitial.load"
"appodeal.interstitial.load_fail"
"appodeal.interstitial.will_close"
"appodeal.reward_video.click"
"appodeal.reward_video.complete"
"appodeal.reward_video.show_fail"
"appodeal.reward_video.show"
"appodeal.reward_video.load"
"appodeal.reward_video.close"
"appodeal.reward_video.load_fail"
"appodeal.native.click"
"appodeal.native.show"
"appodeal.native.load_fail"
"appodeal.native.load"
```

