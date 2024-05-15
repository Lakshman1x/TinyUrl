package com.training.tinyurl.constants;

public class RegexpPattern {
    private RegexpPattern(){}
    public static final String BASE64_PATTERN = "^[A-Za-z0-9+/]*={0,2}$";
    public static final String URL_PATTERN = "^(https?|ftp):\\/\\/[^\\s/$.?#].[^\\s]*$";
    public  static  final String PASSWORD_PATTERN="^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).+$";
}
