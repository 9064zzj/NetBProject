/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package textclass;

import java.io.*;
import java.io.IOException;
import static java.lang.Math.log;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import static textclass.TextCounter.FileToString;
import static textclass.TextCounter.loadFileInDirectory;
import static textclass.TextCounter.stopwordsFilter;
import static textclass.TextCounter.stringToWords;

/**
 *
 * @author zhongjiezheng
 */
public class classifier {  //
    public int  startIndex=1;
    public int numberOfWord=0;     
    public int numberOfFile=0;
    public int totalNumOfLearnedDoc=0;      
    public int numOfClass=0;
    public String fileDir;
    public String outputFile;
    public ArrayList<TextCounter> results = new ArrayList<>();      
    private ArrayList<File> fileList = new ArrayList<>();
    private ArrayList<Label> output = new ArrayList<>();
    private ArrayList<Label> correct = new ArrayList<>();
    int numOfDocByCorrectClass[];
    int numOfDocByLabelClass[];
    
    //inner class for store classified label
    class Label{   //
        public int classLabel;
        public String fileName;
        public Label(int classLabel, String fileName){
            this.classLabel=classLabel;
            this.fileName = fileName;
        }
    }
    
    //constructor
    classifier(ArrayList results, int numberOfWord, String fileDir,String outputFile){
        this.results=results;
        this.numberOfWord = numberOfWord;
        this.fileDir = fileDir;
        this.outputFile=outputFile;
        numOfClass=results.size();
        numOfDocByCorrectClass=new int[numOfClass];
        numOfDocByLabelClass=new int[numOfClass];
        //running();
    }

    
    //this contorls test procedure
    public void running(){
        readCorrectFile();    //read correct label file
        System.out.println("Start working on testing:"+fileDir);
        fileList = loadFileInDirectory(fileDir);   //load all the test file
        numberOfFile=fileList.size();
        int correctNum=0;   //the number of correctly classified numbers
        
        for(int i=startIndex;i<numberOfFile;i++){ //iterative process each test file
            File currentFile = fileList.get(i);
            String fileName = currentFile.getName();
            
            String fileContent = FileToString(currentFile);
            LinkedList<String> wordsCollection = stringToWords(fileContent);
            wordsCollection=stopwordsFilter(wordsCollection); 
            int classifiedLabel = classify(wordsCollection);   //get the classified class label
            Label currentLabel = new Label(classifiedLabel,fileName);   //store the label
            output.add(currentLabel);
            
            //print correct rate
            System.out.println("Classifying: "+fileName);
            for(Label tmp:correct){
                if(fileName.equals(tmp.fileName)){
                    if(classifiedLabel==tmp.classLabel){
                        correctNum++;
                        numOfDocByLabelClass[tmp.classLabel]++;
                        numOfDocByCorrectClass[tmp.classLabel]++;
                        System.out.println("Correct! Label is "+classifiedLabel);
                    }
                    else{
                        numOfDocByCorrectClass[tmp.classLabel]++;
                        System.out.println("Incorrect! Correct Label is "+tmp.classLabel+" classified label is "+classifiedLabel);
                    }
                }
            }
            
        }
        //show static
       double p = (double)(correctNum)/(double)(numberOfFile);
       System.out.println("Correct rate:"+p); 
       System.out.println();
       for(int i=0;i<numOfClass;i++){
           double rate=(double)(numOfDocByLabelClass[i])/(double)(numOfDocByCorrectClass[i]);
           System.out.println("Correct rate for class "+i+" is "+rate);
       }
       
       
       
        try{
            writeOutput();
        }
        catch(IOException e) {
           System.out.println("error"+e.getMessage());   
        }
    }
    
    
    
    public void writeOutput() throws IOException{
        PrintWriter pw = new PrintWriter(new FileWriter(outputFile));
        for(int i = startIndex; i < numberOfFile; i++){
            Label current = output.get(i-1);
            pw.println(current.fileName+" "+current.classLabel);
        }
        pw.close();
    }
    
