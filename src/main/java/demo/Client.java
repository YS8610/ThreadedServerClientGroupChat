package demo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client
{
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public Client (Socket socket, String username)
    {
        try
        {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
        }
        catch (IOException e)
        {
            closeEverything(socket, bufferedWriter, bufferedReader);
        }
    }

    public void sendMsg()
    {
        try
        {
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected())
            {
                String msgToSend = scanner.nextLine();
                bufferedWriter.write(username + ": " + msgToSend );
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
            scanner.close();
        } 
        catch (IOException e)
        {
            closeEverything(socket, bufferedWriter, bufferedReader);
        }
    }

    public void listenForMsg()
    {
        new Thread (new Runnable() 
        {
            @Override
            public void run()
            {
                String msgFromGroupChat;
                while (socket.isConnected())
                {
                    try 
                    {
                        msgFromGroupChat = bufferedReader.readLine();
                        System.out.println(msgFromGroupChat);
                    }
                    catch (Exception e)
                    {
                        closeEverything(socket, bufferedWriter, bufferedReader);
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader)
    {
        try
        {
            if (bufferedReader!=null)
            {
                bufferedReader.close();
            }
            if(bufferedWriter!=null)
            {
                bufferedWriter.close();
            }
            if (socket!=null)
            {
                socket.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your Username");
        String username = scanner.nextLine();
        Socket socket = new Socket("127.0.0.1",3000);
        Client client = new Client(socket, username);
        client.listenForMsg();
        client.sendMsg();
    }
}
