import java.io.Serializable;
import java.util.Date;

public class Meeting implements Serializable {
    private String location;
    private Date meetingTime;    //ask later if string or time
    private int numOfEdits;
    private int numConfirmations; //seen other in real life (0, 1 or 2)
    private boolean IsCompleted; //real life meeting is completed
    private boolean IsConfirmed; //time/place is confirmed by the other user
    private String lastEditUser;

    /**
     * Initiates a new meeting
     * @param location  The location of the meeting
     * @param time  The time of the meeting
     * @param username  The username of the User
     */
    public Meeting(String location, Date time, String username){
        this.location = location;
        this.meetingTime = time;
        this.numOfEdits = 0;
        this.numConfirmations = 0;
        this.IsConfirmed = false;
        this.IsCompleted = false;
        this.lastEditUser = username;
    }

    /**
     * Returns the location of the meeting
     * @return The location of the meeting
     */
    public String getLocation(){
        return this.location;
    }

    /**
     * Returns the User who last edited and made changes
     * @return The User who last edited and made changes
     */
    public String getLastEditUser(){
        return this.lastEditUser;
    }

    /**
     * Sets the User who last edited
     * @param username  The username of the User
     */
    public void setLastEditUser(String username){
        this.lastEditUser = username;
    }

    /**
     * Sets a new location for the meeting
     * @param newLocation   The new location of the meeting
     */
    public void setLocation(String newLocation){
        this.location = newLocation;
    }

    /**
     * Gets the meeting time
     * @return  The meeting time
     */
    public Date getTime(){
        return this.meetingTime;
    }

    /**
     * Sets the new meeting time
     * @param newTime   The new meeting time
     */
    public void setTime(Date newTime){
        this.meetingTime = newTime;
    }

    /**
     * Returns the number of edits made so far
     * @return  The number of edits made so far
     */
    public int getNumOfEdits(){
        return numOfEdits;
    }

    /**
     * Increments the number of edits when a User makes an edit to change the meeting by 1
     */
    public void incrementNumOfEdits(){
        this.numOfEdits += 1;
    }

    /**
     * Returns the number of confirmations for the meeting
     * @return  The number of confirmations
     */
    public int getNumConfirmations(){
        return this.numConfirmations;
    }

    /**
     * Increments the number of confirmations by 1
     */
    public void incrementNumConfirmations(){
        this.numConfirmations += 1;
    }

    /**
     * Returns true iff the meeting is completed, false otherwise
     * @return  True iff the meeting is completed, false otherwise
     */
    public boolean getIsCompleted(){
        return this.IsCompleted;
    }

    /**
     * Sets the isComplete to true when the meeting is complete
     */
    public void changeIsCompleted(){
        this.IsCompleted = true;
    }

    /**
     * Returns true iff the meeting has been confirmed, false otherwise
     * @return Whether the meeting has been confirmed
     */
    public boolean getIsConfirmed(){
        return this.IsConfirmed;
    }

    /**
     * Sets isConfirmed to true when the meeting is confirmed
     */
    public void changeIsConfirmed(){
        this.IsConfirmed = true;
    }

    /**
     * Returns a string which includes, the status of the meeting (i.e., whether it has been complete or its pending
     * for confirmation or still need to arrange), the location, the time, the number of edits made,
     * the number of confirmations, and the User who last edited
     * @return  Tells the status of the meeting
     */
    public String toString(){
        String status;
        if (this.IsCompleted) {
            status = "completed";
        } else if (this.IsConfirmed){
            status = "meeting details agreed, waiting for meeting to happen";
        } else{
            status = "need to agree on meeting details";
        }
        return "Status: " + status + "\nLocation: " + this.location + "\nTime: " + this.meetingTime
                + "\nNumber of Edits: " + this.numOfEdits + "\nNumber of Confirmations: " + this.numConfirmations
                + "\nLast user who modified the meeting: " + this.lastEditUser;
    }
}



