
public class AABBCamera extends Camera  {
	
	private float height, width, x, y;

	public AABBCamera(float x, float y, float width, float height) {
		super(x,y);
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

}
