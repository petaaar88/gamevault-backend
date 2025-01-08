package met.petar_djordjevic_5594.gamevalut_server.repository.customUser;

import met.petar_djordjevic_5594.gamevalut_server.model.customUser.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ICustomUserRepository extends JpaRepository<CustomUser, Integer> {
}
