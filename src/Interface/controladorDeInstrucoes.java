package Interface;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import com.sun.xml.internal.ws.org.objectweb.asm.Label;

import Estruturas.Instrucao;

public class controladorDeInstrucoes 
{
	@FXML
    private TableView<Instrucao> tabelaDeInstrucoes;
    @FXML
    private TableColumn<Instrucao, String> ColunaNDaInstrucao;
    @FXML
    private TableColumn<Instrucao, String> colunaInstrucao;
    @FXML
    private TableColumn<Instrucao, String> colunaDestino;
    @FXML
    private TableColumn<Instrucao, String> colunaOperando1;
    @FXML
    private TableColumn<Instrucao, String> colunaOperando2;
    @FXML
    private TableColumn<Instrucao, String> colunaDependentes;
    @FXML
    private TableColumn<Instrucao, String> colunaCicloInicial;
    
    // Reference to the main application.
    private Interface interfacer;

    /**
     * O construtor.
     * O construtor é chamado antes do método inicialize().
     */
    public controladorDeInstrucoes() {
    }

    /**
     * Inicializa a classe controller. Este método é chamado automaticamente
     *  após o arquivo fxml ter sido carregado.
     */
    @FXML
    private void initialize() {
        // Inicializa a tablea de pessoa com duas colunas.
    	ColunaNDaInstrucao.setCellValueFactory(cellData -> cellData.getValue().ColunaNDaInstrucaoProperty());
    	colunaInstrucao.setCellValueFactory(cellData -> cellData.getValue().colunaInstrucaoProperty());
    	colunaDestino.setCellValueFactory(cellData -> cellData.getValue().colunaDestinoProperty());
    	colunaOperando1.setCellValueFactory(cellData -> cellData.getValue().colunaOperando1Property());
    	colunaOperando2.setCellValueFactory(cellData -> cellData.getValue().colunaOperando2Property());
    	colunaDependentes.setCellValueFactory(cellData -> cellData.getValue().colunaDependentesProperty());
    	colunaCicloInicial.setCellValueFactory(cellData -> cellData.getValue().colunaCicloInicialProperty());
    }

    /**
     * É chamado pela aplicação principal para dar uma referência de volta a si mesmo.
     * 
     * @param mainApp
     */
    public void setInterface(Interface interfacer) {
        this.interfacer = interfacer;

        // Adiciona os dados da observable list na tabela
        tabelaDeInstrucoes.setItems(interfacer.getlistaDeInstrucoes());
    }
}
