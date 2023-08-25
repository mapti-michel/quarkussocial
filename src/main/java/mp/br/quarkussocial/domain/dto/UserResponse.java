package mp.br.quarkussocial.domain.dto;

import io.vertx.core.eventbus.impl.clustered.Serializer;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@Builder
public class UserResponse {

    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "idade", nullable = false)
    private int idade;

}
