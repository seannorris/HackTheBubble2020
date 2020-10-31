package ui.menu;

import ui.TerminalHandler;
import ui.drawing.DrawableBufferedSection;
import ui.drawing.Section;
import ui.drawing.colour.BackgroundColour;
import ui.drawing.colour.Colour;
import ui.drawing.colour.ColouredChar;
import ui.game.Piece;
import ui.input.TextInputKeyHandler;
import ui.input.selection.DrawableSelectableBufferedSection;

import java.io.IOException;

/**
 * Represents a text input box.<br>
 * {@inheritDoc}
 */
public class TextBox extends DrawableSelectableBufferedSection<DrawableBufferedSection>
{
    private String text;
    private final TerminalHandler handler;
    private boolean selected;
    private boolean focused;
    private final String allowedChars;
    
    public static final String ALLOWED_CHARS = "[A-Za-z0-9. _\\-:/&?=#]";
    public static final BackgroundColour FOCUSED_COLOUR = Piece.ENABLED_COLOUR;
    public static final BackgroundColour SELECTED_COLOUR = BackgroundColour.WHITE;
    public static final Colour SELECTED_TEXT_COLOUR = Colour.BLACK;
    
    /**
     * Constructor for TextBox.
     * @param width The width of the text box.
     * @param text The initial text.
     * @param handler The TerminalHandler.
     * @param enabled Boolean; true if text box is enabled for selection.
     * @param allowedChars A regex pattern that typed characters must match to be allowed.
     */
    public TextBox(int width, String text, TerminalHandler handler, boolean enabled, String allowedChars)
    {
        super(new DrawableBufferedSection(width, 1), enabled);
        this.handler = handler;
        this.text = text;
        this.allowedChars = allowedChars;
    }
    
    /**
     * Constructor for TextBox with default allowed characters.
     * @param width The width of the text box.
     * @param text The initial text.
     * @param handler The TerminalHandler.
     * @param enabled Boolean; true if text box is enabled for selection.
     */
    public TextBox(int width, String text, TerminalHandler handler, boolean enabled)
    {
        this(width, text, handler, enabled, ALLOWED_CHARS);
    }
    
    /**
     * Constructor for TextBox with default allowed characters and no initial text.
     * @param width The width of the text box.
     * @param handler The TerminalHandler.
     * @param enabled Boolean; true if text box is enabled for selection.
     */
    public TextBox(int width, TerminalHandler handler, boolean enabled)
    {
        this(width, "", handler, enabled);
    }
    
    /**
     * {@inheritDoc}
     * In this case used to use focused colour.
     */
    @Override
    public ui.input.KeyHandler focus(ui.input.KeyHandler current)
    {
        focused = true;
        return super.focus(current);
    }
    
    /**
     * {@inheritDoc}
     * In this case used to use unfocused colour.
     */
    @Override
    protected void unfocus()
    {
        focused = false;
    }
    
    /**
     * {@inheritDoc}
     * In this case used to take over the key handler with a TextInputKeyHandler.
     */
    @Override
    public ui.input.KeyHandler select(ui.input.KeyHandler current)
    {
        selected = true;
        return new KeyHandler(allowedChars);
    }
    
    /**
     * {@inheritDoc}
     * In this case used to set the colours for everything.
     */
    public void update()
    {
        for(int x = 0, len = Math.min(getBuffer()[0].length, text.length()); x < len; x++)
            getBuffer()[0][x] = new ColouredChar(text.charAt(text.length() - len + x), selected ? SELECTED_TEXT_COLOUR : Colour.DEFAULT,
                    selected ? SELECTED_COLOUR : ((focused ? FOCUSED_COLOUR : BackgroundColour.DEFAULT)));
        
        for(var x = text.length(); x < getBuffer()[0].length; x++)
            getBuffer()[0][x] = new ColouredChar(Section.FILLER_CHAR, selected ? SELECTED_TEXT_COLOUR : Colour.DEFAULT,
                    selected ? SELECTED_COLOUR : ((focused ? FOCUSED_COLOUR : BackgroundColour.DEFAULT)));
    }
    
