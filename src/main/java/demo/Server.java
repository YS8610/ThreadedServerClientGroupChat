package demo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server 
{
    private ServerSocket serverSocket;

    private Server(ServerSocket serverSocket)
    {
        this.serverSocket = serverSocket;
    }

    public void startServer()
    {
        while(!serverSocket.isClosed())
        {
            try 
            {
                Socket socket = serverSocket.accept();
                System.out.println("New Client connected!");
                ClientHandler clientHandler = new ClientHandler(socket);
    
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void closeServerSocket()
    {
        try 
        {
            if(serverSocket!=null)
            {
                serverSocket.close();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException
    {
        ServerSocket serverSocket = new ServerSocket(3000);
        Server server = new Server(serverSocket);
        server.startServer();
    }
}
