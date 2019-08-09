package io.techhublisbon.lego.truck.rest.motor.control.boundary;

import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Controller
public class JoystickResource {

    @ApiOperation(value = "Returns an HTML page containing a joystick to test the system")
    @GetMapping("/joystick")
    public String joy(Model model) throws UnknownHostException {
        String ip = InetAddress.getLocalHost().getHostAddress();
        model.addAttribute("message", ip);
        return "controller";
    }
}
