package com.example.academyproject.views

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.academyproject.R

class CalendarPermissionHelper(
    private val fragment: Fragment,
    private val listener: PermissionGrantedListener
    ) {
    private var isRationaleShown = false
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>

    interface PermissionGrantedListener {
        fun onCalendarPermissionGranted()
    }

    @SuppressLint("MissingPermission")
    fun init() {
        requestPermissionLauncher = fragment.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val notGranted = permissions.values.contains(false)
            if (!notGranted) {
                onCalendarPermissionsGranted()
            } else {
                onCalendarPermissionsNotGranted()
            }
        }

        restorePreferencesData()
    }

    fun terminate() {
        savePreferencesData()
        requestPermissionLauncher.unregister()
    }

    private fun checkSelfPermissions(): Boolean {
        var result = true

        for(permission in PERMISSIONS_CALENDAR) {
            result = result && (
                    ContextCompat.checkSelfPermission(fragment.requireContext(), permission) ==
                            PackageManager.PERMISSION_GRANTED
                    )
        }

        return result
    }

    @SuppressLint("NewApi")
    private fun shouldShowRequestPermissionsRationale(): Boolean {
        var result = true

        for(permission in PERMISSIONS_CALENDAR) {
            result = result && (
                    fragment.requireActivity().shouldShowRequestPermissionRationale(permission)
                    )
        }

        return result
    }

    @SuppressLint("MissingPermission")
    fun requestPermission() {
        when {
            checkSelfPermissions() ->
                onCalendarPermissionsGranted()

            shouldShowRequestPermissionsRationale() ->
                showCalendarPermissionExplanationDialog()

            isRationaleShown ->
                showCalendarPermissionDeniedDialog()

            else ->
                requestCalendarPermission()
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR])
    private fun onCalendarPermissionsGranted() {
        Toast.makeText(fragment.requireContext(), R.string.calendar_permissions_granted, Toast.LENGTH_SHORT).show()
        listener.onCalendarPermissionGranted()
    }

    private fun onCalendarPermissionsNotGranted() {
        Toast.makeText(fragment.requireContext(), R.string.calendar_permissions_not_granted, Toast.LENGTH_SHORT).show()
    }

    private fun showCalendarPermissionExplanationDialog() {
        AlertDialog.Builder(fragment.requireContext())
            .setMessage(R.string.calendar_permission_dialog_description)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                isRationaleShown = true
                requestCalendarPermission()
                dialog.dismiss()
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showCalendarPermissionDeniedDialog() {
        AlertDialog.Builder(fragment.requireContext())
            .setMessage(R.string.calendar_permission_dialog_denied_text)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                fragment.startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + fragment.requireActivity().packageName)
                    )
                )
                dialog.dismiss()
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun requestCalendarPermission() {
        requestPermissionLauncher.launch(PERMISSIONS_CALENDAR)
    }

    private fun savePreferencesData() {
        fragment.requireActivity().getPreferences(Context.MODE_PRIVATE).edit()
                .putBoolean(KEY_CALENDAR_PERMISSION_RATIONALE_SHOWN, isRationaleShown)
                .apply()
    }

    private fun restorePreferencesData() {
        isRationaleShown = fragment.requireActivity().getPreferences(Context.MODE_PRIVATE)?.getBoolean(
            KEY_CALENDAR_PERMISSION_RATIONALE_SHOWN,
            false
        ) ?: false
    }

    companion object {
        private const val KEY_CALENDAR_PERMISSION_RATIONALE_SHOWN = "KEY_CALENDAR_PERMISSION_RATIONALE_SHOWN"
        private val PERMISSIONS_CALENDAR = arrayOf(
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR
        )
    }
}