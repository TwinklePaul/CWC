
@file:JvmName("Constants")

package com.team.NEBULA.cwc.upload

// Notification Channel constants

// Name of Notification Channel for verbose notifications of background work
@JvmField val VERBOSE_NOTIFICATION_CHANNEL_NAME: CharSequence =
        "Verbose WorkManager Notifications"
const val VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION =
        "Shows notifications whenever work starts"
@JvmField val NOTIFICATION_TITLE: CharSequence = "WorkRequest Starting"
const val CHANNEL_ID = "VERBOSE_NOTIFICATION"
const val NOTIFICATION_ID = 1

// The name of the image manipulation work
const val IMAGE_MANIPULATION_WORK_NAME = "image_manipulation_work"

// Other keys
const val TAG_HUMANREADABLETIME = "TAG_HUMANREADABLETIME"
const val TAG_PLACEADDR = "TAG_PLACEADDR"
const val TAG_INFO = "TAG_INFO"
const val TAG_TIMECWC = "TAG_TIMECWC"
const val TAG_IMAGEURLARRAYLIST = "TAG_IMAGEURLARRAYLIST"
const val TAG_USEREMAIL = "TAG_USEREMAIL"
const val TAG_USERNAME = "TAG_USERNAME"
const val KEY_LATITUDE = "KEY_LATITUDE"
const val KEY_LONGITUDE = "KEY_LONGITUDE"
const val KEY_PLACENAME = "KEY_PLACENAME"
const val TAG_OUTPUT = "OUTPUT"

const val DELAY_TIME_MILLIS: Long = 3000
