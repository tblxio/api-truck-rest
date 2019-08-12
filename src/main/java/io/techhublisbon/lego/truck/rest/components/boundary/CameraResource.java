package io.techhublisbon.lego.truck.rest.components.boundary;

import io.swagger.annotations.ApiOperation;
import io.techhublisbon.lego.truck.rest.components.entity.ComponentInfoCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CameraResource {

    private ComponentInfoCollection componentInfoCollection;

    @Autowired
    public CameraResource(ComponentInfoCollection componentInfoCollection) {
        this.componentInfoCollection = componentInfoCollection;
    }


    @ApiOperation(value = "Returns the URL of the video stream")
    @GetMapping("/video")
    public ResponseEntity<String> getVideoStreamPath() {

        String streamString = componentInfoCollection.getVideoStream().getVideoStreamURL();
        return ResponseEntity.ok()
                .body(streamString);
    }
}
