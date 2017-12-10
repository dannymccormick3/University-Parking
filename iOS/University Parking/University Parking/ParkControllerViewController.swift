//
//  ParkControllerViewController.swift
//  University Parking
//
//  Created by Thomas Marosz on 11/15/17.
//  Copyright © 2017 PSE. All rights reserved.
//

import UIKit
import MapKit
import Firebase

class ParkControllerViewController: UIViewController, MKMapViewDelegate,
CLLocationManagerDelegate, UITableViewDataSource {
    
    var spaces: [Space] = []
    
    let lotRef = Database.database().reference(withPath: "lots")
    
    var lots = Array<Lot>()
    
    var selectedLot: Lot?
    
    var reserveAnnotation: Bool?
    
    var selectedSpace: Space?
    
    var reservedSpaces = [Space]()
    
    var selectedSpaceLotID: String?
    
   
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        if !(ReserveOutlet.currentTitle == "Reserve Spot") {
            return reservedSpaces.count
        } else {
            if (selectedLot == nil) {
                return 0
            } else {
                return selectedLot!.spaces.count
            }
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "AvailableSpotCell", for: indexPath)
        
        if !(ReserveOutlet.currentTitle == "Reserve Spot") {
            cell.textLabel!.text = "Reserved Spot: " + reservedSpaces[indexPath.row].getName()
        } else {
            if (selectedLot == nil) {
                cell.textLabel!.text = "No spots available."
            } else {
                cell.textLabel!.text = "Spot: " + selectedLot!.spaces[indexPath.row].getName()
            }
        }
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAtIndexPath indexPath: IndexPath) {
        
        print("Reached code")
        
        if (ReserveOutlet.currentTitle == "Reserve Spot") {
            selectedSpace = selectedLot?.spaces[indexPath.row]
            selectedSpaceLotID = selectedLot?.id
        }
    }
    
    
    @IBOutlet weak var mapView: MKMapView!
    @IBOutlet weak var parkingLotsButton: UIBarButtonItem!
    @IBOutlet weak var accountButton: UIBarButtonItem!
    
    @IBOutlet weak var helpButton: UIBarButtonItem!
    
    @IBOutlet weak var ReserveOutlet: UIButton!

    @IBOutlet weak var TableViewOutlet: UITableView!
    
    @IBOutlet weak var ParkButtonOutlet: UIButton!
    
    func mapView(_ mapView: MKMapView, viewFor annotation: MKAnnotation) -> MKAnnotationView? {
        
        if annotation is Lot {
            
            // guard let annotation = annotation as? Lot else { return nil }
            
            let identifier = "lot"
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
            
        } else if annotation is Space {
            
            let identifier = "space"
            let view = MKMarkerAnnotationView()

            view.markerTintColor = .cyan

            return view
            
            
        }
        return nil
    }
    
    func mapView(_ mapView: MKMapView, annotationView view: MKAnnotationView, calloutAccessoryControlTapped control: UIControl) {
        
        if view.annotation is Lot {
            
            TableViewOutlet.reloadData()
            
            selectedLot = view.annotation as? Lot
            
            TableViewOutlet.reloadData()
            
            TableViewOutlet.isHidden = false
            ReserveOutlet.isHidden = false
            ParkButtonOutlet.isHidden = false
            
        } else if view.annotation is Space {
            
            selectedSpace = view.annotation as! Space
            
            TableViewOutlet.reloadData()
            
        }
        
//        let lotTitle = lot.title
//        let permitInfo = lot.permit
//        let availableSpaces = lot.spaces
        
//        let ac = UIAlertController(title: lotTitle, message: "Permit(s): \(permitInfo)\nAvailable Spaces: \(availableSpaces.count)", preferredStyle: .alert)
//        
//        ac.addAction(UIAlertAction(title: "Reserve", style: .default))
//        present(ac, animated: true)
    }
    
    @IBAction func ReserveCancelButton(_ sender: Any) {
        
        if (ReserveOutlet.currentTitle == "Reserve Spot") {
            
            reserveAnnotation = true
            
            print(selectedLot)
            print(selectedSpace)

            self.mapView.addAnnotation(selectedSpace!)
            selectedSpace?.occupied = true
            reservedSpaces.append(selectedSpace!)
            
            editSpace(for: selectedSpaceLotID!, updatedLot: selectedLot!)
            
            ReserveOutlet.setTitle("Cancel Reservation", for: .normal)
            ParkButtonOutlet.setTitle("Leave Lot", for: .normal)
            TableViewOutlet.reloadData()
            
        } else {
            print("ReserveCancelButton")
            print("========Did not return true=========")
            
            for annotation in mapView.annotations as [MKAnnotation] {
                if annotation is Space {
                    mapView.removeAnnotation(annotation)
                }
            }
            
            for space in reservedSpaces as [Space] {
                space.occupied = false
                editSpace(for: selectedSpaceLotID!, updatedLot: selectedLot!)
            }
            
            reservedSpaces.removeAll()
            
            ReserveOutlet.setTitle("Reserve Spot", for: .normal)
            ParkButtonOutlet.setTitle("Park Now", for: .normal)
            TableViewOutlet.reloadData()
        }
        
    }
    
    @IBAction func ParkButtonAction(_ sender: Any) {
        
        if (ParkButtonOutlet.currentTitle == "Park Now") {
            
            reserveAnnotation = true
            
            print(selectedLot)
            print(selectedSpace)
            
            
            self.mapView.addAnnotation(selectedSpace!)
            selectedSpace?.occupied = true
            reservedSpaces.append(selectedSpace!)
            
            editSpace(for: selectedSpaceLotID!, updatedLot: selectedLot!)
            
            ParkButtonOutlet.setTitle("Leave Lot", for: .normal)
            ReserveOutlet.setTitle("Cancel Reservation", for: .normal)
            TableViewOutlet.reloadData()
            
        } else {
            print("ParkButtonAction")
            print("========Did not return true=========")
            
            for annotation in mapView.annotations as [MKAnnotation] {
                if annotation is Space {
                    mapView.removeAnnotation(annotation)
                }
            }
            
            for space in reservedSpaces as [Space] {
                space.occupied = false
                editSpace(for: selectedSpaceLotID!, updatedLot: selectedLot!)
            }
            
            reservedSpaces.removeAll()
            
            ParkButtonOutlet.setTitle("Park Now", for: .normal)
            ReserveOutlet.setTitle("Reserve Spot", for: .normal)
            TableViewOutlet.reloadData()
        }
        
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
    
    func getAllLots(success: @escaping ([AnyObject]) -> Void) {
        lotRef.observe(.value, with: { (snapshot) in
            self.lots.removeAll()
            let lotDictionary = snapshot.value as? [String : AnyObject] ?? [:]
            for (key, value) in lotDictionary {
                
                if let oneLot = value as? [String: AnyObject] {
                    
                    //if oneTicket["companyId"] as? String != nil {
                        //if (oneTicket["companyId"] as? String)! == user.getCurrentCompany().getCompanyID() {
                            
                            let tempLot = Lot(title: "")
                            tempLot.id = key
                            tempLot.title = oneLot["title"] as! String
                            let lat = oneLot["lat"] as! Double
                            let long = oneLot["long"] as! Double
                            tempLot.coordinate = CLLocationCoordinate2D(latitude: lat, longitude: long)
                            let permitDictionary = oneLot["permits"] as! Array<String>
                            
                            for value in permitDictionary {
                                tempLot.permit.append(value)
                            }
                            
                    
                            let spacesDictionary = oneLot["spaces"] as! Array<[String: AnyObject]>
                            for oneSpace in spacesDictionary {
                                
                                let lat = lat + 0.001
                                let long = long + 0.001
                                
                                let spaceCoordinate = CLLocationCoordinate2D(latitude: lat, longitude: long)
                                let tempSpace = Space(coordinate: spaceCoordinate, name: oneSpace["name"] as! String, title: oneSpace["name"] as! String, permit: oneSpace["permit"] as! String, lot: tempLot.title, occupied: oneSpace["occupied"] as! Bool, priceRateClass: oneSpace["priceRateClass"] as! String)
                                if !(tempSpace.occupied!) {
                                    tempLot.addSpace(newSpace: tempSpace)
                                }
                                
                            }
                            self.lots.append(tempLot)
                        //}
                    //}
                }
            }
           return success(self.lots)
        })
    }
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        let userLocation:CLLocation = locations[0] as CLLocation
        
        let center = CLLocationCoordinate2D(latitude: userLocation.coordinate.latitude, longitude: userLocation.coordinate.longitude)
        let region = MKCoordinateRegion(center: center, span: MKCoordinateSpan(latitudeDelta: 0.01, longitudeDelta: 0.01))
        
        mapView.setRegion(region, animated: true)
        
        // Drop a pin at user's Current Location
//        let myAnnotation: MKPointAnnotation = MKPointAnnotation()
//        myAnnotation.coordinate = CLLocationCoordinate2DMake(userLocation.coordinate.latitude, userLocation.coordinate.longitude);
//        myAnnotation.title = "Current location"
//        mapView.addAnnotation(myAnnotation)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        checkLocationAuthorizationStatus()
        determineCurrentLocation()
        mapView.delegate = self
        reserveAnnotation = false
        //observing the data changes
        
        //let kensington = Lot(title: "Kensington", coordinate: CLLocationCoordinate2D(latitude: 36.142103, longitude: -86.806105), spaces: spaces, permit: "F")
        getAllLots(success: { (response) in
            self.lots = response as! Array<Lot>
            for object in self.lots {
                self.mapView.addAnnotation(object)
            }
           
        })
        print(spaces)

        
    }
    
    func editSpace(for lotID: String, updatedLot: Lot) {
   
        for i in stride(from: 0, to: updatedLot.spaces.count, by: 1) {
            
            lotRef.child(lotID).child("spaces").child("\(i)").updateChildValues([
                "name": updatedLot.getSpaces()[i].getName(),
                "occupied": updatedLot.getSpaces()[i].getOccupied(),
                "permit": updatedLot.getSpaces()[i].getPermit(),
                "priceRateClass": updatedLot.getSpaces()[i].getPriceClass()
                ])
        }
        
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
