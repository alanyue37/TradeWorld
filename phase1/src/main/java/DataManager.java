import java.io.*;

/**
 * Manages the saving and loading of TradeModel.
 * Structure of some methods copied from logging.
 */
public class DataManager {

    private final String filePath;

    /**
     * Creates a new DataManager.
     * @param fileName the name of the file containing the serialized objects
     */
    public DataManager(String fileName) {
        filePath = getFilePath(fileName);
    }

    // get the file path inside the resources folder
    private String getFilePath(String fileName) {
        ClassLoader loader = Main.class.getClassLoader();
        String filePath = String.valueOf(loader.getResource("Main.class"));
        String[] tokens = filePath.split("/");
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < tokens.length-4; i++) {
            sb.append(tokens[i]).append("/");
        }
//        sb.setLength(sb.length() - 1);
        sb.append("src/main/resources/");
        sb.append(fileName);
        return sb.toString();
    }

    /**
     * Reads the TradeModel from file at filePath, or creates a new file
     * with an empty TradeModel if it does not exist.
     *
     * @return the TradeManager read in.
     * @throws IOException if IOException occurs
     * @throws ClassNotFoundException if ClassNotFoundException occurs
     */
    public TradeModel readFromFile() throws IOException, ClassNotFoundException {
        File file = new File(filePath);
        TradeModel tradeModel;
        if (file.createNewFile()) {
            tradeModel = new TradeModel();
            saveToFile(tradeModel);
        } else {
            InputStream fileStream = new FileInputStream(file);
            InputStream buffer = new BufferedInputStream(fileStream);
            ObjectInput input = new ObjectInputStream(buffer);
            // deserialize and return the TradeModel
            tradeModel = (TradeModel) input.readObject();
            input.close();
        }
        return tradeModel;
    }

    /**
     * Writes the TradeModel to file at filePath.
     *
     * @param tm the TradeModel to save.
     * @throws IOException if IOException occurs.
     */
    public void saveToFile(TradeModel tm) throws IOException {
        OutputStream fileStream = new FileOutputStream(filePath);
        OutputStream buffer = new BufferedOutputStream(fileStream);
        ObjectOutput output = new ObjectOutputStream(buffer);

        // serialize the TradeModel
        output.writeObject(tm);
        output.close();
    }
}
