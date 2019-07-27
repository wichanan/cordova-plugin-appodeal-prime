class APNative: APBase, APDNativeAdQueueDelegate, APDNativeAdPresentationDelegate{
    var adQueue: APDNativeAdQueue!

    var view: UIView {
        return self.plugin.viewController.view
    }

    init(id: Int) {
        super.init(id: id)
        self.position = position
    }

    func load () {
        adQueue.settings.type = .auto
        adQueue.settings.autocacheMask = [.media, .icon]
        adQueue.delegate = self
        adQueue.loadAd()
    }
//
    func show(_ position: NSDictionary) {
        //
    }
//
//    func hide() {
//        if (self.nativeAdView?.superview != nil) {
//            self.nativeAd.delegate = nil
//            self.nativeAdView.removeFromSuperview()
//        }
//    }


//    func showNativeAd() {
//        if (self.nativeAd != nil && self.nativeAd!.isAdValid) {
//            self.nativeAdView = FBNativeAdView(nativeAd: self.nativeAd!, with: self.adViewType)
//
//            view.addSubview(self.nativeAdView)
//
//            let size: CGSize = plugin.viewController.view.bounds.size
//            let yOffset: CGFloat = self.position?.value(forKey: "top") as! CGFloat
//            self.nativeAdView.frame = CGRect(x: 0, y: yOffset, width: size.width, height: 420)
//        }
//    }

        
    func nativeAdWillLogImpression(_ nativeAd: APDNativeAd) {
        NSLog("native will log impression")
    }
    
    func nativeAdWillLogUserInteraction(_ nativeAd: APDNativeAd) {
        NSLog("native will log user interaction")
    }
    
    func adQueue(_ adQueue: APDNativeAdQueue, failedWithError error: Error) {
        print("ad queue failed with error " + error.localizedDescription)
    }
    
    func adQueueAdIsAvailable(_ adQueue: APDNativeAdQueue, ofCount count: UInt) {
        NSLog("adqueue is available")
        dump(adQueue)
    }
}
