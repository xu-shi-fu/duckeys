package com.github.xushifustudio.libduckeys.instruments;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.github.xushifustudio.libduckeys.context.Life;
import com.github.xushifustudio.libduckeys.context.LifeManager;
import com.github.xushifustudio.libduckeys.midi.MidiEventRT;
import com.github.xushifustudio.libduckeys.midi.Mode;
import com.github.xushifustudio.libduckeys.midi.Modes;
import com.github.xushifustudio.libduckeys.midi.Note;
import com.github.xushifustudio.libduckeys.ui.box2.SurfaceContext;

public class InstrumentContext extends SurfaceContext {

    private Instrument instrument;
    private MidiEventRT mert;
    private Keyboard keyboard;

    private SensorBuffer sensorBuffer;

    private ModeManager modeManager;
    private ChordManager chordManager;


    public InstrumentContext() {
        this.keyboard = new Keyboard(this);
        this.mert = new MidiEventRT(InstrumentContext.class.getName());
        this.sensorBuffer = new SensorBuffer(1024 * 2);
        this.modeManager = new ModeManager();
        this.chordManager = new ChordManager();
    }


    public void init(Context ctx, SurfaceView view, LifeManager lm) {

        InstrumentContext ic = this; // new InstrumentContext();

        MidiEventRT rt = new MidiEventRT(InstrumentContext.class.getName());
        Life life = new InstrumentHolder(ic);
        SurfaceHolder.Callback callback = new InstrumentSurfaceHolderCallback(ic);
        Runnable looper = new InstrumentRenderLooper(ic);
        View.OnTouchListener on_touch = new InstrumentOnTouchListener(ic);
        Mode mode = Modes.major(Note.forNote(60));
        Instrument instr = new InstrumentImpl(ic);

        ic.setMert(rt);
        ic.setParent(ctx);
        ic.setView(view);
        ic.setView2(null); // after
        ic.setLifeManager(lm);
        ic.setLife(life);
        ic.setHolder(null); // after
        ic.setCallback(callback);
        ic.setOnTouchListener(on_touch);
        ic.setLooper(looper);
        ic.setInstrument(instr);

        ic.setWidth(0);
        ic.setHeight(0);
        ic.setActive(false);
        ic.setLayoutRevision(0);


        ic.getInstrument().apply(mode);

//        return ic;
    }


    public ModeManager getModeManager() {
        return modeManager;
    }

    public void setModeManager(ModeManager modeManager) {
        this.modeManager = modeManager;
    }

    public ChordManager getChordManager() {
        return chordManager;
    }

    public void setChordManager(ChordManager chordManager) {
        this.chordManager = chordManager;
    }

    public SensorBuffer getSensorBuffer() {
        return sensorBuffer;
    }

    public void setSensorBuffer(SensorBuffer sensorBuffer) {
        this.sensorBuffer = sensorBuffer;
    }

    public Keyboard getKeyboard() {
        return keyboard;
    }

    public void setKeyboard(Keyboard keyboard) {
        this.keyboard = keyboard;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

    public MidiEventRT getMert() {
        return mert;
    }

    public void setMert(MidiEventRT mert) {
        this.mert = mert;
    }
}
