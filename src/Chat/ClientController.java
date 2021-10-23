package Chat;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

// esse código é bem simples de entender já que voce deve ter entendido o controller do Server (Servidor)

public class ClientController implements Initializable {

	private Stage stage;

	private Connection conn;

	@FXML
	private TextArea txtmsg;
	@FXML
	private Label usuarionome;
	@FXML
	private TextArea txtinput;

	@FXML
	private Button btndesconnect;

	@FXML
	private Button btnconnect;

	@FXML
	private Button btnexit;

	@FXML
	private Button btnsend;

	public ClientController() {

	}

	public ClientController(Stage stage) {
		this.stage = stage;

		this.stage.setTitle("TESTEEEEEEEEEEEEEE" + " esta off-line - Chat Client");

		this.stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			public void handle(WindowEvent event) {
				System.exit(0);
			}
		});
	}

	public void iniciar(String nomeLogin) {

		conn = new Connection(nomeLogin, txtinput, txtmsg);

		usuarionome.setText(nomeLogin);

		txtmsg.setFont(new Font("Sans serif", 16));

		conn.setButtons(btnconnect, btndesconnect, btnsend);

		btndesconnect.setDisable(true);
		btnsend.setDisable(true);

		txtmsg.setEditable(false);
		txtinput.setFont(new Font("Sans serif", 16));
		txtinput.setEditable(false);
	}

	@FXML
	void handleconnection(ActionEvent event) {
		if (event.getSource().equals(btnconnect)) {
			Executor service = Executors.newFixedThreadPool(1);
			service.execute(conn);
		} else if (event.getSource().equals(btndesconnect)) {
			try {
				conn.desconnect();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	@FXML
	void handledexit(ActionEvent event) {
		System.exit(0);
	}

	@FXML
	void handlesend(ActionEvent event) {

		try {
			conn.sendMessage(txtinput.getText());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

}
