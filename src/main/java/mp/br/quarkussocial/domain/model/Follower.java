package mp.br.quarkussocial.domain.model;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(schema = "quarkussocial", name = "tblfollowers")
@Data
public class Follower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "follow_id")
    private User follower;



}
