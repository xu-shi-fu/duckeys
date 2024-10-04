package com.github.xushifustudio.libduckeys.context;

import java.util.ArrayList;
import java.util.List;

public class ComponentsLoader {

    private final ComponentContext mCC;


    public ComponentsLoader(ComponentContext cc) {
        mCC = cc;
    }


    public interface Loading {
        void onRegisterComponents(ComponentRegistering cr);
    }


    // 加载组件
    public final void loadComponents(Loading loading) {
        ComponentManager cm = new ComponentManager();
        mCC.components = cm;
        loading.onRegisterComponents(cm);
    }
}
