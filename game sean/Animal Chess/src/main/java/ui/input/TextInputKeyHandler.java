package ui.input;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * A key handler for receiving user text input.
 */
public abstract class TextInputKeyHandler implements KeyHandler
{
    private final String allowedChars;
    
    /**
     * Constructor for TextInputKeyHandler.
     * @param allowedChars A regex pattern that typed characters must match to be allowed.
     */
    public TextInputKeyHandler(String allowedChars)
    {
        this.allowedChars = allowedChars;
    }
    
    /**
     * {@inheritDoc}
     * Checks if the character matches the regex pattern and if so calls the {@link #type(char)} method with it.
     * If the character is a backspace call {@link #backspace()}.
     * For any other character call {@link #other(int)}.
     */
    @Override
    public KeyHandler keyPress(int key) throws IOException
    {
        if(Pattern.matches(allowedChars, String.valueOf((char)key)))
            return type((char)key);
        
        if(key == 8)
            return backspace();
        
        return other(key);
    }
    
    /**
     * Called when an allowed character is typed.
     * @param character The allowed character.
     * @return The new KeyHandler.
     */
    public abstract KeyHandler type(char character);
    
    /**
     * Called when the backspace key is pressed.
     * @return The new KeyHandler.
     */
    public abstract KeyHandler backspace();
    
    /**
     * Called when any other key is pressed.
     * @param key The key that was pressed.
     * @return The new KeyHandler.
     * @throws IOException Pass exception up chain.
     */
    public abstract KeyHandler other(int key) throws IOException;
}
