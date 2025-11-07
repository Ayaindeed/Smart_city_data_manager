package smartcity.dao;

import smartcity.model.Sensor;
import jakarta.persistence.EntityManager;
import java.util.List;

public class SensorDao {
    public void save(Sensor s) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(s);
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

    public void update(Sensor s) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(s);
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

    public void delete(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Sensor s = em.find(Sensor.class, id);
            if (s != null) {
                em.remove(s);
            }
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

    public void toggleActive(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Sensor s = em.find(Sensor.class, id);
            if (s != null) {
                s.setActive(!s.isActive());
            }
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

    public List<Sensor> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT s FROM Sensor s", Sensor.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Sensor findById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.find(Sensor.class, id);
        } finally {
            em.close();
        }
    }

    public List<Object[]> getSensorStats() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            // Requête simple qui fonctionne avec H2
            return em.createQuery(
                "SELECT s.type, s.zone, " +
                "(SELECT COUNT(m1) FROM Measurement m1 WHERE m1.sensor = s) as nbMeasures, " +
                "(SELECT MIN(m2.value) FROM Measurement m2 WHERE m2.sensor = s) as minVal, " +
                "(SELECT MAX(m3.value) FROM Measurement m3 WHERE m3.sensor = s) as maxVal, " +
                "(SELECT AVG(m4.value) FROM Measurement m4 WHERE m4.sensor = s) as avgVal " +
                "FROM Sensor s " +
                "ORDER BY s.type, s.zone", Object[].class)
            .getResultList();
        } finally {
            em.close();
        }
    }
}
