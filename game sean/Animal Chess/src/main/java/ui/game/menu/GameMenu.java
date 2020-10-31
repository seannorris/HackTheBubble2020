package ui.game.menu;

import ui.TerminalHandler;
import ui.drawing.Section;
import ui.drawing.colour.BackgroundColour;
import ui.drawing.colour.Colour;
import ui.drawing.colour.ColouredChar;
import ui.game.MinimumSize;
import ui.input.KeyHandler;
import ui.input.selection.layout.SelectableCentredSection;
import ui.menu.Menu;
import ui.menu.MenuItem;
import ui.menu.button.Button;

/**
 * Represents the main game menu.<br>
 * {@inheritDoc}
 */
public class GameMenu extends Menu
{
    public static final Colour BORDER_COLOUR = Colour.DEFAULT;
    public static final BackgroundColour BORDER_BACKGROUND_COLOUR = BackgroundColour.DEFAULT;
    public static final int MENU_ITEM_HEIGHT = 3;
    public static final ColouredChar FILLER = Section.FILLER;
    public static final ColouredChar DOTS = new ColouredChar('.', FILLER.getColour(), FILLER.getBackgroundColour());
    
    private final MinimumSize<SelectableCentredSection<GameMenu>> container;
    
    /**
     * Constructor for GameMenu.
     * @param terminalHandler The TerminalHandler.
     * @param escapeHandler The escape handler.
     * @param enabled Boolean; true if the menu is enabled for selection.
     * @param player1 The Player object for player 1.
     * @param player2 The Player object for player 2.
     */
    public GameMenu(TerminalHandler terminalHandler, ui.input.EscapeHandler escapeHandler, boolean enabled, Player player1, Player player2)
    {
        super(getMenuItems(player1, player2), getButtons(terminalHandler, player1, player2, enabled), terminalHandler, escapeHandler, enabled, FILLER, BORDER_COLOUR, BORDER_BACKGROUND_COLOUR);
        var rows = terminalHandler.getTerminal().getHeight();
        var cols = terminalHandler.getTerminal().getWidth();
        container = new MinimumSize<>(rows, cols, new SelectableCentredSection<>(rows, cols, this), terminalHandler);
        player1.setMenu(this);
        player2.setMenu(this);
        mostRecent.setMenu(this);
    }
    
    /**
     * Gets the menu items from the two players.
     * @param player1 Player 1.
     * @param player2 Player 2.
     * @return The array of MenuItem.
     */
    private static MenuItem[] getMenuItems(Player player1, Player player2)
    {
        return new MenuItem[]
        {
            player1.getSelector(),
            player1.getConfigButton(),
            player2.getSelector(),
            player2.getConfigButton()
        };
    }
    
    /**
     * The most recently created GameStartButton.
     */
    private static GameStartButton mostRecent;
    
    /**
     * Gets the buttons for the bottom of the menu.
     * @param handler The TerminalHandler.
     * @param player1 Player 1.
     * @param player2 Player 2.
     * @param enabled Boolean; true if the menu is enabled for selection.
     * @return The array of buttons.
     */
    private static Button[] getButtons(TerminalHandler handler, Player player1, Player player2, boolean enabled)
    {
        mostRecent = new GameStartButton(handler, enabled, player1, player2);
        return new Button[]
                {
                        mostRecent,
                        new ExitButton(handler, enabled)
                };
    }
    
    /**
     * Getter for the container for the menu.
     * @return The container.
     */
    public MinimumSize<SelectableCentredSection<GameMenu>> getContainer()
    {
        return container;
    }
    
    /**
     * An escape handler that exits to this menu.<br>
     * {@inheritDoc}
     */
    public static class EscapeHandler implements ui.input.EscapeHandler
    {
        private final TerminalHandler handler;
        private GameMenu menu;
    
        /**
         * Constructor for EscapeHandler.
         * @param handler The TerminalHandler.
         */
        public EscapeHandler(TerminalHandler handler)
        {
            this.handler = handler;
        }
    
        /**
         * Constructor for EscapeHandler with a menu specified.
         * @param handler The TerminalHandler.
         * @param menu The game menu to exit to.
         */
        public EscapeHandler(TerminalHandler handler, GameMenu menu)
        {
            this(handler);
            setMenu(menu);
        }
    
        /**
         * Setter for the game menu to exit to.
         * @param menu The new game menu.
         */
        public void setMenu(GameMenu menu)
        {
            this.menu = menu;
        }
    
        /**
         * {@inheritDoc}
         * In this case set the handlers in the TerminalHandler to those of the GameMenu.
         */
        @Override
        public KeyHandler escapePressed()
        {
            handler.setPrintHandler(menu.getContainer());
            handler.setResizeHandler(menu.getContainer());
            return menu.getContainer().getHandler();
        }
    }
}
