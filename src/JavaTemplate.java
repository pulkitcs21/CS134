
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
    private static int[] spritePos = new int[] { 0, 440 };    
    
	private static int currFrame;

    // Texture for the sprite.
    private static int cloud;
    private static int power;
    private static int tree;
    private static int tree1;
    private static int tile;
    private static int bush;
    private static int bush1;
    private static int cloud2;
    private static int platform;
    private static int sky;
    
    // Size of the sprite.
    private static int[] spriteSize = new int[2];
    
    // Size of the tile
    private static int[] tileSize = new int[2];
    
    //Making Background declarations
    private static BackgroundDef backgroundDef;

    

    public static void main(String[] args) {
  
    	
        GLProfile gl2Profile;
        try {
            // Make sure we have a recent version of OpenGL
            gl2Profile = GLProfile.get(GLProfile.GL2);
        }
        catch (GLException ex) {
            System.out.println("OpenGL max supported version is too low.");
            System.exit(1);
            return;
        }

        // Create the window and OpenGL context.
        GLWindow window = GLWindow.create(new GLCapabilities(gl2Profile));
        // OSX doubles the windows size 
        window.setSize(800/2, 600/2);
        window.setTitle("Mario 5");
        window.setVisible(true);
        window.setDefaultCloseOperation(
                WindowClosingProtocol.WindowClosingMode.DISPOSE_ON_CLOSE);
        
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
        gl.glViewport(0, 0, 800, 600);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glOrtho(0, 800, 600, 0, 0, 100);
        gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

		// Game initialization goes here.
		//Initializing backgrounds
        
		sky = glTexImageTGAFile(gl, "backgroundImages/sky.tga",tileSize);
		power = glTexImageTGAFile(gl, "backgroundImages/power.tga",tileSize);
		bush = glTexImageTGAFile(gl, "backgroundImages/bush.tga",tileSize);
		bush1 = glTexImageTGAFile(gl, "backgroundImages/bush1.tga",tileSize);
		tree = glTexImageTGAFile(gl, "backgroundImages/tree.tga",tileSize);
		tree1 = glTexImageTGAFile(gl, "backgroundImages/tree1.tga",tileSize);
		tile = glTexImageTGAFile(gl, "backgroundImages/tile.tga",tileSize);
		platform = glTexImageTGAFile(gl, "backgroundImages/platform.tga",tileSize);
		cloud = glTexImageTGAFile(gl, "backgroundImages/cloud.tga",tileSize);
		cloud2 = glTexImageTGAFile(gl, "backgroundImages/cloud2.tga",tileSize);
		backgroundDef = new BackgroundDef();	
	  	int[] tilearray = new int[backgroundDef.getTileSize()];
		
		for(int i=0; i< backgroundDef.getWidth(); i++) {
			for (int j=0; j< backgroundDef.getHeight(); j++) {
				if (backgroundDef.getTile(i, j) == 0 ){
					tilearray[j*backgroundDef.getWidth() + i] = platform;
				}else if (backgroundDef.getTile(i, j) == 1 ){
					tilearray[j*backgroundDef.getWidth() + i] = sky;
				}else if (backgroundDef.getTile(i, j) == 2 ){
					tilearray[j*backgroundDef.getWidth() + i] = cloud;
				}else if (backgroundDef.getTile(i, j) == 3 ){
					tilearray[j*backgroundDef.getWidth() + i] = cloud2;
				}else if (backgroundDef.getTile(i, j) == 4 ){
					tilearray[j*backgroundDef.getWidth() + i] = power;
				}
				else if (backgroundDef.getTile(i, j) == 5 ){
					tilearray[j*backgroundDef.getWidth() + i] = tile;
				}else if (backgroundDef.getTile(i, j) == 6 ){
					tilearray[j*backgroundDef.getWidth() + i] = tree;
				}else if (backgroundDef.getTile(i, j) == 7 ){
					tilearray[j*backgroundDef.getWidth() + i] = tree1;
				}else if (backgroundDef.getTile(i, j) == 8 ){
					tilearray[j*backgroundDef.getWidth() + i] = bush;
				}else if (backgroundDef.getTile(i, j) == 9 ){
					tilearray[j*backgroundDef.getWidth() + i] = bush1;
				}
			}
		}

		FrameDef[] idle = {
				new FrameDef(glTexImageTGAFile(gl, "Animations/idle.tga", spriteSize), (float) 600),
				
				};
      
		FrameDef[] moveleft = {
				new FrameDef(glTexImageTGAFile(gl, "Animations/left.tga", spriteSize), (float) 100),
				new FrameDef(glTexImageTGAFile(gl, "Animations/left2.tga", spriteSize), (float) 100),
				new FrameDef(glTexImageTGAFile(gl, "Animations/left3.tga", spriteSize), (float) 100)
				};
		FrameDef[] moveright = {
				new FrameDef(glTexImageTGAFile(gl, "Animations/right.tga", spriteSize), (float) 100),
				new FrameDef(glTexImageTGAFile(gl, "Animations/right2.tga", spriteSize), (float) 100),
				new FrameDef(glTexImageTGAFile(gl, "Animations/right3.tga", spriteSize), (float) 100),
				new FrameDef(glTexImageTGAFile(gl, "Animations/right4.tga", spriteSize), (float) 100)
				
				};

   		AnimationDef idleanimation = new AnimationDef(idle);  
    		AnimationDef leftanimation = new AnimationDef(moveleft);  
    		AnimationDef rightanimation = new AnimationDef(moveright); 
    	
    		//Camera Initialization
    		Camera camera = new Camera(0,0);
   		
        // The game loop
        long lastFrameNS;
         
		
        long curFrameNS = System.nanoTime();
       
        while (!shouldExit) {
            System.arraycopy(kbState, 0, kbPrevState, 0, kbState.length);
            lastFrameNS = curFrameNS;
            curFrameNS = System.nanoTime();
            long deltaTimeMS = (curFrameNS - lastFrameNS) / 1000000;          
            // Actually, this runs the entire OS message pump.
            window.display();
      
            
            if (!window.isVisible()) {
                shouldExit = true;
                break;
            }
            
            // Game logic goes here.
            if (kbState[KeyEvent.VK_ESCAPE]) {
                shouldExit   = true;
            }       
                //up
                if(kbState[KeyEvent.VK_UP] && (spritePos[1] >0)) {
                		spritePos[1] -= (deltaTimeMS /16) *3 ;  // update the position when sprite moves
                		rightanimation.updateSprite(deltaTimeMS);
                		currFrame = rightanimation.getCurrentFrame();
                		//Intelligent Camera
                		int y = spritePos[1] - camera.getY();
                		if(camera.getY() > 0) {
                			camera.setY(camera.getY() -3);
                		}
                		} 
                
                // Down Key
                else if(kbState[KeyEvent.VK_DOWN] && spritePos[1] <= (599- spriteSize[1])){
	                	spritePos[1] += (deltaTimeMS /16) *3;             	
	                	leftanimation.updateSprite(deltaTimeMS);
                		currFrame = leftanimation.getCurrentFrame();
                		
                		//Intelligent Camera
                		int y = spritePos[1] - camera.getY();
                		if(camera.getY() < 599) {
                			camera.setY(camera.getY() +3);
                		}
                		
                }
                //Left Key
                else if(kbState[KeyEvent.VK_LEFT] && spritePos[0] > 0){ 
                		spritePos[0] -= (deltaTimeMS /16) *3; 
                		leftanimation.updateSprite(deltaTimeMS);
                		currFrame = leftanimation.getCurrentFrame();
                		//Intelligent Camera
                		int x = spritePos[0] - camera.getX();
                		if(camera.getX() > 0 && x<50) {
                			camera.setX(camera.getX() -3);
                		}
                }
                //right Key
                else if(kbState[KeyEvent.VK_RIGHT] && spritePos[0] <= (799- spriteSize[0])){
                		spritePos[0] +=  (deltaTimeMS /16) *3;
                		rightanimation.updateSprite(deltaTimeMS);
                		currFrame = rightanimation.getCurrentFrame();
                		//Intelligent Camera
                		int x = spritePos[0] - camera.getX();
                		if(camera.getX() < 799 && x<50) {
                			camera.setX(camera.getX() +3);
                		}
                		
                } else {
                		idleanimation.updateSprite(deltaTimeMS);
                		currFrame = idleanimation.getCurrentFrame();
                }
                
                //Camera Controls
                
                //UP
			if (kbState[KeyEvent.VK_W]) {
				if (camera.getY() - 3 < 0 ) {
					camera.setY(camera.getY());
				} else {
					camera.setY(camera.getY() - 3);
				}
			}
			//LEft
			if (kbState[KeyEvent.VK_A]) {
				if (camera.getX() - 3 < 0) {
					camera.setX(camera.getX());
				} else {
					camera.setX(camera.getX() - 3);
				}
			}
			//Down
			if (kbState[KeyEvent.VK_S]) {
				if (camera.getY() + 3 > 599 ) {
					camera.setY(camera.getY());
				} else {
					camera.setY(camera.getY() + 3);
				}
			}
			//Right
			if (kbState[KeyEvent.VK_D]) {
				if (camera.getX() + 3 > 799) {
					camera.setX(camera.getX());
				} else {
					camera.setX(camera.getX() + 3);
				}
			}
 
                      
            gl.glClearColor(0, 0, 0, 1);
            gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
            

            for(int i=0; i<backgroundDef.getWidth(); i++) {
            	for(int j=0; j<backgroundDef.getHeight(); j++) {
            			glDrawSprite(gl, tilearray[j * backgroundDef.getWidth() + i],i * tileSize[0] - camera.getX(),
            					j * tileSize[1] - camera.getY(), tileSize[0], tileSize[1]);
            		} 
            }
            // Draw the sprite
            glDrawSprite(gl, currFrame, spritePos[0], spritePos[1], spriteSize[0],spriteSize[1]);
            
	        	
        }
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

            // Read in the image type.  For our purposes the image type
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
            gl.glTexImage2D(
                    GL2.GL_TEXTURE_2D, 0, GL2.GL_RGBA, imageWidth, imageHeight, 0,
                    GL2.GL_BGRA, GL2.GL_UNSIGNED_BYTE, ByteBuffer.wrap(bytes));
            gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
            gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);

            out_size[0] = imageWidth;
            out_size[1] = imageHeight;
            return tex;
        }
        catch (IOException ex) {
            System.err.format("File: %s -- Unexpected end of file.", filename);
            return 0;
        }
    }

    public static void glDrawSprite(GL2 gl, int tex, int x, int y, int w, int h) {
        gl.glBindTexture(GL2.GL_TEXTURE_2D, tex);
        gl.glBegin(GL2.GL_QUADS);
        {
            gl.glColor3ub((byte)-1, (byte)-1, (byte)-1);
            gl.glTexCoord2f(0, 1);
            gl.glVertex2i(x, y);
            gl.glTexCoord2f(1, 1);
            gl.glVertex2i(x + w, y);
            gl.glTexCoord2f(1, 0);
            gl.glVertex2i(x + w, y + h);
            gl.glTexCoord2f(0, 0);
            gl.glVertex2i(x, y + h);
        }
        gl.glEnd();
    }
}
