
public class NBTest {
	
	public static void main(String[] args) {
		
		TestData testData = new TestData(args[0]);
		testData.storeWords();
		testData.readCountsFromStdIn();
		testData.predictDocumentClass();
	}
}
