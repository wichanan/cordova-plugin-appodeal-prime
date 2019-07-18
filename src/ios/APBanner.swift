class APBanner: APBase {
    var adView: FBAdView!
    var adSize: FBAdSize!
    var position: String!
    
    var view: UIView {
        return self.plugin.viewController.view
    }
    
    init(id: Int, placementID: String, adSize: FBAdSize, position: String) {
        super.init(id: id, placementID: placementID)
        
        self.adSize = adSize
        self.position = position
        self.prepareBanner()
    }
    
    deinit {
        adView = nil
    }
    
    func prepareBanner() {
        if (self.adView == nil) {
            self.adView = FBAdView(placementID: self.placementID, adSize: self.adSize, rootViewController: plugin.viewController)
        }
        let size: CGSize = view.bounds.size
        let yOffset: CGFloat = size.height - 50
        self.adView?.frame = CGRect(x: 0, y: yOffset, width: size.width, height: 50)
        self.adBannerToView(self.adView)
        self.adView?.delegate = self
    }
    
    func hide() {
        if (adView?.superview != nil) {
            adView.delegate = nil
            adView.removeFromSuperview()
            plugin.webView.frame = CGRect(
                x: plugin.webView.bounds.origin.x,
                y: plugin.webView.bounds.origin.y,
                width: plugin.webView.bounds.width,
                height: plugin.webView.bounds.height + 50)
        }
    }
    
    func showBanner() {
        prepareBanner()
        self.adView?.loadAd()
    }
    
    func adViewDidLoad(_ adView: FBAdView) {
        if (self.adView != nil && self.adView!.isAdValid) {
            //            self.adBannerToView(adView)
        }
    }
    
    func adBannerToView(_ adView: UIView) {
        plugin.webView.superview?.addSubview(adView)
        
        plugin.webView.frame = CGRect(
            x: plugin.webView.bounds.origin.x,
            y: plugin.webView.bounds.origin.y,
            width: plugin.webView.bounds.width,
            height: plugin.webView.bounds.height - 50)
    }
    
    func adViewDidClick(_ adView: FBAdView) {
        plugin.emit(eventType: FBANEvents.bannerClick)
    }
    
    func adViewWillLogImpression(_ adView: FBAdView) {
        plugin.emit(eventType: FBANEvents.bannerImpression)
    }
    
    func adView(_ adView: FBAdView, didFailWithError error: Error) {
        print("Banner failed with error" + error.localizedDescription)
        plugin.emit(eventType: FBANEvents.bannerLoadFail, data: error)
    }
}
