package network;

import math.Array2D;
import network.data.digits.Digit;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {

    private DataLoader() {

    }

    /**
     * Nacita data zo suboru.
     * @param pathToFile adresa k suboru
     * @return vrati data.
     */
    public static List<Digit> loadDataFromFile(String pathToFile) {
        JOptionPane.showMessageDialog(null, "Dataset loading process has begun!");
        List<Digit> dataList = new ArrayList<>();


        try (InputStream is = ClassLoader.getSystemResourceAsStream(pathToFile)) {
            if (is == null) {
                JOptionPane.showMessageDialog(null, "Dataset loading process has failed!");
                return null;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = "";

            while ((line = br.readLine()) != null) {
                var parts = line.split(",");
                int label = Integer.parseInt(parts[0]);
                double[][] array = new double[parts.length - 1][1];
                for (int i = 1; i < parts.length; i++) {
                    array[i - 1][0] = Double.parseDouble(parts[i]);
                }
                var a = new Array2D(array);
                dataList.add(new Digit(a, label));
            }


        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to find the specific file!");
        }
        JOptionPane.showMessageDialog(null, "Dataset loading process has ended!");
        return dataList;
    }
}
