package Funcoes;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import Estruturas.Instrucao;
import Estruturas.Registrador;

public class Funcoes 
{
	Scanner ler = new Scanner(System.in);
	int instrucoesIndependentes = 0;
	boolean podeTrocar = true;
	ArrayList<Registrador> conjuntoDeRegistradores = new ArrayList<>();
	
	public ArrayList<Instrucao> reordenar(String caminho)
	{
		ArrayList<Instrucao> buffer = lerGrafo(caminho);
		buffer = buscarInstrucoesIndependentes(buffer);
		buffer = reordenarInstrucoes(buffer);
		buffer = organizarInstrucoes(buffer);
		mostrarInstrucoes(buffer);
		
		return buffer;
	}
	
	/*
	 * Função responsável por criar o banco de registradores
	 * Função interna chamada na leitura do grafo
	 * Recebe uma string que indica o caminho do arquivo .txt que contém o nome dos registradores
	 * Cada linha é um registrador
	 * Preenche a variavel global "Conjunto de Registradores" com o nome dos registradores do .txt
	 * 
	 */
	void criarBancoDeRegistradores(String registradores)
	{
		System.out.println(">>> Inicializando banco de registradores <<<");
		try
		{
			BufferedReader readerBuffer = new BufferedReader(new FileReader(registradores));
			String linha = readerBuffer.readLine();
			linha = readerBuffer.readLine();
			
			while(linha != null)
			{
				Registrador registrador = new Registrador();
				registrador.setNome(linha);
				conjuntoDeRegistradores.add(registrador);
				linha = readerBuffer.readLine();
			}
			readerBuffer.close();
		}
		catch (FileNotFoundException excpetion) 
		{
			System.err.println("Arquivo Invalido");
			System.out.println(">>> A inicialização falhou <<<");
		} catch (IOException excpetion) 
		{
			System.err.println("Erro na leitura");
			excpetion.printStackTrace();
			System.out.println(">>> A inicialização falhou <<<");
		}
		finally
		{
			System.out.println(">>> Inicialização do banco de registradores encerrada <<<");
		}
	}
	
	/*
	 * Função responsável por resetar os registradores para que eles sejam atualizados
	 * Função interna chamada na atualização do banco de registradores
	 * Reseta também o campo "Dependencia Falsa" das instruções para que possam ser atualizadas
	 * Recebe uma lista de instruções
	 * 
	 */
	void resetarRegistradores(ArrayList<Instrucao> instrucoes)
	{
		for (int index = 0; index < conjuntoDeRegistradores.size(); index++)
		{
			conjuntoDeRegistradores.get(index).setEscrito(false);
			conjuntoDeRegistradores.get(index).setLido(false);
		}
		for (int index = 0; index < instrucoes.size(); index++)
		{
			instrucoes.get(index).setDependenciaFalsa(false);
		}
	}
	
	/*
	 * Função responsável por atualizar o banco de registradores
	 * Função interna usada na leitura do grafo e correção de falsas dependencias
	 * Recebe uma lista de instruções a serem atualizadas junto com o banco de registradores
	 * Reseta o estado atual dos registradores e do alerta da lista sobre dependencias falsas
	 * Percorre a lista de instruções e pra cada instrução verifica todo o banco buscando seus registradores
	 * Atualiza os campos "eh escrito" e "eh lido" do registrador e o alerta sobre falsas dependencias da instrução
	 * 
	 */
	void atualizarBancoDeRegistradores(ArrayList<Instrucao> instrucoes)
	{		
		resetarRegistradores(instrucoes);
		for (int index = 0; index < instrucoes.size(); index++)
		{
			for (int indexAux = 0; indexAux < conjuntoDeRegistradores.size(); indexAux++)
			{
				String destinoDaInstrucao = instrucoes.get(index).getDestino().getNome();
				String operando1DaInstrucao = instrucoes.get(index).getOperando1().getNome();
				String operando2DaInstrucao = instrucoes.get(index).getOperando2().getNome();
				String registrador = conjuntoDeRegistradores.get(indexAux).getNome();
				
				if (destinoDaInstrucao.equals(registrador))
				{
					if (conjuntoDeRegistradores.get(indexAux).isEscrito())
					{
						instrucoes.get(index).setDependenciaFalsa(true);
					}
					conjuntoDeRegistradores.get(indexAux).setEscrito(true);
				}
				if (operando1DaInstrucao.equals(registrador) || operando2DaInstrucao.equals(registrador))
				{
					conjuntoDeRegistradores.get(indexAux).setLido(true);
				}
			}
		}
	}
	
