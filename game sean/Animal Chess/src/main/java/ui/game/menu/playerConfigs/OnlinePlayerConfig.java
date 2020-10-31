package ui.game.menu.playerConfigs;

import ui.TerminalHandler;
import ui.menu.TextBox;

/**
 * Helper interface for getting the appropriate type of player config for online players.
 * If player 1 then a client player config, if player 2 a server player config.
 */
public interface OnlinePlayerConfig
{
    short DEFAULT_PORT = 25769;
    
    /**
     * Gets the appropriate online player config.
     * @param terminalHandler The TerminalHandler.
     * @param enabled If the config menu is enabled for selection.
     * @param player2 Boolean; true if config menu belongs to player 2.
     * @return The server/client player config instance.
     */
    static PlayerConfig getInstance(TerminalHandler terminalHandler, boolean enabled, boolean player2)
    {
        return player2 ? new ServerPlayerConfig(terminalHandler, enabled) : new ClientPlayerConfig(terminalHandler, enabled);
    }
    
    /**
     * A text box for entering the port to use.
     */
    class PortBox extends TextBox
    {
        public static final String ALLOWED_CHARS = "[0-9]";
    
        /**
         * Constructor for PortBox.
         * @param handler The TerminalHandler.
         * @param enabled If the port box is enabled for selection.
         */
        public PortBox(TerminalHandler handler, boolean enabled)
        {
            super(6, String.valueOf(DEFAULT_PORT), handler, enabled, ALLOWED_CHARS);
        }
    }
}
