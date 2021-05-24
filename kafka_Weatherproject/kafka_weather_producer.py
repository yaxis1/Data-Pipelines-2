from kafka import KafkaProducer
import time
import json
from json import dumps
import requests

KAFKA_TOPIC_NAME = "Openweather"
KAFKA_BOOTSTRAP_SERVERS = '3.239.36.11:9092'



kafka_producer_obj = KafkaProducer(bootstrap_servers=KAFKA_BOOTSTRAP_SERVERS,
                            value_serializer=lambda x: dumps(x).encode('utf-8'))

def get_weather_detail(openweather_apiendpoint):
    api_response = requests.get(openweather_apiendpoint)
    json_data = api_response.json()
    city_name = json_data['name']
    humidity = json_data['main']['humidity']
    temperature = json_data['main']['temp']
    json_message = {"CityName":city_name, "Temperatue": temperature, "Humidity": humidity, "CreationTime": time.strftime("%Y-%m-%d %H:%M:%S")}
    return json_message
def get_appid():
    return 'fad428a7fe71d3a7f648cba70fa84ca1'
    

while True:
    city_name = "Paris"
    appid = get_appid()
    openweather_apiendpoint = 'http://api.openweathermap.org/data/2.5/weather?appid='+appid+'&q='+city_name 
    json_message = get_weather_detail(openweather_apiendpoint)
    kafka_producer_obj.send(KAFKA_TOPIC_NAME,json_message)
    print("Published message 1: " + json.dumps(json_message))
    print("......")
    time.sleep(2)

if __name__ == "__main__":
    print("Kafka Producer Application Started ... ")

        
