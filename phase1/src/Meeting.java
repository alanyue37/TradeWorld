public class Meeting {
    private String location;
    private String time;    //ask later if string or time
    private int numOfEdits;
    private int numConfirmations; //seen other in real life (0, 1 or 2)
    private boolean IsCompleted; //real life meeting is completed
    private boolean IsConfirmed; //time/place is confirmed by the other user

    public Meeting(String location, String time){
        this.location = location;
        this.time = time;
    }

    public String getLocation(){
        return this.location;
    }

    public void setLocation(String newLocation){
        this.location = newLocation;
    }

    public String getTime(){
        return this.time;
    }

    public void setTime(String newTime){
        this.time = newTime;
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



