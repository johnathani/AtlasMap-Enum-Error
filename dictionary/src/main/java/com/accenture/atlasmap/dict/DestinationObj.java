package com.accenture.atlasmap.dict;

import java.util.Objects;

public class DestinationObj {
    DestinationWrapper wrapper;

    public DestinationWrapper getWrapper() {
        return this.wrapper;
    }

    public void setWrapper(DestinationWrapper wrapper) {
        this.wrapper = wrapper;
    }

    public boolean equals(SourceObj src) {
        if (null == src)
            return false;

        if (null == wrapper) { 
            return null == src.getSourceName() && null == src.getMeh();
        }
        return wrapper.equals(src);
    }
}
