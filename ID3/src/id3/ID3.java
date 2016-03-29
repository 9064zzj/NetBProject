/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package id3;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
/**
 *
 * @author zhongjiezheng
 */
public class ID3 {

    /**
     * @param args the command line arguments
     */
    
    String trainedFile;
    String testFile;
    int numberOfAttr;
    int attrPossValue[];
    int zoom = 100;
    
    Node rootNode;
    
    LinkedList <Example> TrainExps = new LinkedList<>();
    LinkedList BuildExps = new LinkedList();
    LinkedList <Example> TestExps = new LinkedList();
    
    LinkedList <Integer> AttrValues[];
    
    ID3(String trainedFile, String testFile){
        this.testFile=testFile;
        this.trainedFile=trainedFile;
    }
    
    void run(){
        ReadFile(true, trainedFile);
        ReadFile(false,testFile);
        //TestOutput();
        //TestOutput1();
        Learner();
        rootNode.TestExps = TestExps;
        Tester(rootNode);
        PrintTest();
    }
    
    void PrintTest(){
        int total = TestExps.size();
        int matches=0;
        
        for(int i=0; i<total;i++){
            Example tmp = TestExps.get(i);
            if(tmp.match())
                matches++;
        }
        double accuracy = (double)matches/(double)total*100;
        
        System.out.println("Accuracy on testing set ("+total+" instances): "+accuracy+"%");
        
    }
    
    void Tester(Node root){
        if(root.leaf){
            int NumOfExp = root.TestExps.size();
            for(int i=0; i<NumOfExp; i++){
                Example tmp = root.TestExps.get(i);
                tmp.DTclass = root.classifier;
            }
        }
        else{
        int SplitAttr = root.splitAttr;             //
        int NumOfTest = root.TestExps.size();
        for(int i=0; i<NumOfTest ;i++){
            Example tmp = root.TestExps.get(i);      //
            int value = tmp.attrValue[SplitAttr];
       //     System.out.print(value);
            int index = root.splitValues.indexOf(value);
       //     System.out.println(" "+index);
            if(index == -1)
                tmp.DTclass = root.classifier;
            else{
                Node child = root.Child.get(index);
                child.TestExps.add(tmp);
            }
        }
        for(int i=0; i<root.Child.size();i++)
            Tester(root.Child.get(i));            
        }
        
    }
    
    class Example{
        public int attrValue[];
        public int numOfAttr;
        public int classLabel;
        public int DTclass =-1;
        
        Example(int numOfAttr){
            this.numOfAttr=numOfAttr;
            attrValue =new int[numOfAttr];
        }
        
        Example(int attrValue[],int classLabel){
            this.attrValue=attrValue;
            numOfAttr=attrValue.length;
            this.classLabel= classLabel;
        }
        
        public boolean match(){
            if(classLabel == DTclass)
                return true;
            else
                return false;
        }
        
    }
   
    class Node{
        public boolean leaf=false;
        public LinkedList <Example> Exps = new LinkedList<>();
        public int splitAttr;
        public int depth;
        public int classifier =-1;  //only works if it's leaf
        
        public LinkedList <Integer> splitValues = new LinkedList<>();
        
        public LinkedList <Integer> AttrLeft=new LinkedList<>();
        public LinkedList <Node> Child=new LinkedList<>();
        
        public LinkedList <Example> TestExps = new LinkedList<>();
        
        Node(LinkedList<Example> Exps){
            this.Exps = Exps;
        }
        
        Node(){
            
        }
        
        
    }
    
    void Learner(){
        float zoomPercent = (float)zoom/100;
        int NumOfTotalExps = TrainExps.size();
        int NumOfTrainExps = (int) (NumOfTotalExps*zoomPercent);
        
        Node root = new Node();
        for(int i =0; i<NumOfTrainExps;i++)
            root.Exps.add(TrainExps.get(i));
        
        root.depth=0;
        for(int i=0; i<numberOfAttr; i++){
            root.AttrLeft.add(i);
        }
        rootNode = root;
        ID3tree(rootNode);
        PrintBuildTree(rootNode);
        PrintTrainAccuracy();
    }
    
