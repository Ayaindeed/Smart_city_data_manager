package smartcity.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entité Measurement
 * -------------------
 * Représente une mesure enregistrée par un capteur urbain
 * (pollution, bruit, trafic, etc.)
 * Chaque mesure est associée à un Sensor (relation ManyToOne).

 */
@Entity
@Table(name = "measurement")
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private double value;

    @Column(name = "type_measure", length = 100)
    private String typeMeasure;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensor_id", nullable = false)
    private Sensor sensor;

    // ----- Constructeurs -----
    public Measurement() {}

    public Measurement(LocalDateTime timestamp, double value, String typeMeasure, Sensor sensor) {
        this.timestamp = timestamp;
        this.value = value;
        this.typeMeasure = typeMeasure;
        this.sensor = sensor;
    }

    public Measurement(LocalDateTime timestamp, double value, Sensor sensor) {
        this(timestamp, value, "Generic", sensor);
    }
    // ----- Getters / Setters -----
    public Long getId() {
        return id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getTypeMeasure() {
        return typeMeasure;
    }

    public void setTypeMeasure(String typeMeasure) {
        this.typeMeasure = typeMeasure;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    // ----- Helper methods pour identification source -----
    /**
     * Détermine la source de la mesure basée sur son capteur
     * @return "API" | "Généré" | "Manuel"
     */
    public String getSource() {
        if (sensor != null) {
            return sensor.getSource();
        }
        return "Manuel";
    }
    
    /**
     * Badge coloré pour l'affichage de la source
     */
    public String getSourceBadge() {
        if (sensor != null) {
            return sensor.getSourceBadge();
        }
        return "👤 Manuel";
    }

    // ----- toString (utile pour debug / logs) -----
    @Override
    public String toString() {
        return "Measurement{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", value=" + value +
                ", typeMeasure='" + typeMeasure + '\'' +
                ", sensor=" + (sensor != null ? sensor.getId() : null) +
                ", source=" + getSource() +
                '}';
    }
}
