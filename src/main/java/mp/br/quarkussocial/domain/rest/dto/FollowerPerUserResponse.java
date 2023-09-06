package mp.br.quarkussocial.domain.rest.dto;

import lombok.Data;
import mp.br.quarkussocial.domain.model.Follower;

import java.util.List;

@Data
public class FollowerPerUserResponse {

    private Integer followersCount;
    private List<FollowerResponse> content;

}
