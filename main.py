#!/usr/bin/env pybricks-micropython
from pybricks.hubs import EV3Brick
from umqtt.robust import MQTTClient
import json
from pybricks.ev3devices import (Motor, TouchSensor, ColorSensor,
                                 InfraredSensor, UltrasonicSensor, GyroSensor)
from pybricks.parameters import Port, Stop, Direction, Button, Color
from pybricks.robotics import DriveBase
from time import sleep

#initialize evBrick and motors
ev3 = EV3Brick()
left_motor = Motor(Port.B)
right_motor = Motor(Port.C)

robot = DriveBase(left_motor, right_motor, wheel_diameter=55.5, axle_track=104)
ev3.speaker.set_volume(100)
ev3.speaker.set_speech_options(language='en')

#define function which decodes the messages that
# is received from MQTT
def getmessages(topic, msg):
    if str(topic.decode()) == "sensor/myrobot":
        json_str = str(msg.decode())
        print(json_str)
        ev3.screen.print(json_str)
        data = json.loads(json_str)
        ev3.screen.print(round(data['azimuth'],2), round(data['pitch'],2), round(data['roll'],2), sep =",")

        pitch = int(data['roll'])/1.2
        azimuth = int(data['azimuth'])
        print(pitch,azimuth)
        if(0<azimuth<90):
            robot.drive(pitch,0)
        elif(91<azimuth<180):
            robot.drive(pitch,90) 
        elif(181<azimuth<270):
            robot.drive(pitch,180) 
        elif(271<azimuth<359):
            robot.drive(pitch,270) 
         
#define MQTT with name, IP address , username and pw
client = MQTTClient("EV3_Robot","ip address",user = "robot",password="maker")
client.connect()
client.set_callback(getmessages)
client.subscribe("sensor/myrobot/#")

print("Connected and listening....")

while True:
    client.check_msg()
