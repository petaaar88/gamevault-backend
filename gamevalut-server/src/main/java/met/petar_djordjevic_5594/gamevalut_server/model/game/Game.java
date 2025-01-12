package met.petar_djordjevic_5594.gamevalut_server.model.game;

import jakarta.persistence.*;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "game")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(
            name = "deployment_date",
            nullable = false
    )
    private LocalDate deploymentDate;
    @Column(
            name = "description",
            nullable = false
    )
    private String description;
    @Column(
            name = "developer",
            nullable = false
    )
    private String developer;
    @Column(
            name = "download_url",
            nullable = false
    )
    private String downloadUrl;
    @Column( name = "number_of_acquisitions" )
    private BigInteger numberOfAcquisitions;
    @Column( name = "number_of_reviews" )
    private BigInteger numberOfReviews;
    @Enumerated(EnumType.STRING)
    @Column( name = "overall_rating" )
    private GameRating overallRating;
    @Column( name = "overall_rating_percentage" )
    private Integer overallRatingPercentage;
    @Column(
            name = "release_date",
            nullable = false
    )
    private LocalDate releaseDate;
    @Column(
            name = "title",
            nullable = false
    )
    private String title;
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<GameSystemRequirements> systemRequirements;
    @ManyToMany(mappedBy = "games", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Genre> genres = new ArrayList<>();

    public Game() {
    }

    public Game(String description, String developer, String downloadUrl, LocalDate releaseDate, String title) {
        this.description = description;
        this.developer = developer;
        this.downloadUrl = downloadUrl;
        this.releaseDate = releaseDate;
        this.title = title;
    }

    public Game(LocalDate deploymentDate, String description, String developer, String downloadUrl, BigInteger numberOfAcquisitions, BigInteger numberOfReviews, GameRating overallRating, Integer overallRatingPercentage, LocalDate releaseDate, String title) {
        this.deploymentDate = deploymentDate;
        this.description = description;
        this.developer = developer;
        this.downloadUrl = downloadUrl;
        this.numberOfAcquisitions = numberOfAcquisitions;
        this.numberOfReviews = numberOfReviews;
        this.overallRating = overallRating;
        this.overallRatingPercentage = overallRatingPercentage;
        this.releaseDate = releaseDate;
        this.title = title;
    }

    public List<GameSystemRequirements> getSystemRequirements() {
        return systemRequirements;
    }

    public void setSystemRequirements(List<GameSystemRequirements> systemRequirements) {
        this.systemRequirements = systemRequirements;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public BigInteger getNumberOfReviews() {
        return numberOfReviews;
    }

    public void setNumberOfReviews(BigInteger numberOfReviews) {
        this.numberOfReviews = numberOfReviews;
    }

    public BigInteger getNumberOfAcquisitions() {
        return numberOfAcquisitions;
    }

    public void setNumberOfAcquisitions(BigInteger numberOfAcquisitions) {
        this.numberOfAcquisitions = numberOfAcquisitions;
    }
}
