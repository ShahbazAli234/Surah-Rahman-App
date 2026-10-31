package com.example.data.repository

import com.example.data.model.PrayerSchedule
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.*

data class CityLocation(
    val name: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val timeZoneId: String
)

class PrayerRepository {

    val popularCities: List<CityLocation> = listOf(
        CityLocation("Makkah", "Saudi Arabia", 21.4225, 39.8262, "Asia/Riyadh"),
        CityLocation("Madinah", "Saudi Arabia", 24.5247, 39.5692, "Asia/Riyadh"),
        CityLocation("Karachi", "Pakistan", 24.8607, 67.0011, "Asia/Karachi"),
        CityLocation("Lahore", "Pakistan", 31.5204, 74.3587, "Asia/Karachi"),
        CityLocation("Islamabad", "Pakistan", 33.6844, 73.0479, "Asia/Karachi"),
        CityLocation("Hyderabad (Sindh)", "Pakistan", 25.3960, 68.3578, "Asia/Karachi"),
        CityLocation("Sukkur (Sindh)", "Pakistan", 27.7052, 68.8574, "Asia/Karachi"),
        CityLocation("Cairo", "Egypt", 30.0444, 31.2357, "Africa/Cairo"),
        CityLocation("Istanbul", "Turkey", 41.0082, 28.9784, "Europe/Istanbul"),
        CityLocation("Dubai", "UAE", 25.2048, 55.2708, "Asia/Dubai"),
        CityLocation("London", "United Kingdom", 51.5074, -0.1278, "Europe/London"),
        CityLocation("New York", "USA", 40.7128, -74.0060, "America/New_York"),
        CityLocation("Kuala Lumpur", "Malaysia", 3.1390, 101.6869, "Asia/Kuala_Lumpur"),
        CityLocation("Jakarta", "Indonesia", -6.2088, 106.8456, "Asia/Jakarta")
    )

    fun calculateQiblaBearing(lat: Double, lng: Double): Float {
        val kaabaLat = Math.toRadians(21.422487)
        val kaabaLng = Math.toRadians(39.826206)
        val userLat = Math.toRadians(lat)
        val userLng = Math.toRadians(lng)

        val deltaLng = kaabaLng - userLng
        val y = sin(deltaLng)
        val x = cos(userLat) * tan(kaabaLat) - sin(userLat) * cos(deltaLng)
        var qibla = Math.toDegrees(atan2(y, x))
        qibla = (qibla + 360.0) % 360.0
        return qibla.toFloat()
    }

    fun getPrayerSchedule(city: CityLocation, date: Date = Date()): PrayerSchedule {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone(city.timeZoneId))
        calendar.time = date

        val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
        val year = calendar.get(Calendar.YEAR)

        // Solar Declination & Equation of time
        val b = 2.0 * Math.PI * (dayOfYear - 81) / 365.0
        val eot = 9.87 * sin(2 * b) - 7.53 * cos(b) - 1.5 * sin(b) // minutes
        val declination = Math.toRadians(23.45 * sin(b))

        val latRad = Math.toRadians(city.latitude)
        val tzOffsetHours = TimeZone.getTimeZone(city.timeZoneId).getOffset(date.time) / 3600000.0
        val solarNoonUtc = 12.0 - (city.longitude / 15.0) - (eot / 60.0)
        val dhuhrLocal = (solarNoonUtc + tzOffsetHours + 24.0) % 24.0

        // Fajr (18° angle below horizon)
        val fajrAngle = Math.toRadians(-18.0)
        val fajrHourAngle = getHourAngle(latRad, declination, fajrAngle)
        val fajrLocal = dhuhrLocal - (fajrHourAngle / 15.0)

        // Sunrise (-0.833° angle)
        val sunriseAngle = Math.toRadians(-0.833)
        val sunriseHourAngle = getHourAngle(latRad, declination, sunriseAngle)
        val sunriseLocal = dhuhrLocal - (sunriseHourAngle / 15.0)

        // Asr (Shadow factor = 1 + tan|lat - dec|)
        val asrAngle = atan(1.0 + tan(abs(latRad - declination)))
        val asrHourAngle = getHourAngle(latRad, declination, asrAngle)
        val asrLocal = dhuhrLocal + (asrHourAngle / 15.0)

        // Sunset / Maghrib (-0.833° angle)
        val maghribLocal = dhuhrLocal + (sunriseHourAngle / 15.0)

