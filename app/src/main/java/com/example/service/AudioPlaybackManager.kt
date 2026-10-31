package com.example.service

import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.example.data.islamic.IslamicContentData
import com.example.data.model.Reciter
import com.example.data.repository.QuranRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale

data class AudioPlayerState(
    val currentVerseNumber: Int = 1,
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val currentPositionMs: Long = 0L,
    val durationMs: Long = 0L,
    val playbackSpeed: Float = 1.0f,
    val repeatMode: String = "SURAH", // "OFF", "VERSE", "SURAH"
    val reciter: Reciter = IslamicContentData.reciters.first(),
    val sleepTimerRemainingMinutes: Int? = null,
    val isDownloading: Boolean = false,
    val downloadProgressPercent: Int = 0
)

@OptIn(UnstableApi::class)
class AudioPlaybackManager(
    private val context: Context,
    private val quranRepository: QuranRepository
) {
    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private var exoPlayer: ExoPlayer? = null
    private var progressTrackingJob: Job? = null
    private var sleepTimerJob: Job? = null

    private val _playerState = MutableStateFlow(AudioPlayerState())
    val playerState: StateFlow<AudioPlayerState> = _playerState.asStateFlow()

    init {
        initExoPlayer()
    }

    private fun initExoPlayer() {
        if (exoPlayer != null) return

        val player = ExoPlayer.Builder(context).build().apply {
            addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    _playerState.value = _playerState.value.copy(isPlaying = isPlaying)
                    if (isPlaying) {
                        startProgressTracker()
                    } else {
                        stopProgressTracker()
                    }
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    val isBuffering = playbackState == Player.STATE_BUFFERING
                    _playerState.value = _playerState.value.copy(isBuffering = isBuffering)

                    if (playbackState == Player.STATE_ENDED) {
                        handlePlaybackEnded()
                    }
                }

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    val mediaId = mediaItem?.mediaId
                    val verseNum = mediaId?.toIntOrNull() ?: (exoPlayer?.currentMediaItemIndex?.plus(1) ?: 1)
                    _playerState.value = _playerState.value.copy(
                        currentVerseNumber = verseNum,
                        durationMs = exoPlayer?.duration?.coerceAtLeast(0L) ?: 0L
                    )
                }
            })
        }
        exoPlayer = player
        setupPlaylist(IslamicContentData.reciters.first())
    }

    private fun setupPlaylist(reciter: Reciter, initialVerse: Int = 1) {
        val player = exoPlayer ?: return
        val wasPlaying = player.isPlaying

        player.clearMediaItems()
        val mediaItems = (1..78).map { verseNum ->
            val localFile = getLocalAudioFile(reciter.id, verseNum)
            val uri = if (localFile.exists() && localFile.length() > 0) {
                Uri.fromFile(localFile)
            } else {
                val formattedUrl = String.format(Locale.US, reciter.audioBaseUrl, verseNum)
                Uri.parse(formattedUrl)
            }

            val metadata = MediaMetadata.Builder()
                .setTitle("Surah Ar-Rahman - Ayah $verseNum")
                .setArtist(reciter.name)
                .setAlbumTitle("سورة الرحمن")
                .build()

            MediaItem.Builder()
                .setMediaId(verseNum.toString())
                .setUri(uri)
                .setMediaMetadata(metadata)
                .build()
        }

        player.setMediaItems(mediaItems)
        val targetIndex = (initialVerse - 1).coerceIn(0, 77)
        player.seekTo(targetIndex, 0L)
        player.prepare()

        _playerState.value = _playerState.value.copy(
            reciter = reciter,
            currentVerseNumber = initialVerse
        )

        if (wasPlaying) {
            player.play()
        }
    }

    private fun handlePlaybackEnded() {
        val state = _playerState.value
        when (state.repeatMode) {
            "VERSE" -> {
                seekToVerse(state.currentVerseNumber)
                play()
            }
            "SURAH" -> {
                seekToVerse(1)
                play()
            }
            else -> {
                pause()
            }
        }
    }

    fun play() {
        exoPlayer?.play()
    }

    fun pause() {
        exoPlayer?.pause()
    }

    fun togglePlayPause() {
        if (_playerState.value.isPlaying) {
            pause()
        } else {
            play()
        }
    }

    fun seekToVerse(verseNumber: Int) {
        val index = (verseNumber - 1).coerceIn(0, 77)
        exoPlayer?.seekTo(index, 0L)
        _playerState.value = _playerState.value.copy(
            currentVerseNumber = verseNumber,
            currentPositionMs = 0L
        )
    }

    fun skipToNext() {
        val next = (_playerState.value.currentVerseNumber + 1).coerceAtMost(78)
        seekToVerse(next)
    }

    fun skipToPrevious() {
        val prev = (_playerState.value.currentVerseNumber - 1).coerceAtLeast(1)
        seekToVerse(prev)
    }

    fun seekToPosition(positionMs: Long) {
        exoPlayer?.seekTo(positionMs)
        _playerState.value = _playerState.value.copy(currentPositionMs = positionMs)
    }

    fun setPlaybackSpeed(speed: Float) {
        exoPlayer?.playbackParameters = PlaybackParameters(speed)
        _playerState.value = _playerState.value.copy(playbackSpeed = speed)
    }

    fun setRepeatMode(mode: String) {
        _playerState.value = _playerState.value.copy(repeatMode = mode)
    }

    fun switchReciter(reciter: Reciter) {
        val currentVerse = _playerState.value.currentVerseNumber
        setupPlaylist(reciter, currentVerse)
    }

    fun setSleepTimer(minutes: Int?) {
        sleepTimerJob?.cancel()
        _playerState.value = _playerState.value.copy(sleepTimerRemainingMinutes = minutes)

        if (minutes != null && minutes > 0) {
            sleepTimerJob = scope.launch {
                var remaining = minutes
                while (isActive && remaining > 0) {
                    delay(60_000L)
                    remaining--
                    _playerState.value = _playerState.value.copy(sleepTimerRemainingMinutes = remaining)
                }
                pause()
                _playerState.value = _playerState.value.copy(sleepTimerRemainingMinutes = null)
            }
        }
    }

    private fun startProgressTracker() {
        progressTrackingJob?.cancel()
        progressTrackingJob = scope.launch {
            while (isActive) {
                exoPlayer?.let { player ->
                    val pos = player.currentPosition.coerceAtLeast(0L)
                    val dur = player.duration.coerceAtLeast(0L)
                    val currentIdx = player.currentMediaItemIndex
                    val verseNum = currentIdx + 1

                    _playerState.value = _playerState.value.copy(
                        currentPositionMs = pos,
                        durationMs = dur,
                        currentVerseNumber = verseNum
                    )

                    // Automatically update reading progress in DB
                    quranRepository.updateReadingProgress(verseNum)
                }
                delay(300L)
            }
        }
    }

    private fun stopProgressTracker() {
        progressTrackingJob?.cancel()
        progressTrackingJob = null
    }

    private fun getLocalAudioFile(reciterId: String, verseNumber: Int): File {
        val dir = File(context.filesDir, "audio/$reciterId")
        if (!dir.exists()) dir.mkdirs()
        return File(dir, "verse_${String.format(Locale.US, "%03d", verseNumber)}.mp3")
    }

    fun isVerseDownloaded(reciterId: String, verseNumber: Int): Boolean {
        val file = getLocalAudioFile(reciterId, verseNumber)
        return file.exists() && file.length() > 0
    }

    fun isSurahFullyDownloaded(reciterId: String): Boolean {
        for (i in 1..78) {
            if (!isVerseDownloaded(reciterId, i)) return false
        }
        return true
    }

    fun downloadSurah(reciter: Reciter, onComplete: () -> Unit = {}) {
        if (_playerState.value.isDownloading) return

        _playerState.value = _playerState.value.copy(isDownloading = true, downloadProgressPercent = 0)

        scope.launch(Dispatchers.IO) {
            try {
                for (verse in 1..78) {
                    val file = getLocalAudioFile(reciter.id, verse)
                    if (!file.exists() || file.length() == 0L) {
                        val urlStr = String.format(Locale.US, reciter.audioBaseUrl, verse)
                        downloadFile(urlStr, file)
                        quranRepository.recordDownloadedVerse(reciter.id, verse, file.absolutePath, file.length())
                    }
                    val progress = ((verse / 78.0) * 100).toInt()
                    withContext(Dispatchers.Main) {
                        _playerState.value = _playerState.value.copy(downloadProgressPercent = progress)
                    }
                }
                withContext(Dispatchers.Main) {
                    _playerState.value = _playerState.value.copy(
                        isDownloading = false,
                        downloadProgressPercent = 100
                    )
                    // Refresh current playlist with local files
                    setupPlaylist(reciter, _playerState.value.currentVerseNumber)
                    onComplete()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _playerState.value = _playerState.value.copy(isDownloading = false)
                }
            }
        }
    }

    private fun downloadFile(urlStr: String, destination: File) {
        val url = URL(urlStr)
        val conn = url.openConnection() as HttpURLConnection
        conn.connectTimeout = 10000
        conn.readTimeout = 15000
        conn.connect()

        conn.inputStream.use { input ->
            FileOutputStream(destination).use { output ->
                input.copyTo(output)
            }
        }
    }

    fun clearDownloadedAudio(reciterId: String) {
        val dir = File(context.filesDir, "audio/$reciterId")
        if (dir.exists()) {
            dir.deleteRecursively()
        }
        scope.launch {
            quranRepository.clearDownloadsForReciter(reciterId)
            setupPlaylist(_playerState.value.reciter, _playerState.value.currentVerseNumber)
        }
    }

    fun release() {
        stopProgressTracker()
        sleepTimerJob?.cancel()
        exoPlayer?.release()
        exoPlayer = null
    }
}
