/**
 * Abstract sensor class for general purposes.
 */
package cp3406.a2.lenslearn.sensors

import cp3406.a2.lenslearn.model.CategoryViewModel
import kotlin.Unit

abstract class GeneralSensor(
    protected val sensorType: Int, protected val categoryViewModel: CategoryViewModel
) {

    // Senors return different number of values, list makes it multi-purpose
    protected var onSensorValuesChanged: ((List<Float>) -> Unit)? = { }

    abstract val doesSensorExist: Boolean

    abstract fun startListening()

    abstract fun stopListening()

    fun setOnSensorValuesChangedListener(listener: (List<Float>) -> Unit) {
        onSensorValuesChanged = listener
    }
}
