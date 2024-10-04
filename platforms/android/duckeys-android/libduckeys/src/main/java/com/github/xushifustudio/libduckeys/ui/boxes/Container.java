package com.github.xushifustudio.libduckeys.ui.boxes;

import android.view.MotionEvent;

import com.github.xushifustudio.libduckeys.ui.layouts.SimpleLayout;

import java.util.ArrayList;
import java.util.List;

/****
 * Container 表示一个可以包含其它元素的容器
 */
public class Container extends Element {

    private List<Node> children;

    private Layout layout;


    public Container() {
        children = new ArrayList<>();
        layout = SimpleLayout.getInstance();
    }

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    @Override
    public void updateLayout(LayoutContext lc) {
        sortChildren();
        // for self
        if (layout != null) {
            layout.apply(lc, this);
        }
        // for children
        if (children != null) {
            forChildren((child) -> {
                if (child instanceof LayoutAble) {
                    LayoutAble ch2 = (LayoutAble) child;
                    ch2.updateLayout(lc);
                }
            });
        }
        super.updateLayout(lc);
    }


    @Override
    public void onTouch(TouchContext ctx, TouchEventAdapter ada) {

        super.onTouch(ctx, ada);

        TouchPoint point = ada.point;
        if (point.done) return;

        if (ada.depth > ctx.depthLimit) {
            throw new RuntimeException("the onTouch-event-path is too deep");
        }

        // 按从大到小的顺序逐个测试
        forChildren(true, (child) -> {
            if (point.done) {
                return;
            }
            if (isHitChild(child, ada)) {
                TouchEventAdapter a2 = makeTouchEventAdapterForChild(child, ada);
                child.onTouch(ctx, a2);
            }
        });
    }

    private TouchEventAdapter makeTouchEventAdapterForChild(Node child, TouchEventAdapter parent) {
        float x2 = parent.x - child.x;
        float y2 = parent.y - child.y;
        TouchEventAdapter a2 = new TouchEventAdapter(child, x2, y2, parent);
        return a2;
    }


    private boolean isHitChild(Node child, TouchEventAdapter parent) {
        float x2 = parent.x;
        float y2 = parent.y;
        return child.containPoint((int) x2, (int) y2);
    }


    public static interface OnChildHandler {
        void OnChild(Node child);
    }

    private final class OnChildHandlerProxy implements OnChildHandler {

        private final OnChildHandler dest;

        public OnChildHandlerProxy(OnChildHandler h) {
            this.dest = h;
        }

        @Override
        public void OnChild(Node ch) {
            if (ch == null) {
                return;
            }
            final Container self = Container.this;
            if (ch.isChildOf(self)) {
                dest.OnChild(ch);
            }
        }
    }


    // 根据 z 排序，从小到大
    public void sortChildren() {
        List<Node> all = children;
        if (all == null) return;
        all.sort((a, b) -> {
            return a.z - b.z;
        });
    }


    // forChildren 依次列举每个子元素，并交给 h 处理;
    // reverse 如果为 true , 表示按相反的顺序 (z 从大到小) 进行;
    // reverse 如果为 false , 表示按正常的顺序 (z 从小到大) 进行;
    public void forChildren(boolean reverse, OnChildHandler h) {
        final List<Node> all = children;
        if (all == null) {
            return;
        }
        h = new OnChildHandlerProxy(h);
        if (reverse) {
            for (int i = all.size() - 1; i >= 0; i--) {
                h.OnChild(all.get(i));
            }
        } else {
            for (Node item : all) {
                h.OnChild(item);
            }
        }
    }

    // 按默认的顺序（从小到大）列举每一个子元素
    public void forChildren(OnChildHandler h) {
        forChildren(false, h);
    }

    public void addChild(Node child) {
        if (child == null) {
            return;
        }
        children.add(child);
        child.setParent(this);
    }


    public void removeChild(Node child) {
        if (child == null) {
            return;
        }
        if (!child.isChildOf(this)) {
            return;
        }
        children.remove(child);
        child.setParent(null);
    }


    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> list) {
        if (list == null) {
            return;
        }
        for (Node item : list) {
            item.setParent(this);
        }
        this.children = list;
    }
}
