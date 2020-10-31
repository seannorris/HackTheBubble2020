package ui.game.menu.playerConfigs;

import logic.player.Player;
import logic.player.ai.AIPlayer;
import ui.TerminalHandler;
import ui.drawing.colour.BackgroundColour;
import ui.drawing.colour.Colour;
import ui.drawing.colour.ColouredString;
import ui.game.Game;
import ui.game.Piece;
import ui.game.menu.GameMenu;
import ui.menu.LabeledMenuItem;
import ui.menu.MenuItem;
import ui.menu.OptionChooser;

/**
 * Represents an AI player config menu.<br>
 * {@inheritDoc}
 */
public class AIPlayerConfig extends PlayerConfig.NamedPlayerConfig
{
    private static final int DEFAULT_DEPTH = 5;
    private static final String[] DEPTH_OPTIONS = new String[]{
            "1", "2", "3", "4 (Very Easy)", "5 (Easy)", "6 (Normal)", "7 (Hard)", "8 (Extreme)", "9", "10", "11", "12"};
    private static final ColouredString DEPTH_LABEL = new ColouredString("Depth");
    
    private final LabeledMenuItem<OptionChooser> depthSelector;
    
    /**
     * Constructor for AIPlayerConfig.
     * @param terminalHandler The TerminalHandler.
     * @param enabled If the config menu is enabled for selection.
     * @param player2 Boolean; true if config menu belongs to player 2.
     */
    public AIPlayerConfig(TerminalHandler terminalHandler, boolean enabled, boolean player2)
    {
        super(terminalHandler, enabled, player2, getExtras(), String.format("AI Player %d", player2 ? 2 : 1));
        depthSelector = mostRecentDepthSelector;
        depthSelector.getSection().setSelected(DEFAULT_DEPTH);
    }
    
    /**
     * The most recently created depth selector.
     */
    private static LabeledMenuItem<OptionChooser> mostRecentDepthSelector;
    
    /**
     * Gets a new option chooser to choose the depth to perform minmax to.
     * @return The depth selector.
     */
    public static MenuItem[] getExtras()
    {
        mostRecentDepthSelector = new LabeledMenuItem<>(GameMenu.MENU_ITEM_HEIGHT,
                new OptionChooser(DEPTH_OPTIONS, Colour.DEFAULT, Colour.GREEN_BOLD_BRIGHT, BackgroundColour.DEFAULT, Piece.ENABLED_COLOUR, true),
                DEPTH_LABEL, GameMenu.DOTS, GameMenu.FILLER);
        return new MenuItem[] {mostRecentDepthSelector};
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Player getPlayer(Game game)
    {
        return new AIPlayer(isPlayer2(), game, getName(), depthSelector.getSection().getSelected());
    }
}
