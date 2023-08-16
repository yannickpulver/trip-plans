import SwiftUI
import shared




struct ContentView: View {
	var body: some View {
        ZStack {
		ComposeView()
            .ignoresSafeArea(.all)
        }
	}
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
