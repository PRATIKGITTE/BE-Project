package com.project.algo;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import javax.imageio.ImageIO;

/**
 * Reads in the image files and stores BufferedImage's for every example.  Converts to fixed-length
 * feature vectors (of doubles).  Can use RGB (plus grey-scale) or use grey scale.
 *
 * Copyright 2017.  Free for educational and basic-research use.
 *
 * @author: Yuting Liu and Jude Shavlik.
 */
public final class Main {

	// Images are imageSize x imageSize. The provided data is 128x128, but this can be resized by setting this value (or
	// passing in an argument). You might want to resize to 8x8, 16x16, 32x32, or 64x64; this can reduce your network
	// size and speed up debugging runs. ALL IMAGES IN A TRAINING RUN SHOULD BE THE *SAME* SIZE.
	private static int imageSize = 32;

	// We'll hardwire these in, but more robust code would not do so.
	private static enum Category {
		airplanes, butterfly, flower, grand_piano, starfish, watch
	};

	public static int NUM_CATEGORIES = Category.values().length;

	// Store the categories as strings.
	public static List<String> categoryNames = new ArrayList<>();
	static {
		for (Category cat : Category.values()) {
			categoryNames.add(cat.toString());
		}
	}
	
	// If true, FOUR units are used per pixel: red, green, blue, and grey.
	// If false, only ONE (the grey-scale value).
	private static final Boolean useRGB = true;

	// If using RGB, use red+blue+green+grey. Otherwise just use the grey value.
	private static int unitsPerPixel = (useRGB ? 4 : 1);

	// Should be one of { "perceptrons", "oneLayer", "deep" }; You might want to use this if you are trying approaches
	// other than a Deep ANN.
	private static String modelToUse = "deep";

	// The provided code uses a 1D vector of input features. You might want to create a 2D version for your Depp ANN
	// code. Or use the get2DfeatureValue() 'accessor function' that maps 2D coordinates into the 1D vector. The last
	// element in this vector holds the 'teacher-provided' label of the example.
	public static int inputVectorSize;

	// To turn off drop out, set dropoutRate to 0.0 (or a neg number).
	private static double eta = 0.01, fractionOfTrainingToUse = 1.00, dropoutRate = 0.50;

	// Feel free to set to a different value.
	private static int minEpochs = 50;
	private static int maxEpochs = 2000;
	
	private static int MAX_INSTANCES = 30;
	private static boolean FAST = false;

	public static Dataset trainSet, tuneSet, testSet;
	
	public static void main(String[] args) {
		String trainDirectory = "images/trainset/";
		String tuneDirectory = "images/tuneset/";
		String testDirectory = "images/testset/";

		if (args.length > 5) {
			System.err.println(
					"Usage error: java Main <train_set_folder_path> <tune_set_folder_path> <test_set_foler_path> <imageSize>");
			System.exit(1);
		}
		if (args.length >= 1) {
			trainDirectory = args[0];
		}
		if (args.length >= 2) {
			tuneDirectory = args[1];
		}
		if (args.length >= 3) {
			testDirectory = args[2];
		}
		if (args.length >= 4) {
			imageSize = Integer.parseInt(args[3]);
		}

		// Here are statements with the absolute path to open images folder
		File trainsetDir = new File(trainDirectory);
		File tunesetDir = new File(tuneDirectory);
		File testsetDir = new File(testDirectory);
		System.out.println(trainsetDir + " " + tunesetDir + " " + testsetDir + " " + imageSize);

		// create three datasets
		Dataset trainset = new Dataset();
		Dataset tuneset = new Dataset();
		Dataset testset = new Dataset();
		trainSet = trainset;
		tuneSet = tuneset;
		testSet = testset;

		// Load in images into datasets.
		long start = System.currentTimeMillis();
		loadDataset(trainset, trainsetDir);
		System.out.println("The trainset contains " + comma(trainset.getSize()) + " examples.  Took "
				+ convertMillisecondsToTimeSpan(System.currentTimeMillis() - start) + ".");

		start = System.currentTimeMillis();
		loadDataset(tuneset, tunesetDir);
		System.out.println("The  testset contains " + comma(tuneset.getSize()) + " examples.  Took "
				+ convertMillisecondsToTimeSpan(System.currentTimeMillis() - start) + ".");

		start = System.currentTimeMillis();
		loadDataset(testset, testsetDir);
		System.out.println("The  tuneset contains " + comma(testset.getSize()) + " examples.  Took "
				+ convertMillisecondsToTimeSpan(System.currentTimeMillis() - start) + ".");

		// Now train a Deep ANN. You might wish to first use your Lab 2 code here and see how one layer of HUs does. Maybe
		// even try your perceptron code. We are providing code that converts images to feature vectors. Feel free to
		// discard or modify.
		start = System.currentTimeMillis();

		trainANN(trainset, tuneset, testset);
		System.out.println("\nTook " + convertMillisecondsToTimeSpan(System.currentTimeMillis() - start) + " to train.");

	}

