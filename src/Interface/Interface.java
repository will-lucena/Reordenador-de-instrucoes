package Interface;

import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import Estruturas.Instrucao;
import Funcoes.Funcoes;

public class Interface extends Application {

	private Stage primaryStage;
	private BorderPane painelInicial;
	private ObservableList<Instrucao> listaDeInstrucoes = FXCollections.observableArrayList();
	
	public Interface()
	{
		Funcoes funcoes = new Funcoes();
		
		ArrayList<Instrucao> buffer = funcoes.lerGrafo("src/grafo.txt");
		for (int index = 0; index < buffer.size(); index++)
		{
			listaDeInstrucoes.add(buffer.get(index));
		}
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
            loader.setLocation(Interface.class.getResource("TelaInicial.fxml"));
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
            loader.setLocation(Interface.class.getResource("tabelaDeInstrucoes.fxml"));
            Pane mostrarTabela = (Pane) loader.load();

            // Define o person overview dentro do root layout.
            painelInicial.setCenter(mostrarTabela);
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
