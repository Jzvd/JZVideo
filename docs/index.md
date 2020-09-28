# 中文文档

首先下载Demo，Demo是文档的重要组成部分，看完Demo的每个页面，尝试Demo的每个按钮之后，再集成JZVideo的代码。

# QuickStart

见GitHub首页

# 初级用法(或者放到二级页面，也没必要有代码，放链接就好了)

不需要继承JzvdStd，直接调用JzvdStd暴露的接口和变量。效果见Demo的Tab_1

1.[设置视频的填充模式](https://github.com/Jzvd/JZVideo/blob/develop/demo/src/main/java/cn/jzvd/demo/Tab_1_Basic/RotationVideoSizeActivity.java)

```
Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_ADAPTER);
Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_PARENT);
Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_SCROP);
Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_ORIGINAL);
```

2.[设置视频的方向](https://github.com/Jzvd/JZVideo/blob/develop/demo/src/main/java/cn/jzvd/demo/Tab_1_Basic/RotationVideoSizeActivity.java)

```
Jzvd.setTextureViewRotation(90); //0 - 360 旋转的角度
```

3.[自定义播放内核](https://github.com/Jzvd/JZVideo/blob/develop/demo/src/main/java/cn/jzvd/demo/Tab_1_Basic/CustomMediaActivity.java)，选择适合项目的播放内核。
```

jzvdStd.setUp("http://url.mp4", "title", JzvdStd.SCREEN_NORMAL, JZMediaAliyun.class);
jzvdStd.setUp("http://url.mp4", "title", JzvdStd.SCREEN_NORMAL, JZMediaIjk.class);
jzvdStd.setUp("http://url.mp4", "title", JzvdStd.SCREEN_NORMAL, JZMediaSystem.class);
jzvdStd.setUp("http://url.mp4", "title", JzvdStd.SCREEN_NORMAL, JZMediaExo.class);
```

4.[控制全屏前和全屏后的屏幕方向，进入全屏前和全屏后的屏幕方向](https://github.com/Jzvd/JZVideo/blob/develop/demo/src/main/java/cn/jzvd/demo/Tab_1_Basic/OrientationActivity.java#L41)，可以设置为退出全屏时横屏，进入全屏时竖屏。

```
Jzvd.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
Jzvd.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
```

5.[根据传感器自动旋转屏幕进入全屏](https://github.com/Jzvd/JZVideo/blob/develop/demo/src/main/java/cn/jzvd/demo/Tab_1_Basic/ScreenRotateActivity.java)

```
@Override
public void orientationChange(int orientation) {
    if (orientation >= 45 && orientation <= 315 && mJzvdStd.screen == Jzvd.SCREEN_NORMAL) {
        changeScreenFullLandscape(ScreenRotateUtils.orientationDirection);
    } else if (((orientation >= 0 && orientation < 45) || orientation > 315) && mJzvdStd.screen == Jzvd.SCREEN_FULLSCREEN) {
        changeScrenNormal();
    }
}
```

# 继承JzvdStd，实现更多自定义功能，效果见Demo的Tab_2 (内容较多，应放到二级页面)

1.继承JzvdStd后删除不需要的父类控件

2.全屏锁

3.全屏时显示分享按钮

4.全屏时显示title，非全屏隐藏title

5.播放完毕后不隐藏TextureView显示视频的最后一帧

6.播放完成不退出全屏状态

7.设置视频高宽比例

8.非全屏静音，全屏之后有声音

9.全屏显示静音按钮

10.播放mp3

11.倍速播放

# 列表的使用，推荐RecycleView，不推荐ListView




