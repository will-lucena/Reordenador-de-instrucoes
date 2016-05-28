package Estruturas;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Instrucao 
{
	//Informações fornecidas no grafo
	private StringProperty numeroDaInstrucao;
	private StringProperty operacao;
	private ObjectProperty<Registrador> destino;
	private ObjectProperty<Registrador> operando1;
	private ObjectProperty<Registrador> operando2;
	private StringProperty dependentes;
	private StringProperty descricao;
	private IntegerProperty cicloInicial;
	private BooleanProperty dependenciaFalsa;
	
	//construtor padrão
	public Instrucao()
	{
		numeroDaInstrucao = new SimpleStringProperty("");
		operacao = new SimpleStringProperty("");
		destino = new SimpleObjectProperty<Registrador>(new Registrador());
		operando1 = new SimpleObjectProperty<Registrador>(new Registrador());
		operando2 = new SimpleObjectProperty<Registrador>(new Registrador());
		dependentes = new SimpleStringProperty("");
		descricao = new SimpleStringProperty("");
		cicloInicial = new SimpleIntegerProperty(0);
		dependenciaFalsa = new SimpleBooleanProperty(false);
	}
	    
    public String getNumeroDaInstrucao() 
	{
        return numeroDaInstrucao.get();
    }

    public void setNumeroDaInstrucao(String numeroDaInstrucao) 
    {
        this.numeroDaInstrucao.set(numeroDaInstrucao);
    }

    public StringProperty numeroDaInstrucaoProperty() 
    {
        return numeroDaInstrucao;
    }
    
    public String getOperacao() 
	{
        return operacao.get();
    }

    public void setOperacao(String operacao) 
    {
        this.operacao.set(operacao);
    }

    public StringProperty operacaoProperty() 
    {
        return operacao;
    }
    
    public Registrador getDestino() {
        return destino.get();
    }

    public void setDestino(Registrador destino) {
        this.destino.set(destino);
    }

    public ObjectProperty<Registrador> destinoProperty() {
        return destino;
    }
    
    public Registrador getOperando1() {
        return operando1.get();
    }

    public void setOperando1(Registrador operando1) {
        this.operando1.set(operando1);
    }

    public ObjectProperty<Registrador> operando1Property() {
        return operando1;
    }
    
    public Registrador getOperando2() {
        return operando2.get();
    }

    public void setOperando2(Registrador operando2) {
        this.operando2.set(operando2);
    }

    public ObjectProperty<Registrador> operando2Property() {
        return operando2;
    }
    
    public String getDependentes() 
	{
        return dependentes.get();
    }

    public void setDependentes(String dependentes) 
    {
        this.dependentes.set(dependentes);
    }

    public StringProperty dependentesProperty() 
    {
        return dependentes;
    }
    
    public String getDescricao() 
	{
        return descricao.get();
    }

    public void setDescricao(String descricao) 
    {
        this.descricao.set(descricao);
    }

    public StringProperty descricaoProperty() 
    {
        return descricao;
    }
    
    public Integer getCicloInicial() 
	{
        return cicloInicial.get();
    }

    public void setCicloInicial(Integer cicloInicial) 
    {
        this.cicloInicial.set(cicloInicial);
    }

    public IntegerProperty cicloInicialProperty() 
    {
        return cicloInicial;
    }
    
    public Boolean getDependenciaFalsa() 
	{
        return dependenciaFalsa.get();
    }

    public void setDependenciaFalsa(Boolean dependenciaFalsa) 
    {
        this.dependenciaFalsa.set(dependenciaFalsa);
    }

    public BooleanProperty getDependenciaFalsaProperty() 
    {
        return dependenciaFalsa;
    }
}
