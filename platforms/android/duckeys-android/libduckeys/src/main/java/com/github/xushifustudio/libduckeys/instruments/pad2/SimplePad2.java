package com.github.xushifustudio.libduckeys.instruments.pad2;


import android.content.Context;
import android.view.SurfaceView;

import com.github.xushifustudio.libduckeys.context.LifeManager;
import com.github.xushifustudio.libduckeys.instruments.InstrumentContext;

public class SimplePad2 {

    public static InstrumentContext create(Context ctx, SurfaceView view, LifeManager lm) {
        InstrumentContext ic = InstrumentContext.createNewInstance(ctx, view, lm);
        ic.setView2(new SP2View());
        return ic;
    }

}
