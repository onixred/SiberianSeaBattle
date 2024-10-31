package ru.cbr.siberian.feature.first.sea.battle.feature.actio_history;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import ru.cbr.siberian.sea.battle.dao.AbstractDao;
import ru.cbr.siberian.sea.battle.dao.MatchDao;
import ru.cbr.siberian.sea.battle.dao.PlayerDao;

import java.util.Objects;
import java.util.UUID;


@Entity
@Table(name = "actions_history", schema = "siberian_sea_battle")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ActionHistoryDao extends AbstractDao {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "match_id", referencedColumnName = "id", nullable = false)
    private MatchDao match;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "player_id", referencedColumnName = "id", nullable = false)
    private PlayerDao player;

    @Column(name = "x")
    private Integer x;

    @Column(name = "y")
    private Integer y;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        ActionHistoryDao that = (ActionHistoryDao) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
