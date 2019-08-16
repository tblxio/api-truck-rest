package io.techhublisbon.lego.truck.rest.components.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VideoStream {
    private static final Component name = Component.CAMERA;
    private String videoStreamIp;
    private String videoStreamPort;

    public VideoStream(String videoStreamIp, String videoStreamPort) {
        this.videoStreamIp = videoStreamIp;
        this.videoStreamPort = videoStreamPort;
    }

    public String getVideoStreamURL() {
        return "http://" + videoStreamIp + ":" + videoStreamPort;
    }
}
