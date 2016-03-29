/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hmm;

import java.io.*;
import java.util.*;


/**
 *
 * @author zhongjiezheng
 */

/* 
The program only contains one class file called HMM. The main function in this class gets model file name and observation file name from arguments. It will initial HMM class and call run() method.
run() method is center of program, it will conduct the whole process procedure. 
readModel() will read model file which encoding in specific format. It will store all the info. The state transition probabilities will store in a 2D array; so does the probability mass function.
readObserv() will read observation sequence line by line. Each individual line is a single sequence. Each sequence will store in an array list and all the sequence will store in a queue.
findAllPath() will poll one sequence from queue and use Viterbi algorithm to calculate the maximum probability. This method will initial a matrix to store nodes which have each state’s probabilities for each observation and call CalculateInitialState() and CalculateMiddleState() to calculate those states probabilities. For each state under a observation, once decide which path have maximum probabilities, it will set to the nodes and the previous states will became this node’s parent.
When finish calculating, the printPath() method will be called. That function will check states under last observation to get maximum probability and trace back to the first state to get the path. The path point will be stored in a stack so that the sequence can be correctly printed. The path with most probability will print on the output panel. For one observation sequence in the file, there is one line states path on the panel.

 */

public class HMM {

    /**
     * @param args the command line arguments
     */
     
    public String modelFile;        //store model file name
    public String observFile;       //store observation file name 
    public int numOfStates;         //store number of states
    public float initialState[];    //store Pi
    public float transitionProb[][];//store states transition probability
    public int numOfSymbol;         //store number of observation symbol;
    public ArrayList<String> symbolValue; //stroe observation symbol value;
    public float PMF[][];           //store probability mass function for each state and symbol
    
    public Queue<ArrayList<String>> Observations = new LinkedList<>();  //store all the observed sequence in the list.
    
    HMM(String modelFile, String observFile){
        this.modelFile = modelFile;
        this.observFile = observFile;
    }
    
    private class Node{   //create a class for store each route point
        public Node parent;
        public int state;
        public String symbol;
        public float probabilty;
        Node(){
            
        }
        
        Node(float probabilty){
            this.probabilty = probabilty;
        }
        
    }
    
    public void run(){
        readModel();   //read model file
        readObserv();  //read observation file
        findAllPath();  //find all most likely path and print it;
    }
    
    private void findAllPath(){
        while(!Observations.isEmpty()){   //if there are any sequences, do the loop.
            ArrayList<String> sequence = Observations.poll();  //get one sequence
            int length = sequence.size(); 
            Node Mat[][] = new Node[length][numOfStates];    //creat a matrix to store each state's probability
            for(int i =0; i< length; i++){
                if(i==0)
                    CalculateInitialState(Mat, sequence); //Calculate Initial State probability;
                else
                    CalculateMiddleState(i, Mat, sequence); // Calculate middle states probability;
            }
            PrintPath(length, Mat); //find the max probability in the final state
        }
    }
    
    private void PrintPath(int length, Node Mat[][]){
        Stack<Integer> path = new Stack<>();   //store path point
        float maxProbability=0;
        Node maxFinalNodes = null;
        for(int i=0; i<numOfStates; i++){   ///find the nodes with max probability
            //System.out.println("Probability"+Mat[length-1][i].probabilty);
            if(Mat[length-1][i].probabilty>maxProbability){     //find the max final state
                maxProbability = Mat[length-1][i].probabilty;   
                maxFinalNodes = Mat[length-1][i];               
            }
        }
        //System.out.println("maxProbability"+maxProbability);
        
        Node tmp = maxFinalNodes;
        path.add(tmp.state);
        for(int i=0; i<length-1; i++){     //find all path point on the rounte
            tmp = tmp.parent;
            path.add(tmp.state);
        }
        
        for(int i=0; i<length-1;i++){
            System.out.print("S"+(path.pop()+1)+"->");
        }
        System.out.println("S"+(path.pop()+1));
        
    }
    
    private boolean CalculateInitialState(Node Mat[][],ArrayList<String> sequence){
        String FirstObserv = sequence.get(0);   //get initial observation
        int symbolIndex = symbolValue.indexOf(FirstObserv);    //get the symbol index to find the PMF
        if(symbolIndex != -1){  //if found the symbol.
            for(int i=0; i<numOfStates; i++){    
                Node tmp = new Node();
                tmp.probabilty = initialState[i]*PMF[symbolIndex][i];   //calculate the first column probabilities
                tmp.parent = null;
                tmp.state=i;
                tmp.symbol = FirstObserv;
                Mat[0][i]=tmp;   //save state node to first column
            }
            return true;
        }
        else{
            System.out.println(FirstObserv+" Symbol in sequence are not found in data set."); //if not found the symbol, give the error info
            return false;
        }
    }
    
