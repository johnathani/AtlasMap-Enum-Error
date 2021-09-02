package com.accenture.atlasmap.dict;

import java.util.List;
import java.util.Objects;

public class DestinationList {

    List<ListItem> items;

    public List<ListItem> getItems() {
        return this.items;
    }

    public void setItems(List<ListItem> items) {
        this.items = items;
    }

    public boolean equals(SourceList src) {
        if (null == src)
            return false;

        if (null == src.getFound() && null == items)
            return true;

        if (null == src.getFound() && null != items || null != src.getFound() && null == items
                || src.getFound().size() != items.size())
            return false;

        for (int i = 0; i < items.size(); i++) {
            if (!items.get(i).equals(src.getFound().get(i)))
                return false;
        }

        return true;
    }

}
