

public class Main {

	public static void main(String[] args) {
		if(args.length > 0){
			if(args[0].equals("emc") || args[0].equals("EMC")){
				ExactCoverProblem emc = new ExactCoverProblem();
				emc.solve(false);
			}else if(args[0].equals("pavage") || args[0].equals("PAVAGE")){
				TilingProblem.solve(false);
			}
		}else
			System.out.println("No problem to solve! (args.length = " + args.length + ")");

		// uncomment to test n queens
		// NQueensProblem.test();
		
		// uncomment to test with standard input
//		ExactCoverProblem emc = new ExactCoverProblem();
//		emc.solve(false); 
//		for (String[] solution : emc){
//			System.out.println("Possible solution:");
//			for(String line : solution)
//				System.out.println(line);
//		}
		
		 // TilingProblem.solve(false); // uncomment to test with standard input
	}
}
