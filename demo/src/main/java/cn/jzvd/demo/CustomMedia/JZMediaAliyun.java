package cn.jzvd.demo.CustomMedia;

import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.util.Log;
import android.view.Surface;

import com.aliyun.player.AliPlayer;
import com.aliyun.player.AliPlayerFactory;
import com.aliyun.player.IPlayer;
import com.aliyun.player.bean.ErrorInfo;
import com.aliyun.player.bean.InfoBean;
import com.aliyun.player.bean.InfoCode;
import com.aliyun.player.nativeclass.PlayerConfig;
import com.aliyun.player.source.UrlSource;

import cn.jzvd.JZMediaInterface;
import cn.jzvd.Jzvd;
import cn.jzvd.demo.ApplicationDemo;

/**
 * usage: 阿里云播放器内核
 * author: kHRYSTAL
 * create time: 2020-09-01
 * update time:
 * email: 723526676@qq.com
 */
public class JZMediaAliyun extends JZMediaInterface implements IPlayer.OnPreparedListener, IPlayer.OnVideoSizeChangedListener, IPlayer.OnCompletionListener, IPlayer.OnErrorListener, IPlayer.OnInfoListener, IPlayer.OnSeekCompleteListener, IPlayer.OnRenderingStartListener, IPlayer.OnLoadingStatusListener {

    private static final String TAG = JZMediaAliyun.class.getSimpleName();
    private static final int FROM_ALIYUN_PLAYER_INFO = 0x1688;

    AliPlayer aliyunMediaPlayer;
    private boolean isPlaying;
    private long mCurrentPosition;

    public JZMediaAliyun(Jzvd jzvd) {
        super(jzvd);
    }

