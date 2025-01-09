package met.petar_djordjevic_5594.gamevalut_server.service.country;

import met.petar_djordjevic_5594.gamevalut_server.model.country.Country;
import met.petar_djordjevic_5594.gamevalut_server.repository.country.ICountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CountryService {
    @Autowired
    ICountryRepository countryRepository;

    public CountryService() {
    }

    public void addCountry(Country newCountry){
        countryRepository.save(newCountry);
    }

    public Optional<Country> findById(Integer id){
        return countryRepository.findById(id);
    }

}
