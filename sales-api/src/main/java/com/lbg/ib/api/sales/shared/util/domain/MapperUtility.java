package com.lbg.ib.api.sales.shared.util.domain;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MapperUtility {

    private MapperUtility() {

    }

    public static <T> boolean isOf(Class<T> clazz, Object object) {
        return clazz.isInstance(object);
    }

    public static <T> T iterateForType(Object[] references, Class<T> clazz) {
        return doIterateForType(references, clazz, 0);
    }

    private static <T> T doIterateForType(Object[] references, Class<T> clazz, int index) {
        int current = index;
        if (index == references.length) {
            return null;
        }
        if (isOf(clazz, references[current])) {
            return (T) references[current];
        }
        current++;
        return doIterateForType(references, clazz, current);
    }

    public static <T> List<T> convertArrayToList(T[] array) {
        if (array == null) {
            return new LinkedList<T>();
        }
        return Arrays.asList(array);
    }
}
