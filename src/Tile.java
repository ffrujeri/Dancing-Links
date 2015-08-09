import java.util.ArrayList;

// Class used to represent a piece (tile). Contains an ArrayList with
// all possible DISTINCT rotations and/or reflections of the piece.
public class Tile {
	private ArrayList<boolean[][]> shapes;
	
	public Tile(boolean[][] shape){
		shapes = new ArrayList<boolean[][]>();
		shapes.add(shape);
		
		// consider all possible rotations && reflections
		for(int i = 1; i < 4; i++){
			shape = rotate90(shape);
			if(!contains(shape))
				shapes.add(shape);
		}

		shape = reflectHorizontally(shape);
		for(int i = 0; i < 4; i++){
			shape = rotate90(shape);
			if(!contains(shape))
				shapes.add(shape);
		}
	}
	
	public ArrayList<boolean[][]> getShapes(){
		return shapes;
	}
	
	private boolean contains(boolean[][] shape){
		for(boolean[][] s : shapes){
			boolean isEqual = true;
			if((s.length != shape.length) || (s[0].length != shape[0].length))
				isEqual = false;
			for(int i = 0; i < s.length && isEqual; i++)
				for(int j = 0; j < s[0].length && isEqual; j++)
					isEqual = (shape[i][j] == s[i][j]);
			if(isEqual)
				return true;
		}
		
		return false;
	}
	
	private boolean[][] reflectHorizontally(boolean[][] shape){
		int numRows = shape.length,
			numCols = shape[0].length;
		boolean[][] reflectedShape = new boolean[numRows][numCols];
		for (int i = 0; i < numRows; i++)
			for (int j = 0; j < numCols; j++)
				reflectedShape[i][j] = shape[i][numCols-1-j];
		return reflectedShape;
	}
	
	private boolean[][] rotate90(boolean[][] shape){
		int numRows = shape.length,
			numCols = shape[0].length;
		boolean[][] rotatedShape = new boolean[numCols][numRows];
		for (int i = 0; i < numRows; i++)
			for (int j = 0; j < numCols; j++)
				rotatedShape[j][numRows-1-i] = shape[i][j];
		return rotatedShape;
	}

}
