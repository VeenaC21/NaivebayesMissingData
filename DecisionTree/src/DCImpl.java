import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DCImpl {

	static char[][] input = new char[4880][23];
	static HashMap<Integer, ArrayList<Character>> map = new HashMap<Integer, ArrayList<Character>>();
	static int[] check = new int[22];
	static DecisionTree tree = new DecisionTree();

	static double accCount;

	public static void main(String[] args) throws IOException {
		map.put(1,
				new ArrayList<Character>(Arrays.asList('b', 'c', 'x', 'f', 'k',
						's')));
		map.put(2, new ArrayList<Character>(Arrays.asList('f', 'g', 'y', 's')));
		map.put(3,
				new ArrayList<Character>(Arrays.asList('n', 'b', 'c', 'g', 'r',
						'p', 'u', 'e', 'w', 'y')));
		map.put(4, new ArrayList<Character>(Arrays.asList('t', 'f')));
		map.put(5,
				new ArrayList<Character>(Arrays.asList('a', 'l', 'c', 'y', 'f',
						'm', 'n', 'p', 's')));
		map.put(6, new ArrayList<Character>(Arrays.asList('a', 'd', 'f', 'n')));
		map.put(7, new ArrayList<Character>(Arrays.asList('c', 'w', 'd')));
		map.put(8, new ArrayList<Character>(Arrays.asList('b', 'n')));
		map.put(9,
				new ArrayList<Character>(Arrays.asList('k', 'n', 'b', 'h', 'g',
						'r', 'o', 'p', 'u', 'e', 'w', 'y')));
		map.put(10, new ArrayList<Character>(Arrays.asList('e', 't')));
		map.put(11,
				new ArrayList<Character>(Arrays.asList('b', 'c', 'u', 'e', 'z',
						'r', '?')));
		map.put(12, new ArrayList<Character>(Arrays.asList('f', 'y', 'k', 's')));
		map.put(13, new ArrayList<Character>(Arrays.asList('f', 'y', 'k', 's')));
		map.put(14,
				new ArrayList<Character>(Arrays.asList('n', 'b', 'c', 'g', 'o',
						'p', 'e', 'w', 'y')));
		map.put(15,
				new ArrayList<Character>(Arrays.asList('n', 'b', 'c', 'g', 'o',
						'p', 'e', 'w', 'y')));
		map.put(16, new ArrayList<Character>(Arrays.asList('p', 'u')));
		map.put(17, new ArrayList<Character>(Arrays.asList('n', 'o', 'w', 'y')));
		map.put(18, new ArrayList<Character>(Arrays.asList('n', 'o', 't')));
		map.put(19,
				new ArrayList<Character>(Arrays.asList('c', 'e', 'f', 'l', 'n',
						'p', 's', 'z')));
		map.put(20,
				new ArrayList<Character>(Arrays.asList('k', 'n', 'b', 'h', 'r',
						'o', 'u', 'w', 'y')));
		map.put(21,
				new ArrayList<Character>(Arrays.asList('a', 'c', 'n', 's', 'v',
						'y')));
		map.put(22,
				new ArrayList<Character>(Arrays.asList('g', 'l', 'm', 'p', 'u',
						'w', 'd')));

		int count = 0;

		BufferedReader br = new BufferedReader(new FileReader(
				"path_to_training_data"));
		String line = null;
		int colcount = 0;
		while ((line = br.readLine()) != null) {
			String[] values = line.split(",");
			for (String str : values) {
				input[count][colcount] = str.charAt(0);
				colcount++;
			}
			colcount = 0;
			count++;
		}
		br.close();

		ArrayList<Integer> records = new ArrayList<Integer>();
		ArrayList<Integer> attributes = new ArrayList<Integer>();
		for (int i = 0; i <= count - 1; i++) {
			records.add(i);
		}
		for (int i = 1; i <= 22; i++) {
			attributes.add(i);
		}
		Node root = createDecisionTree(records, attributes, -5, '-');
		tree.setRoot(root);
		tree.bfs(root);
		int height=tree.recHeight(root);
		// int c = findAccuracy();
		
		
		//testing the accuracy with test data
		int counttest=0;
		br = new BufferedReader(new FileReader(
				"path_to_test_data"));
		line = null;
		colcount = 0;
		char[][] test=new char[8124][23];
		while ((line = br.readLine()) != null) {
			String[] values = line.split(",");
			for (String str : values) {
				test[counttest][colcount] = str.charAt(0);
				colcount++;
			}
			colcount = 0;
			counttest++;
		}
		br.close();
		System.out.println("count: "+count+" counttest: "+counttest);
		for (int i = 0; i <= counttest-1; i++) {
			traverse(root,test, i);
		}
		System.out.println("account: "+accCount+" "+(accCount/counttest)*100);
		System.out.println(tree.size + " depth: " + height);
	}

	public static Node createDecisionTree(ArrayList<Integer> records,
			ArrayList<Integer> attributes, int attToDivide, char branchValue) {
		// create root node
		Node root = new Node();
		root.attributeNo = attToDivide;
		root.parentCharacteristic = branchValue;
		int countE = 0;
		// check for all edibles or all poisonous
		for (int record : records) {
			if (input[record][0] == 'e')
				countE++;
		}
		int def;// default label
		if (countE > records.size() / 2) {
			def = 1;
		} else
			def = 0;

		if (countE == records.size()) {
			root.yesOrNo = 1;
			return root;
		} else if (countE == 0) {
			root.yesOrNo = 0;
			return root;
		}

		if (attributes.size() == 0) {
			root.yesOrNo = def;
			return root;
		} else {
			int bestAttribute = chooseAttribute(records, attributes,
					attToDivide);
			root.attributeNo = bestAttribute;
			root.parentCharacteristic = branchValue;

			for (char value : map.get(bestAttribute)) {
				// create a subset record
				ArrayList<Integer> nwRecords = new ArrayList<Integer>();
				for (int i : records) {
					if (input[i][bestAttribute] == value)
						nwRecords.add(i);
				}

				if (nwRecords.size() == 0) {
					Node leafNode = new Node();
					leafNode.parentCharacteristic = value;
					leafNode.attributeNo = bestAttribute;// no attribute since its a leaf
												// node
					leafNode.yesOrNo = def;
					root.children.add(leafNode);
				} else {
					attributes.remove(new Integer(bestAttribute));
					Node subNode = createDecisionTree(nwRecords, attributes,
							bestAttribute, value);
					root.yesOrNo=3;
					root.children.add(subNode);
				}

			}

		}
		return root;

	}

	//chooses the best attributes
	public static int chooseAttribute(ArrayList<Integer> records,
			ArrayList<Integer> attributes, int attToDivide) {

		double entropy = calcEntropy(records);
		double subsetEntropy = 0;
		double gain = Double.MIN_VALUE;
		int bestAttribute = -1;
		for (int attribute : attributes) {
			subsetEntropy = calcGain(records, attToDivide, attribute);
			double indGain = entropy - subsetEntropy;
			if (indGain >= gain) {
				gain = indGain;
				bestAttribute = attribute;
			}
		}
		
		if(gain==0){
			return -1;
		}
		return bestAttribute;

	}

	public static double calcEntropy(ArrayList<Integer> records) {
		int count = records.size();
		double countE = 0;
		for (int i : records) {

			if (input[i][0] == 'e')
				countE++;
		}

		double countP = count - countE;

		double part1 = 0;
		double part2 = 0;
		if (countE != 0) {
			part1 = -((countE / count) * Math.log10(countE / count));
		}

		if (countP != 0) {
			part2 = -((countP / count) * Math.log10(countP / count));
		}
		double entropy = part1 + part2;
		return entropy;
	}

	public static double calcGain(ArrayList<Integer> records, int attToDivide,
			int otherAttribute) {

		double count = records.size();

		ArrayList<Character> varieties = map.get(otherAttribute);
		double sum = 0;

		for (Character v : varieties) {
			double countV = 0;
			double countE = 0;
			for (int record : records) {

				if (input[record][otherAttribute] == v) {
					countV++;
					if (input[record][0] == 'e')
						countE++;
				}
			}
			double countP = countV - countE;
			double part1 = 0;
			double part2 = 0;
			if (countV == 0 || count == 0)
				continue;
			if (countE != 0) {
				part1 = -((countE / countV) * Math.log10(countE / countV));
			}

			if (countP != 0) {
				part2 = -((countP / countV) * Math.log10(countP / countV));
			}
			sum += (countV / count) * (part1 + part2);

		}
		return sum;
	}

	//Decision tree traversal used for testing accuracy
	public static void traverse(Node root,char[][] example, int row) {

	
		if (root.yesOrNo != 3) {

			if (root.yesOrNo == 1) {
				if (example[row][0] == 'e')
					accCount++;

			} else if (root.yesOrNo == 0) {
				if (example[row][0] == 'p')
					accCount++;

			} else {
				System.out.println("wrong");
			}

		} else {
			char givenatt = example[row][root.attributeNo];
			
			for (Node n : root.children) {
				if (n.parentCharacteristic == givenatt) {
					traverse(n,example, row);
				}
			}
		}

	}

}