    /**
     * Getter for stored text.
     * @return The stored text.
     */
    public String getText()
    {
        return text;
    }
    
    /**
     * Setter for the stored text.
     * @param text The new text.
     */
    public void setText(String text)
    {
        this.text = text;
    }
    
    /**
     * The TextInputKeyHandler for the TextBox class.<br>
     * {@inheritDoc}
     */
    private class KeyHandler extends TextInputKeyHandler
    {
        private final String old;
        private final MovementKeyHandler movementKeyHandler;
    
        /**
         * Constructor for TextBox KeyHandler.
         * @param allowedChars A regex pattern that typed characters must match to be allowed.
         */
        public KeyHandler(String allowedChars)
        {
            super(allowedChars);
            old = text;
            text = "";
            movementKeyHandler = new MovementKeyHandler();
        }
    
        /**
         * {@inheritDoc}
         * In this case append the typed character to the text box string.
         */
        @Override
        public ui.input.KeyHandler type(char character)
        {
            text += character;
            return this;
        }
    
        /**
         * {@inheritDoc}
         * In this case remove the last character from the text box string.
         */
        @Override
        public ui.input.KeyHandler backspace()
        {
            if(text.length() > 0)
                text = text.substring(0, text.length() - 1);
            
            return this;
        }
    
        /**
         * {@inheritDoc}
         * In this case check to see if the text box editing mode should be left.
         */
        @Override
        public ui.input.KeyHandler other(int key) throws IOException
        {
            return movementKeyHandler.keyPress(key);
        }
    
        /**
         * Used to detect leaving the text box edit mode.<br>
         * {@inheritDoc}
         */
        private class MovementKeyHandler extends ui.input.MovementKeyHandler
        {
            /**
             * Constructor for the TextBox MovementKeyHandler.
             */
            public MovementKeyHandler()
            {
                super(handler, new TextBox.KeyHandler.EscapeHandler());
            }
    
            /**
             * {@inheritDoc}
             * In this case do nothing.
             */
            @Override
            protected ui.input.KeyHandler moveUp()
            {
                return TextBox.KeyHandler.this;
            }
    
            /**
             * {@inheritDoc}
             * In this case do nothing.
             */
            @Override
            protected ui.input.KeyHandler moveDown()
            {
                return TextBox.KeyHandler.this;
            }
    
            /**
             * {@inheritDoc}
             * In this case do nothing.
             */
            @Override
            protected ui.input.KeyHandler moveLeft()
            {
                return TextBox.KeyHandler.this;
            }
    
            /**
             * {@inheritDoc}
             * In this case do nothing.
             */
            @Override
            protected ui.input.KeyHandler moveRight()
            {
                return TextBox.KeyHandler.this;
            }
    
            /**
             * {@inheritDoc}
             * In this case do nothing.
             */
            @Override
            protected ui.input.KeyHandler space()
            {
                return TextBox.KeyHandler.this;
            }
    
            /**
             * {@inheritDoc}
             * In this case exit the selection mode and save the edited text.
             */
            @Override
            protected ui.input.KeyHandler enter()
            {
                selected = false;
                return TextBox.this.getHandler();
            }
    
            /**
             * {@inheritDoc}
             * In this case do nothing.
             */
            @Override
            protected ui.input.KeyHandler other(int key)
            {
                return TextBox.KeyHandler.this;
            }
        }
    
        /**
         * Used to detect cancellation of the text box editing.
         */
        private class EscapeHandler implements ui.input.EscapeHandler
        {
            /**
             * {@inheritDoc}
             * In this case cancel the text box editing.
             */
            @Override
            public ui.input.KeyHandler escapePressed()
            {
                text = old;
                selected = false;
                return TextBox.this.getHandler();
            }
        }
    }
}
