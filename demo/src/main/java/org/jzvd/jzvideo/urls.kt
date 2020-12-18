package org.jzvd.jzvideo

// Chrome浏览器和饺子demo(android.MediaPlayer)相同时间相同网络环境播放，速度相差很远，为什么。
// ijk好像略快，但没有电脑的chrome快。红米手机的自带浏览器可以完整观看视频无需中途加载。

val ldjVideos = arrayOf(
    "http://videos.jzvd.org/v/ldj/01-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/02-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/03-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/04-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/05-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/06-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/07-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/08-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/09-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/10-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/11-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/12-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/13-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/14-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/15-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/16-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/17-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/18-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/19-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/20-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/21-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/22-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/23-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/24-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/25-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/26-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/27-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/28-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/29-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/30-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/31-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/32-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/33-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/34-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/35-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/36-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/37-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/38-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/39-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/40-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/41-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/42-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/43-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/44-ldj.mp4",
    "http://videos.jzvd.org/v/ldj/45-ldj.mp4"
)

val videos = arrayOf(
    "http://videos.jzvd.org/v/饺子还年轻.mp4",
    "http://videos.jzvd.org/v/饺子主动.mp4",
    "http://videos.jzvd.org/v/饺子真萌.mp4",
    "http://videos.jzvd.org/v/饺子真会.mp4",
    "http://videos.jzvd.org/v/饺子运动.mp4",
    "http://videos.jzvd.org/v/饺子有活.mp4",
    "http://videos.jzvd.org/v/饺子星光.mp4",
    "http://videos.jzvd.org/v/饺子想听.mp4",
    "http://videos.jzvd.org/v/饺子想吹.mp4",
    "http://videos.jzvd.org/v/饺子汪汪.mp4",
    "http://videos.jzvd.org/v/饺子偷人.mp4",
    "http://videos.jzvd.org/v/饺子挺住.mp4",
    "http://videos.jzvd.org/v/饺子跳.mp4",
    "http://videos.jzvd.org/v/饺子受不了.mp4",
    "http://videos.jzvd.org/v/饺子三位.mp4",
    "http://videos.jzvd.org/v/饺子起飞.mp4",
    "http://videos.jzvd.org/v/饺子你听.mp4",
    "http://videos.jzvd.org/v/饺子可以了.mp4",
    "http://videos.jzvd.org/v/饺子可以.mp4",
    "http://videos.jzvd.org/v/饺子好妈妈.mp4",
    "http://videos.jzvd.org/v/饺子还小.mp4",
    "http://videos.jzvd.org/v/饺子高兴.mp4",
    "http://videos.jzvd.org/v/饺子高冷.mp4",
    "http://videos.jzvd.org/v/饺子堵住了.mp4",
    "http://videos.jzvd.org/v/饺子都懂.mp4",
    "http://videos.jzvd.org/v/饺子打电话.mp4",
    "http://videos.jzvd.org/v/饺子不服.mp4"
)

val thumbnails = arrayOf(
    "http://videos.jzvd.org/v/饺子还年轻.jpg",
    "http://videos.jzvd.org/v/饺子主动.jpg",
    "http://videos.jzvd.org/v/饺子真萌.jpg",
    "http://videos.jzvd.org/v/饺子真会.jpg",
    "http://videos.jzvd.org/v/饺子运动.jpg",
    "http://videos.jzvd.org/v/饺子有活.jpg",
    "http://videos.jzvd.org/v/饺子星光.jpg",
    "http://videos.jzvd.org/v/饺子想听.jpg",
    "http://videos.jzvd.org/v/饺子想吹.jpg",
    "http://videos.jzvd.org/v/饺子汪汪.jpg",
    "http://videos.jzvd.org/v/饺子偷人.jpg",
    "http://videos.jzvd.org/v/饺子挺住.jpg",
    "http://videos.jzvd.org/v/饺子跳.jpg",
    "http://videos.jzvd.org/v/饺子受不了.jpg",
    "http://videos.jzvd.org/v/饺子三位.jpg",
    "http://videos.jzvd.org/v/饺子起飞.jpg",
    "http://videos.jzvd.org/v/饺子你听.jpg",
    "http://videos.jzvd.org/v/饺子可以了.jpg",
    "http://videos.jzvd.org/v/饺子可以.jpg",
    "http://videos.jzvd.org/v/饺子好妈妈.jpg",
    "http://videos.jzvd.org/v/饺子还小.jpg",
    "http://videos.jzvd.org/v/饺子高兴.jpg",
    "http://videos.jzvd.org/v/饺子高冷.jpg",
    "http://videos.jzvd.org/v/饺子堵住了.jpg",
    "http://videos.jzvd.org/v/饺子都懂.jpg",
    "http://videos.jzvd.org/v/饺子打电话.jpg",
    "http://videos.jzvd.org/v/饺子不服.jpg"
)

val titles = arrayOf(
    "饺子还年轻",
    "饺子主动",
    "饺子真萌",
    "饺子真会",
    "饺子运动",
    "饺子有活",
    "饺子星光",
    "饺子想听",
    "饺子想吹",
    "饺子汪汪",
    "饺子偷人",
    "饺子挺住",
    "饺子跳",
    "饺子受不了",
    "饺子三位",
    "饺子起飞",
    "饺子你听",
    "饺子可以了",
    "饺子可以",
    "饺子好妈妈",
    "饺子还小",
    "饺子高兴",
    "饺子高冷",
    "饺子堵住了",
    "饺子都懂",
    "饺子打电话",
    "饺子不服"
)

val vl1 = videos.copyOfRange(3, 9)
val pl1 = videos.copyOfRange(3, 9)
val tl1 = videos.copyOfRange(3, 9)

val vl2 = videos.copyOfRange(10, 20)
val pl2 = videos.copyOfRange(10, 20)
val tl2 = videos.copyOfRange(10, 20)

val vl3 = videos.copyOfRange(20, 21)
val pl3 = videos.copyOfRange(20, 21)
val tl3 = videos.copyOfRange(20, 21)


val vll = arrayOf(
    vl1, vl2, vl3
)

val pll = arrayOf(
    pl1, pl2, pl3
)

val tll = arrayOf(
    tl1, tl2, tl3
)