    private boolean CalculateMiddleState(int observIndex, Node Mat[][], ArrayList<String> sequence){
        String Observation = sequence.get(observIndex);
        int symbolIndex = symbolValue.indexOf(Observation);
        if(symbolIndex != -1){
            for(int i=0; i<numOfStates; i++){     //i is current state
                float probabilitiesArray[];      //to store the probability transfered from previous probability. 
                probabilitiesArray= new float[numOfStates]; 
                float maxProbability= 0;     //save max probabilities
                int maxFromState = 0;         //save which states have the max probabilities.
                for(int j=0; j<numOfStates; j++){    //j is previous state for current i state;
                    probabilitiesArray[j]= Mat[observIndex-1][j].probabilty*transitionProb[i][j]*PMF[symbolIndex][i]; //calculate each probability transfered from other states.
                    if(probabilitiesArray[j]>maxProbability){    
                        maxProbability=probabilitiesArray[j];
                        maxFromState = j;
                    }
                }
                Node tmp = new Node();
                tmp.probabilty = maxProbability;   //the current state probability is the max one
                tmp.state=i;
                tmp.symbol=Observation;
                tmp.parent=Mat[observIndex-1][maxFromState];     //set where the previous states comes from.
                Mat[observIndex][i] = tmp;
            }
            return true;
        }
        else{
            System.out.println(Observation+" Symbol in sequence are not found in data set.");
            return false;
        }
    }
    
    private boolean readObserv(){ ///read sequence line by line
        try{
            FileInputStream fstream = new FileInputStream(observFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String temp;
            String value[];
            
            while((temp=br.readLine())!=null){
                value = temp.trim().split("\40");
                ArrayList<String> sequence = new ArrayList<>();
                for(int i=0; i<value.length; i++){
                    sequence.add(value[i].trim());
                }
                Observations.add(sequence);
            }
            br.close();
        }
        catch (IOException e) {
           System.out.println("error"+e.getMessage());   
           return false;
       }
        return true;
    }
    
    private boolean readModel(){
        try{
            FileInputStream fstream = new FileInputStream(modelFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            int count = 0;        //count the number of line
            String temp;           //store one line in a file
            String value[];        //store those multiple values in a line.
            while((temp=br.readLine())!=null){
                if(count == 0){  //read num of states
                    numOfStates = Integer.parseInt(temp.trim());
                    initialState = new float[numOfStates];   //initial those array only related to states
                    transitionProb = new float[numOfStates][numOfStates];
                }
                else if(count == 1){  // read initial states probabilities
                    value = temp.trim().split("\40");       
                    if(value.length != numOfStates){
                        System.out.println("Error: Num of states not equal to num of initial probabilities");
                        break;
                    }
                    for(int i=0; i<value.length;i++){
                        initialState[i]= Float.parseFloat(value[i].trim());
                    }
                }
                else if(count == 2){  //read transition state probabilities
                    value = temp.trim().split("\40");
                    if(value.length != numOfStates*numOfStates){
                        System.out.println("Error: Num of states not equal to num of transition probabilities");
                        break;
                    }
                    for(int i=0; i< numOfStates; i++){
                        for(int j=0; j<numOfStates; j++){
                            transitionProb[j][i] = Float.parseFloat(value[i*numOfStates+j].trim());
                        }
                    }
                }
                else if(count == 3){  //read number of symbol
                    numOfSymbol = Integer.parseInt(temp.trim());
                    symbolValue = new ArrayList<String>();
                    PMF = new float[numOfSymbol][numOfStates];
                }
                else if(count == 4){  //read symbol
                    value = temp.trim().split("\40");
                    if(value.length != numOfSymbol){
                        System.out.println("Error: Num of symbol not equal to symbols");
                        break;
                    }
                    for(int i=0; i< numOfSymbol; i++){
                        symbolValue.add(value[i].trim());
                    }
                }
                else if(count == 5){  //read PMF
                    value = temp.trim().split("\40");
                    if(value.length != numOfSymbol*numOfStates){
                        System.out.println("Error: Num of PMF is not right");
                        break;
                    }
                    for(int i=0; i< numOfStates; i++){
                        for(int j=0; j<numOfSymbol; j++){
                            PMF[j][i]=Float.parseFloat(value[i*numOfSymbol+j].trim());
                        }
                    }
                }
                
                count ++;
            }
            
            br.close();
        }
        catch (IOException e) {
           System.out.println("error"+e.getMessage());   
           return false;
       }
        
        return true;
    }
    
    public static void main(String[] args) {
        // TODO code applicationmtest logic here
        String modelFile = args[0];
        String observFile = args[1];
        
        HMM inital = new HMM(modelFile,observFile);
        inital.run();
    }
}
