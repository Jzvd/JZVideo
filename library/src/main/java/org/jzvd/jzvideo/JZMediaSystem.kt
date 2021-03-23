package org.jzvd.jzvideo

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
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
        mediaPlayer!!.setDataSource("http://jzvd.nathen.cn/video/25ae1b1c-1767b2a5e44-0007-1823-c86-de200.mp4")
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

    override fun start() {
        mMediaHandler?.post { mediaPlayer!!.start() }
    }

    override fun pause() {
        mMediaHandler?.post { mediaPlayer!!.pause() }
    }

    override val isPlaying: Boolean
        get() = mediaPlayer!!.isPlaying

    override fun seekTo(time: Long) {
        mMediaHandler?.post {
            try {
                mediaPlayer!!.seekTo(time.toInt())
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
        }
    }

    //这块不好有隐患。
    override fun release() { //not perfect change you later
        if (mMediaHandler != null && mMediaHandlerThread != null && mediaPlayer != null) { //不知道有没有妖孽
            val tmpHandlerThread = mMediaHandlerThread
            val tmpMediaPlayer: MediaPlayer = mediaPlayer as MediaPlayer
//            cn.jzVideoA.JZMediaInterface.SAVED_SURFACE = null
            mMediaHandler?.post {
                tmpMediaPlayer.setSurface(null)
                tmpMediaPlayer.release()
                tmpHandlerThread?.quit()
            }
            mediaPlayer = null
        }
    }

    override val currentPosition: Long
        get() = if (mediaPlayer != null) {
            mediaPlayer!!.currentPosition.toLong()
        } else {
            0
        }

    override val duration: Long
        get() = if (mediaPlayer != null) {
            mediaPlayer!!.duration.toLong()
        } else {
            0
        }


    override fun setVolume(leftVolume: Float, rightVolume: Float) {
        if (mMediaHandler == null) return
        mMediaHandler?.post {
            if (mediaPlayer != null) mediaPlayer!!.setVolume(
                leftVolume,
                rightVolume
            )
        }
    }

    override fun setSpeed(speed: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val pp = mediaPlayer!!.playbackParams
            pp.speed = speed
            mediaPlayer!!.playbackParams = pp
        }
    }

    override fun onPrepared(mediaPlayer: MediaPlayer) {
        handler?.post { jzVideoA?.onPrepared() } //如果是mp3音频，走这里
    }

    override fun onCompletion(mediaPlayer: MediaPlayer) {
        handler?.post { jzVideoA?.onCompletion() }
    }

    override fun onBufferingUpdate(mediaPlayer: MediaPlayer, percent: Int) {
        handler?.post { jzVideoA?.setBufferProgress(percent) }
    }

    override fun onSeekComplete(mediaPlayer: MediaPlayer) {
        handler?.post { jzVideoA?.onSeekComplete() }
    }

    override fun onError(mediaPlayer: MediaPlayer, what: Int, extra: Int): Boolean {
        handler?.post { jzVideoA?.onError(what, extra) }
        return true
    }

    override fun onInfo(mediaPlayer: MediaPlayer, what: Int, extra: Int): Boolean {
        handler?.post { jzVideoA?.onInfo(what, extra) }
        return false
    }

    override fun onVideoSizeChanged(mediaPlayer: MediaPlayer, width: Int, height: Int) {
        handler?.post { jzVideoA?.onVideoSizeChanged(width, height) }
    }

    override fun setSurface(surface: SurfaceHolder) {
        mediaPlayer!!.setDisplay(surface)
    }
}