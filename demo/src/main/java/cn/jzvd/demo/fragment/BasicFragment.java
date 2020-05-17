package cn.jzvd.demo.fragment;

import android.view.View;

import com.bumptech.glide.Glide;

import cn.jzvd.demo.CustomJzvd.MyJzvdStd;
import cn.jzvd.demo.R;

/**
 * Created by pengan.li on 2020/5/8.
 */
public class BasicFragment extends BaseFragment{

    private MyJzvdStd myJzvdStd;

    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.fragment_basic, null);
        myJzvdStd = view.findViewById(R.id.jz_video);
        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        myJzvdStd.setUp("http://jzvd.nathen.cn/342a5f7ef6124a4a8faf00e738b8bee4/cf6d9db0bd4d41f59d09ea0a81e918fd-5287d2089db37e62345123a1be272f8b.mp4"
                , "饺子快长大");
        Glide.with(this).load("http://jzvd-pic.nathen.cn/jzvd-pic/1bb2ebbe-140d-4e2e-abd2-9e7e564f71ac.png").into(myJzvdStd.posterImageView);

    }
}
