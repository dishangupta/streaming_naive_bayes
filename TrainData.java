import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;


public class TrainData {
	
	private Map<String, Integer> yCounter;
	private Map<String, Integer> ywCounter;
	
	private int ystarCounter;
	private Map<String, Integer> ywstarCounter;
		
	public TrainData() {
		yCounter = new HashMap<String, Integer>();
		ywCounter = new HashMap<String, Integer>();		
		ywstarCounter = new HashMap<String, Integer>();
	}
	
	public Map<String, Integer> getYCounter (){
		return yCounter;
	}
	
	public Map<String, Integer> getYWCounter () {
		return ywCounter;
	}
	
	public int getYStarCounter() {
		ystarCounter = Utils.sumMapValues(yCounter);
		return ystarCounter;
	}
	

	public Map<String, Integer> getYWStarCounter (){
		return ywstarCounter;
	}
	
		
	public void buildCounters () {
		Vector<String> tokens;
		String[] docLabels;
		
		try {
			BufferedReader reader = new BufferedReader(		
	            new InputStreamReader(System.in, Charset.defaultCharset())); 
	            String line;
	            while ((line = reader.readLine()) != null) {
	               tokens =  Utils.tokenizeDoc(line);
	               
	               docLabels = Utils.getDocLabels(tokens.get(0));
	               buildYCounter(docLabels);
	               buildYWCounters(docLabels, tokens);
	               buildYWStar(docLabels, tokens);
	                           	                	
	            }
	        }
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("No Input from StdIn!");
		}
		
	}
	
	
	
	public void buildYCounter(String[] docLabels) {
		
		for (int i = 0; i < docLabels.length; i++) {
			Utils.buildMap(yCounter, "Y="+docLabels[i]);
		}
        	
	}
	
	public void buildYWCounters(String[] docLabels, Vector<String> tokens) {
		
		for (int i = 0; i < docLabels.length; i++) {
			for (int j = 1; j < tokens.size(); j++) {
				
				if (ywCounter.size() > Configuration.BUFFER_SIZE) {
					Utils.writeMapToStdOut(ywCounter);
					ywCounter.clear();
				}
				// Build W=?? and Y=?? string
				Utils.buildMap(ywCounter, "W=" + tokens.get(j) + 
							",Y=" + docLabels[i]);
				
			}
		}
	
	}
	
	public void buildYWStar (String[] docLabels, Vector<String> tokens) {
		
        	for (int i = 0; i < docLabels.length; i++) {
        	// Build Y=?? and W=* string
			Utils.buildMapN(ywstarCounter, "Y=" + docLabels[i] + 
					",W=*", tokens.size() - 1);
	
        	}
	}
}
