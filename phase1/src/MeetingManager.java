import java.io.Serializable;
import java.util.Date;

public class MeetingManager implements Serializable {


    public void changeMeeting(Meeting meeting, String location, Date time, String username) {
        meeting.setLocation(location);
        meeting.setTime(time);
        meeting.incrementNumOfEdits();
        meeting.setLastEditUser(username);
    }
    // come back to this

    public Meeting createMeeting(String location, Date time, String username){
        return new Meeting(location, time, username);
    }

    /**
     * agree with the meeting time, place that the other user suggested (only need one user to confirm since we can
     * assume that the one suggesting the meeting details agrees.
     * @param meeting the meeting that the user is agreeing with the time and location.
     */
    public void confirmAgreement(Meeting meeting) {
        meeting.changeIsConfirmed();
        meeting.setLastEditUser(null);
    }

    /**
     * confirm that meeting happened in real life.
     * @param meeting meeting that happened in real life
     */
    public void meetingHappened(Meeting meeting, String username) {
        meeting.incrementNumConfirmations();
        meeting.setLastEditUser(username);
        if (meeting.getNumConfirmations() == 2) {
            meeting.changeIsCompleted();
        }
    }

    /**
     * Return whether a meeting is incomplete. An incomplete meeting is a meeting that the user agreed on the meeting
     * details (time, location), the meeting time has passed, but at least one of the users have not confirmed the
     * meeting happened.
     * @param meeting meeting that is incomplete
     * @return true if the meeting is incomplete and false if the meeting is completed.
     */
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

    public boolean attainedThresholdEdits(Meeting meeting, int numOfEditThreshold){
        return meeting.getNumOfEdits() >= numOfEditThreshold;
    }

    public String getMeetingsInfo(Meeting meeting){
        return meeting.toString();
    }
}