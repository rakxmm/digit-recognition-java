package gui;

import network.DataLoader;
import network.ImageConvertor;
import network.Network;
import network.data.Data;
import network.data.digits.Digit;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
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

        newButton.addActionListener(new ActionListener() {
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

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();


                int response = chooser.showOpenDialog(null);
                if (response == JFileChooser.APPROVE_OPTION) {

                    MainGUI.this.network = Network.load(chooser.getSelectedFile().getAbsolutePath());

                }
            }
        });

        trainButton.addActionListener(new ActionListener() {
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
                }

                double trainingRate = 0;

                try {
                    trainingRate = Double.parseDouble(etaField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invaild training rate format!");
                }


                n.train(dataList, epochs,trainingRate);

            }
        });
        saveButton.addActionListener(new ActionListener() {
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
        canvasPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);

                var panel = MainGUI.this.canvasPanel;

                if (panel instanceof CanvasPanel canvas) {
                    canvas.draw(e.getX(), e.getY());
                }

            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var panel = MainGUI.this.canvasPanel;

                if (panel instanceof CanvasPanel canvas) {
                    canvas.reset();
                }
            }
        });
        testButton.addActionListener(new ActionListener() {
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
                ImageConvertor.saveMatrixToFile(a, "res/digits/" + id);
                n.test(new Digit(a, 0));

            }
        });
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
        this.canvasPanel = new CanvasPanel(20);
    }
}
