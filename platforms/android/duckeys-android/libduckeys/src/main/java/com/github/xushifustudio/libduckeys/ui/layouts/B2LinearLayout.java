package com.github.xushifustudio.libduckeys.ui.layouts;

import android.graphics.Rect;
import android.graphics.RectF;

import com.github.xushifustudio.libduckeys.ui.box2.B2Container;
import com.github.xushifustudio.libduckeys.ui.box2.B2Layout;
import com.github.xushifustudio.libduckeys.ui.box2.B2LayoutParams;
import com.github.xushifustudio.libduckeys.ui.box2.B2LayoutThis;
import com.github.xushifustudio.libduckeys.ui.box2.B2View;

import java.util.ArrayList;
import java.util.List;

public class B2LinearLayout implements B2Layout {

    private final Direction direction;

    private B2LinearLayout(Direction dir) {
        this.direction = dir;
    }


    public enum Direction {
        VERTICAL, HORIZONTAL
    }

    private static class MyBlock {
        float offset;
        float length;
        boolean soft; // 代表这个块是可伸缩的
        int weight;
        MyChildHolder h_child; // optional: maybe null

        MyBlock() {
        }

        MyBlock(float len) {
            this.length = len;
        }

        B2View getChild() {
            MyChildHolder ch = this.h_child;
            if (ch == null) {
                return null;
            }
            return ch.child;
        }
    }


    private static class MyChildHolder {
        final B2View child;
        final B2LayoutParams lp;

        public MyChildHolder(B2View view) {
            this.child = view;
            this.lp = view.getLayoutParams();
        }
    }

    private static class MyParentHolder {
        final B2Container parent;
        final B2LayoutParams lp;
        RectF client;

        public MyParentHolder(B2Container container) {
            this.parent = container;
            this.lp = container.getLayoutParams();
            this.client = this.computeClientRect();
        }

        private RectF computeClientRect() {
            RectF rect = new RectF(0, 0, parent.width, parent.height);
            RectF padding = lp.padding;
            rect.top += padding.top;
            rect.left += padding.left;
            rect.right -= padding.right;
            rect.bottom -= padding.bottom;
            return rect;
        }
    }

    private static class MyLinearBuffer {
        final List<MyBlock> blocks;

        MyLinearBuffer() {
            this.blocks = new ArrayList<>();
        }


        void computeAllSoftBlock(float client_offset, float client_length) {
            int weight_sum = 0;
            float hard_length = 0;
            for (MyBlock block : this.blocks) {
                if (block.soft) {
                    // soft
                    if (block.weight > 0) {
                        weight_sum += block.weight;
                    }
                } else {
                    //hard
                    hard_length += block.length;
                }
            }
            float soft_total_length = client_length - hard_length;
            if (soft_total_length < 0.1 || weight_sum <= 0) {
                return;
            }
            float at = client_offset;
            for (MyBlock block : this.blocks) {
                if (block.soft) {
                    block.length = (soft_total_length * block.weight) / weight_sum;
                }
                block.offset = at;
                at += block.length;
            }
        }


        void add(MyBlock block) {
            if (block == null) {
                return;
            }
            this.blocks.add(block);
        }
    }

    private static class MyCrossBuffer {
        final List<MyBlock> blocks;
        MyBlock max;

        MyCrossBuffer() {
            this.blocks = new ArrayList<>();
        }

        void add(MyBlock block) {
            if (block == null) {
                return;
            }
            final MyBlock older = this.max;
            if (older == null) {
                this.max = block;
            } else {
                if (block.length > older.length) {
                    this.max = block;
                }
            }
            this.blocks.add(block);
        }

        void computeAllSoftBlock(float client_offset, float client_length) {
            MyBlock model = this.max;
            if (model == null) {
                return;
            }
            for (MyBlock block : this.blocks) {
                if (block.soft) {
                    block.length = model.length;
                }
                block.offset = client_offset;
            }
        }
    }


    private static class MyInnerLayoutHorizontal implements B2Layout {
        @Override
        public void apply(B2Container container, B2LayoutThis self) {
            MyCrossBuffer cross = new MyCrossBuffer();
            MyLinearBuffer linear = new MyLinearBuffer();
            MyParentHolder ph = new MyParentHolder(container);
            List<B2View> children = container.listChildren();

            for (B2View child : children) {
                MyChildHolder ch = new MyChildHolder(child);
                this.onChildLinear(ch, ph, linear);
                this.onChildCross(ch, ph, cross);
            }

            RectF client = ph.client;
            cross.computeAllSoftBlock(client.top, client.height());
            linear.computeAllSoftBlock(client.left, client.width());

            this.applyToBlocks(cross);
            this.applyToBlocks(linear);
        }

        private void applyToBlocks(MyCrossBuffer cross) {
            // cross: vertical(y,height)
            for (MyBlock item : cross.blocks) {
                B2View child = item.getChild();
                if (child == null) {
                    continue;
                }
                child.y = item.offset;
                child.height = item.length;
            }
        }

        private void applyToBlocks(MyLinearBuffer linear) {
            // linear: horizontal(x,width)
            for (MyBlock item : linear.blocks) {
                B2View child = item.getChild();
                if (child == null) {
                    continue;
                }
                child.x = item.offset;
                child.width = item.length;
            }
        }

