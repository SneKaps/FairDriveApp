package com.example.fairdriveapp.utils

import android.content.Context
import android.content.pm.PackageManager

object ManifestUtils {
    //function to retrieve api key from manifest file
    fun getApiKeyFromManifest(context: Context): String? {
        val applicationInfo = context.packageManager
            .getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
        val bundle = applicationInfo.metaData
        return bundle.getString("com.google.android.geo.API_KEY")

    }
}