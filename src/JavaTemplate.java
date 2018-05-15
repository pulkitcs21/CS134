
import com.jogamp.nativewindow.WindowClosingProtocol;

import com.jogamp.opengl.*;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.opengl.GLWindow;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Random;
import javax.sound.sampled.Clip;

public class JavaTemplate {
	// Set this to true to make the game loop exit.
	private static boolean shouldExit;

	// The previous frame's keyboard state.
	private static boolean kbPrevState[] = new boolean[256];

	// The current frame's keyboard state.
	private static boolean kbState[] = new boolean[256];

	// Position of the sprite.
	static float spritePosX = 0;
	static float spritePosY = 510;
	private static float[] spritePos = new float[] { spritePosX, spritePosY };

	static float gombooX = 400;
	static float gombooY = 510;
	
	static float koopaX = 480;
	static float koopaY = 510;

	private static float[] bulletPos = new float[] { spritePosX, spritePosY };

	private static int currFrame;
	private static int gombooFrame;
	private static int koopaFrame;
	
	
	// Texture for the sprite.
	private static int cloud;
	private static int power;
	private static int tree;
	private static int wall;
	private static int wall1;
	private static int wall2;
	private static int stair;
	private static int stair2;
	private static int wall3;
	private static int tree1;
	private static int tile;
	private static int bush;
	private static int bush1;
	private static int cloud2;
	private static int platform;
	private static int sky;
	private static int gombootex;
	private static int koopatex;
	private static int bullettex;
	private static int windowWidth = 800;
	private static int windowHeight = 600;
	
	//JUMP
	
	private static boolean isJumping = false;
	private static float gravity = 3.0f;
	private static float yVelocity = 0;
	private static boolean grounded = true;
	private static int jumptime = 0;
	private static int time;
	private static float originalY;
	
	// Size of the sprite.
	private static int[] spriteSize = new int[2];
	private static int[] enemySize = new int[2];
	private static int[] bulletSize = new int[2];
	// Size of the tile
	private static int[] tileSize = new int[2];

	// Making Background declarations
	private static BackgroundDef backgroundDef;

	public static void main(String[] args) {

		GLProfile gl2Profile;
		try {
			// Make sure we have a recent version of OpenGL
			gl2Profile = GLProfile.get(GLProfile.GL2);
		} catch (GLException ex) {
			System.out.println("OpenGL max supported version is too low.");
			System.exit(1);
			return;
		}

		// Create the window and OpenGL context.
		GLWindow window = GLWindow.create(new GLCapabilities(gl2Profile));
		// OSX doubles the windows size
		window.setSize(windowWidth / 2, windowHeight / 2);
		window.setTitle("Super Mairo");
		window.setVisible(true);
		window.setDefaultCloseOperation(WindowClosingProtocol.WindowClosingMode.DISPOSE_ON_CLOSE);

		window.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent keyEvent) {
				if (keyEvent.isAutoRepeat()) {
					return;
				}
				kbState[keyEvent.getKeyCode()] = true;
			}