    void PrintTrainAccuracy(){
        int total = rootNode.Exps.size();
        int matches=0;
        
        for(int i=0; i<total; i++){
            Example TmpExp = TrainExps.get(i);
            if(TmpExp.match())
                matches++;
        }
        double accuracy = (double)matches/(double)total*100;
        
        System.out.println("Accuracy on training set ("+total+" instances): "+accuracy+"%");
    }
    
    void PrintBuildTree(Node root){
        for(int j =0; j<root.Child.size();j++){
            Node child = root.Child.get(j);
            for(int i=0; i<root.depth;i++)
                System.out.print("| ");
            System.out.print("attr"+(root.splitAttr+1)+" = ");
            //System.out.print(root.splitValues);
            System.out.print(root.splitValues.get(j));
            if(child.leaf){
                int DTclass = Classify(child);
                child.classifier = DTclass;
                System.out.print(": "+DTclass);
                System.out.println();
            }
            else{
                System.out.println();
                PrintBuildTree(child);
            }
        }
        
    }
    
    int Classify(Node leaf){
        int posClass=0, negClass=0, DTClass;
        int numOfExp = leaf.Exps.size();
        for(int i=0;i<numOfExp;i++){
            Example tmpExp = leaf.Exps.get(i);
            if(tmpExp.classLabel == 1)
                posClass++;
            else
                negClass++;
        }
        
        if(posClass>=negClass){
           // System.out.println("NumPos: "+posClass+" NumNeg: "+negClass+" DTClass:1");
            DTClass = 1;
        }
        else{
           // System.out.println("NumPos: "+posClass+" NumNeg: "+negClass+" DTClass:0");
            DTClass = 0;
        }
        for(int i=0;i<numOfExp;i++){
            Example tmpExp = leaf.Exps.get(i);
            tmpExp.DTclass = DTClass;
        }
        
        return DTClass;
        
    }
      
    void ID3tree(Node root){
        double entrophy = ClassEntrophy(root);   //calculate entrophy by class
        //System.out.println("Entrophy:"+entrophy);
        if(entrophy ==0){ //If entrophy is 0, reutrn a leaf
            root.leaf = true;
        }
        else{ //If entrophy is not 0
            if(root.AttrLeft.size()==0){  //no more attr, return a leaf
                root.leaf = true;
            }
            else if(root.AttrLeft.size()==1){ //have only 1 attr left
                root.splitAttr = root.AttrLeft.element();
                ChildBuilder(root);//new child
                
            }
            else{  //Have attr num>1
                 int numOfAttr = root.AttrLeft.size();   //see how many Attr we need calucalte
                 double InfoGain[] = new double[numOfAttr];     //info gain for each Attr
                 for(int i=0; i<numOfAttr; i++){                //calculate IG for each Attr
                     InfoGain[i]=InfoGain(root,root.AttrLeft.get(i),entrophy);    
                 }
                 int MaxIGindex = MaxIndex(InfoGain);            //get max ig
                 root.splitAttr=root.AttrLeft.get(MaxIGindex);
                 //new child
                 ChildBuilder(root);
            }
            
        }
    }
    
