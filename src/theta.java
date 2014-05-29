
public class theta {
	public String word;
	public  int id;
	public int numInAtheism;
	public int numInGraphics;
	
	public double getPa(){
		int sum = numInAtheism+numInGraphics+2;  //+2 becuase Laplace correction
		double pa = (double)(numInAtheism+1)/sum;	//+1 because Laplace correction
		return pa;
	}
	
	public double getPg(){
		int sum = numInAtheism+numInGraphics+2;  //+2 becuase Laplace correction
		double pg = (double)(numInGraphics+1)/sum;	//+1 because Laplace correction
		return pg;
	}
}
