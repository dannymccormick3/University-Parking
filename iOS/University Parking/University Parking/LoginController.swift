//
//  ViewController.swift
//  University Parking
//
//  Created by Thomas Marosz on 11/15/17.
//  Copyright © 2017 PSE. All rights reserved.
//

import UIKit

class LoginController: UIViewController {

    @IBOutlet weak var UsernameTextField: UITextField!
    @IBOutlet weak var PasswordTextField: UITextField!
    @IBOutlet weak var LoginButton: UIButton!
    
    // TODO - authentication
    var loginSuccess = true
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    @IBAction func attemptLogin(_ sender: Any) {
        self.performSegue(withIdentifier: "loginSuccessful", sender: self)
    }

}

