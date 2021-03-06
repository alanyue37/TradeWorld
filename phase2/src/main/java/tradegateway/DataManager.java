package tradegateway;

import javafx.scene.image.Image;

import java.io.*;
import java.net.URLDecoder;

/**
 * The class responsible for managing the saving and loading of TradeModel.
 */
class DataManager {

    private final String filePath;

    /**
     * Creates a new DataManager.
     * @param fileName the name of the file containing the serialized objects
     * @throws IOException if a problem occurs decoding the filepath
     */
    public DataManager(String fileName) throws IOException {
        filePath = getFilePath(fileName);
    }

    // get the file path inside the resources folder
    private String getFilePath(String fileName) throws UnsupportedEncodingException {
        ClassLoader loader = DataManager.class.getClassLoader();
        String filePath = String.valueOf(loader.getResource("tradegateway"));
        String decodedPath = URLDecoder.decode(filePath, "UTF-8"); // support for spaces in file path
        String[] tokens = decodedPath.split("/");
        StringBuilder sb = new StringBuilder();
        sb.append("/");
        for (int i = 1; i < tokens.length-4; i++) {
            sb.append(tokens[i]).append("/");
        }
        sb.append("src/main/resources/");
        sb.append(fileName);
        return sb.toString();
    }

    /**
     * Reads the TradeModel from file at filePath, or creates a new file
     * with an empty TradeModel if it does not exist.
     *
     * @return the TradeManager read in
     * @throws IOException if IOException occurs
     * @throws ClassNotFoundException if ClassNotFoundException occurs
     */
    protected TradeModel readFromFile() throws IOException, ClassNotFoundException {
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
     * Reads a image file at filePath.
     *
     * @return the Image read in
     * @throws IOException if IOException occurs
     */
    protected Image readImage(String imageFile) throws IOException {
        File file = new File(getFilePath(imageFile));
        if (file.exists()) {
            InputStream fileStream = new FileInputStream(file);
            return new Image(fileStream);
        }
        return null;
    }

    /**
     * Writes the TradeModel to file at filePath.
     *
     * @param tm the TradeModel to save
     * @throws IOException if IOException occurs
     */
    protected void saveToFile(TradeModel tm) throws IOException {
        OutputStream fileStream = new FileOutputStream(filePath);
        OutputStream buffer = new BufferedOutputStream(fileStream);
        ObjectOutput output = new ObjectOutputStream(buffer);

        // serialize the TradeModel
        output.writeObject(tm);
        output.close();
    }
}
