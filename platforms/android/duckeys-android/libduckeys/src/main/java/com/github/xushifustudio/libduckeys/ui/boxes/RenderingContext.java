package com.github.xushifustudio.libduckeys.ui.boxes;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

public class RenderingContext {

    public long revision; // 表示渲染或布局的版本
    public Viewport viewport;
    public Canvas canvas;

    public final List<RenderingItem> items;

    public RenderingContext() {
        this.items = new ArrayList<>();
    }

}
