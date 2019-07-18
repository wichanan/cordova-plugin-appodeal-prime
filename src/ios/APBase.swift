class APBase: NSObject {
    static var ads = Dictionary<Int, Any>()
    static weak var plugin: FBANPlugin!

    var placementID: String!
    var id: Int!

    var plugin: FBANPlugin {
        return FBANBase.plugin
    }

    init(id: Int, placementID: String) {
        super.init()
        
        self.id = id
        self.placementID = placementID
        FBANBase.ads[id] = self
    }

    deinit {
        FBANBase.ads.removeValue(forKey: self.id)
        self.placementID = nil
    }
}
