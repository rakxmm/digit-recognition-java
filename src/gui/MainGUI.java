package gui;

import network.DataLoader;
import network.ImageConvertor;
import network.Network;
import network.data.digits.Digit;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class MainGUI {
    private JPanel mainPanel;
    private JPanel navBar;
    private JButton loadButton;
    private JButton saveButton;
    private JButton newButton;
    private JPanel panel;
    private JButton trainButton;
    private JButton testButton;
    private JPanel canvasPanel;
    private JButton clearButton;
    private JTextField etaField;

    private Network network;

    public MainGUI() {

        var dataList = DataLoader.loadDataFromFile("res/data/mnist_train.csv");

        JFrame frame = new JFrame();
        frame.setTitle("raCHECK");
        frame.setContentPane(this.mainPanel);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        this.newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainGUI.this.network = new Network(784, 15, 10);
                MainGUI.this.panel.setBorder(BorderFactory.createTitledBorder(
                        BorderFactory.createEmptyBorder(),
                        "New network",
                        TitledBorder.CENTER,
                        TitledBorder.TOP,
                        new Font("Nirmala UI", 1,  10),
                        Color.white
                ));

            }
        });

        this.loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();


                int response = chooser.showOpenDialog(null);
                if (response == JFileChooser.APPROVE_OPTION) {
                    var path = chooser.getSelectedFile().getAbsolutePath();
                    String[] parts = path.split("/");
                    String name = parts[parts.length - 1];
                    MainGUI.this.network = Network.load(path);
                    MainGUI.this.panel.setBorder(BorderFactory.createTitledBorder(
                            BorderFactory.createEmptyBorder(),
                            name,
                            TitledBorder.CENTER,
                            TitledBorder.TOP,
                            new Font("Nirmala UI", 1,  10),
                            Color.white
                    ));
                }
            }
        });

        this.trainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var n = MainGUI.this.network;
                if (n == null) {
                    JOptionPane.showMessageDialog(null, "No network has been loaded!");
                    return;
                }

                int epochs = 0;
                try {
                    epochs = Integer.parseInt(JOptionPane.showInputDialog("How many epochs you want?"));
                } catch (NumberFormatException ex) {
                    return;
                }

                double trainingRate = 0;

                try {
                    trainingRate = Double.parseDouble(MainGUI.this.etaField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invaild training rate format!");
                }


                n.train(dataList, epochs, trainingRate);

            }
        });
        this.saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                var n = MainGUI.this.network;

                if (n == null) {
                    JOptionPane.showMessageDialog(null, "No network has been loaded!");
                    return;
                }

                int response = chooser.showSaveDialog(null);

                if (response == JFileChooser.APPROVE_OPTION) {
                    String filePath = chooser.getSelectedFile().getAbsolutePath();
                    System.out.println();
                    n.save(filePath);
                }
            }
        });
        this.canvasPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);

                var p = MainGUI.this.canvasPanel;

                if (p instanceof CanvasPanel canvas) {
                    canvas.draw(e.getX(), e.getY());
                }

            }
        });
        this.clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var p = MainGUI.this.canvasPanel;

                if (p instanceof CanvasPanel canvas) {
                    canvas.reset();
                }
            }
        });
        this.testButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var n = MainGUI.this.network;
                var canvas = (CanvasPanel)MainGUI.this.canvasPanel;

                var c = new CanvasPanel(5);
                c.setPreferredSize(new Dimension(28, 28));
                var points = canvas.getImagePoints(28, 28);

                var image = new BufferedImage(28, 28, BufferedImage.TYPE_BYTE_GRAY);
                var g = (Graphics2D)image.createGraphics();

                g.setColor(Color.white);
                g.fillRect(0, 0, 28, 28);

                for (Point point : points) {
                    CanvasPanel.paintPoint(point, g, 2);
                }
                int id = new Random().nextInt();
                try {
                    File output = new File("res/digits/" + id + ".png");
                    ImageIO.write(image, "png", output);

                    System.out.println("Image saved!");
                } catch (IOException er) {
                    er.printStackTrace();
                }

                var a = ImageConvertor.getImageMatrix(image);
                a.saveMatrixToFile("res/digits/" + id);
                n.test(new Digit(a, 0));

            }
        });
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
        this.canvasPanel = new CanvasPanel(20);
    }
}
