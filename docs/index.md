[饺子视频播放器文档](https://github.com/Jzvd/JZVideo)围绕Demo([点此下载](https://github.com/Jzvd/JZVideo/releases))进行讲解。集成JZVideo之前，看完Demo的每个页面，点击Demo的每个按钮之后，再开始写代码。

## 一、QuickStart

[见GitHub首页](https://github.com/Jzvd/JZVideo)

## 二、初级用法

不需要继承JzvdStd，直接调用JzvdStd暴露的接口和变量。效果见Demo的Tab_1

1.设置视频的填充模式 [进入源码](https://github.com/Jzvd/JZVideo/blob/develop/demo/src/main/java/cn/jzvd/demo/Tab_1_Basic/RotationVideoSizeActivity.java)

```
Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_ADAPTER);
Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_PARENT);
Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_SCROP);
Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_ORIGINAL);
```

2.设置视频的方向 [进入源码](https://github.com/Jzvd/JZVideo/blob/develop/demo/src/main/java/cn/jzvd/demo/Tab_1_Basic/RotationVideoSizeActivity.java)

```
Jzvd.setTextureViewRotation(90); //0 - 360 旋转的角度
```

3.切换播放内核 [进入源码](https://github.com/Jzvd/JZVideo/blob/develop/demo/src/main/java/cn/jzvd/demo/Tab_1_Basic/CustomMediaActivity.java)，选择适合项目的播放内核。

```
//Demo提供了四个播放内核。JZMediaAliyun,JZMediaIjk.class,JZMediaSystem.class,JZMediaExo.class，通过如下两种方式使用。
jzvdStd.setUp("http://url.mp4", "title", JzvdStd.SCREEN_NORMAL, **.class);
or
jzvdStd.setMediaInterface(**.class);
```

4.分别设置非全屏和全屏后的屏幕方向 [进入源码](https://github.com/Jzvd/JZVideo/blob/develop/demo/src/main/java/cn/jzvd/demo/Tab_1_Basic/OrientationActivity.java#L41)

```
Jzvd.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;// 非全屏是横屏
Jzvd.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;// 进入全屏后是竖屏
```

5.根据传感器自动旋转屏幕进入全屏 [进入源码](https://github.com/Jzvd/JZVideo/blob/develop/demo/src/main/java/cn/jzvd/demo/Tab_1_Basic/ScreenRotateActivity.java)

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

6.预加载 [进入源码]()

```
jzvdStd.startPreloading(); //开始预加载，加载完等待播放
jzvdStd.startVideoAfterPreloading(); //如果预加载完会开始播放，如果未加载则开始加载
```

## 三、继承JzvdStd，实现更多自定义功能，效果见Demo的Tab_2

- AgVideo 完全自定义UI [进入源码](https://github.com/Jzvd/JZVideo/blob/develop/demo/src/main/java/cn/jzvd/demo/Tab_2_Custom/AGVideo/AGVideo.java)

1.继承JzvdStd后删除不需要的父类控件 [进入源码](https://github.com/Jzvd/JZVideo/blob/develop/demo/src/main/java/cn/jzvd/demo/CustomJzvd/MyJzvdStdNoTitleNoClarity.java)

2.全屏锁 [进入源码](https://github.com/Jzvd/JZVideo/blob/develop/demo/src/main/java/cn/jzvd/demo/CustomJzvd/JzvdStdLockScreen.java)

3.全屏时显示分享按钮 [进入源码](https://github.com/Jzvd/JZVideo/blob/develop/demo/src/main/java/cn/jzvd/demo/CustomJzvd/JzvdStdShowShareButtonAfterFullscreen.java)

4.全屏时显示title，非全屏隐藏title [进入源码](https://github.com/Jzvd/JZVideo/blob/develop/demo/src/main/java/cn/jzvd/demo/CustomJzvd/JzvdStdShowTitleAfterFullscreen.java)

5.播放完毕后不隐藏TextureView显示视频的最后一帧 [进入源码](https://github.com/Jzvd/JZVideo/blob/develop/demo/src/main/java/cn/jzvd/demo/CustomJzvd/JzvdStdShowTextureViewAfterAutoComplete.java)

6.全屏播放完成不退出全屏 [进入源码](https://github.com/Jzvd/JZVideo/blob/develop/demo/src/main/java/cn/jzvd/demo/CustomJzvd/JzvdStdAutoCompleteAfterFullscreen.java)

7.设置视频高宽比例 [进入源码](https://github.com/Jzvd/JZVideo/blob/develop/demo/src/main/java/cn/jzvd/demo/Fragment_2_Custom.java#L111)

8.非全屏静音，全屏之后有声音 [进入源码](https://github.com/Jzvd/JZVideo/blob/develop/demo/src/main/java/cn/jzvd/demo/CustomJzvd/JzvdStdVolumeAfterFullscreen.java)

9.全屏显示静音按钮 [进入源码](https://github.com/Jzvd/JZVideo/blob/develop/demo/src/main/java/cn/jzvd/demo/CustomJzvd/JzvdStdVolume.java)

10.播放mp3 [进入源码](https://github.com/Jzvd/JZVideo/blob/develop/demo/src/main/java/cn/jzvd/demo/CustomJzvd/JzvdStdMp3.java)

11.倍速播放 [进入源码](https://github.com/Jzvd/JZVideo/blob/develop/demo/src/main/java/cn/jzvd/demo/CustomJzvd/JzvdStdSpeed.java)

## 四、列表的使用

推荐RecycleView，不推荐ListView

## 五、进阶用法

1.AgVideo 完全自定义UI

2.详情页穿透

3.Gif截图

4.列表自动播放

5.仿抖音UI

6.小窗播

