@objc(AppodealPrime)
class AppodealPrime: CDVPlugin {
    static let testAdID = ""
    var isTestMode = true
    
    var readyCallbackId: String!
    
    override func pluginInitialize() {
        super.pluginInitialize()
        
        isTestMode = false
        FBANBase.plugin = self
    }
    
    @objc(ready:)
    func ready(command: CDVInvokedUrlCommand) {
        readyCallbackId = command.callbackId
        
        self.emit(eventType: FBANEvents.ready, data: [
            "platform": "ios",
            "sdkVersion": FB_AD_SDK_VERSION,
            "isRunningInTestLab": false])
    }
    
    @objc(banner_show:)
    func banner_show(command: CDVInvokedUrlCommand) {
        guard let opts = command.argument(at: 0) as? NSDictionary,
            let id = opts.value(forKey: "id") as? Int,
            var placementID = opts.value(forKey: "placementID") as? String,
            let position = opts.value(forKey: "position") as? String,
            var banner = FBANBase.ads[id] as? FBANBanner?
            else {
                let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: false)
                self.commandDelegate!.send(result, callbackId: command.callbackId)
                return
        }
        
        if banner == nil {
            let adSize = getAdSize(opts)
            if (isTestMode) {
                placementID = FBANEvents.bannerAdTestType + placementID
            }
            banner = FBANBanner(id: id, placementID: placementID, adSize: adSize, position: position)
        }
        
        banner!.showBanner()
        
