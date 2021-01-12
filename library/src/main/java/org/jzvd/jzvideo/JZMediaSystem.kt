package org.jzvd.jzvideo

/**
 * Created by Nathen on 2021/1/13.
 */
class JZMediaSystem(jzVideoA: JZVideoA) : JZMediaInterface(jzVideoA) {
    override fun start() {
        TODO("Not yet implemented")


    }

    override fun prepare() {
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