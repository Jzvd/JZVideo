package cn.jzvd.demo.utils;

import android.os.Parcel;
import android.os.Parcelable;


public class ViewAttr implements Parcelable {
    public static final Creator<ViewAttr> CREATOR = new Creator<ViewAttr>() {
        @Override
        public ViewAttr createFromParcel(Parcel in) {
            return new ViewAttr(in);
        }

        @Override
        public ViewAttr[] newArray(int size) {
            return new ViewAttr[size];
        }
    };
    private int x;
    private int y;
    private int width;
    private int height;

    public ViewAttr() {
    }

    protected ViewAttr(Parcel in) {
        x = in.readInt();
        y = in.readInt();
        width = in.readInt();
        height = in.readInt();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(x);
        dest.writeInt(y);
        dest.writeInt(width);
        dest.writeInt(height);
    }
}
