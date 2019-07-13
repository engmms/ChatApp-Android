package com.peteralexbizjak.chatappandroid.utils

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

object PermissionHelper {

    /**
     * All runtime permissions.
     *
     * The WRITE_EXTERNAL_STORAGE permission implicitly allows reading, making READ_EXTERNAL_STORAGE
     * redundant. The reason why I've included the permission nonetheless, is to have an explicit
     * call to it. If proven to be useless, I will remove it in the future
     */
    private val cameraAndExternalStoragePermissions = arrayOf(
        Manifest.permission.INTERNET,
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    /**
     * Request all necessary runtime permissions beforehand
     *
     * @param activity Activity that fires the permission request
     */
    fun requestRuntimePermissions(activity: Activity) {
        Dexter.withActivity(activity)
            .withPermissions(*cameraAndExternalStoragePermissions)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.isAnyPermissionPermanentlyDenied) showExplanation(activity)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            })
            .withErrorListener { error ->
                Toast.makeText(activity, "Error occurred: $error", Toast.LENGTH_SHORT).show()
            }
            .onSameThread()
            .check()
    }

    private fun showExplanation(activity: Activity) {
        AlertDialog.Builder(activity)
            .setTitle("Missing permissions")
            .setMessage("In order for this application to work, you need to grant all requested permissions")
            .setPositiveButton("OK, go to settings") { _, _ ->
                //Take user to application settings
                val settings = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", activity.packageName, null)
                settings.data = uri
                activity.startActivityForResult(settings, 101)
            }
            .setNegativeButton("Not interested") { _, _ ->
                //Gracefully close the application
                val closeApp = Intent(Intent.ACTION_MAIN)
                closeApp.addCategory(Intent.CATEGORY_HOME)
                closeApp.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                activity.startActivity(closeApp)
            }
            .create()
            .show()
    }
}