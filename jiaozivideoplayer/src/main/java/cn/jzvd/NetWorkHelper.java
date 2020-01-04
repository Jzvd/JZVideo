package cn.jzvd;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresPermission;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;

public class NetWorkHelper {


    private static final Handler handler = new Handler(Looper.getMainLooper());

    public enum NetworkType {
        NETWORK_ETHERNET,
        NETWORK_WIFI,
        NETWORK_4G,
        NETWORK_3G,
        NETWORK_2G,
        NETWORK_UNKNOWN,
        NETWORK_NO
    }

    /**
     * Return whether using ethernet.
     * <p>Must hold
     * {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />}</p>
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    private static boolean isEthernet(Context context) {
        final ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        final NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
        if (info == null) return false;
        NetworkInfo.State state = info.getState();
        if (null == state) return false;
        return state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING;
    }

    @RequiresPermission(ACCESS_NETWORK_STATE)
    private static NetworkInfo getActiveNetworkInfo(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return null;
        return cm.getActiveNetworkInfo();
    }


    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static NetworkType getNetworkType(Context context) {
        if (isEthernet(context)) {
            return NetworkType.NETWORK_ETHERNET;
        }
        NetworkInfo info = getActiveNetworkInfo(context);
        if (info != null && info.isAvailable()) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                return NetworkType.NETWORK_WIFI;
            } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                switch (info.getSubtype()) {
                    case TelephonyManager.NETWORK_TYPE_GSM:
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        return NetworkType.NETWORK_2G;

                    case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        return NetworkType.NETWORK_3G;

                    case TelephonyManager.NETWORK_TYPE_IWLAN:
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        return NetworkType.NETWORK_4G;

                    default:
                        String subtypeName = info.getSubtypeName();
                        if (subtypeName.equalsIgnoreCase("TD-SCDMA")
                                || subtypeName.equalsIgnoreCase("WCDMA")
                                || subtypeName.equalsIgnoreCase("CDMA2000")) {
                            return NetworkType.NETWORK_3G;
                        } else {
                            return NetworkType.NETWORK_UNKNOWN;
                        }
                }
            } else {
                return NetworkType.NETWORK_UNKNOWN;
            }
        }
        return NetworkType.NETWORK_NO;
    }


    /**
     * Register the status of network changed listener.
     *
     * @param listener The status of network changed listener
     */
    public static void registerNetworkStatusChangedListener(Context context, OnNetworkStatusChangedListener listener) {
        if (listener == null) {
            Log.e("TAG", "registerNetworkStatusChangedListener:  null" );
        }
        NetworkChangedReceiver.getInstance().registerListener(context, listener);
    }

    /**
     * unregister the status of network changed listener.
     *
     * @param listener The status of network changed listener
     */
    public static void unregisterNetworkStatusChangedListener(Context context, OnNetworkStatusChangedListener listener) {
        NetworkChangedReceiver.getInstance().unregisterListener(context, listener);
    }

    public static final class NetworkChangedReceiver extends BroadcastReceiver {

        private static NetworkChangedReceiver getInstance() {
            return LazyHolder.INSTANCE;
        }

        private NetworkType mType;
        private Set<OnNetworkStatusChangedListener> mListeners = new HashSet<>();

        void registerListener(Context context, final OnNetworkStatusChangedListener listener) {
            Log.e("TAG", "run: resiter " );

            if (listener == null) return;
            Log.e("TAG", "run: resiter 3" );

            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.e("TAG", "run: resiter2 " );
                    int preSize = mListeners.size();
                    mListeners.add(listener);
                    if (preSize == 0 && mListeners.size() == 1) {
                        mType = getNetworkType(context);
                        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
                        context.registerReceiver(NetworkChangedReceiver.getInstance(), intentFilter);

                    }
                }
            });
        }

        void unregisterListener(Context context, final OnNetworkStatusChangedListener listener) {
            if (listener == null) return;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    int preSize = mListeners.size();
                    mListeners.remove(listener);
                    if (preSize == 1 && mListeners.size() == 0) {
                        context.unregisterReceiver(NetworkChangedReceiver.getInstance());
                    }
                }
            });

        }


        @Override
        public void onReceive(Context context, Intent intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        NetworkType networkType = getNetworkType(context);
                        if (mType == networkType) return;
                        mType = networkType;
                        if (networkType == NetworkType.NETWORK_NO) {
                            for (OnNetworkStatusChangedListener listener : mListeners) {
                                listener.onDisconnected();
                            }
                        } else {
                            for (OnNetworkStatusChangedListener listener : mListeners) {
                                listener.onConnected(networkType);
                            }
                        }
                    }
                },1000);
            }
        }

        private static class LazyHolder {
            private static final NetworkChangedReceiver INSTANCE = new NetworkChangedReceiver();
        }

    }

    public interface OnNetworkStatusChangedListener {
        void onDisconnected();

        void onConnected(NetWorkHelper.NetworkType networkType);
    }

}

