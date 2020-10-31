package ui.game.menu;

import ui.TerminalHandler;
import ui.drawing.colour.BackgroundColour;
import ui.drawing.colour.Colour;
import ui.drawing.colour.ColouredString;
import ui.game.Game;
import ui.game.MinimumSize;
import ui.game.Piece;
import ui.game.menu.playerConfigs.AIPlayerConfig;
import ui.game.menu.playerConfigs.HumanPlayerConfig;
import ui.game.menu.playerConfigs.OnlinePlayerConfig;
import ui.game.menu.playerConfigs.PlayerConfig;
import ui.input.KeyHandler;
import ui.input.selection.layout.SelectableCentredSection;
import ui.menu.LabeledMenuItem;
import ui.menu.MenuItem;
import ui.menu.OptionChooser;
import ui.menu.button.ChangeViewButton;
import ui.menu.button.MenuButton;

/**
 * Represents a player in the context of the game menu.
 */
public class Player
{
    private final boolean player2;
    private final LabeledMenuItem<OptionChooser> selector;
    private final ConfigButton configButton;
    private int selection = -1;
    private final PlayerConfig[] configs;
    
    private static final String[] OPTIONS = new String[]
    {
            "Human",
            "AI",
            "Online"
    };
    
    /**
     * Constructor for Player.
     * Create a new config menu for each type of player.
     * @param player2 Boolean; true if the player is player 2.
     * @param handler The TerminalHandler.
     * @param enabled If the config menus are enabled for selection.
     */
    public Player(boolean player2, TerminalHandler handler, boolean enabled)
    {
        this.player2 = player2;
        this.selector = new LabeledMenuItem<>(GameMenu.MENU_ITEM_HEIGHT,
                new OptionChooser(OPTIONS, Colour.DEFAULT, player2 ? Piece.PLAYER2_COLOUR : Piece.PLAYER1_COLOUR,
                        BackgroundColour.DEFAULT, Piece.ENABLED_COLOUR, enabled),
                new ColouredString(String.format("Player %s Type", getPlayerNumber()),
                        player2 ? Piece.PLAYER2_COLOUR : Piece.PLAYER1_COLOUR), GameMenu.DOTS, GameMenu.FILLER);
        this.configs = new PlayerConfig[]
        {
                new HumanPlayerConfig(handler, enabled, player2),
                new AIPlayerConfig(handler, enabled, player2),
                OnlinePlayerConfig.getInstance(handler, enabled, player2)
        };
        configButton = new ConfigButton(GameMenu.MENU_ITEM_HEIGHT,
                new ChangeViewButton<>(null, null, handler,
                        String.format("Player %s Options", getPlayerNumber()), Colour.DEFAULT,
                        player2 ? Piece.PLAYER2_COLOUR : Piece.PLAYER1_COLOUR,
                        BackgroundColour.DEFAULT, Piece.ENABLED_COLOUR, true));
        
        checkSelection();
    }
    
    /**
     * Gets a new player instance of the selected type.
     * @param game The game the player will belong to.
     * @return The new player.
     */
    public logic.player.Player getPlayer(Game game)
    {
        checkSelection();
        return configs[selection].getPlayer(game);
    }
    
    /**
     * Getter for the player selector.
     * @return The player selector.
     */
    public MenuItem getSelector()
    {
        return selector;
    }
    
    /**
     * Getter for the player config button.
     * @return the player config button.
     */
    public MenuItem getConfigButton()
    {
        checkSelection();
        return configButton;
    }
    
    /**
     * Setter for the game menu.
     * @param menu The new game menu.
     */
    public void setMenu(GameMenu menu)
    {
        for(var config : configs)
            config.setGameMenu(menu);
    }
    
    /**
     * Checks if the user has made a new selection for player type and updates the config button with the appropriate menu.
     */
    private void checkSelection()
    {
        if(selection != selector.getSection().getSelected())
        {
            selection = selector.getSection().getSelected();
    
            configButton.getButton().setContainer(configs[selection].getContainer());
            configButton.getButton().setSelectionHandler(configs[selection].getContainer());
        }
    }
    
    /**
     * Gets the player number as a string from the boolean player2.
     * @return The player number as a string.
     */
    public String getPlayerNumber()
    {
        return player2 ? "2" : "1";
    }
    
    /**
     * A button that launches the player config menu.
     */
    private class ConfigButton extends MenuButton<ChangeViewButton<MinimumSize<SelectableCentredSection<PlayerConfig>>,
            MinimumSize<SelectableCentredSection<PlayerConfig>>>>
    {
        /**
         * Constructor for ConfigButtons.
         * @param rows How many rows to make the menu button tall.
         * @param button The button object to assign.
         */
        public ConfigButton(int rows, ChangeViewButton<MinimumSize<SelectableCentredSection<PlayerConfig>>, MinimumSize<SelectableCentredSection<PlayerConfig>>> button)
        {
            super(rows, button);
        }
    
        /**
         * {@inheritDoc}
         * Before changing to the config, make sure the correct one is selected.
         */
        @Override
        public KeyHandler select(KeyHandler current)
        {
            checkSelection();
            return super.select(current);
        }
    }
}
