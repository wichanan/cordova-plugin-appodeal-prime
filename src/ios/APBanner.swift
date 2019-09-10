class APBanner: APBase, AppodealBannerDelegate {
    
    var view: UIView {
        return self.plugin.viewController.view
    }
    
    override init(id: Int) {
        super.init(id: id)
    }
    
    func hide() {
        Appodeal.hideBanner()
        resizeWebView(50)
    }
    
    func showBanner() {
        Appodeal.showAd(.bannerBottom, rootViewController: self.plugin.viewController)
        Appodeal.setBannerDelegate(self)
        resizeWebView(-50)
    }

    func resizeWebView(_ offset: CGFloat) {
        plugin.webView.frame = CGRect(
            x: plugin.webView.bounds.origin.x,
            y: plugin.webView.bounds.origin.y,
            width: plugin.webView.bounds.width,
            height: plugin.webView.bounds.height + offset)
    }
    
    func bannerDidLoadAdIsPrecache(_ precache: Bool){
        NSLog("banner was loaded")
        plugin.emit(eventType: APEvents.bannerLoad)
    }
    func bannerDidFailToLoadAd(){
        NSLog("banner failed to load");
        plugin.emit(eventType: APEvents.bannerLoadFail)
    }
    func bannerDidClick(){
        NSLog("banner was clicked")
        plugin.emit(eventType: APEvents.bannerClick)
    }
    func bannerDidShow(){
        NSLog("banner was shown")
        plugin.emit(eventType: APEvents.bannerShow)
    }
    func bannerDidExpired(){
        NSLog("banner did expire and could not be shown")
    }
}
