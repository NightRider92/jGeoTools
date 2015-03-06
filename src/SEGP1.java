
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Vector;
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
public class SEGP1 {

    /**
     * Requests data from JTable in application with modified "Point" input and
     * "Height" input "Latitude" and "Longitude" are unmodified because we need
     * to export different kind of data (degree and decimal)
     *
     * @param table is the JTable in application
     * @return
     */
    private static Vector<String[]> RequestData(JTable table) {
        // Get all data from JTable in application
        Vector<String[]> data = new Vector<>();
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        for (int i = 0; i < model.getRowCount(); i++) {
            String point = (String) model.getValueAt(i, 0);
            String latitude = (String) model.getValueAt(i, 1);
            String longitude = (String) model.getValueAt(i, 2);
            String height = (String) model.getValueAt(i, 3);

            // Replace all special characters and whitespaces
            point = point.replaceAll("[^\\d]", "");

            // Get second digit of decimal
            float heightF = Float.parseFloat(height);
            float decimal = (heightF - (int) heightF);

            decimal = decimal * 10;

            float secondDecimal = decimal;
            float resultDecimal = (secondDecimal - (int) secondDecimal);
            int heightN;

            // Check geodetic height in meters and check for decimals above and less 0.5
            // If decimals are above 0.5 then increase entire height by one else leave as it is
            if (heightF < 1000.0f) {
                if (resultDecimal >= 0.49f) {
                    height = Float.toString(heightF).substring(0, 5).replace(".", "");
                    heightN = Integer.parseInt(height) + 1;
                    height = Integer.toString(heightN);

                } else {
                    height = Float.toString(heightF).substring(0, 5).replace(".", "");
                    heightN = Integer.parseInt(height);
                    height = Integer.toString(heightN);
                }
            } else if (heightF < 9000.0f) {
                if (resultDecimal >= 0.49f) {
                    height = Float.toString(heightF).substring(0, 6).replace(".", "");
                    heightN = Integer.parseInt(height) + 1;
                    height = Integer.toString(heightN);

                } else {
                    height = Float.toString(heightF).substring(0, 6).replace(".", "");
                    heightN = Integer.parseInt(height);
                    height = Integer.toString(heightN);
                }
            } else {
                Message.InvalidHeight();
                return null;
            }
            data.add(new String[]{point, latitude, longitude, height});
        }
        return data;
    }

    /**
     * Exports SEGP1 degree format converted data
     *
     * @param Filename is the path to the file containing the data
     * @param Table is the JTable in application
     */
    public static void ExportDEG(String Filename, JTable Table) {

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(Filename));
            Vector<String[]> dataDEG = RequestData(Table);
            for (String[] dataDEG1 : dataDEG) {

                // Remove all special data (like degree symbols ... ) from strings
                String point = dataDEG1[0];
                String latitude = dataDEG1[1].replaceAll("[^\\dA-Za-z ]", "").replaceAll("\\s+", "").substring(0, 8) + "N";
                String longitude = dataDEG1[2].replaceAll("[^\\dA-Za-z ]", ""); //.substring(0, 8) + "E";
                String longitude_result;

                // Split longitude (long range is 0 to 180 degrees) so we can check if
                // degrees are below 100 so we can add "0" in front of the number,
                // otherwise remove leading zero
                String[] lat_split = latitude.split("\\s+");
                String[] long_split = longitude.split("\\s+");

                Float longitude_degrees = Float.valueOf(long_split[0]);

                // We check if degrees are below 100.0 degrees
                if (longitude_degrees < 100.0f) {
                    longitude_result = "0" + longitude.replaceAll("\\s+", "").substring(0, 8) + "E";
                } else {
                    longitude_result = longitude.replaceAll("\\s+", "").substring(0, 9) + "E";
                }

                String height = "0" + dataDEG1[3];
                writer.write("                  " + point + " " + latitude + longitude_result + "                 " + height);
                writer.newLine();
            }
            writer.close();
            Message.ExportSuccess();
        } catch (IOException ex) {
            Message.UnknownIOException();
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Message.UnknownIOException();
            }
        }
    }

    /**
     * Exports SEGP1 decimal format converted data
     *
     * @param Filename is the path to the file containing the data
     * @param Table is the JTable in application
     */
    public static void ExportDEC(String Filename, JTable Table) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(Filename));
            Vector<String[]> dataDEC = RequestData(Table);
            DecimalFormat df = new DecimalFormat("#.00000");
            // Parse data and remove special characters
            for (String[] dataDEC1 : dataDEC) {
                String point = dataDEC1[0];
                String latitude = dataDEC1[1].replaceAll("°", "").replaceAll("'", "").replaceAll("\"", "").replaceAll("[a-zA-Z]", "");
                String longitude = dataDEC1[2].replaceAll("°", "").replaceAll("'", "").replaceAll("\"", "").replaceAll("[a-zA-Z]", "");
                String height = dataDEC1[3];

                // Split latitude and longitude by whitespace to get degrees, minutes and seconds
                // for example: lat_split[0] = minutes | lat_split[1] = minutes | lat_split[2] = seconds
                String[] lat_split = latitude.split("\\s+");
                String[] long_split = longitude.split("\\s+");

                Float latitude_degrees = Float.valueOf(lat_split[0]);
                Float latitude_minutes = Float.valueOf(lat_split[1]);
                Float latitude_seconds = Float.valueOf(lat_split[2]);
                Float latitude_result = (latitude_seconds / 60 + latitude_minutes) / 60 + latitude_degrees;

                Float longitude_degrees = Float.valueOf(long_split[0]);
                Float longitude_minutes = Float.valueOf(long_split[1]);
                Float longitude_seconds = Float.valueOf(long_split[2]);

                Float longitude_result = (longitude_seconds / 60 + longitude_minutes) / 60 + longitude_degrees;

                // We are replacing all commas [,] with dots [.] (SEGP1 decimal format requires it)
                writer.write("                  " + point + " " + "+" + df.format(latitude_result).replaceAll(",", ".") + "-" + df.format(longitude_result).replaceAll(",", ".") + "                 " + height);
                writer.newLine();
            }
            writer.close();
            Message.ExportSuccess();
        } catch (IOException ex) {
            Message.UnknownIOException();
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Message.UnknownIOException();
            }
        }
    }
}
