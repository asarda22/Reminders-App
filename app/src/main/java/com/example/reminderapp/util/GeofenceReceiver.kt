package com.example.reminderapp.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.reminderapp.ui.MapsActivity
import com.example.reminderapp.ui.MapsActivity.Companion.removeGeofences
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceReceiver : BroadcastReceiver() {
    lateinit var lattitude: String
    lateinit var longitude: String
    lateinit var text: String

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            val geofencingEvent = GeofencingEvent.fromIntent(intent)
            val geofencingTransition = geofencingEvent.geofenceTransition

            if (geofencingTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geofencingTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {
                // Retrieve data from intent
                if (intent != null) {
                    text = intent.getStringExtra("message")!!
                    lattitude = intent.getStringExtra("lattitude")!!
                    longitude = intent.getStringExtra("longitude")!!
                }

                /*showNotification(
                    context.applicationContext,
                    "Location\nLat: ${lattitude} - Lon: ${longitude}"
                )*/
                MapsActivity
                    .showNotification(
                        context.applicationContext,
                        text
                    )


                // remove geofence
                val triggeringGeofences = geofencingEvent.triggeringGeofences
                removeGeofences(context, triggeringGeofences)
            }
        }
    }

}