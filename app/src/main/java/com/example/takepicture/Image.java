package com.example.takepicture;

public class Image {
    String status;
    String dehazed1url;
    String dehazed2url;

    public Image() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDehazed1url() {
        return dehazed1url;
    }

    public void setDehazed1url(String dehazed1url) {
        this.dehazed1url = dehazed1url;
    }

    public String getDehazed2url() {
        return dehazed2url;
    }

    public void setDehazed2url(String dehazed2url) {
        this.dehazed2url = dehazed2url;
    }
}
