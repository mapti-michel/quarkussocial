package mp.br.quarkussocial.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(schema = "quarkussocial", name = "tbluser") //Se eu deixar o table vazio, ele entenderá que o nome da tabela é User
public class User extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome")
    private String nome;
    @Column(name = "idade")
    private Integer idade;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) && nome.equals(user.nome) && idade.equals(user.idade);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, idade);
    }
}
