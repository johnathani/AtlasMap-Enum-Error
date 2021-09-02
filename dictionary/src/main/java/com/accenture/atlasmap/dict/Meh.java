package com.accenture.atlasmap.dict;

import java.util.Objects;

public class Meh {
    private String myField;
    private SampleEnum sample;

    public String getMyField() {
        return this.myField;
    }

    public void setMyField(String myField) {
        this.myField = myField;
    }


    public SampleEnum getSample() {
        return this.sample;
    }

    public void setSample(SampleEnum sample) {
        this.sample = sample;
    }

    public boolean equals(Meh src) {
        if (null == src)
            return false;

        return Objects.equals(src.getMyField(), myField) && Objects.equals(src.getSample(), sample);
    }    
}
