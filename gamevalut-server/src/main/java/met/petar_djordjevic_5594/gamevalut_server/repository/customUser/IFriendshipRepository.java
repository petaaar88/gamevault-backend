package met.petar_djordjevic_5594.gamevalut_server.repository.customUser;

import met.petar_djordjevic_5594.gamevalut_server.model.customUser.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IFriendshipRepository extends JpaRepository<Friendship, Integer> {

    @Query(value = "SELECT * FROM users_friends " +
            "WHERE (users_friends.user_id = :user1 AND users_friends.user_id1 = :user2) " +
            "   OR (users_friends.user_id = :user2 AND users_friends.user_id1 = :user1)", nativeQuery = true)
    Optional<Friendship> findByFriendsId(@Param("user1") Integer user1, @Param("user2") Integer user2);

}
