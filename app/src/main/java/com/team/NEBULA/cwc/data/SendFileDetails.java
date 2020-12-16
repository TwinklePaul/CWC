package com.team.NEBULA.cwc.data;

public class SendFileDetails {

    private String lati;
    private String longti;
    private String imageUrl;
    private String humanReadableTime;
    private String timeStamp;

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }



    public SendFileDetails(){}

    public SendFileDetails(String lati, String longti, String imageUrl, String humanReadableTime,String timeStamp){
        this.lati = lati;
        this.longti = longti;
        this.imageUrl=imageUrl;
        this.timeStamp = timeStamp;
        this.humanReadableTime=humanReadableTime;
    }

    public String getLongti() {
        return longti;
    }

    public void setLongti(String longti) {
        this.longti = longti;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public String getHumanReadableTime() {
        return humanReadableTime;
    }

    public void setHumanReadableTime(String humanReadableTime) {
        this.humanReadableTime = humanReadableTime;
    }



    public String getLati() {
        return lati;
    }

    public void setLati(String lati) {
        this.lati = lati;
    }




}
