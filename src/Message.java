
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Moran
 */
public class Message {

    final static String AppName = "jGeoTools";

    public static void ExportSuccess() {
        JOptionPane.showMessageDialog(null, "Data has been exported successfully!", AppName, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void GeneralException() {
        JOptionPane.showMessageDialog(null, "A general error in application has occured. \r\nPlease, check your input data.\r\n\r\nConversion will now stop!", AppName, JOptionPane.ERROR_MESSAGE);
    }

    public static void InvalidLongitude() {
        JOptionPane.showMessageDialog(null, "Longitude in your data is not valid.\r\nConversion will now stop!", AppName, JOptionPane.ERROR_MESSAGE);
    }

    public static void InvalidHeight() {
        JOptionPane.showMessageDialog(null, "Geodetic height is above 9000.0 meters.\r\nConversion will now stop!", AppName, JOptionPane.ERROR_MESSAGE);
    }

    public static void FileNotFound() {
        JOptionPane.showMessageDialog(null, "An error in application has occured!\r\nFile has not been found.", AppName, JOptionPane.ERROR_MESSAGE);
    }

    public static void UnknownIOException() {
        JOptionPane.showMessageDialog(null, "An unknown I/O error in application has occured!", AppName, JOptionPane.ERROR_MESSAGE);
    }
}
