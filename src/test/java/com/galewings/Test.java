package com.galewings;

import org.apache.lucene.search.spell.JaroWinklerDistance;

public class Test {

  public static void main(String[] args) {
    String str1 = "足なんて飾りです。偉い人にはそれが分からんのです。";
    String str2 = "胸なんて飾りです。男にはそれが分からんのです。";

    JaroWinklerDistance jaro = new JaroWinklerDistance();

    System.out.println(jaro.getDistance(str1, str2));
  }
}
