package met.petar_djordjevic_5594.gamevalut_server.repository.country;

import met.petar_djordjevic_5594.gamevalut_server.model.country.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ICountryRepository extends JpaRepository<Country, Integer> {
}
