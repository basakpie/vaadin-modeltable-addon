package com.vaadin.addon.modeltable;

import java.util.HashMap;

/**
 * Created by gmind on 2016-10-31.
 */
public class KeyPairMapper<KEY1, KEY2> {

    private final HashMap<KEY1, KEY2> sourceMap = new HashMap<>();

    private final HashMap<KEY2, KEY1> targetMap = new HashMap<>();

    public void put(KEY1 sourceColumnId, KEY2 targetColumnId) {

        if (sourceColumnId == null || targetColumnId==null) {
            return;
        }
        sourceMap.put(sourceColumnId, targetColumnId);
        targetMap.put(targetColumnId, sourceColumnId);
    }

    public KEY2 getTargetColumnId(KEY1 sourceColumnId) {
        return sourceMap.get(sourceColumnId);
    }

    public KEY1 getSourceColumnId(KEY2 targetColumnId) {
        return targetMap.get(targetColumnId);
    }

    public void remove(KEY1 sourceColumnId) {
        final KEY2 targetColumnId = sourceMap.get(sourceColumnId);

        if (targetColumnId != null) {
            targetMap.remove(targetColumnId);
            sourceMap.remove(sourceColumnId);
        }
    }

    public void removeAll() {
        sourceMap.clear();
        targetMap.clear();
    }

}
