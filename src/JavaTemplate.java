
import com.jogamp.nativewindow.WindowClosingProtocol;

import com.jogamp.opengl.*;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.GLProfile;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Random;

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

	static float enemyPosX = 400;
	static float enemyPosY = 510;
	private static float[] enemyPos = new float[] { enemyPosX, enemyPosY };

	private static float[] bulletPos = new float[] { spritePosX, spritePosY };

	private static int currFrame;
	private static int enemyFrame;

	// Texture for the sprite.
	private static int cloud;
	private static int power;
	private static int tree;
	private static int wall;
	private static int wall1;
	private static int wall2;
	private static int wall3;
	private static int tree1;
	private static int tile;
	private static int bush;
	private static int bush1;
	private static int cloud2;
	private static int platform;
	private static int sky;
	private static int enemytex;
	private static int enemytex2;
	private static int bullettex;
	private static int windowWidth = 800;
	private static int windowHeight = 600;

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
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glOrtho(0, windowWidth, windowHeight, 0, 0, 100);
		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glEnable(GL2.GL_BLEND);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

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

		Tile[] ta = { platformt, skyt, cloudt, cloud2t, powert, tilet, treet, tree1t, busht, bush1t, wallt, wall1t,
				wall2t, wall3t };

		// Enemy Texture
		enemytex = glTexImageTGAFile(gl, "enemy/enemy1.tga", enemySize);

		// Bullet Texture
		bullettex = glTexImageTGAFile(gl, "bullet/fire.tga", bulletSize);

		backgroundDef = new BackgroundDef();

		int[] tilearray = new int[backgroundDef.getTileSize()];

		// Function to draw Background
		drawBackground(tilearray);

		// Mario Animations
		FrameDef[] idle = { new FrameDef(glTexImageTGAFile(gl, "Animations/idle.tga", spriteSize), (float) 600),

		};

		FrameDef[] moveleft = { new FrameDef(glTexImageTGAFile(gl, "Animations/left.tga", spriteSize), (float) 100),
				new FrameDef(glTexImageTGAFile(gl, "Animations/left2.tga", spriteSize), (float) 100),
				new FrameDef(glTexImageTGAFile(gl, "Animations/left3.tga", spriteSize), (float) 100) };
		FrameDef[] moveright = { new FrameDef(glTexImageTGAFile(gl, "Animations/right.tga", spriteSize), (float) 100),
				new FrameDef(glTexImageTGAFile(gl, "Animations/right2.tga", spriteSize), (float) 100),
				new FrameDef(glTexImageTGAFile(gl, "Animations/right3.tga", spriteSize), (float) 100),
				new FrameDef(glTexImageTGAFile(gl, "Animations/right4.tga", spriteSize), (float) 100) };

		// Gomboo Animations
		FrameDef[] enemyidle = {
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/idleenemy1.tga", enemySize), (float) 600),
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/idleenemy2.tga", enemySize), (float) 600),
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/idleenemy3.tga", enemySize), (float) 600),

		};
		FrameDef[] enemymoveleft = {
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/leftenemy1.tga", enemySize), (float) 100),
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/leftenemy2.tga", enemySize), (float) 100),
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/leftenemy3.tga", enemySize), (float) 100),
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/leftenemy4.tga", enemySize), (float) 100) };
		FrameDef[] enemymoveright = {
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/rightenemy1.tga", enemySize), (float) 100),
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/rightenemy2.tga", enemySize), (float) 100),
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/rightenemy3.tga", enemySize), (float) 100),
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/rightenemy4.tga", enemySize), (float) 100),
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/rightenemy5.tga", enemySize), (float) 100) };

		AnimationDef idleanimation = new AnimationDef(idle);
		AnimationDef leftanimation = new AnimationDef(moveleft);
		AnimationDef rightanimation = new AnimationDef(moveright);

		AnimationDef enemyidleanimation = new AnimationDef(enemyidle);
		AnimationDef enemyleftanimation = new AnimationDef(enemymoveleft);
		AnimationDef enemyrightanimation = new AnimationDef(enemymoveright);

		// Camera Initialization
		Camera camera = new Camera(0, 0);

		Enemy enem = new Enemy(enemyPos[0], enemyPos[1], enemySize[0], enemySize[1], enemytex, 100);

		ArrayList<Enemy> enemies = new ArrayList<Enemy>();
		enemies.add(enem);

		// Bullets ArrayList
		ArrayList<Bullet> bullets = new ArrayList<Bullet>();

		/*
		 * Font font; font = new Font(8, "full font"); font.addCharacter('0',
		 * glTexImageTGAFile(gl, "JoglTemplate/fonts/fontA/fullfont_0.tga", new int[] {
		 * 50, 50 }), 6); font.addCharacter('1', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_1.tga", new int[] { 50, 50 }), 3);
		 * font.addCharacter('2', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_2.tga", new int[] { 50, 50 }), 6);
		 * font.addCharacter('3', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_3.tga", new int[] { 50, 50 }), 6);
		 * font.addCharacter('4', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_4.tga", new int[] { 50, 50 }), 6);
		 * font.addCharacter('5', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_5.tga", new int[] { 50, 50 }), 6);
		 * font.addCharacter('6', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_6.tga", new int[] { 50, 50 }), 6);
		 * font.addCharacter('7', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_7.tga", new int[] { 50, 50 }), 6);
		 * font.addCharacter('8', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_8.tga", new int[] { 50, 50 }), 6);
		 * font.addCharacter('9', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_9.tga", new int[] { 50, 50 }), 6);
		 * font.addCharacter('/', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_backslash.tga", new int[] { 50, 50 }), 8);
		 * font.addCharacter('=', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_equals.tga", new int[] { 50, 50 }), 8);
		 * font.addCharacter('a', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Lower_A.tga", new int[] { 50, 50 }), 6);
		 * font.addCharacter('b', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Lower_B.tga", new int[] { 50, 50 }), 6);
		 * font.addCharacter('c', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Lower_C.tga", new int[] { 50, 50 }), 6);
		 * font.addCharacter('d', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Lower_D.tga", new int[] { 50, 50 }), 6);
		 * font.addCharacter('e', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Lower_E.tga", new int[] { 50, 50 }), 5);
		 * font.addCharacter('f', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Lower_F.tga", new int[] { 50, 50 }), 6);
		 * font.addCharacter('g', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Lower_G.tga", new int[] { 50, 50 }), 6);
		 * font.addCharacter('h', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Lower_H.tga", new int[] { 50, 50 }), 6);
		 * font.addCharacter('i', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Lower_I.tga", new int[] { 50, 50 }), 4);
		 * font.addCharacter('j', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Lower_J.tga", new int[] { 50, 50 }), 4);
		 * font.addCharacter('k', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Lower_K.tga", new int[] { 50, 50 }), 6);
		 * font.addCharacter('l', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Lower_L.tga", new int[] { 50, 50 }), 4);
		 * font.addCharacter('m', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Lower_M.tga", new int[] { 50, 50 }), 6);
		 * font.addCharacter('n', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Lower_N.tga", new int[] { 50, 50 }), 5);
		 * font.addCharacter('o', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Lower_O.tga", new int[] { 50, 50 }), 5);
		 * font.addCharacter('p', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Lower_P.tga", new int[] { 50, 50 }), 5);
		 * font.addCharacter('q', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Lower_Q.tga", new int[] { 50, 50 }), 6);
		 * font.addCharacter('r', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Lower_R.tga", new int[] { 50, 50 }), 6);
		 * font.addCharacter('s', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Lower_S.tga", new int[] { 50, 50 }), 5);
		 * font.addCharacter('t', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Lower_T.tga", new int[] { 50, 50 }), 5);
		 * font.addCharacter('u', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Lower_U.tga", new int[] { 50, 50 }), 6);
		 * font.addCharacter('v', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Lower_V.tga", new int[] { 50, 50 }), 6);
		 * font.addCharacter('w', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Lower_W.tga", new int[] { 50, 50 }), 7);
		 * font.addCharacter('x', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Lower_X.tga", new int[] { 50, 50 }), 6);
		 * font.addCharacter('y', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Lower_Y.tga", new int[] { 50, 50 }), 6);
		 * font.addCharacter('z', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Lower_Z.tga", new int[] { 50, 50 }), 6);
		 * font.addCharacter('A', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Upper_A.tga", new int[] { 50, 50 }), 8);
		 * font.addCharacter('B', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Upper_B.tga", new int[] { 50, 50 }), 8);
		 * font.addCharacter('C', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Upper_C.tga", new int[] { 50, 50 }), 8);
		 * font.addCharacter('D', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Upper_D.tga", new int[] { 50, 50 }), 8);
		 * font.addCharacter('E', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Upper_E.tga", new int[] { 50, 50 }), 8);
		 * font.addCharacter('F', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Upper_F.tga", new int[] { 50, 50 }), 8);
		 * font.addCharacter('G', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Upper_G.tga", new int[] { 50, 50 }), 8);
		 * font.addCharacter('H', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Upper_H.tga", new int[] { 50, 50 }), 8);
		 * font.addCharacter('I', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Upper_I.tga", new int[] { 50, 50 }), 6);
		 * font.addCharacter('J', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Upper_J.tga", new int[] { 50, 50 }), 8);
		 * font.addCharacter('K', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Upper_K.tga", new int[] { 50, 50 }), 8);
		 * font.addCharacter('L', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Upper_L.tga", new int[] { 50, 50 }), 8);
		 * font.addCharacter('M', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Upper_M.tga", new int[] { 50, 50 }), 8);
		 * font.addCharacter('N', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Upper_N.tga", new int[] { 50, 50 }), 8);
		 * font.addCharacter('O', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Upper_O.tga", new int[] { 50, 50 }), 8);
		 * font.addCharacter('P', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Upper_P.tga", new int[] { 50, 50 }), 8);
		 * font.addCharacter('Q', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Upper_Q.tga", new int[] { 50, 50 }), 8);
		 * font.addCharacter('R', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Upper_R.tga", new int[] { 50, 50 }), 8);
		 * font.addCharacter('S', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Upper_S.tga", new int[] { 50, 50 }), 8);
		 * font.addCharacter('T', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Upper_T.tga", new int[] { 50, 50 }), 7);
		 * font.addCharacter('U', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Upper_U.tga", new int[] { 50, 50 }), 6);
		 * font.addCharacter('V', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Upper_V.tga", new int[] { 50, 50 }), 6);
		 * font.addCharacter('W', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Upper_W.tga", new int[] { 50, 50 }), 8);
		 * font.addCharacter('X', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Upper_X.tga", new int[] { 50, 50 }), 8);
		 * font.addCharacter('Y', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Upper_Y.tga", new int[] { 50, 50 }), 8);
		 * font.addCharacter('Z', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Upper_Z.tga", new int[] { 50, 50 }), 8);
		 * font.addCharacter('+', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_plus.tga", new int[] { 50, 50 }), 8);
		 * font.addCharacter('.', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_point.tga", new int[] { 50, 50 }), 2);
		 * font.addCharacter('?', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_question.tga", new int[] { 50, 50 }), 8);
		 * font.addCharacter('\\', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_quote.tga", new int[] { 50, 50 }), 8);
		 * font.addCharacter('/', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_slash.tga", new int[] { 50, 50 }), 8);
		 * font.addCharacter(' ', glTexImageTGAFile(gl,
		 * "JoglTemplate/fonts/fontA/fullfont_Space.tga", new int[] { 50, 50 }), 8);
		 * 
		 */
		// The game loop
		int physicsDeltaTime = 10;
		int physicsFrameMS = (int) (System.nanoTime() / 1000000);
		long lastFrameNS;

		float playerspeed = 3.0f / 16;
		float enemyspeed = 2.0f / 16;

		long curFrameNS = System.nanoTime();

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
					for (int j = 0; j < enemies.size(); j++) {
						Enemy e = enemies.get(j);
						AABBCamera enemyAABB = new AABBCamera(e.getX(), e.getY(), e.getWidth(), e.getHeight());
						AABBCamera bulletAABB = new AABBCamera(b.getX(), b.getY(), b.getWidth(), b.getHeight());
						if (AABBIntersect(enemyAABB, bulletAABB)) {
							bullets.remove(i);
							e.setHitpoints(e.getHitpoints() - 50);
							if (e.getHitpoints() <= 0) {
								enemies.remove(j);
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
			for (Enemy e : enemies) {
				int action = e.Update(deltaTimeMS);
				float x = e.getX();
				float y = e.getY();
				if (action == 1) {
					if (e.getX() < spritePos[0] && e.getX() > 0 && (e.getX() < (1600 - enemySize[0]))) {
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

					enemyrightanimation.updateSprite(deltaTimeMS);
					enemyFrame = enemyrightanimation.getCurrentFrame();
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
					enemyleftanimation.updateSprite(deltaTimeMS);
					enemyFrame = enemyleftanimation.getCurrentFrame();
				} else {
					if (e.getX() < spritePos[0] && (e.getX() < (1600 - enemySize[0])) && e.getX() > 0) {
						e.setX(x);
					}
					enemyidleanimation.updateSprite(deltaTimeMS);
					enemyFrame = enemyidleanimation.getCurrentFrame();
				}
			}

			// ANIMATIONS FOR MARIO
			if (kbState[KeyEvent.VK_UP] && (spritePos[1] > 0)) {
				moveUp(deltaTimeMS, rightanimation, playerspeed, ta);
			} else if (kbState[KeyEvent.VK_DOWN] && spritePos[1] <= ((backgroundDef.getWidth() * 30) - spriteSize[1])) {
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
			
			gl.glClearColor(0, 0, 0, 1);
			gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

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
			if (AABBIntersect(cameraAABB, spriteAABB)) {
				glDrawSprite(gl, currFrame, spritePos[0] - camera.getX(), spritePos[1] - camera.getY(), spriteSize[0],
						spriteSize[1]);
			}

			for (Enemy e : enemies) {
				AABBCamera enemyAABB = new AABBCamera(e.getX(), e.getY(), e.getWidth(), e.getHeight());
				if (AABBIntersect(cameraAABB, enemyAABB)) {
					glDrawSprite(gl, enemyFrame, e.getX() - camera.getX(), e.getY() - camera.getY(), enemySize[0],
							enemySize[1]);

				}
			}

			for (Bullet b : bullets) {
				AABBCamera bulletAABB = new AABBCamera(b.getX(), b.getY(), b.getWidth(), b.getHeight());
				if (AABBIntersect(cameraAABB, bulletAABB)) {
					glDrawSprite(gl, bullettex, b.getX() - camera.getX(), b.getY() - camera.getY(), bulletSize[0],
							bulletSize[1]);
				}
			}

		}
	}

	/*
	 * public static void DrawText(Font font, int textSize, float[] charPos, String
	 * text, GL2 gl) { int textwidth = 0; for (int i = 0; i < text.length(); i++) {
	 * char x = text.charAt(i); glDrawSprite(gl, font.fonts.get(x).image,
	 * charPos[0]+textwidth, charPos[1], 30, 30); } }
	 * 
	 */
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
				camera.setX(camera.getX() + 3.0f );
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
			gl.glBindTexture(GL2.GL_TEXTURE_2D, tex);
			gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_RGBA, imageWidth, imageHeight, 0, GL2.GL_BGRA,
					GL2.GL_UNSIGNED_BYTE, ByteBuffer.wrap(bytes));
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);

			out_size[0] = imageWidth;
			out_size[1] = imageHeight;
			return tex;
		} catch (IOException ex) {
			System.err.format("File: %s -- Unexpected end of file.", filename);
			return 0;
		}
	}

	public static void glDrawSprite(GL2 gl, int tex, float x, float y, int w, int h) {

		gl.glBindTexture(GL2.GL_TEXTURE_2D, tex);
		gl.glBegin(GL2.GL_QUADS);
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
