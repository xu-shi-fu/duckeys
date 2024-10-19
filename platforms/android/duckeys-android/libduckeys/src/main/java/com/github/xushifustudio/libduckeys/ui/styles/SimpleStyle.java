package com.github.xushifustudio.libduckeys.ui.styles;

import com.github.xushifustudio.libduckeys.ui.box2.B2PropertyHolder;
import com.github.xushifustudio.libduckeys.ui.box2.B2PropertyQuery;
import com.github.xushifustudio.libduckeys.ui.box2.B2State;
import com.github.xushifustudio.libduckeys.ui.box2.B2Style;
import com.github.xushifustudio.libduckeys.ui.box2.B2Property;

import java.util.HashMap;
import java.util.Map;

public class SimpleStyle implements B2Style {

    private final Map<String, B2PropertyHolder> table;
    private long mRevision;
    private long mUpdatedAt;


    public SimpleStyle() {
        this.table = new HashMap<>();
    }


    private String keyFor(B2PropertyHolder holder) {
        String name = holder.name;
        B2State stat = holder.state;
        if (stat == null) {
            stat = B2State.NORMAL;
        }
        return name + ":" + stat;
    }


    @Override
    public Map<String, B2PropertyHolder> fetchAll(Map<String, B2PropertyHolder> dest) {
        if (dest == null) {
            dest = new HashMap<>();
        }
        dest.putAll(table);
        return dest;
    }

    @Override
    public B2PropertyHolder get(String name) {
        return doInnerGet(name, null);
    }

    @Override
    public B2PropertyHolder get(String name, B2State state) {
        return doInnerGet(name, state);
    }

    private B2PropertyHolder doInnerGet(String name, B2State state) {
        B2PropertyHolder h1 = new B2PropertyHolder();
        h1.state = state;
        h1.name = name;
        String key = keyFor(h1);
        h1 = table.get(key);
        if (h1 == null) {
            return null;
        }
        return new B2PropertyHolder(h1);
    }

    @Override
    public void put(B2PropertyHolder h1) {
        B2PropertyHolder h2 = new B2PropertyHolder(h1);
        String key = keyFor(h2);
        table.put(key, h2);
    }

    @Override
    public long revision() {
        return (mRevision + mUpdatedAt);
    }

    @Override
    public long update() {
        mRevision++;
        mUpdatedAt = System.currentTimeMillis();
        return this.revision();
    }


    @Override
    public boolean query(B2PropertyQuery q) {
        QFinder qf = new QFinder(this);
        return qf.query(q);
    }
}
