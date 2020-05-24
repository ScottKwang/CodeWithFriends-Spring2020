//
//  Kanas.swift
//  Kana Card
//
//  Created by Ádám Balogh on 19/05/2020.
//  Copyright © 2020 Ádám Balogh. All rights reserved.
//

import Foundation

class Kana {
    let hiraganaGojuon = ["あ", "い", "う", "え", "お",
                          "か", "き", "く", "け", "こ",
                          "さ", "し", "す", "せ", "そ",
                          "た", "ち", "つ", "て", "と",
                          "な", "に", "ぬ", "ね", "の",
                          "は", "ひ", "ふ", "へ", "ほ",
                          "ま", "み", "む", "め", "も",
                          "や", "ゆ", "よ",
                          "ら", "り", "る", "れ", "ろ",
                          "わ", "を",
                          "ん"]
    let hiraganaDakuten = ["が", "ぎ", "ぐ", "げ", "ご",
                           "ざ", "じ", "ず", "ぜ", "ぞ",
                           "だ", "ぢ", "づ", "で", "ど",
                           "ば", "び", "ぶ", "べ", "ぼ",
                           "ぱ", "ぴ", "ぷ", "ぺ", "ぽ"]
    let hiraganaYoon = ["きゃ", "きゅ", "きょ",
                        "しゃ", "しゅ", "しょ",
                        "ちゃ", "ちゅ", "ちょ",
                        "にゃ", "にゅ", "にょ",
                        "ひゃ", "ひゅ", "ひょ",
                        "みゃ", "みゅ", "みょ",
                        "りゃ", "りゅ", "りょ",
                        "ぎゃ", "ぎゅ", "ぎょ",
                        "じゃ", "じゅ", "じょ",
                        "びゃ", "びゅ", "びょ",
                        "ぴゃ", "ぴゅ", "ぴょ"]
    let katakanaGojuon = ["ア", "イ", "ウ", "エ", "オ",
                          "カ", "キ", "ク", "ケ", "コ",
                          "サ", "シ", "ス", "セ", "ソ",
                          "タ", "チ", "ツ", "テ", "ト",
                          "ナ", "ニ", "ヌ", "ネ", "ノ",
                          "ハ", "ヒ", "フ", "ヘ", "ホ",
                          "マ", "ミ", "ム", "メ", "モ",
                          "ヤ", "ユ", "ヨ",
                          "ラ", "リ", "ル", "レ", "ロ",
                          "ワ", "ヲ",
                          "ン"]
    let katakanaDakuten = ["ガ", "ギ", "グ", "ゲ", "ゴ",
                           "ザ", "ジ", "ズ", "ゼ", "ゾ",
                           "ダ", "ヂ", "ヅ", "デ", "ド",
                           "バ", "ビ", "ブ", "ベ", "ボ",
                           "パ", "ピ", "プ", "ペ", "ポ"]
    let katakanaYoon = ["キャ", "キュ", "キョ",
                        "シャ", "シュ", "ショ",
                        "チャ", "チュ", "チョ",
                        "ニャ", "ニュ", "ニョ",
                        "ヒャ", "ヒュ", "ヒョ",
                        "ミャ", "ミュ", "ミョ",
                        "リャ", "リュ", "リョ",
                        "ギャ", "ギュ", "ギョ",
                        "ジャ", "ジュ", "ジョ",
                        "ビャ", "ビュ", "ビョ",
                        "ピャ", "ピュ", "ピョ"]
    
    var masterArray: Array<String> = []
    
    var last10Kana: Array<String> = []
    
    var kanaCharacter : String = ""
    
    func getRandomKana(_ characterSet: Array<String>) -> String {
        if masterArray.isEmpty {
            return ""
        }
        else {
            repeat {
                kanaCharacter = characterSet[Int(arc4random_uniform(UInt32(characterSet.count-1)))]
            } while last10Kana.contains(kanaCharacter)
            if last10Kana.count > 9 {
                last10Kana.remove(at: 0)
                last10Kana.append(kanaCharacter)
            }
            else {
                last10Kana.append(kanaCharacter)
            }
            return kanaCharacter
        }
    }
    
    func convertKanaToRomaji(_ randomKana: String) -> String {
        let romaji = NSMutableString(string: randomKana)
        CFStringTransform(romaji, nil, kCFStringTransformHiraganaKatakana, true)
        CFStringTransform(romaji, nil, kCFStringTransformLatinHiragana, true)
        return romaji as String
    }
    
    func buildMasterArray () {
        if kanaToggles.hiraganaGojuon {
            masterArray += kana.hiraganaGojuon
        }
        if kanaToggles.hiraganaDakuten {
            masterArray += kana.hiraganaDakuten
        }
        if kanaToggles.hiraganaYoon {
            masterArray += kana.hiraganaYoon
        }
        if kanaToggles.katakanaGojuon {
            masterArray += kana.katakanaGojuon
        }
        if kanaToggles.katakanaDakuten {
            masterArray += kana.katakanaDakuten
        }
        if kanaToggles.katakanaYoon {
            masterArray += kana.katakanaYoon
        }
    }

    func resetMasterArray() {
        masterArray = []
    }

}

class KanaToggles {
    var hiraganaGojuon = true
    var hiraganaDakuten = true
    var hiraganaYoon = true
    var katakanaGojuon = true
    var katakanaDakuten = true
    var katakanaYoon = true
}

var kanaToggles = KanaToggles()

let kana = Kana()
