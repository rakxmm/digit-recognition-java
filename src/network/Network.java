package network;

import math.Array2D;
import math.activationFunction.NeuralMathFunction;
import math.activationFunction.Sigmoid;
import network.data.Data;
import network.layer.HiddenLayer;
import network.layer.InputLayer;
import network.layer.Layer;
import network.layer.OutputLayer;

import java.awt.*;
import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Network {
    private Layer[] layers;
    private InputLayer input;
    private OutputLayer output;


    public Network(int... sizes) {
        this.layers = new Layer[sizes.length];

        for (int i = 0; i < sizes.length; i++) {
            if (i == 0) {
                this.layers[i] = new InputLayer(sizes[i]);
                this.input = (InputLayer) this.layers[i];
            } else if (i == sizes.length - 1) {
                this.layers[i] = new OutputLayer(sizes[i], this.layers[i - 1]);
                this.output = (OutputLayer) this.layers[i];

            } else {
                this.layers[i] = new HiddenLayer(sizes[i], this.layers[i - 1]);
            }
        }

    }

    public void updateLayers() {
        for (Layer layer : this.layers) {
            if (layer instanceof HiddenLayer hidden) {
                hidden.updateNeurons();
            }
        }

    }

    private void addLayer(Layer l) {
        var layers = new Layer[this.layers.length + 1];

        for (int i = 0; i < this.layers.length; i++) {
            layers[i] = this.layers[i];
        }

        layers[layers.length - 1] = l;
        this.layers = layers;
    }

    private Layer getLast() {
        return this.layers[this.layers.length - 1];
    }

    public void train(List<Data> dataset, int epochs, double learningRate) {
        System.out.println("Training process has begun!");
        long time = System.currentTimeMillis();
        for (int i = 0; i < epochs; i++) {
            int successful = 0;
            Collections.shuffle(dataset);
            for (Data d : dataset) {
                this.input.setInput(d.getDataMatrix());
                this.updateLayers();
                if (this.output.getPrediction().getMaxIndex() == d.getLabel().getMaxIndex()) {
                    successful++;
                }
                this.backpropagate(d.getLabel(), learningRate);
            }
            System.out.printf("     Epoch %d, Success rate: %.2f%%! \n", i + 1, ((double) successful / dataset.size()) * 100);
        }
        System.out.println("Training process has stopped!");
        long s = (System.currentTimeMillis() - time) / 1000;
        System.out.printf("Training lasted:  %d s.\n", s);
    }

    public void backpropagate(Array2D label, double learningRate) {
        this.output.backPropagate(label, learningRate);
    }

    public void test(Data data) {
        this.input.setInput(data.getDataMatrix());
        this.updateLayers();
        System.out.println(this.output.getPrediction().getMaxIndex());
    }

    public static Network load(String filePath) {
        // TO DO next
        Network network = new Network();
        System.out.println("Loading process has begun!");
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {

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
                                network.setInputLayer((InputLayer) network.getLast());
                            } else if (last instanceof OutputLayer) {
                                network.setOutputLayer((OutputLayer) network.getLast());
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
            System.out.println("Failed to find the specific file!");
        }
        System.out.println("Loading process has ended successfully!");

        return network;
    }

    private void setOutputLayer(OutputLayer layer) {
        this.output = layer;
    }

    private void setInputLayer(InputLayer layer) {
        this.input = layer;
    }

    private static NeuralMathFunction getActivationFunction(String activation) {
        switch (activation) {
            case "Sigmoid":
                return new Sigmoid();
        }
        return null;
    }

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





    public void save(String filePath) {

        System.out.println("Saving process has begun!");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            StringBuilder sb = new StringBuilder();
            for (Layer layer : this.layers) {

                Array2D biases = layer.getBiases();
                Array2D weights = layer.getWeights();
                sb.append(String.format("%s:%d\n",layer.getName(), layer.getSize()));
                if (layer instanceof HiddenLayer hidden) {
                    sb.append(String.format("Activation:%s\n", hidden.getActivationFunction().toString()));
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
            System.out.println("Error @saveSettings");
        }
        System.out.println("Saving process has stopped!");
    }


}
