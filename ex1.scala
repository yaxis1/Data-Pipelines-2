import org.apache.spark.sql._
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._

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

def headerToPascalCase(header: String) = header.split("[ _]+").map(_.toLowerCase.capitalize).mkString("")


// This function takes a dataframe and headers string and returns a 
def normalizeHeaders(df: DataFrame, headers: String): DataFrame = {
  val fieldNames = df.schema.fieldNames
  val h = headers.split(",").toList
  var tempDF = df //This will be modified with renamed columns
  (0 to fieldNames.size - 1).foreach { i => 
    // for each element in field names
    val currentField = fieldNames(i)
    val currentHeader = h(i)
    val updatedFieldName = headerToPascalCase(currentHeader)
    // f : (DataFrame, String) => DataFrame
    tempDF = tempDF.withColumnRenamed(currentField, updatedFieldName )
  }
  tempDF
}

def arrayToTuple(values: Array[String]) = values match {
  case Array(a,b,c,d,e,f,g,h,i) => (a,b,c,d,e,f,g,h,i)
  case Array(a,b,c,d,e,f,g,h) =>  (a,b,c,d,e,f,g,h, "")  // because "a,".split(",") returns Array(a) instead of Array(a, "")
  case _ => ("","","","","","","","","") // if not recognized, empty values
}
//Function takes a string and converts it to Dataset

def convertToDataset(csvData: String): Dataset[PrecipitationSample] = {
    
  //Converting string to list inorder to be able to split header from rows, also lists are great!
  val headers :: rows = data.split("\n").toList
  var df = rows.map(_.split(",")).map(arrayToTuple).toDF
  df = normalizeHeaders(df, headers)
  df = df.withColumn("Elevation",col("Elevation").cast(DoubleType))
  df = df.withColumn("Latitude",col("Latitude").cast(DoubleType))
  df = df.withColumn("Longitude",col("Longitude").cast(DoubleType))
  df = df.withColumn("Hpcp", col("Hpcp").cast(IntegerType))

    return df.as[PrecipitationSample]
}

}