        let result = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: true)
        self.commandDelegate!.send(result, callbackId: command.callbackId)
    }
    
    @objc(banner_hide:)
    func banner_hide(command: CDVInvokedUrlCommand) {
        guard let opts = command.argument(at: 0) as? NSDictionary,
            let id = opts.value(forKey: "id") as? Int,
            let banner = FBANBase.ads[id] as? FBANBanner?
            else {
                let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: false)
                self.commandDelegate!.send(result, callbackId: command.callbackId)
                return
        }
        banner!.hide()
        
        let result = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: true)
        self.commandDelegate!.send(result, callbackId: command.callbackId)
    }

    @objc(native_load:)
    func native_load(command: CDVInvokedUrlCommand) {
        guard let opts = command.argument(at: 0) as? NSDictionary,
            let id = opts.value(forKey: "id") as? Int,
            var placementID = opts.value(forKey: "placementID") as? String,
            let position = opts.value(forKey: "position") as? NSDictionary,
            var native = FBANBase.ads[id] as? FBANNative?
            else {
                let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: false)
                self.commandDelegate!.send(result, callbackId: command.callbackId)
                return
        }

        if native == nil {
            if (isTestMode == true) {
                placementID = FBANEvents.nativeAdTestType + placementID
            }
            native = FBANNative(id: id, placementID: placementID, position: position, adViewType: FBNativeAdViewType.dynamic)
        }

        native!.load()

        let result = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: true)
        self.commandDelegate!.send(result, callbackId: command.callbackId)
    }
    
    @objc(native_show:)
    func native_show(command: CDVInvokedUrlCommand) {
        guard let opts = command.argument(at: 0) as? NSDictionary,
            let id = opts.value(forKey: "id") as? Int,
            var placementID = opts.value(forKey: "placementID") as? String,
            let position = opts.value(forKey: "position") as? NSDictionary,
            var native = FBANBase.ads[id] as? FBANNative?
            else {
                let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: false)
                self.commandDelegate!.send(result, callbackId: command.callbackId)
                return
        }

        if native == nil {
            let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "Must load the native before showing")
            self.commandDelegate!.send(result, callbackId: command.callbackId)
            return
        }

        native!.show()

        let result = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: true)
        self.commandDelegate!.send(result, callbackId: command.callbackId)
    }
    
    @objc(native_hide:)
    func native_hide(command: CDVInvokedUrlCommand) {
        guard let opts = command.argument(at: 0) as? NSDictionary,
            let id = opts.value(forKey: "id") as? Int,
            let native = FBANBase.ads[id] as? FBANNative?
            else {
                let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: false)
                self.commandDelegate!.send(result, callbackId: command.callbackId)
                return
        }
        native!.hide()
        
        let result = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: true)
        self.commandDelegate!.send(result, callbackId: command.callbackId)
    }
    
    @objc(native_hide_all:)
    func native_hide_all(command: CDVInvokedUrlCommand) {
        let native = FBANNative(id: 9999, placementID: "", position: [:], adViewType: FBNativeAdViewType.dynamic)
        native.hideAll()
        
        FBANBase.ads.removeValue(forKey: 9999)
        
        let result = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: true)
        self.commandDelegate!.send(result, callbackId: command.callbackId)
    }
    
    @objc(native_banner_show:)
    func native_banner_show(command: CDVInvokedUrlCommand) {
        guard let opts = command.argument(at: 0) as? NSDictionary,
            let id = opts.value(forKey: "id") as? Int,
            var placementID = opts.value(forKey: "placementID") as? String,
            var native_banner = FBANBase.ads[id] as? FBANNativeBanner?
            else {
                let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: false)
                self.commandDelegate!.send(result, callbackId: command.callbackId)
                return
        }
        if native_banner == nil {
            if (isTestMode == true) {
                placementID = FBANEvents.nativeBannerAdTestType + placementID
            }
            native_banner = FBANNativeBanner(id: id, placementID: placementID, position: [:], adviewType: FBNativeBannerAdViewType.genericHeight50.rawValue)
        }

        native_banner!.show()

        let result = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: true)
        self.commandDelegate!.send(result, callbackId: command.callbackId)
    }

    @objc(interstitial_load:)
    func interstitial_load(command: CDVInvokedUrlCommand) {
        guard let opts = command.argument(at: 0) as? NSDictionary,
            let id = opts.value(forKey: "id") as? Int,
            var placementID = opts.value(forKey: "placementID") as? String,
            var interstitial = FBANBase.ads[id] as? FBANInterstitial?
            else {
                let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: false)
                self.commandDelegate!.send(result, callbackId: command.callbackId)
                return
        }
        if interstitial == nil {
            if (isTestMode == true) {
                placementID = FBANEvents.interstitialAdTestType + placementID
            }
            interstitial = FBANInterstitial(id: id, placementID: placementID)
        }
        interstitial!.load()
        
        let result = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: true)
        self.commandDelegate!.send(result, callbackId: command.callbackId)
    }
    
    @objc(interstitial_show:)
    func interstitial_show(command: CDVInvokedUrlCommand) {
        guard let opts = command.argument(at: 0) as? NSDictionary,
            let id = opts.value(forKey: "id") as? Int,
            var placementID = opts.value(forKey: "placementID") as? String,
            var interstitial = FBANBase.ads[id] as? FBANInterstitial?
            else {
                let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: false)
                self.commandDelegate!.send(result, callbackId: command.callbackId)
                return
        }
        if interstitial == nil {
            let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "Must load the native before showing")
            self.commandDelegate!.send(result, callbackId: command.callbackId)
            return
        }
        interstitial!.show()
        
        let result = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: true)
        self.commandDelegate!.send(result, callbackId: command.callbackId)
    }
    
    @objc(reward_video_show:)
    func reward_video_show(command: CDVInvokedUrlCommand) {
        guard let opts = command.argument(at: 0) as? NSDictionary,
            let id = opts.value(forKey: "id") as? Int,
            var placementID = opts.value(forKey: "placementID") as? String,
            var reward_video = FBANBase.ads[id] as? FBANRewardVideo?
            else {
                let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: false)
                self.commandDelegate!.send(result, callbackId: command.callbackId)
                return
        }
        if reward_video == nil {
            if (isTestMode == true) {
                placementID = FBANEvents.rewardVideoAdTestType + placementID
            }
            reward_video = FBANRewardVideo(id: id, placementID: placementID)
        }
        reward_video!.show()
        
        let result = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: true)
        self.commandDelegate!.send(result, callbackId: command.callbackId)
    }
    
    func emit(eventType: String, data: Any = false) {
        let result = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: ["type": eventType, "data": data])
        result?.setKeepCallbackAs(true)
        self.commandDelegate!.send(result, callbackId: readyCallbackId)
    }
    
    func getAdSize(_ opts: NSDictionary) -> FBAdSize {
        if let adSizeType = opts.value(forKey: "adSize") as? Int {
            switch adSizeType {
            case 0:
                return kFBAdSizeHeight90Banner
            case 1:
                return kFBAdSizeHeight50Banner
            case 2:
                return kFBAdSizeHeight250Rectangle
            case 3:
                return kFBAdSize320x50
            default:
                break;
            }
        }
        
        return kFBAdSizeHeight50Banner
    }
}
