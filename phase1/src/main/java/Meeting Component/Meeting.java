import java.io.Serializable;
import java.util.Date;

class Meeting implements Serializable {
    private String location;
    private Date meetingTime;    //ask later if string or time
    private int numOfEdits;
    private int numConfirmations; //seen other in real life (0, 1 or 2)
    private boolean IsCompleted; //real life meeting is completed
    private boolean IsConfirmed; //time/place is confirmed by the other user
    private String lastEditUser;

    protected Meeting(String location, Date time, String username){
        this.location = location;
        this.meetingTime = time;
        this.numOfEdits = 0;
        this.numConfirmations = 0;
        this.IsConfirmed = false;
        this.IsCompleted = false;
        this.lastEditUser = username;
    }

    protected String getLocation(){
        return this.location;
    }

    protected String getLastEditUser(){
        return this.lastEditUser;
    }

    protected void setLastEditUser(String username){
        this.lastEditUser = username;
    }

    protected void setLocation(String newLocation){
        this.location = newLocation;
    }

    protected Date getTime(){
        return this.meetingTime;
    }

    protected void setTime(Date newTime){
        this.meetingTime = newTime;
    }

    protected int getNumOfEdits(){
        return numOfEdits;
    }

    protected void incrementNumOfEdits(){
        this.numOfEdits += 1;
    }

    protected int getNumConfirmations(){
        return this.numConfirmations;
    }

    protected void incrementNumConfirmations(){
        this.numConfirmations += 1;
    }

    protected boolean getIsCompleted(){
        return this.IsCompleted;
    }

    protected void changeIsCompleted(){
        this.IsCompleted = true;
    }

    protected boolean getIsConfirmed(){
        return this.IsConfirmed;
    }

    protected void changeIsConfirmed(){
        this.IsConfirmed = true;
    }

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



