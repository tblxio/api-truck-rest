package io.techhublisbon.lego.truck.rest.settings.entity;

public enum SettingKey {
    MQTT_HOST("MQTT_HOST"), MQTT_PORT("MQTT_PORT"), MQTT_USER("MQTT_USER"), MQTT_PASS("MQTT_PASS");

    private final String key;
    private final String defaultValue;

    SettingKey(final String key) {
        this.key = key;
        this.defaultValue = null;
    }

    public String getKey() {
        return key;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
