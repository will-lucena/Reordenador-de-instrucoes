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
     * O construtor � chamado antes do m�todo inicialize().
     */
    public controladorDeInstrucoes() 
    {
    	
    }

    /**
     * Inicializa a classe controller. Este m�todo � chamado automaticamente
     *  ap�s o arquivo fxml ter sido carregado.
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
     * � chamado pela aplica��o principal para dar uma refer�ncia de volta a si mesmo.
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
    	FileChooser.ExtensionFilter filtro = new FileChooser.ExtensionFilter("grafo files *.txt" , "*.txt");
    	escolherArquivo.getExtensionFilters().add(filtro);
    	File arquivo = escolherArquivo.showOpenDialog(null);
    	if (arquivo != null)
    	{
    		interfaceGrafica.bufferLista.clear();
    		interfaceGrafica.bufferLista = interfaceGrafica.funcoes.lerGrafo(arquivo.getAbsolutePath());
    		if (interfaceGrafica.funcoes.lerGrafo(arquivo.getAbsolutePath()) != null)
    		{
    			interfaceGrafica.path = arquivo.getAbsolutePath();
    			interfaceGrafica.funcoes.mostrarInstrucoes(interfaceGrafica.bufferLista);
        		if (interfaceGrafica.getlistaDeInstrucoes().size() > 0)
        		{
        			interfaceGrafica.getlistaDeInstrucoes().clear();
        		}
        		for (int index = 0; index < interfaceGrafica.bufferLista.size(); index++)
        		{
        			interfaceGrafica.getlistaDeInstrucoes().add(interfaceGrafica.bufferLista.get(index));
        		}
    		}
    		else
        	{
        		interfaceGrafica.alerta.setTitle("ERROR");
        		interfaceGrafica.alerta.setContentText("Falha ao abrir o arquivo");
        		interfaceGrafica.alerta.setHeaderText("Arquivo inv�lido");
        		interfaceGrafica.alerta.showAndWait();
        	}
    	}
    	else
    	{
    		interfaceGrafica.alerta.setTitle("ERROR");
    		interfaceGrafica.alerta.setContentText("Falha ao abrir o arquivo");
    		interfaceGrafica.alerta.setHeaderText("Arquivo inv�lido");
    		interfaceGrafica.alerta.showAndWait();
    	}
    }
    
    @FXML
    private void handleReordenarInstrucoes()
    {
    	if (interfaceGrafica.bufferLista.size() > 0)
    	{
    		interfaceGrafica.bufferLista = interfaceGrafica.funcoes.buscarInstrucoesIndependentes(interfaceGrafica.bufferLista);
    		interfaceGrafica.bufferLista = interfaceGrafica.funcoes.reordenarInstrucoes(interfaceGrafica.bufferLista);
        	interfaceGrafica.getlistaDeInstrucoes().clear();
        	
        	for (int index = 0; index < interfaceGrafica.bufferLista.size(); index++)
    		{
    			interfaceGrafica.getlistaDeInstrucoes().add(interfaceGrafica.bufferLista.get(index));
    		}
    	}
    	else
    	{
    		interfaceGrafica.alerta.setTitle("ERROR");
    		interfaceGrafica.alerta.setContentText("Falha executar instru��o");
    		interfaceGrafica.alerta.setHeaderText("Nenhum grafo selecionado");
    		interfaceGrafica.alerta.showAndWait();
    	}
    }

    @FXML
    private void handleRenomearRegistradores()
    {
    	if (interfaceGrafica.bufferLista.size() > 0)
    	{
    		interfaceGrafica.funcoes.renomearRegistradores(interfaceGrafica.bufferLista);
        	interfaceGrafica.getlistaDeInstrucoes().clear();
        	
        	for (int index = 0; index < interfaceGrafica.bufferLista.size(); index++)
    		{
    			interfaceGrafica.getlistaDeInstrucoes().add(interfaceGrafica.bufferLista.get(index));
    		}
    	}
    	else
    	{
    		interfaceGrafica.alerta.setTitle("ERROR");
    		interfaceGrafica.alerta.setContentText("Falha executar instru��o");
    		interfaceGrafica.alerta.setHeaderText("Nenhum grafo selecionado");
    		interfaceGrafica.alerta.showAndWait();
    	}
    }
    
    @FXML
    private void handleSalvar()
    {
    	File arquivo = new File(interfaceGrafica.path);
    	if (interfaceGrafica.bufferLista.size() > 0)
    	{
    		interfaceGrafica.funcoes.salvarGrafo(interfaceGrafica.bufferLista, arquivo.getAbsolutePath());
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
    		interfaceGrafica.funcoes.salvarGrafo(interfaceGrafica.bufferLista, arquivo.getAbsolutePath());
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
    private void handleReordenarERenomear()
    {
    	if (interfaceGrafica.bufferLista.size() > 0)
    	{
    		interfaceGrafica.bufferLista = interfaceGrafica.funcoes.buscarInstrucoesIndependentes(interfaceGrafica.bufferLista);
    		interfaceGrafica.bufferLista = interfaceGrafica.funcoes.reordenarInstrucoes(interfaceGrafica.bufferLista);
        	interfaceGrafica.funcoes.renomearRegistradores(interfaceGrafica.bufferLista);
        	interfaceGrafica.getlistaDeInstrucoes().clear();
        	
        	for (int index = 0; index < interfaceGrafica.bufferLista.size(); index++)
    		{
    			interfaceGrafica.getlistaDeInstrucoes().add(interfaceGrafica.bufferLista.get(index));
    		}
    	}
    	else
    	{
    		interfaceGrafica.alerta.setTitle("ERROR");
    		interfaceGrafica.alerta.setContentText("Falha executar instru��o");
    		interfaceGrafica.alerta.setHeaderText("Nenhum grafo selecionado");
    		interfaceGrafica.alerta.showAndWait();
    	}
    }
}
