package com.github.xushifustudio.libduckeys.ui.box2;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

public class B2CoordinateSystem {

    public float baseX;
    public float baseY;
    public float baseZ;

    public B2CoordinateSystem() {
    }

    public B2CoordinateSystem(B2View v) {
        if (v != null) {
            this.baseX = v.x;
            this.baseY = v.y;
            this.baseZ = v.z;
        }
    }

    public B2CoordinateSystem(B2CoordinateSystem src) {
        if (src != null) {
            this.baseX = src.baseX;
            this.baseY = src.baseY;
            this.baseZ = src.baseZ;
        }
    }

    public B2CoordinateSystem(B2View v, B2CoordinateSystem parent) {
        if (v == null || parent == null) {
            return;
        }
        this.baseX = parent.baseX + v.x;
        this.baseY = parent.baseY + v.y;
        this.baseZ = parent.baseZ + v.z;
    }

    public B2CoordinateSystem offset(float x, float y, float z) {
        B2CoordinateSystem cs2 = new B2CoordinateSystem(this);
        cs2.baseX += x;
        cs2.baseY += y;
        cs2.baseZ += z;
        return cs2;
    }

    public B2CoordinateSystem offset(B2View v) {
        return new B2CoordinateSystem(v, this);
    }

    public PointF global2local(PointF global) {
        if (global == null) {
            return new PointF();
        }
        float x = global.x - this.baseX;
        float y = global.y - this.baseY;
        return new PointF(x, y);
    }

    public PointF local2global(PointF local) {
        if (local == null) {
            return new PointF();
        }
        float x = this.baseX + local.x;
        float y = this.baseY + local.y;
        return new PointF(x, y);
    }

    public Point global2local(Point global) {
        if (global == null) {
            return new Point();
        }
        int x = (int) (global.x - this.baseX);
        int y = (int) (global.y - this.baseY);
        return new Point(x, y);
    }

    public Point local2global(Point local) {
        if (local == null) {
            return new Point();
        }
        int x = (int) (this.baseX + local.x);
        int y = (int) (this.baseY + local.y);
        return new Point(x, y);
    }


    public Rect local2global(Rect local) {
        if (local == null) {
            return new Rect();
        }
        int t = (int) (this.baseY + local.top);
        int l = (int) (this.baseX + local.left);
        int r = (int) (this.baseX + local.right);
        int b = (int) (this.baseY + local.bottom);
        return new Rect(l, t, r, b);
    }

    public RectF local2global(RectF local) {
        if (local == null) {
            return new RectF();
        }
        float t = (this.baseY + local.top);
        float l = (this.baseX + local.left);
        float r = (this.baseX + local.right);
        float b = (this.baseY + local.bottom);
        return new RectF(l, t, r, b);
    }
}
