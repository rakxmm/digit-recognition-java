import gui.MainGUI;
import network.Network;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
//        var app = new Gui();

        long start = System.nanoTime();

//        var dataList = DataLoader.loadDataFromFile("res/data/mnist_train.csv");

//        var n = new Network(784, 15, 10);
//        n.train(dataList,1,  0.01);
//        n.save("latest.nn");

//        var n2 = Network.load("res/settings/latest.nn");
//        n2.train(dataList, 2, 0.01);
//        n2.save("latest.nn");

        var gui = new MainGUI();


        System.out.println("Time elapsed: " + (System.nanoTime() - start) / 1_000_000.0 + " ms");
    }
}
