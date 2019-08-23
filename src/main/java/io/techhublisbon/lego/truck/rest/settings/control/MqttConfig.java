package io.techhublisbon.lego.truck.rest.settings.control;


import io.techhublisbon.lego.truck.rest.settings.entity.SettingKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MqttConfig {

    @Autowired
    SettingManager settingManager;

    public String getMqttHost() {
        return settingManager.getValue(SettingKey.valueOf("MQTT_HOST"));
    }

    public String getMqttPort() {
        return settingManager.getValue(SettingKey.valueOf("MQTT_PORT"));
    }

    public String getMqttUser() {
        return settingManager.getValue(SettingKey.valueOf("MQTT_USER"));
    }

    public String getMqttPass() {
        return settingManager.getValue(SettingKey.valueOf("MQTT_PASS"));
    }

}
