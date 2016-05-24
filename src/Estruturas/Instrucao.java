package Estruturas;

public class Instrucao 
{
	//Informações fornecidas no grafo
	public String numeroDaInstrucao;
	public String operacao;
	public String destino;
	public String operando1;
	public String operando2;
	public String dependentes;
	public String descricao;
	public int cicloInicial;
	public boolean dependenciaFalsa;
	
	//construtor padrão
	public Instrucao()
	{
		numeroDaInstrucao = "";
		operacao = "";
		destino = "";
		operando1 = "";
		operando2 = "";
		dependentes = "";
		descricao = "";
		cicloInicial = 0;
		dependenciaFalsa = false;
	}
}