	public static void loadDataset(Dataset dataset, File dir) {
		for (File file : dir.listFiles()) {
			// check all files
			if (!file.isFile() || !file.getName().endsWith(".jpg")) {
				continue;
			}
			// String path = file.getAbsolutePath();
			BufferedImage img = null, scaledBI = null;
			try {
				// load in all images
				img = ImageIO.read(file);
				// every image's name is in such format: label_image_XXXX(4 digits) though this code could handle more than
				// 4 digits.
				String name = file.getName();
				int locationOfUnderscoreImage = name.indexOf("_image");

				// Resize the image if requested. Any resizing allowed, but should really be one of 8x8, 16x16, 32x32, or
				// 64x64 (original data is 128x128).
				if (imageSize != 128) {
					scaledBI = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_RGB);
					Graphics2D g = scaledBI.createGraphics();
					g.drawImage(img, 0, 0, imageSize, imageSize, null);
					g.dispose();
				}

				

				if (!FAST || (Math.random() > .80) && MAX_INSTANCES > dataset.getSize()) {
					
				}
				
				if (dataset.getSize() > MAX_INSTANCES && FAST) {
					return;
				}
			} catch (IOException e) {
				System.err.println("Error: cannot load in the image file");
				System.exit(1);
			}
		}
	}
	///////////////////////////////////////////////////////////////////////////////////////////////

	private static Category convertCategoryStringToEnum(String name) {
		if ("airplanes".equals(name))
			// Should have been the singular 'airplane' but we'll live with this minor error.
			return Category.airplanes;
		if ("butterfly".equals(name))
			return Category.butterfly;
		if ("flower".equals(name))
			return Category.flower;
		if ("grand_piano".equals(name))
			return Category.grand_piano;
		if ("starfish".equals(name))
			return Category.starfish;
		if ("watch".equals(name))
			return Category.watch;
		throw new Error("Unknown category: " + name);
	}

	public static double getRandomWeight(int fanin, int fanout) {
		// This is one 'rule of thumb' for initializing weights. Fine for perceptrons and one-layer ANN at least.
		double range = Math.max(Double.MIN_VALUE, 4.0 / Math.sqrt(6.0 * (fanin + fanout)));
		return (2.0 * random() - 1.0) * range;
	}

	// Map from 2D coordinates (in pixels) to the 1D fixed-length feature
	// vector.
	public static double get2DfeatureValue(Vector<Double> ex, int x, int y, int offset) {
		// If only using GREY, then offset = 0; Else offset = 0 for RED, 1 for GREEN, 2 for BLUE, and 3 for GREY.
		return ex.get(unitsPerPixel * (y * imageSize + x) + offset);
		// Jude: I have not used this, so might need debugging.
	}

	///////////////////////////////////////////////////////////////////////////////////////////////

	// Return the count of TESTSET errors for the chosen model.
	private static void trainANN(Dataset trainset, Dataset tuneset, Dataset testset) {
		Instance sampleImage = trainset.getImages().get(0); // Assume there is at least one train image!
		inputVectorSize = sampleImage.getWidth() * sampleImage.getHeight() * unitsPerPixel + 1;
		// The '-1' for the bias is not explicitly added to all examples (instead code should implicitly handle it). The
		// final 1 is for the CATEGORY.

		// For RGB, we use FOUR input units per pixel: red, green, blue, plus grey. Otherwise we only use GREY scale.
		// Pixel values are integers in [0,255], which we convert to a double in [0.0, 1.0]. The last item in a feature
		// vector is the CATEGORY, encoded as a double in 0 to the size on the Category enum. We do not explicitly store
		// the '-1' that is used for the bias. Instead code (to be written) will need to implicitly handle that
		// extra feature.
		System.out.println("\nThe input vector size is " + comma(inputVectorSize - 1) + ".\n");

		Vector<Vector<Double>> trainFeatureVectors = new Vector<Vector<Double>>(trainset.getSize());
		Vector<Vector<Double>> tuneFeatureVectors = new Vector<Vector<Double>>(tuneset.getSize());
		Vector<Vector<Double>> testFeatureVectors = new Vector<Vector<Double>>(testset.getSize());

		
		fillFeatureVectors(trainFeatureVectors, trainset);
		

		// Call your Deep ANN here. We recommend you create a separate class file for that during testing and debugging,
		// but before submitting your code cut-and-paste that code here.

		
			
		
	}

	// fill feature vectors with images from a dataset
	private static void fillFeatureVectors(Vector<Vector<Double>> featureVectors, Dataset dataset) {
		for (Instance image : dataset.getImages()) {
			featureVectors.addElement(convertToFeatureVector(image));
		}
	}

	private static Vector<Double> convertToFeatureVector(Instance image) {
		Vector<Double> result = new Vector<>(inputVectorSize);
		int[][] gray = image.getGrayImage();
		int width = image.getWidth();
		for (int index = 0; index < inputVectorSize - 1; index++) {
			// Need to subtract 1 since the last item is the CATEGORY.
			if (useRGB) {
				int xValue = (index / unitsPerPixel) % width;
				int yValue = (index / unitsPerPixel) / width;
				// System.out.println(" xValue = " + xValue + " and yValue = " + yValue + " for index = " + index);
				if (index % 4 == 0)
					
					// Seems reasonable to also provide the GREY value.
					result.add(image.getGrayImage()[xValue][yValue] / 255.0);
			} else {
				int xValue = index % width;
				int yValue = index / width;
				result.add(gray[xValue][yValue] / 255.0);
			}
		}
		result.add((double) convertCategoryStringToEnum(image.getLabel()).ordinal());
		// The last item is the CATEGORY, representing as an integer starting at 0 (and that int is then coerced to
		// double).

		return result;
	}

	//////////////////// Some utility methods (cut-and-pasted from JWS'
	//////////////////// Utils.java file).
	//////////////////// ///////////////////////////////////////////////////

	private static final long millisecInMinute = 60000;
	private static final long millisecInHour = 60 * millisecInMinute;
	private static final long millisecInDay = 24 * millisecInHour;

	public static String convertMillisecondsToTimeSpan(long millisec) {
		return convertMillisecondsToTimeSpan(millisec, 0);
	}

	public static String convertMillisecondsToTimeSpan(long millisec, int digits) {
		if (millisec == 0) {
			return "0 seconds";
		} // Handle these cases this way rather than saying "0 milliseconds."
		if (millisec < 1000) {
			return comma(millisec) + " milliseconds";
		} // Or just comment out these two lines?
		if (millisec > millisecInDay) {
			return comma(millisec / millisecInDay) + " days and "
					+ convertMillisecondsToTimeSpan(millisec % millisecInDay, digits);
		}
		if (millisec > millisecInHour) {
			return comma(millisec / millisecInHour) + " hours and "
					+ convertMillisecondsToTimeSpan(millisec % millisecInHour, digits);
		}
		if (millisec > millisecInMinute) {
			return comma(millisec / millisecInMinute) + " minutes and "
					+ convertMillisecondsToTimeSpan(millisec % millisecInMinute, digits);
		}

		return truncate(millisec / 1000.0, digits) + " seconds";
	}

	public static String comma(int value) { // Always use separators (e.g., "100,000").
		return String.format("%,d", value);
	}

	public static String comma(long value) { // Always use separators (e.g., "100,000").
		return String.format("%,d", value);
	}

	public static String comma(double value) { // Always use separators (e.g., "100,000").
		return String.format("%,f", value);
	}

	public static String padLeft(String value, int width) {
		String spec = "%" + width + "s";
		return String.format(spec, value);
	}

	/**
	 * Format the given floating point number by truncating it to the specified number of decimal places.
	 *
	 * @param d
	 *           A number.
	 * @param decimals
	 *           How many decimal places the number should have when displayed.
	 * @return A string containing the given number formatted to the specified number of decimal places.
	 */
	public static String truncate(double d, int decimals) {
		double abs = Math.abs(d);
		if (abs > 1e13) {
			return String.format("%." + (decimals + 4) + "g", d);
		} else if (abs > 0 && abs < Math.pow(10, -decimals)) {
			return String.format("%." + decimals + "g", d);
		}
		return String.format("%,." + decimals + "f", d);
	}

	/**
	 * Randomly permute vector in place.
	 *
	 * @param <T>
	 *           Type of vector to permute.
	 * @param vector
	 *           Vector to permute in place.
	 */
	public static <T> void permute(Vector<T> vector) {
		if (vector != null) {
			// NOTE from JWS (2/2/12): not sure this is an unbiased permute; I prefer (1) assigning random number to each
			// element, (2) sorting, (3) removing random numbers. But also see
			// "http://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle" which justifies this.
			/*
			 * To shuffle an array a of n elements (indices 0..n-1): for i from n - 1 downto 1 do j <- random integer with
			 * 0 <= j <= i exchange a[j] and a[i]
			 */

			for (int i = vector.size() - 1; i >= 1; i--) {
				int j = random0toNminus1(i + 1);
				if (j != i) {
					T swap = vector.get(i);
					vector.set(i, vector.get(j));
					vector.set(j, swap);
				}
			}
		}
	}

	public static Random randomInstance = new Random(638 * 838);

	/**
	 * @return The next random double.
	 */
	public static double random() {
		return randomInstance.nextDouble();
	}

	/**
	 * @param lower
	 *           The lower end of the interval.
	 * @param upper
	 *           The upper end of the interval. It is not possible for the returned random number to equal this number.
	 * @return Returns a random integer in the given interval [lower, upper).
	 */
	public static int randomInInterval(int lower, int upper) {
		return lower + (int) Math.floor(random() * (upper - lower));
	}

	/**
	 * @param upper
	 *           The upper bound on the interval.
	 * @return A random number in the interval [0, upper).
	 * @see Utils#randomInInterval(int, int)
	 */
	public static int random0toNminus1(int upper) {
		return randomInInterval(0, upper);
	}

	/////////////////////////////////////////////////////////////////////////////////////////////// Write
	/////////////////////////////////////////////////////////////////////////////////////////////// your
	/////////////////////////////////////////////////////////////////////////////////////////////// own
	/////////////////////////////////////////////////////////////////////////////////////////////// code
	/////////////////////////////////////////////////////////////////////////////////////////////// below
	/////////////////////////////////////////////////////////////////////////////////////////////// here.
	/////////////////////////////////////////////////////////////////////////////////////////////// Feel
	/////////////////////////////////////////////////////////////////////////////////////////////// free
	/////////////////////////////////////////////////////////////////////////////////////////////// to
	/////////////////////////////////////////////////////////////////////////////////////////////// use
	/////////////////////////////////////////////////////////////////////////////////////////////// or
	/////////////////////////////////////////////////////////////////////////////////////////////// discard
	/////////////////////////////////////////////////////////////////////////////////////////////// what
	/////////////////////////////////////////////////////////////////////////////////////////////// is
	/////////////////////////////////////////////////////////////////////////////////////////////// provided.

	private static int trainPerceptrons(Vector<Vector<Double>> trainFeatureVectors,
			Vector<Vector<Double>> tuneFeatureVectors, Vector<Vector<Double>> testFeatureVectors) {
		Vector<Vector<Double>> perceptrons = new Vector<Vector<Double>>(Category.values().length); // One
		// perceptron
		// per
		// category.

		for (int i = 0; i < Category.values().length; i++) {
			Vector<Double> perceptron = new Vector<Double>(inputVectorSize);
			// Note: inputVectorSize includes the OUTPUT CATEGORY as the LAST element. That element in the perceptron will
			// be the BIAS.
			perceptrons.add(perceptron);
			for (int indexWgt = 0; indexWgt < inputVectorSize; indexWgt++)
				perceptron.add(getRandomWeight(inputVectorSize, 1)); // Initialize weights.
		}

		if (fractionOfTrainingToUse < 1.0) { // Randomize list, then get the first N of them.
			int numberToKeep = (int) (fractionOfTrainingToUse * trainFeatureVectors.size());
			Vector<Vector<Double>> trainFeatureVectors_temp = new Vector<Vector<Double>>(numberToKeep);

			permute(trainFeatureVectors); // Note: this is an IN-PLACE permute, but that is OK.
			for (int i = 0; i < numberToKeep; i++) {
				trainFeatureVectors_temp.add(trainFeatureVectors.get(i));
			}
			trainFeatureVectors = trainFeatureVectors_temp;
		}

		int trainSetErrors = Integer.MAX_VALUE, tuneSetErrors = Integer.MAX_VALUE, best_tuneSetErrors = Integer.MAX_VALUE,
				testSetErrors = Integer.MAX_VALUE, best_epoch = -1, testSetErrorsAtBestTune = Integer.MAX_VALUE;
		long overallStart = System.currentTimeMillis(), start = overallStart;

		for (int epoch = 1; epoch <= maxEpochs /* && trainSetErrors > 0 */; epoch++) {
			// might still want to train after trainset error = 0 since we want to get all predictions on the 'right side
			// of zero' (whereas errors defined wrt HIGHEST output).
			permute(trainFeatureVectors); // Note: this is an IN-PLACE permute but that is OK.

			// CODE NEEDED HERE!

			System.out.println("Done with Epoch # " + comma(epoch) + ".  Took "
					+ convertMillisecondsToTimeSpan(System.currentTimeMillis() - start) + " ("
					+ convertMillisecondsToTimeSpan(System.currentTimeMillis() - overallStart) + " overall).");
			reportPerceptronConfig(); // Print out some info after epoch, so you
			// can see what experiment is running in
			// a given console.
			start = System.currentTimeMillis();
		}
		System.out.println(
				"\n***** Best tuneset errors = " + comma(best_tuneSetErrors) + " of " + comma(tuneFeatureVectors.size())
						+ " (" + truncate((100.0 * best_tuneSetErrors) / tuneFeatureVectors.size(), 2) + "%) at epoch = "
						+ comma(best_epoch) + " (testset errors = " + comma(testSetErrorsAtBestTune) + " of "
						+ comma(testFeatureVectors.size()) + ", "
						+ truncate((100.0 * testSetErrorsAtBestTune) / testFeatureVectors.size(), 2) + "%).\n");
		return testSetErrorsAtBestTune;
	}

	private static void reportPerceptronConfig() {
		System.out.println("***** PERCEPTRON: UseRGB = " + useRGB + ", imageSize = " + imageSize + "x" + imageSize
				+ ", fraction of training examples used = " + truncate(fractionOfTrainingToUse, 2) + ", eta = "
				+ truncate(eta, 2) + ", dropout rate = " + truncate(dropoutRate, 2));
	}

	//////////////////////////////////////////////////////////////////////////////////////////////// ONE
	//////////////////////////////////////////////////////////////////////////////////////////////// HIDDEN
	//////////////////////////////////////////////////////////////////////////////////////////////// LAYER

	private static int trainOneHU(
			Vector<Vector<Double>> trainFeatureVectors,
			Vector<Vector<Double>> tuneFeatureVectors,
			Vector<Vector<Double>> testFeatureVectors) {
		ConvolutionalNeuralNetwork cnn = ConvolutionalNeuralNetwork.newBuilder()
				.setInputHeight(imageSize)
				.setInputWidth(imageSize)
				.setFullyConnectedDepth(1)
				.setFullyConnectedWidth(300)
				.setFullyConnectedActivationFunction(ActivationFunction.SIGMOID)
				.setClasses(categoryNames)
				.setLearningRate(eta)
				.setMinEpochs(minEpochs)
				.setMaxEpochs(maxEpochs)
				.build();
		System.out.println("******\tSingle-HU CNN constructed."
				+ " The structure is described below.\t******");
		System.out.println(cnn);
		
		System.out.println("******\tSingle-HU CNN training has begun."
				+ " Updates will be provided after each epoch.\t******");
		
		
		System.out.println("\n******\tSingle-HU CNN testing has begun.\t******");
		cnn.test(testSet, true);		
		return 0;
	}

	
	
	

}
