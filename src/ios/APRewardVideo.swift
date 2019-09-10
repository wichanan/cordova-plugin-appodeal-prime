class APRewardVideo: APBase, AppodealRewardedVideoDelegate {
    func show() {
        Appodeal.showAd(.rewardedVideo, rootViewController: self.plugin.viewController)
        Appodeal.setAutocache(false, types:.rewardedVideo)
        Appodeal.setRewardedVideoDelegate(self)
    }

    // Method called when rewarded video loads
    //
    // - Parameter precache: If precache is YES it means that precached ad loaded
    func rewardedVideoDidLoadAdIsPrecache(_ precache: Bool) {
        plugin.emit(eventType: APEvents.rewardVideoLoad)
    }

    // Method called if rewarded video mediation failed
    func rewardedVideoDidFailToLoadAd() {
        NSLog("rewared video did fail to load ad")
        plugin.emit(eventType: APEvents.rewardVideoLoadFail)
    }
    
    // Method called if rewarded mediation was successful, but ready ad network can't show ad or
    // ad presentation was too frequent according to your placement settings
    //
    // - Parameter error: Error object that indicates error reason
    func rewardedVideoDidFailToPresentWithError(_ error: Error) {
        print("rewared video did fail to present with error: " + error.localizedDescription)
        plugin.emit(eventType: APEvents.rewardVideoShowFailed)
    }
    
    // Method called after rewarded video start displaying
    func rewardedVideoDidPresent() {
        NSLog("rewared video did present")
        plugin.emit(eventType: APEvents.rewardVideoShow)
    }
    
    // Method called before rewarded video leaves screen
    //
    // - Parameter wasFullyWatched: boolean flag indicated that user watch video fully
    func rewardedVideoWillDismissAndWasFullyWatched(_ wasFullyWatched: Bool) {
        if (plugin.originalHeight != nil){
            plugin.webView.frame = CGRect(
                x: plugin.webView.bounds.origin.x,
                y: plugin.webView.bounds.origin.y,
                width: plugin.webView.bounds.width,
                height: plugin.originalHeight)
        }
        NSLog("rewared video will dismiss and fully watched")
        plugin.emit(eventType: APEvents.rewardVideoClose)
    }
    
    //  Method called after fully watch of video
    //
    // - Warning: After call this method rewarded video can stay on screen and show postbanner
    // - Parameters:
    //   - rewardAmount: Amount of app curency tuned via Appodeal Dashboard
    //   - rewardName: Name of app currency tuned via Appodeal Dashboard
    func rewardedVideoDidFinish(_ rewardAmount: Float, name rewardName: String?) {
        NSLog("rewared video did finish")
        plugin.emit(eventType: APEvents.rewardVideoComplete)
    }
    
    // Method is called when rewarded video is clicked
    func rewardedVideoDidClick() {
        NSLog("rewared video did click")
        plugin.emit(eventType: APEvents.rewardVideoClick)
    }
    
    // Method called when rewardedVideo did expire and can not be shown
    func rewardedVideoDidExpired(){
        NSLog("rewared video did expire")
    }
}
