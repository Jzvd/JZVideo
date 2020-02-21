package cn.jzvd.demo;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;

import androidx.annotation.NonNull;


/**
 * Created by HRR on 2020/02/22.
 */
public class ScreenRotateUtils {
    private Activity mActivity;
    private boolean isClickFullScreen = false;
    private boolean isOpenSensor = true;      // 是否打开传输，默认打开
    private boolean isLandscape = false;    // 默认是竖屏
    private boolean isChangeOrientation = true;  // 记录点击全屏后屏幕朝向是否改变，默认会自动切换
    private boolean isEffectSysSetting = true;   // 手机系统的重力感应设置是否生效，默认无效，想要生效改成true就好了
    private SensorManager sm;
    private OrientationSensorListener listener;
    private OrientationChangeListener changeListener;
    private Sensor sensor;
    //上一次的旋转角度
    private int lastOrientation = 0;
    //上一次是否是顺时针
    private boolean lastIsClockwise = false;
    public static boolean isClockwise = false;
    private static int DATA_X = 0;
    private static int DATA_Y = 1;
    private static int DATA_Z = 2;
    private static int ORIENTATION_UNKNOWN = -1;
    private static ScreenRotateUtils instance;

    public ScreenRotateUtils(Context context) {
        sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sm.getDefaultSensor(Sensor.TYPE_GRAVITY);
        listener = new OrientationSensorListener(mHandler);
    }

