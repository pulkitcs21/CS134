import java.util.HashMap;

public class Font {
	int lineHeight;
	String name;
	HashMap <Character, Glyph> fonts;
	
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