	/*
	 * Função responsável por ler o arquivo .txt com o grafo de instruções e criar o ArrayList que servirá como buffer do arquivo
	 * Recebe uma string com o caminho do .txt do grafo
	 * Cria um arrayList para salvar as instruções de forma ordenada
	 * Chama a função de criação de instrução para passando a linha atual do arquivo
	 * Adiciona a instrução criada no arrayList
	 * Retorna o arrayList que servirá como buffer do arquivo durante a execução do programa
	 * 
	 */
	public ArrayList<Instrucao> lerGrafo(String grafo)
	{
		System.out.println(">>> Leitura do grafo iniciada <<<");
		ArrayList<Instrucao> instrucoes = new ArrayList<>();
		try
		{
			/*
			 * FileReader para abrir o arquivo passado
			 * BufferedReader prepara a leitura do arquivo
			 * Dois .readLine seguidos para pular a linha de cabeçalho
			 * Laço para percorrer todas as linhas do arquivo, dado que cada linha é uma instrução
			 * a cada Linha cria uma nova instrução e insere na lista
			 * .close para fechar o FileReader
			 */
			BufferedReader readerBuffer = new BufferedReader(new FileReader(grafo));
			String linha = readerBuffer.readLine();
			linha = readerBuffer.readLine();
			
			while (linha != null)
			{
				instrucoes.add((criarInstrucao(linha)));
				linha = readerBuffer.readLine();
			}
			readerBuffer.close();
			criarBancoDeRegistradores("src/registradores.txt");
			atualizarBancoDeRegistradores(instrucoes);
			return instrucoes;
		}
		catch (IOException exception)
		{
			System.err.println("Erro na leitura do arquivo");
			exception.printStackTrace();
			System.out.println(">>> A leitura Falhou <<<");
			return null;
		}
		finally
		{
			System.out.println(">>> Leitura do grafo encerrada! <<<\n");
		}
	}
	
	/*
	 * Função responsável por criar uma instrução dada uma linha do arquivo de texto
	 * Dada uma linha no seguinte formato
	 * instrucao \t tipo da instrucao \t registrador de destino \t operador1 \t operador2 \t instrucoes dependentes
	 * Sabendo que cada uma das informações sobre a instrução é separada por "\t", busca pelos "\t" pra quebrar a linha
	 * A cada "\t" é salvo no atributo correspondente a informação dada no arquivo
	 * Retorna a instrução lida
	 * 
	 */
	public Instrucao criarInstrucao(String linha)
	{
		int contadorDeTab = 0;
		Instrucao instrucao = new Instrucao();
		for(int index = 0; index < linha.length(); index++)
		{
			char[] line = linha.toCharArray();
			//inst
			if (line[index] != '\t' && contadorDeTab == 0)
				instrucao.setNumeroDaInstrucao(instrucao.getNumeroDaInstrucao() + line[index]);
			else if (line[index] == '\t' && contadorDeTab == 0)
				contadorDeTab++;
			//tipo
			else if (line[index] != '\t' && contadorDeTab == 1)
				instrucao.setOperacao(instrucao.getOperacao() + line[index]);
			else if (line[index] == '\t' && contadorDeTab == 1)
				contadorDeTab++;
			//dest
			else if (line[index] != '\t' && contadorDeTab == 2)
				instrucao.getDestino().setNome(instrucao.getDestino().getNome() + line[index]);
			else if (line[index] == '\t' && contadorDeTab == 2)
				contadorDeTab++;
			//op1
			else if (line[index] != '\t' && contadorDeTab == 3)
				instrucao.getOperando1().setNome(instrucao.getOperando1().getNome() + line[index]);
			else if (line[index] == '\t' && contadorDeTab == 3)
				contadorDeTab++;
			//op2
			else if (line[index] != '\t' && contadorDeTab == 4)
				instrucao.getOperando2().setNome(instrucao.getOperando2().getNome() + line[index]);
			else if (line[index] == '\t' && contadorDeTab == 4)
				contadorDeTab++;
			//inst_dependente
			else if (line[index] != '\t' && contadorDeTab == 5)
				instrucao.setDependentes(instrucao.getDependentes() + line[index]);
			else if (line[index] == '\t' && contadorDeTab == 5)
				contadorDeTab++;
			else if (contadorDeTab > 5)
				break;
		}
		return instrucao;
	}

