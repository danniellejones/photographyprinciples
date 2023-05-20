package cp3406.a2.lenslearn.sensors
//
//import android.content.Context
//import android.content.pm.PackageManager
//import android.hardware.Sensor
//import android.view.GestureDetector
//import android.view.MotionEvent
//
//class Accelerometer(context: Context) : AndroidSensor(
//    context = context,
//    sensorFeature = PackageManager.FEATURE_SENSOR_ACCELEROMETER,
//    sensorType = Sensor.TYPE_ACCELEROMETER
//) {
//
//
//}
//
//class Rotation(context: Context) : AndroidSensor(
//    context = context,
//    sensorFeature = PackageManager.FEATURE_SENSOR_GYROSCOPE,
//    sensorType = Sensor.TYPE_ROTATION_VECTOR
//) {
//
//}
//
//val gestureDetector = GestureDetector(
//    this,
//    object : GestureDetector.SimpleOnGestureListener() {
//        override fun onFling(
//            e1: MotionEvent,
//            e2: MotionEvent,
//            velocityX: Float,
//            velocityY: Float
//        ): Boolean {
//            if (e1 != null && e2 != null) {
//                val distanceX = e2.x - e1.x
//                if (distanceX > 0) {
//                    // swipe right detected, show next image
//                } else {
//                    // swipe left detected, show previous image
//                }
//                return true
//            }
//            return false
//        }
//    })
//
//imageView.setOnTouchListener{ _, event -> gestureDetector.onTouchEvent(event) true }