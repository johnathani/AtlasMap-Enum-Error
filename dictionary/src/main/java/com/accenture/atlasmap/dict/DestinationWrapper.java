package com.accenture.atlasmap.dict;

import java.util.Objects;

public class DestinationWrapper {

    String targetName;
    Meh meh;        

    public String getTargetName() {
        return this.targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public Meh getMeh() {
        return this.meh;
    }

    public void setMeh(Meh meh) {
        this.meh = meh;
    }

    public boolean equals(SourceObj src) {
        if (null == src)
            return false;

        boolean a = Objects.equals(targetName, src.getSourceName());
        boolean b = Objects.equals(meh, src.getMeh());
        return  a && b;
    }        
}