package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.islamic.IslamicContentData
import com.example.data.model.AllahName
import com.example.data.model.DuaItem
import com.example.data.model.PredefinedDhikr
import com.example.ui.components.IslamicDivider
import com.example.ui.theme.AmiriFontFamily
import com.example.ui.theme.CinzelFontFamily
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldLight
import com.example.ui.viewmodel.SurahRahmanViewModel
import com.example.ui.viewmodel.ToolsSubTab
import kotlin.math.roundToInt

@Composable
fun IslamicToolsScreen(
    viewModel: SurahRahmanViewModel,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val subTab by viewModel.toolsSubTab.collectAsState()

    val tabs = listOf(
        ToolsSubTab.TASBIH to "Tasbih Counter",
        ToolsSubTab.PRAYERS to "Prayer Times",
        ToolsSubTab.QIBLA to "Qibla Compass",
        ToolsSubTab.NAMES_OF_ALLAH to "99 Names",
        ToolsSubTab.DUAS to "Daily Duas",
        ToolsSubTab.EVENTS to "Hijri Events"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        // Sub-tabs scrollable row
        ScrollableTabRow(
            selectedTabIndex = tabs.indexOfFirst { it.first == subTab },
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = GoldAccent,
            edgePadding = 12.dp,
            indicator = { tabPositions ->
                val index = tabs.indexOfFirst { it.first == subTab }
                if (index >= 0) {
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[index]),
                        color = GoldAccent
                    )
                }
            }
        ) {
            tabs.forEach { (tab, title) ->
                Tab(
                    selected = subTab == tab,
                    onClick = { viewModel.setToolsSubTab(tab) },
                    text = {
                        Text(
                            text = title,
                            fontSize = 13.sp,
                            fontWeight = if (subTab == tab) FontWeight.Bold else FontWeight.Medium,
                            color = if (subTab == tab) GoldAccent else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    modifier = Modifier.testTag("tool_tab_${tab.name.lowercase()}")
                )
            }
        }

        // Sub-screen content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            when (subTab) {
                ToolsSubTab.TASBIH -> TasbihSection(viewModel)
                ToolsSubTab.PRAYERS -> PrayerTimesSection(viewModel)
                ToolsSubTab.QIBLA -> QiblaCompassSection(viewModel)
                ToolsSubTab.NAMES_OF_ALLAH -> NamesOfAllahSection(viewModel)
                ToolsSubTab.DUAS -> DuasSection(viewModel)
                ToolsSubTab.EVENTS -> HijriEventsSection()
            }
        }
    }
}

