package org.raflab.studsluzbadesktopclient;

import java.io.IOException;
import java.util.Objects;

import org.raflab.studsluzbadesktopclient.services.NavigationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

@Component
public class MainView {

	private final ContextFXMLLoader appFXMLLoader;
	private Scene scene;
    private final NavigationService navigationService;
    public MainView(ContextFXMLLoader appFXMLLoader, NavigationService navigationService) {
        this.appFXMLLoader = appFXMLLoader;
        this.navigationService = navigationService;
    }

    public Scene createScene() {
	  try {		  
		  FXMLLoader loader = appFXMLLoader.getLoader(MainView.class.getResource("/fxml/main.fxml"));
          BorderPane borderPane = loader.load();
          navigationService.setMainRoot(borderPane);
          this.scene = new Scene(borderPane, 1000, 800);
          navigationService.setupShortcuts(this.scene);
		  scene.getStylesheets().add(Objects.requireNonNull(MainView.class.getResource("/css/stylesheet.css")).toExternalForm());
	  } catch (IOException e) {
		  e.printStackTrace();
	  }
	  return this.scene;
	 }

    public void changeRoot(String fxml) {
        FXMLLoader loader = appFXMLLoader.getLoader(MainView.class.getResource("/fxml/" + fxml + ".fxml"));
        try {
            Parent root = loader.load();
            scene.setRoot(root);

            root.requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Parent loadPane(String fxml) {
        FXMLLoader loader = appFXMLLoader.getLoader(MainView.class.getResource("/fxml/" + fxml + ".fxml"));
        try {
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
	
	public void openModal(String fxml) {
		FXMLLoader loader = appFXMLLoader.getLoader(MainView.class.getResource("/fxml/"+fxml+".fxml"));
		try {
			Parent parent = loader.load();
			Scene scene = new Scene(parent, 400, 300);
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        
	        stage.setScene(scene);
	        stage.showAndWait();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void openModal(String fxml, String title) {
		FXMLLoader loader = appFXMLLoader.getLoader(MainView.class.getResource("/fxml/"+fxml+".fxml"));
		try {
			Parent parent = loader.load();
			Scene scene = new Scene(parent, 400, 300);
	        Stage stage = new Stage();
	        stage.setTitle(title);
	        stage.initModality(Modality.APPLICATION_MODAL);
	        
	        stage.setScene(scene);
	        stage.showAndWait();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void openModal(String fxml, String title,  int weight, int height) {
		FXMLLoader loader = appFXMLLoader.getLoader(MainView.class.getResource("/fxml/"+fxml+".fxml"));
		try {
			Parent parent = loader.load();
			Scene scene = new Scene(parent, weight, height);
	        Stage stage = new Stage();
	        stage.setTitle(title);
	        stage.initModality(Modality.APPLICATION_MODAL);
	        
	        stage.setScene(scene);
	        stage.showAndWait();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
