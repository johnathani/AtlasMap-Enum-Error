package com.accenture.atlasmap.dict;

import java.util.Objects;

public class ListItem {
    private String name;
    private Integer ord;
    private Meh value;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrd() {
        return this.ord;
    }

    public void setOrd(Integer ord) {
        this.ord = ord;
    }

    public Meh getValue() {
        return this.value;
    }

    public void setValue(Meh value) {
        this.value = value;
    }

    public boolean equals(ListItem src) {
        if (null == src)
            return false;

        return Objects.equals(src.getName(), name) && Objects.equals(src.getOrd(), ord)
                && value.equals(src.getValue());
    }
}
