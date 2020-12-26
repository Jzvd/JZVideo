package org.jzvd.jzvideo

// Chrome浏览器和饺子demo(android.MediaPlayer)相同时间相同网络环境播放，速度相差很远，为什么。
// ijk好像略快，但没有电脑的chrome快。红米手机的自带浏览器可以完整观看视频无需中途加载。


val cn = "http://8.136.101.204"
val us = "http://videos.jzvd.org"

var server_name: String = cn

val ldjVideos = arrayOf(
    "$server_name/v/ldj/01-ldj.mp4",
    "$server_name/v/ldj/02-ldj.mp4",
    "$server_name/v/ldj/03-ldj.mp4",
    "$server_name/v/ldj/04-ldj.mp4",
    "$server_name/v/ldj/05-ldj.mp4",
    "$server_name/v/ldj/06-ldj.mp4",
    "$server_name/v/ldj/07-ldj.mp4",
    "$server_name/v/ldj/08-ldj.mp4",
    "$server_name/v/ldj/09-ldj.mp4",
    "$server_name/v/ldj/10-ldj.mp4",
    "$server_name/v/ldj/11-ldj.mp4",
    "$server_name/v/ldj/12-ldj.mp4",
    "$server_name/v/ldj/13-ldj.mp4",
    "$server_name/v/ldj/14-ldj.mp4",
    "$server_name/v/ldj/15-ldj.mp4",
    "$server_name/v/ldj/16-ldj.mp4",
    "$server_name/v/ldj/17-ldj.mp4",
    "$server_name/v/ldj/18-ldj.mp4",
    "$server_name/v/ldj/19-ldj.mp4",
    "$server_name/v/ldj/20-ldj.mp4",
    "$server_name/v/ldj/21-ldj.mp4",
    "$server_name/v/ldj/22-ldj.mp4",
    "$server_name/v/ldj/23-ldj.mp4",
    "$server_name/v/ldj/24-ldj.mp4",
    "$server_name/v/ldj/25-ldj.mp4",
    "$server_name/v/ldj/26-ldj.mp4",
    "$server_name/v/ldj/27-ldj.mp4",
    "$server_name/v/ldj/28-ldj.mp4",
    "$server_name/v/ldj/29-ldj.mp4",
    "$server_name/v/ldj/30-ldj.mp4",
    "$server_name/v/ldj/31-ldj.mp4",
    "$server_name/v/ldj/32-ldj.mp4",
    "$server_name/v/ldj/33-ldj.mp4",
    "$server_name/v/ldj/34-ldj.mp4",
    "$server_name/v/ldj/35-ldj.mp4",
    "$server_name/v/ldj/36-ldj.mp4",
    "$server_name/v/ldj/37-ldj.mp4",
    "$server_name/v/ldj/38-ldj.mp4",
    "$server_name/v/ldj/39-ldj.mp4",
    "$server_name/v/ldj/40-ldj.mp4",
    "$server_name/v/ldj/41-ldj.mp4",
    "$server_name/v/ldj/42-ldj.mp4",
    "$server_name/v/ldj/43-ldj.mp4",
    "$server_name/v/ldj/44-ldj.mp4",
    "$server_name/v/ldj/45-ldj.mp4"
)

val cndVideos = arrayOf(
    "https://jzvd.nathen.cn/video/cfe6c30-1767b1bc21f-0007-1823-c86-de200.mp4",//三个不同分辨率
    "https://jzvd.nathen.cn/0339d49439f947419576c33a0aa51545/79e1a938b0d2435d85bd964a77640506-f4e986e3e38ed3f473f7ba82bc07e188-ld.mp4",
    "https://jzvd.nathen.cn/0339d49439f947419576c33a0aa51545/79e1a938b0d2435d85bd964a77640506-8924c7da92ebd789d315bc5de0a81059-fd.mp4",

    "https://jzvd.nathen.cn/video/59aa468b-1767b6d891e-0007-1823-c86-de200.mp4",//饺子还小
    "https://jzvd.nathen.cn/video/25ae1b1c-1767b2a5e44-0007-1823-c86-de200.mp4",//饺子还年轻
    "https://jzvd.nathen.cn/video/5a6465ff-1767b2a5e28-0007-1823-c86-de200.mp4"
)

val cndThumbnail = arrayOf(
    "https://jzvd.nathen.cn/snapshot/0339d49439f947419576c33a0aa5154500005.jpg",
    "",
    "",

    "https://jzvd.nathen.cn/snapshot/61c99f9225c04b24a1d0374e9a3f006700004.jpg",
    "https://jzvd.nathen.cn/snapshot/044ef6cf452d48b795cea0a96ee4ea4100002.jpg",
    "https://jzvd.nathen.cn/snapshot/a172cc6442ff40ffb826829fc78f83b700005.jpg"
)

