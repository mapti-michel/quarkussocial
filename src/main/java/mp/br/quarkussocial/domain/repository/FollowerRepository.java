package mp.br.quarkussocial.domain.repository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import mp.br.quarkussocial.domain.model.Follower;
import mp.br.quarkussocial.domain.model.User;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class FollowerRepository implements PanacheRepository<Follower> {

    public boolean followers(User follower, User user){
/*
        Map<String, Object> params = new HashMap<>();
        params.put("follower", follower);
        params.put("user", user);

        // Or
*/
        var params = Parameters.with("follower", follower).and("user", user).map();
        PanacheQuery<Follower> query = find("follower =:follower and user =:user ", params);
        Optional<Follower> result = query.firstResultOptional();

        return result.isPresent();
    }

    public List<Follower> findByUser(Long userId){

        PanacheQuery<Follower> query = find("user.id", userId);
        return query.list();
    }

    public void deleteByFollowerAndUser(Long followerId, Long userId) {
        var params = Parameters.with("userId", userId)
                        .and("followerId", followerId)
                                .map();

        delete("follower.id =:followerId and user.id =:userId", params);

    }
}
