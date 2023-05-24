/**
 * Abstract sensor class specific to Android sensors for common purposes.
 */
package cp3406.a2.lenslearn.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import cp3406.a2.lenslearn.model.CategoryViewModel

abstract class AndroidSensor(
    private val context: Context,
    private val sensorFeature: String,
    sensorType: Int,
    categoryViewModel: CategoryViewModel
) : GeneralSensor(sensorType, categoryViewModel),
    SensorEventListener {

    override val doesSensorExist: Boolean
        get() = context.packageManager.hasSystemFeature(sensorFeature)

    private val sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val sensor: Sensor? = sensorManager.getDefaultSensor(sensorType)

    /** Start listening to sensor */
    override fun startListening() {
        if (!doesSensorExist || sensor == null) {
            return
        }
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    /** Stop listening to sensor */
    override fun stopListening() {
        if (sensor == null) {
            return
        }
        sensorManager.unregisterListener(this)
    }

    /** Sensor change */
    override fun onSensorChanged(event: SensorEvent?) {
        if (!doesSensorExist) {
            return
        }
        if (event?.sensor?.type == sensorType) {
            onSensorValuesChanged?.invoke(event.values.toList())
        }
    }

    /** Sensor Accuracy Change */
    override fun onAccuracyChanged(sensor: Sensor?, p1: Int) = Unit

}