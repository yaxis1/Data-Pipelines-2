object MorseDecoding {

    // From wikipedia - mapping each char to its corresponding morse.
    val morse = Map(
    'A' ->".-", 'B' -> "-...", 'C' -> "-.-.", 'D' -> "-..", 'E' -> ".",
    'F' -> "..-.", 'G' -> "--.",  'H' -> "....", 'I' -> "..", 'J' -> ".---",
    'K' -> "-.-", 'L' -> ".-..", 'M' -> "--", 'N' -> "-.", 'O' -> "---",
    'P' -> ".--.", 'Q' -> "--.-", 'R' -> ".-.", 'S' -> "...", 'T' -> "-",
    'U' -> "..-", 'V' -> "...-", 'W' -> ".--", 'X' -> "-..-", 'Y' -> "-.--",
    'Z' -> "--..", '0' -> "-----", '1' -> ".----", '2' -> "..---", '3' -> "...--",
    '4' -> "....-", '5' -> ".....", '6' -> "-....", '7' -> "--...", '8' -> "---..",
    '9' -> "----."
    )
  
    // This function takes a string and returns the corresponding character as mapped in morse val.
    def morseDecode(s: String) = {
        
        printf(morse.filter(_._2 == s).map(_._1).mkString(" "))
    }

    def main(args: Array[String]) = {
        args.map(morseDecode)
        println(" ")
    }
}