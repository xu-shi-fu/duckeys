package com.github.xushifustudio.libduckeys.context;

import java.util.List;

public interface Components {


    interface Filter {
        boolean accept(String name, Object obj);
    }

    <T> T find(Class<T> t);

    <T> T findWithFilter(Filter f, Class<T> t);

    // id: name|alias
    <T> T findById(String id, Class<T> t);

    <T> List<T> listWithFilter(Filter f, Class<T> t);

    List<Object> listAll();

}