			@Override
			public void keyReleased(KeyEvent keyEvent) {
				if (keyEvent.isAutoRepeat()) {
					return;
				}
				kbState[keyEvent.getKeyCode()] = false;
			}
		});

		// Setup OpenGL state.
		window.getContext().makeCurrent();
		GL2 gl = window.getGL().getGL2();
		gl.glViewport(0, 0, windowWidth, windowHeight);
		gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
		gl.glOrtho(0, windowWidth, windowHeight, 0, 0, 100);
		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

		// Game initialization goes here.

		// Initializing backgrounds
		sky = glTexImageTGAFile(gl, "backgroundImages/sky.tga", tileSize);
		power = glTexImageTGAFile(gl, "backgroundImages/power.tga", tileSize);
		wall = glTexImageTGAFile(gl, "backgroundImages/wall.tga", tileSize);
		wall1 = glTexImageTGAFile(gl, "backgroundImages/wall1.tga", tileSize);
		wall2 = glTexImageTGAFile(gl, "backgroundImages/wall2.tga", tileSize);
		wall3 = glTexImageTGAFile(gl, "backgroundImages/wall3.tga", tileSize);
		bush = glTexImageTGAFile(gl, "backgroundImages/bush.tga", tileSize);
		bush1 = glTexImageTGAFile(gl, "backgroundImages/bush1.tga", tileSize);
		tree = glTexImageTGAFile(gl, "backgroundImages/tree.tga", tileSize);
		tree1 = glTexImageTGAFile(gl, "backgroundImages/tree1.tga", tileSize);
		tile = glTexImageTGAFile(gl, "backgroundImages/tile.tga", tileSize);
		platform = glTexImageTGAFile(gl, "backgroundImages/platform.tga", tileSize);
		cloud = glTexImageTGAFile(gl, "backgroundImages/cloud.tga", tileSize);
		cloud2 = glTexImageTGAFile(gl, "backgroundImages/cloud2.tga", tileSize);
		stair = glTexImageTGAFile(gl, "backgroundImages/stair.tga", tileSize);
		stair2 = glTexImageTGAFile(gl, "backgroundImages/stair2.tga", tileSize);

		Tile skyt = new Tile(false, sky);
		Tile powert = new Tile(true, power);
		Tile wallt = new Tile(true, wall);
		Tile wall1t = new Tile(true, wall1);
		Tile wall2t = new Tile(true, wall2);
		Tile wall3t = new Tile(true, wall3);
		Tile busht = new Tile(false, bush);
		Tile bush1t = new Tile(false, bush);
		Tile treet = new Tile(false, tree);
		Tile tree1t = new Tile(false, tree1);
		Tile tilet = new Tile(true, tile);
		Tile platformt = new Tile(true, platform);
		Tile cloudt = new Tile(false, cloud);
		Tile cloud2t = new Tile(false, cloud2);
		Tile stairt = new Tile(true, stair);
		Tile stair2t = new Tile(true, stair2);
		Tile[] ta = { platformt, skyt, cloudt, cloud2t, powert, tilet, treet, tree1t, busht, bush1t, wallt, wall1t,
				wall2t, wall3t, stairt, stair2t };

		// Enemy Texture
		gombootex = glTexImageTGAFile(gl, "enemy/enemy1.tga", enemySize);

		koopatex = glTexImageTGAFile(gl, "enemy/koopaidle.tga", enemySize);
		
		// Bullet Texture
		bullettex = glTexImageTGAFile(gl, "bullet/fire.tga", bulletSize);

		backgroundDef = new BackgroundDef();

		int[] tilearray = new int[backgroundDef.getTileSize()];

		// Function to draw Background
		drawBackground(tilearray);

		// Mario Animations
		FrameDef[] idle = { new FrameDef(glTexImageTGAFile(gl, "Animations/idle.tga", spriteSize), 600),

		};
		
		
		FrameDef[] jump= { 
				new FrameDef(glTexImageTGAFile(gl, "Animations/idle.tga", spriteSize), 100),
				new FrameDef(glTexImageTGAFile(gl, "Animations/idle.tga", spriteSize), 100),
				new FrameDef(glTexImageTGAFile(gl, "Animations/idle.tga", spriteSize), 100),
				new FrameDef(glTexImageTGAFile(gl, "Animations/idle.tga", spriteSize), 100),

		};
		FrameDef[] moveleft = { new FrameDef(glTexImageTGAFile(gl, "Animations/left.tga", spriteSize), 100),
				new FrameDef(glTexImageTGAFile(gl, "Animations/left2.tga", spriteSize), 100),
				new FrameDef(glTexImageTGAFile(gl, "Animations/left3.tga", spriteSize), 100) };
		FrameDef[] moveright = { new FrameDef(glTexImageTGAFile(gl, "Animations/right.tga", spriteSize), 100),
				new FrameDef(glTexImageTGAFile(gl, "Animations/right2.tga", spriteSize), 100),
				new FrameDef(glTexImageTGAFile(gl, "Animations/right3.tga", spriteSize), 100),
				new FrameDef(glTexImageTGAFile(gl, "Animations/right4.tga", spriteSize), 100) };

		// Gomboo Animations
		FrameDef[] gombooframeidle = {
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/idleenemy1.tga", enemySize), 600),
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/idleenemy2.tga", enemySize), 600),
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/idleenemy3.tga", enemySize), 600),

		};
		FrameDef[] gombooframeleft = {
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/leftenemy1.tga", enemySize), 100),
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/leftenemy2.tga", enemySize), 100),
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/leftenemy3.tga", enemySize), 100),
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/leftenemy4.tga", enemySize), 100) };
		FrameDef[] gombooframeright = {
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/rightenemy1.tga", enemySize), 100),
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/rightenemy2.tga", enemySize), 100),
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/rightenemy3.tga", enemySize), 100),
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/rightenemy4.tga", enemySize), 100),
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/rightenemy5.tga", enemySize), 100) };
		
		
		// Koopa Animations
				FrameDef[] koopaframeidle = {
						new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/koopaidle.tga", enemySize), 600),
						new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/koopaidle2.tga", enemySize), 600)
				};
				FrameDef[] koopaframeleft = {
						new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/koopaleft.tga", enemySize), 100),
						new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/koopaleft2.tga", enemySize), 100),
						new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/koopaleft3.tga", enemySize), 100),
						new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/koopaleft4.tga", enemySize), 100) };
				FrameDef[] koopaframeright = {
						new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/koopaleft.tga", enemySize), 100),
						new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/koopaleft.tga", enemySize), 100),
						new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/koopaleft.tga", enemySize), 100),
						new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/koopaleft.tga", enemySize), 100),
						new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/koopaleft.tga", enemySize), 100) };

		AnimationDef idleanimation = new AnimationDef(idle);
		AnimationDef leftanimation = new AnimationDef(moveleft);
		AnimationDef rightanimation = new AnimationDef(moveright);
		AnimationDef jumpanimation = new AnimationDef(jump);

		AnimationDef gombooidle = new AnimationDef(gombooframeidle);
		AnimationDef gombooleft = new AnimationDef(gombooframeleft);
		AnimationDef gombooright = new AnimationDef(gombooframeright);
		
		AnimationDef koopaidle = new AnimationDef(koopaframeidle);
		AnimationDef koopaleft = new AnimationDef(koopaframeleft);
		AnimationDef kooparight = new AnimationDef(gombooframeright);

		// Camera Initialization
		Camera camera = new Camera(0, 0);

		Enemy gomboo_ar = new Enemy(gombooX, gombooY, enemySize[0], enemySize[1], gombootex, 100);

		Enemy koopa_ar = new Enemy(koopaX, koopaY, enemySize[0], enemySize[1], gombootex, 100);

		ArrayList<Enemy> arrayGomboo = new ArrayList<Enemy>();
		arrayGomboo.add(gomboo_ar);
		
		ArrayList<Enemy> arraykoopa = new ArrayList<Enemy>();
		arraykoopa.add(koopa_ar);

		// Bullets ArrayList
		ArrayList<Bullet> bullets = new ArrayList<Bullet>();

		
		   Font font = new Font(30, "1"); 
		   font.addCharacter('/',glTexImageTGAFile(gl, "fonts/slash.tga", new int[] {50, 50 }), 20);
		   font.addCharacter('1',glTexImageTGAFile(gl, "fonts/1.tga", new int[] {50, 50 }), 20);
		   font.addCharacter('D', glTexImageTGAFile(gl, "fonts/D.tga", new int[]{50,50}), 30);
		   font.addCharacter('L', glTexImageTGAFile(gl, "fonts/L.tga", new int[]{50,50}), 30);
		   font.addCharacter('O', glTexImageTGAFile(gl, "fonts/O.tga", new int[]{50,50}), 30);
		   font.addCharacter('R', glTexImageTGAFile(gl, "fonts/R.tga", new int[]{50,50}), 30);
		   font.addCharacter('W', glTexImageTGAFile(gl, "fonts/W.tga", new int[]{50,50}), 30);
		     
		// The game loop
		int physicsDeltaTime = 10;
		int physicsFrameMS = (int) (System.nanoTime() / 1000000);
		long lastFrameNS;

		float playerspeed = 3.0f / 16;
		float enemyspeed = 2.0f / 16;

		long curFrameNS = System.nanoTime();

		Sound soundMain = Sound.loadFromFile("sounds/main.wav");
        Clip bgClip = soundMain.playLooping();
        
        
		while (!shouldExit) {
			System.arraycopy(kbState, 0, kbPrevState, 0, kbState.length);
			lastFrameNS = curFrameNS;
			curFrameNS = System.nanoTime();
			long currFrameMS = curFrameNS / 1000000;
			long deltaTimeMS = (curFrameNS - lastFrameNS) / 1000000;
			long defaultTimeMS = 1000;
			long timeMSNextAction = 1000;
			
			// Actually, this runs the entire OS message pump.
			window.display();

			// physics loop
			do {

				for (int i = 0; i < bullets.size(); i++) {
					Bullet b = bullets.get(i);
					b.setX(b.getX() + 5);
					for (int j = 0; j < arrayGomboo.size(); j++) {
						Enemy e = arrayGomboo.get(j);
						AABBCamera enemyAABB = new AABBCamera(e.getX(), e.getY(), e.getWidth(), e.getHeight());
						AABBCamera bulletAABB = new AABBCamera(b.getX(), b.getY(), b.getWidth(), b.getHeight());
						if (AABBIntersect(enemyAABB, bulletAABB)) {
							bullets.remove(i);
							e.setHitpoints(e.getHitpoints() - 50);
							if (e.getHitpoints() <= 0) {
								arrayGomboo.remove(j);
							}
						}
					}
				}

				physicsFrameMS += physicsDeltaTime;

			} while (physicsFrameMS + physicsDeltaTime < currFrameMS);

			if (!window.isVisible()) {
				shouldExit = true;
				break;
			}

			// Game logic goes here.
			if (kbState[KeyEvent.VK_ESCAPE]) {
				shouldExit = true;
			}
			// Bullet Press
			if (kbState[KeyEvent.VK_SPACE] && !kbPrevState[KeyEvent.VK_SPACE]) {
				bullets.add(new Bullet(spritePos[0], spritePos[1], bulletSize[0], bulletSize[1], bullettex));
			}

			// ENEMY AI
			for (Enemy e : arrayGomboo) {
				int action = e.Update(deltaTimeMS);
				float x = e.getX();
				float y = e.getY();
				if (action == 1) {
					if (e.getX() < spritePos[0] && e.getX() > 0 && (e.getX() < (1600 - enemySize[0]))) {
						// Move towards player
						x += deltaTimeMS * enemyspeed;
						if (x < 0)
							x = 0;
						e.setX(x);
					} else {
						if (e.getX() > 0 && (e.getX() < (1600 - enemySize[0]))) {
							x -= deltaTimeMS * enemyspeed;
							if (x < 0)
								x = 0;
							e.setX(x);
						}
					}
					gombooright.updateSprite(deltaTimeMS);
					gombooFrame = gombooright.getCurrentFrame();	
//					kooparight.updateSprite(deltaTimeMS);
//					koopafra = kooparight.getCurrentFrame();
					
				} else if (action == 2) {
					if (e.getX() < spritePos[0] && e.getX() > 0 && (e.getX() < (1600 - enemySize[0]))) {
						// Move away from player
						x -= deltaTimeMS * playerspeed;
						if (x < 0)
							x = 0;
						e.setX(x);
					} else {
						if (e.getX() > 0 && (e.getX() < (1600 - enemySize[0]))) {
							x += deltaTimeMS * playerspeed;
							if (x < 0)
								x = 0;
							e.setX(x);
						}
					}
					gombooleft.updateSprite(deltaTimeMS);
					gombooFrame = gombooleft.getCurrentFrame();
//					koopaleft.updateSprite(deltaTimeMS);
//					koopaFrame = koopaleft.getCurrentFrame();
				} else {
					if (e.getX() < spritePos[0] && (e.getX() < (1600 - enemySize[0])) && e.getX() > 0) {
						e.setX(x);
					}
					gombooidle.updateSprite(deltaTimeMS);
					gombooFrame = gombooidle.getCurrentFrame();
//					koopaidle.updateSprite(deltaTimeMS);
//					koopaFrame = koopaidle.getCurrentFrame();
				}
			}

			if(grounded) {
				yVelocity = 0;
			}
			
			if(grounded && kbState[KeyEvent.VK_P] ) {
				yVelocity = -20;	
				grounded = false;
			}
			
			spritePos[1] += (deltaTimeMS/16) * yVelocity;
			yVelocity += (deltaTimeMS/16) *gravity;
			
			int upperSpriteIndexX = (int) (spritePos[0] / tileSize[0]);
			int upperSpriteIndexY = (int) (spritePos[1] / tileSize[1]);
			int lowerSpriteIndexX = (int) ((spritePos[0] + spriteSize[0] - 1) / tileSize[0]);
			int lowerSpriteIndexY = (int) ((spritePos[1] + spriteSize[1] - 1) / tileSize[1]);
			boxCollisionRight(upperSpriteIndexX, upperSpriteIndexY, lowerSpriteIndexX, lowerSpriteIndexY, ta);
			boxCollisionDown(upperSpriteIndexX, upperSpriteIndexY, lowerSpriteIndexX, lowerSpriteIndexY, ta);
			boxCollisionLeft(upperSpriteIndexX, upperSpriteIndexY, lowerSpriteIndexX, lowerSpriteIndexY, ta);
			// ANIMATIONS FOR MARIO


//			if (isJumping) {
//				jump(deltaTimeMS);
//			}
			
			
			
			if (kbState[KeyEvent.VK_UP]) {
				moveUp(deltaTimeMS, rightanimation, playerspeed, ta);
			}else if (kbState[KeyEvent.VK_DOWN] && spritePos[1] <= ((backgroundDef.getWidth() * 30) - spriteSize[1])) {
				moveDown(deltaTimeMS, leftanimation, playerspeed, ta);
			} else if (kbState[KeyEvent.VK_RIGHT]
					&& spritePos[0] <= ((backgroundDef.getHeight() * backgroundDef.getWidth()) - spriteSize[0])) {
				moveRight(deltaTimeMS, rightanimation, playerspeed, ta);
			} else if (kbState[KeyEvent.VK_LEFT]) {
				moveLeft(deltaTimeMS, leftanimation, playerspeed, ta);
			} else {
				idleanimation.updateSprite(deltaTimeMS);
				currFrame = idleanimation.getCurrentFrame();
			}
		
	
			// Camera Controls
			cameraDown(camera);
			cameraLeft(camera);
			cameraRight(camera);
			cameraUp(camera);
			
			
			intelligentCamera(camera, deltaTimeMS);
			gl.glClearColor(0, 0, 0, 1);
			gl.glClear(GL.GL_COLOR_BUFFER_BIT);

			// Draw background what is shown
			int startIndex = (int) (camera.getX() / tileSize[0]);
			int endIndex = (int) (camera.getX() + 799) / tileSize[0];
			int startY = (int) (camera.getY() / tileSize[1]);
			int endY = (int) (camera.getY() + 599) / tileSize[1];

			for (int i = startIndex; i <= endIndex; i++) {
				for (int j = startY; j <= endY; j++) {
					int index = (j * backgroundDef.getWidth() + i);
					if (index >= tilearray.length)
						continue;
					glDrawSprite(gl, tilearray[j * backgroundDef.getWidth() + i], i * tileSize[0] - camera.getX(),
							j * tileSize[1] - camera.getY(), tileSize[0], tileSize[1]);
				}
			}

			// Sprite Collision
			AABBCamera spriteAABB = new AABBCamera(spritePos[0], spritePos[1], spriteSize[0], spriteSize[1]);
			AABBCamera cameraAABB = new AABBCamera(camera.getX(), camera.getY(), windowWidth, windowHeight);
			
			drawMario(cameraAABB, spriteAABB, gl, cameraAABB);
			
			drawGomboo(gl, arrayGomboo, camera, cameraAABB);
			
			DrawBullet(gl, camera, bullets, cameraAABB);
			
			
			float[] position = {50, 0};
			float[] position2 = {75, font.lineHeight + 5};
			DrawText(font, position,"WORLD", gl);
			DrawText(font, position2,"1/1", gl);
		}
	}

	public static void intelligentCamera(Camera camera, long deltaTimeMS) {
//		if(spritePos[0] - camera.getX() < 100) {
//			camera.setX(camera.getX() - (3.0f * deltaTimeMS));
//		}
//		if(spritePos[1] - camera.getY() < 100) {
//
//			camera.setY(camera.getY() - (3.0f * deltaTimeMS));
//		}
//		if(camera.getX() - spritePos[0]> windowWidth - 400 - spriteSize[0]) {
//
//			camera.setX(camera.getX() + (3.0f * deltaTimeMS));
//		}
//		if(spritePos[1] - camera.getY() > windowWidth - 100 - spriteSize[1]) {
//
//			camera.setY(camera.getY() + (3.0f * deltaTimeMS));
//		}
	
	}
	public static void drawMario(AABBCamera cameraAABB, AABBCamera spriteAABB, GL2 gl, Camera camera) {
		if (AABBIntersect(cameraAABB, spriteAABB)) {
			glDrawSprite(gl, currFrame, spritePos[0] - camera.getX(), spritePos[1] - camera.getY(), spriteSize[0],
					spriteSize[1]);
		}
	}
	public static void drawGomboo(GL2 gl ,ArrayList<Enemy> arrayGomboo,Camera camera,AABBCamera cameraAABB) {
		for (Enemy e : arrayGomboo) {
			AABBCamera enemyAABB = new AABBCamera(e.getX(), e.getY(), e.getWidth(), e.getHeight());
			if (AABBIntersect(cameraAABB, enemyAABB)) {
				glDrawSprite(gl, gombooFrame, e.getX() - camera.getX(), e.getY() - camera.getY(), enemySize[0],
						enemySize[1]);

			}
		}
	}
	public static void DrawBullet(GL2 gl, Camera camera, ArrayList<Bullet> bullets, AABBCamera cameraAABB) {
		for (Bullet b : bullets) {
			AABBCamera bulletAABB = new AABBCamera(b.getX(), b.getY(), b.getWidth(), b.getHeight());
			if (AABBIntersect(cameraAABB, bulletAABB)) {
				glDrawSprite(gl, bullettex, b.getX() - camera.getX(), b.getY() - camera.getY(), bulletSize[0],
						bulletSize[1]);
			}
		}
	}
	public static void DrawText(Font font, float[] charPos, String text, GL2 gl) {
		for (int i = 0; i < text.length(); i++) {
			char x = text.charAt(i);
			glDrawSprite(gl, font.glyphs.get(x).image, charPos[0], charPos[1], font.getlineHeight(), font.glyphs.get(x).width);
			charPos[0] += 30;
		}

	}

	public static void moveUp(long deltaTimeMS, AnimationDef rightanimation, float playerspeed, Tile[] ta) {
		spritePos[1] -= deltaTimeMS * playerspeed;
		// update the position when sprite moves
		rightanimation.updateSprite(deltaTimeMS);
		currFrame = rightanimation.getCurrentFrame();
		int upperSpriteIndexX = (int) (spritePos[0] / tileSize[0]);
		int upperSpriteIndexY = (int) (spritePos[1] / tileSize[1]);
		int lowerSpriteIndexX = (int) ((spritePos[0] + spriteSize[0] - 1) / tileSize[0]);
		int lowerSpriteIndexY = (int) ((spritePos[1] + spriteSize[1] - 1) / tileSize[1]);
		boxCollisionUP(upperSpriteIndexX, upperSpriteIndexY, lowerSpriteIndexX, lowerSpriteIndexY, ta);
	}

	public static void moveDown(long deltaTimeMS, AnimationDef leftanimation, float playerspeed, Tile[] ta) {
		spritePos[1] += deltaTimeMS * playerspeed;
		leftanimation.updateSprite(deltaTimeMS);
		currFrame = leftanimation.getCurrentFrame();
		int upperSpriteIndexX = (int) (spritePos[0] / tileSize[0]);
		int upperSpriteIndexY = (int) (spritePos[1] / tileSize[1]);
		int lowerSpriteIndexX = (int) ((spritePos[0] + spriteSize[0] - 1) / tileSize[0]);
		int lowerSpriteIndexY = (int) ((spritePos[1] + spriteSize[1] - 1) / tileSize[1]);
		boxCollisionDown(upperSpriteIndexX, upperSpriteIndexY, lowerSpriteIndexX, lowerSpriteIndexY, ta);
	}

	public static void moveRight(long deltaTimeMS, AnimationDef rightanimation, float playerspeed, Tile[] ta) {
		spritePos[0] += deltaTimeMS * playerspeed;
		rightanimation.updateSprite(deltaTimeMS);
		currFrame = rightanimation.getCurrentFrame();
		int upperSpriteIndexX = (int) (spritePos[0] / tileSize[0]);
		int upperSpriteIndexY = (int) (spritePos[1] / tileSize[1]);
		int lowerSpriteIndexX = (int) ((spritePos[0] + spriteSize[0] - 1) / tileSize[0]);
		int lowerSpriteIndexY = (int) ((spritePos[1] + spriteSize[1] - 1) / tileSize[1]);
		boxCollisionRight(upperSpriteIndexX, upperSpriteIndexY, lowerSpriteIndexX, lowerSpriteIndexY, ta);
	}

	public static void moveLeft(long deltaTimeMS, AnimationDef leftanimation, float playerspeed, Tile[] ta) {
		spritePos[0] -= deltaTimeMS * playerspeed;
		leftanimation.updateSprite(deltaTimeMS);
		currFrame = leftanimation.getCurrentFrame();
		if (spritePos[0] < 0) {
			spritePos[0] = 0;
		}
		int upperSpriteIndexX = (int) (spritePos[0] / tileSize[0]);
		int upperSpriteIndexY = (int) (spritePos[1] / tileSize[1]);
		int lowerSpriteIndexX = (int) ((spritePos[0] + spriteSize[0] - 1) / tileSize[0]);
		int lowerSpriteIndexY = (int) ((spritePos[1] + spriteSize[1] - 1) / tileSize[1]);
		boxCollisionLeft(upperSpriteIndexX, upperSpriteIndexY, lowerSpriteIndexX, lowerSpriteIndexY, ta);
	}
	

	public static void cameraRight(Camera camera) {
		if (kbState[KeyEvent.VK_D]) {
			if (camera.getX() + windowWidth > backgroundDef.getWidth() * tileSize[0]) {
				camera.setX(camera.getX());
			} else {
				camera.setX(camera.getX() + 3.0f);
			}
		}
	}

	public static void cameraLeft(Camera camera) {
		if (kbState[KeyEvent.VK_A]) {
			if (camera.getX() - 3 < 0) {
				camera.setX(camera.getX());
			} else {
				camera.setX(camera.getX() - 3.0f);
			}
		}
	}

	public static void cameraDown(Camera camera) {

		if (kbState[KeyEvent.VK_S]) {
			if (camera.getY() + windowHeight > backgroundDef.getHeight() * tileSize[1]) {
				camera.setY(camera.getY());
			} else {
				camera.setY(camera.getY() + 3.0f);
			}
		}
	}

	public static void cameraUp(Camera camera) {
		if (kbState[KeyEvent.VK_W]) {
			if (camera.getY() - 3 < 0) {
				camera.setY(camera.getY());
			} else {
				camera.setY(camera.getY() - 3.0f);
			}
		}
	}

	public static void drawBackground(int[] tilearray) {
		BackgroundDef backgroundDef = new BackgroundDef();
		for (int i = 0; i < backgroundDef.getWidth(); i++) {
			for (int j = 0; j < backgroundDef.getHeight(); j++) {
				if (backgroundDef.getTile(i, j) == 0) {
					tilearray[j * backgroundDef.getWidth() + i] = platform;
				} else if (backgroundDef.getTile(i, j) == 1) {
					tilearray[j * backgroundDef.getWidth() + i] = sky;
				} else if (backgroundDef.getTile(i, j) == 2) {
					tilearray[j * backgroundDef.getWidth() + i] = cloud;
				} else if (backgroundDef.getTile(i, j) == 3) {
					tilearray[j * backgroundDef.getWidth() + i] = cloud2;
				} else if (backgroundDef.getTile(i, j) == 4) {
					tilearray[j * backgroundDef.getWidth() + i] = power;
				} else if (backgroundDef.getTile(i, j) == 5) {
					tilearray[j * backgroundDef.getWidth() + i] = tile;
				} else if (backgroundDef.getTile(i, j) == 6) {
					tilearray[j * backgroundDef.getWidth() + i] = tree;
				} else if (backgroundDef.getTile(i, j) == 7) {
					tilearray[j * backgroundDef.getWidth() + i] = tree1;
				} else if (backgroundDef.getTile(i, j) == 8) {
					tilearray[j * backgroundDef.getWidth() + i] = bush;
				} else if (backgroundDef.getTile(i, j) == 9) {
					tilearray[j * backgroundDef.getWidth() + i] = bush1;
				} else if (backgroundDef.getTile(i, j) == 10) {
					tilearray[j * backgroundDef.getWidth() + i] = wall;
				} else if (backgroundDef.getTile(i, j) == 11) {
					tilearray[j * backgroundDef.getWidth() + i] = wall1;
				} else if (backgroundDef.getTile(i, j) == 12) {
					tilearray[j * backgroundDef.getWidth() + i] = wall2;
				} else if (backgroundDef.getTile(i, j) == 13) {
					tilearray[j * backgroundDef.getWidth() + i] = wall3;
				} else if (backgroundDef.getTile(i, j) == 14) {
					tilearray[j * backgroundDef.getWidth() + i] = stair;
				} else if (backgroundDef.getTile(i, j) == 15) {
					tilearray[j * backgroundDef.getWidth() + i] = stair2;
				}
				
			}
		}

	}

	public static void boxCollisionUP(int upperSpriteIndexX, int upperSpriteIndexY, int lowerSpriteIndexX,
			int lowerSpriteIndexY, Tile[] ta) {
		for (int i = upperSpriteIndexX; i <= lowerSpriteIndexX; i++) {
			for (int j = upperSpriteIndexY; j <= lowerSpriteIndexY; j++) {
				int getTile = backgroundDef.getTile(i, j);
				Tile getTile2 = ta[getTile];
				if (getTile2.isCollision()) {
					int tileX = (tileSize[0] * i);
					int tileY = (tileSize[1] * j);
					AABBCamera spriteAABB = new AABBCamera(spritePos[0], spritePos[1], spriteSize[0], spriteSize[1]);
					AABBCamera tileAABB = new AABBCamera(tileX, tileY, tileSize[0], tileSize[1]);
					if (AABBIntersect(spriteAABB, tileAABB)) {
						spritePos[1] += (tileY + tileSize[1] - spritePos[1]);
					}
				}
			}
		}
	}

	public static void boxCollisionDown(int upperSpriteIndexX, int upperSpriteIndexY, int lowerSpriteIndexX,
			int lowerSpriteIndexY, Tile[] ta) {
		for (int i = upperSpriteIndexX; i <= lowerSpriteIndexX; i++) {
			for (int j = upperSpriteIndexY; j <= lowerSpriteIndexY; j++) {
				int getTile = backgroundDef.getTile(i, j);
				Tile getTile2 = ta[getTile];
				if (getTile2.isCollision()) {
					int tileX = (tileSize[0] * i);
					int tileY = (tileSize[1] * j);
					AABBCamera spriteAABB = new AABBCamera(spritePos[0], spritePos[1], spriteSize[0], spriteSize[1]);
					AABBCamera tileAABB = new AABBCamera(tileX, tileY, tileSize[0], tileSize[1]);
					if (AABBIntersect(spriteAABB, tileAABB)) {
						spritePos[1] += (tileY - (spritePos[1] + spriteSize[1]));
					}
				}
			}
		}
	}

	public static void boxCollisionLeft(int upperSpriteIndexX, int upperSpriteIndexY, int lowerSpriteIndexX,
			int lowerSpriteIndexY, Tile[] ta) {
		for (int i = upperSpriteIndexX; i <= lowerSpriteIndexX; i++) {
			for (int j = upperSpriteIndexY; j <= lowerSpriteIndexY; j++) {
				int getTile = backgroundDef.getTile(i, j);
				Tile getTile2 = ta[getTile];
				if (getTile2.isCollision()) {
					int tileX = (tileSize[0] * i);
					int tileY = (tileSize[1] * j);
					AABBCamera spriteAABB = new AABBCamera(spritePos[0], spritePos[1], spriteSize[0], spriteSize[1]);
					AABBCamera tileAABB = new AABBCamera(tileX, tileY, tileSize[0], tileSize[1]);
					if (AABBIntersect(spriteAABB, tileAABB)) {
						spritePos[0] += (tileX + tileSize[0] - spritePos[0]);
					}
				}
			}
		}
	}

	public static void boxCollisionRight(int upperSpriteIndexX, int upperSpriteIndexY, int lowerSpriteIndexX,
			int lowerSpriteIndexY, Tile[] ta) {
		for (int i = upperSpriteIndexX; i <= lowerSpriteIndexX; i++) {
			for (int j = upperSpriteIndexY; j <= lowerSpriteIndexY; j++) {
				int getTile = backgroundDef.getTile(i, j);
				Tile getTile2 = ta[getTile];
				if (getTile2.isCollision()) {
					int tileX = (tileSize[0] * i);
					int tileY = (tileSize[1] * j);
					AABBCamera spriteAABB = new AABBCamera(spritePos[0], spritePos[1], spriteSize[0], spriteSize[1]);
					AABBCamera tileAABB = new AABBCamera(tileX, tileY, tileSize[0], tileSize[1]);
					if (AABBIntersect(spriteAABB, tileAABB)) {
						spritePos[0] += (tileX - (spritePos[0] + spriteSize[0]));
					}
				}
			}
		}
	}

	public static boolean AABBIntersect(AABBCamera box1, AABBCamera box2) {
		// box1 to the right
		if (box1.getX() > box2.getX() + box2.getWidth()) {
			return false;
		}
		// box1 to the left
		if (box1.getX() + box1.getWidth() < box2.getX()) {
			return false;
		}
		// box1 below
		if (box1.getY() > box2.getY() + box2.getHeight()) {
			return false;
		}
		// box1 above
		if (box1.getY() + box1.getHeight() < box2.getY()) {
			return false;
		}
		return true;
	}

	// Load a file into an OpenGL texture and return that texture.
	public static int glTexImageTGAFile(GL2 gl, String filename, int[] out_size) {

		final int BPP = 4;

		DataInputStream file = null;
		try {
			// Open the file.
			file = new DataInputStream(new FileInputStream(filename));
		} catch (FileNotFoundException ex) {
			System.err.format("File: %s -- Could not open for reading.", filename);
			return 0;
		}

		try {
			// Skip first two bytes of data we don't need.
			file.skipBytes(2);

			// Read in the image type. For our purposes the image type
			// should be either a 2 or a 3.
			int imageTypeCode = file.readByte();
			if (imageTypeCode != 2 && imageTypeCode != 3) {
				file.close();
				System.err.format("File: %s -- Unsupported TGA type: %d", filename, imageTypeCode);
				return 0;
			}

			// Skip 9 bytes of data we don't need.
			file.skipBytes(9);

			int imageWidth = Short.reverseBytes(file.readShort());
			int imageHeight = Short.reverseBytes(file.readShort());
			int bitCount = file.readByte();
			file.skipBytes(1);

			// Allocate space for the image data and read it in.
			byte[] bytes = new byte[imageWidth * imageHeight * BPP];

			// Read in data.
			if (bitCount == 32) {
				for (int it = 0; it < imageWidth * imageHeight; ++it) {
					bytes[it * BPP + 0] = file.readByte();
					bytes[it * BPP + 1] = file.readByte();
					bytes[it * BPP + 2] = file.readByte();
					bytes[it * BPP + 3] = file.readByte();
				}
			} else {
				for (int it = 0; it < imageWidth * imageHeight; ++it) {
					bytes[it * BPP + 0] = file.readByte();
					bytes[it * BPP + 1] = file.readByte();
					bytes[it * BPP + 2] = file.readByte();
					bytes[it * BPP + 3] = -1;
				}
			}

			file.close();

			// Load into OpenGL
			int[] texArray = new int[1];
			gl.glGenTextures(1, texArray, 0);
			int tex = texArray[0];
			gl.glBindTexture(GL.GL_TEXTURE_2D, tex);
			gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, imageWidth, imageHeight, 0, GL.GL_BGRA,
					GL.GL_UNSIGNED_BYTE, ByteBuffer.wrap(bytes));
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);

			out_size[0] = imageWidth;
			out_size[1] = imageHeight;
			return tex;
		} catch (IOException ex) {
			System.err.format("File: %s -- Unexpected end of file.", filename);
			return 0;
		}
	}
	

	
	private static void jump(long deltatime) {
		if (!isJumping) {
			isJumping = true;
			originalY = spritePos[1];
		}else if (spritePos[1] >= originalY) {
			
			isJumping = false;
			jumptime = 0;
			return;
		}
		float fixedV = (500 * deltatime / 1000);
		float acc = ((20 * jumptime) * deltatime/ 1000 );
		spritePos[1] = spritePos[1] - fixedV + acc;
		jumptime++;
	}

	public static void glDrawSprite(GL2 gl, int tex, float x, float y, int w, int h) {

		gl.glBindTexture(GL.GL_TEXTURE_2D, tex);
		gl.glBegin(GL2ES3.GL_QUADS);
		{
			gl.glColor3ub((byte) -1, (byte) -1, (byte) -1);
			gl.glTexCoord2f(0, 1);
			gl.glVertex2f(x, y);
			gl.glTexCoord2f(1, 1);
			gl.glVertex2f(x + w, y);
			gl.glTexCoord2f(1, 0);
			gl.glVertex2f(x + w, y + h);
			gl.glTexCoord2f(0, 0);
			gl.glVertex2f(x, y + h);
		}
		gl.glEnd();
	}

}