    void ChildBuilder(Node root){
        int NumOfPossValue = attrPossValue[root.splitAttr];
        int NumOfExps = root.Exps.size();
        int SplitAttr = root.splitAttr;
        int NumOfValue=0;
        LinkedList<Example>[] ChildExp = new LinkedList[NumOfPossValue];
        LinkedList<Integer> AttrValues = new LinkedList<>();
        
        for(int i=0; i < NumOfPossValue;i++){
            if(ChildExp[i]==null)
                ChildExp[i]=new LinkedList<Example>();
        }
        
        for(int i=0; i < NumOfExps; i++){
            Example TmExp = root.Exps.get(i);     
            int value = TmExp.attrValue[SplitAttr];  
            if(!AttrValues.contains(value))         //if have new value
                AttrValues.add(value);               //add to value list
            int index = AttrValues.indexOf(value);
            ChildExp[index].add(TmExp);
        }
        LinkedList<Integer> ChildAttrLeft = root.AttrLeft;
        ChildAttrLeft.remove(new Integer(SplitAttr));
        
        //Node ChildNode[] = new Node[NumOfValue];
        root.splitValues = AttrValues;
        NumOfValue = AttrValues.size();
        for(int i=0; i<NumOfValue; i++){
            Node ChildNode =new Node();
            ChildNode.AttrLeft=(LinkedList)ChildAttrLeft.clone();
            ChildNode.depth=root.depth+1;
            ChildNode.Exps=ChildExp[i];
            root.Child.add(ChildNode);
            ID3tree(ChildNode);
        }
        
//        for(int i=0; i< NumOfValue;i++){
//            ID3tree(ChildNode[i]);
//        }
//        for(int i=0; i< ChildExp[1].size();i++){
//            Example exp = ChildExp[1].get(i);
//            System.out.print(" "+exp.attrValue[root.splitAttr]);
//        }
        
    }
    
