package com.galewings;

import com.galewings.exception.GaleWingsSystemException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.springframework.ui.Model;

/**
 * Modelモッククラス
 */
public class ModelMock implements Model {

  private final Map<String, Object> attr = new HashMap<>();

  @Override
  public Model addAttribute(String attributeName, Object attributeValue) {
    attr.put(attributeName, attributeValue);
    return this;
  }

  @Override
  public Model addAttribute(Object attributeValue) {
    // TODO 利用想定なしのため、例外投げる
    throw new GaleWingsSystemException();
  }

  @Override
  public Model addAllAttributes(Collection<?> attributeValues) {
    // TODO 利用想定なしのため、例外投げる
    throw new GaleWingsSystemException();
  }

  @Override
  public Model addAllAttributes(Map<String, ?> attributes) {
    attr.putAll(attributes);
    return this;
  }

  @Override
  public Model mergeAttributes(Map<String, ?> attributes) {
    attr.putAll(attributes);
    return this;
  }

  @Override
  public boolean containsAttribute(String attributeName) {
    return attr.containsKey(attributeName);
  }

  @Override
  public Object getAttribute(String attributeName) {
    return attr.get(attributeName);
  }

  @Override
  public Map<String, Object> asMap() {
    return attr;
  }
}
