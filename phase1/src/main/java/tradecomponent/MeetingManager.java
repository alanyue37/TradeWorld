package tradecomponent;

import java.io.Serializable;
import java.util.Date;

/**
 * Manages the creation and editing of meetings of trades.
 */
public class MeetingManager implements Serializable {

    /**
     * Changes a meeting according to the proposed location, time, and stores the username of the User that made
     * the last edit
     * @param meeting   The meeting
     * @param location  The location
     * @param time  The time of the meeting
     * @param username  The username of the User
     */
    public void changeMeeting(Meeting meeting, String location, Date time, String username) {
        meeting.setLocation(location);
        meeting.setTime(time);
        meeting.incrementNumOfEdits();
        meeting.setLastEditUser(username);
    }

    /**
     * Returns a new meeting with the proposed location, time, and the User who made the last edit
     * @param location  The location of the meeting
     * @param time  The time of the meeting
     * @param username  The username of the User
     * @return  The new meeting with the location, time, and username
     */
    public Meeting createMeeting(String location, Date time, String username){
        return new Meeting(location, time, username);
    }

    /**
     * Sets meeting to confirm and the last User who edited changes to no one (i.e., empty string)
     * @param meeting   The meeting which includes the location, time, and the username of the User that made the
     *                  last edit
     */
    public void confirmAgreement(Meeting meeting) {
        meeting.changeIsConfirmed();
        meeting.setLastEditUser("");
    }

    /**
     * If there the number of confirmations is 2, this means that the meeting is confirmed and means that the meeting
     * will take place and thus, it is completed
     * @param meeting   The meeting that includes the location, time, and the username of the User that made the last
     *                  edit
     * @param username  The username of another User
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
     * meeting happened
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

    /**
     * Returns the meeting time if the meeting has been confirmed, null otherwise
     * @param meeting    The meeting which includes the location, time, and the username of the User
     * @return  The meeting time if the meeting has been confirmed
     */
    public Date getConfirmedMeetingTime(Meeting meeting){
        if (meeting.getIsConfirmed()){
            return meeting.getTime();
        }
        return null;
    }

    /**
     * Returns true iff True if the number of edits is equal to or greater than the threshold for the number of edits
     * allowed and false if the number of edits is less than the threshold for edits
     * @param meeting   The meeting which includes the location, time, and the username of the User
     * @param numOfEditThreshold    The threshold for how many edits can be made before the screen prints cancelled
     *                              to the screen
     * @return  True if the number of edits is equal to or greater than the threshold for the number of edits allowed
     * and false if the number of edits is less than the threshold for edits
     */
    public boolean attainedThresholdEdits(Meeting meeting, int numOfEditThreshold){
        return meeting.getNumOfEdits() >= numOfEditThreshold;
    }

    /**
     * Returns a string which includes, the status of the meeting (i.e., whether it has been complete or its pending
     * for confirmation or still need to arrange), the location, the time, the number of edits made, the number of
     * confirmations, and the User who last edited
     * @param meeting   The meeting which includes the location, time, and the username of the User
     * @return A string that includes the meeting status, location, time, number of edits made, number of confirmation,
     * and the User who last edited
     */
    public String getMeetingsInfo(Meeting meeting){
        return meeting.toString();
    }

    /**
     * Returns a string of the meeting location
     * @param meeting The meeting which includes the location, time, and the username of the User
     * @return a string representing the location of the meeting
     */
    public String getLastLocation(Meeting meeting){
        return meeting.getLocation();
    }

    /**
     * Returns true iff the last User is not the same and can make the edit to the meeting and false if this User
     * made the last edit and cannot make another edit before getting a response from the other User
     * @param meeting   The meeting which includes the location, time, and the username of the User
     * @param username  The username of the User
     * @return True iff the last User is not the same and can make the edit to the meeting, false otherwise
     */
    public boolean canEditMeeting(Meeting meeting, String username){
        return (!meeting.getLastEditUser().equals(username));
    }

    /**
     * Returns the status of the meeting
     * @param meeting   The meeting which includes the location, time, and the username of the User
     * @return  2 if the meeting is completed, 1 if the meeting is confirmed but has not been completed yet, and
     * 0 otherwise (meeting time has not been agreed upon)
     */
    public int getMeetingStatus(Meeting meeting){
        if (meeting.getIsCompleted()){
            return 2;
        } else if (meeting.getIsConfirmed()){
            return 1;
        } else{
            return 0;
        }
    }

}