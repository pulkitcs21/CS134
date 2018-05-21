
public class Coin {
	private float x,y;
	private int width, height,currentTexture;
	public Coin(float x, float y, int width, int height, int currentTexture) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.currentTexture = currentTexture;
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
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getCurrentTexture() {
		return currentTexture;
	}
	public void setCurrentTexture(int currentTexture) {
		this.currentTexture = currentTexture;
	}
	
	
}
