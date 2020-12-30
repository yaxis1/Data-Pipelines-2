case class PrecipitationSample(
    Station: String, 
    StationName: String, 
    Elevation: Double, 
    Latitude: Double, 
    Longitude: Double, 
    Date: String, 
    Hpcp: Int, 
    MeasurementFlag: String, 
    QualityFlag: String
)

object dataset {

    val data="""STATION,STATION_NAME,ELEVATION,LATITUDE,LONGITUDE,DATE,HPCP,Measurement Flag,Quality Flag
  COOP:310301,ASHEVILLE NC US,682.1,35.5954,-82.5568,20100101 00:00,99999,],
  COOP:310301,ASHEVILLE NC US,682.1,35.5954,-82.5568,20100101 01:00,0,g,
  COOP:310301,ASHEVILLE NC US,682.1,35.5954,-82.5568,20100102 06:00,1, ,
"""

//Function takes a string and converts it to Dataset

def convertToDataset(csvData: String): List[String] = {
    
    //Converting string to list inorder to be able to split header from rows, also lists are great!
    
    val dataa = data.split("\\n").map(_.trim).toList

    dataa    
}
















}