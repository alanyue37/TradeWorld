import java.util.Date;

public class Meeting {
    private String location;
    private Date meetingTime;    //ask later if string or time
    private int numOfEdits;
    private int numConfirmations; //seen other in real life (0, 1 or 2)
    private boolean IsCompleted; //real life meeting is completed
    private boolean IsConfirmed; //time/place is confirmed by the other user

    public Meeting(String location, Date time){
        this.location = location;
        this.meetingTime = time;
    }

    public String getLocation(){
        return this.location;
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


}



