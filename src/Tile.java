public class Tile {
	boolean collision;
	int image;

	
	public Tile(boolean collision, int image) {
		this.collision = collision;
		this.image = image;
	}
	public boolean isCollision() {
		return collision;
	}
	public void setCollision(boolean collision) {
		this.collision = collision;
	}
	public int getImage() {
		return image;
	}
	public void setImage(int image) {
		this.image = image;
	}
	
}
