//
//  Space.swift
//  University Parking
//
//  Created by Thomas Marosz on 11/30/17.
//  Copyright © 2017 PSE. All rights reserved.
//

import UIKit
import MapKit

class Space: NSObject, MKAnnotation {
    var coordinate: CLLocationCoordinate2D
    var title: String?
    var space: Int?
    var permit: String?
    var lot: String?
    var occupied: Bool?
    var priceRateClass: Int?
    
    init(coordinate: CLLocationCoordinate2D, title: String?, space:Int?, permit: String?, lot: String?, occupied: Bool?, priceRateClass: Int?) {
        self.coordinate = coordinate
        self.space = space
        self.title = String(describing: space)
        self.permit = permit
        self.lot = lot
        self.occupied = occupied
        self.priceRateClass = priceRateClass
    }
    
    init?(json: [String: Any]) {
        self.space = json["Space"] as! Int
        self.title = String(describing: space)
        self.permit = json["Permit"] as! String
        self.lot = json["Lot"] as! String
        self.occupied = json["Occupied"] as! Bool
        self.priceRateClass = json["PriceRateClass"] as! Int!
        self.coordinate = CLLocationCoordinate2D(latitude: 36.142103, longitude: -86.806105)

    }

}
