package met.petar_djordjevic_5594.gamevalut_server.repository.customUser;

import met.petar_djordjevic_5594.gamevalut_server.model.customUser.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IFriendshipRepository extends JpaRepository<Friendship, Integer> {

    @Query(value = "SELECT * FROM users_friends " +
            "WHERE user_id = :userId AND user_id1 = :friendId ", nativeQuery = true)
    Optional<Friendship> findByFriendsId(@Param("userId") Integer userId, @Param("friendId") Integer friendId);

}
