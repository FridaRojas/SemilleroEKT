//
//  AppDelegate.swift
//  AgileUs
//
//  Created by user204412 on 11/18/21.
//

import UIKit

var server = "http://10.97.0.137:3041/api/"

var userID = String()
var userName = String()
var email = String()
var employeeNumber = String()

var isLogged = Bool()

@main
class AppDelegate: UIResponder, UIApplicationDelegate {

    func recoverUserInfo() {
        userID = UserDefaults.standard.string(forKey: "userID") ?? String()
        userName = UserDefaults.standard.string(forKey: "userName") ?? String()
        email = UserDefaults.standard.string(forKey: "email") ?? String()
        employeeNumber = UserDefaults.standard.string(forKey: "employeeNumber") ?? String()
        
        //print(userID)
    }

    func recoverLogged(){
        isLogged = UserDefaults.standard.bool(forKey: "isLogged")
    }
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        // Override point for customization after application launch.
        
        recoverUserInfo()
        recoverLogged()
        
        return true
    }

    // MARK: UISceneSession Lifecycle

    func application(_ application: UIApplication, configurationForConnecting connectingSceneSession: UISceneSession, options: UIScene.ConnectionOptions) -> UISceneConfiguration {
        // Called when a new scene session is being created.
        // Use this method to select a configuration to create the new scene with.
        return UISceneConfiguration(name: "Default Configuration", sessionRole: connectingSceneSession.role)
    }

    func application(_ application: UIApplication, didDiscardSceneSessions sceneSessions: Set<UISceneSession>) {
        // Called when the user discards a scene session.
        // If any sessions were discarded while the application was not running, this will be called shortly after application:didFinishLaunchingWithOptions.
        // Use this method to release any resources that were specific to the discarded scenes, as they will not return.
    }


}

