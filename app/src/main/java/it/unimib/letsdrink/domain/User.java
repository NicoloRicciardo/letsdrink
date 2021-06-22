package it.unimib.letsdrink.domain;

import java.util.Objects;

//rappresentazione di uno User
public class User {
    private String userName;
    private String age;
    private String email;
    private String userID;

    public User(String userName, String age, String email, String userID) {
        this.userName = userName;
        this.age = age;
        this.email = email;
        this.userID = userID;
    }

    public User() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userName, user.userName) &&
                Objects.equals(age, user.age) &&
                Objects.equals(email, user.email);
    }

}
