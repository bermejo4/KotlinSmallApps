package com.example.aceleromtros

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import android.widget.TextView
import android.widget.Toast

class Acelerometros : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private lateinit var xText: TextView
    private lateinit var yText: TextView
    private lateinit var zText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acelerometros)
        Log.i("app",  "App lanzada")
        // Al arrancar la aplicación

        // Obtén una instancia del SensorManager
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // Verifica si el acelerómetro está disponible en el dispositivo
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (accelerometer == null) {
            // El acelerómetro no está disponible en este dispositivo
            Toast.makeText(this, "El acelerómetro no está disponible", Toast.LENGTH_SHORT).show()
        }

        xText = findViewById(R.id.textViewX)
        yText = findViewById(R.id.textViewY)
        zText = findViewById(R.id.textViewZ)

    }

    override fun onResume() {
        super.onResume()
        // Registra el escucha del acelerómetro cuando la actividad se reanuda
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }


    override fun onPause() {
        super.onPause()
        // Deja de escuchar el acelerómetro cuando la actividad está en pausa
        sensorManager.unregisterListener(this)
    }


    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            // Accede a los valores de los acelerómetros
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            // Haz lo que desees con los valores de los acelerómetros
            // Por ejemplo, mostrarlos en un TextView o enviarlos a un servidor.
            // En este ejemplo, simplemente los mostraremos en un Toast.
            val message = "Acelerómetro: X = $x, Y = $y, Z = $z"
            //Log.d("x", "acc x: $x")
            xText.text = "La componente x es: $x"
            yText.text = "La componente y es: $y"
            zText.text = "La componente z es: $z"

        }
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No se utiliza en este ejemplo
    }
}

