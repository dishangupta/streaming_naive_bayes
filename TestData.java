
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.Arrays;

public class TestData {
	
	private File testFile;
			
	private Map<String, Integer> yCount;
	private Map<String, Integer> ywCountSubset;
	
	private int ystarCount;
	private Map<String, Integer> ywstarCount;
	
	private Set<String> words;
	private int vocabSize;
	
			
	public TestData (String filePath) {
		testFile = new File(filePath);
		yCount = new HashMap<String, Integer>();
		ywCountSubset = new HashMap<String, Integer>();
		ywstarCount = new HashMap<String, Integer>();
		words = new HashSet<String>();
	}
	
	public int getVocabSize() {
		return vocabSize;
	}
	
	public Map<String, Integer> getYCount (){
		return yCount;
	}
	
	public Map<String, Integer> getYWCount () {
		return ywCountSubset;
	}
	
	public int getYStarCount() {
		return ystarCount;
	}
	

	public Map<String, Integer> getYWStarCount (){
		return ywstarCount;
	}
	
	public void storeWords () {
		String line;
		Vector<String> tokens;
		FileReader file;
		try {
	        file = new FileReader(testFile);
	        BufferedReader reader = new BufferedReader(file);
	        
	        try {
	            while ((line = reader.readLine()) != null) {
	            	tokens = Utils.tokenizeDoc(line);
	            	
	            	for (int i = 1; i < tokens.size(); i++) {
	        			if (!words.contains(tokens.get(i)))
	        				words.add(tokens.get(i));	
	        		}
	            
	            }
	            
	        } finally {
                    reader.close();
            }    
	        
		} catch (FileNotFoundException e) {
	        throw new RuntimeException("File not found");
	    } catch (IOException e) {
	        throw new RuntimeException("IO Error occured");
	    }
	}
	
	public void readCountsFromStdIn () {
		int eventIndex;
		String[] eventRegex = new String[]{"W=\\w+,Y=\\w+", "Y=\\w+", "Y=\\*", "Y=\\w+,W=\\*"};
		String[] eventCount;
		try {
			BufferedReader reader = new BufferedReader(		
	            new InputStreamReader(System.in, Charset.defaultCharset())); 
	            String line;
	            while ((line = reader.readLine()) != null) {
	            	eventCount = line.split("\t");
	            	
	            	// 0:W=w,Y=y, 1:Y=y; 2:Y=*; 3:Y=y,W=*
	            	eventIndex = getEventIndex(eventCount[0], eventRegex);
	            	buildCounts(eventCount, eventIndex);
	                                       	                	
	            }
	        }
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error reading counts!");
		}
		
	}
	
	public int getEventIndex (String event, String[] eventRegex) {
		
		for (int i = 0; i < eventRegex.length; i++){
			if (event.matches(eventRegex[i]))
				return i;
		}
		
		return -1;
	}
	
	public void buildCounts (String[] eventCount, int eventIndex) {
		String word;
		String event = eventCount[0];
		int count = Integer.parseInt(eventCount[1]);
		
		if (eventIndex == 0) {
			word = event.split(",")[0];
			word = word.substring(2, word.length());
			if (words.contains(word))
				ywCountSubset.put(event, count);
		}
		
		else if (eventIndex == 1)  
			yCount.put(event, count);
		
			
		else if (eventIndex == 2)
			ystarCount = count;
				
		else if (eventIndex == 3)
			ywstarCount.put(event, count);
		
		else 
			vocabSize = count;
			
	}
	
	public void predictDocumentClass () {
		double logProb, probWgivenY;
		double maxLogProb;
		String bestClass = "y";
		String line, yw, ywStar;
		Vector<String> tokens;
		FileReader file;
		
		try {
	        	file = new FileReader(testFile);
	        	BufferedReader reader = new BufferedReader(file);
	        	try {
	            		while ((line = reader.readLine()) != null) {
	            			maxLogProb = Double.NEGATIVE_INFINITY;
	            			tokens = Utils.tokenizeDoc(line);
	            	
	            			// Compute log probability for each class
	            			// with Laplace alpha = 1 smoothing
	        			for (String s: yCount.keySet()) {
	        				logProb = Math.log((double) (yCount.get(s)+1)
	        					/(ystarCount+yCount.keySet().size()));
	        			
	        				ywStar = s+",W=*";
	        				for (int i = 1; i < tokens.size(); i++) {
	        					yw = "W="+tokens.get(i)+","+s;
	        					probWgivenY = getConditionalProb(yw, ywStar);
							logProb = logProb + Math.log(probWgivenY);
	        				}
	        				        			
	        				if (logProb >= maxLogProb) {
	        					maxLogProb = logProb;
	        					bestClass = s;
	        				}
	        			}
	        		System.out.println(bestClass.substring(2) + "\t" + maxLogProb);
	            		}
			} finally {
                    		reader.close();
            		}    
	        } catch (FileNotFoundException e) {
	        	throw new RuntimeException("File not found");
	    	} catch (IOException e) {
	        	throw new RuntimeException("IO Error occured");
	    	}
		
	}
	
	public double getConditionalProb(String joint, String marginal) {
		double prob;
		
		if (ywCountSubset.containsKey(joint))
			prob = ywCountSubset.get(joint) + 3;
		else
			prob = 3;
		
		if (ywstarCount.containsKey(marginal))
			prob = (double) prob/(ywstarCount.get(marginal) + 3*vocabSize);
		else
			prob = (double) prob/(3*vocabSize);
			
		return prob;
	}
}
