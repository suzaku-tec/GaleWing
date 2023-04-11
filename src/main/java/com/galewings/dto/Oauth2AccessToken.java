package com.galewings.dto;

public class Oauth2AccessToken {

  public String access_token;
  public Integer expires_in;
  public String token_type;

  public String scope;

  @Override
  public String toString() {
    return "Oauth2AccessToken{" +
        "access_token='" + access_token + '\'' +
        ", expires_in=" + expires_in +
        ", token_type='" + token_type + '\'' +
        ", scope='" + scope + '\'' +
        '}';
  }

}
