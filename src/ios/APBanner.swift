class APBanner: APBase, AppodealBannerDelegate {
    
    var view: UIView {
        return self.plugin.viewController.view
    }
    
    override init(id: Int) {
        super.init(id: id)
    }
    
//    func prepareBanner() {
//        if (self.adView == nil) {
//            self.adView = FBAdView(placementID: self.placementID, adSize: self.adSize, rootViewController: plugin.viewController)
//        }
//        let size: CGSize = view.bounds.size
//        let yOffset: CGFloat = size.height - 50
//        self.adView?.frame = CGRect(x: 0, y: yOffset, width: size.width, height: 50)
//        self.adBannerToView(self.adView)
//        self.adView?.delegate = self
//    }
    
    func hide() {
        Appodeal.hideBanner()
    }
    
    func showBanner() {
        Appodeal.showAd(.bannerBottom, rootViewController: self.plugin.viewController)
        Appodeal.setBannerDelegate(self)
    }
    
//    func adViewDidLoad(_ adView: FBAdView) {
//        if (self.adView != nil && self.adView!.isAdValid) {
//            //            self.adBannerToView(adView)
//        }
//    }
    
    func adBannerToView(_ adView: UIView) {
        plugin.webView.superview?.addSubview(adView)
        
        plugin.webView.frame = CGRect(
            x: plugin.webView.bounds.origin.x,
            y: plugin.webView.bounds.origin.y,
            width: plugin.webView.bounds.width,
            height: plugin.webView.bounds.height - 50)
    }
    
    func bannerDidLoadAdIsPrecache(_ precache: Bool){
        NSLog("banner was loaded")
    }
    func bannerDidFailToLoadAd(){
        NSLog("banner failed to load");
    }
    func bannerDidClick(){
        NSLog("banner was clicked")
    }
    func bannerDidShow(){
        NSLog("banner was shown")
    }
    func bannerDidExpired(){
        NSLog("banner did expire and could not be shown")
    }
}
