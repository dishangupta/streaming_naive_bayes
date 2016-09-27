
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class NBTrain {
	
	public static void main (String[] args) {
		
		TrainData trainData = new TrainData();
		trainData.buildCounters();
		Utils.writeMapToStdOut(trainData.getYWStarCounter());
		Utils.writeMapToStdOut(trainData.getYCounter());
		System.out.println("Y=*" + "\t"+ trainData.getYStarCounter());
	}

}
