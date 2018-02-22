

class FrameDef {
	 public int image;
	 public float frameTimeSecs;
	 
	public FrameDef(int image, float frameTimeSecs) {
		super();
		this.image = image;
		this.frameTimeSecs = frameTimeSecs;
	}
	public int getImage() {
		return image;
	}
	public void setImage(int image) {
		this.image = image;
	}
	public float getFrameTimeSecs() {
		return frameTimeSecs;
	}
	public void setFrameTimeSecs(float frameTimeSecs) {
		this.frameTimeSecs = frameTimeSecs;
	}
	 
}
