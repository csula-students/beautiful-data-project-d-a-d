package edu.csula.datascience.auth;

/**
 * Simple auth to get token to access course server controlled env
 */
public class AuthenticationApp {
    public static void main(String[] args) {
        Authorization auth = new Authorization();

        System.out.println(
            String.format(
                "Token: %s",
                auth.getToken("CS-454")
            )
        );
    }
}
