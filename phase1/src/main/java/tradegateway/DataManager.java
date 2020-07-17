package tradegateway;

import trademisc.Main;

import java.io.*;
import java.net.URLDecoder;

/**
 * Manages the saving and loading of tradegateway.TradeModel.
 * Structure of some methods copied from logging.
 */
class DataManager {

    private final String filePath;

    /**
     * Creates a new DataManager.
     * @param fileName the name of the file containing the serialized objects
     * @throws IOException if a problem occurs decoding the filepath
     */
    DataManager(String fileName) throws IOException {
        filePath = getFilePath(fileName);
    }

    // get the file path inside the resources folder
    private String getFilePath(String fileName) throws UnsupportedEncodingException {
        ClassLoader loader = DataManager.class.getClassLoader();
        String filePath = String.valueOf(loader.getResource("tradegateway"));
        System.out.println(filePath);
        String decodedPath = URLDecoder.decode(filePath, "UTF-8"); // support for spaces in file path
//        System.out.println(decodedPath);
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
     * Reads the tradegateway.TradeModel from file at filePath, or creates a new file
     * with an empty tradegateway.TradeModel if it does not exist.
     *
     * @return the TradeManager read in.
     * @throws IOException if IOException occurs
     * @throws ClassNotFoundException if ClassNotFoundException occurs
     */
    TradeModel readFromFile() throws IOException, ClassNotFoundException {
        File file = new File(filePath);
        TradeModel tradeModel;
        if (file.createNewFile()) {
            tradeModel = new TradeModel();
            saveToFile(tradeModel);
        } else {
            InputStream fileStream = new FileInputStream(file);
            InputStream buffer = new BufferedInputStream(fileStream);
            ObjectInput input = new ObjectInputStream(buffer);
            // deserialize and return the tradegateway.TradeModel
            tradeModel = (TradeModel) input.readObject();
            input.close();
        }
        return tradeModel;
    }

    /**
     * Writes the tradegateway.TradeModel to file at filePath.
     *
     * @param tm the tradegateway.TradeModel to save.
     * @throws IOException if IOException occurs.
     */
    void saveToFile(TradeModel tm) throws IOException {
        OutputStream fileStream = new FileOutputStream(filePath);
        OutputStream buffer = new BufferedOutputStream(fileStream);
        ObjectOutput output = new ObjectOutputStream(buffer);

        // serialize the tradegateway.TradeModel
        output.writeObject(tm);
        output.close();
    }
}
