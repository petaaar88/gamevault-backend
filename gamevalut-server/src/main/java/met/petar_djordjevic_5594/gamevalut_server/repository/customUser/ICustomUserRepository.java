package met.petar_djordjevic_5594.gamevalut_server.repository.customUser;

import met.petar_djordjevic_5594.gamevalut_server.model.customUser.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;


public interface ICustomUserRepository extends JpaRepository<CustomUser, Integer> {


    @Query(value = "SELECT user.* FROM users_friends JOIN user ON user.id = users_friends.user_id1 WHERE user_id = :userId\n ", nativeQuery = true)
    Optional<List<CustomUser>> findAllFriends(@Param("userId")Integer userId);

    @Query(value = "SELECT * FROM user WHERE username LIKE %:username% ", nativeQuery = true)
    Optional<List<CustomUser>> findByUsername(@Param("username")String username);
}
