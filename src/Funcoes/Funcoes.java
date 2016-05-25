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
	boolean prob = false;
	ArrayList<Registrador> conjuntoDeRegistradores = new ArrayList<>();
	
	@SuppressWarnings("resource")
	void criarBancoDeRegistradores(String registradores)
	{
		System.out.println(">>> Inicializando banco de registradores <<<");
		try
		{
			FileReader arquivo;
			arquivo = new FileReader(registradores);
			BufferedReader ler = new BufferedReader(arquivo);
			String linha = ler.readLine();
			linha = ler.readLine();
			
			while(linha != null)
			{
				Registrador reg = new Registrador();
				reg.nome = linha;
				conjuntoDeRegistradores.add(reg);
				linha = ler.readLine();
			}
		}
		catch (FileNotFoundException e) 
		{
			System.out.println(">>> A inicialização falhou <<<");
			e.printStackTrace();
		} catch (IOException e) 
		{
			System.out.println(">>> A inicialização falhou <<<");
			e.printStackTrace();
		}
		finally
		{
			System.out.println(">>> Inicialização do banco de registradores encerrada <<<");
		}
	}
	
	void atualizarBancoDeRegistradores(ArrayList<Instrucao> instrucoes)
	{
		for (int i = 0; i < instrucoes.size(); i++)
		{
			for (int j = 0; j < conjuntoDeRegistradores.size(); j++)
			{
				String s1 = instrucoes.get(i).destino;
				String s2 = instrucoes.get(i).operando1;
				String s3 = instrucoes.get(i).operando2;
				String s4 = conjuntoDeRegistradores.get(j).nome;
				
				if (s1.equals(s4))
				{
					if (conjuntoDeRegistradores.get(j).ehEscrito)
					{
						instrucoes.get(i).dependenciaFalsa = true;
					}
					conjuntoDeRegistradores.get(j).ehEscrito = true;
				}
				if (s2.equals(s4) || s3.equals(s4))
				{
					conjuntoDeRegistradores.get(j).ehLido = true;
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	//Lê grafo de um determinado local
	public ArrayList lerGrafo(String grafo)
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
			FileReader arquivo = new FileReader(grafo);
			BufferedReader ler = new BufferedReader(arquivo);
			String linha = ler.readLine();
			linha = ler.readLine();
			
			while (linha != null)
			{
				//Instrucao n1 = criarInstrucao(linha);
				//No elemento = new No(n1);
				instrucoes.add((criarInstrucao(linha)));
				linha = ler.readLine();
			}
			arquivo.close();
			criarBancoDeRegistradores("E:/Projetos/src/registradores.txt");
			atualizarBancoDeRegistradores(instrucoes);
			return instrucoes;
		}
		catch (IOException e)
		{
			//Caso ocorra algum problema na leitura ou abertura do arquivo
			System.out.println(">>> A leitura Falhou <<<");
			return null;
		}
		finally
		{
			//Após leitura do arquivo criação da lista evoca o método para mostrar as instruções
			for (int i = 0; i < instrucoes.size(); i++)
			{
				System.out.print(instrucoes.get(i).numeroDaInstrucao + "\t");
				System.out.print(instrucoes.get(i).operacao + "\t");
				System.out.print(instrucoes.get(i).destino + "\t");
				System.out.print(instrucoes.get(i).operando1 + "\t");
				System.out.print(instrucoes.get(i).operando2 + "\t");
				System.out.print(instrucoes.get(i).dependentes + "\t");
				System.out.print(instrucoes.get(i).cicloInicial + "\n");
			}
			System.out.println();
			System.out.println(">>> Leitura do grafo encerrada! <<<");
		}
	}
	
	Instrucao criarInstrucao(String linha)
	{
		/*
		 * Dada uma linha do arquivo como entrada, percorre a mesma quebrando em partes
		 * Dado que cada parte é separada por \t, busca por esses chars pra quebrar a linha
		 * A cada \t é salvo no atributo correspondente a informação dada no arquivo
		 * Retorna a instrução lida
		 */
		int cont = 0;
		Instrucao n1 = new Instrucao();
		for(int i = 0; i < linha.length(); i++)
		{
			char[] v = linha.toCharArray();
			//inst
			if (v[i] != '\t' && cont == 0)
				n1.numeroDaInstrucao = n1.numeroDaInstrucao + v[i];
			else if (v[i] == '\t' && cont == 0)
				cont++;
			//tipo
			else if (v[i] != '\t' && cont == 1)
				n1.operacao = n1.operacao + v[i];
			else if (v[i] == '\t' && cont == 1)
				cont++;
			//dest
			else if (v[i] != '\t' && cont == 2)
				n1.destino = n1.destino + v[i];
			else if (v[i] == '\t' && cont == 2)
				cont++;
			//op1
			else if (v[i] != '\t' && cont == 3)
				n1.operando1 = n1.operando1 + v[i];
			else if (v[i] == '\t' && cont == 3)
				cont++;
			//op2
			else if (v[i] != '\t' && cont == 4)
				n1.operando2 = n1.operando2 + v[i];
			else if (v[i] == '\t' && cont == 4)
				cont++;
			//inst_dependente
			else if (v[i] != '\t' && cont == 5)
				n1.dependentes = n1.dependentes + v[i];
		}
		return n1;
	}
	
	public String salvarGrafo(ArrayList<Instrucao> lista)
	{
		System.out.println(">>> Gerando novo grafo <<<");
		String caminho = "grafoNovo.txt";
		
		try 
		{
			FileWriter file = new FileWriter(caminho);
			PrintWriter escrever = new PrintWriter(file);
			escrever.println("#inst 	tipo	dest 	op1 	op2 	#inst_recebe_resultado");
			for (int i = 0; i < lista.size(); i++)
			{
				
				escrever.print(lista.get(i).numeroDaInstrucao + "\t");
				escrever.print(lista.get(i).operacao + "\t");
				escrever.print(lista.get(i).destino + "\t");
				escrever.print(lista.get(i).operando1 + "\t");
				escrever.print(lista.get(i).operando2 + "\t");
				escrever.print(lista.get(i).dependentes + "\t");
				escrever.print(lista.get(i).cicloInicial + "\n");
			}
			file.close();
			System.out.println("Arquivo gerado com sucesso e salvo em: " + caminho);
			return caminho;
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			return caminho;
		}
		finally
		{
			System.out.println(">>> Construção do novo arquivo encerrada! <<<");
		}
	}
	
	boolean compararStrings(String listaDeDependentes, String instrucao)
	{
		char[] s1 = listaDeDependentes.toCharArray();
		char[] s2 = instrucao.toCharArray();
		
		for (int i = 0; i < s1.length; i++)
			if (s1[i] == s2[0])
				return true;
		
		return false;
	}
	
	public ArrayList<Instrucao> buscarInstrucoesIndependentes(ArrayList<Instrucao> lista)
	{
		System.out.println(">>> Busca por instruções independentes iniciada <<<");
		boolean problem = false;
		ArrayList<Instrucao> nova = new ArrayList<>();
		//buscar e inserir instruções independentes
		for (int i = 0; i < lista.size(); i++)
		{
			for (int j = 0; j < lista.size(); j++)
			{	
				String s2 = lista.get(i).numeroDaInstrucao;
				String s1 = lista.get(j).dependentes;
				if (compararStrings(s1, s2) == true)
				{
					problem = true;
					break;
				}
			}
			if (problem == false)
			{
				nova.add(lista.get(i));
				instrucoesIndependentes++;
			}
			problem = false;
		}
		//Inserir instruções com dependencias
		for (int i = 0; i < lista.size(); i++)
		{
			for (int j = 0; j < nova.size(); j++)
			{
				if (lista.get(i).numeroDaInstrucao == nova.get(j).numeroDaInstrucao)
				{
					problem = true;
					break;
				}
			}
			if (problem == false)
				nova.add(lista.get(i));
			problem = false;
		}
		System.out.println("Foram encontradas " + instrucoesIndependentes + " instrucoes independentes");
		System.out.println(">>> Busca de instruções independentes encerrada! <<<");
		return nova;
	}
	
	public void simularCiclos(ArrayList<Instrucao> lista)
	{
		for (int i = 0; i < lista.size(); i++)
		{
			lista.get(i).cicloInicial = i;
		}
		//*
		int aux = 1;
		for (int i = 1; i < lista.size(); i++)
		{
			for (int j = i-1; j >= 0; j--)
			{
				String s2 = lista.get(i).numeroDaInstrucao;
				String s1 = lista.get(j).dependentes;
				
				if (compararStrings(s1, s2) == true)
				{
					int tempo;
					boolean pronto = false;
					while (pronto == false)
					{
						tempo = aux - lista.get(j).cicloInicial;
						if (tempo > 2)
						{
							pronto = true;
						}
						else
						{
							aux++;
						}
					}
				}
			}
			lista.get(i).cicloInicial = aux;
			aux++;
		}
		/**/
	}
	
	boolean buscarConflitoAuxiliar(ArrayList<Instrucao> lista, int comeco, int fim)
	{
		//buscar e inserir instruções sem conflito
		int i = comeco;
		for (int j = i-1; j >= fim; j--)
		{	
			String s2 = lista.get(i).numeroDaInstrucao;
			String s1 = lista.get(j).dependentes;
			int tempo = lista.get(i).cicloInicial - lista.get(j).cicloInicial;
			if (compararStrings(s1, s2) == true && tempo < 3)
			{
				prob = true;
				return false;
			}
		}
		return true;
	}
	
	void trocarPosicao(ArrayList<Instrucao> lista, int x, int y)
	{
		Instrucao tmp = lista.get(y);
		lista.set(y, lista.get(x));
		lista.set(x, tmp);
	}
	
	ArrayList<Instrucao> clonarLista(ArrayList<Instrucao> lista)
	{
		ArrayList<Instrucao> clone = new ArrayList<Instrucao>();
		
		for (int i = 0; i < lista.size(); i++)
		{
			clone.add(lista.get(i));
		}
		
		return clone;
	}
	
	public void show(ArrayList<Instrucao> lista)
	{
		for (int i = 0; i < lista.size(); i++)
		{
			
			System.out.print(lista.get(i).numeroDaInstrucao + "\t");
			System.out.print(lista.get(i).operacao + "\t");
			System.out.print(lista.get(i).destino + "\t");
			System.out.print(lista.get(i).operando1 + "\t");
			System.out.print(lista.get(i).operando2 + "\t");
			System.out.print(lista.get(i).dependentes + "\t");
			System.out.print(lista.get(i).cicloInicial + "\t");
			System.out.print(lista.get(i).dependenciaFalsa + "\n");
		}
		System.out.println();
	}
	
	boolean analisarCorretude(ArrayList<Instrucao> lista)
	{
		for (int i = 0; i < lista.size(); i++)
		{
			for (int j = 0; j < lista.size(); j++)
			{
				String s2 = lista.get(i).numeroDaInstrucao;
				String s1 = lista.get(j).dependentes;
				if (compararStrings(s1, s2) == true && i < j)
				{
					prob = true;
					return false;
				}
			}
		}
		
		return true;
	}	
	
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
	
	public ArrayList<Instrucao> buscarInstrucoesSemConflito(ArrayList<Instrucao> lista)
	{
		System.out.println(">>> Busca por instruções que não dependam de anteriores iniciada <<<");
		ArrayList<Instrucao> nova = clonarLista(lista);
		int tempo = lista.get(lista.size()-1).cicloInicial;
		int cont = 0;
		
		//buscar e inserir instruções sem conflito
		for (int i = lista.size()-1 ; i >= instrucoesIndependentes; i--)
		{
			String antigo = lista.get(i).numeroDaInstrucao;
			for (int j = i-1; j >= instrucoesIndependentes; j--)
			{	
				String s2 = lista.get(i).numeroDaInstrucao;
				String s1 = lista.get(j).dependentes;
				if (compararStrings(s1, s2) == true)
				{
					prob = true;
					break;
				}
				else
				{
					int atual = j+1;
					trocarPosicao(nova, atual, atual-1);
					simularCiclos(nova);
					while(buscarConflitoAuxiliar(nova, atual-1, 0) == true && atual > 1)
					{
						if (!analisarCorretude(nova) || !verificarFalsaDependecia(nova))
						{
							prob = true;
							break;
						}
						atual--;
						trocarPosicao(nova, atual-1, atual);
						simularCiclos(nova);
					}
					if (prob == true)
						trocarPosicao(nova, atual, atual-1);
				}
			}
			String novo = nova.get(i).numeroDaInstrucao;
			if (antigo != novo)
				cont++;
			prob = false;
		}		
		simularCiclos(nova);
		
		System.out.println("Foram encontradas " + cont + " instrucoes sem conflito");
		System.out.println(">>> Busca de instruções nao conflitantes encerrada! <<<");
		if (nova.get(nova.size()-1).cicloInicial < tempo)
			return nova;
		return lista;
	}

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
						Integer.parseInt(lista.get(i).numeroDaInstrucao) < j)
				{
					return false;
				}
			}
		}
		return true;
	}
}