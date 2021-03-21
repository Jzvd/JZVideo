package org.jzvd.jzvideo

import android.media.AudioManager
import android.media.MediaPlayer
import android.view.SurfaceHolder

/**
 * Created by Nathen on 2021/1/13.
 */
class JZMediaSystem(jzVideoA: JZVideoA?) : JZMediaInterface(jzVideoA),
    MediaPlayer.OnPreparedListener, MediaPlayer.OnVideoSizeChangedListener,
    MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener,
    MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener {

    var mediaPlayer: MediaPlayer? = null

    override fun prepare() {
        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)//AudioAttributes代码复杂没有这个好。
        mediaPlayer!!.isLooping = true
        mediaPlayer!!.setDataSource("http://jzvd.nathen.cn/video/25ae1b1c-1767b2a5e44-0007-1823-c86-de200.mp4");
        mediaPlayer!!.setOnPreparedListener(this@JZMediaSystem)
        mediaPlayer!!.setOnCompletionListener(this@JZMediaSystem)
        mediaPlayer!!.setOnBufferingUpdateListener(this@JZMediaSystem)
        mediaPlayer!!.setScreenOnWhilePlaying(true)
        mediaPlayer!!.setOnSeekCompleteListener(this@JZMediaSystem)
        mediaPlayer!!.setOnErrorListener(this@JZMediaSystem)
        mediaPlayer!!.setOnInfoListener(this@JZMediaSystem)
        mediaPlayer!!.setOnVideoSizeChangedListener(this@JZMediaSystem)
        mediaPlayer!!.prepareAsync()
    }

    override fun onPrepared(mp: MediaPlayer?) {
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

    override fun onVideoSizeChanged(mp: MediaPlayer?, width: Int, height: Int) {
        println("fdsfadsa width: ${width}, height: $height")//540, height: 952


    }

    override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {
        TODO("Not yet implemented")
    }

    override fun onCompletion(mp: MediaPlayer?) {
        TODO("Not yet implemented")
    }

    override fun onSeekComplete(mp: MediaPlayer?) {
        TODO("Not yet implemented")
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun onInfo(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        TODO("Not yet implemented")
    }

}