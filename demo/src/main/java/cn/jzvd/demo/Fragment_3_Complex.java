package cn.jzvd.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cn.jzvd.demo.Tab_3_Complex.GetGifActivity;
import cn.jzvd.demo.Tab_3_Complex.ListViewActivity;
import cn.jzvd.demo.Tab_3_Complex.TinyWindow.TinyWindowActivity;

/**
 * Created by pengan.li on 2020/5/8.
 */
public class Fragment_3_Complex extends Fragment implements View.OnClickListener {

    private Button mListView, mTinyWindow, mGetGif;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment_complex, null);
        mListView = view.findViewById(R.id.list_view);
        mTinyWindow = view.findViewById(R.id.tiny_window);
        mGetGif = view.findViewById(R.id.get_gif);

        mGetGif.setOnClickListener(this);
        mListView.setOnClickListener(this);
        mTinyWindow.setOnClickListener(this);
        Fragment_3_Complex sss;
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
            case R.id.get_gif:
                startActivity(new Intent(getContext(), GetGifActivity.class));
                break;
        }

    }
}
