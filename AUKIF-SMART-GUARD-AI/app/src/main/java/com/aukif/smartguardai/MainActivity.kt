
package com.aukif.smartguardai

import android.net.Uri
import android.os.Bundle
import android.graphics.Color
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import org.videolan.libvlc.util.VLCVideoLayout

class MainActivity : AppCompatActivity() {

    private var libVLC: LibVLC? = null
    private var mediaPlayer: MediaPlayer? = null

    private lateinit var videoLayout: VLCVideoLayout
    private lateinit var status: TextView

    private val cameraUrl =
        "rtsp://192.168.100.14/live/ch00_1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = Color.rgb(8, 13, 10)
        window.navigationBarColor = Color.rgb(8, 13, 10)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(20, 20, 20, 20)
            setBackgroundColor(Color.rgb(8, 13, 10))
        }

        val title = TextView(this).apply {
            text = "AUKIF SMART GUARD AI"
            textSize = 22f
            setTextColor(Color.rgb(53, 216, 121))
        }

        status = TextView(this).apply {
            text = "● SIAP TERHUBUNG"
            textSize = 14f
            setTextColor(Color.rgb(53, 216, 121))
            setPadding(0, 20, 0, 20)
        }

        videoLayout = VLCVideoLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
            setBackgroundColor(Color.BLACK)
        }

        val connect = Button(this).apply {
            text = "HUBUNGKAN KE CCTV"
            setOnClickListener { connectCamera() }
        }

        root.addView(title)
        root.addView(status)
        root.addView(videoLayout)
        root.addView(connect)

        setContentView(root)

        try {
            val options = arrayListOf(
                "--rtsp-tcp",
                "--network-caching=300"
            )

            libVLC = LibVLC(this, options)
            mediaPlayer = MediaPlayer(libVLC)

            mediaPlayer?.attachViews(
                videoLayout,
                null,
                false,
                false
            )

        } catch (e: Exception) {
            status.text = "● VLC ERROR"
            Toast.makeText(
                this,
                e.message ?: "Gagal memulai VLC",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun connectCamera() {
        try {
            status.text = "● MENGHUBUNGKAN..."

            val media = Media(
                libVLC,
                Uri.parse(cameraUrl)
            )

            media.setHWDecoderEnabled(false, false)
            media.addOption(":rtsp-tcp")
            media.addOption(":network-caching=300")

            mediaPlayer?.media = media
            media.release()

            mediaPlayer?.play()

            status.text = "● MEMBUKA STREAM CCTV"

        } catch (e: Exception) {
            status.text = "● KONEKSI GAGAL"

            Toast.makeText(
                this,
                e.message ?: "Gagal membuka RTSP",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer?.stop()
    }

    override fun onDestroy() {
        mediaPlayer?.detachViews()
        mediaPlayer?.release()
        mediaPlayer = null

        libVLC?.release()
        libVLC = null

        super.onDestroy()
    }
}
