def toPascalCaseSingle(x: String): String = x(0).toString.capitalize + x.substring(1).toLowerCase()

    //mapping capitalize function to multiple words seperated by _ and space 
def toPascalCase(y: String): String = y.split("[ _]").map(toPascalCaseSingle).mkString 
    
    //mapping toPascalCase function to entire header string.
def headertopascal(header: String):String = header.split(",").map(toPascalCase).mkString(",")

def normalizeHeader(df: DataFrame): DataFrame = {

    val fieldNames = df.schema.fieldNames
    var tempDf = df
    (0 to fieldNames.size-1).foreach { i => 

    
        val currentField = fieldNames(i)
        val updatedFiledName = toPascalCase(currentField)

        if (currentField!= updatedFiledName) {
            tempDf = tempDf.withColumnRenamed(currentField,updatedFiledName)

        }
    
    
    }
}