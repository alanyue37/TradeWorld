import java.io.Serializable;

public class User implements Serializable {

    private String name;
    private String username;
    private String password;

    /**
     * Instantiates a new User
     */
    public User(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }


    /**
     * Gets the name of this User
     *
     * @return The name of this User
     */
    public String getName(){
        return name;
    }

    /**
     * Sets the name of this User
     *
     * @param name The new name of this User
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Gets the username of this User
     *
     * @return The username of this User
     */
    public String getUsername(){
        return username;
    }

    /**
     * Sets the username of this User
     *
     * @param username The new username of this User
     */
    public void setUsername(String username){
        this.username = username;
    }

    /**
     * Gets the password of this User
     *
     * @return The password of this User
     */
    public String getPassword(){
        return password;
    }

    /**
     * Sets the password of this User
     *
     * @param password The new password of this User
     */
    public void setPassword(String password){
        this.password = password;
    }

    /**
     * Returns whether this user has admin capabilities
     *
     * @return Whether the User is an admin
     */
    public boolean isAdmin() {
        return true;
    }

    /**
     * Returns a string representation of this user
     *
     * @return A string representation of this user
     */
    public String toString() {
        return name + "\n" + username;
    }
}