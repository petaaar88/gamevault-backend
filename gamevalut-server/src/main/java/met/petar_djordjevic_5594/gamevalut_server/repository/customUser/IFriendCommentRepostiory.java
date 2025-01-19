package met.petar_djordjevic_5594.gamevalut_server.repository.customUser;

import met.petar_djordjevic_5594.gamevalut_server.model.customUser.FriendComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IFriendCommentRepostiory extends JpaRepository<FriendComment, Integer> {

    @Query(value = "SELECT * FROM friend_comment WHERE users_friends_id = :friendshipId ", nativeQuery = true)
    Optional<FriendComment> findByFriendshipId(@Param("friendshipId")Integer friendshipId);

}
