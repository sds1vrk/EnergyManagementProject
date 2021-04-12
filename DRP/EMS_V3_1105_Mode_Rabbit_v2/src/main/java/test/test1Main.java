package test;

public class test1Main {

	public static void main(String[] args) {
		
		int i=5;
		
		int j=3;
		
		while(true) {
			
			try {
				
				Thread.sleep(2000);
				
				System.out.println("i:"+i);
				System.out.println("j:"+j);
				
				System.out.println(i/j);
				
		
				
				
				j-=1;
				
				
			} catch (Exception e) {
				
				System.out.println("e:"+e.getMessage());
			}
			
			
		}
		
		

	}

}
