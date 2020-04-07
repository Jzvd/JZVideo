package cn.jzvd.demo.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import cn.jzvd.JZUtils;
import cn.jzvd.Jzvd;

/**
 * 列表自动播放工具类
 *
 * @author Liberations
 */
public class AutoPlayUtils {
    public static int positionInList = -1;//记录当前播放列表位置

    private AutoPlayUtils() {
    }

    /**
     * @param firstVisiblePosition 首个可见item位置
     * @param lastVisiblePosition  最后一个可见item位置
     */
    public static void onScrollPlayVideo(RecyclerView recyclerView, int jzvdId, int firstVisiblePosition, int lastVisiblePosition) {
        if (JZUtils.isWifiConnected(recyclerView.getContext())) {
            for (int i = 0; i <= lastVisiblePosition - firstVisiblePosition; i++) {
                View child = recyclerView.getChildAt(i);
                View view = child.findViewById(jzvdId);
                if (view != null && view instanceof Jzvd) {
                    Jzvd player = (Jzvd) view;
                    if (getViewVisiblePercent(player) == 1f) {
                        if (positionInList != i + firstVisiblePosition) {
                            player.startButton.performClick();
                        }
                        break;
                    }
                }
            }
        }
    }

    /**
     * @param firstVisiblePosition 首个可见item位置
     * @param lastVisiblePosition  最后一个可见item位置
     * @param percent              当item被遮挡percent/1时释放,percent取值0-1
     */
    public static void onScrollReleaseAllVideos(int firstVisiblePosition, int lastVisiblePosition, float percent) {
        if (Jzvd.CURRENT_JZVD == null) return;
        if (positionInList >= 0) {
            if ((positionInList <= firstVisiblePosition || positionInList >= lastVisiblePosition - 1)) {
                if (getViewVisiblePercent(Jzvd.CURRENT_JZVD) < percent) {
                    Jzvd.releaseAllVideos();
                }
            }
        }
    }

    /**
     * @param view
     * @return 当前视图可见比列
     */
    public static float getViewVisiblePercent(View view) {
        if (view == null) {
            return 0f;
        }
        float height = view.getHeight();
        Rect rect = new Rect();
        if (!view.getLocalVisibleRect(rect)) {
            return 0f;
        }
        float visibleHeight = rect.bottom - rect.top;
        return visibleHeight / height;
    }
}
