package tradecomponent;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Manages the creation and editing of meetings of trades.
 */
public class MeetingManager implements Serializable {
    private Map<String, Meeting> allMeetings;
    private Map<String, Meeting> ongoingMeetings;
    private Map<String, Meeting> completedMeetings;
    private Map<String, List<String>> tradeToMeetings;
    private int limitOfEdits;
    private final AtomicInteger counter = new AtomicInteger();

    public MeetingManager() {
        this.limitOfEdits = 3;
        this.allMeetings = new HashMap<>();
        this.tradeToMeetings = new HashMap<>();
        this.ongoingMeetings = new HashMap<>();
        this.completedMeetings = new HashMap<>();
    }

    /**
     * Gets the limit of edits that users can suggest before the trade is cancelled
     *
     * @return the limit of edits to the meeting time/place
     */
    public int getLimitEdits() {
        return this.limitOfEdits;
    }

    /**
     * Change the limit of edit that users can suggest before the trade is cancelled
     *
     * @param newLimit  new limit for the edits allowed
     */
    public void changeLimitEdits(int newLimit) {
        this.limitOfEdits = newLimit;
    }

    /**
     * Changes a meeting according to the proposed location, time, and stores the username of the User that made
     * the last edit
     * @param meetingId The Id of the meeting
     * @param location  The location
     * @param time  The time of the meeting
     * @param username  The username of the User
     */
    public void changeMeeting(String meetingId, String location, Date time, String username) {
        Meeting meeting = this.ongoingMeetings.get(meetingId);
        meeting.setLocation(location);
        meeting.setTime(time);
        meeting.incrementNumOfEdits();
        meeting.setLastEditUser(username);
    }

    public void addMeetingToTrade(String tradeId, String meetingId) {
        if (tradeToMeetings.containsKey(tradeId)) {
            tradeToMeetings.get(tradeId).add(meetingId);
        } else {
            List<String> meetingList = new ArrayList<>();
            meetingList.add(meetingId);
            tradeToMeetings.put(tradeId, meetingList);
        }
    }

    /**
     * Returns a new meeting with the proposed location, time, and the User who made the last edit
     * @param location  The location of the meeting
     * @param time  The time of the meeting
     * @param username  The username of the User
     * @return  The new meeting with the location, time, and username
     */
    public String createMeeting(String location, Date time, String username) {
        String meetingId = String.valueOf(counter.getAndIncrement());
        Meeting meeting = new Meeting(location, time, username, meetingId);
        allMeetings.put(meetingId, meeting);
        ongoingMeetings.put(meetingId, meeting);
        return meetingId;
    }

    /**
     * Sets meeting to confirm and the last User who edited changes to no one (i.e., empty string)
     * @param meetingId   id of the meeting
     */
    public void confirmAgreement(String meetingId) {
        Meeting meeting = ongoingMeetings.get(meetingId);
        meeting.changeIsConfirmed();
        meeting.setLastEditUser("");
    }

    /**
     * If there the number of confirmations is 2, this means that the meeting is confirmed and means that the meeting
     * will take place and thus, it is completed
     * @param meetingId   id of the meeting
     * @param username  The username of another User
     */
    public void meetingHappened(String meetingId, String username) {
        Meeting meeting = ongoingMeetings.get(meetingId);
        meeting.incrementNumConfirmations();
        meeting.setLastEditUser(username);
        if (meeting.getNumConfirmations() == 2) {
            meeting.changeIsCompleted();
            ongoingMeetings.remove(meetingId);
            completedMeetings.put(meetingId, meeting);
        }
    }

    /**
     * Return whether a meeting is incomplete. An incomplete meeting is a meeting that the user agreed on the meeting
     * details (time, location), the meeting time has passed, but at least one of the users have not confirmed the
     * meeting happened
     * @return true if the meeting is incomplete and false if the meeting is completed.
     */
    public List<String> getTradesIncompleteMeetings(List<String> trades) {
        List<String> incomplete = new ArrayList<>();
        for (String tradeId : trades) {
            for (String meetingId : tradeToMeetings.get(tradeId)) {
                if (isIncompleteMeeting(meetingId)) {
                    incomplete.add(tradeId);
                }
            }
        } return incomplete;
    }

