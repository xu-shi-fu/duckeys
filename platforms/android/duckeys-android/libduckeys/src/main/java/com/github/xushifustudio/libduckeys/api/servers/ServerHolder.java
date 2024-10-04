package com.github.xushifustudio.libduckeys.api.servers;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.github.xushifustudio.libduckeys.context.BaseLife;
import com.github.xushifustudio.libduckeys.context.ComponentContext;
import com.github.xushifustudio.libduckeys.context.ComponentLife;
import com.github.xushifustudio.libduckeys.context.ComponentRegistration;
import com.github.xushifustudio.libduckeys.context.Components;
import com.github.xushifustudio.libduckeys.context.Life;

import java.util.ArrayList;
import java.util.List;

public class ServerHolder extends BaseLife {

    private Server server;
    private Components components;
    private ComponentContext componentContext;
    private ComponentRegistration[] mComLifeList;


    public ServerHolder() {
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Components getComponents() {
        return components;
    }

    public void setComponents(Components components) {
        this.components = components;
    }


    public ComponentContext getComponentContext() {
        return componentContext;
    }

    public void setComponentContext(ComponentContext componentContext) {
        this.componentContext = componentContext;
    }

    private interface OnLifeListener {
        void onLife(Life life);
    }

    private void invokeOnLifeListener(Life life, OnLifeListener listener) {
        if (life == null || listener == null) {
            return;
        }
        listener.onLife(life);
    }


    private void invokeOnLifeListeners(boolean reverse, OnLifeListener l) {
        ComponentRegistration[] list = mComLifeList;
        if (list == null) {
            list = loadComLifeList();
            mComLifeList = list;
        }
        if (reverse) {
            for (int i = list.length - 1; i >= 0; i--) {
                invokeOnLifeListener(list[i].life, l);
            }
        } else {
            for (int i = 0; i < list.length; i++) {
                invokeOnLifeListener(list[i].life, l);
            }
        }
    }

    private ComponentRegistration[] loadComLifeList() {
        List<Object> src = this.components.listAll();
        List<ComponentRegistration> dst = new ArrayList<>();
        ComponentContext cc = componentContext;
        for (Object item1 : src) {
            if (item1 instanceof ComponentLife) {
                ComponentLife cl = (ComponentLife) item1;
                ComponentRegistration reg = cl.init(cc);
                if (reg != null) {
                    dst.add(reg);
                }
            }
        }
        dst.sort((a, b) -> {
            return a.order - b.order;
        });
        int size = dst.size();
        return dst.toArray(new ComponentRegistration[size]);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        invokeOnLifeListeners(false, (l) -> {
            l.onCreate(savedInstanceState);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        invokeOnLifeListeners(false, Life::onStart);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        invokeOnLifeListeners(false, Life::onRestart);
    }

    @Override
    public void onStop() {
        // 实际上，这个方法永远不会被调用，切勿使用
        super.onStop();
        invokeOnLifeListeners(true, Life::onStop);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        invokeOnLifeListeners(true, Life::onDestroy);
    }
}
