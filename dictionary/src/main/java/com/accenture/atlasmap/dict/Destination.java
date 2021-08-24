package com.accenture.atlasmap.dict;

import java.util.Objects;

public class Destination {
   String header;
   SampleEnum sample;
   int value;
   Integer nullableValue;

   public String getHeader() {
      return this.header;
   }

   public void setHeader(String header) {
      this.header = header;
   }

   public SampleEnum getSample() {
      return this.sample;
   }

   public void setSample(SampleEnum sample) {
      this.sample = sample;
   }

   public int getValue() {
      return this.value;
   }

   public void setValue(int value) {
      this.value = value;
   }

   public Integer getNullableValue() {
      return this.nullableValue;
   }

   public void setNullableValue(Integer nullableValue) {
      this.nullableValue = nullableValue;
   }

   public boolean equals(Source src) {
      if (null == src)
         return false;
      return Objects.equals(header, src.getSourceHeader()) && Objects.equals(sample, src.getSourceEnum())
            && value == src.getSourceInt() && Objects.equals(nullableValue, src.getSourceNullableInt());
   }

}
