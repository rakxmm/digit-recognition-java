package network;

import math.Array2D;
import math.activationFunction.NeuralMathFunction;
import math.activationFunction.ReLU;
import math.activationFunction.Sigmoid;
import network.data.digits.Digit;
import network.layer.HiddenLayer;
import network.layer.InputLayer;
import network.layer.Layer;
import network.layer.OutputLayer;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;


public class Network {
    private Layer[] layers;
    private InputLayer input;
    private OutputLayer output;


    /**
     * Vytvorenie instancie neuronovej siete.
     * @param sizes velkosti danych vrstiev
     */
    public Network(int... sizes) {
        this.layers = new Layer[sizes.length];

        for (int i = 0; i < sizes.length; i++) {
            if (i == 0) {
                this.layers[i] = new InputLayer(sizes[i]);
                this.input = (InputLayer)this.layers[i];
            } else if (i == sizes.length - 1) {
                this.layers[i] = new OutputLayer(sizes[i], this.layers[i - 1]);
                this.output = (OutputLayer)this.layers[i];

            } else {
                this.layers[i] = new HiddenLayer(sizes[i], this.layers[i - 1]);
            }
        }

    }

    /**
     * Funkcia zabezpecuje feedforwarding, od prvej do poslednej vrstvy
     */
    public void updateLayers() {
        for (Layer layer : this.layers) {
            if (layer instanceof HiddenLayer hidden) {
                hidden.updateNeurons();
            }
        }

    }

    /**
     * Funkcia nam umoznuje pridat vrstvu do neuronovej siete
     * @param l vrstva, ktoru chceme pridat
     */
    private void addLayer(Layer l) {
        var newLayers = new Layer[this.layers.length + 1];

        for (int i = 0; i < this.layers.length; i++) {
            newLayers[i] = this.layers[i];
        }

        newLayers[newLayers.length - 1] = l;
        this.layers = newLayers;
    }

    /**
     * @return vrati nam instanciu na poslednu vrstvu
     */
    private Layer getLast() {
        return this.layers[this.layers.length - 1];
    }

    /**
     * Funkcia ma na starost vykonat trenovanie neuronovej siete.
     * @param dataset odkaz na dataset, na ktorom budeme trenovat neuronovu siet.
     * @param epochs pocet treningov.
     * @param learningRate rychlost ucenia.
     */
    public void train(List<Digit> dataset, int epochs, double learningRate) {
        if (dataset == null) {
            return;
        }
        JOptionPane.showMessageDialog(null, "Training process has begun!");
        long time = System.currentTimeMillis();
        for (int i = 0; i < epochs; i++) {
            int successful = 0;
            Collections.shuffle(dataset);
            for (Digit d : dataset) {
                this.input.setInput(d.getData());
                this.updateLayers();
                if (this.output.getPrediction().getMaxIndex() == d.getLabel().getMaxIndex()) {
                    successful++;
                }
                this.backpropagate(d.getLabel(), learningRate);
            }
            JOptionPane.showMessageDialog(null, String.format("Epoch %d, Success rate: %.2f%%! \n", i + 1, ((double)successful / dataset.size()) * 100));
        }
        JOptionPane.showMessageDialog(null, "Training process has stopped!");
        long s = (System.currentTimeMillis() - time) / 1000;
        JOptionPane.showMessageDialog(null, String.format("Training lasted:  %d s.\n", s));
    }

    /**
     * Funkcia vykona spatnu propagaciu neuronovej siete. Od poslednej vrstvy po prvu.
     * @param label vektor, ktory reprezentuje spravny vysledok inputu.
     * @param learningRate, rychlost ucenia
     */
    public void backpropagate(Array2D label, double learningRate) {
        this.output.backPropagate(label, learningRate);
    }

    /**
     * Otestuje neuronovu siet na nejakych datach.
     * @param data, instancia dat
     */
    public void test(Digit data) {
        this.input.setInput(data.getData());
        this.updateLayers();
        JOptionPane.showMessageDialog(null, "The guessed number is: " + this.output.getPrediction().getMaxIndex());
    }

