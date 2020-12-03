import java.util.Arrays;

public class testClass3 {
	public static void main(String[] args) {
		String blankrow="";
		blankrow+="1,";
		blankrow+="2,";
		blankrow+="3,";
		blankrow+="51,";
		int var=51;
		int flag=0;
		int[] arr = Arrays.stream(blankrow.split(",")).map(String::trim).mapToInt(Integer::parseInt).toArray();
		for(int i : arr) {
			if (i == var) {
				flag=1;
			}
		}
		System.out.println(flag);


		/*	if(blankrow.contains(Integer.toString(var))) {
		System.out.println("yes");
	}
	else {
		System.out.println("false");
	}
	int test=2+var;
	System.out.println(test);*/
		/*	for(int i=1;i<blank.length;i++) {
		System.out.println(blank[i]);
	}*/
	}
}
