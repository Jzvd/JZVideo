package cn.jzvd.demo.BigUIChangeAG;

public class AGEpsodeEntity {
    private String videoUrl;
    private String videoName;

    public AGEpsodeEntity(String videoUrl, String videoName) {
        this.videoUrl = videoUrl;
        this.videoName = videoName;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }
}
