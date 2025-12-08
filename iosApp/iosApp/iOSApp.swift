import SwiftUI
import FirebaseCore
import FirebaseMessaging
import ComposeApp
import BackgroundTasks


class AppDelegate: NSObject, UIApplicationDelegate {

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil
    ) -> Bool {

        FirebaseApp.configure()
        AppInitializer.shared.onApplicationStart()

        schedulePrayerRefresh()
        return true
    }

    func application(
        _ application: UIApplication,
        didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data
    ) {
        Messaging.messaging().apnsToken = deviceToken
    }
}

func schedulePrayerRefresh() {
    print("📆 schedulePrayerRefresh called")
    let request = BGAppRefreshTaskRequest(identifier: "com.knight.salah.Salah.prayerRefresh")
    request.earliestBeginDate = Date(timeIntervalSinceNow: 6 * 60 * 60)

    do {
        try BGTaskScheduler.shared.submit(request)
        print("✅ BG task submitted")
    } catch {
        print("❌ Failed to submit BG task: \(error)")
    }
}


func handlePrayerRefreshTask(task: BGAppRefreshTask) {
    print("🔥 BGAppRefreshTask started at \(Date())")

    schedulePrayerRefresh()

    PrayerBgWorker.shared.handle(daysToSchedule: 7)

    task.expirationHandler = {
        print("⚠️ BGAppRefreshTask expired")
    }

    task.setTaskCompleted(success: true)
    print("✅ BGAppRefreshTask completed")
}



@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
    
    
    init() {
        print("🪵 Registering BG task handler")
        BGTaskScheduler.shared.register(
            forTaskWithIdentifier: "com.knight.salah.Salah.prayerRefresh",
            using: nil
        ) { task in
            print("⚡️ BGTaskScheduler handler invoked")
            handlePrayerRefreshTask(task: task as! BGAppRefreshTask)
        }
    }

    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

