package solver;

public class DancingNode{
	
	protected DancingNode left;
	protected DancingNode right;
	protected DancingNode up;
	protected DancingNode down;
	protected ColumnNode columnNode;
	
	public DancingNode() {
		this.left = this;
		this.right = this;
		this.up = this;
		this.down = this;
	}
	
	public DancingNode(ColumnNode columnNode) {
		this.left = this;
		this.right = this;
		this.up = this;
		this.down = this;
		this.columnNode = columnNode;
	}
	
	public DancingNode setDown(DancingNode node) {
		node.down = this.down;
		node.down.up = node;
		node.up = this;
		this.down = node;
		
		return node;
	}
	
	public DancingNode setRight(DancingNode node) {
		node.right = this.right;
		node.right.left = node;
		node.left = this;
		this.right = node;
		
		return node;
	}
	
	public void disconnectHorizontal() {
		this.left.right = this.right;
		this.right.left = this.left;
	}
	
	public void connectHorizontal() {
		this.left.right = this;
		this.right.left = this;
	}
	
	public void disconnectVertical() {
		this.up.down = this.down;
		this.down.up = this.up;
	}
	
	public void connectVertical() {
		this.up.down = this;
		this.down.up = this;
	}
}