// ------------------------------------------------------------------------------------------------
// 1. Digital Tasbih Section
// ------------------------------------------------------------------------------------------------
@Composable
private fun TasbihSection(viewModel: SurahRahmanViewModel) {
    val selectedDhikr by viewModel.selectedDhikr.collectAsState()
    val dhikrProgressList by viewModel.dhikrProgressList.collectAsState()
    val customTarget by viewModel.customDhikrTarget.collectAsState()

    val currentProgress = dhikrProgressList.find { it.dhikrId == selectedDhikr.id }
    val count = currentProgress?.count ?: 0
    val cycles = currentProgress?.completedCycles ?: 0
    val target = customTarget

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Dhikr selection chips
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Select Dhikr (الأذكار):",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldAccent,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(IslamicContentData.predefinedDhikrList) { dhikr ->
                        val isSelected = selectedDhikr.id == dhikr.id
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.selectDhikr(dhikr) },
                            label = { Text(dhikr.transliteration) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = GoldAccent.copy(alpha = 0.2f),
                                selectedLabelColor = GoldAccent
                            )
                        )
                    }
                }
            }
        }

        // Dhikr Display Card
        item {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .border(1.dp, GoldAccent.copy(alpha = 0.4f), RoundedCornerShape(18.dp)),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = selectedDhikr.arabic,
                        fontFamily = AmiriFontFamily,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = selectedDhikr.transliteration,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = selectedDhikr.englishMeaning,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = selectedDhikr.urduMeaning,
                        fontFamily = AmiriFontFamily,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Big Interactive Tasbih Tap Button
        item {
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .border(4.dp, GoldAccent, CircleShape)
                    .clickable { viewModel.incrementDhikrCount() }
                    .testTag("tasbih_counter_btn"),
                contentAlignment = Alignment.Center
            ) {
                // Circular progress ring
                val progressFrac = (count.toFloat() / target.toFloat()).coerceIn(0f, 1f)
                CircularProgressIndicator(
                    progress = { progressFrac },
                    modifier = Modifier.size(232.dp),
                    color = GoldAccent,
                    strokeWidth = 6.dp,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$count",
                        fontSize = 62.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent
                    )
                    Text(
                        text = "Target: $target",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "TAP TO COUNT",
                        fontSize = 11.sp,
                        letterSpacing = 1.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = GoldLight.copy(alpha = 0.7f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        // Target Selector & Cycles & Reset Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Cycles
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                ) {
                    Text(
                        text = "Completed Cycles: $cycles",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }

                // Target selector chips (33, 100)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(33, 100).forEach { tgt ->
                        val selected = customTarget == tgt
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (selected) GoldAccent else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.clickable { viewModel.setCustomDhikrTarget(tgt) }
                        ) {
                            Text(
                                text = "$tgt",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selected) EmeraldDark else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                // Reset Button
                IconButton(
                    onClick = { viewModel.resetDhikrCount() },
                    modifier = Modifier.testTag("reset_tasbih_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reset Count",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        // Dhikr Virtue Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = GoldAccent,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Virtue & Reward",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = GoldAccent
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = selectedDhikr.reward,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

// ------------------------------------------------------------------------------------------------
// 2. Prayer Times Section
// ------------------------------------------------------------------------------------------------
@Composable
private fun PrayerTimesSection(viewModel: SurahRahmanViewModel) {
    val selectedCity by viewModel.selectedCity.collectAsState()
    val schedule = remember(selectedCity) {
        viewModel.prayerRepository.getPrayerSchedule(selectedCity)
    }

    var showCityDropdown by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // City Selector Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { showCityDropdown = !showCityDropdown },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = GoldAccent)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Location (Tap to change):", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(schedule.cityName, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                    Text(
                        text = if (showCityDropdown) "Done" else "Change",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent
                    )
                }
            }
        }

        // City Selector List
        if (showCityDropdown) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, GoldAccent.copy(alpha = 0.3f), RoundedCornerShape(14.dp)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        viewModel.prayerRepository.popularCities.forEach { city ->
                            val isSelected = selectedCity.name == city.name
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.setSelectedCity(city)
                                        showCityDropdown = false
                                    }
                                    .padding(vertical = 8.dp, horizontal = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${city.name}, ${city.country}",
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) GoldAccent else MaterialTheme.colorScheme.onSurface
                                )
                                if (isSelected) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        // Next Prayer Countdown Card
        item {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .border(1.dp, GoldAccent, RoundedCornerShape(18.dp)),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Next Prayer: ${schedule.nextPrayerName}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = GoldLight
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = schedule.nextPrayerTime,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "in ${schedule.remainingTimeFormatted}",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    IslamicDivider()
                    Text(
                        text = "${schedule.dateString} • ${schedule.hijriDateString}",
                        fontSize = 11.sp,
                        color = GoldLight.copy(alpha = 0.8f)
                    )
                }
            }
        }

        // 5 Daily Prayers Schedule Cards
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                PrayerRowItem("Fajr (الفجر)", schedule.fajr, schedule.nextPrayerName == "Fajr")
                PrayerRowItem("Sunrise (الشروق)", schedule.sunrise, schedule.nextPrayerName == "Sunrise")
                PrayerRowItem("Dhuhr (الظهر)", schedule.dhuhr, schedule.nextPrayerName == "Dhuhr")
                PrayerRowItem("Asr (العصر)", schedule.asr, schedule.nextPrayerName == "Asr")
                PrayerRowItem("Maghrib (المغرب)", schedule.maghrib, schedule.nextPrayerName == "Maghrib")
                PrayerRowItem("Isha (العشاء)", schedule.isha, schedule.nextPrayerName == "Isha")
            }
        }
    }
}

@Composable
private fun PrayerRowItem(
    name: String,
    time: String,
    isNext: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = if (isNext) 1.5.dp else 0.5.dp,
                color = if (isNext) GoldAccent else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isNext) GoldAccent.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = if (isNext) GoldAccent else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = name,
                    fontSize = 14.sp,
                    fontWeight = if (isNext) FontWeight.Bold else FontWeight.Medium,
                    color = if (isNext) GoldAccent else MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = time,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = if (isNext) GoldAccent else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

