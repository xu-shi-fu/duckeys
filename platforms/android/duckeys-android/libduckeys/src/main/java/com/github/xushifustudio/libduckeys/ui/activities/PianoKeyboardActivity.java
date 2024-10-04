package com.github.xushifustudio.libduckeys.ui.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.SurfaceView;

import androidx.annotation.Nullable;

import com.github.xushifustudio.libduckeys.context.LifeActivity;
import com.github.xushifustudio.libduckeys.R;
import com.github.xushifustudio.libduckeys.context.LifeManager;
import com.github.xushifustudio.libduckeys.ui.boxes.Viewport;
import com.github.xushifustudio.libduckeys.ui.elements.piano.PianoKeyboard;
import com.github.xushifustudio.libduckeys.ui.elements.piano.PianoScrollingOverview;
import com.github.xushifustudio.libduckeys.ui.views.SurfaceViewController;

public class PianoKeyboardActivity extends LifeActivity {

    private SurfaceView mSurfaceView1;
    private SurfaceView mSurfaceView2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        SurfaceViewController surfaceVC1 = new SurfaceViewController(this);
        SurfaceViewController surfaceVC2 = new SurfaceViewController(this);

        LifeManager lifeMan = this.getLifeManager();
        lifeMan.add(surfaceVC1);
        lifeMan.add(surfaceVC2);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_piano_keyboard);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mSurfaceView1 = findViewById(R.id.surface_view_small);
        mSurfaceView2 = findViewById(R.id.surface_view_large);

        Viewport vpt1 = makeViewport1();
        Viewport vpt2 = makeViewport2();

        surfaceVC1.init(mSurfaceView1, vpt1);
        surfaceVC2.init(mSurfaceView2, vpt2);
    }

    private Viewport makeViewport1() {
        Viewport vpt = new Viewport();
        PianoScrollingOverview view = new PianoScrollingOverview();
        vpt.root = view;
        return vpt;
    }

    private Viewport makeViewport2() {
        Viewport vpt = new Viewport();
        PianoKeyboard kbd = new PianoKeyboard();
        vpt.root = kbd;
        kbd.countKeysInView = 37;
        return vpt;
    }
}
