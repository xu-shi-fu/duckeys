package com.bitwormhole.libduckeys.ui.boxes;

import java.util.List;

public class Viewport extends Box implements RenderAble, LayoutAble {

    public Element root;
    public long revision; // 表示渲染或布局的版本


    /**
     * invoke 调用 Runnable，带同步锁
     */
    public synchronized void invoke(Runnable r) {
        revision++;
        r.run();
    }

    @Override
    public void updateLayout(LayoutContext lc) {
        if (root == null || lc == null) {
            return;
        }
        invoke(() -> {
            final Viewport vpt = Viewport.this;
            root.x = 0;
            root.y = 0;
            root.width = vpt.width;
            root.height = vpt.height;
            root.updateLayout(lc);
        });
    }


    @Override
    public void render(RenderingContext rc, RenderingItem itemIgnored) {

        if (root == null || rc == null) {
            return;
        }

        final List<RenderingItem> list = rc.items;

        // walk
        NodeTreeWalker walker = new NodeTreeWalker();
        walker.walk(root, (item1) -> {
            RenderingItem item2 = new RenderingItem(rc, item1);
            list.add(item2);
        });

        // sort (根据z轴排序)
        list.sort((a, b) -> {
            return a.z - b.z;
        });

        // paint
        for (RenderingItem item : list) {
            item.target.render(rc, item);
        }
    }
}
