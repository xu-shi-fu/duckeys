package com.github.xushifustudio.libduckeys.ui.box2;

import com.github.xushifustudio.libduckeys.ui.styles.AlignProperty;
import com.github.xushifustudio.libduckeys.ui.styles.BaseStyle;
import com.github.xushifustudio.libduckeys.ui.styles.ColorProperty;
import com.github.xushifustudio.libduckeys.ui.styles.SizeProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class B2StyleBuilder {

    private B2StyleNode parent;
    private B2State state;
    private final List<B2PropertyHolder> list;

    public B2StyleBuilder() {
        this.list = new ArrayList<>();
    }

    public void setParent(B2StyleNode parent) {
        this.parent = parent;
    }

    public B2State getState() {
        return state;
    }


    public B2StyleNode getParent() {
        return parent;
    }


    public void setState(B2State state) {
        this.state = state;
    }

    public void setParent(B2Style parent) {
        this.parent = BaseStyle.wrap(parent);
    }


    public B2StyleNode create() {
        BaseStyle result = new BaseStyle(this.parent);
        list.forEach((holder) -> {
            result.put(holder);
        });
        return result;
    }

    public B2StyleBuilder putAlign(String name, B2Align align) {
        AlignProperty p = new AlignProperty(align);
        B2PropertyHolder holder = new B2PropertyHolder();
        holder.name = name;
        holder.property = p;
        holder.handler = p.getHandler();
        holder.state = state;
        list.add(holder);
        return this;
    }


    public B2StyleBuilder putColor(String name, int color) {
        ColorProperty p = new ColorProperty(color);
        B2PropertyHolder holder = new B2PropertyHolder();
        holder.name = name;
        holder.property = p;
        holder.handler = p.getHandler();
        holder.state = state;
        list.add(holder);
        return this;
    }

    public B2StyleBuilder putSize(String name, float size) {
        SizeProperty p = new SizeProperty(size);
        B2PropertyHolder holder = new B2PropertyHolder();
        holder.name = name;
        holder.property = p;
        holder.handler = p.getHandler();
        holder.state = state;
        list.add(holder);
        return this;
    }

}
