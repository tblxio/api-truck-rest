package io.techhublisbon.lego.truck.rest.settings.control;

import io.techhublisbon.lego.truck.rest.settings.entity.SettingKey;
import org.springframework.stereotype.Component;

@Component
public class SettingManager {

    public String getValue(final SettingKey key) {
        final String value = System.getenv(key.getKey());
        return value != null ? value : key.getDefaultValue();
    }
}
