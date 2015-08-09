import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;


public class TilingProblem {
	
	public static void solve(boolean printSolutions){
		Header h = readAndCreateData();
		ExactCoverProblem emc = new ExactCoverProblem();
		int num = emc.search(h, new LinkedList<Cell>(), printSolutions);
		System.out.println(num);
	}

	// Using the board and tiles already read from standard input,
	// creates the corresponding data structure for the exact
	// cover problem, considering that each tile should be used
	// exactly one time, and that it can be rotated and/or reflected
	// as necessary
	private static boolean[][] getPavingProblemMatrix(int numAvailableSpaces, int[][] positionsMatrix, Tile[] tiles) {
		int matrixNumRows = positionsMatrix.length,
			matrixNumCols = positionsMatrix[0].length,
			numTiles = tiles.length;
		
		int totalNumColumns = numAvailableSpaces + numTiles;
		ArrayList<boolean[]> lines = new ArrayList<boolean[]>();
		
		// for each Tile, try to place it in every position of the board
		for(int k = 0; k < numTiles; k++){
			for (int i = 0; i < matrixNumRows; i++) {
				for (int j = 0; j < matrixNumCols; j++){
					for(boolean[][] shape : tiles[k].getShapes()){
						boolean[] matrixRow = place(shape, positionsMatrix, i, j, matrixNumRows, matrixNumCols, totalNumColumns);
						// if it is possible to place the tile, add line to matrix
						if(matrixRow != null){
							matrixRow[numAvailableSpaces + k] = true; // mark column corresponding to tile
							if(!lines.contains(matrixRow))
								lines.add(matrixRow);
						}
					}
				}
			}
		}
		
		boolean[][] exactCoverMatrix = new boolean[lines.size()][];
		int i = 0;
		for (boolean[] line : lines)
			  exactCoverMatrix[i++] = line;
		return exactCoverMatrix;
	}

	// Tries to place a tile on a specific position of the board, returning
	// the corresponding EMC line with board cells occupied if it is possible
	private static boolean[] place(boolean[][] shape, int[][] positionsMatrix, 
			int row, int column, int nRows, int nCols, int totalNumColumns) {
		boolean[] newRow = new boolean[totalNumColumns];
		for (int i = 0; i < shape.length; i++) {
			for (int j = 0; j < shape[0].length; j++){
				if(shape[i][j]){
					if(row + i >= nRows || column + j >= nCols)
						return null;
					else if(positionsMatrix[row+i][column+j] != -1)
						newRow[positionsMatrix[row+i][column+j]] = true;
					else
						return null;
				}
			}
		}

		return newRow;
	}


	// Reads from standard input the board and tiles, calling
	// the method getPavingProblemMatrix to obtain the corresponding
	// exact cover problem matrix and returning its header
	private static Header readAndCreateData(){
		Scanner s = new Scanner(System.in);
		
		int numCols = s.nextInt(),
			numRows = s.nextInt();
		s.nextLine();
		
		// read board available positions
		int numAvailableSpaces = 0;
		int[][] positionsMatrix = new int[numRows][numCols];
		for (int i = 0; i < numRows; i++) {
			String line = s.nextLine();
			for (int j = 0; j < numCols; j++){
				if(line.charAt(j)=='*'){
					positionsMatrix[i][j] = numAvailableSpaces++;
				}else{
					positionsMatrix[i][j] = -1;
				}
			}
		}
		
		// read tiles
		int numTiles = s.nextInt();
		Tile[] tiles = new Tile[numTiles];
		for(int k = 0; k < numTiles; k++){
			int numColsTile = s.nextInt(),
				numRowsTile = s.nextInt();
			s.nextLine();
			boolean[][] shape = new boolean[numRowsTile][numColsTile];
			for (int i = 0; i < numRowsTile; i++) {
				String line = s.nextLine();
				for (int j = 0; j < numColsTile; j++){
					shape[i][j] = (line.charAt(j)=='*');
				}
			}
			
			tiles[k] = new Tile(shape);
		}
		
		// create matrix to be solved
		boolean[][] matrix = getPavingProblemMatrix(numAvailableSpaces, positionsMatrix, tiles);
		
		
		// uncomment to print built matrix to be solved as an EMC problem
//		for (int i = 0; i < matrix.length; i++) {
//			for (int j = 0; j < matrix[i].length; j++)
//				System.out.print((matrix[i][j] ? "1" : "0"));
//			System.out.println();
//		}
//		System.out.println(matrix.length + " * " + matrix[0].length);
		
		s.close();
		return ExactCoverProblem.createDataStructure(matrix[0].length, matrix.length, matrix[0].length, 0, matrix);
	}
	
}
