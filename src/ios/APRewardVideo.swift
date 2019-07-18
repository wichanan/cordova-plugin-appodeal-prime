class APRewardVideo: APBase {
    var rewardedVideoAd: FBRewardedVideoAd!

    deinit {
        rewardedVideoAd = nil
    }

    func load() {
        self.rewardedVideoAd = FBRewardedVideoAd(placementID: placementID)
        self.rewardedVideoAd!.delegate = self
        self.rewardedVideoAd!.load()
    }

    func show () {
        if (rewardedVideoAd != nil) {
            rewardedVideoAd.show(fromRootViewController: plugin.viewController)
        }
    }

    func rewardedVideoAdDidLoad(_ rewardedVideoAd: FBRewardedVideoAd) {
        if (rewardedVideoAd.isAdValid) {
            rewardedVideoAd.show(fromRootViewController: plugin.viewController)
        }
    }

    func rewardedVideoAdDidClick(_ rewardedVideoAd: FBRewardedVideoAd) {
        plugin.emit(eventType: FBANEvents.rewardVideoClick)
    }
    
    func rewardedVideoAdDidClose(_ rewardedVideoAd: FBRewardedVideoAd) {
        plugin.emit(eventType: FBANEvents.rewardVideoClose)
    }
    
    func rewardedVideoAdVideoComplete(_ rewardedVideoAd: FBRewardedVideoAd) {
        plugin.emit(eventType: FBANEvents.rewardVideoComplete)
    }
    
    func rewardedVideoAdWillClose(_ rewardedVideoAd: FBRewardedVideoAd) {
        plugin.emit(eventType: FBANEvents.rewardVideoWillClose)
    }
    
    func rewardedVideoAdWillLogImpression(_ rewardedVideoAd: FBRewardedVideoAd) {
        plugin.emit(eventType: FBANEvents.rewardVideoImpression)
    }
    
    func rewardedVideoAd(_ rewardedVideoAd: FBRewardedVideoAd, didFailWithError error: Error) {
        print("Error showing interstitial ad with: " + error.localizedDescription)
        plugin.emit(eventType: FBANEvents.rewardVideoLoadFail, data: error)
    }

    func rewardedVideoAdServerRewardDidFail(_ rewardedVideoAd: FBRewardedVideoAd) {
        plugin.emit(eventType: FBANEvents.rewardVideoServerFail)
    }

    func rewardedVideoAdServerRewardDidSucceed(_ rewardedVideoAd: FBRewardedVideoAd) {
        plugin.emit(eventType: FBANEvents.rewardVideoServerSuccess)
    }
}
