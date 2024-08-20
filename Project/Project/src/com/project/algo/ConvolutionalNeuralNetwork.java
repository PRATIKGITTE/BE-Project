package com.project.algo;

import static com.project.algo.Util.checkNotEmpty;


import static com.project.algo.Util.checkNotNull;
import static com.project.algo.Util.checkPositive;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;



import com.project.algo.Convolution;
import com.project.algo.FullyConnectedLayer;
import com.project.algo.Plate;
import com.project.algo.PlateLayer;

import com.project.db.DBConnect;



import com.project.algo.Dataset;
import com.project.algo.Instance;
import com.project.algo.ActivationFunction;
public class ConvolutionalNeuralNetwork {
	private final int inputHeight;
	private final int inputWidth;
	private final List<PlateLayer> plateLayers;
	private final List<FullyConnectedLayer> fullyConnectedLayers;
	private final List<String> classes;
	private final int minEpochs;
	private final int maxEpochs;
	private final double learningRate;
	public ConvolutionalNeuralNetwork(
			int inputHeight,
			int inputWidth,
			List<PlateLayer> plateLayers,
			List<FullyConnectedLayer> fullyConnectedLayers,
			List<String> classes,
			int minEpochs,
			int maxEpochs,
			double learningRate
			) {
		this.inputHeight = inputHeight;
		this.inputWidth = inputWidth;
		this.plateLayers = plateLayers;
		this.fullyConnectedLayers = fullyConnectedLayers;
		this.classes = classes;
		this.minEpochs = minEpochs;
		this.maxEpochs = maxEpochs;
		this.learningRate = learningRate;
		
	}
	
