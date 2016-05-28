package Interface;

import Funcoes.Funcoes;

public class Main 
{
	public static void main(String[] args) 
	{
		Funcoes teste = new Funcoes();
		String caminho = "src/grafo.txt";
		String saida = "src/instruções reordenadas.txt";
		teste.reordenar(caminho);
	}
}
