import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

public class CopyOfMain {
	public static int count = 0;
	public static Vector<Doc> docs;
	public static Vector<Doc> actualClasses;
	public static Vector<Doc> testDocs;
//	public static Vector<Doc> trainDocs;
	public static Vector<Node> PQ;
	public static int type = 2;
	public static int countTest = 0;
	
	public static double calculateInfo(Vector<Doc> set){
		double numA=0;
		double numG=0;
		double size = set.size();
		
		//if size is 0, than no need to calculate because info gain is 1
		if(size == 0){
			return 1;
		}
		//count how many document is in graphics and atheism
		for(int i = 0; i<size; i++){
			if(set.get(i).getGroup() == 1){
				numA++;
			}
			else{
				numG++;
			}
		}
		if(numA==0||numG==0){
			return 0;
		}
		double pa = numA/size;
		double pg = numG/size;
		double info = (Math.log10(pa)/Math.log10(2))*pa*(-1)+(Math.log10(pg)/Math.log10(2))*pg*(-1);
		return info;
	}
	
	/*
	 * Insert a specific node into the priority queue according to IG
	 */
	public static void insertIntoPQ(Node node){
		if(PQ.size() == 0){
			PQ.add(node);
			return;
		}
		else {
			for(int i=0; i<PQ.size(); i++){
				if(node.getMaxIG() > PQ.get(i).getMaxIG()){
					PQ.insertElementAt(node, i);
					return;
				}
			}
			PQ.add(node);
		}
		return;
	}
	
	/*
	 * Generate a new node here.
	 */
	public static Node generateNewNode(Vector<Doc> set){
		double maxIG=0;
		int maxFeature=0;
		int aCount = 0;
		int gCount = 0;
		boolean stop = false;
		double IG;
		double IE;
		double IE1;
		double IE2;
		double p = 0;
		int size = set.size();
		IE = calculateInfo(set);
		if(size == 0){
			maxIG = IE;
			stop = true;
		}
		else{
			for(int i=0; i<3565; i++){  //there are 3565 words,try to separate according to each work
				Vector<Doc>group1 = new Vector<Doc>();		//has the word
				Vector<Doc>group2 = new Vector<Doc>();		//doesn't has the word
				aCount = 0;
				gCount = 0;
				for(int j=0; j<size;j++){
					//use aCount/gCount to check if all doc are in same group, if yes, this is a stop
					if(set.get(j).getGroup() == 1){
						aCount++;
					}
					else{
						gCount++;
					}
					if(set.get(j).hasWord(i)){
						group1.add(set.get(j));
					}
					else{
						group2.add(set.get(j));
					}
				}
				
				
				// calculate IE for both groups, then calculate IG
				IE1 = calculateInfo(group1);
				IE2 = calculateInfo(group2);
				if(type == 1){
					IG = IE - (((double)1/2)*IE1+((double)1/2)*IE2);
				}
				else {
					IG = IE - (((double)group1.size()/size)*IE1+((double)group2.size()/size)*IE2);
				}
				if(IG>=maxIG){
					maxIG = IG;
					maxFeature = i;
					p = (double)aCount/set.size();
					
				}
				if(aCount==0 || gCount==0){
					stop = true;
					break;
				}
			}
		}
		
		Node node = new Node();
		node.setSet(set);
		node.setMaxFeature(maxFeature);
		node.setMaxIG(maxIG);
		node.setStop(stop);
		node.setLeftNode(null);
		node.setRightNode(null);
		node.setpointEstimate(p);
		
		return node;
	}

	
	public static void splitMethod(){
		Node m_node = PQ.get(0);
		Vector<Doc> set = m_node.getSet(); //choose the first node to split on
		int maxFeature=m_node.getMaxFeature();
		int size = set.size();
		
		Vector<Doc>group1 = new Vector<Doc>();		//has the word
		Vector<Doc>group2 = new Vector<Doc>();		//doesn't has the word
		for(int j=0; j<size;j++){
			if(set.get(j).hasWord(maxFeature)){
				group1.add(set.get(j));
			}
			else{
				group2.add(set.get(j));
			}
		}
		
		
		Node leftnode = generateNewNode(group1);
		Node rightnode = generateNewNode(group2);
		
		m_node.setLeftNode(leftnode);
		m_node.setRightNode(rightnode);
		
		/*
		 * Pop the first node from the PQ since we've already expand it
		 * Insert new nodes into PQ according to IG.
		 */
		PQ.removeElementAt(0);
		
		//if one of the is stop, we dont/cant split anymore!
		if(!(leftnode.getStop())){
			insertIntoPQ(leftnode);
		}
		if(!(rightnode.getStop())){
			insertIntoPQ(rightnode);
		}
		
	}
	
