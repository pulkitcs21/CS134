

public class AnimationDef {
	FrameDef[] animations;
	int currentFrame;
	private float timeRemaining;
	boolean finished;
	
	public AnimationDef(FrameDef[] f) {
		animations = f;
		currentFrame = 0;
		timeRemaining = animations[0].frameTimeSecs;
		finished = false;
	}
	

	public void updateSprite(float deltaTime) {
		timeRemaining -= deltaTime;
		if (timeRemaining <= 0) {
			currentFrame++;
			if (currentFrame > animations.length - 1) {
				finished = true;
				currentFrame = 0;
			}
			timeRemaining += animations[currentFrame].frameTimeSecs;
		}
	}
	
	public int getCurrentFrame() {
		return animations[currentFrame].getImage();
	}

}
