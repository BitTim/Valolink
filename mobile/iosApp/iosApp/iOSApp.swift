import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView()
                .onOpenURL(perform: { url in
                    SupabaseDeeplinkHandlerIos.shared.handleDeeplink(url: url)
                })
        }
    }
}