// ------------------------------------------------------------------------------------------------
// 3. Qibla Compass Section
// ------------------------------------------------------------------------------------------------
@Composable
private fun QiblaCompassSection(viewModel: SurahRahmanViewModel) {
    val context = LocalContext.current
    val selectedCity by viewModel.selectedCity.collectAsState()
    val qiblaBearing = remember(selectedCity) {
        viewModel.prayerRepository.calculateQiblaBearing(selectedCity.latitude, selectedCity.longitude)
    }

    var deviceAzimuth by remember { mutableFloatStateOf(0f) }

    // Register hardware compass sensor
    DisposableEffect(Unit) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        val rotationSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
            ?: sensorManager?.getDefaultSensor(Sensor.TYPE_ORIENTATION)

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event == null) return
                if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
                    val rotationMatrix = FloatArray(9)
                    SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                    val orientation = FloatArray(3)
                    SensorManager.getOrientation(rotationMatrix, orientation)
                    val azimuthDeg = (Math.toDegrees(orientation[0].toDouble()) + 360.0) % 360.0
                    deviceAzimuth = azimuthDeg.toFloat()
                } else if (event.sensor.type == Sensor.TYPE_ORIENTATION) {
                    deviceAzimuth = event.values[0]
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        rotationSensor?.let {
            sensorManager?.registerListener(listener, it, SensorManager.SENSOR_DELAY_UI)
        }

        onDispose {
            sensorManager?.unregisterListener(listener)
        }
    }

    val animatedDialRotation by animateFloatAsState(targetValue = -deviceAzimuth, label = "dial_rot")
    val relativeQiblaAngle = (qiblaBearing - deviceAzimuth + 360f) % 360f
    val isAligned = relativeQiblaAngle < 5f || relativeQiblaAngle > 355f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Location Info Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Qibla for ${selectedCity.name}, ${selectedCity.country}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Kaaba Bearing: ${qiblaBearing.roundToInt()}° from True North",
                        fontSize = 12.sp,
                        color = GoldAccent
                    )
                }
                Icon(Icons.Default.Mosque, contentDescription = null, tint = GoldAccent)
            }
        }

        // Circular Compass Dial
        Box(
            modifier = Modifier
                .size(280.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
                .border(3.dp, if (isAligned) GoldAccent else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // Rotating Compass Degree Ring
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .rotate(animatedDialRotation),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "N",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.TopCenter).padding(top = 10.dp)
                )
                Text(
                    text = "S",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 10.dp)
                )
                Text(
                    text = "E",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.CenterEnd).padding(end = 12.dp)
                )
                Text(
                    text = "W",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.CenterStart).padding(start = 12.dp)
                )
            }

            // Fixed Center Indicator pointing to Kaaba
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.rotate(relativeQiblaAngle)
            ) {
                Icon(
                    imageVector = Icons.Default.Mosque,
                    contentDescription = "Kaaba Needle",
                    tint = GoldAccent,
                    modifier = Modifier.size(42.dp)
                )
                Spacer(modifier = Modifier.height(30.dp))
            }

            // Center degree text
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${deviceAzimuth.roundToInt()}°",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = GoldAccent
                )
            }
        }

        // Status Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (isAligned) EmeraldPrimary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isAligned) "✓ Facing Qibla (Makkah)" else "Rotate phone until needle aligns with Kaaba",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isAligned) GoldAccent else MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Hold device flat on palm away from magnetic interference",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// ------------------------------------------------------------------------------------------------
// 4. 99 Names of Allah Section
// ------------------------------------------------------------------------------------------------
@Composable
private fun NamesOfAllahSection(viewModel: SurahRahmanViewModel) {
    val searchQuery by viewModel.namesSearchQuery.collectAsState()
    val namesList by viewModel.displayedAllahNames.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.setNamesSearchQuery(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search 99 Names in Arabic, English, or Urdu...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = GoldAccent) },
            shape = RoundedCornerShape(14.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GoldAccent)
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(namesList, key = { it.number }) { name ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(14.dp)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "#${name.number} ${name.transliteration}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = name.arabic,
                                fontFamily = AmiriFontFamily,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldAccent
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "English: ${name.englishMeaning}",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "اردو: ${name.urduMeaning}",
                            fontFamily = AmiriFontFamily,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = name.explanation,
                            fontSize = 11.sp,
                            lineHeight = 16.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }
}

// ------------------------------------------------------------------------------------------------
// 5. Daily Duas Section
// ------------------------------------------------------------------------------------------------
@Composable
private fun DuasSection(viewModel: SurahRahmanViewModel) {
    val context = LocalContext.current
    val selectedCategory by viewModel.selectedDuaCategory.collectAsState()
    val duasList by viewModel.displayedDuas.collectAsState()

    val categories = listOf("All", "Gratitude", "Mercy", "Protection", "Heart")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories) { cat ->
                FilterChip(
                    selected = selectedCategory == cat,
                    onClick = { viewModel.setSelectedDuaCategory(cat) },
                    label = { Text(cat) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = GoldAccent.copy(alpha = 0.2f),
                        selectedLabelColor = GoldAccent
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(duasList, key = { it.id }) { dua ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, GoldAccent.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = dua.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = GoldAccent
                            )
                            IconButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    clipboard.setPrimaryClip(ClipData.newPlainText(dua.title, "${dua.arabic}\n\n${dua.translationEnglish}"))
                                    Toast.makeText(context, "Dua copied", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy Dua", modifier = Modifier.size(18.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = dua.arabic,
                            fontFamily = AmiriFontFamily,
                            fontSize = 22.sp,
                            lineHeight = 36.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = dua.transliteration,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "English: ${dua.translationEnglish}",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "اردو: ${dua.translationUrdu}",
                            fontFamily = AmiriFontFamily,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Ref: ${dua.reference}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = GoldAccent
                        )
                    }
                }
            }
        }
    }
}

// ------------------------------------------------------------------------------------------------
// 6. Hijri Events Section
// ------------------------------------------------------------------------------------------------
@Composable
private fun HijriEventsSection() {
    val events = IslamicContentData.islamicEvents

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Islamic Hijri Calendar & Events",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = GoldLight
                        )
                        Text(
                            text = "Key holy dates observed throughout the Islamic year",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }

        items(events) { event ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = GoldAccent.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldAccent.copy(alpha = 0.4f)),
                        modifier = Modifier.width(90.dp)
                    ) {
                        Text(
                            text = event.hijriDate,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = GoldAccent,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = event.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = event.arabicTitle,
                                fontFamily = AmiriFontFamily,
                                fontSize = 15.sp,
                                color = GoldAccent
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = event.description,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
