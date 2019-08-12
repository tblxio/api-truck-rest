package io.techhublisbon.lego.truck.rest.components.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VideoStream {
    private final Component name = Component.CAMERA;
    private String VideoStreamIp;
    private String VideoStreamPort;

    public VideoStream(String videoStreamIp, String videoStreamPort) {
        VideoStreamIp = videoStreamIp;
        VideoStreamPort = videoStreamPort;
    }

    public String getVideoStreamURL() {
        return "http://" + VideoStreamIp + ":" + VideoStreamPort;
    }
}
