
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


public class ExactCoverProblem implements Iterable<String[]>{
	private List<String[]> solutions;

	public ExactCoverProblem(){
		solutions = new LinkedList<String[]>();
	}
	
    // Creates data structure (doubly linked list of cells 
	// representing the matrix, with a header of columns)
	// from matrix, considering primary and secondary columns
	public static Header createDataStructure(int numCols, int numRows, 
			int numPriCols, int numSecCols, boolean[][] matrix){
		// create header for headers row
		Header h = new Header();
		h.left = h.right = h.up = h.down = h;
		
		// create headers row
		for (int j = 0; j < numCols; j++) {
			Header newH = new Header();
			newH.up = newH.down = newH;
			newH.right = h;
			newH.left = h.left;
			h.left.right = newH;
			h.left = newH;
			newH.name = new String(Character.toChars('A'+j)); 
		}

		
		for (int i = 0; i < numRows; i++) {
			List<Cell> curRow = new LinkedList<Cell>();

			// create vertical links for current row
			Header curCol = (Header) h.right;
			for (int j = 0; j < numCols; j++) {
				if(matrix[i][j]){
					Cell c = new Cell();
					c.up = curCol.up;
					c.down = curCol;
					c.left = c.right = c;
					c.column = curCol;
					curCol.up.down = c;
					curCol.up = c;
					curCol.size++;
					curRow.add(c);
				}
				
				curCol = (Header) curCol.right;
			}
			
			// create horizontal links for current row
			if (curRow.size() > 0){
				Iterator<Cell> it = curRow.iterator();
				Cell first = it.next();
				while (it.hasNext()) {
					Cell c = it.next();
					c.left = first.left;
					c.right = first;
					first.left.right = c;
					first.left = c;
				}
			}
		}
		
		configurePrimarySecondaryColumns(h, numPriCols, numSecCols);
		return h;
	}
		
	// Solves exact cover problem using Knuth's dancing links
	public int search(Header h, LinkedList<Cell> output, boolean printResults){
		if(h.right == h){
			saveResult(output);
			if (printResults)
				printResult(output);
			return 1;
		}

		// simple choice of column => h.right
		Header c = (Header) h.right;
		
		// S heuristics to reduce total number of recursive calls
		int s = Integer.MAX_VALUE;
		Header hs = (Header) h.right;
		while (hs != h){
			if (hs.size < s) {
				s = hs.size;
				c = hs;
			}
			hs = (Header) hs.right;
		}

		int numSolutions = 0;

		cover(c);
		Cell r = c.down, j;
		while (r != c) {
			output.add(r);

			for (j = r.right; j != r; j = j.right)
				cover((Header) j.column);

			numSolutions += search(h, output, printResults);

			for (j = r.left; j != r; j = j.left)
				uncover((Header) j.column);

			output.remove(r);
			r = r.down;
		}
		
		uncover(c);
		return numSolutions;
	}
	
	public void solve(boolean printSolutions){
		Header h = readData();
		int num = search(h, new LinkedList<Cell>(), printSolutions);
		System.out.println(num);
	}

	private static void configurePrimarySecondaryColumns(Header h, int numPriCols, int numSecCols){		
		int numCols = numPriCols + numSecCols;
		Cell cols[] = new Cell[numCols];
		Header current = (Header) h.right;
		for (int i = 0; i < numCols; i++) {
			cols[i] = current;
			current = (Header) current.right;
		}

		if (numPriCols > 0) {
			cols[numPriCols - 1].right = h;
			h.left = cols[numPriCols - 1];
		}
		
		for (int i = numPriCols; i < numCols; i++) {
			cols[i].left = cols[i];
			cols[i].right = cols[i];
		}
	}
	
	// Removes column from header and all cells of rows which 
	// have a cell in this column, except for the cells in the
	// column itself
	private void cover(Header column) {
		column.right.left = column.left;
		column.left.right = column.right;
		Cell i = column.down;
		while (i != column) {
			Cell j = i.right;
			while (j != i) {
				j.down.up = j.up;
				j.up.down = j.down;
				((Header) j.column).size--;
				j = j.right;
			}i = i.down;
		}
	}
	
	// Prints result in contained in output
    private void printResult(List<Cell> output){
    	System.out.println("\nPossible solution: ");
		for(Cell c : output){
			Cell c2 = c;
			do{
				System.out.print(c2.column.name + " ");
				c2 = c2.right;
			}while(c2 != c);
			System.out.println();
		}
    }

    // Reads from standard input and creates data structure
	// (doubly linked list of cells representing the matrix,
	// with a header of columns), considering primary and 
	// secondary columns
	private Header readData(){
		Scanner s = new Scanner(System.in);
		
		int numPriCols = s.nextInt(),
			numSecCols = s.nextInt(),
			numRows = s.nextInt(),
			numCols = numPriCols + numSecCols;

		s.nextLine();
		
		boolean[][] matrix = new boolean[numRows][numCols];
		for (int i = 0; i < numRows; i++) {
			String line = s.nextLine();
			for (int j = 0; j < numCols; j++)
				matrix[i][j] = (line.charAt(j)=='1');
		}

		s.close();
		return createDataStructure(numCols, numRows, numPriCols, numSecCols, matrix);
	}

	// Saves result in contained in output
    private void saveResult(List<Cell> output){
    	String s[] = new String[output.size()];
    	int counter = 0;
		for(Cell c : output){
			s[counter] = "";
			Cell c2 = c;
			do{
				s[counter] += c2.column.name + " ";
				c2 = c2.right;
			}while(c2 != c);
			counter++;
		}
		
		solutions.add(s);
    }

    // Undoes cover(column)
	private void uncover(Header column) {
		Cell i = column.up;
		while (i != column) {
			Cell j = i.left;
			while (j != i) {
				((Header) j.column).size++;
				j.down.up = j;
				j.up.down = j;
				j = j.left;
			}i = i.up;
		}
		
		column.right.left = column;
		column.left.right = column;
	}

	@Override
	public Iterator<String[]> iterator() {
		return solutions.iterator();
	}
}
