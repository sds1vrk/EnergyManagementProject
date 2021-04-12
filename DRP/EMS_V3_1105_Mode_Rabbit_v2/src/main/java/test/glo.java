package test;
import java.util.concurrent.ConcurrentHashMap;
public class glo {
	
	public static ConcurrentHashMap<String, Double> test=new ConcurrentHashMap<String, Double>();

	public static ConcurrentHashMap<String, Double> getTest() {
		return test;
	}

	public static void setTest(ConcurrentHashMap<String, Double> test) {
		glo.test = test;
	}
	
	
	

}
