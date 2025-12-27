//
//  ContentView.swift
//  lazyinfinitegridios
//
//  Created by Sachin Bijalwan on 23/12/25.
//

import SwiftUI
import ComposeApp

struct ContentView: View {
    var body: some View {
        ComposeView()
            .edgesIgnoringSafeArea(.all)
    }
}

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

#Preview {
    ContentView()
}
