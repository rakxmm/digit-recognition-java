package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;



public class CanvasPanel extends JPanel {

    private ArrayList<Point> pointList;
    private int brushSize = 0;

    public CanvasPanel(int brushSize) {
        this.pointList = new ArrayList<>();
        this.brushSize = brushSize;
    }

    public void reset() {
        this.pointList.clear();
        this.repaint();
    }

    public void draw(int x, int y) {
        this.pointList.add(new Point(x, y));
        this.repaint();
    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.white);


        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());

        g2d.setColor(Color.black);

        for (Point point : pointList) {
            CanvasPanel.paintPoint(point, g2d, this.brushSize);
        }

    }
    public static void paintPoint(Point p, Graphics2D g, int brushSize) {
        g.setColor(Color.black);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.45f));
        g.fillRect(p.x, p.y, brushSize, brushSize);
    }

    public void setPointList(ArrayList<Point> pointList) {
        this.pointList = pointList;
    }

    public ArrayList<Point> getImagePoints(int width, int height) {
        ArrayList<Point> scaled = new ArrayList<>();
        for (Point p : this.pointList) {
            int x = (int)(p.x * ((double) width / this.getWidth()));
            int y = (int)(p.y * ((double) height / this.getHeight()));
            scaled.add(new Point(x, y));
        }
        return scaled;
    }

    public BufferedImage getImage() {
        var image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        var g = image.createGraphics();
        this.paint(g);
        g.dispose();
        return image;
    }



}
