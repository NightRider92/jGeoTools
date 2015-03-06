
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * Gets input WGS84 (World Geodetic System) data
     *
     * @param Filename is the path to the file containing the data
     * @return
     * @throws java.io.FileNotFoundException
     */
    public static List<String> Import(String Filename) throws FileNotFoundException, IOException {
        //Open file and set default java encoding (for special characters)
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(Filename), System.getProperty("sun.jnu.encoding")));
        List<String> data = new ArrayList<>();

        while (reader.ready()) {
            String line = reader.readLine();
            data.add(line);
        }
        reader.close();
        return data;
    }
}
