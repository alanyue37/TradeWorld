import java.util.HashSet;

/**
 * Communicates with the AdminUser
 */
public class CommunicationManager {
    private final HashSet<String> getUnfreezeRequests;

    public CommunicationManager(HashSet<String> getUnfreezeRequests) {
        this.getUnfreezeRequests = getUnfreezeRequests;
    }

    public HashSet<String> getGetUnfreezeRequests() {
        return getUnfreezeRequests;
    }
}
