package cn.jzvd.demo.api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.jzvd.JzvdStd;
import cn.jzvd.demo.utils.AnimatedGifEncoder;

/**
 * gif截图
 * Created by dl on 2020/03/30.
 */

public class GifCreateHelper {

    /**
     * 获取某一时刻视频图片
     *
     * @param url
     * @param type 链接类型：NETWORK 网络，LOCAL 本地
     * @param time 视频的时间点（单位：毫秒）
     * @return
     */
    public static final int NETWORK = 0;
    public static final int LOCAL = 1;
    private final String completeButNoImageTag = "completeButError";
    public JzvdStd mPlayer;
    //最后生成的gif的默认存储路径
    public String mGifPath = "";
    String cacheImageDir = Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/jiaoziTemp";
    boolean isDownloadComplete = false;
    private JzGifListener mJzGifListener;
    //gif的帧之间延时
    private int mDelay = 50;
    //采样率
    private int mSampleSize = 1;
    //缩小比例
    private int mSmallScale = 5;
    //gif时长，毫秒
    private int mGifPeriod = 5000;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * @param delay        每一帧之间的延时
     * @param inSampleSize 采样率，最小值1 即：每隔inSampleSize个像素点，取一个读入到内存。越大处理越快
     * @param smallScale   缩小倍数，越大处理越快
     * @param gifPeriod    gif时长，毫秒
     * @param gifPath      gif文件的存储路径
     */
    public GifCreateHelper(JzvdStd jzvdStd, JzGifListener jzGifListener,
                           int delay, int inSampleSize, int smallScale, int gifPeriod, String gifPath) {
        mPlayer = jzvdStd;
        mJzGifListener = jzGifListener;
        mDelay = delay;
        mSampleSize = inSampleSize;
        mSmallScale = smallScale;
        mGifPeriod = gifPeriod;
        mGifPath = TextUtils.isEmpty(gifPath) ? mGifPath : gifPath;
    }

