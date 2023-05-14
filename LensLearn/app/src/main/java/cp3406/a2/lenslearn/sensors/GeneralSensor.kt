package cp3406.a2.lenslearn.sensors

import kotlin.Unit

abstract class GeneralSensor(
    protected val sensorType: Int
) {

    protected var onSensorValuesChanged: ((List<Float>) -> Unit)? = { }

    abstract val doesSensorExist: Boolean

    abstract fun startListening()

    abstract fun stopListening()

    fun setOnSensorValuesChangedListener(listener: (List<Float>) -> Unit) {
        onSensorValuesChanged = listener
    }
}
