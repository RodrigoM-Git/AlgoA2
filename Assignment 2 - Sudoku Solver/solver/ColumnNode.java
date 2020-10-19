package solver;

public class ColumnNode extends DancingNode{
	private int size;
	private String name;
	
	
	public ColumnNode(String name) {
		super();
		this.size = 0;
		this.name = name;
		this.columnNode = this;
	}
	
	public void cover() {
		disconnectHorizontal();
		
		for(DancingNode colNode = this.down; colNode != this; colNode = colNode.down) {
			for(DancingNode rowNode = colNode.right; rowNode != colNode; rowNode = rowNode.right) {
				rowNode.disconnectVertical();
				rowNode.columnNode.size--;
			}
		}
	}
	
	public void uncover() {
		for(DancingNode colNode = this.up; colNode != this; colNode = colNode.up) {
			for(DancingNode rowNode = colNode.left; rowNode != colNode; rowNode = rowNode.left) {
				rowNode.columnNode.size++;
				rowNode.connectVertical();
			}
		}
		connectHorizontal();
	}
	
	public int getSize() {
		return this.size;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	public String getName() {
		return this.name;
	}
	
	
}

