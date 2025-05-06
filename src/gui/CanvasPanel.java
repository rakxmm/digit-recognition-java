package gui;

import javax.swing.JPanel;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;



public class CanvasPanel extends JPanel {

    private ArrayList<Point> pointList;
    private int brushSize = 0;

    /**
     * Vytvori instanciu pre platno s velkostou stetca
     * @param brushSize int
     */
    public CanvasPanel(int brushSize) {
        this.pointList = new ArrayList<>();
        this.brushSize = brushSize;
    }

    /**
     * Vycisti platno
     */
    public void reset() {
        this.pointList.clear();
        this.repaint();
    }

    /**
     * Kresli na platno podla x, y
     * @param x int
     * @param y int
     */
    public void draw(int x, int y) {
        this.pointList.add(new Point(x, y));
        this.repaint();
    }


    /**
     * Metoda ktora je refreshnuta vzdy ked sa zavola repaint(), pomocou Graphics komponentu
     * @param g Graphics
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(Color.white);


        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());

        g2d.setColor(Color.black);

        for (Point point : this.pointList) {
            CanvasPanel.paintPoint(point, g2d, this.brushSize);
        }

    }

    /**
     * Nakresli bod podla suradnic na platno s velkostou stetca
     * @param p bod so suradnicami
     * @param g grapficky komponent
     * @param brushSize velkost stetca
     */
    public static void paintPoint(Point p, Graphics2D g, int brushSize) {
        g.setColor(Color.black);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.45f));
        g.fillRect(p.x, p.y, brushSize, brushSize);
    }

    /**
     * Nastavime atribut pre zoznam bodov
     * @param pointList ArrayList<Point>
     */
    public void setPointList(ArrayList<Point> pointList) {
        this.pointList = pointList;
    }

    /**
     * Getter na atribut, zoznam bodov
     * @param width int
     * @param height int
     * @return ArrayList<Point>
     */
    public ArrayList<Point> getImagePoints(int width, int height) {
        ArrayList<Point> scaled = new ArrayList<>();
        for (Point p : this.pointList) {
            int x = (int)(p.x * ((double)width / this.getWidth()));
            int y = (int)(p.y * ((double)height / this.getHeight()));
            scaled.add(new Point(x, y));
        }
        return scaled;
    }

    /**
     * Vrati obrazok, ktory sa aktualne nachadza na platne
     * @return BufferedImage
     */
    public BufferedImage getImage() {
        var image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        var g = image.createGraphics();
        this.paint(g);
        g.dispose();
        return image;
    }



}
