package Chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import javafx.scene.control.TextArea;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/*
Created on : 11/04/2021, 17:54:54
  Author     : Mateus Fernando Diniz
*/
 
public class Connection implements Runnable {
    private Socket socket;
    
    private OutputStream out;
    private Writer ouw;
    private BufferedWriter bfw;
    
    
    private String nome;
    private TextArea message, input;
    
    private Button conn, desconn, send;
    
    public Connection(String nome, TextArea input, TextArea msg) {
        this.nome = nome;
        this.input = input;
        message = msg;
    }
    
   
    public void run() {
        try {
            connection();
            toListen();
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }
    
    // obtem conexao do socket que sera usado no controller ClientController.java
    public Socket getSocket() {
        if(socket != null)
            return socket;
        return null;
    }
    
    // metodo responsavel pela conexao
    public void connection() throws IOException {
        socket = new Socket("127.0.0.1", 12345);
        
        out = socket.getOutputStream();
        ouw = new OutputStreamWriter(out);
        bfw = new BufferedWriter(ouw);
        bfw.write(nome + "\r\n");
        bfw.flush();
        bfw.write(nome + " esta on-line...\r\n\n");
        bfw.flush();
        message.appendText("Voce esta conectado (on-line)...\r\n\n");
        
        input.setEditable(true);
        conn.setDisable(true);
        send.setDisable(false);
        desconn.setDisable(false);
    }
    
    // metodo responsavel pela desconexao
    public void desconnect() throws IOException {
        if(socket != null) {
            bfw.write(nome + " se desconectou...\r\n");
            bfw.flush();
            out.close();
            ouw.close();
            bfw.close();
            socket.close();
            
            input.setEditable(false);
            conn.setDisable(false);
            send.setDisable(true);
            desconn.setDisable(true);
        }
    }
    
    // método responsavel por enviar mensagem
    public void sendMessage(String msg) throws IOException {
        if(socket != null && !msg.equals("")) {
            bfw.write(nome + " -> " + msg + "\r\n");
            bfw.flush();
            message.appendText(nome + " diz -> " + msg + "\r\n");
            input.clear();
        }
    }
    
    // metodo responsavel por ouvir as mensagens de outros clientes conectados ao servidor
    public void toListen() throws IOException {
        InputStream in = socket.getInputStream();
        InputStreamReader inr = new InputStreamReader(in);
        BufferedReader bfr = new BufferedReader(inr);
        String msg = null;
        
        while(true) {
            if(bfr.ready()) {
                msg = bfr.readLine();
                message.appendText(msg + "\r\n");
            }
        }
    }
    
    // metodo responsavel por sair do chat
    public void exit() throws IOException {
        if(socket != null) {
            bfw.write(nome + " saiu do chat (off-line)...\r\n");
            bfw.flush();
            socket.close();
        }
        
        System.exit(0);
    }
    
    // metodo para configurar (set) os botoes no controller ClientController.java
    public void setButtons(Button conn, Button desconn, Button send) {
        this.conn = conn;
        this.desconn = desconn;
        this.send = send;
    }
}