    /**
     * 删除文件夹和文件夹里面的文件
     *
     * @param dir
     */
    public static void deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDirWihtFile(file); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }

    public static Bitmap getBitmapFormVideoUrl(String url, int type, long time) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            if (Build.VERSION.SDK_INT >= 14) {

                if (NETWORK == type) {
                    retriever.setDataSource(url, new HashMap<String, String>());
                } else if (LOCAL == type) {
                    if (url.contains("file:///")) {
                        url = url.replace("file://", "");
                    }
                    retriever.setDataSource(url);
                } else {
                    retriever.setDataSource(url, new HashMap<String, String>());
                }
            } else {
                retriever.setDataSource(url);
            }

            //可惜这个方法不能设置采样率，不然效率能提高点。（可优化点）
            bitmap = retriever.getFrameAtTime(time * 1000, MediaMetadataRetriever.OPTION_CLOSEST);//微秒
        } catch (Exception e) { // IllegalArgumentException RuntimeException
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    /**
     * 开始gif截图
     */
    public void startGif() {
        startGif(mPlayer.getCurrentPositionWhenPlaying(), (String) mPlayer.jzDataSource.getCurrentUrl());
    }

    /**
     * @param bitmapFromTime gif图在视频中的开始时间
     * @param vedioUrl       视频链接
     */
    public void startGif(long bitmapFromTime, String vedioUrl) {
        int bitmapCount = mGifPeriod / mDelay;
        String[] picList = new String[bitmapCount];
        isDownloadComplete = false;
        for (int i = 0; i < bitmapCount; i++) {
            final int index = i;
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    //可优化点，找到替代的能加入采样点api，能提高效率,此处先缓存到本地，全放入内存占用空间太大
                    String path = saveBitmap(getBitmapFormVideoUrl(vedioUrl, vedioUrl.startsWith("http") ? NETWORK : LOCAL, bitmapFromTime + index * mDelay),
                            cacheImageDir + "/" + System.currentTimeMillis() + "index-" + index + ".png");
                    boolean isCurrentSuccess = true;
                    if (!TextUtils.isEmpty(path)) {
                        picList[index] = path;
                    } else {
                        picList[index] = completeButNoImageTag;
                        isCurrentSuccess = false;
                    }

                    checkCompleteAndDoNext(picList, isCurrentSuccess);
                }
            });
        }
    }

    private void checkCompleteAndDoNext(String[] picList, boolean isCurrentSuccess) {
        synchronized (GifCreateHelper.class) {
            if (isDownloadComplete) {
                return;
            }

            if (picList == null || picList.length == 0) {
                combinePicToGif(picList);
            }

            int emptyCount = 0;
            for (String path : picList) {
                if (TextUtils.isEmpty(path)) {
                    emptyCount++;
                }
            }

            mJzGifListener.process(picList.length - emptyCount, picList.length, isCurrentSuccess ? "下载成功" : "下载失败");

            if (emptyCount == 0) {
                isDownloadComplete = true;
                combinePicToGif(picList);
            }
        }
    }

    private void combinePicToGif(String[] picList) {
        File gifFile = ensureFile(new File(mGifPath));
        ArrayList<String> rightPic = new ArrayList<>();
        for (String picItem : picList) {
            if (!TextUtils.isEmpty(picItem) && !completeButNoImageTag.equals(picItem)) {
                rightPic.add(picItem);
            }
        }

        if (rightPic.size() > 2) {
            if (createGif(gifFile, rightPic, mDelay, mSampleSize, mSmallScale)) {
                mJzGifListener.result(true, gifFile);
            } else {
                mJzGifListener.result(false, null);
            }
        } else {
            mJzGifListener.result(false, null);
        }
        deleteDirWihtFile(new File(cacheImageDir));//清除缓存的图片
    }

    /**
     * 保存bitmap到本地
     *
     * @param bitmap Bitmap
     * @param path   本地路径
     */
    public String saveBitmap(Bitmap bitmap, String path) {
        if (bitmap == null) {
            return null;
        }
        try {
            File filePic = ensureFile(new File(path));
            FileOutputStream fos = new FileOutputStream(filePic);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            return null;
        }
        return path;
    }

    /**
     * 如果文件或者路径为空，就自动创建
     *
     * @param file
     * @return
     */
    private File ensureFile(File file) {
        if (!file.exists()) {
            try {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }

                file.createNewFile();
            } catch (IOException var7) {
                var7.printStackTrace();
            }
        }

        return file;
    }

    /**
     * 生成gif图
     *
     * @param file         保存的文件路径，请确保文件夹目录已经创建
     * @param pics         需要转化的bitmap本地路径集合
     * @param delay        每一帧之间的延时
     * @param inSampleSize 采样率，最小值1 即：每隔inSampleSize个像素点，取一个读入到内存。越大处理越快
     * @param smallScale   缩小倍数，越大处理越快
     */
    public boolean createGif(File file, List<String> pics, int delay, int inSampleSize, int smallScale) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        AnimatedGifEncoder localAnimatedGifEncoder = new AnimatedGifEncoder();
        localAnimatedGifEncoder.start(baos);
        localAnimatedGifEncoder.setRepeat(0);//设置生成gif的开始播放时间。0为立即开始播放
        localAnimatedGifEncoder.setDelay(delay);
        for (int i = 0; i < pics.size(); i++) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = inSampleSize;
            options.inJustDecodeBounds = true; // 先获取原大小
            BitmapFactory.decodeFile(pics.get(i), options);
            double w = (double) options.outWidth / smallScale;
            double h = (double) options.outHeight / smallScale;
            options.inJustDecodeBounds = false; // 获取新的大小
            Bitmap bitmap = BitmapFactory.decodeFile(pics.get(i), options);
            Bitmap pic = ThumbnailUtils.extractThumbnail(bitmap, (int) w, (int) h);
            localAnimatedGifEncoder.addFrame(pic);
            bitmap.recycle();
            pic.recycle();
            mJzGifListener.process(i, pics.size(), "组合中");
        }
        localAnimatedGifEncoder.finish();//finish
        try {
            FileOutputStream fos = new FileOutputStream(file.getPath());
            baos.writeTo(fos);
            baos.flush();
            fos.flush();
            baos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * 保存进度以及结果的回调
     */
    public interface JzGifListener {

        void process(int curPosition, int total, String status);

        void result(boolean success, File file);
    }
}
