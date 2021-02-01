package org.jzvd.jzvideo

import android.media.AudioManager
import android.media.MediaPlayer
import android.provider.MediaStore
import android.view.Surface
import android.view.SurfaceHolder

/**
 * Created by Nathen on 2021/1/13.
 */
class JZMediaSystem : JZMediaInterface(),
    MediaPlayer.OnPreparedListener {

    var mediaPlayer: MediaPlayer? = null

    override fun prepare() {
        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)//AudioAttributes代码复杂没有这个好。
        mediaPlayer!!.isLooping = true
        mediaPlayer!!.setOnPreparedListener(this@JZMediaSystem)
        mediaPlayer!!.setDataSource("http://jzvd.nathen.cn/video/25ae1b1c-1767b2a5e44-0007-1823-c86-de200.mp4");
        println("fdsafds 1 " + System.currentTimeMillis())
        mediaPlayer!!.prepareAsync()
        println("fdsafds 2 " + System.currentTimeMillis())
    }

    override fun onPrepared(mp: MediaPlayer?) {
        println("fdsafds 31 " + System.currentTimeMillis())
        mp?.start()
    }

    override fun start() {
        TODO("Not yet implemented")
    }

    override fun pause() {
        TODO("Not yet implemented")
    }

    override val isPlaying: Boolean
        get() = TODO("Not yet implemented")

    override fun seekTo(time: Long) {
        TODO("Not yet implemented")
    }

    override fun release() {
        TODO("Not yet implemented")
    }

    override fun setSurface(surfaceHolder: SurfaceHolder) {
        mediaPlayer?.setDisplay(surfaceHolder)
    }

    override val currentPosition: Long
        get() = TODO("Not yet implemented")
    override val duration: Long
        get() = TODO("Not yet implemented")

    override fun setVolume(leftVolume: Float, rightVolume: Float) {
        TODO("Not yet implemented")
    }

    override fun setSpeed(speed: Float) {
        TODO("Not yet implemented")
    }


}