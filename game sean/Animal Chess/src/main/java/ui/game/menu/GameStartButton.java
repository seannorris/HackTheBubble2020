package ui.game.menu;

import ui.TerminalHandler;
import ui.drawing.colour.BackgroundColour;
import ui.drawing.colour.Colour;
import ui.game.Game;
import ui.game.Piece;
import ui.input.KeyHandler;
import ui.menu.button.Button;

/**
 * Starts a new game.<br>
 * {@inheritDoc}
 */
public class GameStartButton extends Button
{
    private final TerminalHandler handler;
    private final Player player1;
    private final Player player2;
    private GameMenu menu;
    
    public static final String TEXT = "Play";
    
    /**
     * Constructor for GameStartButton.
     * @param handler The TerminalHandler.
     * @param enabled Boolean; true if the menu is enabled for selection.
     * @param player1 Player 1.
     * @param player2 Player 2.
     */
    public GameStartButton(TerminalHandler handler, boolean enabled, Player player1, Player player2)
    {
        super(TEXT, Colour.DEFAULT, Colour.GREEN, BackgroundColour.DEFAULT, Piece.ENABLED_COLOUR, enabled);
        this.handler = handler;
        this.player1 = player1;
        this.player2 = player2;
    }
    
    /**
     * Setter for the game menu.
     * @param menu The new game menu.
     */
    public void setMenu(GameMenu menu)
    {
        this.menu = menu;
    }
    
    /**
     * {@inheritDoc}
     * In this case set the handlers in the TerminalHandler to those of the game and call the game's play method.
     */
    @Override
    public KeyHandler select(KeyHandler current)
    {
        var game = new Game(new logic.Game(), player1, player2, menu, handler, enabled());
        handler.setResizeHandler(game.getContainer());
        handler.setPrintHandler(game.getContainer());
        handler.setKeyHandler(game.getContainer().getHandler());
        return game.play();
    }
}
