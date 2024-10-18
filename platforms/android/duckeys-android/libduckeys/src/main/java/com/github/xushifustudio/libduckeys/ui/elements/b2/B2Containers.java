package com.github.xushifustudio.libduckeys.ui.elements.b2;


import com.github.xushifustudio.libduckeys.ui.box2.B2Container;
import com.github.xushifustudio.libduckeys.ui.box2.B2View;
import com.github.xushifustudio.libduckeys.ui.layouts.B2GridLayout;
import com.github.xushifustudio.libduckeys.ui.layouts.B2LinearLayout;

public class B2Containers extends B2View {

    public static B2Container createWithGridLayout(int col, int row) {
        B2GridLayout layout = B2GridLayout.newInstance(col, row);
        B2Container con = new B2Container();
        con.setLayout(layout);
        return con;
    }

    public static B2Container createWithLinearLayout(B2LinearLayout.Direction dir) {
        B2LinearLayout layout = B2LinearLayout.newInstance(dir);
        B2Container con = new B2Container();
        con.setLayout(layout);
        return con;
    }

}
