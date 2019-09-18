package com.app.singlehotel.Util;

public class Events {

    // Event used to send message from login notify.
    public static class Login {
        private String login;

        public Login(String login) {
            this.login = login;
        }

        public String getLogin() {
            return login;
        }
    }

}
