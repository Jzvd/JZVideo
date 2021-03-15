package org.jzvd.jzvideo.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.jzvd.demo.R
import org.jzvd.jzvideo.JZMediaSystem
import org.jzvd.jzvideo.JZVideoA

class mActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.jzvideo_activity_)

        val jzVideoA: JZVideoA = findViewById(R.id.jz_video)

        //这个版本可以只用surfaceview
        jzVideoA.setUp(
            "http://jzvd.nathen.cn/video/25ae1b1c-1767b2a5e44-0007-1823-c86-de200.mp4",
            JZMediaSystem::class,
        )
        jzVideoA.start()


    }

}