        private void onChildCross(MyChildHolder ch, MyParentHolder ph, MyCrossBuffer cross) {
            // cross: vertical(y,height)

            final MyBlock b1 = new MyBlock();
            final int layout_height = ch.lp.layoutHeight;

            if (layout_height > 0) {
                b1.length = layout_height;
            } else if (layout_height == B2LayoutParams.SIZE_AS_CONTENT) {
                b1.length = ch.child.contentHeight;
            } else if (layout_height == B2LayoutParams.SIZE_AS_PARENT) {
                b1.length = ph.client.height();
            } else if (layout_height == B2LayoutParams.SIZE_AS_WEIGHT) {
                // fill parent with 100% size
                b1.length = ch.child.contentHeight;
                b1.soft = true;
                b1.weight = 1;
            } else {
                b1.length = ch.child.contentHeight; // bad value
            }

            b1.h_child = ch;
            cross.add(b1);
        }

        private void onChildLinear(MyChildHolder ch, MyParentHolder ph, MyLinearBuffer linear) {

            // linear: horizontal(x,width)

            RectF margin = ch.lp.margin;
            RectF padding = ch.lp.padding;
            final MyBlock b1 = new MyBlock();
            b1.h_child = ch;
            final int layout_width = ch.lp.layoutWidth;

            linear.add(new MyBlock(margin.left));

            if (layout_width > 0) {
                b1.length = layout_width;
            } else if (layout_width == B2LayoutParams.SIZE_AS_CONTENT) {
                b1.length = ch.child.contentWidth + padding.left + padding.right;
            } else if (layout_width == B2LayoutParams.SIZE_AS_PARENT) {
                b1.length = ph.client.width();
            } else if (layout_width == B2LayoutParams.SIZE_AS_WEIGHT) {
                b1.soft = true;
                b1.weight = ch.lp.layoutWeight;
            } else {
                b1.length = 0;
            }

            linear.add(b1);
            linear.add(new MyBlock(margin.right));
        }
    }

    private static class MyInnerLayoutVertical implements B2Layout {
        @Override
        public void apply(B2Container container, B2LayoutThis self) {
            MyCrossBuffer cross = new MyCrossBuffer();
            MyLinearBuffer linear = new MyLinearBuffer();
            MyParentHolder ph = new MyParentHolder(container);
            List<B2View> children = container.listChildren();

            for (B2View child : children) {
                MyChildHolder ch = new MyChildHolder(child);
                this.onChildCross(ch, ph, cross);
                this.onChildLinear(ch, ph, linear);
            }

            RectF client = ph.client;
            cross.computeAllSoftBlock(client.left, client.width());
            linear.computeAllSoftBlock(client.top, client.height());

            this.applyToBlocks(cross);
            this.applyToBlocks(linear);
        }

        private void applyToBlocks(MyCrossBuffer cross) {
            // cross: horizontal(x,width)
            for (MyBlock item : cross.blocks) {
                B2View child = item.getChild();
                if (child == null) {
                    continue;
                }
                child.x = item.offset;
                child.width = item.length;
            }
        }

        private void applyToBlocks(MyLinearBuffer linear) {
            // linear: vertical(y,height)
            for (MyBlock item : linear.blocks) {
                B2View child = item.getChild();
                if (child == null) {
                    continue;
                }
                child.y = item.offset;
                child.height = item.length;
            }
        }


        private void onChildCross(MyChildHolder ch, MyParentHolder ph, MyCrossBuffer cross) {
            // cross: horizontal(x,width)

            final MyBlock b1 = new MyBlock();
            final int layout_width = ch.lp.layoutWidth;

            if (layout_width > 0) {
                b1.length = layout_width;
            } else if (layout_width == B2LayoutParams.SIZE_AS_CONTENT) {
                b1.length = ch.child.contentWidth;
            } else if (layout_width == B2LayoutParams.SIZE_AS_PARENT) {
                b1.length = ph.client.width();
            } else if (layout_width == B2LayoutParams.SIZE_AS_WEIGHT) {
                // fill parent with 100% size
                b1.length = ch.child.contentWidth;
                b1.soft = true;
                b1.weight = 1;
            } else {
                b1.length = ch.child.contentWidth; // bad value
            }

            b1.h_child = ch;
            cross.add(b1);
        }

        private void onChildLinear(MyChildHolder ch, MyParentHolder ph, MyLinearBuffer linear) {
            // linear: vertical(y,height)
            RectF margin = ch.lp.margin;
            RectF padding = ch.lp.padding;
            final MyBlock b1 = new MyBlock();
            b1.h_child = ch;
            final int layout_height = ch.lp.layoutHeight;

            linear.add(new MyBlock(margin.top));

            if (layout_height > 0) {
                b1.length = layout_height;
            } else if (layout_height == B2LayoutParams.SIZE_AS_CONTENT) {
                b1.length = ch.child.contentHeight + padding.top + padding.bottom;
            } else if (layout_height == B2LayoutParams.SIZE_AS_PARENT) {
                b1.length = ph.client.height();
            } else if (layout_height == B2LayoutParams.SIZE_AS_WEIGHT) {
                b1.soft = true;
                b1.weight = ch.lp.layoutWeight;
            } else {
                b1.length = 0;
            }

            linear.add(b1);
            linear.add(new MyBlock(margin.bottom));
        }
    }


    public static B2LinearLayout newInstance(Direction dir) {
        return new B2LinearLayout(dir);
    }


    @Override
    public void apply(B2Container container, B2LayoutThis self) {
        B2Layout inner;
        if (this.direction == Direction.HORIZONTAL) {
            inner = new MyInnerLayoutHorizontal();
        } else {
            inner = new MyInnerLayoutVertical();
        }
        inner.apply(container, self);
    }
}
