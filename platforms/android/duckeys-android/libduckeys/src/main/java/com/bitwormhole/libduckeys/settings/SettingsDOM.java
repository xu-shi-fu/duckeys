package com.bitwormhole.libduckeys.settings;

import java.util.ArrayList;
import java.util.List;

public class SettingGroup extends SettingNode {

    public SettingGroup(SettingContext ctx, String aName, SettingNode aParent) {
        super(ctx, aName, aParent);
    }

    public List<SettingNode> children() {
        return new ArrayList<>();
    }

}
