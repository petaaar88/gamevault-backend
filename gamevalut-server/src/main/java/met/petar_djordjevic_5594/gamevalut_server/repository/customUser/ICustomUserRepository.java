package met.petar_djordjevic_5594.gamevalut_server.repository.customUser;

import met.petar_djordjevic_5594.gamevalut_server.model.customUser.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ICustomUserRepository extends JpaRepository<CustomUser, Integer> {

    @Query(value = "SELECT \n" +
            "    u.* -- Selektujemo sve kolone iz tabele `user`\n" +
            "FROM \n" +
            "    users_friends uf\n" +
            "JOIN \n" +
            "    user u ON (uf.user_id = u.id OR uf.user_id1 = u.id)\n" +
            "WHERE \n" +
            "    (uf.user_id = :userId OR uf.user_id1 = :userId)\n" +
            "    AND u.id != :userId\n" +
            "    AND uf.status = 'accepted';\n", nativeQuery = true)
    Optional<List<CustomUser>> findAllFriends(Integer userId);
}
