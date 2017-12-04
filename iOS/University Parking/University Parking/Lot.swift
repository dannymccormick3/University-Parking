//
//  Lot.swift
//  University Parking
//
//  Created by Thomas Marosz on 11/29/17.
//  Copyright © 2017 PSE. All rights reserved.
//

import UIKit
import MapKit

class Lot: NSObject, MKAnnotation {
    var title: String?
    var coordinate: CLLocationCoordinate2D
    var spaces: [Space]
    var permit: String
    
    init(title: String, coordinate: CLLocationCoordinate2D, spaces: [Space], permit: String) {
        self.title = title
        self.coordinate = coordinate
        self.spaces = spaces
        self.permit = permit
        
        super.init()
    }
    
//    var subtitle: String? {
//        return "Permit: \(permit); Spaces available: \(spaces.count)"
//    }

}
