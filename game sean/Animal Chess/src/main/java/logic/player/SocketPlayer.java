package logic.player;

import logic.Game;
import logic.piece.Chick;
import logic.piece.Elephant;
import logic.piece.Giraffe;
import logic.piece.Lion;
import logic.piece.Piece;
import logic.piece.Rooster;
import ui.BlockingThread;
import ui.TerminalHandler;
import ui.drawing.colour.ColouredString;
import ui.drawing.colour.WrappingColouredString;
import ui.game.MinimumSize;
import ui.game.menu.ExitButton;
import ui.game.menu.GameMenu;
import ui.input.ExitEscapeHandler;
import ui.input.selection.layout.SelectableCentredSection;
import ui.menu.Dialog;
import ui.menu.button.Button;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Represents a player connected via a web socket.<br>
 * {@inheritDoc}
 */
public abstract class SocketPlayer extends Player
{
    private final ui.game.Game game;
    private final short port;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    
    /**
     * Constructor for SocketPlayer.
     * @param player2 Boolean; true if player is player 2.
     * @param game The UI game handler the player belongs to.
     * @param port The port the socket is on.
     */
    public SocketPlayer(boolean player2, ui.game.Game game, short port)
    {
        super(player2, "Connection failure");
        this.game = game;
        this.port = port;
    }
    
    /**
     * Getter for the port, available to subclasses.
     * @return The port.
     */
    protected short getPort()
    {
        return port;
    }
    