    /**
     * Nacita neuronovu siet zo suboru.
     * @param filePath cielova cesta k suboru
     * @return nacitanu neuronovu siet
     */
    public static Network load(String filePath) {
        Network network = new Network();
        JOptionPane.showMessageDialog(null, "Loading process has begun!");
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;


            while ((line = br.readLine()) != null) {
                var parts = line.split(":");

                if (parts.length > 1) {
                    var command = parts[0];

                    switch (command) {
                        case "Layer":
                            network.loadLayer(line);
                            var last = network.getLast();
                            if (last instanceof InputLayer) {
                                network.setInputLayer((InputLayer)network.getLast());
                            } else if (last instanceof OutputLayer) {
                                network.setOutputLayer((OutputLayer)network.getLast());
                            }
                            break;
                        case "Weights":
                        case "Biases":
                            String[] dimensions = parts[1].split("x");
                            int height = Integer.parseInt(dimensions[0]);
                            int width = Integer.parseInt(dimensions[1]);

                            StringBuilder data = new StringBuilder();
                            for (int i = 0; i < height; i++) {
                                data.append(br.readLine() + "\n");
                            }

                            var array = network.parseDataToArray(data.toString(), height, width);
                            if (command.equalsIgnoreCase("weights")) {
                                network.getLast().setWeights(array);
                            } else {
                                network.getLast().setBiases(array);
                            }
                            break;
                        case "Activation":
                            var lastLayer = network.getLast();
                            if (lastLayer instanceof HiddenLayer hidden) {
                                hidden.setActivationFunction(Network.getActivationFunction(parts[1]));
                            }
                            break;
                    }
                }
            }




        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to find the specific file!");
        }
        JOptionPane.showMessageDialog(null, "Loading process has ended successfully!");

        return network;
    }

    /**
     * Nastavi atribut pre vystupnu vrstvu.
     * @param layer
     */
    private void setOutputLayer(OutputLayer layer) {
        this.output = layer;
    }

    /**
     * Nastavi atribut pre vstupnu vrstvu.
     * @param layer
     */
    private void setInputLayer(InputLayer layer) {
        this.input = layer;
    }

    /**
     * @param activation nazov aktivacnej funkcie
     * @return vrati instanciu aktivacnej funkcie
     */
    private static NeuralMathFunction getActivationFunction(String activation) {
        switch (activation) {
            case "Sigmoid":
                return new Sigmoid();
            case "ReLU":
                return new ReLU();
        }
        return null;
    }

    /**
     * Funkcia rozdeli data na vektor podla zadanych rozmerov.
     * @param data data, ktore chceme rozdelit
     * @param height pocet riadkov vo vektore
     * @param width pocet stlpcov vo vektore
     * @return vrati vektor, v ktorom su ulozene data
     */
    private Array2D parseDataToArray(String data, int height, int width) {
        double[][] ar = new double[height][width];

        var lines = data.split("\n");
        for (int i = 0; i < lines.length; i++) {
            var parts = lines[i].split(" ");
            for (int j = 0; j < parts.length; j++) {
                ar[i][j] = Double.parseDouble(parts[j]);
            }
        }
        return new Array2D(ar);
    }

    /**
     * Nacita sa do neuronovej siete nova vrstva na zaklade prikazu.
     * @param command prikaz podla, ktoreho urcime o aky proces a vrstvu ide
     */
    private void loadLayer(String command) {
        var parts = command.split(":");
        switch (parts[1]) {
            case "Hidden":
                this.addLayer(new HiddenLayer(Integer.parseInt(parts[2]), this.getLast()));
                break;
            case "Output":
                this.addLayer(new OutputLayer(Integer.parseInt(parts[2]), this.getLast()));
                break;
            case "Input":
                this.addLayer(new InputLayer(Integer.parseInt(parts[2])));
                break;
        }
    }

    /**
     * Ulozi neuronovu siet do suboru.
     * @param filePath cielova adresa, kde chceme subor ulozit.
     */
    public void save(String filePath) {

        JOptionPane.showMessageDialog(null, "Saving process has begun!");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            StringBuilder sb = new StringBuilder();
            for (Layer layer : this.layers) {

                Array2D biases = layer.getBiases();
                Array2D weights = layer.getWeights();
                sb.append(String.format("%s:%d\n", layer.getName(), layer.getSize()));
                if (layer instanceof HiddenLayer hidden) {
                    sb.append(String.format("Activation:%s\n", hidden.getActivationFunction().getName()));
                }
                if (biases != null) {
                    sb.append(String.format("Biases:%dx%d\n", biases.height(), biases.width()));
                    for (int i = 0; i < biases.height(); i++) {
                        String bias = "";
                        for (int j = 0; j < biases.width(); j++) {
                            if (j != biases.width() - 1) {
                                bias += biases.get(i, j) + " ";
                            } else {
                                bias += biases.get(i, j) + "\n";
                            }
                        }
                        sb.append(bias);
                    }
                }

                if (weights != null) {
                    sb.append(String.format("Weights:%dx%d\n", weights.height(), weights.width()));
                    for (int i = 0; i < weights.height(); i++) {
                        String weight = "";
                        for (int j = 0; j < weights.width(); j++) {
                            if (j != weights.width() - 1) {
                                weight += weights.get(i, j) + " ";
                            } else {
                                weight += weights.get(i, j) + "\n";
                            }
                        }
                        sb.append(weight);
                    }
                }

            }
            bw.write(sb.toString());

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error @saveSettings");
        }
        JOptionPane.showMessageDialog(null, "Saving process has stopped!");
    }


}
