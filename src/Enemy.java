
public class Enemy {
	private float x,y;
	private int width, height,currentTexture, hitpoints;
	
	public Enemy(float x, float y, int width, int height, int currentTexture, int hitpoints) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.currentTexture = currentTexture;
		this.hitpoints = hitpoints;
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
	public int getHitpoints() {
		return hitpoints;
	}
	public void setHitpoints(int hitpoints) {
		this.hitpoints = hitpoints;
	}
	
}
