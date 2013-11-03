/**
 * 
 */
package com.saba.vocabquestion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.Pointer;

/**
 * @author ratish
 *
 */
public class VocabularyQuestionCreation {
	private static final String SPACE = " ";
	private IDictionary dict = null;
	private Map<String, Integer> wordCounts = new HashMap<String,Integer>();
	private Map<Integer, String > countsWords = new HashMap<Integer, String>();
	public static void main(String[] args) {
		VocabularyQuestionCreation vocabularyQuestionCreation = new VocabularyQuestionCreation();
		vocabularyQuestionCreation.execute();
	}

	private void loadWordnet(){
		try {
			String wnhome = "/home/ratish/Downloads/WordNet-3.0";
			String path = wnhome + File . separator + "dict" ;
			URL url = new URL ( "file" , null , path ) ;
			// construct the dictionary object and open it
			dict = new Dictionary ( url ) ;
			dict . open () ;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void execute() {
		loadWordnet();
		
	}
	
	private void loadCountsFile(){
		try {
			BufferedReader br = new BufferedReader(new FileReader("/home/ratish/project/vocabquestiongeneration/all.al.o5"));
			String readLine = null;
			while((readLine = br.readLine())!= null){
				if(readLine.indexOf("aj0 ")!= -1){
					String [] args = readLine.split(SPACE);
					wordCounts.put(args[1], Integer.valueOf(args[0]));
					countsWords.put(Integer.valueOf(args[0]), args[1]);
				}
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private List<String> getDistractors(String input){
		List<String> distractors = new ArrayList<String>();
		Integer count = wordCounts.get(input);
		for(int i = count -10; i<count+10; i++){
			if(countsWords.containsKey(i)){
				distractors.add(countsWords.get(i));
			}
		}
		return distractors;
	}
	
	private void getVocabularyQuestions(){
		IIndexWord idxWord = dict.getIndexWord ( "happy" , POS.ADJECTIVE) ;
		IWordID wordID = idxWord . getWordIDs () . get (0) ; // 1 st meaning
		IWord word = dict . getWord ( wordID ) ;
		List<IWordID> wordIds = word.getRelatedWords(Pointer.ANTONYM);
		for (IWordID iWordID : wordIds) {
			System.out.println("Antonym "+dict.getWord(iWordID));
		}
		ISynset synset = word.getSynset();
		List<IWord> synsetWords = synset.getWords();
		System.out.println(" tagCount "+idxWord.getTagSenseCount());
		for (IWord iWord : synsetWords) {
			System.out.println("Synonym "+iWord.getLemma() );
		}
		System.out.println("id "+synset.getID());
		System.out.println("gloss "+synset.getGloss());
		System.out.println("lex "+synset.getLexicalFile());
		System.out.println("offset "+synset.getOffset());
		
	}

}
