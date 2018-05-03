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
	HashMap<Character, Glyph> glyphs;

	public Font(int lineHeight, String name) {
		this.lineHeight = lineHeight;
		this.name = name;
		glyphs = new HashMap<Character, Glyph>();
	}

	public void addCharacter(char character, int image, int width) {
		Glyph glyph = new Glyph(image, width);
		glyphs.put(character, glyph);
	}

	public int getlineHeight() {
		return lineHeight;
	}
	
	public String getName() {
		return name;
	}
	
	

}
