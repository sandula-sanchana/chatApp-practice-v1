package edu.ijse.chatapp_practice_demo.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientUiController implements Initializable {

    public ImageView imageV;
    Socket socket;
    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;
    Image image;

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField messageField;

    @FXML
    void onSend(ActionEvent event) {
       if (messageField.getText().isEmpty()) {
           showMassage("Please enter your message");
       }
       String message = messageField.getText();
        try {
            dataOutputStream.writeUTF("TEXT");
            dataOutputStream.writeUTF(message);
            dataOutputStream.flush();
            showMassage("you > "+message);
            messageField.clear();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void selectImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("image files","*.jpg","*.png"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File file=fileChooser.showOpenDialog(null);

        if(file!=null){
            image = new Image(file.toURI().toString());
            imageV.setImage(image);
        }else {
            showMassage("Please select a file");
        }

    }

    @FXML
    void onSendImage(ActionEvent event) {
        try {
            dataOutputStream.writeUTF("IMAGE");

            File file=new File(new URI(image.getUrl()));
            BufferedImage image=ImageIO.read(file);
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            ImageIO.write(image,"jpg",baos);
            byte[] byteArray=baos.toByteArray();
            dataOutputStream.writeInt(byteArray.length);
            dataOutputStream.write(byteArray);
            dataOutputStream.flush();
            showMassage("you > "+"image sent");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            socket=new Socket("localhost",2004);
            dataInputStream=new DataInputStream(socket.getInputStream());
            dataOutputStream=new DataOutputStream(socket.getOutputStream());

            new Thread(()->{
                try {
                   while (true){
                       String message=dataInputStream.readUTF();
                       if(message.equals("exit")){
                           showMassage("server exit");
                           break;
                       }
                       showMassage( "server > "+message);
                   }

                } catch (IOException e) {
                    throw new RuntimeException(e);
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
