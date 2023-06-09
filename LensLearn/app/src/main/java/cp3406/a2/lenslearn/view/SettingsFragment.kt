/** Uses SharedPreferences for settings. */
package cp3406.a2.lenslearn.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import cp3406.a2.lenslearn.R

class SettingsFragment : PreferenceFragmentCompat() {

    private companion object {
        const val CAMERA_PERMISSION_REQUEST_CODE = 100
        const val INTERNET_PERMISSION_REQUEST_CODE = 101
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        // Add Dark Theme Setting
        val darkThemePreference = findPreference<SwitchPreference>("dark_theme_preference")
        darkThemePreference?.setOnPreferenceChangeListener { _, newValue ->
            val isDarkThemeEnabled = newValue as Boolean
            if (isDarkThemeEnabled) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            true
        }

        // Add Camera Permission Setting
        val cameraPermissionPreference =
            findPreference<SwitchPreference>("camera_permission_preference")
        cameraPermissionPreference?.setOnPreferenceChangeListener { _, newValue ->
            val isCameraPermissionEnabled = newValue as Boolean
            if (isCameraPermissionEnabled) {
                requestCameraPermission()
            } else {
                revokeCameraPermission()
            }
            true
        }

        // Add Internet Permission Setting
        val internetPermissionPreference =
            findPreference<SwitchPreference>("internet_permission_preference")
        internetPermissionPreference?.setOnPreferenceChangeListener { _, newValue ->
            val isInternetPermissionEnabled = newValue as Boolean
            // Request internet permission
            if (isInternetPermissionEnabled) {
                requestInternetPermission()
            } else {
                revokeInternetPermission()
            }
            true
        }
    }

    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun revokeCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun requestInternetPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.INTERNET
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.INTERNET),
                INTERNET_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun revokeInternetPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.INTERNET
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.INTERNET),
                INTERNET_PERMISSION_REQUEST_CODE
            )
        }
    }
}
