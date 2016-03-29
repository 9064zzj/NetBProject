/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnetworks;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
/**
 *
 * @author zhongjiezheng
 */
public class NeuralNetworks {
    
    public String trainedFile;
    public String testFile;
    double learningRate;
    int numOfIteration;
 
    int numOfAttr;     
    double weights[];  //index 0 is A0,...,
    
    LinkedList <Example> TrainedExps = new LinkedList<>();
    LinkedList <Example> TestExps = new LinkedList();
    
    class Example{
        public int attrValue[];
        public int classLabel;
        public int classifedLabel;
        
        Example(int numOfAttr){
            this.attrValue = new int[numOfAttr];
        }
        
    }
    
    
    NeuralNetworks(String trainedFile, String testFile, double learningRate, int numOfIt){
        this.trainedFile = trainedFile;
        this.testFile = testFile;
        this.learningRate = learningRate;
        this.numOfIteration = numOfIt;
    }
    
    void run(){
        readFile(true,trainedFile);
        readFile(false,testFile);
       // TestTestOutput();
        Learning();
        LearningAccu();
        Test();
        TestingAccu();
        
    }
    
    void TestingAccu(){
        int accuracy=0;
        int numOfExp = TestExps.size();
        for(int i=0; i< numOfExp; i++){
            Example Tmp = TestExps.get(i);
            if(Tmp.classLabel==Tmp.classifedLabel)
                accuracy++;
        }
        double rate = (double)accuracy/numOfExp*100;
        System.out.println("Accuracy on testing set: "+rate+"%");
    }
    
    void Test(){
        int numOfTestExps = TestExps.size();
        for(int i=0; i<numOfTestExps; i++){
            Example TmpExp = TestExps.get(i);
            double in = weightedSum(TmpExp);
            double o = sigmoidOutput(in);
            TmpExp.classifedLabel = classifier(o);
        }
    }
    
    void LearningAccu(){
        int accuracy=0;
        int numOfExp = TrainedExps.size();
        for(int i=0; i< numOfExp; i++){
            Example Tmp = TrainedExps.get(i);
            //System.out.println("Learning class:"+Tmp.classLabel+" labelled class:"+Tmp.classifedLabel);
            if(Tmp.classLabel==Tmp.classifedLabel)
                accuracy++;
        }
        double rate = (double)accuracy/numOfExp*100;
        System.out.println("Accuracy on training set: "+rate+"%");
    }
    
    void Learning(){
        int numOfTrainExps = TrainedExps.size();
        if(numOfTrainExps==0)
            System.out.println("No trained exps");
        for(int i=0; i<numOfIteration; i++){
            
            int index = i%numOfTrainExps;
            Example TmpExp = TrainedExps.get(index);
            double in = weightedSum(TmpExp);
            double o = sigmoidOutput(in);
          //  System.out.println("Learn Output:"+o);
            TmpExp.classifedLabel=classifier(o);
            updateWeight(TmpExp,o);
        }
        
    }
    
    void updateWeight(Example exp, double o){
        for(int i=0; i<numOfAttr;i++){
            double err = exp.classLabel - o;
            double dG = o*(1-o);
            double descent = learningRate*err*exp.attrValue[i]*dG;
            weights[i] += descent;
        }
    }
    
    double sigmoidOutput(double input){
        return 1/(1+Math.exp(-input));
    }
    
    int classifier(double output){
        if(output<0.5)
            return 0;
        else
            return 1;
    }
    
    double weightedSum(Example Exp){
        double sum = 0;
        for(int i=0; i<numOfAttr; i++){
            double tmpValue = weights[i]*Exp.attrValue[i];
            sum += tmpValue;
        }
        return sum;
    }
    
    boolean readFile(boolean isTraining, String filename ){
        try{
            FileInputStream fstream = new FileInputStream(filename);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String headInfo[];
            String temp;
            String attr[];
            int count = 0;
            
            while((temp=br.readLine())!=null){
                if(count ==0){   //read head info
                    headInfo=temp.trim().split("(\t)|(\40)");  //separate head by space
                    if(isTraining){      //read read train file
                        numOfAttr = headInfo.length+1;
                        weights =new double[numOfAttr];
                    }
                    else{  //read test file
                        if((headInfo.length+1)!=numOfAttr){
                            System.out.println("Test file & Train file doesn't match");
                            break;
                        }
                    }
                }
                else{   //reading data
                    attr=temp.trim().split("(\t)|(\40)");
                    Example TmExp = new Example(numOfAttr);
                    if((attr.length) == numOfAttr){
                        TmExp.attrValue[0]=1;
                        for(int i=1; i<numOfAttr; i++){
                            TmExp.attrValue[i]=Integer.parseInt(attr[i-1]);
                        }
                        TmExp.classLabel=Integer.parseInt(attr[numOfAttr-1]);
                        if(isTraining)//add to training data
                            TrainedExps.add(TmExp);
                        else //add to testdata
                            TestExps.add(TmExp);
                    }
                    else{
                        System.out.println("Line "+count+": Data not match!");
                    }
                }
                count++;
            }
            br.close();
        }
        catch (IOException e) {
           System.out.println("error"+e.getMessage());   
           return false;
       }
        
        return true;
    }

    
    void TestTrainOutput(){
        int NumOfExp = TrainedExps.size();
        
        for(int i=0; i< NumOfExp; i++){
            Example TmExp = TrainedExps.get(i);
            System.out.print("Exp"+(i+1)+": ");
            
            for(int j=0; j<numOfAttr;j++){
                System.out.print(TmExp.attrValue[j]+" ");
            }
            System.out.print("Class:"+TmExp.classLabel);
            System.out.println();
        }
    }
    
    void TestTestOutput(){
        int NumOfExp = TestExps.size();
        
        for(int i=0; i< NumOfExp; i++){
            Example TmExp = TestExps.get(i);
            System.out.print("Exp"+(i+1)+": ");
            
            for(int j=0; j<numOfAttr;j++){
                System.out.print(TmExp.attrValue[j]+" ");
            }
            System.out.print("Class:"+TmExp.classLabel);
            System.out.println();
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        String trainedFile = args[0];
        String testFile = args[1];
        double learningRate = Double.parseDouble(args[2]);
        int numOfIt = Integer.parseInt(args[3]);
        
        NeuralNetworks inital = new NeuralNetworks(trainedFile,testFile,learningRate, numOfIt);
        inital.run();
    }
}
