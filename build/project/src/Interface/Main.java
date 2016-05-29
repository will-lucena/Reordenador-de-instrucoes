package Interface;

import Funcoes.Funcoes;

public class Main 
{
	public static void main(String[] args) 
	{
		Funcoes teste = new Funcoes();
		String caminho = "src/grafo.txt";
		teste.reordenar(caminho);
	}
}
