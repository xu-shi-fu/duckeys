package com.github.xushifustudio.libduckeys.settings;

import java.util.List;

public interface SettingBank {

    int scope();

    String getBankName(SettingContext ctx);

}
