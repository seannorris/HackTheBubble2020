package ui.game.menu.playerConfigs;

import logic.player.HumanPlayer;
import logic.player.Player;
import ui.TerminalHandler;
import ui.game.Game;
import ui.menu.MenuItem;

/**
 * Represents a human player config menu.<br>
 * {@inheritDoc}
 */
public class HumanPlayerConfig extends PlayerConfig.NamedPlayerConfig
{
    /**
     * Constructor for HumanPlayerConfig.
     * @param terminalHandler The TerminalHandler.
     * @param enabled If the config menu is enabled for selection.
     * @param player2 Boolean; true if config menu belongs to player 2.
     */
    public HumanPlayerConfig(TerminalHandler terminalHandler, boolean enabled, boolean player2)
    {
        super(terminalHandler, enabled, player2, new MenuItem[0], String.format("Human Player %d", player2 ? 2 : 1));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Player getPlayer(Game game)
    {
        return new HumanPlayer(isPlayer2(), getName(), game);
    }
}