	/*
	 * Função responsável por gerar um arquivo de texto com base no arrayList usado como buffer de um grafo
	 * Função interna chamada na leitura do grafo
	 * Recebe um arrayList
	 * Cria um buffer do tipo "file writer" para criar o arquivo de texto que será passado como saida
	 * Percorre o arrayList passado na entrada e a cada indice escreve seus atributos na linha do arquivo
	 * Retorna o caminho do arquivo de saida
	 */
	public String salvarGrafo(ArrayList<Instrucao> lista, String localParaArmazenamento)
	{
		System.out.println(">>> Gerando novo grafo <<<");
		
		try 
		{
			PrintWriter escrever = new PrintWriter(new FileWriter(localParaArmazenamento));
			escrever.println("#inst 	tipo	dest 	op1 	op2 	#inst_recebe_resultado	Ciclo_Inicial	Falsa_Dependencia");
			for (int index = 0; index < lista.size(); index++)
			{
				
				escrever.print(lista.get(index).getNumeroDaInstrucao() + "\t");
				escrever.print(lista.get(index).getOperacao() + "\t");
				escrever.print(lista.get(index).getDestino().getNome() + "\t");
				escrever.print(lista.get(index).getOperando1().getNome() + "\t");
				escrever.print(lista.get(index).getOperando2().getNome() + "\t");
				escrever.print(lista.get(index).getDependentes() + "\t");
				escrever.print(lista.get(index).getCicloInicial() + "\t");
				escrever.print(lista.get(index).getDependenciaFalsa() + "\n");
			}
			escrever.close();
			System.out.println("Arquivo gerado com sucesso e salvo em: " + localParaArmazenamento);
			return localParaArmazenamento;
		} 
		catch (IOException exception) 
		{
			System.err.println("Erro na escrita do arquivo");
			exception.printStackTrace();
			System.out.println(">>> A escrita falhou <<<");
			return "";
		}
		finally
		{
			System.out.println(">>> Construção do novo arquivo encerrada! <<<\n");
		}
	}
	
	/*
	 * Função responsável por fazer a busca de instruções completamente independentes
	 * Recebe o arrayList com as instruções
	 * Esta função é o primeiro nivel da reordenação pq só busca por instruções que não tenham nenhum tipo de dependecia
	 * Ela é dividida em dois blocos
	 * Passo1: Busca e insere num arrayList auxiliar as instruções independentes
	 * Passo2: Busca e insere no arrayList auxiliar as intruções restantes
	 * Retorna a lista criada
	 * 
	 */
	ArrayList<Instrucao> buscarInstrucoesIndependentes(ArrayList<Instrucao> listaDeInstrucoes)
	{
		System.out.println(">>> Busca por instruções independentes iniciada <<<");
		boolean indepentente = true;
		ArrayList<Instrucao> bufferDaLista = new ArrayList<>();
		//buscar e inserir instruções independentes
		for (int index = 0; index < listaDeInstrucoes.size(); index++)
		{
			for (int indexAux = 0; indexAux < listaDeInstrucoes.size(); indexAux++)
			{	
				String instrucaoDeDentro = listaDeInstrucoes.get(indexAux).getDependentes();
				String instrucaoDeFora = listaDeInstrucoes.get(index).getNumeroDaInstrucao();
				if (instrucaoDeDentro.contains(instrucaoDeFora))
				{
					indepentente = false;
					break;
				}
			}
			if (indepentente)
			{
				bufferDaLista.add(listaDeInstrucoes.get(index));
				instrucoesIndependentes++;
			}
			indepentente = true;
		}
		//Inserir instruções com dependencias
		for (int index = 0; index < listaDeInstrucoes.size(); index++)
		{
			for (int indexAux = 0; indexAux < bufferDaLista.size(); indexAux++)
			{
				if (listaDeInstrucoes.get(index).getNumeroDaInstrucao() == bufferDaLista.get(indexAux).getNumeroDaInstrucao())
				{
					indepentente = true;
					break;
				}
			}
			if (!indepentente)
			{
				bufferDaLista.add(listaDeInstrucoes.get(index));
			}
			indepentente = false;
			
		}
		System.out.println("Foram encontradas " + instrucoesIndependentes + " instrucoes independentes");
		System.out.println(">>> Busca de instruções independentes encerrada! <<<\n");
		return bufferDaLista;
	}
	
