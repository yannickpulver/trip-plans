import SwiftUI
import shared
import FirebaseCore

@main
struct iOSApp: App {
    init() {
        KoinModuleKt.doInitKoin()
        FirebaseApp.configure()
    }
    
	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}
