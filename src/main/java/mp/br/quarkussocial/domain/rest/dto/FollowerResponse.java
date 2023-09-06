package mp.br.quarkussocial.domain.rest.dto;


import lombok.Data;
import mp.br.quarkussocial.domain.model.Follower;

@Data
public class FollowerResponse {

    private Long id;
    private String nome;

    public FollowerResponse(){

    }

    public FollowerResponse(Follower follower){
        this(follower.getId(), follower.getFollower().getNome());
    }

    public FollowerResponse(Long id, String nome){
        this.id = id;
        this.nome = nome;
    }

}