	/*
	 * Função responsável por simular os ciclos de um pipeline com uma lista de instruções
	 * Recebe um arrayList das instruções a serem simuladas
	 * Percorre a lista verificando as dependencias para criar as bolhas necessárias
	 * Inicializa o campo de ciclo inicial das instruções
	 * 
	 */
	void simularCiclos(ArrayList<Instrucao> listaDeInstrucoes)
	{
		for (int index = 0; index < listaDeInstrucoes.size(); index++)
		{
			listaDeInstrucoes.get(index).setCicloInicial(index);
		}
		
		int cicloAtual = 1;
		for (int index = 1; index < listaDeInstrucoes.size(); index++)
		{
			for (int indexAux = index-1; indexAux >= 0; indexAux--)
			{
				String instrucaoDeFora = listaDeInstrucoes.get(index).getNumeroDaInstrucao();
				String instrucaoDeDentro = listaDeInstrucoes.get(indexAux).getDependentes();
				
				if (instrucaoDeFora.contains(instrucaoDeDentro))
				{
					int tempo;
					boolean podeInserir = false;
					while (!podeInserir)
					{
						tempo = cicloAtual - listaDeInstrucoes.get(indexAux).getCicloInicial();
						if (tempo > 2)
						{
							podeInserir = true;
						}
						else
						{
							cicloAtual++;
						}
					}
				}
			}
			listaDeInstrucoes.get(index).setCicloInicial(cicloAtual);
			cicloAtual++;
		}
	}
	
	/*
	 * Função responsável por auxiliar na analise de conflito nas reordenações
	 * Esta função é de uso interno, chamada no reordenamento de instruções
	 * Verifica um pequeno bloco de instruções da lista
	 * Recebe um arrayList com as instruções, um inteiro indicando a posição inicial do "bloco" e outro indicando o final
	 * Percorre esse "bloco" de instruções avaliando se há conflito nelas
	 * Um conflito ocorre quando ao reordenar as intruções uma instrução com dependencia começa antes da sua "fornecedora" terminar
	 * Retorna true, caso a troca da posição das instruções não gere conflito e false caso contrário
	 *
	 */
	boolean buscarConflitoAuxiliar(ArrayList<Instrucao> lista, int comeco)
	{
		//buscar e inserir instruções sem conflito
		int index = comeco;
		for (int indexAux = index-1; indexAux >= 0; indexAux--)
		{	
			String instrucaoReordenada = lista.get(index).getNumeroDaInstrucao();
			String instrucaoAtual = lista.get(indexAux).getDependentes();
			int tempo = lista.get(index).getCicloInicial() - lista.get(indexAux).getCicloInicial();
			if (instrucaoAtual.contains(instrucaoReordenada) && tempo < 3)
			{
				podeTrocar = false;
				return false;
			}
		}
		return true;
	}
	
