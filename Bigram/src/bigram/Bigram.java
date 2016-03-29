/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bigram;

import java.io.*;
import java.lang.*;
import java.util.*;

/**
 *
 * @author zhongjiezheng
 */
public class Bigram {

    /**
     * @param args the command line arguments
     */
    public static final String defaultCorpusPath = "/Users/zhongjiezheng/Desktop/15F/NLP/Homework/HW2/NLPCorpusTreebank2Parts.txt";
    public String sentence;
    public double NoSoomth;
    public double AddOneSoomth;
    public double GoodTuring;
    public int numOfWords;
    private HashSet wordBase = new HashSet();
    private ArrayList<String> sentenceWords;
    private int[][] bigramCounts;
    private int[] unigramCounts;
   // private PorterStemmer stemmer = new PorterStemmer();
    
    public Bigram(String sentence){
        this.sentence = sentence;
    }
    
    private void countFromCorpus(){
        File file = new File(defaultCorpusPath);
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            String[] word;
            int lastWordIndex = -1;
            int index;
            int nextIndex;
            while( (line = br.readLine())!= null ){
                word = line.split("(\\s+)|(\\tab+)|(\\\\\\/+)");
                for(int i=0;i< word.length; i++){
                    //get stem of the word
                    //word[i] = stemmer.stem(word[i]);
                    //add to wordBase
                    
                    
                    index = sentenceWords.indexOf(word[i]);
                    //count unigram
                    if(index != -1)
                        unigramCounts[index]++;
                    ////////////////////////////////////////////
                    //count bigram
                    //handle first word in a line
                    if(i == 0){
                        //check last from previous line and 1st in current line
                        if(index != -1 && lastWordIndex != -1){
                            bigramCounts[lastWordIndex][index]++;
                        }
                        //check 1st and 2nd word in current line
                        else if(index !=-1 && word.length > 1){
                            nextIndex = sentenceWords.indexOf(word[1]);
                            if(nextIndex != -1){
                                bigramCounts[index][nextIndex]++;
                            }
                        }
                        lastWordIndex = -1;
                    }
                    //handle last word in a line
                    if(i == word.length -1){
                        if(index != -1)
                            lastWordIndex = index;
                        else
                            lastWordIndex = -1;
                    }
                    //handle the rest
                    else{
                        if(index != -1){
                            nextIndex = sentenceWords.indexOf(word[i+1]);
                            if(nextIndex != -1){
                                bigramCounts[index][nextIndex]++;
                            }
                        }
                    }
                }
            }
        }
        catch(IOException ex){
            System.out.println("Didn't find corpus file or file is damaged");
            return;
        };
        
        System.out.println("The unigram count for each word is: (same sequence as words in sentence)");
        for(int tmp: unigramCounts){
            System.out.print(tmp+" ");
        }
        System.out.println("\n");
        
        System.out.println("The bigram count for each word is: (the horizontial sequence is the same as words in sentence)");
        