    private boolean isIncompleteMeeting(String meetingId){
        Meeting meeting = allMeetings.get(meetingId);
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
     * Returns true iff True if the number of edits is equal to or greater than the threshold for the number of edits
     * allowed and false if the number of edits is less than the threshold for edits
     * @param meetingId   id of the meeting
     * @return  True if the number of edits is equal to or greater than the threshold for the number of edits allowed
     * and false if the number of edits is less than the threshold for edits
     */
    public boolean attainedThresholdEdits(String meetingId){
        return ongoingMeetings.get(meetingId).getNumOfEdits() >= limitOfEdits;
    }

    /**
     * Returns a string which includes, the status of the meeting (i.e., whether it has been complete or its pending
     * for confirmation or still need to arrange), the location, the time, the number of edits made, the number of
     * confirmations, and the User who last edited
     * @return A string that includes the meeting status, location, time, number of edits made, number of confirmation,
     * and the User who last edited
     */
    public String getMeetingsInfo(String tradeId) {
        StringBuilder meetingDetails = new StringBuilder();
        for (String meetingId : tradeToMeetings.get(tradeId)) {
            meetingDetails.append("Meeting: \n").append(allMeetings.get(meetingId).toString()).append("\n");
        }
        return meetingDetails.toString();
    }

    /**
     * Returns the status of the meeting
     * @param meetingId   id of the meeting
     * @return  2 if the meeting is completed, 1 if the meeting is confirmed but has not been completed yet, and
     * 0 otherwise (meeting time has not been agreed upon)
     */
    public int getMeetingStatus(String meetingId){
        Meeting meeting = allMeetings.get(meetingId);
        if (meeting.getIsCompleted()){
            return 2;
        } else if (meeting.getIsConfirmed()){
            return 1;
        } else{
            return 0;
        }
    }

    public boolean isConfirmed(String meetingId) {
        Meeting meeting = allMeetings.get(meetingId);
        return meeting.getIsConfirmed();
    }

    public Date getMeetingTime(String meetingId){
        Meeting meeting = allMeetings.get(meetingId);
        return meeting.getTime();
    }

    public String getMeetingLocation(String meetingId) {
        Meeting meeting = allMeetings.get(meetingId);
        return meeting.getLocation();
    }

    public List<String> getMeetingsOfTrade(String tradeId) {
        if (tradeToMeetings.containsKey(tradeId)) {
            return tradeToMeetings.get(tradeId);
        } else { return new ArrayList<>(); }
    }

    public boolean canChangeMeeting(String tradeId, String username) {
        if (tradeToMeetings.get(tradeId) == null) {
            return false;
        } else {
            String meetingId = tradeToMeetings.get(tradeId).get(tradeToMeetings.get(tradeId).size() - 1);
            Meeting meeting = allMeetings.get(meetingId);
            return (!meeting.getLastEditUser().equals(username));
        }
    }

    private List<String> getTradesPastDays(int numDays, List<String> trades) {
        List<String> pastTrades = new ArrayList<>();
        for (String tradeId : trades) {
            Calendar compare = Calendar.getInstance();
            compare.add(Calendar.DATE, -7);
            Date comparisonDate = compare.getTime();
            if (getMeetingTime(tradeToMeetings.get(tradeId).get(tradeToMeetings.get(tradeId).size() - 1)).after(comparisonDate)) {
                pastTrades.add(tradeId);
            }
        }
        return pastTrades;
    }

    public List<String> getToCheckTrades(List<String> trades, String type) {
        List<String> toCheck = new ArrayList<>();
        if (type.equals("proposed")) {
            for (String tradeId : trades) {
                String meetingId = tradeToMeetings.get(tradeId).get(tradeToMeetings.get(tradeId).size() - 1);
                if (getMeetingStatus(meetingId) != 1) {
                    toCheck.add(tradeId); }
            }
        } else { // to confirm
            for (String tradeId : trades) {
                String meetingId = tradeToMeetings.get(tradeId).get(tradeToMeetings.get(tradeId).size() - 1);
                if (getMeetingStatus(meetingId) == 1) {
                    Calendar cal = Calendar.getInstance();
                    Date newDate = cal.getTime();
                    if (getMeetingTime(meetingId).before(newDate)) {
                        toCheck.add(tradeId); }
                }
            }
        } return toCheck;
    }

    public void cancelMeetings(String tradeId) {
        allMeetings.remove(tradeId);
        ongoingMeetings.remove(tradeId);
        completedMeetings.remove(tradeId);
        tradeToMeetings.remove(tradeId);
    }
}