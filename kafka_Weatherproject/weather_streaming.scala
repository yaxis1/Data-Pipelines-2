package streaming_scala

import sparksql
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{StringType, StructType, DateType} 
import org.apache.spark.SparkContext

object weather_streaming{

    def main(args: Array[String]): Unit = {
        val KAFKA_TOPIC_NAME = "Openweather"
        val KAFKA_BOOTSTRAP_SERVERS = '3.239.36.11:9092'
        System.setProperty("HADOOP_USER_NAME",'ubuntu')

        val spark = SparkSession.builder.master(master="local[*]")
            .appName(name = "Weather Streaming")
            .getOrCreate()

        spark.sparkContext.setLogLevel("ERROR")

        //From kafka
        val weather_detail_df = spark.readStream
            .format(source = "kafka")
            .option("kafka.bootstrap.servers", KAFKA_BOOTSTRAP_SERVERS)
            .option("subscribe", KAFKA_TOPIC_NAME)
            .option("startingOffsets","latest")
            .load()

        println("Schema for weather")
        weather_detail_df.printSchema()

        val weather_detail_df_1 = weather_detail_df.selectExpr(exprs = "CAST(value AS STRING)", "CAST(timestamp AS TIMESTAMP)")
        
        //Define a schema for transaction_detail data

        val transaction_detail_schema = StructType(Array(

            StructField("CityName", StringType),
            StructField("Temperature", DoubleType),
            StructField("Humidity", IntegerType),
            StructField("CreationTime", StringType)
        ))

        val weather_detail_df_2 = weather_detail_df_1.select

            (from_json(col(colName="value"), transaction_detail_schema)
            .as(alias="weather_detail"),col(colName="timestamp"))

        val weather_detail_df_3 = weather_detail_df_2.select(col="weather_detail.*", cols="timestamp")
        
        val weather_detail_df_4 = weather_detail_df_3.withColumn(colName="CreationDate",
        weather_detail_df_3("CreationTime").cast(DateType))

        println("Schema for weather_detail_df_4")

        weather_detail_df_4.printSchema()

        val weather_detail_df_5 = weather_detail_df_4.select(col = "CityName",
        cols = "Temperature", "Humidity", "CreationTime", "CreationDate")

        println("Schema for weather_deatail_df_5")

        weather_deatail_df_5.printSchema()
        
        // Writing result to console
        val weather_detail_write_stream = weather_deatail_df_5
            .writeStream 
            .trigger(Trigger.ProcessingTime(interval="10 Seconds"))
            .outputMode(outputMode = "append")
            .option("truncate","false")
            .format(source = "console")
            .start()
        
        // Writing result to hdfs
        weather_deatail_df_5.writeStream
            .format(source="csv")
            .option("path", "pathtohdfs")
            .option("checkpointLocation", "pathtohdfs")
            .start()
        weather_detail_write_stream.awaitTermination()
    
    }




}
