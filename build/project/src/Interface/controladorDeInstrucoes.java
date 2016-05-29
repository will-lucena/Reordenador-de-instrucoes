package Interface;

import java.io.File;
import Estruturas.Instrucao;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;

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
    private TableColumn<Instrucao, Integer> colunaCicloInicial;
    
    // Reference to the main application.
    private InterfaceGrafica interfaceGrafica;

    /**
     * O construtor.
     * O construtor é chamado antes do método inicialize().
     */
    public controladorDeInstrucoes() 
    {
    	
    }

    /**
     * Inicializa a classe controller. Este método é chamado automaticamente
     *  após o arquivo fxml ter sido carregado.
     */
    @FXML
    private void initialize() {
        // Inicializa a tablea de pessoa com duas colunas.
    	ColunaNDaInstrucao.setCellValueFactory(cellData -> cellData.getValue().numeroDaInstrucaoProperty());
    	colunaInstrucao.setCellValueFactory(cellData -> cellData.getValue().operacaoProperty());
    	colunaDestino.setCellValueFactory(cellData -> cellData.getValue().destinoProperty().get().nomeProperty());
    	colunaOperando1.setCellValueFactory(cellData -> cellData.getValue().operando1Property().get().nomeProperty());
    	colunaOperando2.setCellValueFactory(cellData -> cellData.getValue().operando2Property().get().nomeProperty());
    	colunaDependentes.setCellValueFactory(cellData -> cellData.getValue().dependentesProperty());
    	colunaCicloInicial.setCellValueFactory(cellData -> cellData.getValue().cicloInicialProperty().asObject());
    }

    /**
     * É chamado pela aplicação principal para dar uma referência de volta a si mesmo.
     * 
     * @param mainApp
     */
    public void setInterface(InterfaceGrafica interfaceGrafica) {
        this.interfaceGrafica = interfaceGrafica;

        // Adiciona os dados da observable list na tabela
        tabelaDeInstrucoes.setItems(interfaceGrafica.getlistaDeInstrucoes());
    }
    
    @FXML
    private void handleAbrir() 
    {
    	//ObservableList<Instrucao> listaDeInstrucoes = FXCollections.observableArrayList();
    	FileChooser escolherArquivo = new FileChooser();
    	escolherArquivo.setTitle("Open Resource File");
    	File arquivo = escolherArquivo.showOpenDialog(null);
    	if (arquivo != null)
    	{
    		interfaceGrafica.path = arquivo.getAbsolutePath();
    		interfaceGrafica.buffer = interfaceGrafica.funcoes.lerGrafo(arquivo.getAbsolutePath());
    		interfaceGrafica.funcoes.mostrarInstrucoes(interfaceGrafica.buffer);
    		for (int index = 0; index < interfaceGrafica.buffer.size(); index++)
    		{
    			interfaceGrafica.getlistaDeInstrucoes().add(interfaceGrafica.buffer.get(index));
    		}
    	}
    }
    
    @FXML
    private void handleReordenarInstrucoes()
    {
    	interfaceGrafica.buffer = interfaceGrafica.funcoes.buscarInstrucoesIndependentes(interfaceGrafica.buffer);
    	interfaceGrafica.buffer = interfaceGrafica.funcoes.reordenarInstrucoes(interfaceGrafica.buffer);
    	interfaceGrafica.getlistaDeInstrucoes().clear();
    	
    	for (int index = 0; index < interfaceGrafica.buffer.size(); index++)
		{
			interfaceGrafica.getlistaDeInstrucoes().add(interfaceGrafica.buffer.get(index));
		}
    }

    @FXML
    private void handleRenomearRegistradores()
    {
    	interfaceGrafica.funcoes.corrigirFalsasDependecias(interfaceGrafica.buffer);
    	interfaceGrafica.getlistaDeInstrucoes().clear();
    	
    	for (int index = 0; index < interfaceGrafica.buffer.size(); index++)
		{
			interfaceGrafica.getlistaDeInstrucoes().add(interfaceGrafica.buffer.get(index));
		}
    }
    
    @FXML
    private void handleSalvar()
    {
    	File arquivo = new File(interfaceGrafica.path);
    	interfaceGrafica.funcoes.salvarGrafo(interfaceGrafica.buffer, arquivo.getAbsolutePath());
    }
    
    @FXML
    private void handleSalvarComo()
    {
    	FileChooser escolherArquivo = new FileChooser();
    	escolherArquivo.setTitle("Salvar em...");
    	File arquivo = escolherArquivo.showSaveDialog(null);
    	interfaceGrafica.funcoes.salvarGrafo(interfaceGrafica.buffer, arquivo.getAbsolutePath());
    }
    
    @FXML
    private void handleSobre()
    {
    	
    }
    
    @FXML
    private void handleReordenar()
    {
    	interfaceGrafica.buffer = interfaceGrafica.funcoes.buscarInstrucoesIndependentes(interfaceGrafica.buffer);
    	interfaceGrafica.buffer = interfaceGrafica.funcoes.reordenarInstrucoes(interfaceGrafica.buffer);
    	interfaceGrafica.getlistaDeInstrucoes().clear();
    	
    	for (int index = 0; index < interfaceGrafica.buffer.size(); index++)
		{
			interfaceGrafica.getlistaDeInstrucoes().add(interfaceGrafica.buffer.get(index));
		}
    }
}
