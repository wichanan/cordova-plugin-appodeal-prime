class APRewardVideo: APBase, APDRewardedVideoDelegate {

    func show() {
        Appodeal.showAd(.rewardedVideo, rootViewController: self.plugin.viewController)
        Appodeal.setAutocache(false, types:.rewardedVideo)
        
        Appodeal.setRewardedVideoDelegate(self as? AppodealRewardedVideoDelegate)
    }

    // Method called if rewarded video mediation failed
    func rewardedVideoDidFailToLoadAd() {
        NSLog("rewared video did fail to load ad")
    }
    
    // Method called if rewarded mediation was successful, but ready ad network can't show ad or
    // ad presentation was too frequent according to your placement settings
    //
    // - Parameter error: Error object that indicates error reason
    func rewardedVideoDidFailToPresentWithError(_ error: Error) {
        print("rewared video did fail to present with error: " + error.localizedDescription)
    }
    
    // Method called after rewarded video start displaying
    func rewardedVideoDidPresent() {
        NSLog("rewared video did present")
    }
    
    // Method called before rewarded video leaves screen
    //
    // - Parameter wasFullyWatched: boolean flag indicated that user watch video fully
    func rewardedVideoWillDismissAndWasFullyWatched(_ wasFullyWatched: Bool) {
        NSLog("rewared video will dismiss and fully watched")
    }
    
    //  Method called after fully watch of video
    //
    // - Warning: After call this method rewarded video can stay on screen and show postbanner
    // - Parameters:
    //   - rewardAmount: Amount of app curency tuned via Appodeal Dashboard
    //   - rewardName: Name of app currency tuned via Appodeal Dashboard
    func rewardedVideoDidFinish(_ rewardAmount: Float, name rewardName: String?) {
        NSLog("rewared video did finish")
    }
    
    // Method is called when rewarded video is clicked
    func rewardedVideoDidClick() {
        NSLog("rewared video did click")
    }
    
    // Method called when rewardedVideo did expire and can not be shown
    func rewardedVideoDidExpired(){
        NSLog("rewared video did expire")
    }
}