	/*
	 * Função responsável por trocar a posição de duas instruções
	 * Está função é de uso interno, chamada no reordenamento de instruções
	 * Recebe um arrayList com as intruções e dois inteiros que indicam as posições no arrayList que deverão ser trocadas
	 * Armazena a instrução da posição x em um buffer e troca x com y e em seguida y com o buffer
	 * 
	 */
	void trocarPosicao(ArrayList<Instrucao> lista, int x, int y)
	{
		Instrucao buffer = lista.get(y);
		lista.set(y, lista.get(x));
		lista.set(x, buffer);
	}
	
	/*
	 * Função responsável por clonar uma lista
	 * Está função é de uso interno e faz uma cópia de uma lista passada como entrada
	 * Recebe um arrayList de instruções a ser oopiado
	 * Cria uma lista temporaria que servirá de buffer, percorre a lista passada como parâmetro
	 * Atribuindo cada elemento da lista em uma posição do buffer
	 * Retorna o buffer criado
	 * 
	 */
	ArrayList<Instrucao> clonarLista(ArrayList<Instrucao> listaDeInstrucoes)
	{
		ArrayList<Instrucao> buffer = new ArrayList<Instrucao>();
		
		for (int index = 0; index < listaDeInstrucoes.size(); index++)
		{
			buffer.add(listaDeInstrucoes.get(index));
		}
		
		return buffer;
	}
	
	/*
	 * Função responsável por mostrar uma lista
	 * Está função recebe um arrayList com instruções e imprime seus elementos
	 * Recebe um arrayList
	 * Percorre o arrayList passado como parâmetro e imprime todos os campos de sua instrução, uma por uma
	 * 
	 */
	public void mostrarInstrucoes(ArrayList<Instrucao> listaDeInstrucoes)
	{
		System.out.println(">>> Exibindo grafo <<<");
		System.out.println();
		System.out.println("#inst 	tipo	dest 	op1 	op2 	#inst_recebe_resultado	Ciclo_Inicial"
				+ "	Falsa_Dependencia");
		for (int index = 0; index < listaDeInstrucoes.size(); index++)
		{
			System.out.print(listaDeInstrucoes.get(index).getNumeroDaInstrucao() + "\t");
			System.out.print(listaDeInstrucoes.get(index).getOperacao() + "\t");
			System.out.print(listaDeInstrucoes.get(index).getDestino().getNome() + "\t");
			System.out.print(listaDeInstrucoes.get(index).getOperando1().getNome() + "\t");
			System.out.print(listaDeInstrucoes.get(index).getOperando2().getNome() + "\t\t");
			System.out.print(listaDeInstrucoes.get(index).getDependentes() + "\t\t\t");
			System.out.print(listaDeInstrucoes.get(index).getCicloInicial() + "\t\t");
			System.out.print(listaDeInstrucoes.get(index).getDependenciaFalsa() + "\n");
		}
		System.out.println();
		System.out.println(">>> Exibicao encerrada! <<<\n");
	}
	
	/*
	 * Função responsável por manter a corretude do grafo
	 * Está função é de uso interno e recebe um arrayList de instruções, retornando um booleano
	 * A corretude é mantida se uma instrução y depende da x e a y vem depois da x, para todas as instruções
	 * Percorre o arrayList passado como parâmetro e pra cada posição busca seus dependentes
	 * Se houver algum dependente que venha antes da posição atual, a corretude foi comprometida
	 * Retorna true se o programa está correto, false caso contrário
	 * 
	 */
	boolean analisarCorretude(ArrayList<Instrucao> listaDeInstrucoes)
	{
		for (int index = 0; index < listaDeInstrucoes.size(); index++)
		{
			for (int indexAux = 0; indexAux < listaDeInstrucoes.size(); indexAux++)
			{
				String instrucaoDeFora = listaDeInstrucoes.get(index).getDependentes();
				String instrucaoDeDentro = listaDeInstrucoes.get(indexAux).getNumeroDaInstrucao();
				if (instrucaoDeDentro.contains(instrucaoDeFora) && index < indexAux)
				{
					podeTrocar = false;
					return false;
				}
			}
		}
		return true;
	}	
	
