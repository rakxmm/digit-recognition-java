package network;

import math.Array2D;
import network.data.Data;
import network.data.digits.Digit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
    public static List<Data> loadDataFromFile(String pathToFile) {
        System.out.println("Dataset loading process has begun!");
        List<Data> dataList = new ArrayList<>();


        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            String line = "";

            while ((line = br.readLine()) != null) {
                var parts = line.split(",");
                int label = Integer.parseInt(parts[0]);
                double[][] array = new double[parts.length - 1][1];
                for (int i = 1; i < parts.length; i++) {
                    array[i - 1][0] = Double.parseDouble(parts[i]);
                }

                dataList.add(new Digit(new Array2D(array), label));
            }


        } catch (IOException e) {
            System.out.println("Failed to find the specific file!");
        }
        System.out.println("Dataset loading process has ended!");
        return dataList;
    }
}
