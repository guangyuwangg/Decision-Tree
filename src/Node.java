import java.util.Vector;

public class Node {
	private Node leftnode;	//has a feature
	private Node rightnode;	//doesn't has a feature
	private int maxFeature;
	private double maxIG;
	private Vector <Doc>set;
	private boolean stop = false; 	//indicate whether the stop criteria is met
	private double pointEstimate;

	public double getpointEstimate(){
		return pointEstimate;
	}
	public void setpointEstimate(double p){
		pointEstimate = p;
	}
	
	public boolean getStop(){
		return stop;
	}
	public void setStop(boolean b){
		stop = b;
	}
	
	public int getMaxFeature(){
		return maxFeature;
	}
	public void setMaxFeature(int i){
		maxFeature = i;
	}
	
	public double getMaxIG(){
		return maxIG;
	}
	public void setMaxIG(double i){
		maxIG = i;
	}
	
	public Node getLeftNode(){
		return leftnode;
	}
	public void setLeftNode(Node n){
		leftnode = n;
	}
	
	public Node getRightNode(){
		return rightnode;
	}
	public void setRightNode(Node n){
		rightnode = n;
	}
	
	public Vector<Doc> getSet(){
		return set;
	}
	public void setSet(Vector<Doc> s){
		set = s;
	}
	
	
}
