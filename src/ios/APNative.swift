class APNative: APBase, APDNativeAdQueueDelegate, APDNativeAdPresentationDelegate{
    var adQueue: APDNativeAdQueue!
    var nativeArray: [APDNativeAd] = []
    var nativeAdView: UIView!
    var nativeAd: APDNativeAd!

    var view: UIView {
        return self.plugin.viewController.view
    }
    
    deinit {
        nativeAdView = nil
        nativeAd = nil
    }

    func load () {
        adQueue = APDNativeAdQueue()
        adQueue.settings.type = .auto
        adQueue.settings.autocacheMask = [.media, .icon]
        adQueue.delegate = self
        adQueue.loadAd()
    }

    func show(_ position: NSDictionary) {
        if nativeArray.count > 0 {
            nativeAd = nativeArray.randomElement()
            nativeAd.delegate = self
            nativeAdView = UIView()
            do {
                let adView = try nativeAd.getViewForPlacement("default", withRootViewController: plugin.viewController)
                
                let size: CGSize = view.bounds.size
                let yOffset: CGFloat = position.value(forKey: "top") as! CGFloat
                self.nativeAdView.frame = CGRect(x: 0, y: yOffset, width: size.width, height: 420)
                
                adView.frame = nativeAdView.bounds
                nativeAdView.addSubview(adView)
                view.addSubview(self.nativeAdView)
            } catch {
                print("error")
            }
        }
    }
    
    func hide() {
        if (self.nativeAdView?.superview != nil) {
            self.nativeAd.delegate = nil
            self.nativeAd = nil
            self.nativeAdView.removeFromSuperview()
            self.nativeAdView = nil
        }
    }
        
    func nativeAdWillLogImpression(_ nativeAd: APDNativeAd) {
        NSLog("native will log impression")
        plugin.emit(eventType: APEvents.nativeShow)
    }
    
    func nativeAdWillLogUserInteraction(_ nativeAd: APDNativeAd) {
        NSLog("native will log user interaction")
        plugin.emit(eventType: APEvents.nativeClick)
    }
    
    func adQueue(_ adQueue: APDNativeAdQueue, failedWithError error: Error) {
        print("adqueue failed with error " + error.localizedDescription)
        plugin.emit(eventType: APEvents.nativeLoadFail)
    }
    
    func adQueueAdIsAvailable(_ adQueue: APDNativeAdQueue, ofCount count: UInt) {
        NSLog("adqueue is available")
        nativeArray = []
        nativeArray.append(contentsOf: adQueue.getNativeAds(ofCount: 2))
        plugin.emit(eventType: APEvents.nativeLoad)
    }
}
