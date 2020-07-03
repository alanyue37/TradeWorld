import java.util.Date;

public class MeetingManager {

    private int limitOfEdits;

    public void changeMeeting(Meeting meeting, String location, Date time) {
        meeting.setLocation(location);
        meeting.setTime(time);
        meeting.incrementNumOfEdits();
    }
    // come back to this

    public void confirmMeeting(Meeting meeting) {
        meeting.changeIsConfirmed();
    }

    public void confirmTrade(Meeting meeting) {
        meeting.incrementNumConfirmations();

        if (meeting.getNumConfirmations() == 2) {
            meeting.changeIsCompleted();
        }
    }

    // public void setLimitOfEdits(int
}