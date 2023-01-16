package config;

import lombok.Getter;

@Getter
public class Endpoints {
    public static String BASE_URL = "https://jsonplaceholder.typicode.com";
    public static String ID = "id";
    public static String POSTS = "/posts";
    public static final String POSTS_ID = POSTS + "/{" + ID + "}";
}
