package grid;

import java.util.ArrayList;
import java.util.List;

public class KillerCage{
	
	private List<KillerCell> cells;
	private int targetValue;
	
	public KillerCage(int targetValue) {
		this.targetValue = targetValue;
		cells = new ArrayList<KillerCell>();
	}
	
	public void addCell(KillerCell cell) {
		cells.add(cell);
	}
	
	public int getTarget() {
		return this.targetValue;
	}
	
	public List<KillerCell> getCells() {
		return this.cells;
	}
}
