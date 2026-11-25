package com.aukif.smartguardai

import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.rtsp.RtspMediaSource
import androidx.media3.ui.PlayerView

class MainActivity : AppCompatActivity() {
    private var player: ExoPlayer? = null
    private lateinit var playerView: PlayerView
    private lateinit var status: TextView
    private lateinit var urlField: TextView

    // URL awal berdasarkan hasil pengujian Onvier. Dapat diganti di kode untuk uji awal.
    private val cameraUrl = "rtsp://192.168.100.14/live/ch00_1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = android.graphics.Color.rgb(8, 13, 10)
        window.navigationBarColor = android.graphics.Color.rgb(8, 13, 10)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(20)
            setBackgroundColor(android.graphics.Color.rgb(8, 13, 10))
        }
        val title = TextView(this).apply {
            text = "AUKIF SMART GUARD AI"
            textSize = 22f
            setTextColor(android.graphics.Color.rgb(53, 216, 121))
            setPadding(0, 18, 0, 4)
        }
        val subtitle = TextView(this).apply {
            text = "SMART CCTV SECURITY · V1.0 ALPHA"
            textSize = 12f
            setTextColor(0xFF9AA99F.toInt())
        }
        status = TextView(this).apply {
            text = "● SIAP TERHUBUNG"
            textSize = 13f
            setTextColor(android.graphics.Color.rgb(53, 216, 121))
            setPadding(0, 22, 0, 12)
        }
        playerView = PlayerView(this).apply {
            useController = true
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0, 1f
            )
            setBackgroundColor(android.graphics.Color.BLACK)
        }
        urlField = TextView(this).apply {
            text = "Kamera: $cameraUrl"
            textSize = 11f
            setTextColor(0xFF9AA99F.toInt())
            setPadding(0, 12, 0, 12)
        }
        val connect = Button(this).apply {
            text = "HUBUNGKAN KE CCTV"
            setOnClickListener { connectCamera() }
        }
        root.addView(title)
        root.addView(subtitle)
        root.addView(status)
        root.addView(playerView)
        root.addView(urlField)
        root.addView(connect)
        setContentView(root)
    }

    private fun connectCamera() {
        releasePlayer()
        status.text = "● MENGHUBUNGKAN..."
        val exo = ExoPlayer.Builder(this).build()
        player = exo
        playerView.player = exo
        exo.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                status.text = when (playbackState) {
                    Player.STATE_BUFFERING -> "● MEMUAT VIDEO..."
                    Player.STATE_READY -> "● CCTV TERHUBUNG"
                    Player.STATE_ENDED -> "● STREAM SELESAI"
                    else -> "● MENUNGGU STREAM"
                }
            }
            override fun onPlayerError(error: PlaybackException) {
                status.text = "● KONEKSI GAGAL"
                Toast.makeText(this@MainActivity, error.message ?: "Gagal membuka RTSP", Toast.LENGTH_LONG).show()
            }
        })
        val item = MediaItem.fromUri(Uri.parse(cameraUrl))
        val source = RtspMediaSource.Factory()
            .setForceUseRtpTcp(true)
            .createMediaSource(item)
        exo.setMediaSource(source)
        exo.prepare()
        exo.playWhenReady = true
    }

    private fun releasePlayer() {
        playerView.player = null
        player?.release()
        player = null
    }

    override fun onStop() {
        super.onStop()
        // Tahap alpha: hentikan player saat activity tidak terlihat.
        // Pipeline foreground + AI background akan dibuat dan diuji pada tahap berikutnya.
        releasePlayer()
    }

    override fun onDestroy() {
        releasePlayer()
        super.onDestroy()
    }
}