    int MaxIndex(double arry[]){
        double max=0;
        int maxIndex=0;
        for(int i=0; i<arry.length; i++){
            if(max<arry[i]){
                max=arry[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }
    
    double InfoGain(Node root, int Attr, double ClassEntrophy){
        int NumOfPossValue = attrPossValue[Attr];          //needs know how many values this attr have
        //System.out.println("NumOfPossVal: "+NumOfPossValue);
        int NumOfExpsWithValue[] = new int[NumOfPossValue];
        int NumOfExpsWithValueHavePosClass[] = new int[NumOfPossValue];
        LinkedList<Integer> AttrValues = new LinkedList<>();
        
        int NumOfExps = root.Exps.size();
        for(int i=0; i< NumOfExps; i++){             //travse all the exps
            Example TmExp = root.Exps.get(i);     
            int value = TmExp.attrValue[Attr];  
            if(!AttrValues.contains(value)){         //if have new value
                AttrValues.add(value);               //add to value list
                if(AttrValues.size()>NumOfPossValue){ //if more than max num value,break
                    System.out.println("Data Error: Attr"+Attr+" in exp"+i+" have overmaxed");
                    break;
                }
            }
            int index = AttrValues.indexOf(value);
            NumOfExpsWithValue[index]++;
            if(TmExp.classLabel==1)
                NumOfExpsWithValueHavePosClass[index]++;
        }
        
        //Calculate average entrophy
        double AVEntrophy=0;
//        double ValueEntrophies[] = new double[NumOfPossValue];
        
        for(int i=0; i<NumOfPossValue; i++){    //for each value
            double P = (double)NumOfExpsWithValueHavePosClass[i]/(double)NumOfExpsWithValue[i];   //P for Pos class woth value i
            double nP = (double)(NumOfExpsWithValue[i]-NumOfExpsWithValueHavePosClass[i])/(double)NumOfExpsWithValue[i];  //P for Neg class
            double ValueEntrophy;
            if(P==0 || nP==0)
                ValueEntrophy=0;
            else
                ValueEntrophy=-P*(Math.log(P)/Math.log(2))-nP*(Math.log(nP)/Math.log(2));
            AVEntrophy += ((double) NumOfExpsWithValue[i]/(double)NumOfExps)*ValueEntrophy;
            //System.out.println("VEntrophy: "+((double) NumOfExpsWithValue[i]/(double)NumOfExps)*ValueEntrophy);
        }
        //System.out.println();
//        System.out.println("AVE: "+AVEntrophy);
//        System.out.println("IG: "+(ClassEntrophy-AVEntrophy));
//        System.out.println();
        
        return ClassEntrophy-AVEntrophy;
        
//        for(int i=0;i<NumOfPossValue;i++ ){
//            System.out.println(NumOfExpsWithValueHavePosClass[i]);
//        }
        
        
        
//        double AttrEnts[] = new double[NumOfValue];     //Store entrophy for each value
//        
//        for(int i=0; i<NumOfValue;i++){          //calculate each value's entrophy
//            
//            int NumOfExp=0;                           //have of exps have one particular value
//            int NumOfTotal = root.Exps.size();        
//            
//            for(int j =0; j<NumOfTotal; j++){
//                Example TmpExp = root.Exps.get(i);
//                if(TmpExp.attrValue[Attr] == i)
//                    NumOfExp++;
//            }
//            double
//        }
    }
    
    double ClassEntrophy(Node root){
         int numOfExp = root.Exps.size();
         int numOfPos=0;
         Example Exp;
         
         for(int i=0; i<numOfExp; i++){
             Exp =root.Exps.get(i);
             if(Exp.classLabel==1)
                 numOfPos++;
         }
         
         double pP = (float)numOfPos/(float)numOfExp;
         double nP = 1-pP;
         
         if(pP > nP)
             root.classifier = 1;
         else
             root.classifier = 0;
         
         if(pP == 0.0 || nP==0.0)
             return 0;
         else
             return -pP*(Math.log(pP)/Math.log(2))-nP*(Math.log(nP)/Math.log(2));
         
    } 
    
    boolean ReadFile(boolean train, String fileName){
        try{
            FileInputStream fstream = new FileInputStream(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String headInfo[];
            String temp;
            String attr[];
            int count = 0;
            
            while((temp=br.readLine())!=null){
                if(count ==0){   //read head info
                    headInfo=temp.split("(\t)|(\40)");  //separate head by space
                    if(train){      //read read train file
                        numberOfAttr = headInfo.length/2;
                        //System.out.println("NumOfA:"+numberOfAttr);
                        attrPossValue = new int[numberOfAttr];
                        AttrValues =new LinkedList[numberOfAttr];
                        
                        for(int i=0; i<numberOfAttr;i++){
                            int tempNum = Integer.parseInt(headInfo[2*i+1]);
                            attrPossValue[i]=tempNum;
                            AttrValues[i] = new LinkedList<Integer>();
                        }
                    }
                    else{  //read test file
                        if((headInfo.length/2)!=numberOfAttr){
                            System.out.println("Test file & Train file doesn't match");
                            break;
                        }
                    }
                }
                else{   //reading data
                    attr=temp.split("\t");
                    Example TmExp = new Example(numberOfAttr);
                    if(attr.length == numberOfAttr+1){
                        for(int i=0; i<numberOfAttr; i++){
                            TmExp.attrValue[i]=Integer.parseInt(attr[i]);
                        }
                        TmExp.classLabel=Integer.parseInt(attr[numberOfAttr]);
                        if(train)//add to training data
                            TrainExps.add(TmExp);
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
   
    void TestOutput(){
        int NumOfExp = TrainExps.size();
        
        for(int i=0; i< NumOfExp; i++){
            Example TmExp = TrainExps.get(i);
            System.out.print("Exp"+(i+1)+": ");
            
            for(int j=0; j<numberOfAttr;j++){
                System.out.print(TmExp.attrValue[j]+" ");
            }
            System.out.print("Class:"+TmExp.classLabel);
            System.out.println();
        }
    }
    
    void TestOutput1(){
        int NumOfExp = TestExps.size();
        
        for(int i=0; i< NumOfExp; i++){
            Example TmExp = TestExps.get(i);
            System.out.print("Exp"+(i+1)+": ");
            
            for(int j=0; j<numberOfAttr;j++){
                System.out.print(TmExp.attrValue[j]+" ");
            }
            System.out.print("Class:"+TmExp.classLabel);
            System.out.println();
        }
    }
   
    public static void main(String[] args) {
        // TODO code application logic here
        String trainedFile = args[0];
        String testFile = args[1];
        ID3 inital = new ID3(trainedFile,testFile);
        inital.run();
    }
}
