class APInterstitial: APBase, AppodealInterstitialDelegate {
    var view: UIView {
        return self.plugin.viewController.view
    }
    
    func show() {
        Appodeal.showAd(.interstitial, rootViewController: self.plugin.viewController)
        Appodeal.setAutocache(false, types:.interstitial)
        Appodeal.setInterstitialDelegate(self)
    }
    
    // Method called if interstitial mediation failed
    func interstitialDidFailToLoadAd() {
        NSLog("interstitial did fail to load ad")
    }
    
    // Method called if interstitial mediation was success, but ready ad network can't show ad or
    // ad presentation was to frequently according your placement settings
    func interstitialDidFailToPresent() {
        NSLog("interstitial did fail to present")
    }
    
    // Method called when interstitial will display on screen
    func interstitialWillPresent() {
        NSLog("interstitial will display")
    }
    
    // Method called after interstitial leave screeen
    func interstitialDidDismiss() {
        view.frame = CGRect(
            x: view.bounds.origin.x,
            y: view.bounds.origin.y,
            width: view.bounds.width,
            height: view.bounds.height - 50)
        NSLog("interstitial did dismiss")
    }
    
    // Method called when user tap on interstitial
    func interstitialDidClick() {
        NSLog("interstitial did click")
    }
    
    // Method called when interstitial did expire and could not be shown
    func interstitialDidExpired(){
        NSLog("interstitial did expire")
    }
}