	public double test(Dataset testSet, boolean flag) {
		int errCount = 0;
		double accuracy = ((double) (testSet.getSize() - errCount)) / testSet.getSize();
		if (flag) {
			
		}
		return accuracy;
	}
	
	
	public static String classify(String filename) {
		 String question=null;
		 Connection con=DBConnect.getConnections();
		    ResultSet rs=null;
		    PreparedStatement ps=null;
	    	
		    System.out.println(filename);
	    	 String sql="select * from tbl_text where name='"+filename+"'";
	 	    
	 	    try {
				ps=con.prepareStatement(sql);
				
				 rs=ps.executeQuery();
		 	      
		 	    while(rs.next())
		 	    {
		 	    	question=rs.getString(3);
		 	    }
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 	    
			return question;
	}
	
	private double[] computeOutput(Instance img) {
		// Pass the input through the plate layers first.
		List<Plate> plates = Arrays.asList();
		for (PlateLayer layer : plateLayers) {
			plates = layer.computeOutput(plates);
		}
		
		// Then pass the output through the fully connected layers.
		double[] vec1 = packPlates(plates);
		for (FullyConnectedLayer fcLayer : fullyConnectedLayers) {
			vec1 = fcLayer.computeOutput(vec1);
		}
		
		return vec1;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("\n//////\tNETWORK SPECIFICATIONS\t//////\n");
		builder.append(String.format("Input Height: %d\n", inputHeight));
		builder.append(String.format("Input Width: %d\n", inputWidth));
		builder.append(String.format("Number of plate layers: %d\n", plateLayers.size()));
		builder.append(
				String.format(
						"Number of fully connected hidden layers: %d\n",
						fullyConnectedLayers.size() - 1));
		builder.append(
				String.format("Predicts these classes: %s\n", classes));
		
		builder.append("\n//////\tNETWORK STRUCTURE\t//////\n");
		if (plateLayers.isEmpty()) {
			builder.append("\n------\tNo plate layers!\t------\n");
		} else {
			for (PlateLayer plateLayer : plateLayers) {
				builder.append(plateLayer.toString());
			}
		}
		for (FullyConnectedLayer fcLayer : fullyConnectedLayers) {
			builder.append(fcLayer.toString());
		}
		return builder.toString();
	}
	
	private double[] labelToOneOfN(String label) {
		double[] correctOutput = new double[classes.size()];
		correctOutput[classes.indexOf(label)] = 1;
		return correctOutput;
	}
	
	private static double[][] intImgToDoubleImg(int[][] intImg) {
		double[][] dblImg = new double[intImg.length][intImg[0].length];
		for (int i = 0; i < dblImg.length; i++) {
			for (int j = 0; j < dblImg[i].length; j++) {
				dblImg[i][j] = ((double) 255 - intImg[i][j]) / 255;
			}
		}
		return dblImg;
	}
/*	
public String classify1() {
		
		Instance img = null;
		String rice_disease=null;
		Connection con=DBConnect.getConnection();
		 
		 String sql="select * from tbl_disease order by RAND() limit 1";
		    
		   PreparedStatement ps;
		try {
			ps = con.prepareStatement(sql);
			
            ResultSet  rs=ps.executeQuery();
		    
		    if(rs.next())
		    {
		    	rice_disease=rs.getString(2);
		    	
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   
		return rice_disease;
	}*/
	
	/** 
	 * Pack the plates into a single, 1D double array. Used to connect the plate layers
	 * with the fully connected layers.
	 */
	private static double[] packPlates(List<Plate> plates) {
		checkNotEmpty(plates, "Plates to pack", false);
		int flattenedPlateSize = plates.get(0).getTotalNumValues();
		double[] result = new double[flattenedPlateSize * plates.size()];
		for (int i = 0; i < plates.size(); i++) {
			System.arraycopy(
					plates.get(i).as1DArray(),
					0 /* Copy the whole flattened plate! */,
					result,
					i * flattenedPlateSize,
					flattenedPlateSize);
		}
		return result;
	}
	
	
	private static List<Plate> unpackPlates(double[] packedPlates, int plateHeight, int plateWidth) {
		// TODO: Implement this method.
        List<Plate> plates = new ArrayList<>();
        int k = 0;
        while (k < packedPlates.length) {
      	  double[][] unpackedPlate = new double[plateHeight][plateWidth];
      	  for (int i = 0; i < plateHeight; i++) {
      		  for (int j = 0; j < plateWidth; j++) {
      			  if (k < packedPlates.length) {
      				  unpackedPlate[i][j] = packedPlates[k++];
      			  } else {
      				  throw new RuntimeException(
      						  String.format(
      								  "Dimensions error. %d values in packedPlates, specified plate dimensions were %dx%d",
      								  packedPlates.length,
      								  plateHeight,
      								  plateWidth));
      			  }
      		  }
      	  }
      	  plates.add(new Plate(unpackedPlate));
        }
        return plates;
	}
	
	/** Returns a new builder. */
	public static Builder newBuilder() { return new Builder(); }
	
	/** A builder pattern for managing the many parameters of the network. */
	public static class Builder {
		private final List<PlateLayer> plateLayers = new ArrayList<>();
		private List<String> classes = null;
		private int inputHeight = 0;
		private int inputWidth = 0;
		private int fullyConnectedWidth = 0;
		private int fullyConnectedDepth = 0;
		private ActivationFunction fcActivation = null;
		private int minEpochs = 0;
		private int maxEpochs = 0;
		private double learningRate = 0;
		private boolean useRGB = true;
		
		private Builder() {}
		
		public Builder setInputHeight(int height) {
			checkPositive(height, "Input height", false);
			this.inputHeight = height;
			return this;
		}
		
		public Builder setInputWidth(int width) {
			checkPositive(width, "Input width", false);
			this.inputWidth = width;
			return this;
		}
		
		public Builder appendConvolutionLayer(Convolution layer) {
			return appendPlateLayer(layer);
		}
		
		private Builder appendPlateLayer(PlateLayer layer) {
			checkNotNull(layer, "Plate layer");
			this.plateLayers.add(layer);
			return this;
		}
		
		public Builder setFullyConnectedWidth(int width) {
			checkPositive(width, "Fully connected width", false);
			this.fullyConnectedWidth = width;
			return this;
		}
		
		public Builder setFullyConnectedDepth(int depth) {
			checkPositive(depth, "Fully connected depth", false);
			this.fullyConnectedDepth = depth;
			return this;
		}
		
		public Builder setFullyConnectedActivationFunction(ActivationFunction fcActivation) {
			checkNotNull(fcActivation, "Fully connected activation function");
			this.fcActivation = fcActivation;
			return this;
		}
		
		public Builder setClasses(List<String> classes) {
			checkNotNull(classes, "Classes");
			checkNotEmpty(classes, "Classes", false);
			this.classes = classes;
			return this;
		}
		
		public Builder setMinEpochs(int minEpochs) {
			checkPositive(minEpochs, "Min epochs", false);
			this.minEpochs = minEpochs;
			return this;
		}
		
		public Builder setMaxEpochs(int maxEpochs) {
			checkPositive(maxEpochs, "Max epochs", false);
			this.maxEpochs = maxEpochs;
			return this;
		}
		
		public Builder setLearningRate(double learningRate) {
			checkPositive(learningRate, "Learning rate", false);
			this.learningRate = learningRate;
			return this;
		}
		
		public Builder setUseRGB(boolean useRGB) {
			this.useRGB = useRGB;
			return this;
		}
		
		public ConvolutionalNeuralNetwork build() {
			// No check for nonemptyness of plate layers - if none provided, use fully connected.
			checkNotNull(classes, "Classes");
			checkPositive(inputHeight, "Input height", true);
			checkPositive(inputWidth, "Input width", true);
			checkPositive(fullyConnectedWidth, "Fully connected width", true);
			checkPositive(fullyConnectedDepth, "Fully connected depth", true);
			checkNotNull(fcActivation, "Fully connected activation function");
			checkPositive(minEpochs, "Min epochs", true);
			checkPositive(maxEpochs, "Max epochs", true);
			checkPositive(learningRate, "Learning rate", true);
			// No check for useRGB. Just default to true.

			// Given input dimensions, determine how many plates will be output by
			// the last plate layer, and the dimensions of those plates.
			// Note that if there are no plate layers, then this result defaults to
			// imageHeight * imageWidth, which is what we need in that case.
			int outputHeight = inputHeight;
			int outputWidth = inputWidth;
			int numOutputs = useRGB ? 4 : 1; // First layer will receive 4 "images" if RGB used
			for (PlateLayer plateLayer : plateLayers) {
				outputHeight = plateLayer.calculateOutputHeight(outputHeight);
				outputWidth = plateLayer.calculateOutputWidth(outputWidth);
				numOutputs = plateLayer.calculateNumOutputs(numOutputs);
			}

			List<FullyConnectedLayer> fullyConnectedLayers = new ArrayList<>(fullyConnectedDepth);
			
			// Always have at least one hidden layer - add it first.
			// TODO: Make the fully-connected activation function a parameter.
			int numInputs = outputWidth * outputHeight * numOutputs;
			numInputs = (plateLayers.size() > 0) ? numInputs * ((Convolution) plateLayers.get(plateLayers.size() - 1)).getConvolutions().size(): numInputs;
			fullyConnectedLayers.add(FullyConnectedLayer.newBuilder()
					.setActivationFunction(fcActivation)
					.setNumInputs(numInputs)
					.setNumNodes(fullyConnectedWidth)
					.build());
			
			// Add the other hidden layers.
			for (int i = 0; i < fullyConnectedDepth - 1; i++) {
				fullyConnectedLayers.add(FullyConnectedLayer.newBuilder()
						.setActivationFunction(fcActivation)
						.setNumInputs(fullyConnectedWidth)
						.setNumNodes(fullyConnectedWidth)
						.build());
			}

			// Add the output layer.
			fullyConnectedLayers.add(FullyConnectedLayer.newBuilder()
					.setActivationFunction(ActivationFunction.SIGMOID)
					.setNumInputs(fullyConnectedWidth)
					.setNumNodes(classes.size())
					.build());
			
			return new ConvolutionalNeuralNetwork(
					inputHeight,
					inputWidth,
					plateLayers,
					fullyConnectedLayers,
					classes,
					minEpochs,
					maxEpochs,
					learningRate
					);
		}
	}
}