	/*
	 * Função responsável por ordenar as intruções por ordem do ciclo inicial
	 * Recebe um arrayList de instruções e retorna-o ordenado
	 * Percorre todo o arrayList buscando alguma instrução que esteja fora de ordem
	 * A ordenação é mantida pela regra ciclo inicial de x > ciclo inicial de y <=> y -> x
	 * Retorna um arrayList com as intruções ordenadas por ordem crescente do ciclo inicial
	 * 
	 */
	ArrayList<Instrucao> organizarInstrucoes(ArrayList<Instrucao> listaDeInstrucoes)
	{
		for (int index = 0; index < listaDeInstrucoes.size(); index++)
		{
			for (int indexAux = 0; indexAux < listaDeInstrucoes.size(); indexAux++)
			{
				if (listaDeInstrucoes.get(indexAux).getCicloInicial() > listaDeInstrucoes.get(index).getCicloInicial())
				{
					trocarPosicao(listaDeInstrucoes, index, indexAux);
				}
			}
		}
		return listaDeInstrucoes;
	}
	
	/*
	 * Função responsável por fazer o reordenamento reordenar as instruções
	 * Percorre todo o arrayList e testa instruções que podem ser reordenadas e pra qual nova posição ela pode ir
	 * Recebe um arrayList de instruções
	 * Cria um arrayList que servirá de buffer para reordenamento e começa o percorrimento pela ultima instrução
	 * o percorrimento é feito do fim para o começo do vetor, verificando se a instrução atual pode ser trocada
	 * pela anterior, sempre verificando a corretude, quando chegar em um ponto que a troca da posição não pode ser
	 * feita, a instrução é realocada pra ultima posição que pode ser inserida
	 * Se o reordenamento for produtivo (total de ciclos menor), retorna o arrayList usado como buffer
	 * caso contrario, retorna a lista do jeito que foi passada
	 * 
	 */
	ArrayList<Instrucao> reordenarInstrucoes(ArrayList<Instrucao> listaDeInstrucoes)
	{
		System.out.println(">>> Reordenamento de instrucoes iniciado <<<");
		ArrayList<Instrucao> buffer = clonarLista(listaDeInstrucoes);
		int totalDeCiclosAntigo = listaDeInstrucoes.get(listaDeInstrucoes.size()-1).getCicloInicial() + 4;
		int instrucoesReordenadas = 0;
		
		for (int index = listaDeInstrucoes.size()-1 ; index >= instrucoesIndependentes; index--)
		{
			String instrucaoAntesDoOrdenamento = listaDeInstrucoes.get(index).getNumeroDaInstrucao();
			for (int indexAux = index-1; indexAux >= instrucoesIndependentes; indexAux--)
			{	
				String dependentes = listaDeInstrucoes.get(indexAux).getDependentes();
				String instrucaoDeFora = listaDeInstrucoes.get(index).getNumeroDaInstrucao();
				if (dependentes.contains(instrucaoDeFora))
				{
					podeTrocar = false;
					break;
				}
				else
				{
					int posicaoAtualDaInstrucao = indexAux+1;
					trocarPosicao(buffer, posicaoAtualDaInstrucao, posicaoAtualDaInstrucao-1);
					simularCiclos(buffer);
					while(buscarConflitoAuxiliar(buffer, posicaoAtualDaInstrucao-1) == true && posicaoAtualDaInstrucao > 1)
					{
						if (!analisarCorretude(buffer) || !corrigirFalsasDependecias(buffer))
						{
							podeTrocar = false;
							break;
						}
						posicaoAtualDaInstrucao--;
						trocarPosicao(buffer, posicaoAtualDaInstrucao-1, posicaoAtualDaInstrucao);
						simularCiclos(buffer);
					}
					if (!podeTrocar)
						trocarPosicao(buffer, posicaoAtualDaInstrucao, posicaoAtualDaInstrucao-1);
				}
			}
			String instrucaoDepoisDoOrdenamento = buffer.get(index).getNumeroDaInstrucao();
			if (instrucaoAntesDoOrdenamento != instrucaoDepoisDoOrdenamento)
				instrucoesReordenadas++;
			podeTrocar = true;
		}		
		simularCiclos(buffer);
		
		System.out.println("Foram reordenada(s) " + instrucoesReordenadas + " instruções");
		System.out.println(">>> Reordenamento encerrado! <<<\n");
		if (buffer.get(buffer.size()-1).getCicloInicial() + 4 < totalDeCiclosAntigo)
			return buffer;
		return listaDeInstrucoes;
	}

