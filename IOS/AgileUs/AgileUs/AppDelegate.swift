//
//  AppDelegate.swift
//  AgileUs
//
//  Created by user204412 on 11/18/21.
//

import UIKit
import Firebase
import FirebaseMessaging

var server = "http://ec2-3-144-86-49.us-east-2.compute.amazonaws.com:8080/Servicios-0.0.1-SNAPSHOT/api/"

var userID = String()
var userName = String()
var email = String()
var employeeNumber = String()
var pushNotificationToken = String()
var rolName = String()
var hierarchyLevel = Int()

var isLogged = Bool()

@main
class AppDelegate: UIResponder, UIApplicationDelegate, UNUserNotificationCenterDelegate, MessagingDelegate
{

    let gcmMessagingIDKey = "Api_Goo"

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {

        FirebaseApp.configure()
        recoverUserInfo()
        recoverLogged()

        //Ask if the user want to allow notifications
        if #available(ios 10.0, *)
        {
            UNUserNotificationCenter.current().delegate = self
            let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
            UNUserNotificationCenter.current().requestAuthorization(options: authOptions, completionHandler: {_, _ in})
        }
        else
        {
            let settings: UIUserNotificationSettings = UIUserNotificationSettings(types: [.alert, .badge, .sound], categories: nil)
            application.registerUserNotificationSettings(settings)
        }
        application.registerForRemoteNotifications()

        //Get notification token of firebase
        Messaging.messaging().delegate = self
        Messaging.messaging().token
        {
            token, error in
            if let error = error
            {
                print("Error fetching FCM registration token: \(error)")
            }
            else if let token = token
            {
             print("FCM registration token:  \(token)")
                pushNotificationToken = "\(token)"

            }
        }

        return true
    }

    func recoverUserInfo() {
        userID = UserDefaults.standard.string(forKey: "userID") ?? String()
        userName = UserDefaults.standard.string(forKey: "userName") ?? String()
        email = UserDefaults.standard.string(forKey: "email") ?? String()
        employeeNumber = UserDefaults.standard.string(forKey: "employeeNumber") ?? String()
        rolName = UserDefaults.standard.string(forKey: "rolName") ?? String()
        hierarchyLevel = UserDefaults.standard.integer(forKey: "hierarchyLevel")
    }

    func recoverLogged(){
        isLogged = UserDefaults.standard.bool(forKey: "isLogged")
    }

    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?)
    {
        let dataDict:[String: String] = ["token": fcmToken ?? ""]
        NotificationCenter.default.post(name: Notification.Name("FCMToken"), object: nil, userInfo:dataDict)
    }

    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data)
    {
        Messaging.messaging().apnsToken = deviceToken
    }

    func  userNotificationCenter(_ center: UNUserNotificationCenter, willPresent notification: UNNotification, withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void)
    {
        let userInfo = notification.request.content.userInfo
        Messaging.messaging().appDidReceiveMessage(userInfo)
        print(userInfo)
        if #available(iOS 14.0, *)
        {
            completionHandler([[.banner, .list, .sound]])
        }
        else
        {
            completionHandler([[.alert, .sound]])
        }
    }

    func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void)
    {
        let userInfo = response.notification.request.content.userInfo
        Messaging.messaging().appDidReceiveMessage(userInfo)
        print(userInfo)
        completionHandler()
    }

    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable : Any], fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void)
    {
        if let messageID = userInfo[gcmMessagingIDKey]
        {
            print("Message ID: \(messageID)")
        }
        print(userInfo)
        completionHandler(UIBackgroundFetchResult.newData)
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

    func applicationDidEnterBackground(_ application: UIApplication) {
        UserDefaults.standard.setValue(String(), forKey: "userID")
        UserDefaults.standard.setValue(String(), forKey: "userName")
        UserDefaults.standard.setValue(String(), forKey: "email")
        UserDefaults.standard.setValue(String(), forKey: "employeeNumber")
        UserDefaults.standard.setValue(false, forKey: "isLogged")
        UserDefaults.standard.setValue(String(), forKey: "rolName")
        UserDefaults.standard.setValue(String(), forKey: "hierarchyLevek")
    }

}
