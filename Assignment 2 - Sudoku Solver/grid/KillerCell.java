package grid;

public class KillerCell{
	
	private int row, column, value;
	private KillerCage cage;
	
	public KillerCell(int row, int column){
		this.value = -1;
		this.row = row;
		this.column = column;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public int getRow() {
		return this.row;
	}
	
	public int getColumn() {
		return this.column;
	}
	
	public KillerCage getCage() {
		return this.cage;
	}
	
	public void setCage(KillerCage cage) {
		this.cage = cage;
	}
}
