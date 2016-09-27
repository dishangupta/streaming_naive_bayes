
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Vector;


public class MergeCounts {
	
	public static void main (String[] args) {
		
		String key;
		String previousKey = null;
		
		String word;
		String previousWord = null;
		int count = 0, vocabSize = 0;
				
		try {
			BufferedReader reader = new BufferedReader(		
	            new InputStreamReader(System.in, Charset.defaultCharset())); 
	            String line;
	            while ((line = reader.readLine()) != null) {
	            	
	            	key = line.split("\t")[0];
	            	word = key.split(",")[0];
	            		            		            	
	            	if (key.equals(previousKey))
	            		count = count + Integer.parseInt(line.split("\t")[1]);
	            	else {
	            		if (previousKey != null)
	            			System.out.println(previousKey + "\t" + count);
	            		count = Integer.parseInt(line.split("\t")[1]);
	            		previousKey = key;
	            	}
	            		
	            	// Compute vocab size
	            	if (word.equals(previousWord))
	            		continue;
	            	else {
	            		vocabSize++;
	            		previousWord = word;
	            	}
	            }
	            System.out.println(previousKey + "\t" + count);
	            System.out.println("Vocab Size:" + "\t" + vocabSize);
	        }
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("No Input from StdIn!");
		}
		
	}

}
