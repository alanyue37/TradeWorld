package tradecomponent;

import java.io.Serializable;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents a meeting to exchange item(s) in a trade
 */
class Meeting implements Serializable {
    private String location;
    private Date meetingTime;
    private int numOfEdits;
    private int numConfirmations; // seen other in real life (0, 1 or 2)
    private boolean IsCompleted; // real life meeting is completed
    private boolean IsConfirmed; // time/place is confirmed by the other user
    private String lastEditUser;
    private final String meetingId;

    /**
     * Initiates a new meeting
     * @param location  The location of the meeting
     * @param time  The time of the meeting
     * @param username  The username of the User who suggested the meeting
     */
    protected Meeting(String location, Date time, String username, String meetingId){
        this.location = location;
        this.meetingTime = time;
        this.numOfEdits = 0;
        this.numConfirmations = 0;
        this.IsConfirmed = false;
        this.IsCompleted = false;
        this.lastEditUser = username;
        this.meetingId = meetingId;
    }

    /**
     * Returns the location of the meeting
     * @return The location of the meeting
     */
    protected String getLocation(){
        return this.location;
    }

    /**
     * Returns the User who last edited and made changes
     * @return The User who last edited and made changes
     */
    protected String getLastEditUser(){
        return this.lastEditUser;
    }

    /**
     * Sets the User who last edited
     * @param username  The username of the User
     */
    protected void setLastEditUser(String username){
        this.lastEditUser = username;
    }

    /**
     * Sets a new location for the meeting
     * @param newLocation   The new location of the meeting
     */
    protected void setLocation(String newLocation){
        this.location = newLocation;
    }

    /**
     * Gets the meeting time
     * @return  The meeting time
     */
    protected Date getTime(){
        return this.meetingTime;
    }

    /**
     * Sets the new meeting time
     * @param newTime   The new meeting time
     */
    protected void setTime(Date newTime){
        this.meetingTime = newTime;
    }

    /**
     * Returns the number of edits made so far
     * @return  The number of edits made so far
     */
    protected int getNumOfEdits(){
        return numOfEdits;
    }

    /**
     * Increments the number of edits when a User makes an edit to change the meeting by 1
     */
    protected void incrementNumOfEdits(){
        this.numOfEdits += 1;
    }

    /**
     * Returns the number of confirmations for the meeting
     * @return  The number of confirmations
     */
    protected int getNumConfirmations(){
        return this.numConfirmations;
    }

    /**
     * Increments the number of confirmations by 1
     */
    protected void incrementNumConfirmations(){
        this.numConfirmations += 1;
    }

    /**
     * Sets the isComplete to true when the meeting is complete
     */
    protected void changeIsCompleted(){
        this.IsCompleted = true;
    }

    /**
     * Returns true iff the meeting has been confirmed, false otherwise
     * @return Whether the meeting has been confirmed
     */
    protected boolean getIsConfirmed(){
        return this.IsConfirmed;
    }

    /**
     * Sets isConfirmed to true when the meeting is confirmed
     */
    protected void changeIsConfirmed(){
        this.IsConfirmed = true;
    }

    /**
     * Returns a JSON object of the meeting which includes: the status of the meeting
     * (i.e., whether it has been complete or is pending for confirmation or still need to arrange),
     * the location, the time, the number of edits made, the number of confirmations, and the User who last edited
     * @return  String representation of the meeting
     */
    public JSONObject getMeetingInfo() throws JSONException {
        JSONObject info = new JSONObject();
        info.put("Meeting ID", meetingId);
        if (this.IsCompleted) {
            info.put("Status", "completed");
        } else if (this.IsConfirmed) {
            info.put("Status", "meeting details agreed; waiting for meeting to happen");
        } else {
            info.put("Status", "need to agree on meeting details");
        }
        info.put("Location", location);
        info.put("Time", meetingTime.toString());
        info.put ("Number of Edits", numOfEdits);
        info.put("Number of Confirmations", String.valueOf(numConfirmations));
        info.put("Last user who modified the meeting", lastEditUser);
        return info;
    }
}



