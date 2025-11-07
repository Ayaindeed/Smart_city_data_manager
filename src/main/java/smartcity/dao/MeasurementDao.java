package smartcity.dao;

import smartcity.model.Measurement;
import jakarta.persistence.EntityManager;
import java.util.List;

/**
 * DAO Measurement
 * ---------------
 * Couche d'accès aux données pour l'entité Measurement.
 * Statistiques analytiques (AVG/MIN/MAX) avec GROUP BY temporel et par capteur.
 *
 * Dépend de : JpaUtil, Measurement, Sensor
 */
public class MeasurementDao {
    public void save(Measurement m) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(m);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Object[]> getStatsByType() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                "SELECT s.type, " +
                "       COUNT(m), " +
                "       AVG(m.value), " +
                "       MIN(m.value), " +
                "       MAX(m.value), " +
                "       s.zone " +
                "FROM Measurement m " +
                "JOIN m.sensor s " +
                "GROUP BY s.type, s.zone " +
                "ORDER BY s.type, s.zone", Object[].class)
            .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Object[]> getStatsByTimeRange() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            // Requête simplifiée pour H2 - statistiques par jour
            return em.createQuery(
                "SELECT YEAR(m.timestamp), MONTH(m.timestamp), DAY(m.timestamp), " +
                "       COUNT(m), " +
                "       AVG(m.value), " +
                "       MIN(m.value), " +
                "       MAX(m.value) " +
                "FROM Measurement m " +
                "GROUP BY YEAR(m.timestamp), MONTH(m.timestamp), DAY(m.timestamp) " +
                "ORDER BY YEAR(m.timestamp) DESC, MONTH(m.timestamp) DESC, DAY(m.timestamp) DESC", Object[].class)
            .setMaxResults(10)  // Limiter aux 10 derniers jours
            .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Object[]> getHourlyStats(String date) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                "SELECT HOUR(m.timestamp), " +
                "       COUNT(m), " +
                "       AVG(m.value), " +
                "       MIN(m.value), " +
                "       MAX(m.value) " +
                "FROM Measurement m " +
                "WHERE DATE(m.timestamp) = :date " +
                "GROUP BY HOUR(m.timestamp) " +
                "ORDER BY HOUR(m.timestamp)", Object[].class)
            .setParameter("date", date)
            .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Measurement> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT m FROM Measurement m JOIN FETCH m.sensor",
                    Measurement.class
            ).getResultList();
        } finally {
            em.close();
        }
    }
}
