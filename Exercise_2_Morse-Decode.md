```scala

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
```
  
```scala

    // This function takes a string and returns the corresponding character as mapped in morse val.
    def morseDecode(s: String) = {
  
        printf(morse.filter(_._2 == s).map(_._1).mkString(" "))
    }
```

```scala

    def main(args: Array[String]) = {
        args.map(morseDecode)
        println(" ")
    }
}
```

![exercise2](https://user-images.githubusercontent.com/38083799/112092286-7be72300-8b97-11eb-91a7-1777867ee69d.png)



