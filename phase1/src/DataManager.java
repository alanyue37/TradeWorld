import java.io.*;

/**
 * Manages the saving and loading of TradeModel.
 * Structure of some methods copied from logging.
 */
public class DataManager {

    /**
     * Creates a new DataManager.
     */
    public DataManager() {
    }

    /**
     * Reads the TradeModel from file at filePath, or creates a new file
     * with an empty TradeModel if it does not exist.
     *
     * @param filePath the file to write the records to.
     * @return the TradeManager read in.
     * @throws IOException if IOExcpeption occurs
     * @throws ClassNotFoundException if ClassNotFoundExcpetion occurs
     */
    public TradeModel readFromFile(String filePath) throws IOException, ClassNotFoundException {
        File file = new File(filePath);
        if (file.exists()) {
            InputStream fileStream = new FileInputStream(filePath);
            InputStream buffer = new BufferedInputStream(fileStream);
            ObjectInput input = new ObjectInputStream(buffer);

            // deserialize and return the TradeModel
            TradeModel tm = (TradeModel) input.readObject();
            input.close();
            return tm;
        } else {
            boolean success = file.createNewFile();
            TradeModel tm = new TradeModel();
            saveToFile(filePath, tm);
            return tm;
        }
    }

    /**
     * Writes the TradeModel to file at filePath.
     *
     * @param filePath the file to write the TradeModel to.
     * @param tm the TradeModel to save.
     * @throws IOException if IOException occurs.
     */
    public void saveToFile(String filePath, TradeModel tm) throws IOException {
        OutputStream fileStream = new FileOutputStream(filePath);
        OutputStream buffer = new BufferedOutputStream(fileStream);
        ObjectOutput output = new ObjectOutputStream(buffer);

        // serialize the TradeModel
        output.writeObject(tm);
        output.close();
    }
}
