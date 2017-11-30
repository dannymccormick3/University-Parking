//
//  ParkControllerViewController.swift
//  University Parking
//
//  Created by Thomas Marosz on 11/15/17.
//  Copyright © 2017 PSE. All rights reserved.
//

import UIKit
import MapKit

class ParkControllerViewController: UIViewController, MKMapViewDelegate,
CLLocationManagerDelegate, UITableViewDataSource {
    
    let kensington = Lot(title: "Kensington", coordinate: CLLocationCoordinate2D(latitude: 36.142103, longitude: -86.806105), spaces: [1, 2, 3, 4, 5], permit: "F")
    
    var selectedLot: Lot?
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if (selectedLot == nil) {
            return 0
        } else {
            return selectedLot!.spaces.count
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "AvailableSpotCell", for: indexPath)
        
        if (selectedLot == nil) {
            cell.textLabel!.text = "No spots available."
        } else {
            cell.textLabel!.text = "Spot: \(String(describing: selectedLot!.spaces[indexPath.row]))"
        }
        
        
        return cell
    }
    
    @IBOutlet weak var mapView: MKMapView!
    @IBOutlet weak var parkingLotsButton: UIBarButtonItem!
    @IBOutlet weak var accountButton: UIBarButtonItem!
    
    @IBOutlet weak var helpButton: UIBarButtonItem!
    
    @IBOutlet weak var ReserveOutlet: UIButton!

    @IBOutlet weak var TableViewOutlet: UITableView!
    
    func mapView(_ mapView: MKMapView, viewFor annotation: MKAnnotation) -> MKAnnotationView? {
        
        guard let annotation = annotation as? Lot else { return nil }
        
        let identifier = "marker"
        var view: MKMarkerAnnotationView
        
        if let dequeuedView = mapView.dequeueReusableAnnotationView(withIdentifier: identifier)
            as? MKMarkerAnnotationView {
            dequeuedView.annotation = annotation
            view = dequeuedView
        } else {
           
            view = MKMarkerAnnotationView(annotation: annotation, reuseIdentifier: identifier)
            view.canShowCallout = true
            view.calloutOffset = CGPoint(x: -5, y: 5)
            view.rightCalloutAccessoryView = UIButton(type: .detailDisclosure)
        }
        return view
    }
    
    func mapView(_ mapView: MKMapView, annotationView view: MKAnnotationView, calloutAccessoryControlTapped control: UIControl) {
        
        selectedLot = view.annotation as! Lot
        
        TableViewOutlet.reloadData()
//        let lotTitle = lot.title
//        let permitInfo = lot.permit
//        let availableSpaces = lot.spaces
        
//        let ac = UIAlertController(title: lotTitle, message: "Permit(s): \(permitInfo)\nAvailable Spaces: \(availableSpaces.count)", preferredStyle: .alert)
//        
//        ac.addAction(UIAlertAction(title: "Reserve", style: .default))
//        present(ac, animated: true)
    }
    
    let locationManager = CLLocationManager()
    func checkLocationAuthorizationStatus() {
        if CLLocationManager.authorizationStatus() == .authorizedWhenInUse {
            mapView.showsUserLocation = true
        } else {
            locationManager.requestWhenInUseAuthorization()
        }
    }
    
    // https://www.raywenderlich.com/160517/mapkit-tutorial-getting-started
    
    let regionRadius: CLLocationDistance = 1000
    func centerMapOnLocation(location: CLLocation) {
        let coordinateRegion = MKCoordinateRegionMakeWithDistance(location.coordinate,
                                                                  regionRadius, regionRadius)
        mapView.setRegion(coordinateRegion, animated: true)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func determineCurrentLocation() {
        locationManager.delegate = self
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        
        if CLLocationManager.locationServicesEnabled() {
            locationManager.startUpdatingLocation()
        }
    }
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        let userLocation:CLLocation = locations[0] as CLLocation
        
        let center = CLLocationCoordinate2D(latitude: userLocation.coordinate.latitude, longitude: userLocation.coordinate.longitude)
        let region = MKCoordinateRegion(center: center, span: MKCoordinateSpan(latitudeDelta: 0.01, longitudeDelta: 0.01))
        
        mapView.setRegion(region, animated: true)
        
        // Drop a pin at user's Current Location
        let myAnnotation: MKPointAnnotation = MKPointAnnotation()
        myAnnotation.coordinate = CLLocationCoordinate2DMake(userLocation.coordinate.latitude, userLocation.coordinate.longitude);
        myAnnotation.title = "Current location"
        mapView.addAnnotation(myAnnotation)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        checkLocationAuthorizationStatus()
        determineCurrentLocation()
        mapView.delegate = self
        mapView.addAnnotation(kensington)
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
