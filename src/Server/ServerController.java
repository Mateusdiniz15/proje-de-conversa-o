package Server;

//importando APIs (modulos) JavaFX
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

//declara classe ServerController que implementa a interface Initializable
public class ServerController implements Initializable {

	// decorador @FXML diz que a variavel vem do template escrito em FXML, portanto tem o decorador @FXML
 @FXML
 private TextArea txtstatus;	// componente txtstatus vem do template FXML do Servidor do chat
 private static ServerSocket server;	// ServerSocket server serve para a comunica��o via Rede pelo Aplicativo Java, �s por onde a comunica��o acontece
 
 private Stage stage;	// stage em Controller?  Este vem do ServerApp.java quando instancia (cria) um novo objeto com o new ServerController();
 
 // este construtor serve como auxlio quando precisamos instanciar um Controller sem um argumento (parametro)
 public ServerController() {
     
 }
 
 // este construtor tem um parametro do tipo Stage que recebera de ServerApp.java
 public ServerController(Stage stage) {
     this.stage = stage;	// stage propriamente dito
     
     // aqui � o momento de houver um close (fechar) na janela do aplicativo Java atraves do bot�o  X fechar da janela
     this.stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
     	// metodo handle obrigatario (@Override)
         
         public void handle(WindowEvent event) {	// este metodo � o manipulador (handle) de eventos
             // bloco try para as instru��es comuns
         	try {
         		// se server diferente (!=) de null (nulo)
                 if(server != null)	// ent�o
                     server.close();	// fechar aplicativo
             }	// catch caso de erro (exception)
             catch(Exception ex) {
                 ex.printStackTrace();	// aqui imprime o rastreamento de pilha (printStackTrace)
             }
             System.exit(0);	// fecha o aplicativo com System.exit(0) - (0) significa sem erro - se fosse (1) seria em caso de erro como System.exit(1)
         }
         
     });
 }
 
 // metodo obrigatario da interface Initializable para auto executar as instru��es com o metodo abaixo initialize()
 
 public void initialize(URL location, ResourceBundle resources) {
 	// define a Thread com o tamanho variado (newCachedThreadPool()) j� que n�o sabemos quantos clientes (Apps) teremos no chat 
 	Executor service = Executors.newCachedThreadPool();
     service.execute(new Connection());	// execute o service (servi�o) com a instancia da classe Connection com o new Connection()
 }
 
 // o metodo connect() � responsavel por esperar alguem se conectar ao chat no client app
 public void connect() throws Exception {	// acrescenta-se um throws Exception para o tratamento de erro obrigatorio
     server = new ServerSocket(12345);	// instancia (cria) um SeverSocket na porta 12345 - voce podera alterar a porta por uma porta disponivel no dispositivo
     txtstatus.setEditable(false);	// faz com que txtstatus (TextArea) do servidor n�o fique editavel com o uso do setEditable(false)
     txtstatus.setFont(new Font("Sans serif", 16));	// define um tipo de fonte para Sans serif tamanho 16
     txtstatus.setWrapText(true);	// faz com que tenha uma quebra de linha setWrapText para true
     txtstatus.appendText("Servidor indexado a porta: " + 12345 + "\r\n");	// adicione um texto "Servidor indexado a porta: 12345"
     
     // loop while true infinito, j� que n�o sabemos por quanto tempo o servidor ficara ativo (rodando)
     while(true) {
         txtstatus.appendText("Aguardando conex�o...\r\n");	// define um texto "Aguardando conex�o..."
         Socket socket = server.accept();	// aqui ouve se alguem se conectara ao servidor (server.accept())
         txtstatus.appendText("Cliente conectado...\r\n\n");	// se for o caso, diz, "Cliente conectado..."
         
         // declare um service como newCachedThreadPool ja que n�o sabemos quantos clientes tera, por isso newCachedThreadPool
         Executor service = Executors.newCachedThreadPool();
         // execute o service (servi�o) com new ServerThread - ServerThread vem de ServerThread.java
         // aqui passa o componente txtstatus para poder ficar visivel durante a execu��o da Thread para manipula-lo quando necessario
         service.execute(new ServerThread(socket, txtstatus));
     }
 }
 
 // classe privada interna chamada de Connection que tambe � uma Thread para efetuar a conex�o chamando o metodo connect() acima
 private class Connection implements Runnable { // implementa a interface Runnable (que � uma Thread)
     
 	// metodo run() obrigatario (@Override)
     
     public void run() {
         try {
             connect();	// conecta?
         }
         catch(Exception ex) {
             ex.printStackTrace();
         }
     }
 }
}
