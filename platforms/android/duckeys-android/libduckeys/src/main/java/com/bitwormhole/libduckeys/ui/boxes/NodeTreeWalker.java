package com.bitwormhole.libduckeys.ui.boxes;

import android.util.Log;

import com.bitwormhole.libduckeys.helper.DuckLogger;

public class NodeTreeWalker {

    private int depthLimit;

    public NodeTreeWalker() {
        this.depthLimit = 32;
    }

    public interface Handler {
        void handleNode(Node node);
    }


    private void walkInto(Node node, Handler h, int depth) {

        if (node == null || h == null) {
            return;
        }
        if (depth > depthLimit) {
            Log.e(DuckLogger.TAG, "NodeTreeWalker: the tree is too deep");
            return;
        }

        h.handleNode(node);

        if (node instanceof Container) {
            Container can = (Container) node;
            can.forChildren((child) -> {
                walkInto(child, h, depth + 1);
            });
        }
    }

    public void walk(Node node, Handler h) {
        walkInto(node, h, 0);
    }
}
