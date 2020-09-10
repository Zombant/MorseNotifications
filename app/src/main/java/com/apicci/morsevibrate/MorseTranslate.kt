package com.apicci.morsevibrate

class MorseTranslate() {
    companion object {

        //Space between symbols handled in MainService.kt
        val SPACE_BETWEEN_LETTERS_SYMBOL = "l"
        val SPACE_BETWEEN_WORDS_SYMBOL = "s"

        public fun TextToMorse(inputString: String?): String? {
            if(inputString == null){
                return null;
            }

            var builder: StringBuilder = StringBuilder()
            for(i in inputString.indices){
                var output: String = when(inputString.get(i).toLowerCase()){
                    ' ' -> SPACE_BETWEEN_WORDS_SYMBOL
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
                    '&' -> ".-..."
                    '\'' -> ".----."
                    '@' -> ".--.-."
                    ')' -> "-.--.-"
                    '(' -> "-.--."
                    ':' -> "---..."
                    ',' -> "--..--"
                    '=' -> "-...-"
                    '!' -> "-.-.--"
                    '.' -> ".-.-.-"
                    '-' -> "-....-"
                    '+' -> ".-.-."
                    '"' -> ".-..-."
                    '?' -> "..--.."
                    '/' -> "-..-."
                    else -> "n"
                }
                builder.append(output)
                builder.append(SPACE_BETWEEN_LETTERS_SYMBOL)
            }

            return builder.toString()

        }
    }


}