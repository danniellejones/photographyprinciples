package cp3406.a2.lenslearn.sensors

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import cp3406.a2.lenslearn.model.CategoryViewModel
import kotlin.math.abs
import kotlin.math.sqrt

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
//class Rotation(context: Context, categoryViewModel: CategoryViewModel) : AndroidSensor(
//    context = context,
//    sensorFeature = PackageManager.FEATURE_SENSOR_GYROSCOPE,
//    sensorType = Sensor.TYPE_ROTATION_VECTOR,
//    categoryViewModel = categoryViewModel) {}

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

    @SuppressLint("ClickableViewAccessibility")  // TODO: Improve Accessibility
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        private val swipeThreshold = 5
        private val swipeVelocityThreshold = 5

        override fun onDown(e: MotionEvent): Boolean {
            return true  // Must be set to true for swipe to work
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            val diffX = e2.x - e1.x
            val diffY = e2.y - e1.y

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