val videos = arrayOf(
    //width > height
    "$server_name/v/饺子主动.mp4",
    "$server_name/v/饺子运动.mp4",
    "$server_name/v/饺子有活.mp4",
    "$server_name/v/饺子星光.mp4",
    "$server_name/v/饺子想吹.mp4",
    "$server_name/v/饺子汪汪.mp4",
    "$server_name/v/饺子偷人.mp4",
    "$server_name/v/饺子跳.mp4",
    "$server_name/v/饺子受不了.mp4",
    "$server_name/v/饺子三位.mp4",
    "$server_name/v/饺子起飞.mp4",
    "$server_name/v/饺子你听.mp4",
    "$server_name/v/饺子可以了.mp4",
    "$server_name/v/饺子还小.mp4",
    "$server_name/v/饺子高兴.mp4",
    "$server_name/v/饺子高冷.mp4",
    "$server_name/v/饺子堵住了.mp4",
    "$server_name/v/饺子都懂.mp4",
    "$server_name/v/饺子打电话.mp4",
    "$server_name/v/饺子不服.mp4",
    //height > width
    "$server_name/v/饺子还年轻.mp4",
    "$server_name/v/饺子好妈妈.mp4",
    "$server_name/v/饺子可以.mp4",
    "$server_name/v/饺子挺住.mp4",
    "$server_name/v/饺子想听.mp4",
    "$server_name/v/饺子真会.mp4",
    "$server_name/v/饺子真萌.mp4"

    )

val thumbnails = arrayOf(
    "$server_name/v/饺子主动.jpg",
    "$server_name/v/饺子运动.jpg",
    "$server_name/v/饺子有活.jpg",
    "$server_name/v/饺子星光.jpg",
    "$server_name/v/饺子想吹.jpg",
    "$server_name/v/饺子汪汪.jpg",
    "$server_name/v/饺子偷人.jpg",
    "$server_name/v/饺子跳.jpg",
    "$server_name/v/饺子受不了.jpg",
    "$server_name/v/饺子三位.jpg",
    "$server_name/v/饺子起飞.jpg",
    "$server_name/v/饺子你听.jpg",
    "$server_name/v/饺子可以了.jpg",
    "$server_name/v/饺子还小.jpg",
    "$server_name/v/饺子高兴.jpg",
    "$server_name/v/饺子高冷.jpg",
    "$server_name/v/饺子堵住了.jpg",
    "$server_name/v/饺子都懂.jpg",
    "$server_name/v/饺子打电话.jpg",
    "$server_name/v/饺子不服.jpg",

    //height > width
    "$server_name/v/饺子还年轻.jpg",
    "$server_name/v/饺子好妈妈.jpg",
    "$server_name/v/饺子可以.jpg",
    "$server_name/v/饺子挺住.jpg",
    "$server_name/v/饺子想听.jpg",
    "$server_name/v/饺子真会.jpg",
    "$server_name/v/饺子真萌.jpg"

)

val titles = arrayOf(
    //width > height
    "饺子主动",
    "饺子运动",
    "饺子有活",
    "饺子星光",
    "饺子想吹",
    "饺子汪汪",
    "饺子偷人",
    "饺子跳",
    "饺子受不了",
    "饺子三位",

    "饺子起飞",
    "饺子你听",
    "饺子可以了",
    "饺子还小",
    "饺子高兴",
    "饺子高冷",
    "饺子堵住了",
    "饺子都懂",
    "饺子打电话",
    "饺子不服",

    //height > width
    "饺子还年轻",
    "饺子好妈妈",
    "饺子可以",
    "饺子挺住",
    "饺子想听",
    "饺子真会",
    "饺子真萌"
)

val vl1 = videos.copyOfRange(0, 9)
val pl1 = thumbnails.copyOfRange(0, 9)
val tl1 = titles.copyOfRange(0, 9)

val vl2 = videos.copyOfRange(10, 20)
val pl2 = thumbnails.copyOfRange(10, 20)
val tl2 = titles.copyOfRange(10, 20)

val vl3 = videos.copyOfRange(20, 26)
val pl3 = thumbnails.copyOfRange(20, 26)
val tl3 = titles.copyOfRange(20, 26)


val vll = arrayOf(
    vl1, vl2, vl3
)

val pll = arrayOf(
    pl1, pl2, pl3
)

val tll = arrayOf(
    tl1, tl2, tl3
)





