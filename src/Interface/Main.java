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
		String caminho = "grafo.txt";
		ArrayList<Instrucao> lista = teste.lerGrafo(caminho);
		ArrayList<Instrucao> beta = teste.buscarInstrucoesIndependentes(lista);
		teste.simularCiclos(beta);
		teste.salvarGrafo(beta);
		//*
		beta = teste.buscarInstrucoesSemConflito(beta);
		beta = teste.organizarInstrucoes(beta);
		teste.salvarGrafo(beta);
		teste.show(beta);
		/**/
	}
}
