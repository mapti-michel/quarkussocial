package mp.br.quarkussocial.domain.rest;

import java.io.Serializable;
import java.util.Objects;

public class UserRequest implements Serializable {

    private String nome;
    private Integer idade;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

}
