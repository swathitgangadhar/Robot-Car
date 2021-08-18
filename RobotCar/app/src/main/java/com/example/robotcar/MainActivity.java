package com.example.robotcar;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends Activity implements SensorEventListener {
    private static final String TAG = "MainActivity";
    private SensorManager mSensorManager;
    private Sensor mOrientation;
    private MqttClient mqttClient;
    private String clientID = "Robot";
    private String passwd = "maker";
    private String username = "robot";
    private String hostAddress = "tcp://ip address"; //add the ip address
    private int port = 1883;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getString(R.string.app_name));
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        try {
            mqttClient = new MqttClient(hostAddress, clientID, null);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setKeepAliveInterval(180);
            options.setPassword(passwd.toCharArray());
            options.setUserName(username);
            mqttClient.connect(options);
            Toast.makeText(this, "Connected!!", Toast.LENGTH_LONG).show();

        } catch (MqttException e) {
            Log.d(TAG, "onCreate: " + e);
            e.printStackTrace();
            Toast.makeText(this, "Cannot connect to MQTT. Please try again later!!", Toast.LENGTH_LONG).show();
        }


    }


    @Override
    public synchronized void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mOrientation, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float azimuth_angle = event.values[0];
        float pitch_angle = event.values[1];
        float roll_angle = event.values[2];
        Log.d(TAG, ":onSensorChanged " + azimuth_angle + "," + pitch_angle + "," + roll_angle);

        if (mqttClient != null) {
            if (mqttClient.isConnected()) {
                try {
                    String msg = "{\"azimuth\":" + azimuth_angle +
                            ", \"pitch\": " + pitch_angle + ",\"roll\": " + roll_angle + "}";
                    mqttClient.publish("sensor/myrobot", new MqttMessage(msg.getBytes()));
                } catch (Exception e) {
                    Log.d(TAG, "onSensorChanged: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG, "onAccuracyChanged: ");
    }
}