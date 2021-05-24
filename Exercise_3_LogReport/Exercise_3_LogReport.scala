//Creating a class to hold log data
case class Log(ip: String, ident: String, user: String, 
                     datetime: String, request: String, status: String, 
                     size: String, referer: String, userAgent: String, unk: String)

object App{

    // Every element will be mapped to above class using this function 
    def toLog(params: List[String]) = Log(params(0), params(1), params(2), params(3), params(4), params(5), params(6), params(7), params(8), params(9))

    // Takes log file processes 
    def processLogs(inputPath: String = "access.log.gz", outputPath: String = "output") = {
        
        import org.apache.spark.sql.SparkSession //Will be used to filter logs using sparksql
 
        // Starting a spark session
        val spark = SparkSession.builder().appName("testings").master("local").getOrCreate
        
        import spark.implicits._
        import org.apache.spark.sql._
        import org.apache.spark.sql.functions._ 

        
        val logs = spark.read.text(inputPath)

        val logAsString = logs.map(_.getString(0))

        // Using regex to filter individual data based on the data types.
        val R = """^(?<ip>[0-9.]+) 
                    (?<identd>[^ ]) 
                    (?<user>[^ ]) 
                    \[(?<datetime>[^\]]+)\] 
                    \"(?<request>[^\"]*)\" 
                    (?<status>[^ ]*) 
                    (?<size>[^ ]*) 
                    \"(?<referer>[^\"]*)\" 
                    \"(?<useragent>[^\"]*)\" 
                    \"(?<unk>[^\"]*)\"""".r

        val dsParsed = logAsString.flatMap(x => R.unapplySeq(x))

        val ds = dsParsed.map(toLog _)

        // Formatting column datetime
        val dsWithTime = ds.withColumn("datetime", to_timestamp(ds("datetime"), "dd/MMM/yyyy:HH:mm:ss X"))

        val REQ_EX = "([^ ]+)[ ]+([^ ]+)[ ]+([^ ]+)".r

        val dsExtended = dsWithTime.withColumn("method", regexp_extract(dsWithTime("request"), REQ_EX.toString, 1)).withColumn("uri", regexp_extract(dsWithTime("request"), REQ_EX.toString, 2)).withColumn("http", regexp_extract(dsWithTime("request"), REQ_EX.toString, 3)).drop("request")

        dsExtended.cache
        
        // Creating a temporary data frame
        dsExtended.createOrReplaceTempView("Log")

        // Spark sql to filter all the dates having too big number of connection (> 20000)
        val dfSelectedLogs = spark.sql("""select t1.* from (select cast(datetime as date) as date, ip, uri from Log) as t1 join (select cast(datetime as date) as date, count(*) as count from Log group by date having count > 20000) as t2 on t1.date = t2.date""")
        
        dfSelectedLogs.createOrReplaceTempView("SelectedLogs") //Creating a temporary view for this filtered logs

        // Using sparksql functions to access logs by ip count, uri count and dates. 
        val accessByIpCount = spark.sql("select date, ip, count(*) as ipCount from SelectedLogs group by date, ip")
        val accessByUriCount = spark.sql("select date as uriDate, uri, count(*) as uriCount from SelectedLogs group by date, uri")
        val datesReport = spark.sql("select distinct(date) from SelectedLogs")
        
        val report = accessByIpCount.join(accessByUriCount, accessByUriCount("uriDate") === accessByIpCount("date")).drop("uriDate")

        val ipStruct = accessByIpCount.withColumn("IP", struct("ip","ipCount")).select("date", "IP").groupBy("date").agg(collect_list("IP").as("IP"))

        val uriStruct = accessByUriCount.withColumn("URI", struct("uri","uriCount")).select("uriDate", "URI").groupBy("UriDate").agg(collect_list("URI").as("URI"))

        // Merging above outputs into required format
        val mergedStruct = ipStruct.join(uriStruct,ipStruct("date") ===  uriStruct("uriDate"),"inner").drop("uriDate")

        // Generating json output merged struct
        mergedStruct.repartition(1).write.mode("Overwrite").json(outputPath)

        spark.close()
    }

    def createReport(gzPath: String, outputPath: String): Unit = {
        println("*Report:*")
        processLogs( gzPath, outputPath)
        println("Report generated!")
    }

    def main(args: Array[String]){
        createReport("access.log.gz", "output")
    }
}