        for(int i = 0; i< numOfWords;i++){
            int[] tmp1 = bigramCounts[i];
            System.out.print(sentenceWords.get(i)+": ");
            for(int tmp: tmp1){
                System.out.print(tmp+" ");
            }
            System.out.println();
        }
        System.out.println();
    }
    
    private void Initialization(){
        sentence = sentence.trim();
        sentence = sentence.toLowerCase();
        sentenceWords = new ArrayList<>(Arrays.asList(sentence.split(" ")));
        numOfWords = sentenceWords.size();
        //initial bigram count array.
        bigramCounts = new int[numOfWords][numOfWords];
        //initial unigram count array.
        unigramCounts  = new int[numOfWords]; 
        
    }
    
    private void calculate(){
        NoSmooth();
        AddOne();
        GoodTuring();
        
        if(numOfWords <= 1){
            System.out.println("Can not compute bigram, make sure there are at least 2 words in your sentence");
            return;
        }
        printNoSoomthTable();
        printAddOneTable();
        printGoodTuringTable();
        
        if(NoSoomth >= 0){
            System.out.println("The sentence probability is: "+NoSoomth+" (No Soomthing)");
        }
        else{
            System.out.println("The no-soomthing sentence probability can not be calculated becasue there are some words appear zero times.");
        }
        
        System.out.println("The sentence probability is: "+AddOneSoomth+" (Add-one Soomthing)");
        
        System.out.println("The sentence probability is: "+GoodTuring+" (Good-Turing)");
    }
    
    private void printNoSoomthTable(){
        System.out.println("The bigram probabilities (No-Soomthing) for each word is: (the horizontial sequence is the same as words in sentence)");
        double bigramProb=0;
         
        for(int i = 0; i< numOfWords;i++){
            int[] tmp1 = bigramCounts[i];
            System.out.print(sentenceWords.get(i)+": ");
            for(int tmp: tmp1){
                if(unigramCounts[i]!=0){
                    bigramProb = (double)(tmp)/(double)(unigramCounts[i]);
                    System.out.print(bigramProb+" ");
                }
                else{
                    System.out.print("NULL/Divided by 0 ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
    
    private void printAddOneTable(){
        System.out.println("The bigram probabilities (Add-One) for each word is: (the horizontial sequence is the same as words in sentence)");
        double bigramProb=0;
        int V = wordBase.size();
        for(int i = 0; i< numOfWords;i++){
            int[] tmp1 = bigramCounts[i];
            System.out.print(sentenceWords.get(i)+": ");
            for(int tmp: tmp1){
                bigramProb = (double)(tmp+1)/(double)(unigramCounts[i]+V);
                System.out.print(bigramProb+" ");
            }
            System.out.println();
        }
        System.out.println();
    }
    
    private void printGoodTuringTable(){
        System.out.println("The bigram probabilities (Good-Turing) for each word is: (the horizontial sequence is the same as words in sentence)");
        double bigramProb=0;
        int c, Nc,Nc1,N=0;
        double cStar;
        for(int[] tmp1:bigramCounts){
            for(int tmp: tmp1){
                N += tmp;
            }
        }
        for(int i = 0; i< numOfWords;i++){
            int[] tmp1 = bigramCounts[i];
            System.out.print(sentenceWords.get(i)+": ");
            for(int tmp: tmp1){
                c = tmp;
                if(c!=0){
                   Nc = NcCounter(c);
                   Nc1 = NcCounter(c+1);
                   cStar = (double)((c+1)*Nc1)/(double)(Nc);
                   bigramProb = cStar/(double)(N);
                }
                else{
                     Nc1 = NcCounter(c+1);
                     bigramProb = (double)(Nc1)/(double)(N);
                }
                System.out.print(bigramProb+" ");
            }
            System.out.println();
        }
        System.out.println();
        
    }
    
    private void NoSmooth(){
        NoSoomth = 1;
        if(numOfWords == 1)
            NoSoomth = -1;
        for(int i=0; i<numOfWords-1;i++){
            double conditionalProb;
            if(unigramCounts[i] != 0){
               conditionalProb = (double)(bigramCounts[i][i+1])/(double)(unigramCounts[i]);
            }
            else{
                conditionalProb = -1;
            }
            NoSoomth = NoSoomth*conditionalProb;
        }
    }
    
    private void AddOne(){
        int V = wordBase.size();
        AddOneSoomth =1;
        if(numOfWords == 1)
            AddOneSoomth= -1;
        
        double conditionalProb;
        for(int i=0; i<numOfWords-1;i++){
            conditionalProb = (double)(bigramCounts[i][i+1]+1)/(double)(unigramCounts[i]+V);
            AddOneSoomth =AddOneSoomth*conditionalProb;
        }
    }
    
    private void GoodTuring(){
        GoodTuring = 1;
        if(numOfWords == 1)
            AddOneSoomth= -1;
        double conditionalProb;
        int c, Nc,Nc1,N=0;
        double cStar;
        
        for(int[] tmp1:bigramCounts){
            for(int tmp: tmp1){
                N += tmp;
            }
        }
        
        
        for(int i=0; i<numOfWords-1;i++){
            c = bigramCounts[i][i+1];
            if(c!=0){
                Nc = NcCounter(c);
                Nc1 = NcCounter(c+1);
                cStar = (double)((c+1)*Nc1)/(double)(Nc);
                conditionalProb = cStar/(double)(N);
            }
            else{
                Nc1 = NcCounter(c+1);
                conditionalProb = (double)(Nc1)/(double)(N);
            }
            GoodTuring = GoodTuring * conditionalProb;
        }
    }
    
    private int NcCounter(int c){
        int countOfC = 0;
        for(int[] tmp1: bigramCounts){
            for(int tmp: tmp1){
                if(tmp == c)
                    countOfC++;
            }
        }
        return countOfC;
    }
    
    public void run(){
        Initialization();
        countFromCorpus();
        calculate();
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner in = new Scanner(System.in);
        System.out.println("The default corpus file path is:"+defaultCorpusPath);
        System.out.println("Please type the sentence:(No punctuation and numeric number please)");
        String sentence = in.nextLine();
        System.out.println();
        Bigram demo = new Bigram(sentence);
        demo.run();
    }
    
}
