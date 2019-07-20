class APBase: NSObject {
    static var ads = Dictionary<Int, Any>()
    static weak var plugin: AppodealPrime!
    var id: Int!

    var plugin: AppodealPrime {
        return APBase.plugin
    }

    init(id: Int) {
        super.init()
        
        self.id = id
        APBase.ads[id] = self
    }

    deinit {
        APBase.ads.removeValue(forKey: self.id)
    }
}
