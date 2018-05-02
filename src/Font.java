import java.util.HashMap;
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
public class Font {
	int lineHeight;
	String name;
	HashMap<Character, Glyph> fonts;

	public Font(int lineHeight, String name) {
		this.lineHeight = lineHeight;
		this.name = name;
		fonts = new HashMap<Character, Glyph>();
	}

	public void addCharacter(char character, int image, int width) {
		Glyph glyph = new Glyph(image, width);
		fonts.put(character, glyph);
	}

}
//
//class FontDef {
//	
//	private GlyphDef[] fonts;
//	public FontDef(GL2 gl) {
//		init(gl);
//	}
//
//	public void init(GL2 gl) {
//		fonts = new GlyphDef[95];
//		int[] size = new int[2];
//		for (int i = 0; i < 95; i++) {
//			if (i < 9) {
//				fonts[i] = new GlyphDef("char0" + (i + 1));
//				fonts[i].setTex(FinalProj.glTexImageTGAFile(gl, "Font/fonts/char0" + (i + 1) + ".tga", size));
//				fonts[i].setWidth(size[0]);
//				// System.out.println(i + ": " + fonts[i].texName + " || " +
//				// (char) (i + 32));
//			} else {
//				fonts[i] = new GlyphDef("char" + (i + 1));
//				fonts[i].setTex(FinalProj.glTexImageTGAFile(gl, "Font/fonts/char" + (i + 1) + ".tga", size));
//				fonts[i].setWidth(size[0]);
//				// System.out.println(i + ": " + fonts[i].texName + " || " +
//				// (char) (i + 32));
//			}
//		}
//		lineHeight = size[1];
//	}
//}
//
//class GlyphDef{
//	int tex;
//	int width;
//	public GlyphDef(int image, int width) {
//		this.tex = image;
//		this.width = width;
//	}
//	public int getImage() {
//		return tex;
//	}
//	public void setTex(int tex) {
//		this.tex = tex;
//	}
//	public int getWidth() {
//		return width;
//	}
//	public void setWidth(int width) {
//		this.width = width;
//	}
//	
//}