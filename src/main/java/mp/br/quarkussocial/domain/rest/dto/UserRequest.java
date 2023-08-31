package mp.br.quarkussocial.domain.rest.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class UserRequest  implements Serializable {

    @NotBlank(message = "Name is required")
    private String nome;

    @NotNull(message = "Idade is required")
    private Integer idade;

    /*

    @Column(name = "id", nullable = false)
    @Column(name = "nome", nullable = false)
    @Column(name = "idade", nullable = false)


 */

}
