case class Sample(
    station: String,
    stationName: String,
    elevation: Double,
    latitute: Double,
    longitude: Double,
    date: String,
    hpcp: Int,
    measurement: String,
    qualityFlag: String)

case class SAMPLE(
    Station: String,
    StationName: String,
    Elevation: Double,
    Latitude: Double,
    Longitude: Double,
    Date: String,
    Hpcp: Int,
    MeasurementFlag: String,
    QualityFlag: String )


object App {
    val PATH = "/home/starman/scala"
    
    //"C:/Users/verce/AppData/Local/Packages/CanonicalGroupLimited.UbuntuonWindows_79rhkp1fndgsc/LocalState/rootfs/home/starman/scala"
    val EXPECTED_HEADER = "STATION,STATION_NAME,ELEVATION,LATITUDE,LONGITUDE,DATE,HPCP,Measurement Flag,Quality Flag"

    def readFile(path: String = PATH): List[String] = scala.io.Source.fromFile(new java.io.File(path)).getLines().toList
    
    def validateFile(data: List[String]) = {
        if (data(0) != EXPECTED_HEADER) {
            throw new Exception("INVALID HEADER") 
        } 
        else {println("File is validated")}
    }

    def processFile(data: List[String]) = {
        println(data(1))
    }
    
    //function takes a "line" i.e first line from "data" and returns "Sample" object
    def toSample(line: String): Sample = {
        val Array(
            station: String,
            stationName: String,
            elevation_string: String,
            latitute_string: String,
            longitude_string: String,
            date: String,
            hpcp_string: String,
            measurement: String,
            qualityFlag: String 
        ) = line.split(",") 
         

        val s = Sample(
            station,
            stationName,
            elevation_string.toDouble,
            latitute_string.toDouble,
            longitude_string.toDouble,
            date,
            hpcp_string.toInt,
            measurement,
            qualityFlag
        )
        return s
    }

    //capitalize single word
    def toPascalCaseSingle(x: String): String = x(0).toString.capitalize + x.substring(1).toLowerCase()

    //mapping capitalize function to multiple words seperated by _ and space 
    def toPascalCase(y: String): String = y.split("[ _]").map(toPascalCaseSingle).mkString 
    
    //mapping toPascalCase function to entire header string.
    def headertopascal(header: String):String = header.split(",").map(toPascalCase).mkString(",")

 
    def main(args: Array[String]) = {
        val data = readFile(PATH + "/badfile1.txt")
        validateFile(data) 
        processFile(data)
        headertopascal(data(0))
    }
}


