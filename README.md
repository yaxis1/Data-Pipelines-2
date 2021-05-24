# Data-Pipelines-2
This repository demonstrates several scala usecases and a glimpse into it's potential when used in combination with other technologies.
- Java
- Kafka
- Spark

## Tree view
![image](https://user-images.githubusercontent.com/38083799/119356231-7ba5f980-bca6-11eb-9cbe-712fd60e2c32.png)

#### Install Kafka

Adding confluent's public key
```sh
$ wget -qO - http://packages.confluent.io/deb/3.0/archive.key | sudo apt-key add -
```

Adding repo to linux aws instance
```sh
sudo add-apt-repository 'deb [arch=amd64] http://packages.confluent.io/deb/3.0 stable main'
```
Installing confluent platform:
```sh
sudo apt-get update
sudo apt-get install confluent-platform-2.11
```
> /usr/bin/     # Driver scripts for starting/stopping services, prefixed with &lt;package&gt; names
> /etc/<package>/            # Configuration files
> /usr/share/java/<package>/ # Jars

#### Run Zookeper

```sh
sudo zookeeper-server-start /etc/kafka/zookeeper.properties
```

#### Start kafka
```sh
sudo kafka-server-start /etc/kafka/server.properties
```
#### Start Producer
```sh
kafka-console-producer --broker-list localhost:9092 --topic dsti
```



#### Start Consumer
```sh
 kafka-console-consumer --zookeeper localhost:2181 --topic dsti --from-beginning
```
The producer and consumer are running - this will be later implemented for KAFKA_TOPIC - Openweather
  ![image](https://user-images.githubusercontent.com/38083799/119328092-088b8b80-bc84-11eb-9f50-15c33348f2f0.png)

#### Installing spark
```sh
wget https://downloads.apache.org/spark/spark-3.0.1/spark-3.0.1-bin-hadoop2.7.tgz
```
  
  ![image](https://user-images.githubusercontent.com/38083799/119328173-1e994c00-bc84-11eb-9eae-6dac982c264c.png)

 
#### Installing sbt to package the project
```sh
echo "deb https://dl.bintray.com/sbt/debian /" | sudo tee -a /etc/apt/sources.list.d/sbt.list
curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | sudo apt-key add
sudo apt-get update
sudo apt-get install sbt 
```
 
#### Generating a .jar out of projects:
 Move to project folder
 ```sh
 sbt compile
 sbt run
 sbt package
 ```

### Spark use case
 Weather data needs to be imported from an open weather API that fetches data like temperature, humidity etc.
 
 To process this a schema needs to be formulated in spark using SparkSession, then we import spark.sql.functions to make further processing on imported data. 
 
 The package 'package streaming_scala' takes input from kafka producer - "kafka_weather_producer.py" - This producer contans an API that produces daily weather data. 
 
 The scala package reads from KAFKA_BOOTSTRAP_SERVERS and is subscribed to KAFKA_TOPIC_NAME - "OpenWeather", - the package first casts the date column as DATETYPE and temperature as DOUBLE - Generates a dataframe out of it.
 
 Necessary transformations are made and a final schema is generated. 
 
#### Importing real time weather data into spark data frame
 Schema:
 ![image](https://user-images.githubusercontent.com/38083799/119366713-87e38400-bcb1-11eb-8670-381efda868b6.png)

 The schema is now ready for input data
