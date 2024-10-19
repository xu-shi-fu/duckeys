package com.github.xushifustudio.libduckeys.ui.styles;

import com.github.xushifustudio.libduckeys.ui.box2.B2PropertyHolder;
import com.github.xushifustudio.libduckeys.ui.box2.B2PropertyQuery;
import com.github.xushifustudio.libduckeys.ui.box2.B2State;
import com.github.xushifustudio.libduckeys.ui.box2.B2Style;
import com.github.xushifustudio.libduckeys.ui.box2.B2StyleNode;
import com.github.xushifustudio.libduckeys.ui.box2.B2Property;

import java.util.Map;

public class BaseStyle implements B2Style, B2StyleNode {

    private final B2StyleNode mParent;
    private final B2Style mMyself;

    public BaseStyle() {
        mParent = null;
        mMyself = new SimpleStyle();
    }

    public BaseStyle(B2StyleNode parent) {
        mParent = parent;
        mMyself = new SimpleStyle();
    }

    public BaseStyle(B2StyleNode parent, B2Style simple) {
        if (simple == null) {
            simple = new SimpleStyle();
        }
        mParent = parent;
        mMyself = simple;
    }


    public static B2StyleNode wrap(B2Style style) {
        if (style instanceof B2StyleNode) {
            return (B2StyleNode) style;
        }
        return new BaseStyle(null, style);
    }


    @Override
    public B2StyleNode parent() {
        return mParent;
    }

    @Override
    public B2Style self() {
        return mMyself;
    }

    @Override
    public long revision() {
        long rev = this.mMyself.revision();
        final B2StyleNode parent = mParent;
        if (parent != null) {
            rev += parent.revision();
        }
        return rev;
    }

    @Override
    public long update() {
        this.mMyself.update();
        return this.revision();
    }

    @Override
    public boolean query(B2PropertyQuery q) {
        QFinder qf = new QFinder(this);
        return qf.query(q);
    }

    @Override
    public Map<String, B2PropertyHolder> fetchAll(Map<String, B2PropertyHolder> dest) {
        B2StyleNode parent = mParent;
        if (parent != null) {
            dest = parent.fetchAll(dest);
        }
        return mMyself.fetchAll(dest);
    }

    private B2PropertyHolder doInnerGet(String name, B2State state) {
        B2PropertyHolder h1 = mMyself.get(name, state);
        if (h1 != null) {
            return h1;
        }
        B2StyleNode parent = mParent;
        if (parent != null) {
            return parent.get(name, state);
        }
        return null;
    }

    @Override
    public B2PropertyHolder get(String name) {
        return doInnerGet(name, null);
    }

    @Override
    public B2PropertyHolder get(String name, B2State state) {
        return doInnerGet(name, state);
    }

    @Override
    public void put(B2PropertyHolder h) {
        mMyself.put(h);
    }
}
