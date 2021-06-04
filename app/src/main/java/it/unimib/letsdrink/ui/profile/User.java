package it.unimib.letsdrink.ui.profile;

public class User {
    private String user_name;
    private String age;
    private String email;
    /*private String password;*/
    private String userID;

    public User() {
    }

    public User(String user_name, String age, String email, String userID) {
        this.user_name = user_name;
        this.age = age;
        this.email = email;
        /*this.password = password;*/
        this.userID = userID;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /*public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }*/

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
