import java.util.Vector;


public class Doc {
	private int group;  // 1=atheism, 2=graphics
	private Vector<Integer> words = new Vector<Integer>();
	
	public void initWords(){
		for(int i=0; i<3566;i++){
			words.add(new Integer(0));
		}
	}
	public void addWord(int i){
		words.set(i-1, new Integer(1));
	}
	
	public boolean hasWord(int i){
		if(words.get(i) == 1){
			return true;
		}
		else{
			return false;
		}
	}
	
	public void setGroup(int i){
		group = i;
	}
	public int getGroup(){
		return group;
	}
}
