package mp.br.quarkussocial.domain.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import mp.br.quarkussocial.domain.model.User;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {


}
