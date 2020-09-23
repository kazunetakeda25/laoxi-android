package com.driver.routedisplayhelper

import android.graphics.Color
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import java.util.Collections.emptyList

class RouteDisplay {
    private var googlemap: GoogleMap? = null
    private var simplifiedLine: List<LatLng> = emptyList()
    internal var tolerlance = 5.0
    fun RouteFromGoogle(map: GoogleMap, origin: LatLng, dest: LatLng) {
        this.googlemap = map
        val url = getDirectionsUrl(origin, dest)
        val downloadTask = DownloadTask(googlemap)
        // Start downloading json data from Google Directions API
        downloadTask.execute(url)
    }

    fun RouteFromTrace(map: GoogleMap, points: List<LatLng>) {
        this.googlemap = map
        simplifiedLine = PolyUtil.simplify(points, tolerlance)
        map.addPolyline(PolylineOptions().addAll(simplifiedLine).color(Color.RED))
    }

    private fun getDirectionsUrl(origin: LatLng, dest: LatLng): String {
        // Origin of route
        val str_origin = "origin=" + origin.latitude + "," + origin.longitude
        // Destination of route
        val str_dest = "destination=" + dest.latitude + "," + dest.longitude
        // Sensor enabled
        val sensor = "sensor=false"
        // Building the parameters to the web service
        val parameters = "$str_origin&$str_dest&$sensor"
        // Output format
        val output = "json"
        // Building the url to the web service
        val url = "https://maps.googleapis.com/maps/api/directions/$output?$parameters"
        return url
    }
}
