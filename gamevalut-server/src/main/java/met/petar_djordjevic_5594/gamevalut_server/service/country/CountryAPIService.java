package met.petar_djordjevic_5594.gamevalut_server.service.country;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.http.client.reactive.JdkClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class CountryAPIService {
//    @Autowired
//    private Environment env;
//    private RestClient restClient;
//
//    public CountryAPIService(RestClient.Builder builder) {
//
//        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory();
//        this.restClient = builder
//                .baseUrl(env.getProperty("country-api.api.url"))
//                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + env.getProperty("country-api.api.key"))
//                .requestFactory(requestFactory)
//                .build();
//    }
//
//    public String getAllCountries() {
//        return restClient.get()
//                .uri("/all")
//                .retrieve()
//                .body(String.class);
//
//    }

}
