package Estruturas;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Registrador 
{
	private BooleanProperty escrito;
	private BooleanProperty lido;
	private StringProperty nome;
	
	public Registrador()
	{
		this.nome = new SimpleStringProperty("");
		this.escrito = new SimpleBooleanProperty(false);
		this.lido = new SimpleBooleanProperty(false);
	}

	public boolean isEscrito() 
	{
		return this.escrito.get();
	}

	public void setEscrito(boolean escrito) 
	{
		this.escrito.set(escrito);
	}

	public boolean isLido() 
	{
		return this.lido.get();
	}

	public void setLido(boolean lido) 
	{
		this.lido.set(lido);
	}

	public String getNome() 
	{
		return this.nome.get();
	}

	public void setNome(String nome) 
	{
		this.nome.set(nome);
	}
	
	public StringProperty nomeProperty() 
    {
        return this.nome;
    }
}
