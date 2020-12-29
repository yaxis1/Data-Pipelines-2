case class Sample(
    station: String,
    stationName: String,
    elevation: Double,
    latitute: Double,
    longitude: Double,
    date: String,
    hpcp: Int,
    measurement: String,
    qualityFlag: String )

object App {
    val PATH = "C:/Users/verce/AppData/Local/Packages/CanonicalGroupLimited.UbuntuonWindows_79rhkp1fndgsc/LocalState/rootfs/home/starman/scala"
    val EXPECTED_HEADER = "STATION,STATION_NAME,ELEVATION,LATITUDE,LONGITUDE,DATE,HPCP,Measurement Flag,Quality Flag"

    def readFile(path: String = PATH): List[String] = scala.io.Source.fromFile(new java.io.File(path)).getLines().toList
    
    def validateFile(data: List[String]) = {
        if (data(0) != EXPECTED_HEADER) {
            throw new Exception("INVALID HEADER") 
        } 
    }

    def processFile(data: List[String]) = {
        println(data(1))
    }
    
    def toSample(line: String): Sample ={ 


    }

    def main(args: Array[String]) = {
        val data = readFile(PATH + "/badfile1.txt")
        validateFile(data) 
        processFile(data)}
}

