package smartcity.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity Sensor
 * -------------
 * Represents an IoT sensor in a Smart City environment.
 * Each Sensor can produce many Measurements.
 *
 * Example: a sensor of type "Pollution" or "Traffic" located in a city zone.
 */
@Entity
@Table(name = "sensor")
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, length = 80)
    private String type;

    @Column(length = 120)
    private String zone;

    @Column(nullable = false)
    private boolean active = true;

    @OneToMany(mappedBy = "sensor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Measurement> measurements = new ArrayList<>();

    // ----- Constructors -----
    public Sensor() {}

    public Sensor(String name, String type, String zone, boolean active) {
        this.name = name;
        this.type = type;
        this.zone = zone;
        this.active = active;
    }

    // ----- Getters / Setters -----
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }

    // ----- Helper methods -----
    public void addMeasurement(Measurement m) {
        measurements.add(m);
        m.setSensor(this);
    }

    public void removeMeasurement(Measurement m) {
        measurements.remove(m);
        m.setSensor(null);
    }

    // ----- Helper methods pour identification source -----
    /**
     * Détermine la source du capteur basée sur son nom/type
     * @return "API" | "Généré" | "Manuel"
     */
    public String getSource() {
        if (name != null) {
            if (name.contains("Capteur-") && (type.equals("Météo") || type.equals("Pollution") || type.equals("Bruit"))) {
                return "API";
            } else if (name.contains("Capteur") || name.contains("Batch")) {
                return "Généré";
            }
        }
        return "Manuel";
    }
    
    /**
     * Badge coloré pour l'affichage de la source
     */
    public String getSourceBadge() {
        switch (getSource()) {
            case "API": return "🌐 API";
            case "Généré": return "⚙️ Généré";
            default: return "👤 Manuel";
        }
    }

    // ----- toString() -----
    @Override
    public String toString() {
        return "Sensor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", zone='" + zone + '\'' +
                ", active=" + active +
                ", source=" + getSource() +
                '}';
    }
}
