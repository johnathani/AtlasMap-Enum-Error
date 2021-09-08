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

    @Override
    public boolean equals(Object srcO) {        
        if (null == srcO || !(srcO instanceof Meh))
            return false;

        Meh src = (Meh)srcO;

        boolean a = Objects.equals(src.getSample(), sample);
        boolean b = Objects.equals(src.getMyField(), myField);

        return a && b;
    }    
}
