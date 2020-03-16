package cn.jzvd.demo.BigUIChangeAG;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.jzvd.demo.R;
import cn.jzvd.demo.utils.DpOrPxUtils;

public class VideoSpeedPopup extends PopupWindow implements View.OnClickListener {
    private TextView speedOne,speedTwo,speedThree,speedFour,speedFive;
    private SpeedChangeListener speedChangeListener;
    private Context mC;
    private LayoutInflater inflater;
    private View contentView;
   public VideoSpeedPopup(Context context){
        super(context);
       mC=context;
       inflater=(LayoutInflater)mC.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       contentView=inflater.inflate(R.layout.popup_video_speed,null);
       setContentView(contentView);
       setHeight(WindowManager.LayoutParams.MATCH_PARENT);
       setWidth(DpOrPxUtils.dip2px(context,200));
       speedOne=contentView.findViewById(R.id.pop_speed_1);
       speedTwo=contentView.findViewById(R.id.pop_speed_1_25);
       speedThree=contentView.findViewById(R.id.pop_speed_1_5);
       speedFour=contentView.findViewById(R.id.pop_speed_1_75);
       speedFive=contentView.findViewById(R.id.pop_speed_2);
       setOutsideTouchable(true);
       //不设置该属性，弹窗于屏幕边框会有缝隙并且背景不是半透明
       setBackgroundDrawable(new BitmapDrawable());
       speedOne.setOnClickListener(this);
       speedTwo.setOnClickListener(this);
       speedThree.setOnClickListener(this);
       speedFour.setOnClickListener(this);
       speedFive.setOnClickListener(this);
   }

    @Override
    public void onClick(View v) {
        if (speedChangeListener!=null){
            switch (v.getId()){
                case R.id.pop_speed_1:
                    speedOne.setTextColor(mC.getResources().getColor(R.color.ThemeColor));
                    speedTwo.setTextColor(mC.getResources().getColor(R.color.colorWhite));
                    speedThree.setTextColor(mC.getResources().getColor(R.color.colorWhite));
                    speedFour.setTextColor(mC.getResources().getColor(R.color.colorWhite));
                    speedFive.setTextColor(mC.getResources().getColor(R.color.colorWhite));
                    speedChangeListener.speedChange(1f);
                    break;
                case R.id.pop_speed_1_25:
                    speedOne.setTextColor(mC.getResources().getColor(R.color.colorWhite));
                    speedTwo.setTextColor(mC.getResources().getColor(R.color.ThemeColor));
                    speedThree.setTextColor(mC.getResources().getColor(R.color.colorWhite));
                    speedFour.setTextColor(mC.getResources().getColor(R.color.colorWhite));
                    speedFive.setTextColor(mC.getResources().getColor(R.color.colorWhite));
                    speedChangeListener.speedChange(1.25f);
                    break;
                case R.id.pop_speed_1_5:
                    speedOne.setTextColor(mC.getResources().getColor(R.color.colorWhite));
                    speedTwo.setTextColor(mC.getResources().getColor(R.color.colorWhite));
                    speedThree.setTextColor(mC.getResources().getColor(R.color.ThemeColor));
                    speedFour.setTextColor(mC.getResources().getColor(R.color.colorWhite));
                    speedFive.setTextColor(mC.getResources().getColor(R.color.colorWhite));
                    speedChangeListener.speedChange(1.5f);
                    break;
                case R.id.pop_speed_1_75:
                    speedOne.setTextColor(mC.getResources().getColor(R.color.colorWhite));
                    speedTwo.setTextColor(mC.getResources().getColor(R.color.colorWhite));
                    speedThree.setTextColor(mC.getResources().getColor(R.color.colorWhite));
                    speedFour.setTextColor(mC.getResources().getColor(R.color.ThemeColor));
                    speedFive.setTextColor(mC.getResources().getColor(R.color.colorWhite));
                    speedChangeListener.speedChange(1.75f);
                    break;
                case R.id.pop_speed_2:
                    speedOne.setTextColor(mC.getResources().getColor(R.color.colorWhite));
                    speedTwo.setTextColor(mC.getResources().getColor(R.color.colorWhite));
                    speedThree.setTextColor(mC.getResources().getColor(R.color.colorWhite));
                    speedFour.setTextColor(mC.getResources().getColor(R.color.colorWhite));
                    speedFive.setTextColor(mC.getResources().getColor(R.color.ThemeColor));
                    speedChangeListener.speedChange(2f);
                    break;
            }
            dismiss();
        }
    }

    public SpeedChangeListener getSpeedChangeListener() {
        return speedChangeListener;
    }

    public void setSpeedChangeListener(SpeedChangeListener speedChangeListener) {
        this.speedChangeListener = speedChangeListener;
    }

    public interface SpeedChangeListener{
        void speedChange(float speed);
    }
}
