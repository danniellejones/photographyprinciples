package cp3406.a2.lenslearn.sensors

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import cp3406.a2.lenslearn.model.CategoryViewModel
import kotlin.math.sqrt
import kotlin.math.abs

/** Use Accelerometer for shaking the device */
class Accelerometer(context: Context, categoryViewModel: CategoryViewModel) : AndroidSensor(
    context = context,
    sensorFeature = PackageManager.FEATURE_SENSOR_ACCELEROMETER,
    sensorType = Sensor.TYPE_ACCELEROMETER,
    categoryViewModel = categoryViewModel
) {
    private val shakeThreshold = 15f

    override fun onSensorChanged(event: SensorEvent?) {
        super.onSensorChanged(event)

        val x = event?.values?.get(0) ?: 0f
        val y = event?.values?.get(1) ?: 0f
        val z = event?.values?.get(2) ?: 0f

        val magnitude = sqrt((x * x + y * y + z * z).toDouble()).toFloat()

        if (magnitude > shakeThreshold) {
            // Shake gesture detected, update the shakeLiveData
            categoryViewModel.toggleShaken()
        }
    }
}

/** TODO: Use rotation sensors for photographing at different angles */
//class Rotation(context: Context) : AndroidSensor(
//    context = context,
//    sensorFeature = PackageManager.FEATURE_SENSOR_GYROSCOPE,
//    sensorType = Sensor.TYPE_ROTATION_VECTOR
//) {
//
//}

/** Use Swipe Gestures for image identification */
class SwipeGestureDetector(
    context: Context,
    private val gestureEventListener: GestureEventListener
) :
    View.OnTouchListener {

    private val gestureDetector: GestureDetector

    init {
        gestureDetector = GestureDetector(context, GestureListener())
    }

    override fun onTouch(view: View, event: MotionEvent): Boolean {
//        Log.d("Sensor", "onTouch: $event")
        return gestureDetector.onTouchEvent(event)
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        private val swipeThreshold = 5
        private val swipeVelocityThreshold = 5

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            val diffX = e2.x - e1.x
            val diffY = e2.y - e1.y
//            Log.d("Sensor", "diffX: $diffX, diffY: $diffY")

            if (abs(diffX) > abs(diffY)) {
                if (abs(diffX) > swipeThreshold && abs(velocityX) > swipeVelocityThreshold) {
                    if (diffX > 0) {
                        gestureEventListener.onSwipeRight()
                    } else {
                        gestureEventListener.onSwipeLeft()
                    }
                    return true
                }
            }
            return false
        }
    }
}