//
//  ContentView.swift
//  Kana Card
//
//  Created by Ádám Balogh on 19/05/2020.
//  Copyright © 2020 Ádám Balogh. All rights reserved.
//

import SwiftUI

struct ContentView: View {
    @State var hiraganaGojuonToggle = true
    @State var hiraganaDakutenToggle = true
    @State var hiraganaYoonToggle = true
    @State var katakanaGojuonToggle = true
    @State var katakanaDakutenToggle = true
    @State var katakanaYoonToggle = true
    
    @Environment (\.colorScheme) var colorScheme: ColorScheme
    
    func changeCheckmark(classToggle: inout Bool, stateToggle: Bool) -> String {
        kana.resetMasterArray()
        classToggle = Bool(stateToggle)
        if stateToggle {
            return "checkmark.square"
        }
        else {
            return "square"
        }
    }
    
    func switchStateToggle(_ stateToggle: inout Bool) {
        if stateToggle {
            stateToggle = false
        }
        else {
            stateToggle = true
        }
    }
    
    var body: some View {
        NavigationView{
            VStack{
                HStack{
                    Text("Hiragana Gojūon")
                        .font(.headline)
                    Spacer()
                    Button(
                        action: {
                            self.switchStateToggle(&self.hiraganaGojuonToggle)
                    }) {
                        Image(systemName: changeCheckmark(classToggle: &kanaToggles.hiraganaGojuon, stateToggle: hiraganaGojuonToggle))
                            .font(.system(size: 40))
                            .accentColor(colorScheme == .dark ? .white : .black)
                    }
                }
                HStack{
                    Text("Hiragana Dakuten")
                    .font(.headline)
                    Spacer()
                    Button(
                        action: {
                            self.switchStateToggle(&self.hiraganaDakutenToggle)
                        }) {
                            Image(systemName: changeCheckmark(classToggle: &kanaToggles.hiraganaDakuten, stateToggle: hiraganaDakutenToggle))
                            .font(.system(size: 40))                            .accentColor(colorScheme == .dark ? .white : .black)

                        }
                }
                HStack{
                    Text("Hiragana Yōon")
                    .font(.headline)
                    Spacer()
                    Button(
                        action: {
                            self.switchStateToggle(&self.hiraganaYoonToggle)
                        }) {
                            Image(systemName: changeCheckmark(classToggle: &kanaToggles.hiraganaYoon, stateToggle: hiraganaYoonToggle))
                            .font(.system(size: 40))                            .accentColor(colorScheme == .dark ? .white : .black)

                        }
                }
                HStack{
                    Text("Katakana Gojūon")
                    .font(.headline)
                    Spacer()
                    Button(
                        action: {
                            self.switchStateToggle(&self.katakanaGojuonToggle)
                        }) {
                            Image(systemName: changeCheckmark(classToggle: &kanaToggles.katakanaGojuon, stateToggle: katakanaGojuonToggle))
                            .font(.system(size: 40))                            .accentColor(colorScheme == .dark ? .white : .black)

                        }
                }
                HStack{
                    Text("Katakana Dakuten")
                    .font(.headline)
                    Spacer()
                    Button(
                        action: {
                            self.switchStateToggle(&self.katakanaDakutenToggle)
                        }) {
                            Image(systemName: changeCheckmark(classToggle: &kanaToggles.katakanaDakuten, stateToggle: katakanaDakutenToggle))
                            .font(.system(size: 40))                            .accentColor(colorScheme == .dark ? .white : .black)

                        }
                }
                HStack{
                    Text("Katakana Yōon")
                    .font(.headline)
                    Spacer()
                    Button(
                        action: {
                            self.switchStateToggle(&self.katakanaYoonToggle)
                        }) {
                            Image(systemName: changeCheckmark(classToggle: &kanaToggles.katakanaYoon, stateToggle: katakanaYoonToggle))
                            .font(.system(size: 40))                            .accentColor(colorScheme == .dark ? .white : .black)

                        }
                }
                Spacer()
                NavigationLink(destination: CardView()) {
                    Text("Next").font(.title).bold()
                }
            }.padding()
            .navigationBarTitle(Text("Categories"))
                .onDisappear(perform: kana.buildMasterArray)
        }
        .padding(0.0)
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}

struct DarkContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
            .colorScheme(/*@START_MENU_TOKEN@*/.dark/*@END_MENU_TOKEN@*/)
    }
}
