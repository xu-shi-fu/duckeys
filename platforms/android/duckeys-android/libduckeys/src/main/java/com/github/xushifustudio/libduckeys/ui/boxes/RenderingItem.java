package com.github.xushifustudio.libduckeys.ui.boxes;


import android.util.Log;

import com.github.xushifustudio.libduckeys.helper.DuckLogger;

/**
 * RenderingItem 表示一个 Node 在 Viewport 中的绝对位置
 */
public class RenderingItem extends Box {

    public final RenderingContext context;
    public final Node target;

    // copy the src
    public RenderingItem(RenderingItem src) {
        super(src);
        context = src.context;
        target = src.target;
    }

    public RenderingItem(RenderingContext ctx, Node tar) {
        this.context = ctx;
        this.target = tar;
        computeGeometry();
    }


    private void computeGeometry() {
        int x1 = 0;
        int y1 = 0;
        int z1 = 0;
        Node ptr = target;
        final int depthMax = 32;
        int depth = 0;
        for (; ptr != null; ) {
            if (depth > depthMax) {
                Log.e(DuckLogger.TAG, "RenderingItem: the parent-path is too deep");
                break;
            }
            x1 += ptr.x;
            y1 += ptr.y;
            z1 += ptr.z;
            ptr = ptr.getParent();
            depth++;
        }
        this.x = x1;
        this.y = y1;
        this.z = z1;
        this.width = target.width;
        this.height = target.height;
    }
}
