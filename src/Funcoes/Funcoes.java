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
	
	public String reordenar(String caminho, String saida)
	{
		ArrayList<Instrucao> buffer = lerGrafo(caminho);
		buffer = buscarInstrucoesIndependentes(buffer);
		buffer = reordenarInstrucoes(buffer);
		buffer = organizarInstrucoes(buffer);
		salvarGrafo(buffer, saida);
		mostrarInstrucoes(buffer);
		return saida;
	}
	
	/*
	 * Fun��o respons�vel por criar o banco de registradores
	 * Fun��o interna chamada na leitura do grafo
	 * Recebe uma string que indica o caminho do arquivo .txt que cont�m o nome dos registradores
	 * Cada linha � um registrador
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
				registrador.nome = linha;
				conjuntoDeRegistradores.add(registrador);
				linha = readerBuffer.readLine();
			}
			readerBuffer.close();
		}
		catch (FileNotFoundException excpetion) 
		{
			System.err.println("Arquivo Invalido");
			System.out.println(">>> A inicializa��o falhou <<<");
		} catch (IOException excpetion) 
		{
			System.err.println("Erro na leitura");
			excpetion.printStackTrace();
			System.out.println(">>> A inicializa��o falhou <<<");
		}
		finally
		{
			System.out.println(">>> Inicializa��o do banco de registradores encerrada <<<");
		}
	}
	
	/*
	 * Fun��o respons�vel por resetar os registradores para que eles sejam atualizados
	 * Fun��o interna chamada na atualiza��o do banco de registradores
	 * Reseta tamb�m o campo "Dependencia Falsa" das instru��es para que possam ser atualizadas
	 * Recebe uma lista de instru��es
	 * 
	 */
	void resetarRegistradores(ArrayList<Instrucao> instrucoes)
	{
		for (int index = 0; index < conjuntoDeRegistradores.size(); index++)
		{
			conjuntoDeRegistradores.get(index).ehEscrito = false;
			conjuntoDeRegistradores.get(index).ehLido = false;
		}
		for (int index = 0; index < instrucoes.size(); index++)
		{
			instrucoes.get(index).dependenciaFalsa = false;
		}
	}
	
	/*
	 * Fun��o respons�vel por atualizar o banco de registradores
	 * Fun��o interna usada na leitura do grafo e corre��o de falsas dependencias
	 * Recebe uma lista de instru��es a serem atualizadas junto com o banco de registradores
	 * Reseta o estado atual dos registradores e do alerta da lista sobre dependencias falsas
	 * Percorre a lista de instru��es e pra cada instru��o verifica todo o banco buscando seus registradores
	 * Atualiza os campos "eh escrito" e "eh lido" do registrador e o alerta sobre falsas dependencias da instru��o
	 * 
	 */
	void atualizarBancoDeRegistradores(ArrayList<Instrucao> instrucoes)
	{		
		resetarRegistradores(instrucoes);
		for (int index = 0; index < instrucoes.size(); index++)
		{
			for (int indexAux = 0; indexAux < conjuntoDeRegistradores.size(); indexAux++)
			{
				String s1 = instrucoes.get(index).destino.nome;
				String s2 = instrucoes.get(index).operando1.nome;
				String s3 = instrucoes.get(index).operando2.nome;
				String s4 = conjuntoDeRegistradores.get(indexAux).nome;
				
				if (s1.equals(s4))
				{
					if (conjuntoDeRegistradores.get(indexAux).ehEscrito)
					{
						instrucoes.get(index).dependenciaFalsa = true;
					}
					conjuntoDeRegistradores.get(indexAux).ehEscrito = true;
				}
				if (s2.equals(s4) || s3.equals(s4))
				{
					conjuntoDeRegistradores.get(indexAux).ehLido = true;
				}
			}
		}
	}
	
	/*
	 * Fun��o respons�vel por ler o arquivo .txt com o grafo de instru��es e criar o ArrayList que servir� como buffer do arquivo
	 * Recebe uma string com o caminho do .txt do grafo
	 * Cria um arrayList para salvar as instru��es de forma ordenada
	 * Chama a fun��o de cria��o de instru��o para passando a linha atual do arquivo
	 * Adiciona a instru��o criada no arrayList
	 * Retorna o arrayList que servir� como buffer do arquivo durante a execu��o do programa
	 * 
	 */
	ArrayList<Instrucao> lerGrafo(String grafo)
	{
		System.out.println(">>> Leitura do grafo iniciada <<<");
		ArrayList<Instrucao> instrucoes = new ArrayList<>();
		try
		{
			/*
			 * FileReader para abrir o arquivo passado
			 * BufferedReader prepara a leitura do arquivo
			 * Dois .readLine seguidos para pular a linha de cabe�alho
			 * La�o para percorrer todas as linhas do arquivo, dado que cada linha � uma instru��o
			 * a cada Linha cria uma nova instru��o e insere na lista
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
	 * Fun��o respons�vel por criar uma instru��o dada uma linha do arquivo de texto
	 * Dada uma linha no seguinte formato
	 * instrucao \t tipo da instrucao \t registrador de destino \t operador1 \t operador2 \t instrucoes dependentes
	 * Sabendo que cada uma das informa��es sobre a instru��o � separada por "\t", busca pelos "\t" pra quebrar a linha
	 * A cada "\t" � salvo no atributo correspondente a informa��o dada no arquivo
	 * Retorna a instru��o lida
	 * 
	 */
	Instrucao criarInstrucao(String linha)
	{
		int contadorDetab = 0;
		Instrucao instrucao = new Instrucao();
		for(int index = 0; index < linha.length(); index++)
		{
			char[] line = linha.toCharArray();
			//inst
			if (line[index] != '\t' && contadorDetab == 0)
				instrucao.numeroDaInstrucao = instrucao.numeroDaInstrucao + line[index];
			else if (line[index] == '\t' && contadorDetab == 0)
				contadorDetab++;
			//tipo
			else if (line[index] != '\t' && contadorDetab == 1)
				instrucao.operacao = instrucao.operacao + line[index];
			else if (line[index] == '\t' && contadorDetab == 1)
				contadorDetab++;
			//dest
			else if (line[index] != '\t' && contadorDetab == 2)
				instrucao.destino.nome = instrucao.destino.nome + line[index];
			else if (line[index] == '\t' && contadorDetab == 2)
				contadorDetab++;
			//op1
			else if (line[index] != '\t' && contadorDetab == 3)
				instrucao.operando1.nome = instrucao.operando1.nome + line[index];
			else if (line[index] == '\t' && contadorDetab == 3)
				contadorDetab++;
			//op2
			else if (line[index] != '\t' && contadorDetab == 4)
				instrucao.operando2.nome = instrucao.operando2.nome + line[index];
			else if (line[index] == '\t' && contadorDetab == 4)
				contadorDetab++;
			//inst_dependente
			else if (line[index] != '\t' && contadorDetab == 5)
				instrucao.dependentes = instrucao.dependentes + line[index];
		}
		return instrucao;
	}

	/*
	 * Fun��o respons�vel por gerar um arquivo de texto com base no arrayList usado como buffer de um grafo
	 * Fun��o interna chamada na leitura do grafo
	 * Recebe um arrayList
	 * Cria um buffer do tipo "file writer" para criar o arquivo de texto que ser� passado como saida
	 * Percorre o arrayList passado na entrada e a cada indice escreve seus atributos na linha do arquivo
	 * Retorna o caminho do arquivo de saida
	 */
	String salvarGrafo(ArrayList<Instrucao> lista, String localParaArmazenamento)
	{
		System.out.println(">>> Gerando novo grafo <<<");
		
		try 
		{
			PrintWriter escrever = new PrintWriter(new FileWriter(localParaArmazenamento));
			escrever.println("#inst 	tipo	dest 	op1 	op2 	#inst_recebe_resultado	Ciclo_Inicial	Falsa_Dependencia");
			for (int index = 0; index < lista.size(); index++)
			{
				
				escrever.print(lista.get(index).numeroDaInstrucao + "\t");
				escrever.print(lista.get(index).operacao + "\t");
				escrever.print(lista.get(index).destino.nome + "\t");
				escrever.print(lista.get(index).operando1.nome + "\t");
				escrever.print(lista.get(index).operando2.nome + "\t");
				escrever.print(lista.get(index).dependentes + "\t");
				escrever.print(lista.get(index).cicloInicial + "\t");
				escrever.print(lista.get(index).dependenciaFalsa + "\n");
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
			System.out.println(">>> Constru��o do novo arquivo encerrada! <<<\n");
		}
	}
	
	/*
	 * Fun��o respons�vel por fazer a busca de instru��es completamente independentes
	 * Recebe o arrayList com as instru��es
	 * Esta fun��o � o primeiro nivel da reordena��o pq s� busca por instru��es que n�o tenham nenhum tipo de dependecia
	 * Ela � dividida em dois blocos
	 * Passo1: Busca e insere num arrayList auxiliar as instru��es independentes
	 * Passo2: Busca e insere no arrayList auxiliar as intru��es restantes
	 * Retorna a lista criada
	 * 
	 */
	ArrayList<Instrucao> buscarInstrucoesIndependentes(ArrayList<Instrucao> listaDeInstrucoes)
	{
		System.out.println(">>> Busca por instru��es independentes iniciada <<<");
		boolean indepentente = true;
		ArrayList<Instrucao> bufferDaLista = new ArrayList<>();
		//buscar e inserir instru��es independentes
		for (int index = 0; index < listaDeInstrucoes.size(); index++)
		{
			for (int indexAux = 0; indexAux < listaDeInstrucoes.size(); indexAux++)
			{	
				String instrucaoDeDentro = listaDeInstrucoes.get(indexAux).dependentes;
				String instrucaoDeFora = listaDeInstrucoes.get(index).numeroDaInstrucao;
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
		//Inserir instru��es com dependencias
		for (int index = 0; index < listaDeInstrucoes.size(); index++)
		{
			for (int indexAux = 0; indexAux < bufferDaLista.size(); indexAux++)
			{
				if (listaDeInstrucoes.get(index).numeroDaInstrucao == bufferDaLista.get(indexAux).numeroDaInstrucao)
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
		System.out.println(">>> Busca de instru��es independentes encerrada! <<<\n");
		return bufferDaLista;
	}
	
	/*
	 * Fun��o respons�vel por simular os ciclos de um pipeline com uma lista de instru��es
	 * Recebe um arrayList das instru��es a serem simuladas
	 * Percorre a lista verificando as dependencias para criar as bolhas necess�rias
	 * Inicializa o campo de ciclo inicial das instru��es
	 * 
	 */
	void simularCiclos(ArrayList<Instrucao> listaDeInstrucoes)
	{
		for (int index = 0; index < listaDeInstrucoes.size(); index++)
		{
			listaDeInstrucoes.get(index).cicloInicial = index;
		}
		
		int cicloAtual = 1;
		for (int index = 1; index < listaDeInstrucoes.size(); index++)
		{
			for (int indexAux = index-1; indexAux >= 0; indexAux--)
			{
				String instrucaoDeFora = listaDeInstrucoes.get(index).numeroDaInstrucao;
				String instrucaoDeDentro = listaDeInstrucoes.get(indexAux).dependentes;
				
				if (instrucaoDeFora.contains(instrucaoDeDentro))
				{
					int tempo;
					boolean podeInserir = false;
					while (!podeInserir)
					{
						tempo = cicloAtual - listaDeInstrucoes.get(indexAux).cicloInicial;
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
			listaDeInstrucoes.get(index).cicloInicial = cicloAtual;
			cicloAtual++;
		}
	}
	
	/*
	 * Fun��o respons�vel por auxiliar na analise de conflito nas reordena��es
	 * Esta fun��o � de uso interno, chamada no reordenamento de instru��es
	 * Verifica um pequeno bloco de instru��es da lista
	 * Recebe um arrayList com as instru��es, um inteiro indicando a posi��o inicial do "bloco" e outro indicando o final
	 * Percorre esse "bloco" de instru��es avaliando se h� conflito nelas
	 * Um conflito ocorre quando ao reordenar as intru��es uma instru��o com dependencia come�a antes da sua "fornecedora" terminar
	 * Retorna true, caso a troca da posi��o das instru��es n�o gere conflito e false caso contr�rio
	 *
	 */
	boolean buscarConflitoAuxiliar(ArrayList<Instrucao> lista, int comeco)
	{
		//buscar e inserir instru��es sem conflito
		int index = comeco;
		for (int indexAux = index-1; indexAux >= 0; indexAux--)
		{	
			String instrucaoReordenada = lista.get(index).numeroDaInstrucao;
			String instrucaoAtual = lista.get(indexAux).dependentes;
			int tempo = lista.get(index).cicloInicial - lista.get(indexAux).cicloInicial;
			if (instrucaoAtual.contains(instrucaoReordenada) && tempo < 3)
			{
				podeTrocar = false;
				return false;
			}
		}
		return true;
	}
	
	/*
	 * Fun��o respons�vel por trocar a posi��o de duas instru��es
	 * Est� fun��o � de uso interno, chamada no reordenamento de instru��es
	 * Recebe um arrayList com as intru��es e dois inteiros que indicam as posi��es no arrayList que dever�o ser trocadas
	 * Armazena a instru��o da posi��o x em um buffer e troca x com y e em seguida y com o buffer
	 * 
	 */
	void trocarPosicao(ArrayList<Instrucao> lista, int x, int y)
	{
		Instrucao buffer = lista.get(y);
		lista.set(y, lista.get(x));
		lista.set(x, buffer);
	}
	
	/*
	 * Fun��o respons�vel por clonar uma lista
	 * Est� fun��o � de uso interno e faz uma c�pia de uma lista passada como entrada
	 * Recebe um arrayList de instru��es a ser oopiado
	 * Cria uma lista temporaria que servir� de buffer, percorre a lista passada como par�metro
	 * Atribuindo cada elemento da lista em uma posi��o do buffer
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
	 * Fun��o respons�vel por mostrar uma lista
	 * Est� fun��o recebe um arrayList com instru��es e imprime seus elementos
	 * Recebe um arrayList
	 * Percorre o arrayList passado como par�metro e imprime todos os campos de sua instru��o, uma por uma
	 * 
	 */
	void mostrarInstrucoes(ArrayList<Instrucao> listaDeInstrucoes)
	{
		System.out.println(">>> Exibindo grafo <<<");
		System.out.println();
		System.out.println("#inst 	tipo	dest 	op1 	op2 	#inst_recebe_resultado	Ciclo_Inicial"
				+ "	Falsa_Dependencia");
		for (int index = 0; index < listaDeInstrucoes.size(); index++)
		{
			System.out.print(listaDeInstrucoes.get(index).numeroDaInstrucao + "\t");
			System.out.print(listaDeInstrucoes.get(index).operacao + "\t");
			System.out.print(listaDeInstrucoes.get(index).destino.nome + "\t");
			System.out.print(listaDeInstrucoes.get(index).operando1.nome + "\t");
			System.out.print(listaDeInstrucoes.get(index).operando2.nome + "\t\t");
			System.out.print(listaDeInstrucoes.get(index).dependentes + "\t\t\t");
			System.out.print(listaDeInstrucoes.get(index).cicloInicial + "\t\t");
			System.out.print(listaDeInstrucoes.get(index).dependenciaFalsa + "\n");
		}
		System.out.println();
		System.out.println(">>> Exibicao encerrada! <<<\n");
	}
	
	/*
	 * Fun��o respons�vel por manter a corretude do grafo
	 * Est� fun��o � de uso interno e recebe um arrayList de instru��es, retornando um booleano
	 * A corretude � mantida se uma instru��o y depende da x e a y vem depois da x, para todas as instru��es
	 * Percorre o arrayList passado como par�metro e pra cada posi��o busca seus dependentes
	 * Se houver algum dependente que venha antes da posi��o atual, a corretude foi comprometida
	 * Retorna true se o programa est� correto, false caso contr�rio
	 * 
	 */
	boolean analisarCorretude(ArrayList<Instrucao> listaDeInstrucoes)
	{
		for (int index = 0; index < listaDeInstrucoes.size(); index++)
		{
			for (int indexAux = 0; indexAux < listaDeInstrucoes.size(); indexAux++)
			{
				String instrucaoDeFora = listaDeInstrucoes.get(index).dependentes;
				String instrucaoDeDentro = listaDeInstrucoes.get(indexAux).numeroDaInstrucao;
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
	 * Fun��o respons�vel por ordenar as intru��es por ordem do ciclo inicial
	 * Recebe um arrayList de instru��es e retorna-o ordenado
	 * Percorre todo o arrayList buscando alguma instru��o que esteja fora de ordem
	 * A ordena��o � mantida pela regra ciclo inicial de x > ciclo inicial de y <=> y -> x
	 * Retorna um arrayList com as intru��es ordenadas por ordem crescente do ciclo inicial
	 * 
	 */
	ArrayList<Instrucao> organizarInstrucoes(ArrayList<Instrucao> listaDeInstrucoes)
	{
		for (int index = 0; index < listaDeInstrucoes.size(); index++)
		{
			for (int indexAux = 0; indexAux < listaDeInstrucoes.size(); indexAux++)
			{
				if (listaDeInstrucoes.get(indexAux).cicloInicial > listaDeInstrucoes.get(index).cicloInicial)
				{
					trocarPosicao(listaDeInstrucoes, index, indexAux);
				}
			}
		}
		return listaDeInstrucoes;
	}
	
	
	/*
	 * Fun��o respons�vel por fazer o reordenamento reordenar as instru��es
	 * Percorre todo o arrayList e testa instru��es que podem ser reordenadas e pra qual nova posi��o ela pode ir
	 * Recebe um arrayList de instru��es
	 * Cria um arrayList que servir� de buffer para reordenamento e come�a o percorrimento pela ultima instru��o
	 * o percorrimento � feito do fim para o come�o do vetor, verificando se a instru��o atual pode ser trocada
	 * pela anterior, sempre verificando a corretude, quando chegar em um ponto que a troca da posi��o n�o pode ser
	 * feita, a instru��o � realocada pra ultima posi��o que pode ser inserida
	 * Se o reordenamento for produtivo (total de ciclos menor), retorna o arrayList usado como buffer
	 * caso contrario, retorna a lista do jeito que foi passada
	 * 
	 */
	ArrayList<Instrucao> reordenarInstrucoes(ArrayList<Instrucao> listaDeInstrucoes)
	{
		System.out.println(">>> Reordenamento de instrucoes iniciado <<<");
		ArrayList<Instrucao> buffer = clonarLista(listaDeInstrucoes);
		int totalDeCiclosAntigo = listaDeInstrucoes.get(listaDeInstrucoes.size()-1).cicloInicial + 4;
		int instrucoesReordenadas = 0;
		
		for (int index = listaDeInstrucoes.size()-1 ; index >= instrucoesIndependentes; index--)
		{
			String instrucaoAntesDoOrdenamento = listaDeInstrucoes.get(index).numeroDaInstrucao;
			for (int indexAux = index-1; indexAux >= instrucoesIndependentes; indexAux--)
			{	
				String dependentes = listaDeInstrucoes.get(indexAux).dependentes;
				String instrucaoDeFora = listaDeInstrucoes.get(index).numeroDaInstrucao;
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
			String instrucaoDepoisDoOrdenamento = buffer.get(index).numeroDaInstrucao;
			if (instrucaoAntesDoOrdenamento != instrucaoDepoisDoOrdenamento)
				instrucoesReordenadas++;
			podeTrocar = true;
		}		
		simularCiclos(buffer);
		
		System.out.println("Foram reordenada(s) " + instrucoesReordenadas + " instru��es");
		System.out.println(">>> Reordenamento encerrado! <<<\n");
		if (buffer.get(buffer.size()-1).cicloInicial + 4 < totalDeCiclosAntigo)
			return buffer;
		return listaDeInstrucoes;
	}

	/*
	 * Fun��o respons�vel por buscar e corrigir as falsas dependencias
	 * Est� � uma fun��o interna, usada em conjunto com a analise de corretude no reordenamento
	 * Recebe um arrayList de instru��es e corrige as falsas dependencias se houver recursos necess�rios
	 * Recebe um arrayList de instru��es e retorna um booleano
	 * Primeiro armazena a posicao das instru��es que possuem falsas dependencias em um arrayList de quarentena
	 * ap�s saber quais instru��es possuem falsas dependencias, percorre a lista afim de trocar o registrador da 
	 * da instru��o em quarentena, ao encontra-la busca um registrador vago no banco de registradores
	 * se n�o houver, n�o � possivel corrigir todas falsas dependencias, retorna false indicando o ocorrido
	 * caso exista, busca as instru��es que dependem desta e atualiza seus registradores
	 * Por final atualiza o banco de registradores para salvar as modifica��es e retorna true
	 * 
	 */
	boolean corrigirFalsasDependecias(ArrayList<Instrucao> listaDeInstrucoes)
	{
		ArrayList<Integer> instrucoesEmQuarentena = new ArrayList<>();
		for (int index = 0; index< listaDeInstrucoes.size(); index++)
		{
			if(listaDeInstrucoes.get(index).dependenciaFalsa)
			{
				instrucoesEmQuarentena.add(index);
			}
		}
		
		for(int index = 0; index < instrucoesEmQuarentena.size(); index++)
		{
			for (int indexAux = 0; indexAux < listaDeInstrucoes.size(); indexAux++)
			{
				Instrucao instrucaoEmQuarentena = listaDeInstrucoes.get(instrucoesEmQuarentena.get(index));
				if(instrucaoEmQuarentena.destino.equals(listaDeInstrucoes.get(indexAux).destino) && 
						instrucoesEmQuarentena.get(index) >= indexAux)
				{
					Registrador registradorAntigo = instrucaoEmQuarentena.destino;
					instrucaoEmQuarentena.destino = buscarNovoRegistrador();
					if (instrucaoEmQuarentena.destino == null)
					{
						instrucaoEmQuarentena.destino = registradorAntigo;
						return false;
					}
					else
					{
						for (int posicao = indexAux; posicao < listaDeInstrucoes.size(); posicao++)
						{
							String dependentes = instrucaoEmQuarentena.dependentes;
							String instrucaoAtual = listaDeInstrucoes.get(posicao).numeroDaInstrucao;
							if (dependentes.contains(instrucaoAtual))
							{
								if (listaDeInstrucoes.get(posicao).operando1.nome.contains(registradorAntigo.nome))
								{
									listaDeInstrucoes.get(posicao).operando1 = instrucaoEmQuarentena.destino;
								}
								if (listaDeInstrucoes.get(posicao).operando2.nome.contains(registradorAntigo.nome))
								{
									listaDeInstrucoes.get(posicao).operando2 = instrucaoEmQuarentena.destino;
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
	 * Fun��o respons�vel por buscar um registrador vago no banco de registradores
	 * Est� fun��o � interna, chamada na corre��o de falsas dependencias para pegar um novo registrador
	 * Percorre o banco de registradores buscando um n�o usado e o retorna
	 * Caso n�o exista, retorna null
	 * 
	 */
	Registrador buscarNovoRegistrador()
	{
		for(int index = 0; index < conjuntoDeRegistradores.size(); index++)
		{
			if (!conjuntoDeRegistradores.get(index).ehEscrito && !conjuntoDeRegistradores.get(index).ehLido)
			{
				return conjuntoDeRegistradores.get(index);
			}
		}
		return null;
	}
}