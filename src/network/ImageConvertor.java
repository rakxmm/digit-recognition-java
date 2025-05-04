package network;

import math.Array2D;


import java.awt.Color;
import java.awt.image.BufferedImage;

public class ImageConvertor {

    private ImageConvertor() {
    }


    /**
     * Tato metoda je od ChatGPT
     * @param image obrazok, ktoreho maticu chceme.
     * @return vrati vektor/maticu, ktory reprezentuje obrazok
     */
    public static Array2D getImageMatrix(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        double[][] grayscaleMatrix = new double[height * width][1];

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

}
