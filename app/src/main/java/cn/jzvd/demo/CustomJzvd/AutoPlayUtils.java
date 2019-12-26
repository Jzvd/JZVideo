package cn.jzvd.demo.CustomJzvd;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import cn.jzvd.JZUtils;
import cn.jzvd.Jzvd;
import cn.jzvd.demo.R;

public class AutoPlayUtils {
    public static int positionInList = -1;//记录当前播放列表位置

    private AutoPlayUtils() {
    }

    /**
     * @param firstVisiblePosition
     * @param lastVisiblePosition
     */
    public static void onScrollPlayVideo(RecyclerView recyclerView, int firstVisiblePosition, int lastVisiblePosition) {
        if (JZUtils.isWifiConnected(recyclerView.getContext())) {
            for (int i = 0; i <= lastVisiblePosition - firstVisiblePosition; i++) {
                View child = recyclerView.getChildAt(i);
                View view = child.findViewById(R.id.jzvdplayer);
                if (view != null && view instanceof JzvdStdRv) {
                    JzvdStdRv player = (JzvdStdRv) view;
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
     * @param firstVisiblePosition
     * @param lastVisiblePosition
     * @param percent
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
