class APNativeBanner: APBase {
    var nativeBannerAd: FBNativeBannerAd!
    var position: NSDictionary!
    var adviewType: Int!
    
    var view: UIView {
        return self.plugin.viewController.view
    }
    
    init(id: Int, placementID: String, position: NSDictionary, adviewType: Int) {
        super.init(id: id, placementID: placementID)
        
        self.position = position
        self.adviewType = adviewType
    }
    
    deinit {
        self.nativeBannerAd = nil
    }
    
    func show() {
        self.nativeBannerAd = FBNativeBannerAd(placementID: placementID)
        self.nativeBannerAd?.delegate = self
        self.nativeBannerAd?.loadAd()
    }
    
    func nativeBannerAdDidLoad(_ nativeBannerAd: FBNativeBannerAd) {
        showNativeBanner()
    }
    
    func showNativeBanner() {
        if (self.nativeBannerAd != nil && self.nativeBannerAd!.isAdValid) {
            let adView = FBNativeBannerAdView(nativeBannerAd: self.nativeBannerAd, with: FBNativeBannerAdViewType(rawValue: self.adviewType!)!)
            
            plugin.viewController.view.addSubview(adView)
            
            let size: CGSize = plugin.viewController.view.bounds.size
            adView.frame = CGRect(x: 0, y: 20, width: size.width, height: getNativeBannerHeight())
        }
    }
    
    private func getNativeBannerHeight() -> CGFloat {
        switch self.adviewType {
        case 1:
            return 100
        case 2:
            return 120
        case 5:
            return 50
        default: break
        }
        
        return 50
    }
    
    func nativeBannerAdDidClick(_ nativeBannerAd: FBNativeBannerAd) {
        plugin.emit(eventType: FBANEvents.nativeBannerClick)
    }
    
    func nativeBannerAdDidFinishHandlingClick(_ nativeBannerAd: FBNativeBannerAd) {
        plugin.emit(eventType: FBANEvents.nativeBannerClickFinish)
    }
    
    func nativeBannerAdWillLogImpression(_ nativeBannerAd: FBNativeBannerAd) {
        plugin.emit(eventType: FBANEvents.nativeBannerImpression)
    }
    
    func nativeBannerAd(_ nativeBannerAd: FBNativeBannerAd, didFailWithError error: Error) {
        print("Error loadin native banner ad with: " + error.localizedDescription)
        plugin.emit(eventType: FBANEvents.nativeBannerLoadFail, data: error)
    }
}
