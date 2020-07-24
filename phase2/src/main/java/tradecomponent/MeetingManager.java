package tradecomponent;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Manages the creation and editing of meetings of trades.
 */
public class MeetingManager implements Serializable {
    private Map<String, Meeting> ongoingMeetings;
    private Map<String, Meeting> completedMeetings;
    private Map<String, List<String>> tradeToMeetings;
    private int limitOfEdits;
    private final AtomicInteger counter = new AtomicInteger();

    public MeetingManager() {
        this.limitOfEdits = 3;
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
     * @param newLimit new limit for the edits allowed
     */
    public void changeLimitEdits(int newLimit) {
        this.limitOfEdits = newLimit;
    }

    /**
     * Changes a meeting according to the proposed location, time, and stores the username of the User that made
     * the last edit
     *
     * @param meetingId The Id of the meeting
     * @param location  The location
     * @param time      The time of the meeting
     * @param username  The username of the User
     */
    public void changeMeeting(String meetingId, String location, Date time, String username) {
        Meeting meeting = this.ongoingMeetings.get(meetingId);
        meeting.setLocation(location);
        meeting.setTime(time);
        meeting.incrementNumOfEdits();
        meeting.setLastEditUser(username);
    }

    /**
     * Returns a new meeting with the proposed location, time, and the User who made the last edit
     *
     * @param location The location of the meeting
     * @param time     The time of the meeting
     * @param username The username of the User
     * @param tradeId  id of the trade to add the meeting
     * @return The new meeting with the location, time, and username
     */
    public String createMeeting(String location, Date time, String username, String tradeId) {
        String meetingId = String.valueOf(counter.getAndIncrement());
        Meeting meeting = new Meeting(location, time, username, meetingId);
        ongoingMeetings.put(meetingId, meeting);
        if (this.tradeToMeetings.containsKey(tradeId)) {
            tradeToMeetings.get(tradeId).add(meetingId);
        } else {
            List<String> meetings = new ArrayList<>();
            meetings.add(meetingId);
            tradeToMeetings.put(tradeId, meetings);
        }
        return meetingId;
    }

    /**
     * Sets meeting to confirm and the last User who edited changes to no one (i.e., empty string)
     *
     * @param meetingId id of the meeting
     */
    public void confirmAgreement(String meetingId) {
        Meeting meeting = ongoingMeetings.get(meetingId);
        meeting.changeIsConfirmed();
        meeting.setLastEditUser("");
    }

    /**
     * If there the number of confirmations is 2, this means that the meeting is confirmed and means that the meeting
     * will take place and thus, it is completed
     *
     * @param meetingId id of the meeting
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
     *
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
        }
        return incomplete;
    }

    private boolean isIncompleteMeeting(String meetingId) {
        if (!this.ongoingMeetings.containsKey(meetingId)) {
            return false;
        }
        Meeting meeting = ongoingMeetings.get(meetingId);
        Date today = new Date();
        if (meeting.getIsConfirmed()) {
            return meeting.getTime().before(today);
        }
        return false;
    }

    /**
     * Returns true iff True if the number of edits is equal to or greater than the threshold for the number of edits
     * allowed and false if the number of edits is less than the threshold for edits
     *
     * @param meetingId id of the meeting
     * @return True if the number of edits is equal to or greater than the threshold for the number of edits allowed
     * and false if the number of edits is less than the threshold for edits
     */
    public boolean attainedThresholdEdits(String meetingId) {
        return ongoingMeetings.get(meetingId).getNumOfEdits() >= (limitOfEdits * 2);
    }

    /**
     * Returns a string which includes, the status of the meeting (i.e., whether it has been complete or its pending
     * for confirmation or still need to arrange), the location, the time, the number of edits made, the number of
     * confirmations, and the User who last edited
     *
     * @return A string that includes the meeting status, location, time, number of edits made, number of confirmation,
     * and the User who last edited
     */
    public String getMeetingsInfo(String tradeId) {
        StringBuilder meetingDetails = new StringBuilder();
        for (String meetingId : tradeToMeetings.get(tradeId)) {
            Meeting meeting = getMeeting(meetingId);
            meetingDetails.append("Meeting: \n").append(meeting.toString()).append("\n");
        }
        return meetingDetails.toString();
    }


    public Date getLastMeetingTime(String tradeId) {
        Meeting meeting = getMeeting(this.tradeToMeetings.get(tradeId).get(tradeToMeetings.get(tradeId).size()-1));
        return meeting.getTime();
    }

    public String getLastMeetingLocation(String tradeId) {
        Meeting meeting = getMeeting(this.tradeToMeetings.get(tradeId).get(tradeToMeetings.get(tradeId).size() - 1));
        return meeting.getLocation();
    }

    private Meeting getMeeting(String meetingId){
        Meeting meeting;
        if (this.ongoingMeetings.containsKey(meetingId)) {
            meeting = ongoingMeetings.get(meetingId);
        } else {
            meeting = completedMeetings.get(meetingId);
        }
        return meeting;
    }

    public boolean canChangeMeeting(String tradeId, String username) {
        if (tradeToMeetings.get(tradeId) == null) {
            return false;
        } else {
            String meetingId = tradeToMeetings.get(tradeId).get(tradeToMeetings.get(tradeId).size() - 1);
            Meeting meeting = getMeeting(meetingId);
            return (!meeting.getLastEditUser().equals(username));
        }
    }

    public List<String> getMeetingsPastDays(List<String> trades) {
        List<String> pastTrades = new ArrayList<>();
        for (String tradeId : trades) {
            Calendar compare = Calendar.getInstance();
            compare.add(Calendar.DATE, -7);
            Date comparisonDate = compare.getTime();
            if (getLastMeetingTime(tradeId).after(comparisonDate)) {
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
                if (!this.ongoingMeetings.get(meetingId).getIsConfirmed()) {
                    toCheck.add(tradeId); }
            }
        } else { // to confirm
            for (String tradeId : trades) {
                String meetingId = tradeToMeetings.get(tradeId).get(tradeToMeetings.get(tradeId).size() - 1);
                if (this.ongoingMeetings.get(meetingId).getIsConfirmed()) {
                    Calendar cal = Calendar.getInstance();
                    Date newDate = cal.getTime();
                    if (getLastMeetingTime(tradeId).before(newDate)) {
                        toCheck.add(tradeId); }
                }
            }
        } return toCheck;
    }

    public void cancelMeetingsOfTrade(String tradeId) {
        List<String> meetingIds = tradeToMeetings.get(tradeId);
        for (String id: meetingIds){
            if (ongoingMeetings.containsKey(id)){
                ongoingMeetings.remove(id);
            } else {
                completedMeetings.remove(id);
            }
        }
        tradeToMeetings.remove(tradeId);
    }

    public boolean tradeMeetingsCompleted(String tradeId){
        for (String id: this.tradeToMeetings.get(tradeId)){
            if (!this.completedMeetings.containsKey(id)){
                return false;
            }
        }
        return true;
    }


    public List<String> sortedMeeting(List<String> tradeIds){
        List<String> sorted = new ArrayList<>();
        if (tradeIds.size() == 0){
            return sorted;
        }
        sorted.add(tradeIds.get(0));
        for (int i = 1; i < tradeIds.size(); i++) {
            int ii = 0;
            while (ii < sorted.size() && getLastMeetingTime(sorted.get(ii)).before(getLastMeetingTime(tradeIds.get(i)))) {
                ii++;
            }
            sorted.add(ii, tradeIds.get(i));
        }
        return sorted;
    }
    // goes from oldest (index 0) to newest
}