package cn.jzvd.demo.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import cn.jzvd.demo.ListViewActivity;
import cn.jzvd.demo.R;
import cn.jzvd.demo.TinyWindow.TinyWindowActivity;
import cn.jzvd.demo.api.BigUIChangeAG.UiBigChangeAGActivity;

/**
 * Created by pengan.li on 2020/5/8.
 */
public class ComplexFragment extends BaseFragment implements View.OnClickListener{

    private Button mListView,mTinyWindow,mCustomAgVideo;

    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.fragment_complex, null);
        mListView = view.findViewById(R.id.list_view);
        mTinyWindow = view.findViewById(R.id.tiny_window);
        mCustomAgVideo = view.findViewById(R.id.custom_ag_video);
        mListView.setOnClickListener(this);
        mTinyWindow.setOnClickListener(this);
        mCustomAgVideo.setOnClickListener(this);
        return view;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.list_view:
                startActivity(new Intent(mContext, ListViewActivity.class));
                break;
            case R.id.tiny_window:
                startActivity(new Intent(mContext, TinyWindowActivity.class));
                break;
            case R.id.custom_ag_video:
                startActivity(new Intent(mContext, UiBigChangeAGActivity.class));
                break;
        }

    }
}
