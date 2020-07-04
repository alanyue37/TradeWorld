import java.util.Date;

public class MeetingManager {

    public void changeMeeting(Meeting meeting, String location, Date time) {
        meeting.setLocation(location);
        meeting.setTime(time);
        meeting.incrementNumOfEdits();
    }
    // come back to this

    public void confirmAgreement(Meeting meeting) {
        meeting.changeIsConfirmed();
    }

    public void meetingHappened(Meeting meeting) {
        meeting.incrementNumConfirmations();

        if (meeting.getNumConfirmations() == 2) {
            meeting.changeIsCompleted();
        }
    }

    public boolean isIncompleteMeeting(Meeting meeting){
        Date today = new Date();
        if (meeting.getIsCompleted()){
            return false;
        }
        if (meeting.getIsConfirmed()){
            return meeting.getTime().before(today);
        }
        return false;
    }

    public boolean getMeetingCompleted(Meeting meeting){
        return meeting.getIsCompleted();
    }

    public boolean getExchangeConfirmed(Meeting meeting){
        return meeting.getIsConfirmed();
    }

    public Date getConfirmedMeetingTime(Meeting meeting){
        if (meeting.getIsConfirmed()){
            return meeting.getTime();
        }
        return null;
    }


    // public void setLimitOfEdits(int
}