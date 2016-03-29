/*
this is the class do learning, each labelled class have one such TextCounter class
 */
package textclass;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 *
 * @author zhongjiezheng
 */
public class TextCounter {
    public int startIndex=1;
    public int size=0;                 //number of example label in this class
    public double Pre_Probability=0;
    public int wordNumberInClass;
    String classDirectory;
    private ArrayList<File> fileList = new ArrayList<>();      //store all the file
    public ArrayList<String> wordsIndex = new ArrayList<>();    //store the words, there is no duplicate word inside
    public ArrayList<Integer> wordsFrequencies = new ArrayList<>();   //store word frequency, the actually word store in wordIndex. They shar same index
    public Set<String> wordLibrary = new HashSet<>();     //store all the words
    
    //constructor
    TextCounter(String classDirectory){
        this.classDirectory = classDirectory;
        running();
    }
   
    public void bagging(){
        
    }
    
    
    //this function control learning
    public void running(){
        System.out.println("Start working on learning:"+classDirectory);
        fileList = loadFileInDirectory(classDirectory);   //load all file in this class to fileList
        size=fileList.size();
        for(int i=startIndex;i<size;i++){ //iterative process each file
            File currentFile = fileList.get(i);   //get one file
            String fileContent = FileToString(currentFile);    //convert it to string
            LinkedList<String> wordsCollection = stringToWords(fileContent);   //word segment
            wordsCollection=stopwordsFilter(wordsCollection);       //kick out stop words
            wordsStatic(wordsCollection);        //get static info of these words
        }
        wordNumberCalculate();        //get number of all the words
    }
    
    
    public void wordsStatic(LinkedList<String> words){
        Queue<String> wordsQ = new LinkedList(words);
        while(!wordsQ.isEmpty()){
            String currentWord = wordsQ.poll();
            int wordIndex=0;
            
            if(wordLibrary.add(currentWord)){  //if there is no such word
                wordsIndex.add(currentWord);
                wordsFrequencies.add(1);
            }
            else{  //if the word already in the set
                wordIndex = wordsIndex.indexOf(currentWord);
                int frequency = wordsFrequencies.get(wordIndex)+1;
                wordsFrequencies.set(wordIndex, frequency);
            }
        }
        noiseCancel();
    }
    
    public void noiseCancel(){
        for(Iterator<String> iterator = wordsIndex.iterator(); iterator.hasNext();){
            String noise = iterator.next();
            //int indexOfNoise = wordsIndex.indexOf(noise);
            if(noise.length()>15){         //if one word length longer than 15, treat it as noise
                int indexOfNoise = wordsIndex.indexOf(noise);
                wordsFrequencies.remove(indexOfNoise);
                iterator.remove();
                wordLibrary.remove(noise);
            }
        }
        
//        for(Iterator<String> iterator = wordsIndex.iterator(); iterator.hasNext();){
//            String noise = iterator.next();
//            int indexOfNoise = wordsIndex.indexOf(noise);
//            if(wordsFrequencies.get(indexOfNoise)<2){
//                //int indexOfNoise = wordsIndex.indexOf(noise);
//                wordsFrequencies.remove(indexOfNoise);
//                iterator.remove();
//                wordLibrary.remove(noise);
//            }
//        }
    }
    
    public void wordNumberCalculate(){
        for(int num:wordsFrequencies){
            wordNumberInClass=wordNumberInClass+num;
        }
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
			//sb is a string which include all words and punctuation in a txt(mail)
			 s = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return s;
	}
   
    public static LinkedList<String> stringToWords(String s){
		//s = s.replaceAll( "[\\pP+~$`^=|<>～｀＄＾＋＝｜＜＞￥×]" , " ").replaceAll("((\r\n)|\n)[\\s\t ]*(\\1)+", "$1").replaceAll("\\d",""); 
                //s=s.replaceAll("[^A-Za-z]", " ").replaceAll(" {2,}", " ");
                s= filter(s);
                //s=s.replaceAll("[\\W]|_", " ");
		String[] s1 = s.split("(\40)|(\t)");
		LinkedList<String> words = new LinkedList<String>();
		for(int i = 0; i < s1.length; i ++){
			if(s1[i].length() != 0)
				words.add(s1[i].toLowerCase().trim());
		}
		return words;
		
	}
    
    public static final Pattern UNDESIRABLES = Pattern.compile("[^a-zA-Z\\\\s]");
    
    public static String filter(String input){
        return UNDESIRABLES.matcher(input).replaceAll(" ");
        //return input.replaceAll("[^a-zA-Z\\\\s]"," ");
        //return input.replace(input, input)
    }
    
    
    public static LinkedList<String> stopwordsFilter(LinkedList<String> input){
        stopwords stopword = new stopwords();
        input.removeAll(stopword.stopwords);
        return input;
    }
}