    @Override
    public void start() {
        if (aliyunMediaPlayer != null) {
            isPlaying = true;
            aliyunMediaPlayer.start();
            handler.post(() -> jzvd.onStatePlaying()); // 需要手动调用开始播放 改变ui播放状态
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        if (SAVED_SURFACE == null) {
            SAVED_SURFACE = surface;
            prepare();
        } else {
            jzvd.textureView.setSurfaceTexture(SAVED_SURFACE);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public void prepare() {
        release();
        mMediaHandler = new Handler();
        handler = new Handler();
        mMediaHandler.post(() -> {
            aliyunMediaPlayer = AliPlayerFactory.createAliPlayer(ApplicationDemo.APP_CONTEXT);

            //region 阿里云播放器基本设置 具体参考: {@link https://help.aliyun.com/document_detail/124714.html?spm=a2c4g.11186623.6.1083.2dbf2722XQM0Mr}
            //先获取配置
            PlayerConfig config = aliyunMediaPlayer.getConfig();
            //设置网络超时时间，单位ms
            config.mNetworkTimeout = 5000;
            //设置超时重试次数。每次重试间隔为networkTimeout。networkRetryCount=0则表示不重试，重试策略app决定，默认值为2
            config.mNetworkRetryCount = 2;
            //  配置请求头 refer UA
            //定义header
//            String[] headers = new String[1];
//            headers[0]="Host:xxx.com";//比如需要设置Host到header中。
//            config.setCustomHeaders(headers);
//            config.mUserAgent = "需要设置的UserAgent";
            //最大延迟。注意：直播有效。当延时比较大时，播放器sdk内部会追帧等，保证播放器的延时在这个范围内。
            config.mMaxDelayTime = 5000;
            // 最大缓冲区时长。单位ms。播放器每次最多加载这么长时间的缓冲数据。
            config.mMaxBufferDuration = 50000;
            //高缓冲时长。单位ms。当网络不好导致加载数据时，如果加载的缓冲时长到达这个值，结束加载状态。
            config.mHighBufferDuration = 3000;
            // 起播缓冲区时长。单位ms。这个时间设置越短，起播越快。也可能会导致播放之后很快就会进入加载状态。
            config.mStartBufferDuration = 500;
            //设置配置给播放器
            aliyunMediaPlayer.setConfig(config);
            //endregion

//            aliyunMediaPlayer.setAutoPlay(true); // 是否自动播放
//            aliyunMediaPlayer.setLoop(true); // 是否循环播放

            //region 监听
            aliyunMediaPlayer.setOnPreparedListener(JZMediaAliyun.this); // 准备成功事件
            aliyunMediaPlayer.setOnVideoSizeChangedListener(JZMediaAliyun.this); // 视频分辨率变化回调
            aliyunMediaPlayer.setOnCompletionListener(JZMediaAliyun.this); // 播放完成事件
            aliyunMediaPlayer.setOnErrorListener(JZMediaAliyun.this); // 出错事件
            aliyunMediaPlayer.setOnInfoListener(JZMediaAliyun.this); //其他信息的事件，type包括了：循环播放开始，缓冲位置，当前播放位置，自动播放开始等
            aliyunMediaPlayer.setOnSeekCompleteListener(JZMediaAliyun.this); // 拖动事件
            aliyunMediaPlayer.setOnRenderingStartListener(JZMediaAliyun.this); // 首帧渲染显示事件
            aliyunMediaPlayer.setOnLoadingStatusListener(JZMediaAliyun.this);
            //endregion

            //设置配置给播放器
            try {
                // 创建DataSource，准备播放 可通过jzDataSource区分是否加密
                UrlSource urlSource = new UrlSource();
                urlSource.setUri(jzvd.jzDataSource.getCurrentUrl().toString());
// 加密播放配置 以下的key可以通过jzDataSource去获取
//                VidSts vidSts = new VidSts();
//                vidSts.setVid("videoId");
//                vidSts.setAccessKeyId("akId");
//                vidSts.setAccessKeySecret("akSecret");
//                vidSts.setSecurityToken("authToken");
//                vidSts.setRegion("cn-shanghai");
                aliyunMediaPlayer.setDataSource(urlSource);
                aliyunMediaPlayer.setSurface(new Surface(SAVED_SURFACE));
                aliyunMediaPlayer.prepare();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        });
    }

    @Override
    public void pause() {
        if (aliyunMediaPlayer != null) {
            isPlaying = false;
            aliyunMediaPlayer.pause();
        }
    }

    @Override
    public boolean isPlaying() {
        return isPlaying;
    }

    @Override
    public void seekTo(long time) {
        if (aliyunMediaPlayer != null)
            aliyunMediaPlayer.seekTo(time);
    }

    @Override
    public void release() {
        if (mMediaHandler != null && aliyunMediaPlayer != null) {
            AliPlayer tmpMediaPlayer = aliyunMediaPlayer;
            JZMediaInterface.SAVED_SURFACE = null;
            isPlaying = false;
            mCurrentPosition = 0L;
            mMediaHandler.post(() -> {
                tmpMediaPlayer.setSurface(null);
                tmpMediaPlayer.release();
            });
            aliyunMediaPlayer = null;
        }
    }

    @Override
    public long getCurrentPosition() {
        if (aliyunMediaPlayer != null) {
            return mCurrentPosition;
        }
        return 0L;
    }

    @Override
    public long getDuration() {
        if (aliyunMediaPlayer != null) {
            return aliyunMediaPlayer.getDuration();
        }
        return 0L;
    }

    @Override
    public void setVolume(float leftVolume, float rightVolume) {
        aliyunMediaPlayer.setVolume(Math.max(leftVolume, rightVolume));
    }

    @Override
    public void setSpeed(float speed) {
        if (aliyunMediaPlayer != null)
            aliyunMediaPlayer.setSpeed(speed);
    }

    @Override
    public void setSurface(Surface surface) {
        if (aliyunMediaPlayer != null)
            aliyunMediaPlayer.setSurface(surface);
    }

    @Override
    public void onPrepared() {
        handler.post(() -> jzvd.onPrepared());
    }

    @Override
    public void onVideoSizeChanged(int i, int i1) {
        handler.post(() -> jzvd.onVideoSizeChanged(aliyunMediaPlayer.getVideoWidth(), aliyunMediaPlayer.getVideoHeight()));
    }

    @Override
    public void onCompletion() {
        isPlaying = false;
        mCurrentPosition = 0L;
        handler.post(() -> jzvd.onCompletion());
    }

    @Override
    public void onError(ErrorInfo errorInfo) {
        isPlaying = false;
        mCurrentPosition = 0L;
        // 具体参考阿里云播放器错误码
        handler.post(() -> jzvd.onError(errorInfo.getCode().getValue(), errorInfo.getCode().getValue()));
    }

    @Override
    public void onInfo(InfoBean infoBean) {
        if (infoBean.getCode() == InfoCode.AutoPlayStart) {
            //自动播放开始,需要设置播放状态
            isPlaying = true;
        } else if (infoBean.getCode() == InfoCode.BufferedPosition) {
            //更新bufferedPosition
            long videoBufferedPosition = infoBean.getExtraValue();
            handler.post(() -> jzvd.setBufferProgress((int) videoBufferedPosition));
        } else if (infoBean.getCode() == InfoCode.CurrentPosition) {
            //更新currentPosition
            mCurrentPosition = infoBean.getExtraValue();
        } else {
            handler.post(() -> jzvd.onInfo(FROM_ALIYUN_PLAYER_INFO, infoBean.getCode().getValue()));
        }
    }

    @Override
    public void onSeekComplete() {
        //拖动结束
        handler.post(() -> jzvd.onSeekComplete());
    }

    @Override
    public void onRenderingStart() {
        // 首帧渲染回调
    }

    @Override
    public void onLoadingBegin() {
        //缓冲开始
    }

    @Override
    public void onLoadingProgress(int percent, float kbps) {
        // 正在缓冲进度 可配置ui 参数1显示secondary进度条 参数2控制显示kbp/s
        handler.post(() -> jzvd.setBufferProgress(percent));
    }

    @Override
    public void onLoadingEnd() {
        //缓冲结束
    }
}
