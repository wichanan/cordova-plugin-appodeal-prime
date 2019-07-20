class APInterstitial: APBase, APDInterstitalAdDelegate {

    func show() {
        Appodeal.showAd(.interstitial, rootViewController: self.plugin.viewController)
        Appodeal.setAutocache(false, types:.interstitial)
        Appodeal.setInterstitialDelegate(self as? AppodealInterstitialDelegate)
    }

    func interstitialAdDidAppear(_ interstitialAd: APDInterstitialAd) {
        
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
}
