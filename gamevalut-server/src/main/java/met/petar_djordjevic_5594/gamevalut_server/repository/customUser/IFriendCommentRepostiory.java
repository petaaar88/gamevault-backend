package met.petar_djordjevic_5594.gamevalut_server.repository.customUser;

import met.petar_djordjevic_5594.gamevalut_server.model.customUser.FriendComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IFriendCommentRepostiory extends JpaRepository<FriendComment, Integer> {

    @Query(value = "SELECT * FROM friend_comment WHERE users_friends_id = :friendshipId ", nativeQuery = true)
    Optional<FriendComment> findByFriendshipId(@Param("friendshipId")Integer friendshipId);

    @Query(value = "SELECT COUNT(friend_comment.id) FROM friend_comment JOIN users_friends ON friend_comment.users_friends_id = users_friends.id WHERE users_friends.user_id1 = :userId", nativeQuery = true)
    Long countFriendComments(@Param("userId")Integer userId);

    @Query(value = "select friend_comment.* from friend_comment JOIN users_friends ON friend_comment.users_friends_id = users_friends.id WHERE users_friends.user_id1 = :userId LIMIT :limit OFFSET :offset ", nativeQuery = true)
    Optional<List<FriendComment>> findCommentsAndPaginate(@Param("userId")Integer userId, @Param("limit") Integer limit, @Param("offset") Integer offset);
}
