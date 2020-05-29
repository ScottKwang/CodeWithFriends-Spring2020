//
//  CardView.swift
//  Kana Card
//
//  Created by Ádám Balogh on 19/05/2020.
//  Copyright © 2020 Ádám Balogh. All rights reserved.
//

import SwiftUI



struct CardView: View {
    @State var kanaIsShown = false
    @State var shownKana = ""
    @State var buttonText = "Start"
    @Environment (\.colorScheme) var colorScheme: ColorScheme

    @ViewBuilder
    var body: some View {
        VStack(){
            GeometryReader{g in
                Text(self.buttonText)
                    .font(.system(size: g.size.height > g.size.width ? g.size.width * 0.2: g.size.height * 0.2))
                    .fontWeight(.bold)
                    .foregroundColor(self.colorScheme == .dark ? Color.white : Color.black)
            }
            }.frame(minWidth: 0, maxWidth: .infinity, minHeight: 0, maxHeight: .infinity, alignment: .topLeading)
            .background(self.colorScheme == .dark ? Color.black : Color.white)
            .onTapGesture {
                kana.buildMasterArray()
                if kana.masterArray.isEmpty {
                    self.buttonText = "Select at least 1 category"
                }
                else if self.kanaIsShown {
                    self.shownKana = kana.convertKanaToRomaji(self.shownKana)
                    self.buttonText = self.shownKana
                    self.kanaIsShown.toggle()
                }
                else {
                    self.shownKana = kana.getRandomKana(kana.masterArray)
                    self.buttonText = self.shownKana
                    self.kanaIsShown.toggle()
                }
        }
    }
}

struct CardView_Previews: PreviewProvider {
    static var previews: some View {
        CardView()
    }
}

struct DarkCardView_Previews: PreviewProvider {
    static var previews: some View {
        CardView()
            .colorScheme(.dark)
    }
}
