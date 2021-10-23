package Chat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * Mateus Fernando Diniz
 */

// n�o explicarei em detalhes o cliente, j� que espero que voc� tenha entendido o Server
// a classe ChatClient se parece com a classe de ServerApp.java

public class ChatClient extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
        	Parent root =FXMLLoader.load(getClass().getResource("/chat/chatCliente.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        
        new ClientController(primaryStage);
        
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
