package com.galewings;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GaleWingsJson {

  public static String toJson(Object obj) {
    ObjectMapper mapper = new ObjectMapper();

    try {
      return mapper.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      return "";
    }
  }
}
