package Login;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import Chat.ChatClient;
import Chat.ClientController;
import gui.Constraints;
import gui.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class Logincontroller implements Initializable {
	@FXML
	private Button bconectar;
	@FXML
	private TextField nomelogin;
	@FXML
	private ComboBox<Person> comboBoxPerson;
	@FXML
	private ObservableList<Person> obsList;

	   @FXML
	    void conecta(ActionEvent event) { // TODO troquei o nome do metodo pois estava com o mesmo nome do objeto

		try {

			((Node) (event.getSource())).getScene().getWindow().hide();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/chat/chatCliente.fxml"));
			Parent root = (Parent) loader.load();
			
			
			ClientController ClienttController = loader.getController();
			ClienttController.iniciar(nomelogin.getText());  // TODO criei este metodo pra passar o usuario logado
			
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle("Chat Client");
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	};

	@FXML
	public void ComboBoxPersonAction() {
		// Person person = comboBoxPerson.getSelectionModel().getSelectedItem();
		// System.out.println(person);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// Constraints.setTextFieldInteger(txthost);
		List<Person> list = new ArrayList<>();
		list.add(new Person("Inspetor"));
		list.add(new Person("Secretaria"));

		obsList = FXCollections.observableArrayList(list);
		comboBoxPerson.setItems(obsList);

		Callback<ListView<Person>, ListCell<Person>> factory = lv -> new ListCell<Person>() {
			@Override
			protected void updateItem(Person item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		comboBoxPerson.setCellFactory(factory);
		comboBoxPerson.setButtonCell(factory.call(null));
	}

}
