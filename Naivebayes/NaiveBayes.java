import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class NaiveBayes {

	static char[][] traininput = new char[266][17];

	static char[][] test = new char[169][17];
	static int trainrows;
	static int testrows;

	static double[][] marginalProb = new double[17][4];
	static int[][] countsTrain = new int[17][2];
	static char[] testResult = new char[169];

	public static void main(String[] args) throws NumberFormatException,
			IOException {
		int count = 0;

		BufferedReader br = new BufferedReader(new FileReader(
				"/Users/veenac/Downloads/voting_train.data"));
		String line = null;
		int colcount = 0;
		while ((line = br.readLine()) != null) {
			String[] values = line.split(",");
			for (String str : values) {
				traininput[count][colcount] = str.charAt(0);
				colcount++;
			}
			colcount = 0;
			count++;
		}
		System.out.println(count);
		trainrows=count;
		count = 0;
		br.close();
		br = new BufferedReader(new FileReader(
				"/Users/veenac/Downloads/voting_test.data"));
		line = null;
		colcount = 0;
		while ((line = br.readLine()) != null) {
			String[] values = line.split(",");
			for (String str : values) {
				test[count][colcount] = str.charAt(0);
				colcount++;
			}
			colcount = 0;
			count++;
		}
		System.out.println(count);
		testrows=count;
		br.close();
		
		naiveBayes();
		int acc=0;
		for (int i = 0; i <= 168; i++)  {
			if(testResult[i]==test[i][0]){
				acc++;
			}
		}
		
		System.out.println(acc/(double)testrows);
	}

	public static void naiveBayes() {

		int countR = 0;
		int countD = 0;
		for (int i = 0; i <= 265; i++) {

			if (traininput[i][0] == 'r')
				countR++;
			else
				countD++;
		}

		int mcountR = 0;
		int mcountD = 0;

		int countyR = 0;
		int countnR = 0;
		int countyD = 0;
		int countnD = 0;
		for (int j = 1; j <= 16; j++) {

			for (int i = 0; i <= 265; i++) {

				if (traininput[i][0] == 'r') {

					if (traininput[i][j] == 'y') {
						mcountR++;
						countyR++;
					} else if (traininput[i][j] == 'n') {
						mcountR++;
						countnR++;
					}
				}

				if (traininput[i][0] == 'd') {

					if (traininput[i][j] == 'y') {
						mcountD++;
						countyD++;
					} else if (traininput[i][j] == 'n') {
						mcountD++;
						countnD++;
					}
				}

			}

			marginalProb[j][0] = countyR / (double) mcountR;
			marginalProb[j][1] = countnR / (double) mcountR;
			marginalProb[j][2] = countyD / (double) mcountD;
			marginalProb[j][3] = countnD / (double) mcountD;
			
			countsTrain[j][0]=mcountR;
			countsTrain[j][1]=mcountD;
			mcountR = 0;
			mcountD = 0;

			countyR = 0;
			countnR = 0;
			countyD = 0;
			countnD = 0;
		}

		//fill in the missing values for test data
		/*for (int i = 0; i <= 168; i++)  {

			for (int j = 1; j <= 16; j++){

				if(test[i][j]=='?'){
					if(test[i][0]=='r'){
						if(marginalProb[j][0]>marginalProb[j][1]){
							test[i][j]='y';
						}
						else{
							test[i][j]='n';
						}
					}
					
					else if(test[i][0]=='d'){
						if(marginalProb[j][2]>marginalProb[j][3]){
							test[i][j]='y';
						}
						else{
							test[i][j]='n';
						}
					}
				}
			}
		}*/
		
		
		//cosider logs
		double product1=0;//republic
		double product2=0;//democratic
		for (int i = 0; i <= 168; i++)  {

			product1+=Math.log(countR/(double)169);
			product2+=Math.log(countD/(double)169);
			for (int j = 1; j <= 16; j++){
				
				
					if(test[i][j]=='y'){
						product1+=Math.log(marginalProb[j][0]);
						product2+=Math.log(marginalProb[j][2]);
					}
					else if(test[i][j]=='n'){
						product1+=Math.log(marginalProb[j][1]);
						product2+=Math.log(marginalProb[j][3]);
					}
				
					
			}
			
			if(product1>product2){
				testResult[i]='r';
			}
			else
				testResult[i]='d';
			
			product1=0;
			product2=0;
		}

		
		/*double product1=1;//republic
		double product2=1;//democratic
		for (int i = 0; i <= 168; i++)  {

			product1*=1/(double)countR;
			product2*=1/(double)countD;
			for (int j = 1; j <= 16; j++){
				
				
					if(test[i][j]=='y'){
						product1*=marginalProb[j][0];
						product2*=marginalProb[j][2];
					}
					else if(test[i][j]=='n'){
						product1*=marginalProb[j][1];
						product2*=marginalProb[j][3];
					}
				
					
			}
			
			if(product1>product2){
				testResult[i]='r';
			}
			else
				testResult[i]='d';
			
			product1=1;
			product2=1;
		}*/
	}

}
