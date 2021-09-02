package com.accenture.atlasmap.dict;

import java.util.List;

public class SourceList {
    public String noise1;
    public String noise2;

    List<ListItem> found;

    public String getNoise1() {
        return this.noise1;
    }

    public void setNoise1(String noise1) {
        this.noise1 = noise1;
    }

    public String getNoise2() {
        return this.noise2;
    }

    public void setNoise2(String noise2) {
        this.noise2 = noise2;
    }

    public List<ListItem> getFound() {
        return this.found;
    }

    public void setFound(List<ListItem> found) {
        this.found = found;
    }
}
