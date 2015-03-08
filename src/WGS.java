
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Moran
 */
public class WGS {

    private static final List<String> input = new ArrayList();

    /**
     * Reads all WGS84 data from the specified file
     *
     * @param Filename is the path of the file
     */
    private static void requestDataFromFile(String Filename) {

        BufferedReader reader = null;
        input.clear();

        try {
            //Open file and set default java encoding (for special characters)
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(Filename), System.getProperty("sun.jnu.encoding")));
            while (reader.ready()) {
                String line = reader.readLine();
                input.add(line);
            }
            reader.close();
        } catch (FileNotFoundException ex) {
            Message.FileNotFound();
        } catch (UnsupportedEncodingException ex) {
            Message.InvalidEncoding();
        } catch (IOException ex) {
            Message.UnknownIOException();
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Message.UnknownIOException();
            }
        }
    }

    /**
     * Splits and loads WGS84 data into DefaultTableModel
     *
     * @param Filename is the path of the file
     * @param Table is the jTable used in application
     */
    public static void importData(String Filename, JTable Table) {

        // Gets default table model and clears it so new data can be fitted in
        DefaultTableModel model = (DefaultTableModel) Table.getModel();
        requestDataFromFile(Filename);
        model.setRowCount(0);

        // Splits line in segments (point,latitude,longitude and height) and loads 
        // the data into the table
        if (input.size() > 0) {
            input.stream().map((input1) -> input1.split("[ ]{2,}|\t|,")).forEach((line) -> {
                /*  String point = line[0]; String latitude = line[1];  String longitude = line[2]; String height = line[3];   */
                model.addRow(new Object[]{line[0], line[1], line[2], line[3]});
            });
        }
    }
}
