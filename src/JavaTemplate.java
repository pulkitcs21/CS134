
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
	private static int marioHealth = 200;
	private static boolean playerdie = false;
	private static boolean gameOver = false;
	private static int score = 0;

	// =====================================================================================

	// =====================================================================================
	private static int currFrame;
	private static int gombooFrame;
	private static int koopaFrame;
	// =====================================================================================
	// TEXTURE FOR SPRITE.
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
	private static int enemyBulletTex;
	private static int coinFrame;
	private static int coinTex;
	private static int windowWidth = 800;
	private static int windowHeight = 600;
	private static boolean die = false;
	// =====================================================================================
	// JUMP
	private static boolean grounded = true;
	private static float gravity = 1;
	private static float velocity = 0;

	// =====================================================================================
	private static int offsetMaxX = 100 * 128 - windowWidth;
	private static int offsetMinX = 0;
	// Size of the sprite.
	private static int[] spriteSize = new int[2];
	private static int[] enemySize = new int[2];
	private static int[] bulletSize = new int[2];
	private static int[] enemyBulletSize = new int[2];
	// Size of the tile
	private static int[] tileSize = new int[2];
	private static int[] koopaIdle = new int[2];
	private static int[] koopaLeftRight = new int[2];
	// =====================================================================================

	private static Camera camera;
	
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

		koopatex = glTexImageTGAFile(gl, "enemy/koopaidle.tga", koopaIdle);

		// Bullet Texture
		bullettex = glTexImageTGAFile(gl, "bullet/fire.tga", bulletSize);

		enemyBulletTex = glTexImageTGAFile(gl, "bullet/enemyBullet.tga", enemyBulletSize);
		
		//TODO: Change SpriteSize
		coinTex = glTexImageTGAFile(gl, "coin/coin1.tga", spriteSize);

		backgroundDef = new BackgroundDef();

		int[] tilearray = new int[backgroundDef.getTileSize()];

		// Function to draw Background
		backgroundInitialize(tilearray);

		// Mario Animations
		FrameDef[] idle = { new FrameDef(glTexImageTGAFile(gl, "Animations/idle.tga", spriteSize), 600),

		};

		FrameDef[] jump = { new FrameDef(glTexImageTGAFile(gl, "Animations/idle.tga", spriteSize), 100),
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
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/idleenemy1.tga", enemySize), 200),
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/idleenemy2.tga", enemySize), 200),
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/idleenemy3.tga", enemySize), 200),

		};
		FrameDef[] gombooframeleft = {
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/leftenemy1.tga", enemySize), 300),
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/leftenemy2.tga", enemySize), 300),
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/leftenemy3.tga", enemySize), 300),
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/leftenemy4.tga", enemySize), 300) };
		FrameDef[] gombooframeright = {
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/rightenemy1.tga", enemySize), 300),
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/rightenemy2.tga", enemySize), 300),
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/rightenemy3.tga", enemySize), 300),
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/rightenemy4.tga", enemySize), 300),
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/rightenemy5.tga", enemySize), 300) };

		// Koopa Animations
		FrameDef[] koopaframeidle = {
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/koopaidle.tga", koopaIdle), 600),
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/koopaidle2.tga", koopaIdle), 600) };
		FrameDef[] koopaframeleft = {
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/koopaleft.tga", koopaLeftRight), 350),
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/koopaleft2.tga", koopaLeftRight), 350),
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/koopaleft3.tga", koopaLeftRight), 350),
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/koopaleft4.tga", koopaLeftRight), 350) };
		FrameDef[] koopaframeright = {
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/kooparight1.tga", koopaLeftRight), 350),
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/kooparight2.tga", koopaLeftRight), 350),
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/kooparight3.tga", koopaLeftRight), 350),
				new FrameDef(glTexImageTGAFile(gl, "enemyAnimations/kooparight4.tga", koopaLeftRight), 350) };

		// Coin Animations
		//TODO: Change SpriteSize
		FrameDef[] coin_idle = { new FrameDef(glTexImageTGAFile(gl, "coin/coin1.tga", spriteSize), 100),
				new FrameDef(glTexImageTGAFile(gl, "coin/coin2.tga", spriteSize), 100),
				new FrameDef(glTexImageTGAFile(gl, "coin/coin3.tga", spriteSize), 100),
				new FrameDef(glTexImageTGAFile(gl, "coin/coin4.tga", spriteSize), 100),
				new FrameDef(glTexImageTGAFile(gl, "coin/coin5.tga", spriteSize), 100),
				new FrameDef(glTexImageTGAFile(gl, "coin/coin6.tga", spriteSize), 100),
				new FrameDef(glTexImageTGAFile(gl, "coin/coin7.tga", spriteSize), 100), };

		// ANimationDef for Mario
		AnimationDef idleanimation = new AnimationDef(idle);
		AnimationDef leftanimation = new AnimationDef(moveleft);
		AnimationDef rightanimation = new AnimationDef(moveright);
		AnimationDef jumpanimation = new AnimationDef(jump);

		// ANimationDef for Gomboo
		AnimationDef gombooidle = new AnimationDef(gombooframeidle);
		AnimationDef gombooleft = new AnimationDef(gombooframeleft);
		AnimationDef gombooright = new AnimationDef(gombooframeright);

		// ANimationDef for Koopa
		AnimationDef koopaidle = new AnimationDef(koopaframeidle);
		AnimationDef koopaleft = new AnimationDef(koopaframeleft);
		AnimationDef kooparight = new AnimationDef(koopaframeright);

		// ANimationDef for Coin
		AnimationDef coin_flip = new AnimationDef(coin_idle);

		// Camera Initialization
		 camera = new Camera(0, 0);

		ArrayList<Enemy> Gomboo_list = new ArrayList<Enemy>();
		Enemy e1 = new Enemy(400, 510, enemySize[0], enemySize[1], gombootex, 100);
		// Enemy e2 = new Enemy(420, 510, enemySize[0], enemySize[1], gombootex, 100);
		Enemy e3 = new Enemy(1100, 510, enemySize[0], enemySize[1], gombootex, 100);
		// Enemy e4 = new Enemy(1120, 510, enemySize[0], enemySize[1], gombootex, 100);
		Enemy e5 = new Enemy(1600, 510, enemySize[0], enemySize[1], gombootex, 100);
		Enemy e7 = new Enemy(2100, 510, enemySize[0], enemySize[1], gombootex, 100);
		// Enemy e8 = new Enemy(2120, 510, enemySize[0], enemySize[1], gombootex, 100);
		Enemy e12 = new Enemy(2800, 510, enemySize[0], enemySize[1], gombootex, 100);
		Enemy e13 = new Enemy(3050, 510, enemySize[0], enemySize[1], gombootex, 100);
		Enemy e9 = new Enemy(3300, 510, enemySize[0], enemySize[1], gombootex, 100);
		Enemy e10 = new Enemy(3800, 510, enemySize[0], enemySize[1], gombootex, 100);
		Enemy e11 = new Enemy(4200, 510, enemySize[0], enemySize[1], gombootex, 100);
		Enemy e14 = new Enemy(4500, 510, enemySize[0], enemySize[1], gombootex, 100);
		Enemy e15 = new Enemy(4520, 510, enemySize[0], enemySize[1], gombootex, 100);
		Enemy e16 = new Enemy(5200, 510, enemySize[0], enemySize[1], gombootex, 100);

		Gomboo_list.add(e1);
		// Gomboo_list.add(e2);
		Gomboo_list.add(e3);
		// Gomboo_list.add(e4);
		Gomboo_list.add(e5);
		Gomboo_list.add(e7);
		// Gomboo_list.add(e8);
		Gomboo_list.add(e9);
		Gomboo_list.add(e10);
		Gomboo_list.add(e11);
		Gomboo_list.add(e12);
		Gomboo_list.add(e13);
		Gomboo_list.add(e14);
		Gomboo_list.add(e15);
		Gomboo_list.add(e16);

		ArrayList<Enemy> koopa_list = new ArrayList<Enemy>();
		Enemy e17 = new Enemy(480, 510, enemySize[0], enemySize[1], koopatex, 100);
		Enemy e18 = new Enemy(800, 510, enemySize[0], enemySize[1], koopatex, 100);
		Enemy e19 = new Enemy(1200, 510, enemySize[0], enemySize[1], koopatex, 100);
		Enemy e20 = new Enemy(1600, 510, enemySize[0], enemySize[1], koopatex, 100);
		Enemy e21 = new Enemy(2200, 510, enemySize[0], enemySize[1], koopatex, 100);
		Enemy e22 = new Enemy(2400, 510, enemySize[0], enemySize[1], koopatex, 100);

		koopa_list.add(e17);
		koopa_list.add(e18);
		koopa_list.add(e19);
		koopa_list.add(e20);
		koopa_list.add(e21);
		koopa_list.add(e22);

		// Bullets ArrayList
		ArrayList<Bullet> bullets = new ArrayList<Bullet>();

		ArrayList<Bullet> Enemybullets = new ArrayList<Bullet>();

		// FONT TEXTURE
		Font font = new Font(30, "1");
		font.addCharacter('/', glTexImageTGAFile(gl, "fonts/slash.tga", new int[] { 50, 50 }), 20);
		font.addCharacter(' ', glTexImageTGAFile(gl, "fonts/space.tga", new int[] { 50, 50 }), 30);
		font.addCharacter('0', glTexImageTGAFile(gl, "fonts/0.tga", new int[] { 50, 50 }), 25);
		font.addCharacter('1', glTexImageTGAFile(gl, "fonts/1.tga", new int[] { 50, 50 }), 25);
		font.addCharacter('2', glTexImageTGAFile(gl, "fonts/2.tga", new int[] { 50, 50 }), 25);
		font.addCharacter('3', glTexImageTGAFile(gl, "fonts/3.tga", new int[] { 50, 50 }), 25);
		font.addCharacter('4', glTexImageTGAFile(gl, "fonts/4.tga", new int[] { 50, 50 }), 25);
		font.addCharacter('5', glTexImageTGAFile(gl, "fonts/5.tga", new int[] { 50, 50 }), 25);
		font.addCharacter('6', glTexImageTGAFile(gl, "fonts/6.tga", new int[] { 50, 50 }), 25);
		font.addCharacter('7', glTexImageTGAFile(gl, "fonts/7.tga", new int[] { 50, 50 }), 25);
		font.addCharacter('8', glTexImageTGAFile(gl, "fonts/8.tga", new int[] { 50, 50 }), 25);
		font.addCharacter('9', glTexImageTGAFile(gl, "fonts/9.tga", new int[] { 50, 50 }), 25);
		font.addCharacter('D', glTexImageTGAFile(gl, "fonts/upperD.tga", new int[] { 50, 50 }), 30);
		font.addCharacter('L', glTexImageTGAFile(gl, "fonts/upperL.tga", new int[] { 50, 50 }), 30);
		font.addCharacter('O', glTexImageTGAFile(gl, "fonts/upperO.tga", new int[] { 50, 50 }), 30);
		font.addCharacter('R', glTexImageTGAFile(gl, "fonts/upperR.tga", new int[] { 50, 50 }), 30);
		font.addCharacter('W', glTexImageTGAFile(gl, "fonts/upperW.tga", new int[] { 50, 50 }), 30);
		font.addCharacter('A', glTexImageTGAFile(gl, "fonts/upperA.tga", new int[] { 50, 50 }), 30);
		font.addCharacter('B', glTexImageTGAFile(gl, "fonts/upperB.tga", new int[] { 50, 50 }), 30);
		font.addCharacter('C', glTexImageTGAFile(gl, "fonts/upperC.tga", new int[] { 50, 50 }), 30);
		font.addCharacter('E', glTexImageTGAFile(gl, "fonts/upperE.tga", new int[] { 50, 50 }), 30);
		font.addCharacter('F', glTexImageTGAFile(gl, "fonts/upperF.tga", new int[] { 50, 50 }), 30);
		font.addCharacter('G', glTexImageTGAFile(gl, "fonts/upperG.tga", new int[] { 50, 50 }), 30);
		font.addCharacter('H', glTexImageTGAFile(gl, "fonts/upperH.tga", new int[] { 50, 50 }), 30);
		font.addCharacter('I', glTexImageTGAFile(gl, "fonts/upperI.tga", new int[] { 50, 50 }), 30);
		font.addCharacter('J', glTexImageTGAFile(gl, "fonts/upperJ.tga", new int[] { 50, 50 }), 30);
		font.addCharacter('K', glTexImageTGAFile(gl, "fonts/upperK.tga", new int[] { 50, 50 }), 30);
		font.addCharacter('M', glTexImageTGAFile(gl, "fonts/upperM.tga", new int[] { 50, 50 }), 30);
		font.addCharacter('N', glTexImageTGAFile(gl, "fonts/upperN.tga", new int[] { 50, 50 }), 30);
		font.addCharacter('P', glTexImageTGAFile(gl, "fonts/upperP.tga", new int[] { 50, 50 }), 30);
		font.addCharacter('Q', glTexImageTGAFile(gl, "fonts/upperQ.tga", new int[] { 50, 50 }), 30);
		font.addCharacter('S', glTexImageTGAFile(gl, "fonts/upperS.tga", new int[] { 50, 50 }), 30);
		font.addCharacter('T', glTexImageTGAFile(gl, "fonts/upperT.tga", new int[] { 50, 50 }), 30);
		font.addCharacter('U', glTexImageTGAFile(gl, "fonts/upperU.tga", new int[] { 50, 50 }), 30);
		font.addCharacter('V', glTexImageTGAFile(gl, "fonts/upperV.tga", new int[] { 50, 50 }), 30);
		font.addCharacter('X', glTexImageTGAFile(gl, "fonts/upperX.tga", new int[] { 50, 50 }), 30);
		font.addCharacter('Y', glTexImageTGAFile(gl, "fonts/upperY.tga", new int[] { 50, 50 }), 30);
		font.addCharacter('Z', glTexImageTGAFile(gl, "fonts/upperZ.tga", new int[] { 50, 50 }), 30);

		// The game loop
		int physicsDeltaTime = 10;
		int physicsFrameMS = (int) (System.nanoTime() / 1000000);
		long lastFrameNS;

		float playerspeed = 3.0f / 16;
		float enemyspeed = 2.0f / 16;

		long curFrameNS = System.nanoTime();

		Sound soundMain = Sound.loadFromFile("sounds/main.wav");
		Clip bgCLip = soundMain.playLooping();
		Sound mari_die = Sound.loadFromFile("sounds/mariodie.wav");
		Clip marioDie = null;

		while (!shouldExit) {
			System.arraycopy(kbState, 0, kbPrevState, 0, kbState.length);
			lastFrameNS = curFrameNS;
			curFrameNS = System.nanoTime();
			long currFrameMS = curFrameNS / 1000000;
			long deltaTimeMS = (curFrameNS - lastFrameNS) / 1000000;
			long defaultTimeMS = 2000;
			long timeMSNextAction = 0;

			// Actually, this runs the entire OS message pump.
			window.display();

			// physics loop
			do {

				for (int i = 0; i < bullets.size(); i++) {
					Bullet b = bullets.get(i);
					b.setX(b.getX() + 5);
					for (int j = 0; j < Gomboo_list.size(); j++) {
						Enemy e = Gomboo_list.get(j);
						AABBCamera enemyAABB = new AABBCamera(e.getX(), e.getY(), e.getWidth(), e.getHeight());
						AABBCamera bulletAABB = new AABBCamera(b.getX(), b.getY(), b.getWidth(), b.getHeight());
						if (AABBIntersect(enemyAABB, bulletAABB) && bullets.size() > 0) {
							bullets.remove(i);
							score += 50;
							e.setHitpoints(e.getHitpoints() - 50);
							if (e.getHitpoints() <= 0) {
								Gomboo_list.remove(j);
							}
						} else if (b.getX() > (camera.getX() + windowWidth - 20) && bullets.size() > 0) {
							bullets.remove(i);
						}
					}
				}

				// GOMBOO AI TO SHOOT
				for (int i = 0; i < Enemybullets.size(); i++) {
					Bullet b = Enemybullets.get(i);
					if (b.getX() > 0) {
						b.setX(b.getX() - 5);
					}
					for (int j = 0; j < Gomboo_list.size(); j++) {
						Enemy e = Gomboo_list.get(j);
						AABBCamera spriteAABB = new AABBCamera(spritePos[0], spritePos[1], spriteSize[0],
								spriteSize[1]);
						AABBCamera bulletAABB = new AABBCamera(b.getX(), b.getY(), b.getWidth(), b.getHeight());
						if (AABBIntersect(spriteAABB, bulletAABB) && Enemybullets.size() > 0) {
							Enemybullets.remove(i);
							i--;
							
							marioHealth -= 200;
							playerdie = true;
							gameOver = true;
							die= true;
						} else if (b.getX() <= camera.getX() + 10 && Enemybullets.size() > 0) {
							Enemybullets.remove(i);
							i--;
						}
					}
				}

				// KOOPA PROJECTILE HIT
				for (int i = 0; i < bullets.size(); i++) {
					Bullet b = bullets.get(i);
					b.setX(b.getX() + 5);
					for (int j = 0; j < koopa_list.size(); j++) {
						Enemy e = koopa_list.get(j);
						AABBCamera enemyAABB = new AABBCamera(e.getX(), e.getY(), e.getWidth(), e.getHeight());
						AABBCamera bulletAABB = new AABBCamera(b.getX(), b.getY(), b.getWidth(), b.getHeight());
						if (AABBIntersect(enemyAABB, bulletAABB) && bullets.size() > 0) {
							bullets.remove(i);
							score += 50;
							e.setHitpoints(e.getHitpoints() - 50);
							if (e.getHitpoints() <= 0) {
								koopa_list.remove(j);
							}
						} else if (b.getX() > (camera.getX() + windowWidth - 20) && bullets.size() > 0) {
							bullets.remove(i);
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

			// ADD BULLET TO ARRAYLIST
			if (kbState[KeyEvent.VK_SPACE] && !kbPrevState[KeyEvent.VK_SPACE]) {
				bullets.add(new Bullet(spritePos[0], spritePos[1], bulletSize[0], bulletSize[1], bullettex));
			}

			for (int i = 0; i < Gomboo_list.size(); i++) {
				Enemy e = Gomboo_list.get(i);
				if (e.getX() - spritePos[0] < 150) {
					e.timeNext -= deltaTimeMS;
					if (e.timeNext <= 0) {
						e.timeNext += defaultTimeMS;
						Enemybullets.add(new Bullet(e.getX(), e.getY(), bulletSize[0], bulletSize[1], enemyBulletTex));
					}
				}
			}

			// GOMBOO AI
			enemyAI(Gomboo_list, deltaTimeMS, enemyspeed, gombooright, gombooleft, gombooidle, ta);

			// KOOPA AI
			koopaAI(koopa_list, deltaTimeMS, enemyspeed, kooparight, koopaleft, koopaidle, ta);

			// JUMP FUNCTION OF MARIO
			if (grounded) {
				velocity = 0;
			}
			if (grounded && kbState[KeyEvent.VK_UP]) {
				velocity = -17;
			}
			spritePos[1] += (deltaTimeMS / 16) * velocity;
			velocity += (deltaTimeMS / 16) * gravity;

			grounded = false;
	
			int upperSpriteIndexX = (int) (spritePos[0] / tileSize[0]);
			int upperSpriteIndexY = (int) (spritePos[1] / tileSize[1]);
			int lowerSpriteIndexX = (int) ((spritePos[0] + spriteSize[0] - 1) / tileSize[0]);
			int lowerSpriteIndexY = (int) ((spritePos[1] + spriteSize[1] - 1) / tileSize[1]);

			// COLLISION RESOLUTION WHEN JUMPS
			for (int i = upperSpriteIndexX; i <= lowerSpriteIndexX; i++) {
				for (int j = upperSpriteIndexY; j <= lowerSpriteIndexY; j++) {
					if (j * backgroundDef.getWidth() + i >= backgroundDef.getTileSize())
						continue;
					int getTile = backgroundDef.getTile(i, j);
					Tile getTile2 = ta[getTile];
					if (getTile2.isCollision()) {
						int tileX = (tileSize[0] * i);
						int tileY = (tileSize[1] * j);
						AABBCamera spriteAABB = new AABBCamera(spritePos[0], spritePos[1], spriteSize[0],
								spriteSize[1]);
						AABBCamera tileAABB = new AABBCamera(tileX, tileY, tileSize[0], tileSize[1]);
						if (AABBIntersect(spriteAABB, tileAABB)) {
							if (tileY < spritePos[1] + spriteSize[1]
									&& spritePos[1] + spriteSize[1] < tileY + tileSize[1]) {
								spritePos[1] = tileY - spriteSize[1];
								grounded = true;
								spriteAABB = new AABBCamera(spritePos[0], spritePos[1], spriteSize[0], spriteSize[1]);
							}
							if (tileY < spritePos[1] && spritePos[1] < tileY + tileSize[1]) {
								spritePos[1] = tileY + tileSize[1];
								spriteAABB = new AABBCamera(spritePos[0], spritePos[1], spriteSize[0], spriteSize[1]);
							}
						}
					}
				}
			}
			
			//PLAY ANIMATION FOR COINS
			for (int i = upperSpriteIndexX; i <= lowerSpriteIndexX; i++) {
				for (int j = upperSpriteIndexY; j <= lowerSpriteIndexY; j++) {
					if (j * backgroundDef.getWidth() + i >= backgroundDef.getTileSize())
						continue;
					int getTile = backgroundDef.getTile(i, j);
					if(getTile == 4) {
						Tile getTile2 = ta[getTile];
						if (getTile2.isCollision()) {
							int position_x = backgroundDef.getWidth() * i;
							int position_y= backgroundDef.getHeight() * j;
							coin_flip.updateSprite(deltaTimeMS);
							coinFrame = coin_flip.getCurrentFrame();
						}
					}
				}
			}
					
					
			// IF MARIO JUMPS ON GOMBOO THEY DIE
			for (int i = 0; i < Gomboo_list.size(); i++) {
				Enemy e = Gomboo_list.get(i);
				if (velocity > 0 && e.getY() < spritePos[1] + spriteSize[1]
						&& spritePos[1] + spriteSize[1] < e.getY() + e.getHeight()) {
					if (inBetween(e.getX(), spritePos[0], e.getX() + e.getWidth())
							|| inBetween(e.getX(), spritePos[0] + spriteSize[0], e.getX() + e.getWidth())) {
						Gomboo_list.remove(i);
						i--;
						score += 50;
					}
				}
			}

			// IF MARIO JUMPS ON KOOPA THEY DIE
			for (int i = 0; i < koopa_list.size(); i++) {
				Enemy e = koopa_list.get(i);
				if (velocity > 0 && e.getY() < spritePos[1] + spriteSize[1]
						&& spritePos[1] + spriteSize[1] < e.getY() + e.getHeight()) {
					if (inBetween(e.getX(), spritePos[0], e.getX() + e.getWidth())
							|| inBetween(e.getX(), spritePos[0] + spriteSize[0], e.getX() + e.getWidth())) {
						koopa_list.remove(i);
						i--;
						score += 50;
					}
				}
			}

			if (kbState[KeyEvent.VK_P]) {
				moveUp(deltaTimeMS, rightanimation, playerspeed, ta);
			} else if (kbState[KeyEvent.VK_DOWN] && spritePos[1] <= ((backgroundDef.getWidth() * 30) - spriteSize[1])) {
				moveDown(deltaTimeMS, leftanimation, playerspeed, ta);
			} else if (kbState[KeyEvent.VK_RIGHT]
					&& spritePos[0] <= ((tileSize[0] * backgroundDef.getWidth()) - spriteSize[0])) {
				moveRight(deltaTimeMS, rightanimation, playerspeed, ta);
			} else if (kbState[KeyEvent.VK_LEFT]) {
				moveLeft(deltaTimeMS, leftanimation, playerspeed, ta);
			} else {
				idleanimation.updateSprite(deltaTimeMS);
				currFrame = idleanimation.getCurrentFrame();
			}

			intelligentCamera(camera, deltaTimeMS);

			gl.glClearColor(0, 0, 0, 1);
			gl.glClear(GL.GL_COLOR_BUFFER_BIT);

			// DRAW BACKGROUND
			int startIndex = (int) (camera.getX() / tileSize[0]);
			int endIndex = (int) (camera.getX() + (windowWidth - 1)) / tileSize[0];
			int startY = (int) (camera.getY() / tileSize[1]);
			int endY = (int) (camera.getY() + (windowHeight - 1)) / tileSize[1];

			drawBackground(startIndex, endIndex, startY, endY, tilearray, gl, camera);
			// SPRITE COLLISION
			AABBCamera spriteAABB = new AABBCamera(spritePos[0], spritePos[1], spriteSize[0], spriteSize[1]);
			AABBCamera cameraAABB = new AABBCamera(camera.getX(), camera.getY(), windowWidth, windowHeight);
			
			//KILL MARIO IF HE TOUCHES GOMBOO
			kill_mario_goomba(Gomboo_list);
			
			// KILL MARIO IF HE TOUCHES KOOPA
			kill_mario_koopa(koopa_list);
			
			drawMario(cameraAABB, spriteAABB, gl, cameraAABB);

			if (!gameOver)
				drawGomboo(gl, Gomboo_list, camera, cameraAABB);

			if (!gameOver)
				drawKoopa(gl, koopa_list, camera, cameraAABB);

			if (!gameOver)
				drawBullet(gl, camera, bullets, cameraAABB);

			if (!gameOver)
				drawEnemyBullet(gl, camera, Enemybullets, cameraAABB);
			
			int count = 0;
			for (int i = upperSpriteIndexX; i <= lowerSpriteIndexX; i++) {
				for (int j = upperSpriteIndexY; j <= lowerSpriteIndexY; j++) {
					if (j * backgroundDef.getWidth() + i >= backgroundDef.getTileSize())
						continue;
					int getTile = backgroundDef.getTile(i, j);
					if(getTile == 4) {
						Tile getTile2 = ta[getTile];
						int tileY = (tileSize[1] * j);
						if (getTile2.isCollision() && spritePos[1] >= tileY) {
							int tileX = (tileSize[0] * i);
							coin_flip.updateSprite(deltaTimeMS);
							coinFrame = coin_flip.getCurrentFrame();
			AABBCamera coinAABB = new AABBCamera(tileX, tileY, spriteSize[0], spriteSize[1]);
			if (AABBIntersect(cameraAABB, coinAABB) && !gameOver) {
				glDrawSprite(gl, coinFrame, tileX - camera.getX(), (tileY - tileSize[1] - 30) - camera.getY(), spriteSize[0],
						spriteSize[1]);
			}
						}
						score += 50;
					}
				}
			}
			
			
			float[] position = { 50, 0 };
			float[] position2 = { 75, font.lineHeight + 5 };
			float[] score_position = { 300, 0 };
			float[] score_position2 = { 300, font.lineHeight + 5 };
			drawText(font, position, "WORLD", gl);
			drawText(font, position2, "1/1", gl);
			drawText(font, score_position, "SCORE", gl);
			String score_text = Integer.toString(score);
			drawText(font, score_position2, score_text, gl);

			// MARIO GOES IN HOLES
			if (spritePos[1] > camera.getY() + windowHeight) {
					gameOver = true;
					playerdie=true;
			}

			// WHEN MARIO DIES
			if (playerdie) {
				float[] die_position = { 140, 300 };
				drawText(font, die_position, "ENTER FOR NEW GAME", gl);
				bgCLip.stop();
				if (die) {
					die= false;
					
				}
			}
			
			//RESTART GAME
			if (kbState[KeyEvent.VK_ENTER]) {
				playerdie = false;
				gameOver = false;
				camera = new Camera(0,0);
				spritePos= new float[] { spritePosX, spritePosY };
				Gomboo_list.clear(); // CLEAR GOMBOOS
				koopa_list.clear(); // CLEAR KOOPA
				bullets.clear(); // CLEAR BULLETS
				Enemybullets.clear();
				score=0;
				Gomboo_list.add(new Enemy(400, 510, enemySize[0], enemySize[1], gombootex, 100));
				Gomboo_list.add(new Enemy(1100, 510, enemySize[0], enemySize[1], gombootex, 100));
				Gomboo_list.add(new Enemy(1600, 510, enemySize[0], enemySize[1], gombootex, 100));
				Gomboo_list.add(new Enemy(2100, 510, enemySize[0], enemySize[1], gombootex, 100));
				Gomboo_list.add(new Enemy(2800, 510, enemySize[0], enemySize[1], gombootex, 100));
				Gomboo_list.add(new Enemy(3050, 510, enemySize[0], enemySize[1], gombootex, 100));
				Gomboo_list.add(new Enemy(3300, 510, enemySize[0], enemySize[1], gombootex, 100));
				Gomboo_list.add(new Enemy(3800, 510, enemySize[0], enemySize[1], gombootex, 100));
				Gomboo_list.add(new Enemy(4200, 510, enemySize[0], enemySize[1], gombootex, 100));
				Gomboo_list.add(new Enemy(4500, 510, enemySize[0], enemySize[1], gombootex, 100));
				Gomboo_list.add(new Enemy(5200, 510, enemySize[0], enemySize[1], gombootex, 100));
				
				koopa_list.add(new Enemy(480, 510, enemySize[0], enemySize[1], koopatex, 100));
				koopa_list.add(new Enemy(800, 510, enemySize[0], enemySize[1], koopatex, 100));
				koopa_list.add(new Enemy(1200, 510, enemySize[0], enemySize[1], koopatex, 100));
				koopa_list.add(new Enemy(1700, 510, enemySize[0], enemySize[1], koopatex, 100));
				koopa_list.add(new Enemy(2200, 510, enemySize[0], enemySize[1], koopatex, 100));
				koopa_list.add(new Enemy(2400, 510, enemySize[0], enemySize[1], koopatex, 100));
		

			}
		}
	}


	public static void kill_mario_goomba(ArrayList<Enemy> Gomboo_list) {
		for(int i=0; i < Gomboo_list.size(); i++) {
			Enemy e = Gomboo_list.get(i);
			AABBCamera enemyAABB = new AABBCamera(e.getX(), e.getY(), e.getWidth(), e.getHeight());
			if(spritePos[0] + spriteSize[0] >= e.getX() && ((spritePos[0] + spriteSize[0] + spriteSize[1]) >= (e.getX() + e.getHeight())) 
					&& spritePos[1] >= e.getY()) {
				marioHealth -= 200;
				playerdie = true;
				gameOver = true;
			}
			
			if(spritePos[0] < (e.getX() + e.getWidth()) && spritePos[0] > e.getX() && spritePos[1] >= e.getY()) {
				marioHealth -= 200;
				playerdie = true;
				gameOver = true;
			}
		}
	}
	public static void kill_mario_koopa(ArrayList<Enemy> koopa_list) {
		for(int i=0; i < koopa_list.size(); i++) {
			Enemy e = koopa_list.get(i);
			AABBCamera enemyAABB = new AABBCamera(e.getX(), e.getY(), e.getWidth(), e.getHeight());
			if(spritePos[0] + spriteSize[0] >= e.getX() && ((spritePos[0] + spriteSize[0] + spriteSize[1]) >= (e.getX() + e.getHeight())) 
					&& spritePos[1] >= e.getY()) {
				marioHealth -= 200;
				playerdie = true;
				gameOver = true;
			}
			
			if(spritePos[0] < (e.getX() + e.getWidth()) && spritePos[0] > e.getX() && spritePos[1] >= e.getY()) {
				marioHealth -= 200;
				playerdie = true;
				gameOver = true;
			}
		}
	}
	public static void koopaAI(ArrayList<Enemy> koopa_list, long deltaTimeMS, float enemyspeed,
			AnimationDef kooparight, AnimationDef koopaleft, AnimationDef koopaidle, Tile[] ta) {
		for (Enemy e : koopa_list) {
			int action = e.Update(deltaTimeMS);
			int upperSpriteIndexX = (int) (e.getX() / tileSize[0]);
			int upperSpriteIndexY = (int) (e.getY() / tileSize[1]);
			int lowerSpriteIndexX = (int) ((e.getX() + e.getX() - 1) / tileSize[0]);
			int lowerSpriteIndexY = (int) ((e.getY() + e.getY() - 1) / tileSize[1]);
			if (action == 1) {
				// FOLLOW THE MARIO
				float x = e.getX();
				// ENEMY IS ON THE LEFT
				if (e.getX() < spritePos[0] && e.getX() > 0 && (e.getX() < (1600 - enemySize[0]))) {
					// MOVE RIGHT
					x += deltaTimeMS * enemyspeed;
					if (x < 0)
						x = 0;
					e.setX(x);
					kooparight.updateSprite(deltaTimeMS);
					koopaFrame = kooparight.getCurrentFrame();
					EnemyboxCollisionRight(upperSpriteIndexX, upperSpriteIndexY, lowerSpriteIndexX,
							lowerSpriteIndexY, ta, e);
				}
				// ENEMY IS ON THE RIGHT
				// MOVE LEFT
				if (e.getX() > spritePos[0] && (e.getX() < (1600 - enemySize[0]))) {
					x -= deltaTimeMS * enemyspeed;
					if (x < 0)
						x = 0;
					e.setX(x);
					koopaleft.updateSprite(deltaTimeMS);
					koopaFrame = koopaleft.getCurrentFrame();
					EnemyboxCollisionLeft(upperSpriteIndexX, upperSpriteIndexY, lowerSpriteIndexX,
							lowerSpriteIndexY, ta, e);
				}

			} else if (action == 2) {
				// ENEMY IS ON THE LEFT
				if (e.getX() < spritePos[0] && e.getX() > 0 && (e.getX() < (1600 - enemySize[0]))) {
					// IF ENEMY TOO CLOSE, MOVE AWAY TO LEFT
					if (spritePos[0] - e.getX() < 20) {
						e.setX(e.getX() - deltaTimeMS * enemyspeed);
						koopaleft.updateSprite(deltaTimeMS);
						koopaFrame = koopaleft.getCurrentFrame();
						EnemyboxCollisionLeft(upperSpriteIndexX, upperSpriteIndexY, lowerSpriteIndexX,
								lowerSpriteIndexY, ta, e);
					}
				}

				// ENEMY IS ON RIGHT
				if (e.getX() > spritePos[0] && (e.getX() < (1600 - enemySize[0]))) {
					if (e.getX() - spritePos[0] < 20) {
						// MOVE TO RIGHT
						e.setX(e.getX() + deltaTimeMS * enemyspeed);
						kooparight.updateSprite(deltaTimeMS);
						koopaFrame = kooparight.getCurrentFrame();
						EnemyboxCollisionRight(upperSpriteIndexX, upperSpriteIndexY, lowerSpriteIndexX,
								lowerSpriteIndexY, ta, e);
					}
				}
			} else {
				if (e.getX() < spritePos[0] && (e.getX() < (1600 - enemySize[0])) && e.getX() > 0) {
					e.setX(e.getX());
				}
				koopaidle.updateSprite(deltaTimeMS);
				koopaFrame = koopaidle.getCurrentFrame();
			}
		}
	}
	public static void enemyAI(ArrayList<Enemy> Gomboo_list, long deltaTimeMS, float enemyspeed,
			AnimationDef gombooright, AnimationDef gombooleft, AnimationDef gombooidle, Tile[] ta) {
		for (Enemy e : Gomboo_list) {
			int action = e.Update(deltaTimeMS);

			int upperSpriteIndexX = (int) (e.getX() / tileSize[0]);
			int upperSpriteIndexY = (int) (e.getY() / tileSize[1]);
			int lowerSpriteIndexX = (int) ((e.getX() + e.getX() - 1) / tileSize[0]);
			int lowerSpriteIndexY = (int) ((e.getY() + e.getY() - 1) / tileSize[1]);

			if (action == 1) {
				// FOLLOW THE MARIO
				float x = e.getX();
				// ENEMY IS ON THE LEFT
				if (e.getX() < spritePos[0] && e.getX() > 0 && (e.getX() < (1600 - enemySize[0]))) {
					// MOVE RIGHT
					x += deltaTimeMS * enemyspeed;
					if (x < 0)
						x = 0;
					e.setX(x);
					gombooright.updateSprite(deltaTimeMS);
					gombooFrame = gombooright.getCurrentFrame();
					EnemyboxCollisionRight(upperSpriteIndexX, upperSpriteIndexY, lowerSpriteIndexX, lowerSpriteIndexY,
							ta, e);
				}
				// ENEMY IS ON THE RIGHT
				// MOVE LEFT
				if (e.getX() > spritePos[0] && (e.getX() < (1600 - enemySize[0]))) {
					x -= deltaTimeMS * enemyspeed;
					if (x < 0)
						x = 0;
					e.setX(x);
					gombooleft.updateSprite(deltaTimeMS);
					gombooFrame = gombooleft.getCurrentFrame();
					EnemyboxCollisionLeft(upperSpriteIndexX, upperSpriteIndexY, lowerSpriteIndexX, lowerSpriteIndexY,
							ta, e);
				}

			} else if (action == 2) {
				// ENEMY IS ON THE LEFT
				if (e.getX() < spritePos[0] && e.getX() > 0 && (e.getX() < (1600 - enemySize[0]))) {
					// IF ENEMY TOO CLOSE, MOVE AWAY TO LEFT
					if (spritePos[0] - e.getX() < 20) {
						e.setX(e.getX() - deltaTimeMS * enemyspeed);
						gombooleft.updateSprite(deltaTimeMS);
						gombooFrame = gombooleft.getCurrentFrame();
						EnemyboxCollisionLeft(upperSpriteIndexX, upperSpriteIndexY, lowerSpriteIndexX,
								lowerSpriteIndexY, ta, e);
					}
				}

				// ENEMY IS ON RIGHT
				if (e.getX() > spritePos[0] && (e.getX() < (1600 - enemySize[0]))) {
					if (e.getX() - spritePos[0] < 20) {
						// MOVE TO RIGHT
						e.setX(e.getX() + deltaTimeMS * enemyspeed);
						gombooright.updateSprite(deltaTimeMS);
						gombooFrame = gombooright.getCurrentFrame();
						EnemyboxCollisionRight(upperSpriteIndexX, upperSpriteIndexY, lowerSpriteIndexX,
								lowerSpriteIndexY, ta, e);
					}
				}
			} else {
				if (e.getX() < spritePos[0] && (e.getX() < (1600 - enemySize[0])) && e.getX() > 0) {
					e.setX(e.getX());
				}
				gombooidle.updateSprite(deltaTimeMS);
				gombooFrame = gombooidle.getCurrentFrame();
			}
		}
	}

	public static void drawBackground(int startIndex, int endIndex, int startY, int endY, int[] tilearray, GL2 gl,
			Camera camera) {
		for (int i = startIndex; i <= endIndex; i++) {
			for (int j = startY; j <= endY; j++) {
				int index = (j * backgroundDef.getWidth() + i);
				if (index >= tilearray.length)
					continue;
				if (!gameOver) {
					glDrawSprite(gl, tilearray[j * backgroundDef.getWidth() + i], i * tileSize[0] - camera.getX(),
							j * tileSize[1] - camera.getY(), tileSize[0], tileSize[1]);
				}
			}
		}
	}

	public static void intelligentCamera(Camera camera, long deltaTimeMS) {
		camera.setX(spritePos[0] - windowWidth / 2);
		if (camera.getX() > offsetMaxX) {
			camera.setX(offsetMaxX);
		} else if (camera.getX() < offsetMinX) {
			camera.setX(offsetMinX);
		}

	}

	public static void drawMario(AABBCamera cameraAABB, AABBCamera spriteAABB, GL2 gl, Camera camera) {
		if (AABBIntersect(cameraAABB, spriteAABB) && !gameOver) {
			glDrawSprite(gl, currFrame, spritePos[0] - camera.getX(), spritePos[1] - camera.getY(), spriteSize[0],
					spriteSize[1]);
		}
	}

	public static void drawGomboo(GL2 gl, ArrayList<Enemy> arrayGomboo, Camera camera, AABBCamera cameraAABB) {
		for (Enemy e : arrayGomboo) {
			AABBCamera enemyAABB = new AABBCamera(e.getX(), e.getY(), e.getWidth(), e.getHeight());
			if (AABBIntersect(cameraAABB, enemyAABB)) {
				glDrawSprite(gl, gombooFrame, e.getX() - camera.getX(), e.getY() - camera.getY(), enemySize[0],
						enemySize[1]);

			}
		}
	}

	public static void drawKoopa(GL2 gl, ArrayList<Enemy> koopa_list, Camera camera, AABBCamera cameraAABB) {
		for (Enemy e : koopa_list) {
			AABBCamera enemyAABB = new AABBCamera(e.getX(), e.getY(), e.getWidth(), e.getHeight());
			if (AABBIntersect(cameraAABB, enemyAABB)) {
				glDrawSprite(gl, koopaFrame, e.getX() - camera.getX(), e.getY() - camera.getY(), enemySize[0],
						enemySize[1]);

			}
		}
	}

	public static void drawBullet(GL2 gl, Camera camera, ArrayList<Bullet> bullets, AABBCamera cameraAABB) {
		for (Bullet b : bullets) {
			AABBCamera bulletAABB = new AABBCamera(b.getX(), b.getY(), b.getWidth(), b.getHeight());
			if (AABBIntersect(cameraAABB, bulletAABB)) {
				glDrawSprite(gl, bullettex, b.getX() - camera.getX(), b.getY() - camera.getY(), bulletSize[0],
						bulletSize[1]);
			}
		}
	}

	public static void drawEnemyBullet(GL2 gl, Camera camera, ArrayList<Bullet> enemyBullets, AABBCamera cameraAABB) {
		for (Bullet b : enemyBullets) {
			AABBCamera bulletAABB = new AABBCamera(b.getX(), b.getY(), b.getWidth(), b.getHeight());
			if (AABBIntersect(cameraAABB, bulletAABB)) {
				glDrawSprite(gl, enemyBulletTex, b.getX() - camera.getX(), b.getY() - camera.getY(), bulletSize[0],
						bulletSize[1]);
			}
		}
	}

	public static void drawText(Font font, float[] charPos, String text, GL2 gl) {
		for (int i = 0; i < text.length(); i++) {
			char x = text.charAt(i);
			glDrawSprite(gl, font.glyphs.get(x).image, charPos[0], charPos[1], font.getlineHeight(),
					font.glyphs.get(x).width);
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
				camera.setX(camera.getX() + 9.0f);
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
				camera.setY(camera.getY() + 9.0f);
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

	public static void backgroundInitialize(int[] tilearray) {
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
				if (j * backgroundDef.getWidth() + i >= backgroundDef.getTileSize())
					continue;
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
				if (j * backgroundDef.getWidth() + i >= backgroundDef.getTileSize())
					continue;
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
				if (j * backgroundDef.getWidth() + i >= backgroundDef.getTileSize())
					continue;
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

	public static void EnemyboxCollisionLeft(int upperSpriteIndexX, int upperSpriteIndexY, int lowerSpriteIndexX,
			int lowerSpriteIndexY, Tile[] ta, Enemy e) {
		for (int i = upperSpriteIndexX; i <= lowerSpriteIndexX; i++) {
			for (int j = upperSpriteIndexY; j <= lowerSpriteIndexY; j++) {
				if (j * backgroundDef.getWidth() + i >= backgroundDef.getTileSize())
					continue;
				int getTile = backgroundDef.getTile(i, j);
				Tile getTile2 = ta[getTile];
				if (getTile2.isCollision()) {
					int tileX = (tileSize[0] * i);
					int tileY = (tileSize[1] * j);
					AABBCamera spriteAABB = new AABBCamera(e.getX(), e.getY(), enemySize[0], enemySize[1]);
					AABBCamera tileAABB = new AABBCamera(tileX, tileY, tileSize[0], tileSize[1]);
					if (AABBIntersect(spriteAABB, tileAABB)) {
						e.setX(e.getX() + (tileX + tileSize[0] - e.getX()));
						spriteAABB = new AABBCamera(e.getX(), e.getY(), enemySize[0], enemySize[1]);
					}
				}
			}
		}
	}

	public static void EnemyboxCollisionRight(int upperSpriteIndexX, int upperSpriteIndexY, int lowerSpriteIndexX,
			int lowerSpriteIndexY, Tile[] ta, Enemy e) {
		for (int i = upperSpriteIndexX; i <= lowerSpriteIndexX; i++) {
			for (int j = upperSpriteIndexY; j <= lowerSpriteIndexY; j++) {
				if (j * backgroundDef.getWidth() + i >= backgroundDef.getTileSize())
					continue;
				int getTile = backgroundDef.getTile(i, j);
				Tile getTile2 = ta[getTile];
				if (getTile2.isCollision()) {
					int tileX = (tileSize[0] * i);
					int tileY = (tileSize[1] * j);
					AABBCamera spriteAABB = new AABBCamera(e.getX(), e.getY(), enemySize[0], enemySize[1]);
					AABBCamera tileAABB = new AABBCamera(tileX, tileY, tileSize[0], tileSize[1]);
					if (AABBIntersect(spriteAABB, tileAABB)) {
						e.setX(e.getX() + (tileX - (e.getX() + enemySize[0])));
						spriteAABB = new AABBCamera(e.getX(), e.getY(), enemySize[0], enemySize[1]);
					}
				}
			}
		}
	}

	public static void boxCollisionRight(int upperSpriteIndexX, int upperSpriteIndexY, int lowerSpriteIndexX,
			int lowerSpriteIndexY, Tile[] ta) {
		for (int i = upperSpriteIndexX; i <= lowerSpriteIndexX; i++) {
			for (int j = upperSpriteIndexY; j <= lowerSpriteIndexY; j++) {
				if (j * backgroundDef.getWidth() + i >= backgroundDef.getTileSize())
					continue;
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

	public static boolean inBetween(float x, float y, float z) {
		return x <= y && y <= z;
	}
}