    //this function returns classified label
    public int classify(LinkedList<String> wordsCollection){
        //calculate coniditional probability for every class
        int classLabel=0;
        double maxProb=0.0;
        for(int i=0; i<numOfClass; i++){   //iterate calculate conditional probability for every class.
            TextCounter currentClass = results.get(i);
            double WordProb = GetWordProb(wordsCollection,currentClass);    //get P(w|c) for all w
            double classProbability = log(currentClass.Pre_Probability)+WordProb;  //P(c)*P(w|c)
            //System.out.println("Prob is "+classProbability);
            if(i==0)
                maxProb = classProbability;   //find max P
            
            if(maxProb<classProbability){
                maxProb=classProbability;
                classLabel = i;
            }
        }
        return classLabel;
        
    }
    
    
    //calculate P(w|c) for all the w
    public double GetWordProb(LinkedList<String> wordsCollection,TextCounter currentClass){
        int wordSize = wordsCollection.size();
        double WordProb =0.0;
        
        for(int i=0; i < wordSize; i++){
            String currentWord = wordsCollection.get(i);
            if(currentWord.length()>15)
                continue;
            double WordConditionalProb;
            int indexOfWord = currentClass.wordsIndex.indexOf(currentWord);
            if(indexOfWord == -1){
                WordConditionalProb = 1.0/(double)(currentClass.wordNumberInClass+numberOfWord);
            }
            else{
                WordConditionalProb = (double)(currentClass.wordsFrequencies.get(indexOfWord)+1.0)/(double)(currentClass.wordNumberInClass+numberOfWord);
                
            }
//            if(WordConditionalProb<=0.0)
//                    System.out.println("Crazzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzy: "+WordConditionalProb);
            WordProb = WordProb+log(WordConditionalProb);   //because the number is too small, use log here
        }
        return WordProb;
    }
    
    public static ArrayList<File> loadFileInDirectory(String directory){
		ArrayList<File> fileList = new ArrayList<File>();
		try {
			Files.walk(Paths.get(directory)).forEach(filePath -> {
				
			    if (Files.isRegularFile(filePath)) {
			        fileList.add(filePath.toFile());
			        
			    }
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileList;
    }
    
    public static String FileToString(File f){
		StringBuffer sb = new StringBuffer();
		String s = "";
		//load file
		try {
			InputStreamReader reader = new InputStreamReader(new FileInputStream(f));
			BufferedReader br = new BufferedReader(reader);
			String line = "";
			while((line = br.readLine()) != null){
				sb.append(line);
			}
			 s = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return s;
	}
    
    public static LinkedList<String> stringToWords(String s){
		//s = s.replaceAll( "[\\pP+~$`^=|<>～｀＄＾＋＝｜＜＞￥×]" , " ").replaceAll("((\r\n)|\n)[\\s\t ]*(\\1)+", "$1").replaceAll("\\d",""); 
                s=s.replaceAll("[^A-Za-z]+", " ");//.replaceAll(" {2,}", " ");
                //s=s.replaceAll("[\\W]|_", " ");
		String[] s1 = s.split("(\40)|(\t)");
		LinkedList<String> words = new LinkedList<String>();
		for(int i = 0; i < s1.length; i ++){
			if(s1[i].length() != 0)
				words.add(s1[i].toLowerCase().trim());
		}
		return words;
		
        }
    
    public void readCorrectFile(){
        String fileName = "/Users/zhongjiezheng/Desktop/project/dev_label.txt";
        try{
            FileInputStream fstream = new FileInputStream(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String info[];
            String line;
            while((line=br.readLine())!=null){
                info = line.split(" ");
                String file = info[0];
                int label = Integer.parseInt(info[1]);
                Label tmp = new Label(label,file);
                correct.add(tmp);
            }
          
        }
        catch(IOException e) {
           System.out.println("error"+e.getMessage());   

        }
    }
    
    public static LinkedList<String> stopwordsFilter(LinkedList<String> input){
        stopwords stopword = new stopwords();
        input.removeAll(stopword.stopwords);
        return input;
    }
    
}
