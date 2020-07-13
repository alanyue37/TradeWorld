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

    public Meeting(String location, Date time, String username){
        this.location = location;
        this.meetingTime = time;
        this.numOfEdits = 0;
        this.numConfirmations = 0;
        this.IsConfirmed = false;
        this.IsCompleted = false;
        this.lastEditUser = username;
    }

    public String getLocation(){
        return this.location;
    }

    public String getLastEditUser(){
        return this.lastEditUser;
    }

    public void setLastEditUser(String username){
        this.lastEditUser = username;
    }

    public void setLocation(String newLocation){
        this.location = newLocation;
    }

    public Date getTime(){
        return this.meetingTime;
    }

    public void setTime(Date newTime){
        this.meetingTime = newTime;
    }

    public int getNumOfEdits(){
        return numOfEdits;
    }

    public void incrementNumOfEdits(){
        this.numOfEdits ++;
    }

    public int getNumConfirmations(){
        return this.numConfirmations;
    }

    public void incrementNumConfirmations(){
        this.numConfirmations ++;
    }

    public boolean getIsCompleted(){
        return this.IsCompleted;
    }

    public void changeIsCompleted(){
        this.IsCompleted = true;
    }

    public boolean getIsConfirmed(){
        return this.IsConfirmed;
    }

    public void changeIsConfirmed(){
        this.IsConfirmed = true;
    }

    public String toString(){
        String status;
        if (this.IsCompleted) {
            status = "completed";
        } else if (this.IsConfirmed){
            status = "meeting details agreed, waiting to confirm";
        } else{
            status = "need to agree on meeting details";
        }
        return "Status" + status + "\nLocation" + this.location + "\nTime" + this.meetingTime
                + "\nNumOfEdits" + this.numOfEdits + "\nnumConfirmation" + this.numConfirmations
                + "\nLast user who modified the meeting" + this.lastEditUser;
    }

}



