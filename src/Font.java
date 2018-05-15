import java.util.HashMap;
public class Font {
	int lineHeight;
	String name;
	HashMap<Character, Glyph> glyphs;

	public Font(int lineHeight, String name) {
		this.lineHeight = lineHeight;
		this.name = name;
		glyphs = new HashMap<Character, Glyph>();
	}

	public void addCharacter(Character character, int image, int width) {
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
