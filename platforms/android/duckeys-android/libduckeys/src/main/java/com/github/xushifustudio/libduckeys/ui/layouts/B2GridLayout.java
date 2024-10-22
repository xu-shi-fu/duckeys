package com.github.xushifustudio.libduckeys.ui.layouts;

import android.graphics.RectF;

import com.github.xushifustudio.libduckeys.ui.box2.B2Container;
import com.github.xushifustudio.libduckeys.ui.box2.B2Layout;
import com.github.xushifustudio.libduckeys.ui.box2.B2LayoutThis;
import com.github.xushifustudio.libduckeys.ui.box2.B2View;

import java.util.List;

public class B2GridLayout implements B2Layout {


    public static final int ORDER_LEFT_TO_RIGHT = 0x80;
    public static final int ORDER_RIGHT_TO_LEFT = 0x40;
    public static final int ORDER_TOP_TO_BOTTOM = 0x20;
    public static final int ORDER_BOTTOM_TO_TOP = 0x10;
    public static final int ORDER_BOTTOM_OR_TOP_FIRST = 0x02;
    public static final int ORDER_LEFT_OR_RIGHT_FIRST = 0x01;


    // order 由 [FROM_LEFT_TO_RIGHT | FROM_RIGHT_TO_LEFT | FROM_TOP_TO_BOTTOM | ... ] 这几个值合成
    private int order;
    private int countRows;
    private int countColumns;

    private B2GridLayout(int col, int row) {
        this.countRows = row;
        this.countColumns = col;
        this.order = ORDER_LEFT_TO_RIGHT | ORDER_TOP_TO_BOTTOM | ORDER_BOTTOM_OR_TOP_FIRST;
    }


    public static B2GridLayout newInstance() {
        return new B2GridLayout(3, 3);
    }

    public static B2GridLayout newInstance(int col, int row) {
        return new B2GridLayout(col, row);
    }


    public int getCountRows() {
        return countRows;
    }

    public void setCountRows(int countRows) {
        this.countRows = countRows;
    }

    public int getCountColumns() {
        return countColumns;
    }

    public void setCountColumns(int countColumns) {
        this.countColumns = countColumns;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public void apply(B2Container parent, B2LayoutThis self) {

        List<B2View> children = parent.listChildren();
        final float parent_w = parent.width;
        final float parent_h = parent.height;
        final int col = getRegularColumnNum();
        final int row = getRegularRowNum();
        final float cell_w = parent_w / col;
        final float cell_h = parent_h / row;

        Model mod = new Model();
        mod.cell_w = cell_w;
        mod.cell_h = cell_h;
        mod.num_col = col;
        mod.num_row = row;

        Indexer idx1 = getIndexer1(mod);
        Indexer idx2 = getIndexer2(mod);
        int j = 0; // index for children
        idx1.reset();

        while (idx1.hasMore()) {
            idx1.next();
            idx2.reset();
            while (idx2.hasMore()) {
                idx2.next();
                if (j < children.size()) {
                    B2View child = children.get(j);
                    child.x = mod.x;
                    child.y = mod.y;
                    child.width = cell_w;
                    child.height = cell_h;
                    child.visible = true;
                }
                j++;
            }
        }

        // 隐藏剩余的 child
        for (; j < children.size(); j++) {
            B2View child = children.get(j);
            child.visible = false;
        }
    }

    @Override
    public void computeContentSize(B2Container container, RectF size) {

    }

    private interface Indexer {
        void reset();

        void next();

        boolean hasMore();
    }

    private static class Model {
        float x;
        float y;
        float cell_w;
        float cell_h;
        int num_col;
        int num_row;
    }


    private static class MyLeftRightIndexer implements Indexer {

        private final Model model;
        int from; // index of first
        int to; // index of (last+1)
        int step;
        int current; // index now

        MyLeftRightIndexer(Model mo) {
            this.model = mo;
        }

        private void updateModel(int index) {
            model.x = model.cell_w * index;
        }

        @Override
        public void reset() {
            current = from;
        }

        @Override
        public void next() {
            this.updateModel(current);
            current += step;
        }

        @Override
        public boolean hasMore() {
            if (step == 0) {
                return false;
            }
            return (step < 0) ? (current > to) : (current < to);
        }
    }

    private static class MyTopBottomIndexer implements Indexer {

        private final Model model;
        int from; // index of first
        int to; // index of (last+1)
        int step;
        int current; // index now

        MyTopBottomIndexer(Model mo) {
            this.model = mo;
        }

        private void updateModel(int index) {
            model.y = model.cell_h * index;
        }

        @Override
        public void reset() {
            current = from;
        }

        @Override
        public void next() {
            this.updateModel(current);
            current += step;
        }

        @Override
        public boolean hasMore() {
            if (step == 0) {
                return false;
            }
            return (step < 0) ? (current > to) : (current < to);
        }
    }

    private Indexer getIndexer1(Model mo) {
        if (isTopBottomFirst()) {
            return getIndexerTB(mo);
        }
        return getIndexerLR(mo);
    }

    private Indexer getIndexer2(Model mo) {
        if (isTopBottomFirst()) {
            return getIndexerLR(mo);
        }
        return getIndexerTB(mo);
    }

    private Indexer getIndexerLR(Model mo) {
        MyLeftRightIndexer idx = new MyLeftRightIndexer(mo);
        if (isLeftToRight()) {
            // left -> right
            idx.from = 0;
            idx.to = mo.num_col;
            idx.step = 1;
        } else {
            // left <- right
            idx.from = mo.num_col - 1;
            idx.to = -1;
            idx.step = -1;
        }
        idx.current = idx.from;
        return idx;
    }

    private Indexer getIndexerTB(Model mo) {
        MyTopBottomIndexer idx = new MyTopBottomIndexer(mo);
        if (isTopToBottom()) {
            // top -> bottom
            idx.from = 0;
            idx.to = mo.num_row;
            idx.step = 1;
        } else {
            // top <- bottom
            idx.from = mo.num_row - 1;
            idx.to = -1;
            idx.step = -1;
        }
        idx.current = idx.from;
        return idx;
    }

    private boolean isLeftToRight() {
        int l2r = order & ORDER_LEFT_TO_RIGHT;
        int r2l = order & ORDER_RIGHT_TO_LEFT;
        return ((l2r != 0) || (r2l == 0));
    }

    private boolean isTopToBottom() {
        int t2b = order & ORDER_TOP_TO_BOTTOM;
        int b2t = order & ORDER_BOTTOM_TO_TOP;
        return ((t2b != 0) || (b2t == 0));
    }

    private boolean isTopBottomFirst() {
        int lr1 = order & ORDER_LEFT_OR_RIGHT_FIRST;
        int bt1 = order & ORDER_BOTTOM_OR_TOP_FIRST;
        return ((lr1 == 0) || (bt1 != 0));
    }


    private int getRegularColumnNum() {
        int num = this.countColumns;
        if (num < 1) {
            num = 1;
        }
        return num;
    }

    private int getRegularRowNum() {
        int num = this.countRows;
        if (num < 1) {
            num = 1;
        }
        return num;
    }
}