	public static void runtest(Node root){
//		int total = actualClasses.size();
//		Doc t_Doc;
//		int t_Feature;
//		countTest++;
//		for(int k = 0; k<testDocs.size(); k++){
//			t_Doc = testDocs.get(k);
//			Node treeNode = root;
//			// while: not stop criteria
//			while((treeNode.getStop() == false && (treeNode.getLeftNode()!= null || treeNode.getRightNode()!= null))){
//				//get the node's max feature
//				t_Feature = treeNode.getMaxFeature();
//				//according to whether the doc has this feature, decide which branch to go
//				if(t_Doc.hasWord(t_Feature)){
//					treeNode = treeNode.getLeftNode();
//				}
//				else {
//					treeNode = treeNode.getRightNode();
//				}
//			}
//			
//			// check why the loop stopped
//			if(treeNode.getStop() == true){
//				// if it stop because the set is empty, then we just guess one
//				if(treeNode.getSet().size() == 0){
//					t_Doc.setGroup(1);
//				}
//				else {	// not empty, then all docs are in the same group
//					t_Doc.setGroup(treeNode.getSet().get(0).getGroup());
//				} 
//			}
//			else{
//				// loop stop because the node is not expanded. Just give a favor
//				if(treeNode.getpointEstimate() > (double)0.5){
//					t_Doc.setGroup(1);
//				}
//				else{
//					t_Doc.setGroup(2);
//				}
//			}
//		}
//		
//		int c_count = 0;
//		//calculate the accuracy
//		for(int k = 0; k<testDocs.size(); k++){
//			if(testDocs.get(k).getGroup() == actualClasses.get(k).getGroup()){
//				c_count++;
//			}
//		}
//		
//		double accuracy = (double)c_count/total;
//
//		System.out.println(countTest+"	"+accuracy);
		
		int total = actualClasses.size();
		Doc t_Doc;
		int t_Feature;
		countTest++;
		for(int k = 0; k<testDocs.size(); k++){
			t_Doc = testDocs.get(k);
			Node treeNode = root;
			// while: not stop criteria
			while((treeNode.getStop() == false && (treeNode.getLeftNode()!= null || treeNode.getRightNode()!= null))){
				//get the node's max feature
				t_Feature = treeNode.getMaxFeature();
				//according to whether the doc has this feature, decide which branch to go
				if(t_Doc.hasWord(t_Feature)){
					treeNode = treeNode.getLeftNode();
				}
				else {
					treeNode = treeNode.getRightNode();
				}
			}
			
			// check why the loop stopped
			if(treeNode.getStop() == true){
				// if it stop because the set is empty, then we just guess one
				if(treeNode.getSet().size() == 0){
					t_Doc.setGroup(1);
				}
				else {	// not empty, then all docs are in the same group
					t_Doc.setGroup(treeNode.getSet().get(0).getGroup());
				} 
			}
			else{
				// loop stop because the node is not expanded. Just give a favor
				if(treeNode.getpointEstimate() > (double)0.5){
					t_Doc.setGroup(1);
				}
				else{
					t_Doc.setGroup(2);
				}
			}
		}
		
		int c_count = 0;
		//calculate the accuracy
		for(int k = 0; k<testDocs.size(); k++){
			if(testDocs.get(k).getGroup() == actualClasses.get(k).getGroup()){
				c_count++;
			}
		}
		
		double accuracy = (double)c_count/total;

		System.out.println(countTest+"	"+accuracy);
	}
	
	public static void main(String[] args) {
		Node root;
		PQ = new Vector<Node>();
		docs = new Vector<Doc>(1062);
		actualClasses = new Vector<Doc>();
		testDocs = new Vector<Doc>();
		
//		trainDocs = new Vector<Doc>();
		
		BufferedReader br = null;
		
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
		
		//start to build the tree
		root = generateNewNode(docs);
		PQ.add(root);
//		PrintStream out = null;
//		try {
//			out = new PrintStream(new FileOutputStream("src/weightTest.txt", true));
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	    System.setOut(out);
	    
		for(int n=0;n<100 && PQ.size()>0;n++){
			runtest(root);
			splitMethod();
		}		
	}
}
