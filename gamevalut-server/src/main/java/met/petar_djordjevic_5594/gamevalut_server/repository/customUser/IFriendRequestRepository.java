package met.petar_djordjevic_5594.gamevalut_server.repository.customUser;

import met.petar_djordjevic_5594.gamevalut_server.model.customUser.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IFriendRequestRepository extends JpaRepository<FriendRequest, Integer> {
}
