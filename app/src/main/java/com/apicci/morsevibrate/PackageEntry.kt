package com.apicci.morsevibrate

import java.io.Serializable

//Data that is used to show each entry on the apps list
data class PackageEntry(val appName: String, val appPackage: String, var checked: Boolean) : Serializable