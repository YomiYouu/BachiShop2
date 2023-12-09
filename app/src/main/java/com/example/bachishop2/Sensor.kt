package com.example.bachishop2

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.PowerManager
import android.os.PowerManager.WakeLock
import android.util.Log
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.bachishop2.databinding.ActivitySensorBinding

class Sensor : AppCompatActivity(), SensorEventListener {
    private lateinit var binding: ActivitySensorBinding
    private lateinit var sensorManager: SensorManager
    private var lightSensor: Sensor? = null
    private var proxi: Sensor? = null
    private lateinit var relativeLayout: RelativeLayout
    private lateinit var textView: TextView
    private lateinit var powerManager: PowerManager
    private lateinit var wakeLock: PowerManager.WakeLock


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySensorBinding.inflate(layoutInflater)
        setContentView(binding.root)



        relativeLayout = binding.relativeLayout
        textView = binding.textView
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(
            PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK,
            "com.example.sensoriluminacionb:SensorProximityWakeLock"
        )

        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        proxi = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        if (lightSensor == null) {

        }
        binding.imageView.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        lightSensor?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
        proxi?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        if(wakeLock.isHeld){
            wakeLock.release()
            Log.d("proximitySensor", "Liberando WakeLock")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
            val lightValue = event.values[0]

            textView.text = "Nivel de Iluminación: $lightValue lux"

            val backgroundColor = getColorForLightLevel(lightValue)
            relativeLayout.setBackgroundColor(backgroundColor)
        }
        if(event?.sensor?.type == Sensor.TYPE_PROXIMITY){
            val proxivalue = event.values[0]

            val backgroundColor = getColorForProximidad(proxivalue)
            relativeLayout.setBackgroundColor(backgroundColor)
        }
    }

    private fun getColorForLightLevel(lightValue: Float): Int {

        val minLight = 0f
        val maxLight = 10000f

        /**
         * val hue = 120f  // colores
         * val saturacion = 1f  // completamente saturado
         * val valor = 1f  // brillo completo
         *val color = Color.HSVToColor(floatArrayOf(hue, saturation, value))
         *
         */

        val hue = (lightValue - minLight) / (maxLight - minLight) * 120f // Hue en el rango 0-120
        return Color.HSVToColor(floatArrayOf(hue, 1f, 1f))
    }
    private fun getColorForProximidad(proxivalue: Float): Int {

        val minLight = 0f
        val maxLight = 10000f
        val cambiocolor = 0.5f
        return if (proxivalue <= cambiocolor) {
            Color.BLACK //
        } else{

            /**
             * val hue = 120f  // colores
             * val saturacion = 1f  // completamente saturado
             * val valor = 1f  // brillo completo
             *val color = Color.HSVToColor(floatArrayOf(hue, saturation, value))
             *
             */

            val hue = (proxivalue - minLight) / (maxLight - minLight) * 120f // Hue en el rango 0-120
            return Color.HSVToColor(floatArrayOf(hue, 1f, 1f))
        }
    }
}
