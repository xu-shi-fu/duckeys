package com.github.xushifustudio.libduckeys.ui.styles;

import com.github.xushifustudio.libduckeys.ui.box2.B2Align;
import com.github.xushifustudio.libduckeys.ui.box2.B2PropertyQuery;
import com.github.xushifustudio.libduckeys.ui.box2.B2State;
import com.github.xushifustudio.libduckeys.ui.box2.B2Style;
import com.github.xushifustudio.libduckeys.ui.box2.B2Property;

public class B2StyleReader {

    private final B2Style style;
    private B2State state;

    public B2StyleReader(B2Style s) {
        this.style = s;
    }


    public B2Style getStyle() {
        return style;
    }

    public B2State getState() {
        return state;
    }

    public void setState(B2State state) {
        this.state = state;
    }

    private B2PropertyQuery createNewQuery(String name, String[] names) {
        B2PropertyQuery q = new B2PropertyQuery();
        if (name != null) {
            q.names.add(name);
        }
        if (names != null) {
            for (String item : names) {
                if (item == null) {
                    continue;
                }
                q.names.add(item);
            }
        }
        return q;
    }


    public float readSize(String name) {
        return innerReadSize(name, null);
    }

    public float readSize(String[] names) {
        return innerReadSize(null, names);
    }

    public int readInt(String name) {
        IntegerProperty p = new IntegerProperty(0);
        B2PropertyQuery q = this.createNewQuery(name, null);
        q.property = p;
        q.state = this.state;
        this.style.query(q);
        if (q.ok) {
            p = (IntegerProperty) q.property;
            return p.getValue();
        }
        return 0;
    }


    public int readColor(String name) {
        return innerReadColor(name, null);
    }

    public int readColor(String[] names) {
        return innerReadColor(null, names);
    }

    public B2Align readAlign(String name) {
        return innerReadAlign(name, null);
    }

    public B2Align readAlign(String[] names) {
        return innerReadAlign(null, names);
    }

    // private ///////////////////////////////////

    private float innerReadSize(String name, String[] names) {
        SizeProperty p = new SizeProperty(0);
        B2PropertyQuery q = this.createNewQuery(name, names);
        q.property = p;
        q.state = this.state;
        this.style.query(q);
        if (q.ok) {
            p = (SizeProperty) q.property;
            return p.getSize();
        }
        return 0;
    }

    private int innerReadColor(String name, String[] names) {
        ColorProperty p = new ColorProperty(0);
        B2PropertyQuery q = this.createNewQuery(name, names);
        q.property = p;
        q.state = this.state;
        this.style.query(q);
        if (q.ok) {
            p = (ColorProperty) q.property;
            return p.getColor();
        }
        return 0;
    }

    private B2Align innerReadAlign(String name, String[] names) {
        final B2Align def = B2Align.LEFT;
        AlignProperty p = new AlignProperty(def);
        B2PropertyQuery q = this.createNewQuery(name, names);
        q.property = p;
        q.state = this.state;
        this.style.query(q);
        if (q.ok) {
            p = (AlignProperty) q.property;
            return p.getAlign();
        }
        return def;
    }
}
