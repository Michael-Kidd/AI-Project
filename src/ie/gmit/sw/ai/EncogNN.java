package ie.gmit.sw.ai;

import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;


/*
 *  Inputs
 *  -------------
 *  	strength, venom, playerPos, hidePos
 *  1)Strength  - level of energy the spider has
 *  2) Venom - Amount of venom the spider has left
 *  3) is the spider next to the player
 *  4) is the spider in its hiding place
 *  
 *  Outputs
 *  -------------
 *  1) Chase
 *  2) Attack
 *  3) Hide
 *  4) Heal
 *  
 */
public class EncogNN {
	
	private static BasicNetwork network;
	private static MLDataSet trainingSet;
	private static ResilientPropagation train;
	
	private static double[][] data = { //Strength, Venom, Hiding, Healing 
			{ 1, 0, 0, 0 }, //has strength, no venom, not near player or hiding
			{ 0, 1, 0, 0 }, // no strength, has venom, not near player or hiding
			{ 0, 0, 1, 0 }, //no strength or venom, near player, not near hidng spot
			{ 0, 0, 0, 1 }, //no strength, no venom, not near player but is near hiding spot
			{ 0, 0, 1, 1 }, //no strength, no veno,. near player and hiding spot			
			{ 1, 1, 0, 0 }, //has strength, has venom, not near player or hiding spots
			{ 1, 1, 1, 0 }, //has strength, has venom, near player, not near hiding
			{ 1, 1, 0, 1 }, //has strength has venom, not near player, but is near hiding spot
			{ 1, 1, 1, 1 }, //has strength, has venom, near both player and hiding spot
			{ 0, 1, 1, 0 }, //No Strength, has Venom, not near player and not near the hiding spot
			{ 0, 1, 0, 1 }, //No Strength, has Venom, not near player and in the hiding spot
			{ 0, 0, 0, 0 }}; //No Strength, No Venom, not near player or near hiding spot

	private static double[][] expected = { //Chase, Attack, Hide, Heal
			{ 0, 0, 1, 0 }, //hide
			{ 0, 0, 1, 0 }, //hide
			{ 0, 0, 1, 0 }, //hide
			{ 0, 0, 0, 1 }, //heal
			{ 0, 0, 0, 1 }, //heal
			{ 1, 0, 0, 0 }, //chase
			{ 0, 1, 0, 0 }, //Attack
			{ 1, 0, 0, 0 }, //chase
			{ 0, 1, 0, 0 }, //Attack
			{ 0, 0, 1, 0 }, //Hide
			{ 0, 0, 0, 1 }, //Heal
			{ 0, 0, 1, 0 }  //Hide
	};
	
	
	public void go() {
		
		//----------------------------------------------------
		//Step 1: Declare Network Topology
		//----------------------------------------------------
		
		network = createNetwork();
		
		//----------------------------------------------------
		//Step 2: Create the training data set
		//----------------------------------------------------
		
		trainingSet = new BasicMLDataSet(data, expected);
		
		//----------------------------------------------------
		//Step 3: Train the NN
		//----------------------------------------------------
		
		setTrain(trainNetwork(network, trainingSet));
		
		//----------------------------------------------------
		//Step 4: Test the NN
		//----------------------------------------------------		
		
		test(trainingSet, network);
		
		//----------------------------------------------------
		//Step 4: Test the NN
		//----------------------------------------------------		
		
		Encog.getInstance().shutdown();
		
		//----------------------------------------------------
		//Step 5: shutdown
		//----------------------------------------------------		
		
		
		
	}
	
	BasicNetwork createNetwork() {
		
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(null, true, 4));
		network.addLayer(new BasicLayer(new ActivationSigmoid(), false, 2));
		network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 4));
		network.getStructure().finalizeStructure();
		network.reset();
		
		return network;
		
	}
	
	ResilientPropagation trainNetwork( BasicNetwork network, MLDataSet trainingSet) {
		
		ResilientPropagation train = new ResilientPropagation(network, trainingSet);
		
		double minError = 0.1;
		int epoch = 1;
		
		do {
			train.iteration();
			epoch++;
		} while (train.getError() > minError);
		
		train.finishTraining();
		
		System.out.println("[INFO] training complete in " + epoch + " epochs with error=" + train.getError());
		
		return train;
	}
	
	void test(MLDataSet trainingSet, BasicNetwork network) {
		
		System.out.println("[INFO] Testing the network:");	
		
		for(MLDataPair pair: trainingSet ) {
			
			MLData output = network.compute(pair.getInput());
			
			System.out.println(pair.getInput().getData(0) + "," 
							 + pair.getInput().getData(1)
							 + ", Y=" + (int)Math.round(output.getData(0)) 
							 + ", Yd=" + (int) pair.getIdeal().getData(0));
		}
		
	}
	
	public static int getState(int strength, int venom, int playerPos, int hidePos) {
		
		double[] input = {strength, venom, playerPos, hidePos};
		
		//System.out.println(input[0] +" " + input[1] +" " +input[2] +" " +input[3] );
		
		MLData data = new BasicMLData(input);
		
		//MLData data2 = network.compute(data);
		
		int val = network.classify(data);
		
		return val;
	}

	public static ResilientPropagation getTrain() {
		return train;
	}

	public static void setTrain(ResilientPropagation train) {
		EncogNN.train = train;
	}
	
}
