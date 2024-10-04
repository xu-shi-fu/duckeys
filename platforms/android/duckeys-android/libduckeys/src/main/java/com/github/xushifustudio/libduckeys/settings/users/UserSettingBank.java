package com.github.xushifustudio.libduckeys.settings.users;

import com.github.xushifustudio.libduckeys.settings.SettingBankNamer;
import com.github.xushifustudio.libduckeys.settings.SettingContext;
import com.github.xushifustudio.libduckeys.settings.SettingBank;
import com.github.xushifustudio.libduckeys.settings.SettingScope;
import com.github.xushifustudio.libduckeys.settings.apps.CurrentUserSettings;

public class UserSettingBank implements SettingBank {

    private String name; // the user.name
    private String email; // the user.email


    @Override
    public int scope() {
        return SettingScope.USER;
    }

    @Override
    public String getBankName(SettingContext ctx) {
        CurrentUserSettings user = ctx.current;
        if (user == null) {
            user = new CurrentUserSettings();
        }
        SettingBankNamer namer = new SettingBankNamer();
        namer.setPrefix("user");
        namer.addParam("duckeys/settings/users");
        namer.addParam(user.getEmail());
        return namer.name();
    }
}
