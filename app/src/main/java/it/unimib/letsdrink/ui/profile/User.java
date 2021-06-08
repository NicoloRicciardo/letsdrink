package it.unimib.letsdrink.ui.profile;

import java.util.ArrayList;

import it.unimib.letsdrink.domain.Cocktail;

public class User {
    private String userName;
    private String age;
    private String email;
    private String userID;
    private ArrayList<Cocktail> customDrinks;

    public User() {
    }

    public User(String userName, String age, String email, String userID, ArrayList<Cocktail> custom_drinks) {
        this.userName = userName;
        this.age = age;
        this.email = email;
        this.userID = userID;
        this.customDrinks = custom_drinks;
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

    public ArrayList<Cocktail> getCustomDrinks() {
        return customDrinks;
    }

    public void addNewCustomDrink (Cocktail newDrink){
        this.customDrinks.add(newDrink);
    }

    public void removeCustomDrink (Cocktail userDrink){
        this.customDrinks.remove(userDrink);
    }
}
