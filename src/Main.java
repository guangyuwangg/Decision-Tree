import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

public class Main {
	public static int count = 0;
	public static Vector<Doc> docs;
	public static Vector<Doc> actualClasses;
	public static Vector<Doc> testDocs;
	public static Vector<theta> thetas;
//	public static Vector<Doc> trainDocs;
	public static Vector<Node> PQ;
	public static int type = 2;
	public static int countTest = 0;
	
	
	public static void initializeThetas(){
		for(int i = 0; i<thetas.size(); i++){
			int aHasThisWord = 0;
			int gHasThisWord = 0;
			for(int j=0; j<docs.size();j++){
				if(docs.get(j).hasWord(thetas.get(i).id)){
					if(docs.get(j).getGroup() == 1){
						aHasThisWord++;
					}
					else{
						gHasThisWord++;
					}
				}
			}
			thetas.get(i).numInAtheism = aHasThisWord;
			thetas.get(i).numInGraphics = gHasThisWord;
		}
		
	}
	
	public static int calLabel(Doc doc){
		double Patheism = 0;
		double Pgraphics = 0;
		
		int size = thetas.size();
		
		for(int i=0; i<thetas.size(); i++){
			int t_id = thetas.get(i).id;
			if(doc.hasWord(t_id)){
				Patheism = Patheism + (double)(Math.log10(thetas.get(i).getPa()));
				Pgraphics = Pgraphics + (double)(Math.log10(thetas.get(i).getPg()));
			}
		}
		
		//make decision
		System.out.println("PA: "+Patheism+" PG: "+Pgraphics);
		if(Patheism > Pgraphics){
			return 1;
		}
		else {
			return 2;
		}
	}

	
	public static void main(String[] args) {

		docs = new Vector<Doc>();
		thetas = new Vector<theta>();
		actualClasses = new Vector<Doc>();
		testDocs = new Vector<Doc>();
		
//		trainDocs = new Vector<Doc>();
		
		BufferedReader br = null;
		
		//load words and initialize theta objects
		try {
			String sCurrentLine;
			int wordId = 0;
			br = new BufferedReader(new FileReader("src/words.txt"));
			for(int i=0; (sCurrentLine = br.readLine()) != null;i++){
				wordId = i;
				theta m_t = new theta();
				m_t.id = wordId;
				m_t.word = sCurrentLine;
				thetas.add(m_t);
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		//read labels for training
		try {	 
			String sCurrentLine;
			Doc m_doc;
			br = new BufferedReader(new FileReader("src/trainLabel.txt"));
			while ((sCurrentLine = br.readLine()) != null){
				m_doc = new Doc();
				m_doc.setGroup(Integer.parseInt(sCurrentLine));
				docs.add(m_doc);
				
//				m_doc = new Doc();
//				trainDocs.add(m_doc);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		//read labels for testing
		try {	 
			String sCurrentLine;
			Doc m_doc;
			br = new BufferedReader(new FileReader("src/testLabel.txt"));
			while ((sCurrentLine = br.readLine()) != null){
				m_doc = new Doc();
				m_doc.setGroup(Integer.parseInt(sCurrentLine));
				actualClasses.add(m_doc);
				
				//create an empty doc for testData
				m_doc = new Doc();
				testDocs.add(m_doc);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		try {
 
			String sCurrentLine;
			String []tokens;
			String deli = "[	]+";
			int docId = 0;
			docs.get(0).initWords();
//			trainDocs.get(0).initWords();
			br = new BufferedReader(new FileReader("src/trainData.txt"));
			for(int i=0; (sCurrentLine = br.readLine()) != null;){
				tokens = sCurrentLine.split(deli);
				docId = Integer.parseInt(tokens[0]);
				if(i+1 == docId){
					docs.get(i).addWord(Integer.parseInt(tokens[1]));
//					trainDocs.get(i).addWord(Integer.parseInt(tokens[1]));
				}
				else{
					i++;
					docs.get(i).initWords();
					docs.get(i).addWord(Integer.parseInt(tokens[1]));
					
//					trainDocs.get(i).initWords();
//					trainDocs.get(i).addWord(Integer.parseInt(tokens[1]));
				}
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		//read data for testing
		try {
			 
			String sCurrentLine;
			String []tokens;
			String deli = "[	]+";
			int docId = 0;
			testDocs.get(0).initWords();
			br = new BufferedReader(new FileReader("src/testData.txt"));
			for(int i=0; (sCurrentLine = br.readLine()) != null;){
				tokens = sCurrentLine.split(deli);
				docId = Integer.parseInt(tokens[0]);
				if(i+1 == docId){
					testDocs.get(i).addWord(Integer.parseInt(tokens[1]));
				}
				else{
					i++;
					testDocs.get(i).initWords();
					testDocs.get(i).addWord(Integer.parseInt(tokens[1]));
				}
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		initializeThetas();
		
		int numCorrect = 0;
		int test_size = testDocs.size();
		for(int k = 0; k<test_size; k++){
			int label = calLabel(testDocs.get(k));
			if(label == actualClasses.get(k).getGroup()){
				numCorrect++;
			}
		}
		System.out.println(numCorrect);
	}
}
