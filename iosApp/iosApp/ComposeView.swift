//
//  ComposeView.swift
//  iosApp
//
//  Created by Yannick Pulver on 13.07.23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared
import SwiftUI

struct ComposeView: UIViewControllerRepresentable {
    func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context) {
        
    }
    
    func makeUIViewController(context: Context) -> some UIViewController {
        let controller = MainViewControllerKt.MainViewController()
        controller.overrideUserInterfaceStyle = .light
        return controller
    }
}
