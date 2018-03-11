
public class Character {
	private int width, height,currentTexture;
	private float x,y;
	
	public Character(float x, float y, int width, int height, int tex) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.currentTexture =tex;
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
