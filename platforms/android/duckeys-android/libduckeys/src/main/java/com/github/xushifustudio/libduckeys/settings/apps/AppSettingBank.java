package com.github.xushifustudio.libduckeys.settings.apps;

import com.github.xushifustudio.libduckeys.settings.SettingBankNamer;
import com.github.xushifustudio.libduckeys.settings.SettingContext;
import com.github.xushifustudio.libduckeys.settings.SettingBank;
import com.github.xushifustudio.libduckeys.settings.SettingScope;

public class AppSettingBank implements SettingBank {

    @Override
    public int scope() {
        return SettingScope.APP;
    }

    @Override
    public String getBankName(SettingContext sc) {
        SettingBankNamer namer = new SettingBankNamer();
        namer.setPrefix("app");
        namer.addParam("duckeys/settings/app");
        return namer.name();
    }

}
