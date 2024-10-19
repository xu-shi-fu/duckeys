package com.github.xushifustudio.libduckeys.ui.box2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class B2Children extends B2View {

    private final B2Container parent;
    private ChildHolder first;
    private ChildHolder last;
    private int count;
    private boolean redoSortRequired;

    public B2Children(B2Container aParent) {
        this.parent = aParent;
    }

    public void add(B2View child) {
        innerAdd(child, 0);
    }

    public void add(B2View child, int id) {
        innerAdd(child, id);
    }

    private void innerAdd(B2View child, int id) {
        if (child == null) {
            return;
        }
        ChildHolder h = new ChildHolder();
        h.child = child;
        h.id = id;
        if (first == null || last == null) {
            first = h;
        } else {
            last.next = h;
            h.prev = last;
        }
        last = h;
        count++;
        redoSortRequired = true;
        child.setParent(this.parent, false);
    }

    public void remove(B2View child) {
        if (child == null) {
            return;
        }
        ChildHolder h = findHolder(child);
        if (h == null) {
            return;
        }
        if (h.prev == null) {
            // as first
            first = h.next;
        } else {
            h.prev.next = h.next;
        }
        if (h.next == null) {
            // as last
            last = h.prev;
        } else {
            h.next.prev = h.prev;
        }
        count--;
        redoSortRequired = true;
        child.setParent(null, false);
    }

    private ChildHolder findHolder(B2View child) {
        if (child == null) {
            return null;
        }
        int todo = count + 2;
        ChildHolder p1 = this.first;
        ChildHolder p2 = this.last;
        while (todo > 0) {
            if (p1 == null || p2 == null) {
                break;
            }
            if (child.equals(p1.child)) {
                return p1;
            }
            if (child.equals(p2.child)) {
                return p2;
            }
            p1 = p1.next;
            p2 = p2.prev;
            todo -= 2;
        }
        return null;
    }

    public void forItems(ChildHandler h) {
        this.tryRedoSort();
        ChildHolder p = this.first;
        for (; p != null; p = p.next) {
            if (p.child == null) {
                continue;
            }
            boolean wantMore = h.onChild(p.child, p);
            if (!wantMore) {
                break;
            }
        }
    }

    public void forItemsReverse(ChildHandler h) {
        this.tryRedoSort();
        ChildHolder p = this.last;
        for (; p != null; p = p.prev) {
            if (p.child == null) {
                continue;
            }
            boolean wantMore = h.onChild(p.child, p);
            if (!wantMore) {
                break;
            }
        }
    }

    public void sort(Comparator<ChildHolder> comp) {
        this.innerSort(comp);
    }

    private void tryRedoSort() {
        if (this.redoSortRequired) {
            this.redoSortRequired = false;
            this.innerSort(null);
        }
    }

    private void innerSort(Comparator<ChildHolder> comp) {
        if (comp == null) {
            comp = (a, b) -> {
                float diff = a.child.z - b.child.z;
                if (B2Size.isZero(diff)) {
                    return 0;
                }
                return (diff < 0) ? -1 : 1;
            };
        }
        List<ChildHolder> list = new ArrayList<>();
        for (ChildHolder p = this.first; p != null; p = p.next) {
            if (p.child != null) {
                list.add(p);
            }
        }
        list.sort(comp);
        ChildHolder p1 = null;
        ChildHolder p2 = null;
        for (ChildHolder h : list) {
            h.next = null;
            h.prev = p2;
            if (p1 == null) {
                p1 = h;
            }
            if (p2 != null) {
                p2.next = h;
            }
            p2 = h;
        }
        this.first = p1;
        this.last = p2;
        this.count = list.size();
    }

    public interface ChildHandler {
        // 返回： true=继续，false=退出
        boolean onChild(B2View child, ChildHolder holder);
    }

    public static class ChildHolder {
        private ChildHolder prev;
        private ChildHolder next;
        private B2View child;
        private int id;
        private int index;
    }
}
