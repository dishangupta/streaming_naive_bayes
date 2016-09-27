
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Vector;

public class Utils {

	public static Vector<String> tokenizeDoc(String cur_doc) {
		String[] words = cur_doc.split("\\s+");
		Vector<String> tokens = new Vector<String>();
		
		tokens.add(words[0]);
		for (int i = 1; i < words.length; i++) {
			words[i] = words[i].replaceAll("\\W", "");
			words[i] = words[i].replaceAll("\\d+.*", "");
			
			if (words[i].length() > 0) {
				tokens.add(words[i]);
			}
		
		}
		return tokens;
	}

	
	
	public static String[] getDocLabels (String token) {
		return token.split(",");
	}

	public static void buildMap(Map<String, Integer> map, String str) {
		if (map.containsKey(str))
			map.put(str, map.get(str) + 1);
		else 
			map.put(str, 1);
	}
	
	public static void buildMapN(Map<String, Integer> map, String str, int N) {
		if (map.containsKey(str))
			map.put(str, map.get(str) + N);
		else 
			map.put(str, N);
	}
	
	public static int sumMapValues (Map<String, Integer> map) {
		int count = 0;
		for (String s: map.keySet())
			count = count + map.get(s);
		return count;
	}
	
	public static void writeMapToStdOut (Map<String, Integer> map) {
		try {    
			  BufferedWriter log = new BufferedWriter(new 
					  OutputStreamWriter(System.out));

			  for (String s: map.keySet()) 
				log.write(s+"\t"+map.get(s)+"\n");
			  log.flush();
			  
			}
			catch (Exception e) {
			  e.printStackTrace();
			}
		
	}


}
