package ui.game.menu.playerConfigs;

import logic.player.Player;
import ui.TerminalHandler;
import ui.drawing.colour.BackgroundColour;
import ui.drawing.colour.Colour;
import ui.drawing.colour.ColouredString;
import ui.game.Game;
import ui.game.MinimumSize;
import ui.game.Piece;
import ui.game.menu.GameMenu;
import ui.input.selection.layout.SelectableCentredSection;
import ui.menu.LabeledMenuItem;
import ui.menu.Menu;
import ui.menu.MenuItem;
import ui.menu.MenuString;
import ui.menu.TextBox;
import ui.menu.button.Button;
import ui.menu.button.ChangeViewButton;

/**
 * Represents a player's configuration menu.<br>
 * {@inheritDoc}
 */
public abstract class PlayerConfig extends Menu
{
    private final MinimumSize<SelectableCentredSection<PlayerConfig>> container;
    private final ChangeViewButton<MinimumSize<SelectableCentredSection<GameMenu>>, MinimumSize<SelectableCentredSection<GameMenu>>> button;
    private final GameMenu.EscapeHandler escapeHandler;
    private final boolean player2;
    
    public static final String DONE_BUTTON_TEXT = "Done";
    
    /**
     * Constructor for PlayerConfig.
     * @param terminalHandler The TerminalHandler.
     * @param enabled If the config menu is enabled for selection.
     * @param player2 Boolean; true if config menu belongs to player 2.
     * @param extras Implementation specific menu items.
     */
    public PlayerConfig(TerminalHandler terminalHandler, boolean enabled, boolean player2, MenuItem[] extras)
    {
        super(getMenuItems(extras), getButtons(player2, terminalHandler), terminalHandler, getEscapeHandler(terminalHandler), enabled,
                GameMenu.FILLER, player2 ? Piece.PLAYER2_COLOUR : Piece.PLAYER1_COLOUR, GameMenu.BORDER_BACKGROUND_COLOUR);
        var rows = terminalHandler.getTerminal().getHeight();
        var cols = terminalHandler.getTerminal().getWidth();
        container = new MinimumSize<>(rows, cols, new SelectableCentredSection<>(rows, cols, this), terminalHandler);
        button = mostRecentButton;
        escapeHandler = mostRecentEscapeHandler;
        this.player2 = player2;
    }
    
    /**
     * Gets the menu items that are common to all the player config menus and adds the extras to the end.
     * @param extras The extra menu items.
     * @return The array containing the common and extra menu items.
     */
    private static MenuItem[] getMenuItems(MenuItem[] extras)
    {
        var common = new MenuItem[]
                {
                        new MenuString(new ColouredString("Note: press enter to edit text fields"), GameMenu.MENU_ITEM_HEIGHT)
                };
        var out = new MenuItem[common.length + (extras != null ? extras.length : 0)];
        System.arraycopy(common, 0, out, 0, common.length);
        if(extras != null)
            System.arraycopy(extras, 0, out, common.length, extras.length);
        
        return out;
    }
    
    /**
     * Stores the most recently created return to menu button.
     */
    private static ChangeViewButton<MinimumSize<SelectableCentredSection<GameMenu>>, MinimumSize<SelectableCentredSection<GameMenu>>> mostRecentButton;
    
    /**
     * Gets the return to menu button for the bottom of the menu.
     * @param player2 Boolean; true if config menu belongs to player 2.
     * @param handler The TerminalHandler.
     * @return The array of buttons containing the return to menu button.
     */
    private static Button[] getButtons(boolean player2, TerminalHandler handler)
    {
        return new Button[] {mostRecentButton = new ChangeViewButton<>(handler, DONE_BUTTON_TEXT,
                Colour.DEFAULT, player2 ? Piece.PLAYER2_COLOUR : Piece.PLAYER1_COLOUR,
                BackgroundColour.DEFAULT, Piece.ENABLED_COLOUR, true)};
    }
    
    /**
     * Stores the most recently created EscapeHandler.
     */
    private static GameMenu.EscapeHandler mostRecentEscapeHandler;
    
    /**
     * Gets a new escape handler that returns to the main menu.
     * @param handler The TerminalHandler.
     * @return The escape handler.
     */
    private static GameMenu.EscapeHandler getEscapeHandler(TerminalHandler handler)
    {
        return mostRecentEscapeHandler = new GameMenu.EscapeHandler(handler);
    }
    
    /**
     * Returns the container that handles positioning the config menu on the screen.
     * @return The container.
     */
    public MinimumSize<SelectableCentredSection<PlayerConfig>> getContainer()
    {
        return container;
    }
    
    /**
     * Returns the player the menu belongs to.
     * @return Boolean; true if config menu belongs to player 2.
     */
    public boolean isPlayer2()
    {
        return player2;
    }
    
    /**
     * Gets an instance of the player.
     * @param game The game for the player to belong to.
     * @return The new player instance.
     */
    public abstract Player getPlayer(Game game);
    
    /**
     * Sets the main game menu which the config menu belongs to.
     * @param menu The new main game menu.
     */
    public void setGameMenu(GameMenu menu)
    {
        escapeHandler.setMenu(menu);
        button.setContainer(menu.getContainer());
        button.setSelectionHandler(menu.getContainer());
    }
    
    /**
     * Represents a player's configuration menu where the user can type a name.<br>
     * {@inheritDoc}
     */
    public static abstract class NamedPlayerConfig extends PlayerConfig
    {
        private final TextBox nameBox;
    
        /**
         * Constructor for NamedPlayerConfig.
         * @param terminalHandler The TerminalHandler.
         * @param enabled If the config menu is enabled for selection.
         * @param player2 Boolean; true if config menu belongs to player 2.
         * @param extras Implementation specific menu items.
         * @param name The initial name.
         */
        public NamedPlayerConfig(TerminalHandler terminalHandler, boolean enabled, boolean player2, MenuItem[] extras, String name)
        {
            super(terminalHandler, enabled, player2, getMenuItems(terminalHandler, extras, name));
            nameBox = mostRecentNameBox;
        }
    
        /**
         * The most recently created name text box.
         */
        private static TextBox mostRecentNameBox;
    
        /**
         * Combines any additional menu items and places them after the name text box.
         * @param handler The TerminalHandler.
         * @param extras The extra menu items.
         * @param name The initial name.
         * @return The array containing the name and extra menu items.
         */
        private static MenuItem[] getMenuItems(TerminalHandler handler, MenuItem[] extras, String name)
        {
            mostRecentNameBox = new TextBox(20, name, handler, true);
            var common = new MenuItem[]
                    {
                            new LabeledMenuItem<>(GameMenu.MENU_ITEM_HEIGHT, mostRecentNameBox, new ColouredString("Name"), GameMenu.DOTS, GameMenu.FILLER)
                    };
            var out = new MenuItem[common.length + (extras != null ? extras.length : 0)];
            System.arraycopy(common, 0, out, 0, common.length);
            if(extras != null)
                System.arraycopy(extras, 0, out, common.length, extras.length);
        
            return out;
        }
    
        /**
         * Getter for the player's name.
         * @return The player's name.
         */
        public String getName()
        {
            return nameBox.getText();
        }
    
        /**
         * Setter for the player's name.
         * @param name The new name.
         */
        public void setName(String name)
        {
            nameBox.setText(name);
        }
    
    }
}
