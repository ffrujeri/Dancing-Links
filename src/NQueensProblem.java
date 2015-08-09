import java.util.LinkedList;


public class NQueensProblem {
	public static void solve(int n, boolean printSolutions){
		Header h = createData(n);
		ExactCoverProblem emc = new ExactCoverProblem();
		int num = emc.search(h, new LinkedList<Cell>(), printSolutions);
		System.out.println(num);
	}

	public static void test(){
		for (int i = 1; i < 5; i++){
			System.out.println("N queens problem with N = " + i);
			NQueensProblem.solve(i, true);
			System.out.println("\n------------");
		}for (int i = 5; i <= 12; i++){
			System.out.print("N queens problem with N = " + i + ", #solutions = ");
			NQueensProblem.solve(i, false);
		}
	}
	
	// Creates matrix to be solved by the exact cover problem,
	// considering rows and columns as primary and diagonals and
	// reverse diagonals as secondary
	private static Header createData(int n){
		// n lines and n columns are primary
		// 2*(2*n - 1) diagonals are secondary
		// possible optimization: consider 2*(2*n - 1) - 4,
		// ignoring diagonals on the edges (no possible intersection)
		int numRows = n*n,
			numPriCols = 2*n,
			numSecCols = 4*n - 2,
			numCols = numPriCols + numSecCols;
		boolean[][] matrix = new boolean[numRows][numCols];

		for(int i = 0; i < n*n; i++){
			int line = i/n,
				column = i%n,
				mainDiagonal = (n-1) - line + column,
				reverseDiagonal = line + column;
			matrix[i][line] = matrix[i][column+n] = true;
			matrix[i][mainDiagonal+2*n] = matrix[i][reverseDiagonal+4*n-1] = true;
		}
		
		
		// uncomment to print built matrix to be solved as an EMC problem
//		for (int i = 0; i < matrix.length; i++) {
//			for (int j = 0; j < matrix[i].length; j++)
//				System.out.print((matrix[i][j] ? "1" : "0"));
//			System.out.println();
//		}
//		System.out.println(matrix.length + " * " + matrix[0].length);
		
		return ExactCoverProblem.createDataStructure(numCols, numRows, numPriCols, numSecCols, matrix);
	}
	
}
