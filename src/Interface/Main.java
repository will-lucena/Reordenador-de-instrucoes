package Interface;

import java.util.ArrayList;

import Estruturas.Instrucao;
import Funcoes.Funcoes;

public class Main 
{

	@SuppressWarnings("unchecked")
	public static void main(String[] args) 
	{
		Funcoes teste = new Funcoes();
		String caminho = "src/grafo.txt";
		String saida = "src/novografo.txt";
		ArrayList<Instrucao> lista = teste.lerGrafo(caminho);
		lista = teste.buscarInstrucoesIndependentes(lista);
		teste.simularCiclos(lista);
		teste.salvarGrafo(lista, saida);
		lista = teste.buscarInstrucoesSemConflito(lista);
		lista = teste.organizarInstrucoes(lista);
		teste.salvarGrafo(lista, saida);
		teste.mostrarInstrucoes(lista);
	}
}
