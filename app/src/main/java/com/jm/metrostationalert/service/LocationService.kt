package com.jm.metrostationalert.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.jm.metrostationalert.MainActivity
import com.jm.metrostationalert.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kr.jm.domain.model.AlertSettings
import kr.jm.domain.model.LocationData
import kr.jm.domain.usecase.GetAlertSettingsUseCase
import kr.jm.domain.usecase.GetAlertStationLocationUseCase
import kr.jm.domain.util.LocationUtils
import javax.inject.Inject

@AndroidEntryPoint
class LocationService : Service() {

    @Inject
    lateinit var getAlertStationLocationUseCase: GetAlertStationLocationUseCase

    @Inject
    lateinit var getAlertSettingsUseCase: GetAlertSettingsUseCase

    private val serviceScope = CoroutineScope(Dispatchers.IO + Job())
    private var bookmarkLocationData: LocationData? = null
    private var alertSettings: AlertSettings? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private val notificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
    private var alertState = true

    override fun onCreate() {
        super.onCreate()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        observeUserPreferences()
        createLocationRequest()
        createLocationCallback()
        startLocationUpdates()
    }

    private fun observeUserPreferences() {
        serviceScope.launch {
            combine(
                getAlertStationLocationUseCase(),
                getAlertSettingsUseCase()
            ) { location, settings ->
                Pair(location, settings)
            }.collect { (location, settings) ->
                Log.d(TAG, "User preferences updated:")
                Log.d(TAG, "  Location: $location")
                Log.d(TAG, "  Settings: $settings")
                bookmarkLocationData = location
                alertSettings = settings
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(3000)
            .build()
    }

    private fun createLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let {
                    handleNewLocation(it)
                }
            }
        }
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null
            )
        }
    }

    private fun handleNewLocation(location: Location) {
        Log.d(TAG, "handleNewLocation called")
        Log.d(TAG, "Current location: ${location.latitude}, ${location.longitude}")

        val bookmarkLocation = bookmarkLocationData
        val settings = alertSettings

        Log.d(TAG, "BookmarkLocation: $bookmarkLocation")
        Log.d(TAG, "AlertSettings: $settings")

        if (bookmarkLocation == null) {
            Log.w(TAG, "BookmarkLocation is null - cannot calculate distance")
            return
        }

        if (settings == null) {
            Log.w(TAG, "AlertSettings is null - cannot determine alert distance")
            return
        }

        if (bookmarkLocation.latitude == 0.0 && bookmarkLocation.longitude == 0.0) {
            Log.w(TAG, "Station location not set (0.0, 0.0) - cannot calculate distance")
            return
        }

        val distance = LocationUtils.calculateDistance(
            bookmarkLatitude = bookmarkLocation.latitude,
            bookmarkLongitude = bookmarkLocation.longitude,
            currentLatitude = location.latitude,
            currentLongitude = location.longitude
        )

        Log.d(TAG, "Calculated distance: ${String.format("%.2f", distance)}km")
        Log.d(TAG, "Alert distance setting: ${settings.alertDistance}km")
        Log.d(TAG, "Alert state: $alertState")

        if (distance <= settings.alertDistance && alertState) {
            Log.i(TAG, "Sending notification - distance $distance <= ${settings.alertDistance}")
            sendNotification()
            alertState = false
        } else if (distance > settings.alertDistance) {
            Log.d(TAG, "Distance $distance > ${settings.alertDistance} - resetting alert state")
            alertState = true
        } else {
            Log.d(TAG, "Alert already sent, waiting for user to move away")
        }
    }

    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannels()

        val notification = createForegroundNotification()
        startForeground(FOREGROUND_NOTIFICATION_ID, notification)

        if (intent?.action == ACTION_STOP) {
            stopSelf()
        }

        return START_NOT_STICKY
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val foregroundChannel = NotificationChannel(
                FOREGROUND_CHANNEL_ID,
                "백그라운드 서비스",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(foregroundChannel)

            val alertChannel = NotificationChannel(
                ALERT_CHANNEL_ID,
                "하차 알림",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(alertChannel)
        }
    }

    private fun createForegroundNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, FOREGROUND_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("지하철 하차 알림이 활성화되었습니다")
                .setContentText("백그라운드에서 위치를 모니터링하고 있습니다")
                .setContentIntent(pendingIntent)
                .build()
        } else {
            @Suppress("DEPRECATION")
            Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("지하철 하차 알림이 활성화되었습니다")
                .setContentText("백그라운드에서 위치를 모니터링하고 있습니다")
                .setContentIntent(pendingIntent)
                .build()
        }
    }

    private fun sendNotification() {
        val settings = alertSettings ?: return

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val closeIntent = Intent(this, LocationService::class.java).apply {
            action = ACTION_STOP
        }
        val closePendingIntent = PendingIntent.getService(
            this, 0, closeIntent, PendingIntent.FLAG_IMMUTABLE
        )

        val notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, ALERT_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(settings.notiTitle)
                .setContentText(settings.notiContent)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()
        } else {
            @Suppress("DEPRECATION")
            Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(settings.notiTitle)
                .setContentText(settings.notiContent)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()
        }

        notificationManager.notify(ALERT_NOTIFICATION_ID, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        serviceScope.cancel()
    }

    companion object {
        private const val TAG = "LocationService"
        private const val FOREGROUND_CHANNEL_ID = "foreground_service_channel"
        private const val ALERT_CHANNEL_ID = "alert_channel"
        private const val FOREGROUND_NOTIFICATION_ID = 1
        private const val ALERT_NOTIFICATION_ID = 2
        const val ACTION_STOP = "ACTION_STOP"
    }
}