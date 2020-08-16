package usercomponent;

import java.io.Serializable;

/**
 * The entity class that represents a user in the trading system.
 */
class User implements Serializable {

    private String name;
    private String username;
    private String password;

    /**
     * Instantiates a new User.
     *
     * @param name the given name
     * @param username the given username
     * @param password the given password
     */
    protected User(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    /**
     * Gets the name of this User.
     * @return the name of this User
     */
    protected String getName(){
        return name;
    }

    /**
     * Sets the name of this User.
     * @param name the new name of this User
     */
    protected void setName(String name){
        this.name = name;
    }

    /**
     * Gets the username of this User.
     * @return the username of this User
     */
    protected String getUsername(){
        return username;
    }

    /**
     * Sets the username of this User.
     * @param username the new username of this User
     */
    protected void setUsername(String username){
        this.username = username;
    }

    /**
     * Gets the password of this User.
     * @return the password of this User
     */
    protected String getPassword(){
        return password;
    }

    /**
     * Sets the password of this User.
     * @param password the new password of this User
     */
    protected void setPassword(String password){
        this.password = password;
    }

    /**
     * Returns whether this user has admin capabilities.
     * @return whether the User is an admin
     */
    protected boolean isAdmin() {
        return true;
    }

    /**
     * Returns the string representation of this user.
     * @return the string representation of this user
     */
    public String toString() {
        return name + "\n" + username;
    }
}