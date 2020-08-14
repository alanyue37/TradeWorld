package tradecomponent;

import org.json.JSONException;
import org.json.JSONObject;
import tradegateway.ObservableDataModel;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Manages the creation and editing of meetings of trades.
 */
public class MeetingManager implements Serializable {
    private final ObservableDataModel observableDataModel;
    private Map<String, Meeting> ongoingMeetings;
    private Map<String, Meeting> completedMeetings;
    private Map<String, List<String>> tradeToMeetings;
    private int limitOfEdits;
    private final AtomicInteger counter = new AtomicInteger();

    /**
     * Constructor for TradeManager.
     */
    public MeetingManager(ObservableDataModel observableDataModel) {
        this.limitOfEdits = 3;
        this.tradeToMeetings = new HashMap<>();
        this.ongoingMeetings = new HashMap<>();
        this.completedMeetings = new HashMap<>();
        this.observableDataModel = observableDataModel;
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
        observableDataModel.setChanged();
    }

    /**
     * Changes a meeting according to the proposed location, time, and stores the username of the User that made
     * the last edit
     *
     * @param meetingId The ID of the meeting
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
        observableDataModel.setChanged();
    }

    /**
     * Returns a new meeting with the proposed location, time, and the User who made the last edit
     *
     * @param location The location of the meeting
     * @param time     The time of the meeting
     * @param username The username of the User
     * @param tradeId  ID of the trade to add the meeting
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
        observableDataModel.setChanged();
        return meetingId;
    }

    /**
     * Sets meeting to confirm and the last User who edited changes to no one (i.e., empty string)
     *
     * @param meetingId ID of the meeting
     */
    public void confirmAgreement(String meetingId) {
        Meeting meeting = ongoingMeetings.get(meetingId);
        meeting.changeIsConfirmed();
        meeting.setLastEditUser("");
        observableDataModel.setChanged();
    }

    /**
     * If there the number of confirmations is 2, this means that the meeting is confirmed and means that the meeting
     * will take place and thus, it is completed
     *
     * @param meetingId ID of the meeting
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
     * Returns a list of JSON objects of all the meetings of a trade which includes, the status(es) of the meeting(s)
     * (i.e., whether it has been complete or its pending for confirmation or still need to arrange), the location(s),
     * the time(s), the number of edits made, the number of confirmations, and the User(s) who last edited
     *
     * @return A list of JSON objects that includes the meeting status(es), location(s), time(s), number of edits made, number of confirmations,
     * and the User(s) who last edited
     */
    public List<JSONObject> getMeetingsInfo(String tradeId) throws JSONException {
        List<JSONObject> meetingDetails = new ArrayList<>();
        for (String meetingId : tradeToMeetings.get(tradeId)) {
            Meeting meeting = getMeeting(meetingId);
            meetingDetails.add(meeting.getMeetingInfo());
        }
        return meetingDetails;
    }

    /**
     * Returns the Date of the last meeting planned in a trade
     *
     * @param tradeId ID of the trade
     * @return Date of the last meeting planned in a trade
     */
    public Date getLastMeetingTime(String tradeId) {
        Meeting meeting = getMeeting(this.tradeToMeetings.get(tradeId).get(tradeToMeetings.get(tradeId).size() - 1));
        return meeting.getTime();
    }

    /**
     * Returns the Location of the last meeting planned in a trade
     *
     * @param tradeId ID of the trade
     * @return Location of the last meeting planned in a trade
     */
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

    /**
     * Returns if a user can edit or confirm a meeting, which they can if they were not the last user to make an edit or confirm
     *
     * @param tradeId ID of the trade
     * @param username username of the user to make an edit or confirm
     * @return true if the user is not the last user to make an edit or confirm, false otherwise
     */
    public boolean canChangeMeeting(String tradeId, String username) {
        if (tradeToMeetings.get(tradeId) == null) {
            return false;
        } else {
            String meetingId = tradeToMeetings.get(tradeId).get(tradeToMeetings.get(tradeId).size() - 1);
            Meeting meeting = getMeeting(meetingId);
            return (!meeting.getLastEditUser().equals(username));
        }
    }

    /**
     * Takes a list of trade IDs and returns the IDs of the trades that had their last meeting in the past 7 days
     *
     * @param trades list of trade IDs
     * @return list of trade IDs with their last meeting in the past 7 days
     */
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

    /**
     * Returns a list of trade IDs of all trades that are proposed or need to be confirmed
     *
     * @param trades list of trade IDs
     * @param type proposed or to be confirmed
     * @return list of trade IDs of type "type"
     */
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

    /**
     * Cancels (removes) the meetings of a trade
     *
     * @param tradeId ID of the trade
     */
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
        observableDataModel.setChanged();
    }

    /**
     * Returns if all the meetings of a trade are completed
     *
     * @param tradeId ID of the trade
     * @return true if all the meetings are completed, false otherwise
     */
    public boolean tradeMeetingsCompleted(String tradeId){
        for (String id: this.tradeToMeetings.get(tradeId)){
            if (!this.completedMeetings.containsKey(id)){
                return false;
            }
        }
        return true;
    }

    /**
     * Takes in a list of trade IDs and returns a sorted list of the IDs, going from the trade with the oldest last
     * meeting time to the trade with the most recent last meeting time
     *
     * @param tradeIds list of trade IDs
     * @return list of trade IDs sorted by last meeting time
     */
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