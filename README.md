# Robot-Car
Controlling the movement of the Car with an Orientation sensor of the Android phone. 

# Working system:
The idea behind the Robot car to operate the car with the simple use of movements from an Android device. This project involves two types of methods of moving the robot, which is Automatic mode and Manual mode. In the Automatic mode, the robot is made to follow the line using the PID controller. whereas in the Manual mode, the robot is controlled using the Orientation sensor in the Android device. The switch between the two of them is handled in the Android app. The  MQTT server is used to communicate with the Android app and the robot.

Initially, the robot is set to Manual mode and the MQtt is set up and connected both to the robot and the app. When the Automatic mode is switched on in the app, in the robot program the Automatic code is initiated. Using the PID controllers the Speed, Proportional gain, Integral gain and Derivative is entered in the app and passed to the Robot through the MQTT, the Robot moves follwing the defined path. 









https://user-images.githubusercontent.com/88834520/149845324-5d1b3a13-fb6b-48da-a500-20024697564a.mp4

