package cn.jzvd.demo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cn.jzvd.demo.ListViewActivity;
import cn.jzvd.demo.R;
import cn.jzvd.demo.TinyWindow.TinyWindowActivity;
import cn.jzvd.demo.api.BigUIChangeAG.UiBigChangeAGActivity;

/**
 * Created by pengan.li on 2020/5/8.
 */
public class ComplexFragment extends Fragment implements View.OnClickListener{

    private Button mListView,mTinyWindow,mCustomAgVideo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment_complex, null);
        mListView = view.findViewById(R.id.list_view);
        mTinyWindow = view.findViewById(R.id.tiny_window);
        mCustomAgVideo = view.findViewById(R.id.custom_ag_video);
        mListView.setOnClickListener(this);
        mTinyWindow.setOnClickListener(this);
        mCustomAgVideo.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.list_view:
                startActivity(new Intent(getContext(), ListViewActivity.class));
                break;
            case R.id.tiny_window:
                startActivity(new Intent(getContext(), TinyWindowActivity.class));
                break;
            case R.id.custom_ag_video:
                startActivity(new Intent(getContext(), UiBigChangeAGActivity.class));
                break;
        }

    }
}
