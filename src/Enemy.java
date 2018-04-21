import java.util.Random;

public class Enemy{
	private float x,y;
	private int width, height,currentTexture, hitpoints;
	long timeMSNextAction = 0;
	long defaultTimeMS = 1000;
	int action =0;
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
	
	public int Update(long deltatime) {
		int random = new Random().nextInt(6) + 1;
		timeMSNextAction -= deltatime;
		if (timeMSNextAction <= 0) {
			timeMSNextAction += defaultTimeMS;
			// Aggressive
			if (random > 0 && random <= 2) {
				action = 1;
			} else if (random > 2 && random <= 4) {
				// defensive
				action = 2;
				
			} else {
				//Do Nothing
				action = 0;

			}
		}
		return action;
	}
	
}