    /**
     * Set the socket to a validly connected one.
     * Defines the input and output readers for the socket.
     * @param socket The socket to assign.
     */
    protected void setSocket(Socket socket)
    {
        this.socket = socket;
        try
        {
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * {@inheritDoc}
     * In this case this is gotten from the socket, so if it is not defined call the getSocket method.
     */
    @Override
    public String getName()
    {
        if(socket == null)
            getSocket();
        
        return super.getName();
    }
    
    /**
     * {@inheritDoc}
     * In this case send the board and hand states through the socket then wait for it to do the same back.
     * Still process user input during this time.
     */
    @Override
    public void doMove()
    {
        sendState();
        var receiver = new Receiver();
        try
        {
            if(!game.getTerminalHandler().await(receiver))
            {
                game.getGame().win(!isPlayer2());
                receiver.join();
                return;
            }
        }
        catch(IOException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }
        game.getGame().setPlayer2Turn(!isPlayer2());
    }
    
    /**
     * {@inheritDoc}
     * In this case close the socket if it is open after sending the winner.
     */
    public void gameOver()
    {
        if(socket == null)
            return;
        
        try
        {
            out.writeBoolean(true);
            out.writeBoolean(game.getGame().getWinner());
            out.flush();
            out.close();
            in.close();
            socket.close();
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Sends the state of the board to the socket.
     */
    protected void sendState()
    {
        try
        {
            out.writeBoolean(false);
            out.writeInt(game.getGame().getBoard().size());
            for(var piece : game.getGame().getBoard().values())
                sendPiece(piece);
            
            out.writeInt(game.getGame().getPlayer1Hand().size());
            for(var piece : game.getGame().getPlayer1Hand())
                sendPiece(piece);
            
            out.writeInt(game.getGame().getPlayer2Hand().size());
            for(var piece : game.getGame().getPlayer2Hand())
                sendPiece(piece);
            
            out.flush();
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Receives the board state from the socket.
     */
    protected void receiveState()
    {
        try
        {
            if(in.readBoolean())
            {
                game.getGame().win(in.readBoolean());
                return;
            }
            game.getGame().getBoard().clear();
            for(var x = in.readInt(); x > 0; x--)
            {
                var piece = receivePiece();
                game.getGame().getBoard().put(piece.getPosition(), piece);
            }
            game.getGame().getPlayer1Hand().clear();
            for(var x = in.readInt(); x > 0; x--)
                game.getGame().getPlayer1Hand().add(receivePiece());
            
            game.getGame().getPlayer2Hand().clear();
            for(var x = in.readInt(); x > 0; x--)
                game.getGame().getPlayer2Hand().add(receivePiece());
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Sends a Coord object to the socket by sending the x and y coordinates as integers.
     * @param in The Coord to send.
     */
    private void sendCoord(Game.Coord in)
    {
        try
        {
            out.writeInt(in.getX());
            out.writeInt(in.getY());
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Receives a Coord object from the socket.
     * @return The Coord object.
     */
    private Game.Coord receiveCoord()
    {
        try
        {
            return Game.Coord.at(in.readInt(), in.readInt());
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Sends a piece object to the socket.
     * Sends the type of the piece as a byte.
     * @param piece The piece to send.
     */
    private void sendPiece(Piece piece)
    {
        try
        {
            out.writeInt(piece.getId());
            out.writeBoolean(piece.isPlayer2());
            if(piece.getPosition() == null)
                out.writeBoolean(true);
            else
            {
                out.writeBoolean(false);
                sendCoord(piece.getPosition());
            }
            
            if(piece instanceof Chick)
                out.writeByte(1);
            else if(piece instanceof Rooster)
                out.writeByte(2);
            else if(piece instanceof Elephant)
                out.writeByte(3);
            else if(piece instanceof Giraffe)
                out.writeByte(4);
            else if(piece instanceof Lion)
                out.writeByte(5);
            else
                throw new RuntimeException("Invalid Piece");
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Receives a piece from the socket.
     * @return The received piece.
     */
    private Piece receivePiece()
    {
        try
        {
            var id = in.readInt();
            var player2 = in.readBoolean();
            var inHand = in.readBoolean();
            var coord = inHand ? null : receiveCoord();
            
            switch(in.readByte())
            {
                case 1:
                    return new Chick(player2, coord, id);
                case 2:
                    return new Rooster(player2, coord, id);
                case 3:
                    return new Elephant(player2, coord, id);
                case 4:
                    return new Giraffe(player2, coord, id);
                case 5:
                    return new Lion(player2, coord, game.getGame(), id);
                default:
                    throw new RuntimeException("Invalid Piece");
            }
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Sends a string to the socket.
     * @param in The string to send.
     */
    protected void sendString(String in)
    {
        try
        {
            out.writeUTF(in);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Receives a string from the socket.
     * @return The received string.
     */
    protected String receiveString()
    {
        try
        {
            return in.readUTF();
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Getter for the input reader.  Available to the subclasses.
     * @return The input reader.
     */
    protected DataInputStream getIn()
    {
        return in;
    }
    
    /**
     * Getter for the output writer.  Available to the subclasses.
     * @return The output writer.
     */
    protected DataOutputStream getOut()
    {
        return out;
    }
    
    /**
     * Getter for the UI game object.
     * @return The UI game object.
     */
    protected ui.game.Game getGame()
    {
        return game;
    }
    
    /**
     * Gets the socket connection and exchanges player names.
     */
    protected abstract void getSocket();
    
    /**
     * Receives board state from the socket.<br>
     * {@inheritDoc}
     */
    private class Receiver extends BlockingThread
    {
        /**
         * {@inheritDoc}
         * In this case receives the board state.
         */
        @Override
        protected void operation()
        {
            receiveState();
        }
    }
    
    /**
     * Represents a server socket player.  This is always player 2.<br>
     * {@inheritDoc}
     */
    public static class ServerPlayer extends SocketPlayer
    {
        private ServerSocket serverSocket;
    
        /**
         * Constructor for ServerPlayer.
         * @param game The UI game handler the player belongs to.
         * @param port The port the socket is on.
         */
        public ServerPlayer(ui.game.Game game, short port)
        {
            super(true, game, port);
        }
    
        /**
         * {@inheritDoc}
         * In this case this is done by creating a new server socket and showing a connection screen until a client connects.
         * Any future connections are rejected.
         * Send the other player's name first, then receives its own name.
         */
        @Override
        protected void getSocket()
        {
            try
            {
                serverSocket = new ServerSocket(getPort());
                var accepter = new Accepter(serverSocket);
                var connectScreen = new ConnectingMessage(getGame().getTerminalHandler());
                getGame().getTerminalHandler().setKeyHandler(connectScreen.getContainer().getHandler());
                getGame().getTerminalHandler().setResizeHandler(connectScreen.getContainer());
                getGame().getTerminalHandler().setPrintHandler(connectScreen.getContainer());
                
                if(!getGame().getTerminalHandler().await(accepter) || getGame().getGame().isWon())
                    return;
                
                if(accepter.isError())
                {
                    setName("Connection failure");
                    getGame().getGame().win(isPlayer2());
                    return;
                }
                setSocket(accepter.getAccepted());
                
                var rejecter = new Rejecter();
                rejecter.start();
                
                getOut().writeBoolean(false);
                sendString(getGame().getGame().getPlayer1().getName());
                getOut().flush();
                setName(receiveString());
            }
            catch(SocketException e)
            {
                setName("Connection failure");
                getGame().getGame().win(isPlayer2());
            }
            catch(IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    
        /**
         * {@inheritDoc}
         * If the server socket is open close that too.
         */
        @Override
        public void gameOver()
        {
            try
            {
                if(serverSocket != null)
                    serverSocket.close();
            }
            catch(IOException e)
            {
                throw new RuntimeException(e);
            }
            super.gameOver();
        }
    
        /**
         * Accepts a connection on the server socket.<br>
         * {@inheritDoc}
         */
        private static class Accepter extends BlockingThread
        {
            private final ServerSocket serverSocket;
            private Socket accepted;
            private boolean error;
    
            /**
             * Constructor for Accepter.
             * @param serverSocket The server socket to accept on.
             */
            private Accepter(ServerSocket serverSocket)
            {
                this.serverSocket = serverSocket;
            }
    
            /**
             * {@inheritDoc}
             * In this case accept the first connection to the server socket.
             */
            @Override
            protected void operation()
            {
                try
                {
                    accepted = serverSocket.accept();
                }
                catch(SocketException e)
                {
                    error = true;
                }
                catch(IOException e)
                {
                    throw new RuntimeException(e);
                }
            }
    
            /**
             * Getter for the accepted socket.
             * @return The socket.
             */
            public Socket getAccepted()
            {
                return accepted;
            }
    
            /**
             * Returns true if there was an error accepting the connection.
             * @return Boolean; true if an error has occured.
             */
            public boolean isError()
            {
                return error;
            }
        }
    
        /**
         * Rejects any subsequent connections to the server socket.
         */
        private class Rejecter extends Thread
        {
            /**
             * Sends a boolean that will tell the connecting client the connection has been rejected.
             * Continues until the server socket is closed.
             */
            public void run()
            {
                while(!serverSocket.isClosed())
                {
                    try
                    {
                        var socket = serverSocket.accept();
                        var out = new DataOutputStream(socket.getOutputStream());
                        out.writeBoolean(true);
                        out.flush();
                        out.close();
                        socket.close();
                    }
                    catch(IOException ignored) { }
                }
            }
        }
    
        /**
         * Shown while waiting for a client to connect.
         * Allows the user to cancel the connection.<br>
         * {@inheritDoc}
         */
        private static class ConnectingMessage extends Dialog
        {
            public static final WrappingColouredString SECTION = new WrappingColouredString(new ColouredString("Waiting for connection..."), 5, 30);
            
            private final MinimumSize<SelectableCentredSection<Dialog>> container;
    
            /**
             * Constructor for ConnectingMessage.
             * @param handler The terminal handler.
             */
            public ConnectingMessage(TerminalHandler handler)
            {
                super(SECTION, new Button[]{new ExitButton(handler, true)}, handler, new ExitEscapeHandler(handler),
                        true, GameMenu.FILLER, GameMenu.BORDER_COLOUR, GameMenu.BORDER_BACKGROUND_COLOUR);
                var rows = handler.getTerminal().getHeight();
                var cols = handler.getTerminal().getWidth();
                container = new MinimumSize<>(rows, cols, new SelectableCentredSection<>(rows, cols, this), handler);
            }
    
            /**
             * Getter for the container of the dialog.
             * @return The container.
             */
            public MinimumSize<SelectableCentredSection<Dialog>> getContainer()
            {
                return container;
            }
        }
    }
    
    /**
     * Represents a client socket player.  This is always player 1.<br>
     * {@inheritDoc}
     */
    public static class ClientPlayer extends SocketPlayer
    {
        private final String host;
        private boolean doSend;
    
        /**
         * Constructor for ClientPlayer.
         * @param game The UI game handler the player belongs to.
         * @param host The IP to connect.
         * @param port The port the socket is on.
         */
        public ClientPlayer(ui.game.Game game, String host, short port)
        {
            super(false, game, port);
            this.host = host;
        }
    
        /**
         * {@inheritDoc}
         * In this case this is done by connecting to the server's port and ip.
         * If true is received first then the connection has been rejected.
         * Wait for a name to be sent then send the other player's name.
         */
        @Override
        protected void getSocket()
        {
            try
            {
                setSocket(new Socket(host, getPort()));
                if(getIn().readBoolean())
                {
                    getGame().getGame().win(!getGame().getGame().isPlayer2Turn());
                    return;
                }
                setName(receiveString());
                sendString(getGame().getGame().getPlayer2().getName());
                getOut().flush();
            }
            catch(SocketException e)
            {
                setName("Connection failure");
                getGame().getGame().win(isPlayer2());
            }
            catch(IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    
        /**
         * {@inheritDoc}
         * If it is the first turn skip sending.
         */
        @Override
        protected void sendState()
        {
            if(doSend)
                super.sendState();
            else
                doSend = true;
        }
    }
}