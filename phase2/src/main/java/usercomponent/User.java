package usercomponent;

import java.io.Serializable;

class User implements Serializable {

    private String name;
    private String username;
    private String password;

    /**
     * Instantiates a new User
     *
     * @param name The given name
     * @param username The given username
     * @param password The given password
     */
    protected User(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    /**
     * Gets the name of this User
     *
     * @return The name of this User
     */
    protected String getName(){
        return name;
    }

    /**
     * Sets the name of this User
     *
     * @param name The new name of this User
     */
    protected void setName(String name){
        this.name = name;
    }

    /**
     * Gets the username of this User
     *
     * @return The username of this User
     */
    protected String getUsername(){
        return username;
    }

    /**
     * Sets the username of this User
     *
     * @param username The new username of this User
     */
    protected void setUsername(String username){
        this.username = username;
    }

    /**
     * Gets the password of this User
     *
     * @return The password of this User
     */
    protected String getPassword(){
        return password;
    }

    /**
     * Sets the password of this User
     *
     * @param password The new password of this User
     */
    protected void setPassword(String password){
        this.password = password;
    }

    /**
     * Returns whether this user has admin capabilities
     *
     * @return Whether the User is an admin
     */
    protected boolean isAdmin() {
        return true;
    }

}