        // Isha (18° angle)
        val ishaLocal = dhuhrLocal + (fajrHourAngle / 15.0)

        val fajrStr = formatTime(fajrLocal)
        val sunriseStr = formatTime(sunriseLocal)
        val dhuhrStr = formatTime(dhuhrLocal)
        val asrStr = formatTime(asrLocal)
        val maghribStr = formatTime(maghribLocal)
        val ishaStr = formatTime(ishaLocal)

        // Next prayer calculation
        val currentLocalHours = calendar.get(Calendar.HOUR_OF_DAY) + calendar.get(Calendar.MINUTE) / 60.0
        val prayers = listOf(
            Triple("Fajr", fajrLocal, fajrStr),
            Triple("Sunrise", sunriseLocal, sunriseStr),
            Triple("Dhuhr", dhuhrLocal, dhuhrStr),
            Triple("Asr", asrLocal, asrStr),
            Triple("Maghrib", maghribLocal, maghribStr),
            Triple("Isha", ishaLocal, ishaStr)
        )

        val next = prayers.firstOrNull { it.second > currentLocalHours } ?: Triple("Fajr", fajrLocal + 24.0, fajrStr)
        val diffMinutes = ((next.second - currentLocalHours) * 60).toInt()
        val remainingFormatted = "${diffMinutes / 60}h ${diffMinutes % 60}m"

        val dateFormat = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault())
        val hijriDateStr = calculateApproximateHijriDate(calendar)

        return PrayerSchedule(
            cityName = "${city.name}, ${city.country}",
            dateString = dateFormat.format(date),
            hijriDateString = hijriDateStr,
            fajr = fajrStr,
            sunrise = sunriseStr,
            dhuhr = dhuhrStr,
            asr = asrStr,
            maghrib = maghribStr,
            isha = ishaStr,
            nextPrayerName = next.first,
            nextPrayerTime = next.third,
            remainingTimeFormatted = remainingFormatted
        )
    }

    private fun getHourAngle(latRad: Double, decRad: Double, altitudeRad: Double): Double {
        val cosH = (sin(altitudeRad) - sin(latRad) * sin(decRad)) / (cos(latRad) * cos(decRad))
        val clamped = cosH.coerceIn(-1.0, 1.0)
        return Math.toDegrees(acos(clamped))
    }

    private fun formatTime(hoursFloat: Double): String {
        var h = (hoursFloat % 24.0).toInt()
        if (h < 0) h += 24
        val m = (((hoursFloat % 24.0) - h) * 60.0).roundToInt().coerceIn(0, 59)
        val amPm = if (h >= 12) "PM" else "AM"
        val displayH = if (h % 12 == 0) 12 else h % 12
        return String.format(Locale.US, "%02d:%02d %s", displayH, m, amPm)
    }

    private fun calculateApproximateHijriDate(cal: Calendar): String {
        // Astronomical approximation based on Julian Day
        val y = cal.get(Calendar.YEAR)
        val m = cal.get(Calendar.MONTH) + 1
        val d = cal.get(Calendar.DAY_OF_MONTH)

        var jd = (1461 * (y + 4800 + (m - 14) / 12)) / 4 +
                (367 * (m - 2 - 12 * ((m - 14) / 12))) / 12 -
                (3 * ((y + 4900 + (m - 14) / 12) / 100)) / 4 + d - 32075

        val l = jd - 1948440 + 10632
        val n = (l - 1) / 10631
        val l1 = l - 10631 * n + 354
        val j = ((10985 - l1) / 5316) * ((50 * l1) / 17719) + (l1 / 5670) * ((43 * l1) / 15238)
        val l2 = l1 - ((30 - j) / 15) * ((17719 * j) / 50) - (j / 16) * ((15238 * j) / 43) + 29
        val hijriMonth = (24 * l2) / 709
        val hijriDay = l2 - (709 * hijriMonth) / 24
        val hijriYear = 30 * n + j - 30

        val hijriMonthNames = listOf(
            "Muharram", "Safar", "Rabi' al-Awwal", "Rabi' al-Thani",
            "Jumada al-Awwal", "Jumada al-Thani", "Rajab", "Sha'ban",
            "Ramadan", "Shawwal", "Dhul-Qi'dah", "Dhul-Hijjah"
        )
        val monthName = hijriMonthNames.getOrElse((hijriMonth - 1).coerceIn(0, 11)) { "Ramadan" }
        return "$hijriDay $monthName $hijriYear AH"
    }
}
