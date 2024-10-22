package com.github.xushifustudio.libduckeys.instruments.pad2;


import android.content.Context;
import android.view.SurfaceView;

import com.github.xushifustudio.libduckeys.context.LifeManager;
import com.github.xushifustudio.libduckeys.instruments.InstrumentContext;
import com.github.xushifustudio.libduckeys.ui.box2.B2View;
import com.github.xushifustudio.libduckeys.ui.elements.b2.B2DebugLayerView;

public class SimplePad2 {

    public static InstrumentContext create(Context ctx, SurfaceView view, LifeManager lm) {

        //     B2View v2 = new SP2View();
        // B2DebugLayerView v_debug = B2DebugLayerView.wrap(v2);
        // v2 = v_debug;
        //  v_debug.setDisplayFPS(true);
        // v_debug.setDisplayTouchAtGrid(true);


        PadContext ic = new PadContext();
        ic.init(ctx, view, lm);

        Pad4x4View v2 = new Pad4x4View(ic);
        ic.setView2(v2);

        return ic;
    }

}
