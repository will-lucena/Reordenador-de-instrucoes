package Estruturas;

public class Instrucao 
{
	//Informações fornecidas no grafo
	public String numeroDaInstrucao;
	public String operacao;
	public Registrador destino;
	public Registrador operando1;
	public Registrador operando2;
	public String dependentes;
	public String descricao;
	public int cicloInicial;
	public boolean dependenciaFalsa;
	
	//construtor padrão
	public Instrucao()
	{
		numeroDaInstrucao = "";
		operacao = "";
		destino = new Registrador();
		operando1 = new Registrador();
		operando2 = new Registrador();
		dependentes = "";
		descricao = "";
		cicloInicial = 0;
		dependenciaFalsa = false;
	}
}