	/*
	 * Função responsável por buscar e corrigir as falsas dependencias
	 * Está é uma função interna, usada em conjunto com a analise de corretude no reordenamento
	 * Recebe um arrayList de instruções e corrige as falsas dependencias se houver recursos necessários
	 * Recebe um arrayList de instruções e retorna um booleano
	 * Primeiro armazena a posicao das instruções que possuem falsas dependencias em um arrayList de quarentena
	 * após saber quais instruções possuem falsas dependencias, percorre a lista afim de trocar o registrador da 
	 * da instrução em quarentena, ao encontra-la busca um registrador vago no banco de registradores
	 * se não houver, não é possivel corrigir todas falsas dependencias, retorna false indicando o ocorrido
	 * caso exista, busca as instruções que dependem desta e atualiza seus registradores
	 * Por final atualiza o banco de registradores para salvar as modificações e retorna true
	 * 
	 */
	boolean corrigirFalsasDependecias(ArrayList<Instrucao> listaDeInstrucoes)
	{
		ArrayList<Integer> instrucoesEmQuarentena = new ArrayList<>();
		for (int index = 0; index< listaDeInstrucoes.size(); index++)
		{
			if(listaDeInstrucoes.get(index).getDependenciaFalsa())
			{
				instrucoesEmQuarentena.add(index);
			}
		}
		
		for(int index = 0; index < instrucoesEmQuarentena.size(); index++)
		{
			for (int indexAux = 0; indexAux < listaDeInstrucoes.size(); indexAux++)
			{
				Instrucao instrucaoEmQuarentena = listaDeInstrucoes.get(instrucoesEmQuarentena.get(index));
				if(instrucaoEmQuarentena.getDestino().getNome().equals(listaDeInstrucoes.get(indexAux).getDestino().getNome()) && 
						instrucoesEmQuarentena.get(index) >= indexAux)
				{
					Registrador registradorAntigo = instrucaoEmQuarentena.getDestino();
					instrucaoEmQuarentena.setDestino(buscarNovoRegistrador());
					if (instrucaoEmQuarentena.getDestino() == null)
					{
						instrucaoEmQuarentena.setDestino(registradorAntigo);
						return false;
					}
					else
					{
						for (int posicao = indexAux; posicao < listaDeInstrucoes.size(); posicao++)
						{
							String dependentes = instrucaoEmQuarentena.getDependentes();
							String instrucaoAtual = listaDeInstrucoes.get(posicao).getNumeroDaInstrucao();
							if (dependentes.contains(instrucaoAtual))
							{
								if (listaDeInstrucoes.get(posicao).getOperando1().getNome().contains(registradorAntigo.getNome()))
								{
									listaDeInstrucoes.get(posicao).setOperando1(instrucaoEmQuarentena.getDestino());
								}
								if (listaDeInstrucoes.get(posicao).getOperando2().getNome().contains(registradorAntigo.getNome()))
								{
									listaDeInstrucoes.get(posicao).setOperando2(instrucaoEmQuarentena.getDestino());
								}
							}
						}
					}
					atualizarBancoDeRegistradores(listaDeInstrucoes);
				}
			}
		}
		return true;
	}

	/*
	 * Função responsável por buscar um registrador vago no banco de registradores
	 * Está função é interna, chamada na correção de falsas dependencias para pegar um novo registrador
	 * Percorre o banco de registradores buscando um não usado e o retorna
	 * Caso não exista, retorna null
	 * 
	 */
	Registrador buscarNovoRegistrador()
	{
		for(int index = 0; index < conjuntoDeRegistradores.size(); index++)
		{
			if (!conjuntoDeRegistradores.get(index).isEscrito() && !conjuntoDeRegistradores.get(index).isLido())
			{
				return conjuntoDeRegistradores.get(index);
			}
		}
		return null;
	}
}