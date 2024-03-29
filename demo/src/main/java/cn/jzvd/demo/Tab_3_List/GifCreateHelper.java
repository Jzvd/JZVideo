package cn.jzvd.demo.Tab_3_List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.jzvd.JzvdStd;
import cn.jzvd.demo.utils.AnimatedGifEncoder;
import wseemann.media.FFmpegMediaMetadataRetriever;

/**
 * gif截图
 * Created by dl on 2020/03/30.
 */

public class GifCreateHelper {

    private final String completeButNoImageTag = "completeButError";
    public JzvdStd mPlayer;
    //最后生成的gif的默认存储路径
    public String mGifPath = Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/jiaozi-" + System.currentTimeMillis() + ".gif";
    String cacheImageDir = Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/jiaoziTemp";
    boolean isDownloadComplete = false;
    //缩小比例
    @Deprecated
    private int mSmallScale = 1;
    private JzGifListener mJzGifListener;
    //gif的帧之间延时
    private int mDelay = 50;
    //采样率
    private int mSampleSize = 1;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * @param delay        每一帧之间的延时
     * @param inSampleSize 采样率，最小值1 即：每隔inSampleSize个像素点，取一个读入到内存。越大处理越快
     * @param gifPath      gif文件的存储路径
     */
    public GifCreateHelper(JzvdStd jzvdStd, JzGifListener jzGifListener,
                           int delay, int inSampleSize, String gifPath) {
        mPlayer = jzvdStd;
        mJzGifListener = jzGifListener;
        mDelay = delay;
        mSampleSize = inSampleSize;
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

    private FFmpegMediaMetadataRetriever prepareFFmpegMediaMetadataRetriever(String vedioUrl) {
        FFmpegMediaMetadataRetriever mmr = new FFmpegMediaMetadataRetriever();
        mmr.setDataSource(vedioUrl);
        mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ALBUM);
        mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ARTIST);

        return mmr;
    }

    public void getPeriodGif(long gifStartTime,long gifEndTime,int bitmapWidth,int bitmapHeight){
        String vedioUrl=(String) mPlayer.jzDataSource.getCurrentUrl();
        int bitmapCount = (int)((gifEndTime-gifStartTime) / mDelay);
        String[] picList = new String[bitmapCount];
        isDownloadComplete = false;
        FFmpegMediaMetadataRetriever mmr = prepareFFmpegMediaMetadataRetriever(vedioUrl);
        for (int i = 0; i < bitmapCount; i++) {
            final int index = i;
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    //先缓存到本地，全放入内存占用空间太大
                    String path = saveBitmap(mmr.getScaledFrameAtTime((gifStartTime + index * mDelay) * 1000, FFmpegMediaMetadataRetriever.OPTION_CLOSEST, bitmapWidth, bitmapHeight),
                            cacheImageDir + "/" + System.currentTimeMillis() + "index-" + index + ".png");
                    boolean isCurrentSuccess = true;
                    if (!TextUtils.isEmpty(path)) {
                        picList[index] = path;
                    } else {
                        picList[index] = completeButNoImageTag;
                        isCurrentSuccess = false;
                    }

                    checkCompleteAndDoNext(picList, isCurrentSuccess);
                    if (isDownloadComplete) {
                        mmr.release();
                    }
                }
            });
        }
    }

    public List<Bitmap> getBitmaps(List<Long> bitmapTime, int bitmapWidth,int bitmapHeight) {
        String vedioUrl=(String) mPlayer.jzDataSource.getCurrentUrl();
        List<Bitmap> bitmaps = new ArrayList<>();
        FFmpegMediaMetadataRetriever mmr = prepareFFmpegMediaMetadataRetriever(vedioUrl);

        for (int i = 0; i < bitmapTime.size(); i++) {
            // 给的时间可能会超过视频总时长，所以要try catch
            try {
                Bitmap bitmap = mmr.getScaledFrameAtTime(bitmapTime.get(i)*1000, FFmpegMediaMetadataRetriever.OPTION_CLOSEST, bitmapWidth, bitmapHeight);
                bitmaps.add(bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        mmr.release();
        return bitmaps;
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
        if(picList==null){
            picList=new String[0];
        }
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
        } finally {
            bitmap.recycle();
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
