
package Server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * /*
  Created on : 11/04/2021, 17:54:54
    Author     : Mateus Fernando Diniz
 */
 

// declarando classe ServerApp que estende (herda) de Application
public class ServerApp extends Application {
    
	// metodo start obrigatario (@Override) de Application
    @Override
    public void start(Stage primaryStage) {
    	// bloco de instrução
        try {	// no try vão as instruçõeses comuns de Java
            Parent root = FXMLLoader.load(getClass().getResource("chatServerFXML.fxml")); // carrega (load) o template FXML
            Scene scene = new Scene(root);	// instancia (cria) novo objeto scene que contera o template
            primaryStage.setScene(scene);	// define (set) Scene em primaryStage
        } // catch para tratar o erro (error) caso de uma exceção (exception)
        catch(Exception ex) {
            ex.printStackTrace();	// imprime um rastreamento de pilha (printStackTrace)
        }
        
        // instancia (cria) um novo objeto ServerController e passa um Stage (primaryStage)
        new ServerController(primaryStage);
        
        primaryStage.setTitle("Chat Server");	// define o titulo da janela com setTitle
        primaryStage.setResizable(false);	// não maximizar janela com setResizable(false)
        primaryStage.show();	// agora mostre (show) a janela
    }

    /**
     * @param args the command line arguments
     */
    // metodo estatico principal main para executar o aplicativo Java
    public static void main(String[] args) {
        launch(args);	// lance (launch) todos os argumentos (args) e execute o aplicativo Java
    }
    
}

