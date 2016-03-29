/*
This the main part, please read comment in main function as the beginning.
 */
package textclass;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 *
 * @author zhongjiezheng
 */
public class TextClass {

    /**
     * @param args the command line arguments
     */
    int numberOfClass=0;              //total number of class
    int numberOfWords=0;              //total number of distinctive word
    int numberOfFile=0;               //total number of learned example
    String testFileDirectory;         //where the test set is
    String outputFile;                //output file name
    String rootDiectory="/Users/zhongjiezheng/Desktop/project";        //where the file package is.    
    ArrayList<String> classLabels = new ArrayList<>();         //The name of class
    ArrayList<TextCounter> results = new ArrayList<>();        //Store a self defined class that keep learned models
    Set<String> wordsLibrary = new HashSet();                 //words library.
    
    
    //the constructor
    TextClass(String testFileDirectory, String outputFile){
        this.testFileDirectory=testFileDirectory;
        this.outputFile=outputFile;
    }
    
    //this is the key part of this program, this method controlls everything
    public void running(){
        boolean getclass = getClassLabel();      ///get class label from class_name.txt
        if(!getclass){
            return;
        }
        //testLabel()
        long startLearnTime = System.nanoTime();   
        Learning();                                //the learning part.
        long endLearnTime =System.nanoTime(); 
        Testing();                                // the testing part
        long endTime =System.nanoTime();
        
        System.out.println("Learning Time: "+(endLearnTime-startLearnTime)/1000000000);
        System.out.println("Testing Time: "+(endTime-endLearnTime)/1000000000);
        
    }
    
    public void Testing(){
        classifier classification = new classifier(results,numberOfWords,testFileDirectory,outputFile);
        classification.running();
    }
    
    
    //this method control learning procedure
    public void Learning(){
        String classFileDirectory;        //where learning file is
        for(int classIndex=0; classIndex<numberOfClass;classIndex++ ){ //processsing file class by class
            String classLabel = classLabels.get(classIndex);
            classFileDirectory=rootDiectory.concat("/train/"+classLabel+"/");    //get directory of current traning class example
            TextCounter classResult  = new TextCounter(classFileDirectory);       //learn model of this class
            LearnResult(classResult);               
            addWordsLibrary(classResult);           
            numberOfFile=numberOfFile+classResult.size;        
            results.add(classResult);            //add the learned model to list
        }
        numberOfWords=wordsLibrary.size();
        getTheTotalNumOfLearnedDoc();
        System.out.println("There are "+numberOfWords+" distinct words in library");
        
    }
    
    //calculate Nc/N here
    public void getTheTotalNumOfLearnedDoc(){   
        for(TextCounter tmp:results){
            double p = (double)(tmp.size)/(double)(numberOfFile);
            tmp.Pre_Probability=p;
            if(tmp.Pre_Probability<=0)
                System.out.println("Pre_Probability is 0 or minus: "+tmp.Pre_Probability);
        }
    }
    
    //combine words from every class to a big library.
    public void addWordsLibrary(TextCounter Result){
        for(String word:Result.wordsIndex){
            wordsLibrary.add(word);
        }
    }
    
    //test
    public void testLabel(){
        for(String tmp:classLabels){
            System.out.println(tmp);
        }
    }
    
    
    //output some learned data
    public void LearnResult(TextCounter Result){
        System.out.println("There are "+Result.size+" files");
        System.out.println("There are "+Result.wordsIndex.size()+" distinct words");
//        for(String tmp:Result.wordsIndex){
//            System.out.print(tmp+" ");
//        }
    }
    
    
    //read from class_name.txt
    public boolean getClassLabel(){
        String fileName = rootDiectory+"/class_name.txt";
        try{
            FileInputStream fstream = new FileInputStream(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String info[];
            String line;
            while((line=br.readLine())!=null){
                info = line.split(" ");
                String className = info[1];
                classLabels.add(className);
            }
            numberOfClass = classLabels.size();
        }
        catch(IOException e) {
           System.out.println("error"+e.getMessage());   
           return false;
        }
        return true;
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        String testFile = args[0];  //get the testfile directory, be sure you have / in the end.
        String outputFile = args[1];  //get the name of output file
        TextClass demo = new TextClass(testFile,outputFile);   //initial main class
        demo.running();  //run the function
    }
    
}
