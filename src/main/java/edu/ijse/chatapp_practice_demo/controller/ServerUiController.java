package edu.ijse.chatapp_practice_demo.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerUiController  implements Initializable {

    ServerSocket serverSocket;
    Socket localSocket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;

    @FXML
    private TextArea chatArea;

    @FXML
    private ImageView imageShower;

    @FXML
    private TextField messageField;

    @FXML
    void onSend(ActionEvent event) {
        try {
            dataOutputStream.writeUTF(messageField.getText());
            dataOutputStream.flush();
            showMassage("you > "+messageField.getText());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            serverSocket=new ServerSocket(2004);
            showMassage("server started");
            localSocket=serverSocket.accept();
            dataInputStream=new DataInputStream(localSocket.getInputStream());
            dataInputStream=new DataInputStream(localSocket.getInputStream());
            showMassage("accepted connection-client connected to server");
            new Thread(()->{
                while(true){
                    try {
                        String header=dataInputStream.readUTF();
                        if(header.equals("TEXT")) {
                            String message = dataInputStream.readUTF();
                            showMassage("client > " + message);
                            if (message.equals("exit")) {
                                showMassage("client closed");
                                break;
                            }
                            }
                        if(header.equals("IMAGE")) {

                            int length=dataInputStream.readInt();
                            byte[] image=new byte[length];
                            dataInputStream.readFully(image);

                            ByteArrayInputStream bais=new ByteArrayInputStream(image);

                            Image rImage=new Image(bais);
                            imageShower.setImage(rImage);
                            showMassage("image > "+"received the image");
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showMassage(String message){
        chatArea.appendText(message);
    }
}

