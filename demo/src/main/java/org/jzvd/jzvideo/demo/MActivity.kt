package org.jzvd.jzvideo.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.jzvd.demo.R
import org.jzvd.jzvideo.JZMediaSystem
import org.jzvd.jzvideo.JZSurfaceView
import org.jzvd.jzvideo.JZVideoA

class mActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.jzvideo_activity_)

        val jzVideoA: JZVideoA = findViewById(R.id.jz_video)

        jzVideoA.setUp("", JZMediaSystem::class, JZSurfaceView::class)



    }

}