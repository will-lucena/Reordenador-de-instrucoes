package Interface;

import java.io.IOException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import Estruturas.Instrucao;
import Funcoes.Funcoes;

public class InterfaceGrafica extends Application {

	private Stage primaryStage;
	private BorderPane painelInicial;
	private ObservableList<Instrucao> listaDeInstrucoes = FXCollections.observableArrayList();
	public Funcoes funcoes = new Funcoes();
	String path = "";
	Alert alerta = new Alert(AlertType.WARNING);
	Alert notificacao = new Alert(AlertType.INFORMATION);
	
	public InterfaceGrafica()
	{		
		
	}
	
	public ObservableList<Instrucao> getlistaDeInstrucoes() 
	{
        return listaDeInstrucoes;
    }
	
	@Override
	public void start(Stage primaryStage) 
	{
		this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Reordenador de instruções");
        initTelaInicial();

        mostrarTabela();
	}
	
	public void initTelaInicial() 
	{
        try 
        {
            // Carrega o root layout do arquivo fxml.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(InterfaceGrafica.class.getResource("TelaInicial.fxml"));
            painelInicial = (BorderPane) loader.load();

            // Mostra a scene (cena) contendo oroot layout.
            Scene scene = new Scene(painelInicial);
            primaryStage.setScene(scene);
            primaryStage.show();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
	
	public void mostrarTabela() 
	{
        try 
        {
            // Carrega o person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(InterfaceGrafica.class.getResource("tabelaDeInstrucoes.fxml"));
            Pane mostrarTabela = (Pane) loader.load();
            
            
            // Define o person overview dentro do root layout.
            painelInicial.setCenter(mostrarTabela);
            
         // Dá ao controlador acesso à the main app.
            controladorDeInstrucoes controller = loader.getController();
            controller.setInterface(this);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

	public static void main(String[] args) 
	{
		launch(args);
	}
}
