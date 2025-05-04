package network;

import math.Array2D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class ImageConvertor {

    private ImageConvertor() {
    }

    public static Array2D getImageMatrix(String imagePath) {
        try {
            BufferedImage image = ImageIO.read(new File(imagePath));
            return ImageConvertor.getImageMatrix(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Tato metoda je od ChatGPT
     * @param image
     * @return
     */
    public static Array2D getImageMatrix(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        double[][] grayscaleMatrix = new double[height*width][1];

        // Prejdeme každý pixel a konvertujeme ho na grayscale
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = new Color(image.getRGB(x, y));

                // Vzorec pre konverziu na grayscale (luminosity method)
                int gray = (int)(0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue());

                // Uložíme hodnotu do matice
                var index = x + y * 28;
                grayscaleMatrix[index][0] = 255 - gray;
            }
        }

        return new Array2D(grayscaleMatrix);
    }

    public static void saveMatrixToFile(Array2D array2D, String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {

            int index = 1;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < array2D.height(); i++) {
                for (int j = 0; j < array2D.width(); j++) {


                    if (index % 28 == 0) {
                        sb.append(String.format("%.1f\n", array2D.get(i, j)));
                        bw.write(sb.toString());
                        sb = new StringBuilder();
                    } else {
                        sb.append(String.format("%.1f, ", array2D.get(i, j)));
                    }

                    index++;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
