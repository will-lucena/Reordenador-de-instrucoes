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
	boolean trouble = false;
	ArrayList<Registrador> conjuntoDeRegistradores = new ArrayList<>();
	
	/*
	 * Fun��o respons�vel por criar o banco de registradores
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
			System.err.println("Falha na leitura");
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
	public ArrayList<Instrucao> lerGrafo(String grafo)
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
			//Ap�s leitura do arquivo cria��o da lista evoca o m�todo para mostrar as instru��es
			mostrarInstrucoes(instrucoes);
			System.out.println(">>> Leitura do grafo encerrada! <<<");
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
	 * Recebe um arrayList
	 * Cria um buffer do tipo "file writer" para criar o arquivo de texto que ser� passado como saida
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
			System.out.println(">>> Constru��o do novo arquivo encerrada! <<<");
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
	 */
	public ArrayList<Instrucao> buscarInstrucoesIndependentes(ArrayList<Instrucao> listaDeInstrucoes)
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
		System.out.println(">>> Busca de instru��es independentes encerrada! <<<");
		return bufferDaLista;
	}
	
	/*
	 * Fun��o respons�vel por simular os ciclos de um pipeline com uma lista de instru��es
	 * Recebe um arrayList das instru��es a serem simuladas
	 * Percorre a lista verificando as dependencias para criar as bolhas necess�rias
	 * Inicializa o campo de ciclo inicial das instru��es
	 * 
	 */
	public void simularCiclos(ArrayList<Instrucao> listaDeInstrucoes)
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
	 * Esta fun��o � de uso interno, chamada no reordenamento de instru��es e verifica um pequeno bloco de instru��es da lista
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
				trouble = true;
				return false;
			}
		}
		return true;
	}
	
	/*
	 * N�o corrigida
	 */
	void trocarPosicao(ArrayList<Instrucao> lista, int x, int y)
	{
		Instrucao tmp = lista.get(y);
		lista.set(y, lista.get(x));
		lista.set(x, tmp);
	}
	
	/*
	 * N�o corrigida
	 */
	ArrayList<Instrucao> clonarLista(ArrayList<Instrucao> lista)
	{
		ArrayList<Instrucao> clone = new ArrayList<Instrucao>();
		
		for (int i = 0; i < lista.size(); i++)
		{
			clone.add(lista.get(i));
		}
		
		return clone;
	}
	
	/*
	 * N�o corrigida
	 */
	public void mostrarInstrucoes(ArrayList<Instrucao> lista)
	{
		for (int i = 0; i < lista.size(); i++)
		{
			
			System.out.print(lista.get(i).numeroDaInstrucao + "\t");
			System.out.print(lista.get(i).operacao + "\t");
			System.out.print(lista.get(i).destino.nome + "\t");
			System.out.print(lista.get(i).operando1.nome + "\t");
			System.out.print(lista.get(i).operando2.nome + "\t");
			System.out.print(lista.get(i).dependentes + "\t");
			System.out.print(lista.get(i).cicloInicial + "\t");
			System.out.print(lista.get(i).dependenciaFalsa + "\n");
		}
		System.out.println();
	}
	
	/*
	 * N�o corrigida
	 */
	boolean analisarCorretude(ArrayList<Instrucao> lista)
	{
		for (int i = 0; i < lista.size(); i++)
		{
			for (int j = 0; j < lista.size(); j++)
			{
				String s1 = lista.get(i).numeroDaInstrucao;
				String s2 = lista.get(j).dependentes;
				if (s2.contains(s1) && i < j)
				{
					trouble = true;
					return false;
				}
			}
		}
		
		return true;
	}	
	
	/*
	 * N�o corrigida
	 */
	public ArrayList<Instrucao> organizarInstrucoes(ArrayList<Instrucao> lista)
	{
		for (int i = 0; i < lista.size(); i++)
		{
			for (int j = 0; j < lista.size(); j++)
			{
				if (lista.get(j).cicloInicial > lista.get(i).cicloInicial)
				{
					trocarPosicao(lista, i, j);
				}
			}
		}
		
		return lista;
	}
	
	/*
	 * N�o corrigida
	 */
	public ArrayList<Instrucao> buscarInstrucoesSemConflito(ArrayList<Instrucao> lista)
	{
		System.out.println(">>> Busca por instru��es que n�o dependam de anteriores iniciada <<<");
		ArrayList<Instrucao> nova = clonarLista(lista);
		int tempo = lista.get(lista.size()-1).cicloInicial;
		int cont = 0;
		
		//buscar e inserir instru��es sem conflito
		for (int i = lista.size()-1 ; i >= instrucoesIndependentes; i--)
		{
			String antigo = lista.get(i).numeroDaInstrucao;
			for (int j = i-1; j >= instrucoesIndependentes; j--)
			{	
				String s1 = lista.get(i).numeroDaInstrucao;
				String s2 = lista.get(j).dependentes;
				if (s2.contains(s1))
				{
					trouble = true;
					break;
				}
				else
				{
					int atual = j+1;
					trocarPosicao(nova, atual, atual-1);
					simularCiclos(nova);
					while(buscarConflitoAuxiliar(nova, atual-1) == true && atual > 1)
					{
						if (!analisarCorretude(nova) || !verificarFalsaDependecia(nova))
						{
							trouble = true;
							break;
						}
						atual--;
						trocarPosicao(nova, atual-1, atual);
						simularCiclos(nova);
					}
					if (trouble == true)
						trocarPosicao(nova, atual, atual-1);
				}
			}
			String novo = nova.get(i).numeroDaInstrucao;
			if (antigo != novo)
				cont++;
			trouble = false;
		}		
		simularCiclos(nova);
		
		System.out.println("Foram encontradas " + cont + " instrucoes sem conflito");
		System.out.println(">>> Busca de instru��es nao conflitantes encerrada! <<<");
		if (nova.get(nova.size()-1).cicloInicial < tempo)
			return nova;
		return lista;
	}

	/*
	 * N�o corrigida
	 */
	public boolean verificarFalsaDependecia(ArrayList<Instrucao> lista)
	{
		ArrayList<Integer> emQuarentena = new ArrayList<>();
		for (int i = 0; i< lista.size(); i++)
		{
			if(lista.get(i).dependenciaFalsa)
			{
				emQuarentena.add(i);
			}
		}
		
		for(int i = 0; i < emQuarentena.size(); i++)
		{
			for (int j = 0; j < lista.size(); j++)
			{
				if(lista.get(emQuarentena.get(i)).destino.equals(lista.get(j).destino) && 
						emQuarentena.get(i) >= j)
				{
					Registrador antigo = lista.get(emQuarentena.get(i)).destino;
					lista.get(emQuarentena.get(i)).destino = buscarNovoRegistrador();
					if (lista.get(emQuarentena.get(i)).destino == null)
					{
						lista.get(emQuarentena.get(i)).destino = antigo;
						return false;
					}
					else
					{
						for (int k = j; k < lista.size(); k++)
						{
							String s2 = lista.get(emQuarentena.get(i)).dependentes;
							String s1 = lista.get(k).numeroDaInstrucao;
							if (s2.contains(s1))
							{
								if (lista.get(k).operando1.nome.contains(antigo.nome))
								{
									lista.get(k).operando1 = lista.get(emQuarentena.get(i)).destino;
								}
								if (lista.get(k).operando2.nome.contains(antigo.nome))
								{
									lista.get(k).operando2 = lista.get(emQuarentena.get(i)).destino;
								}
							}
						}
					}
					atualizarBancoDeRegistradores(lista);
				}
			}
		}
		return true;
	}

	/*
	 * N�o corrigida
	 */
	public Registrador buscarNovoRegistrador()
	{
		for(int i = 0; i < conjuntoDeRegistradores.size(); i++)
		{
			if (!conjuntoDeRegistradores.get(i).ehEscrito && !conjuntoDeRegistradores.get(i).ehLido)
			{
				return conjuntoDeRegistradores.get(i);
			}
		}
		
		return null;
	}
}