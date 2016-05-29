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
    	FileChooser escolherArquivo = new FileChooser();
    	escolherArquivo.setTitle("Open Resource File");
    	File arquivo = escolherArquivo.showOpenDialog(null);
    	if (arquivo != null)
    	{
    		interfaceGrafica.funcoes.bufferDeInstrucoes.clear();
    		interfaceGrafica.path = arquivo.getAbsolutePath();
    		interfaceGrafica.funcoes.lerGrafo(arquivo.getAbsolutePath());
    		interfaceGrafica.funcoes.mostrarInstrucoes();
    		if (interfaceGrafica.getlistaDeInstrucoes().size() > 0)
    		{
    			interfaceGrafica.getlistaDeInstrucoes().clear();
    		}
    		for (int index = 0; index < interfaceGrafica.funcoes.bufferDeInstrucoes.size(); index++)
    		{
    			interfaceGrafica.getlistaDeInstrucoes().add(interfaceGrafica.funcoes.bufferDeInstrucoes.get(index));
    		}
    	}
    	else
    	{
    		interfaceGrafica.alerta.setTitle("ERROR");
    		interfaceGrafica.alerta.setContentText("Falha ao abrir o arquivo");
    		interfaceGrafica.alerta.setHeaderText("Arquivo inválido");
    		interfaceGrafica.alerta.showAndWait();
    	}
    }
    
    @FXML
    private void handleReordenarInstrucoes()
    {
    	if (interfaceGrafica.funcoes.bufferDeInstrucoes.size() > 0)
    	{
    		interfaceGrafica.funcoes.buscarInstrucoesIndependentes();
        	interfaceGrafica.funcoes.reordenarInstrucoes();
        	interfaceGrafica.getlistaDeInstrucoes().clear();
        	
        	for (int index = 0; index < interfaceGrafica.funcoes.bufferDeInstrucoes.size(); index++)
    		{
    			interfaceGrafica.getlistaDeInstrucoes().add(interfaceGrafica.funcoes.bufferDeInstrucoes.get(index));
    		}
    	}
    	else
    	{
    		interfaceGrafica.alerta.setTitle("ERROR");
    		interfaceGrafica.alerta.setContentText("Falha executar instrução");
    		interfaceGrafica.alerta.setHeaderText("Nenhum grafo selecionado");
    		interfaceGrafica.alerta.showAndWait();
    	}
    }

    @FXML
    private void handleRenomearRegistradores()
    {
    	if (interfaceGrafica.funcoes.bufferDeInstrucoes.size() > 0)
    	{
    		interfaceGrafica.funcoes.corrigirFalsasDependecias();
        	interfaceGrafica.getlistaDeInstrucoes().clear();
        	
        	for (int index = 0; index < interfaceGrafica.funcoes.bufferDeInstrucoes.size(); index++)
    		{
    			interfaceGrafica.getlistaDeInstrucoes().add(interfaceGrafica.funcoes.bufferDeInstrucoes.get(index));
    		}
    	}
    	else
    	{
    		interfaceGrafica.alerta.setTitle("ERROR");
    		interfaceGrafica.alerta.setContentText("Falha executar instrução");
    		interfaceGrafica.alerta.setHeaderText("Nenhum grafo selecionado");
    		interfaceGrafica.alerta.showAndWait();
    	}
    }
    
    @FXML
    private void handleSalvar()
    {
    	File arquivo = new File(interfaceGrafica.path);
    	if (interfaceGrafica.funcoes.bufferDeInstrucoes.size() > 0)
    	{
    		interfaceGrafica.funcoes.salvarGrafo(arquivo.getAbsolutePath());
    		interfaceGrafica.path = arquivo.getAbsolutePath();
    		interfaceGrafica.notificacao.setTitle("Notication");
    		interfaceGrafica.notificacao.setContentText("Arquivo salvo em: " + arquivo.getAbsolutePath());
    		interfaceGrafica.notificacao.setHeaderText("Arquivo salvo com sucesso");
    		interfaceGrafica.notificacao.showAndWait();
    	}
    }
    
    @FXML
    private void handleSalvarComo()
    {
    	FileChooser escolherArquivo = new FileChooser();
    	escolherArquivo.setTitle("Salvar em...");
    	File arquivo = escolherArquivo.showSaveDialog(null);	
    	if (arquivo != null)
    	{
    		interfaceGrafica.funcoes.salvarGrafo(arquivo.getAbsolutePath());
    		interfaceGrafica.path = arquivo.getAbsolutePath();
    		interfaceGrafica.notificacao.setTitle("Notication");
    		interfaceGrafica.notificacao.setContentText("Arquivo salvo em: " + arquivo.getAbsolutePath());
    		interfaceGrafica.notificacao.setHeaderText("Arquivo salvo com sucesso");
    		interfaceGrafica.notificacao.showAndWait();
    	}
    }
    
    @FXML
    private void handleSobre()
    {
    	
    }
    
    @FXML
    private void handleReordenar()
    {
    	if (interfaceGrafica.funcoes.bufferDeInstrucoes.size() > 0)
    	{
    		interfaceGrafica.funcoes.buscarInstrucoesIndependentes();
        	interfaceGrafica.funcoes.reordenarInstrucoes();
        	interfaceGrafica.getlistaDeInstrucoes().clear();
        	
        	for (int index = 0; index < interfaceGrafica.funcoes.bufferDeInstrucoes.size(); index++)
    		{
    			interfaceGrafica.getlistaDeInstrucoes().add(interfaceGrafica.funcoes.bufferDeInstrucoes.get(index));
    		}
    	}
    	else
    	{
    		interfaceGrafica.alerta.setTitle("ERROR");
    		interfaceGrafica.alerta.setContentText("Falha executar instrução");
    		interfaceGrafica.alerta.setHeaderText("Nenhum grafo selecionado");
    		interfaceGrafica.alerta.showAndWait();
    	}
    }
}
