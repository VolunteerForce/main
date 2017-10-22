package com.codepath.volunteerhero.imgur;

/**
 * Created by jan_spidlen on 10/21/17.
 */

public class Constants {
    /*
      Logging flag
     */
    public static final boolean LOGGING = false;

    /*
      Your imgur client id. You need this to upload to imgur.

      More here: https://api.imgur.com/
     */
    public static final String MY_IMGUR_CLIENT_ID = "c8dfa5f32ff1fe5";
    public static final String MY_IMGUR_CLIENT_SECRET = "11c24cb4a34bf812ac7e14fbd19963d038965d0c";

    /*
      Redirect URL for android.
     */
    public static final String MY_IMGUR_REDIRECT_URL = "http://android/volunteerhero";

    /*
      Client Auth
     */
    public static String getClientAuth() {
        return "Client-ID " + MY_IMGUR_CLIENT_ID;
    }

}