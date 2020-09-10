package com.apicci.morsevibrate

import android.widget.Toast

class MorseTranslate() {

    private lateinit var outputString: String;

    companion object {
        public fun TextToMorse(inputString: String): String {
            var builder: StringBuilder = StringBuilder()
            for(i in inputString.indices){
                var output: String = when(inputString[i].toLowerCase()){
                    ' ' -> "s"
                    'a' -> ".-"
                    'b' -> "-..."
                    'c' -> "-.-."
                    'd' -> "-.."
                    'e' -> "."
                    'f' -> "..-."
                    'g' -> "--."
                    'h' -> "...."
                    'i' -> ".."
                    'j' -> ".---"
                    'k' -> "-.-"
                    'l' -> ".-.."
                    'm' -> "--"
                    'n' -> "-."
                    'o' -> "---"
                    'p' -> ".--."
                    'q' -> "--.-"
                    'r' -> ".-."
                    's' -> "..."
                    't' -> "-"
                    'u' -> "..-"
                    'v' -> "...-"
                    'w' -> ".--"
                    'x' -> "-..-"
                    'y' -> "-.--"
                    'z' -> "--.."
                    '1' -> ".----"
                    '2' -> "..---"
                    '3' -> "...--"
                    '4' -> "....-"
                    '5' -> "....."
                    '6' -> "-...."
                    '7' -> "--..."
                    '8' -> "---.."
                    '9' -> "----."
                    '0' -> "-----"
                    else -> "n"
                }
                builder.append(output)
            }

            return builder.toString()

        }
    }


}