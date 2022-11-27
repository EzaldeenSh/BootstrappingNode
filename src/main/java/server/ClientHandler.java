package server;


import communication.Message;
import nodes.NodesContainer;
import data.User;
import data.UserGenerator;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClientHandler implements Runnable{
    private final ObjectOutputStream toClient;
    private final ObjectInputStream fromClient;
    private static final List<User> users =  new ArrayList<>();
    private final Socket client;


    public ClientHandler(Socket socket) throws IOException {
        toClient = new ObjectOutputStream(socket.getOutputStream());
        fromClient = new ObjectInputStream(socket.getInputStream());
        client = socket;
    }
    @Override
    public void run() {
        boolean portSent = false;
        boolean userSent = false;
        try {
            while (!portSent || !userSent){
            String request = (String)fromClient.readObject();
                 if(request.equals("Port")){
                     NodesContainer nodesContainer = NodesContainer.getInstance();

                     int nodePort = nodesContainer.getLeastRegisteredNodePort();
                     System.out.println(nodePort);
                     try {
                         toClient.writeInt(nodePort);
                         toClient.flush();
                         portSent = true;
                         System.out.println("node port written " + nodePort);
                     } catch (IOException e) {
                         throw new RuntimeException(e);
                     }

                 }
            else if(request.equals("User")){
                     UserGenerator userGenerator = new UserGenerator();
                     User user = userGenerator.generateUser();
                     System.out.println("new user is: " + user.toString());

                     while (users.contains(user)){
                         user = userGenerator.generateUser();
                         System.out.println("new user is: " + user.toString());
                     }
                     users.add(user);
                     System.out.println("new list of users are: " );
                     for(User user1 : users){
                         System.out.println(user1.toString());
                     }
                     try {
                         toClient.writeObject(user);
                         toClient.flush();
                         userSent = true;
                         System.out.println("User written");
                     } catch (IOException e) {
                         throw new RuntimeException(e);
                     }
                     JSONObject jsonObject = new JSONObject();
                     jsonObject.put("username" , user.getUsername());
                     jsonObject.put("password" , user.getPassword());
                     Message message = new Message();
                     message.setFunction("WriteObject");
                     String[] params = new String[3];
                     params[0] = "admin";
                     params[1] = "users";
                     params[2] = jsonObject.toJSONString();;
                     message.setParams(params);



                     /*broadcast message to all 4 nodes*/

                 }
        }
         client.close();

    } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

