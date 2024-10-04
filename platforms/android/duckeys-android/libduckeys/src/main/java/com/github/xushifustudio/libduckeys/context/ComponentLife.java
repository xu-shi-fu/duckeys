package com.github.xushifustudio.libduckeys.context;

import java.util.List;

// ComponentLife 接口用于初始化一个组件。如果组件需要执行标准的初始化过程，就实现这个接口；
// 如果该组件对更详细的生命周期进行处理，则返回一个 Life 接口；否则，可以返回 null。
public interface ComponentLife {

    ComponentRegistration init(ComponentContext cc);

}