    public static ScreenRotateUtils getInstance(Context context) {
        if (instance == null) {
            instance = new ScreenRotateUtils(context);
        }
        return instance;
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 888) {
                int orientation = msg.arg1;
                /**
                 * 根据手机屏幕的朝向角度，来设置内容的横竖屏，并且记录状态
                 */
                changeListener.orientationChange(orientation);
            }
        }
    };

    void setOrientationChangeListener(OrientationChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    interface OrientationChangeListener {
        void orientationChange(int orientation);
    }

    class OrientationSensorListener implements SensorEventListener {
        private Handler rotateHandler;

        public OrientationSensorListener(Handler rotateHandler) {
            this.rotateHandler = rotateHandler;
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;
            int orientation = ORIENTATION_UNKNOWN;
            float x = -values[DATA_X];
            float y = -values[DATA_Y];
            float z = -values[DATA_Z];
            float magnitude = x * x + y * y;
            if (magnitude * 4 >= z * z) {
                float oneEightyOverPi = 57.29577957855f;
                float angle = (float) (Math.atan2(-y,x) * oneEightyOverPi);

                orientation = 90 - Math.round(angle);
                // normalize to 0 - 359 range
                while (orientation >= 360) {
                    orientation -= 360;
                }
                while (orientation < 0) {
                    orientation += 360;
                }
            }

            /**
             * 获取手机系统的重力感应开关设置，这段代码看需求，不要就删除
             * screenchange = 1 表示开启，screenchange = 0 表示禁用
             * 要是禁用了就直接返回
             */
            if (isEffectSysSetting) {
                try {
                    int isRotate = Settings.System.getInt(mActivity.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION);
                    // 如果用户禁用掉了重力感应就直接return
                    if (isRotate == 0) {
                        return;
                    }
                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                }


                //判断当前旋转是顺时针还是逆时针
                if (orientation > lastOrientation) {
                    isClockwise = true;
                } else if (orientation < lastOrientation) {
                    isClockwise = false;
                } else {
                    isClockwise = lastIsClockwise;
                }
                //保存当前旋转方向是否是顺时针
                lastIsClockwise = isClockwise;
                // 只有点了按钮时才需要根据当前的状态来更新状态
                if (isClickFullScreen) {
                    if (isLandscape && screenIsPortrait(orientation)) {           // 之前是横屏，并且当前是竖屏的状态
                        updateState(false, false, true, true);
                    } else if (!isLandscape && screenIsLandscape(orientation)) {  // 之前是竖屏，并且当前是横屏的状态
                        updateState(true, false, true, true);
                    } else if (isLandscape && screenIsLandscape(orientation)) {    // 之前是横屏，现在还是横屏的状态
                        isChangeOrientation = false;
                    } else if (!isLandscape && screenIsPortrait(orientation)) {  // 之前是竖屏，现在还是竖屏的状态
                        isChangeOrientation = false;
                    }
                }

                // 判断是否要进行中断信息传递
                if (!isOpenSensor) {
                    return;
                }
                //保存旋转角度，用做下次旋转时的判断
                lastOrientation = orientation;
                rotateHandler.obtainMessage(888, orientation, 0).sendToTarget();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }

    /**
     * 更新状态
     *
     * @param isLandscape         横屏
     * @param isClickFullScreen   全屏点击
     * @param isOpenSensor        打开传输
     * @param isChangeOrientation 朝向改变
     */
    private void updateState(boolean isLandscape, boolean isClickFullScreen, boolean isOpenSensor, boolean isChangeOrientation) {
        this.isLandscape = isLandscape;
        this.isClickFullScreen = isClickFullScreen;
        this.isOpenSensor = isOpenSensor;
        this.isChangeOrientation = isChangeOrientation;
    }

    /**
     * 当前屏幕朝向是否横屏
     *
     * @param orientation
     * @return
     */
    private boolean screenIsLandscape(int orientation) {
        if (isClockwise) {
            if (orientation >= 75 && orientation <= 344) {
                return true;
            } else {
                return false;
            }
        } else {
            if (orientation >= 16 && orientation <= 285) {
                return true;
            } else {
                return false;
            }
        }
    }


    /**
     * 当前屏幕朝向是否竖屏
     *
     * @param orientation
     * @return
     */
    private boolean screenIsPortrait(int orientation) {
        if (isClockwise) {
            if ((orientation >= 345 && orientation <= 359) || (orientation >= 0 && orientation <= 74)) {
                return true;
            } else {
                return false;
            }
        } else {
            if ((orientation >= 0 && orientation <= 15) || (orientation >= 284 && orientation <= 359)) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 根据朝向来改变屏幕朝向
     *
     * @param isLandscape
     * @param isNeedChangeOrientation 是否需要改变判断值
     */
    private void setOrientation(boolean isLandscape, boolean isNeedChangeOrientation) {
        if (isLandscape) {
            // 切换成竖屏
            if (isNeedChangeOrientation) this.isLandscape = false;
        } else {
            // 切换成横屏
            if (isNeedChangeOrientation) this.isLandscape = true;
        }
    }

    void setOrientation(boolean isLandscape) {
        setOrientation(isLandscape, false);
        this.isLandscape = !this.isLandscape;
    }

    /**
     * 开启监听
     * 绑定切换横竖屏Activity的生命周期
     *
     * @param activity
     */
    void start(Activity activity) {
        mActivity = activity;
        sm.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI);
    }

    /**
     * 注销监听
     */
    void stop() {
        sm.unregisterListener(listener);
        mActivity = null;  // 防止内存泄漏
    }


    /**
     * 当前屏幕的朝向，是否是横屏
     *
     * @return
     */
    boolean isLandscape() {
        return this.isLandscape;
    }

    /**
     * 设置系统横竖屏按钮是否生效，默认无效
     *
     * @param isEffect
     */
    void setEffetSysSetting(boolean isEffect) {
        isEffectSysSetting = isEffect;
    }

    /**
     * 旋转的开关，全屏按钮点击时调用
     */
    void toggleRotate() {

        /**
         * 先判断是否已经开启了重力感应，没开启就直接普通的切换横竖屏
         */
        if (isEffectSysSetting) {
            try {
                int isRotate = Settings.System.getInt(mActivity.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION);

                // 如果用户禁用掉了重力感应就直接切换
                if (isRotate == 0) {
                    setOrientation(isLandscape, true);
                    return;
                }
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

        }

        /**
         * 如果开启了重力i感应就需要修改状态
         */
        isOpenSensor = false;
        isClickFullScreen = true;
        if (isChangeOrientation) {
            setOrientation(isLandscape, false);
        } else {
            setOrientation(isLandscape, false);
        }
        isLandscape = !isLandscape;
    }
}
