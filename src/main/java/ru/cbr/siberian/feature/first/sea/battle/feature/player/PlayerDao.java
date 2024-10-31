package ru.cbr.siberian.feature.first.sea.battle.feature.player;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import ru.cbr.siberian.sea.battle.dao.AbstractDao;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "players", schema = "siberian_sea_battle")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDao extends AbstractDao {

    @Id
    @GeneratedValue
    @Column(name = "id", unique = true)
    private UUID id;

    @Column(name = "chanel_id", unique = true)
    private UUID chanelId;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "password")
    private String password;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        PlayerDao playerDao = (PlayerDao) o;
        return getId() != null && Objects.equals(getId(), playerDao.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
