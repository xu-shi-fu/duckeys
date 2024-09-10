package com.bitwormhole.libduckeys.ui.boxes;

import com.bitwormhole.libduckeys.ui.layouts.SimpleLayout;

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

    public static interface OnChildHandler {
        void OnChild(Node child);
    }

    public void forChildren(OnChildHandler h) {
        List<Node> all = children;
        if (all == null) {
            return;
        }
        for (Node item : all) {
            if (item == null) {
                continue;
            }
            if (item.isChildOf(this)) {
                h.OnChild(item);
            }
        }
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
