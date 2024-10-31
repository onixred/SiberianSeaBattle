package ru.cbr.siberian.feature.first.sea.battle.match;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import ru.cbr.siberian.feature.first.sea.battle.common.AbstractDao;
import ru.cbr.siberian.feature.first.sea.battle.player.PlayerDao;


import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "matches", schema = "siberian_sea_battle")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MatchDao extends AbstractDao {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "size_grid", nullable = false)
    private Integer sizeGrid;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false)
    private PlayerDao owner;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "opponent_id", referencedColumnName = "id")
    private PlayerDao opponent;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "winner_id", referencedColumnName = "id")
    private PlayerDao winner;

    @JoinColumn(name = "status")
    private MatchStatus status;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        MatchDao matchDao = (MatchDao) o;
        return getId() != null && Objects.equals(getId(), matchDao.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
