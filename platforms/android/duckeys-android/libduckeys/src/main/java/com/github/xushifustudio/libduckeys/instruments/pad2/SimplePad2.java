package com.github.xushifustudio.libduckeys.instruments.pad2;


import android.content.Context;
import android.view.SurfaceView;

import com.github.xushifustudio.libduckeys.context.LifeManager;
import com.github.xushifustudio.libduckeys.instruments.InstrumentContext;
import com.github.xushifustudio.libduckeys.ui.box2.B2View;
import com.github.xushifustudio.libduckeys.ui.elements.b2.B2DebugLayerView;

public class SimplePad2 {

    public static InstrumentContext create(Context ctx, SurfaceView view, LifeManager lm) {

        PadContext ic = new PadContext();
        ic.init(ctx, view, lm);

        SimplePadView view2 = new SimplePadView(ic);
        ic.setView2(view2);

        return ic;
    }

}
