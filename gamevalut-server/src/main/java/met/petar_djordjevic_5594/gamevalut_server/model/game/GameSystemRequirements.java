package met.petar_djordjevic_5594.gamevalut_server.model.game;

import jakarta.persistence.*;

@Entity(name = "game_system_requirements")
public class GameSystemRequirements {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(
            name = "cpu",
            nullable = false
    )
    private String cpu;
    @Column(
            name = "gpu",
            nullable = false
    )
    private String gpu;
    @Column(
            name = "expected_storage",
            nullable = false
    )
    private Integer expectedStorage;
    @Column(
            name = "storage",
            nullable = false
    )
    private Integer storage;
    @Column(
            name = "operating_system",
            nullable = false
    )
    private String operatingSystem;
    @Column(
            name = "ram",
            nullable = false
    )
    private Integer ram;
    @Column(
            name = "type",
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    private GameSystemRequirementsType type;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    public GameSystemRequirements() {
    }

    public GameSystemRequirements(String cpu, String gpu, Integer expectedStorage, Integer storage, String operatingSystem, Integer ram, GameSystemRequirementsType type) {
        this.cpu = cpu;
        this.gpu = gpu;
        this.expectedStorage = expectedStorage;
        this.storage = storage;
        this.operatingSystem = operatingSystem;
        this.ram = ram;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getGpu() {
        return gpu;
    }

    public void setGpu(String gpu) {
        this.gpu = gpu;
    }

    public Integer getExpectedStorage() {
        return expectedStorage;
    }

    public void setExpectedStorage(Integer expectedStorage) {
        this.expectedStorage = expectedStorage;
    }

    public Integer getStorage() {
        return storage;
    }

    public void setStorage(Integer storage) {
        this.storage = storage;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public Integer getRam() {
        return ram;
    }

    public void setRam(Integer ram) {
        this.ram = ram;
    }

    public GameSystemRequirementsType getType() {
        return type;
    }

    public void setType(GameSystemRequirementsType type) {
        this.type = type;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
