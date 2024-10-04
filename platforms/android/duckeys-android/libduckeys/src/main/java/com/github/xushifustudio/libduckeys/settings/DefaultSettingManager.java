package com.github.xushifustudio.libduckeys.settings;

import com.github.xushifustudio.libduckeys.context.ComponentContext;
import com.github.xushifustudio.libduckeys.context.ComponentLife;
import com.github.xushifustudio.libduckeys.context.ComponentRegistration;
import com.github.xushifustudio.libduckeys.context.LifeProxy;

import java.util.ArrayList;
import java.util.List;

public class DefaultSettingManager implements SettingManager, ComponentLife {

    private final SettingContext mSettingContext;

    public DefaultSettingManager() {
        mSettingContext = new SettingContext();
    }

    @Override
    public SettingSession openSession() {
        DefaultSettingsSession session = new DefaultSettingsSession(mSettingContext);
        session.open();
        return session;
    }

    private void scanItems(ComponentContext cc) {
        List<Object> src = cc.components.listAll();
        List<SettingBank> banks = new ArrayList<>();
        List<SettingProperty> props = new ArrayList<>();
        for (Object item : src) {
            if (item instanceof SettingProperty) {
                SettingProperty sp = (SettingProperty) item;
                props.add(sp);
            } else if (item instanceof SettingBank) {
                SettingBank bank = (SettingBank) item;
                banks.add(bank);
            }
        }
        mSettingContext.context = cc.context;
        mSettingContext.setProperties(props);
        mSettingContext.setBanks(banks);
    }


    @Override
    public ComponentRegistration init(ComponentContext cc) {

        LifeProxy lp = new LifeProxy();
        lp.setOnCreate(() -> {
            scanItems(cc);
        });

        ComponentRegistration cr = new ComponentRegistration();
        cr.instance = this;
        cr.life = lp;
        return cr;
    }
}
