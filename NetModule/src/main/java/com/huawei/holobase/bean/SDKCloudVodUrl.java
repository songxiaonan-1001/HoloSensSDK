package com.huawei.holobase.bean;



public class SDKCloudVodUrl {

    /**
     * {
     *     "device_id":"101590000020",
     *     "channel_id":"0",
     *     "playback_url":"jvmp://114.116.207.112:7231/vod/101590000020?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJjaGFubmVsX2lkIjoiMCIsImRldmljZV9pZCI6IjEwMTU5MDAwMDAyMCIsImVuZF90aW1lIjoiMjAyMC0wOC0xMCAyMzo1OTo1OSIsImV4cGlyZV90aW1lIjoiMjAyMC0wOC0xMFQxOToxNDozMCswODowMCIsInN0YXJ0X3RpbWUiOiIyMDIwLTA4LTEwIDAwOjAwOjAwIiwidXNlcl9pZCI6IjEyNDE2MDgyNTcyMDIwMDgwNzIxMTcwNyJ9.nF2tsOhdYiDNYvPKFXHWTZCdT1zI30lwGcrDT3ShLIY"
     * }
     */


    private String device_id;
    private String channel_id;
    private String playback_url;

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getPlayback_url() {
        return playback_url;
    }

    public void setPlayback_url(String playback_url) {
        this.playback_url = playback_url;
    }
}
