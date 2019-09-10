@objc(AppodealPrime)
class AppodealPrime: CDVPlugin {
    static let testAdID = ""
    var isTestMode = true
    var originalHeight: CGFloat!
    
    var readyCallbackId: String!
    
    override func pluginInitialize() {
        super.pluginInitialize()
        
        isTestMode = false
        APBase.plugin = self
        Appodeal.disableNetwork("admob")
        let apiKey = getAPIKey()
//        setTestEnv()
        Appodeal.initialize(withApiKey: apiKey, types: [AppodealAdType.banner, AppodealAdType.interstitial, AppodealAdType.nativeAd, AppodealAdType.rewardedVideo], hasConsent: true)
    }
    
    func getAPIKey() -> String {
        let api_key = Bundle.main.object(forInfoDictionaryKey: "ApodealApplicationIdentifier") as? String
        return api_key!
    }
    
    func setTestEnv() {
        Appodeal.setTestingEnabled(true)
        Appodeal.setLogLevel(.verbose)
    }
    
    @objc(ready:)
    func ready(command: CDVInvokedUrlCommand) {
        readyCallbackId = command.callbackId
        if (originalHeight == nil) {
            originalHeight = self.webView.frame.height
        }
        self.emit(eventType: APEvents.ready, data: [
            "platform": "ios",
            "sdkVersion": Appodeal.getVersion()])
    }
    
    @objc(banner_show:)
    func banner_show(command: CDVInvokedUrlCommand) {
        guard let opts = command.argument(at: 0) as? NSDictionary,
            let id = opts.value(forKey: "id") as? Int,
            var banner = APBase.ads[id] as? APBanner?
            else {
                let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: false)
                self.commandDelegate!.send(result, callbackId: command.callbackId)
                return
        }
        
        if banner == nil {
            banner = APBanner(id: id)
        }
        
        banner!.showBanner()
        
        let result = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: true)
        self.commandDelegate!.send(result, callbackId: command.callbackId)
    }
    
    @objc(banner_hide:)
    func banner_hide(command: CDVInvokedUrlCommand) {
        guard let opts = command.argument(at: 0) as? NSDictionary,
            let id = opts.value(forKey: "id") as? Int,
            let banner = APBase.ads[id] as? APBanner?
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
            var native = APBase.ads[id] as? APNative?
            else {
                let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: false)
                self.commandDelegate!.send(result, callbackId: command.callbackId)
                return
        }

        if native == nil {
            native = APNative(id: id)
        }

        native!.load()

        let result = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: true)
        self.commandDelegate!.send(result, callbackId: command.callbackId)
    }
    
    @objc(native_show:)
    func native_show(command: CDVInvokedUrlCommand) {
        guard let opts = command.argument(at: 0) as? NSDictionary,
            let id = opts.value(forKey: "id") as? Int,
            let position = opts.value(forKey: "position") as? NSDictionary,
            let native = APBase.ads[id] as? APNative?
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

        native!.show(position)

        let result = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: true)
        self.commandDelegate!.send(result, callbackId: command.callbackId)
    }
    
    @objc(native_hide:)
    func native_hide(command: CDVInvokedUrlCommand) {
        guard let opts = command.argument(at: 0) as? NSDictionary,
            let id = opts.value(forKey: "id") as? Int,
            let native = APBase.ads[id] as? APNative?
            else {
                let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: false)
                self.commandDelegate!.send(result, callbackId: command.callbackId)
                return
        }
        native!.hide()

        let result = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: true)
        self.commandDelegate!.send(result, callbackId: command.callbackId)
    }
    
    @objc(interstitial_show:)
    func interstitial_show(command: CDVInvokedUrlCommand) {
        guard let opts = command.argument(at: 0) as? NSDictionary,
            let id = opts.value(forKey: "id") as? Int,
            var interstitial = APBase.ads[id] as? APInterstitial?
            else {
                let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: false)
                self.commandDelegate!.send(result, callbackId: command.callbackId)
                return
        }
        if interstitial == nil {
            interstitial = APInterstitial(id: id)
        }
        interstitial!.show()
        
        let result = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: true)
        self.commandDelegate!.send(result, callbackId: command.callbackId)
    }
    
    @objc(reward_video_show:)
    func reward_video_show(command: CDVInvokedUrlCommand) {
        guard let opts = command.argument(at: 0) as? NSDictionary,
            let id = opts.value(forKey: "id") as? Int,
            var reward_video = APBase.ads[id] as? APRewardVideo?
            else {
                let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: false)
                self.commandDelegate!.send(result, callbackId: command.callbackId)
                return
        }
        if reward_video == nil {
            reward_video = APRewardVideo(id: id)
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
}
