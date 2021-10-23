package Server;
/*
  Created on : 11/04/2021, 17:54:54
    Author     : Mateus Fernando Diniz
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.scene.control.TextArea;



// declarar classe ServerThread que implementa a interface Runnable ao qual a classe sera uma Thread
public class ServerThread implements Runnable {
	// declara um ArrayList para armazenar valores durante a conversa
    private static ArrayList<BufferedWriter> clients = new ArrayList<BufferedWriter>(); 
    private Socket socket;	// socket para a conexão e comunicação cliente/servidor
    
    // os Streams para a comunicação e  envio de textos
    private InputStream in;
    private InputStreamReader inr;
    private BufferedReader bfr;
    private BufferedWriter bfw;
    
    private TextArea status;	// area de status (estado) que servir como uma area do template FXML do servidor na Thread
    private String name;	// nome do usuario no chat
    
    // index (indice) responsavel pelo controle de desligamento inesperado do cliente ao servidor e tambem para o desligamento normal quando concedido pelo usuario
    private int index = -1;	// serve como meio de manter a conexão estavel durante uma desconexão normal ou no do cliente ao servidor
    
    // construtor ServerThread que contem como parametros um socket e textarea (status)
    public ServerThread(Socket socket, TextArea status) {
        this.status = status;
        this.socket = socket;
        
        try {
            in = socket.getInputStream();	// obtem Streams da conexão cliente/servidor de socket.getInputStream() para leitura
            inr = new InputStreamReader(in);	// atribui in ao new InputStreamReader(in)
            bfr = new BufferedReader(inr);	// atribui inr ao new BufferedReader(inr)
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }
    
    // metodo run() obrigatario (@Override) de Runnable
    
    public void run() {
    	// declara uma msg como null responsavel pela mensagem que dira ao aplicativo  servidor como se proceder quando o cliente se desconecta repentinamente
        String msg = null;
        
        try {
            OutputStream out = socket.getOutputStream(); // obtem de socket (conexão do cliente ao server) uns dados Streams de getOutputStream() para a escrita
            Writer ouw = new OutputStreamWriter(out);	// atribui out ao new OutputStreamWriter(out)
            bfw = new BufferedWriter(ouw);	// atribui ouw ao new BufferedWriter(ouw)
            clients.add(bfw);	// adiciona (add) ao ArrayList clients
            name = bfr.readLine();	// faz uma leitura de linha readLine()
            
            // loop while true infinito pois não se sabe até quando estara ouvindo do cliente os envios de dados via chat cliente/servidor
            while(true) {
                sendToAll(bfw);	// envia para todos os clientes sendToAll() exceto para o cliente atual
            }
        }	// caso de dado um erro de conexão inesperada de cliente ao servidor, por exemplo: acabou a energia eletrica na casa e desligou o dispositivo
        catch(SocketException ex) {	// trate em catch o SocketException de um desligamento repentino ao servidor
            ++index;	// incrementa um indice (index) até chegar ao indice correto do cliente que se desliga-ra
            removeClient(index, "failed server connection...");	// remove um cliente inativo da lista para não dar bug de erro no servidor
            new ReturnFailedConnection();	// faz retornar ao funcionamento correto ao servidor
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }
    
    // metodo responsavel em remover um cliente inativo para o servidor não parar de funcionar
    public void removeClient(int index, String msg) {	// index e msg como parametros
        // essas mensagens nos ifs são para um tipo de desligamento em particular
    	if(msg.equals(name + " saiu do chat (off-line)..."))
            clients.remove(index);
        if(msg.equals(name + " se desconectou..."))
            clients.remove(index);
        if(msg.equals("failed server connection...")) {	// esse ultimo é para um desligamento repentino do cliente ao server
            clients.remove(index);
        }
    }
    
    // metodo responsavel por enviar os dados do chat para todos os clientes, exceto para o atual
    public void sendToAll(BufferedWriter bfw) throws IOException {
        String msg = null;	// mensagem que representa a comunicação do cliente/servidor
        
        // se tem mensagem (ready()) a ler
        if(bfr.ready()) {	// então
            msg = bfr.readLine();	// lÊ uma linha de mensagem (readLine())
            
            // percorre todos os clientes do ArrayList por onde os dados estão armazenados
            for(BufferedWriter bf : clients) {
            	// se diferente do cliente atual
            	if(bfw != bf) {	// então
                    bf.write(msg + "\r\n");	// escreve para os demais clientes
                    bf.flush();	// fora o envio (flush())
                    ++index;	// incrementa o indice (index) para saber a posição do cliente no ArrayList (clients) para o uso no metodo removeClient
                }
                else {	// senão se for o cliente atual
                    ++index; //	incrementa o indice para dar continuidade
                    // remove se for o caso o cliente para os demais casos que não seja o desligamento inesperado como "failed server connection..."
                    removeClient(index, msg);
                }
            }
            
            status.appendText(msg + "\r\n");	// adicione mensagem msg ao status do servidor
        }
        
        index = -1; // reseta index para a posição inicial -1
    }
    
    // classe privada interna responsavel para dar continuidade ao funcionamento normal do servidor 
    private class ReturnFailedConnection {
        public ReturnFailedConnection() {
            try {
            	// volta a ser um loop while true infinito?
                while(true) {
                    sendToAll(bfw);	// volta a enviar para todos?
                }
            }
            catch(IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}

