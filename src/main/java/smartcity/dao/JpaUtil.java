package smartcity.dao;

import jakarta.persistence.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utilitaire JPA
 * --------------
 * Gestionnaire centralisé des connexions JPA/Hibernate.
 * Fournit EntityManagerFactory et gestion des transactions RESOURCE_LOCAL.
 *
 */
public class JpaUtil {
    private static final Logger logger = Logger.getLogger(JpaUtil.class.getName());
    private static EntityManagerFactory emf;
    
    static {
        try {
            emf = Persistence.createEntityManagerFactory("smartCityPU");
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (emf != null && emf.isOpen()) {
                    emf.close();
                }
            }));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur lors de l'initialisation de JPA", e);
            throw new ExceptionInInitializerError(e);
        }
    }

    public static EntityManager getEntityManager() {
        if (emf == null || !emf.isOpen()) {
            emf = Persistence.createEntityManagerFactory("smartCityPU");
        }
        return emf.createEntityManager();
    }
    
    // Méthode pour tester la connexion et forcer la création des tables
    public static void ensureTablesExist() {
        EntityManager em = getEntityManager();
        try {
            // Simple test pour s'assurer que les tables existent
            em.createQuery("SELECT COUNT(s) FROM Sensor s").getSingleResult();
            logger.info("✅ Tables vérifiées avec succès");
        } catch (Exception e) {
            logger.info("ℹ️ Première initialisation des tables en cours...");
        } finally {
            em.close();
        }
    }

    public static void close() {
        try {
            if (emf != null && emf.isOpen()) {
                emf.close();
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Erreur lors de la fermeture de l'EntityManagerFactory", e);
        }
    }
    
    // Méthode utilitaire pour les transactions RESOURCE_LOCAL
    public static void withTransaction(TransactionRunnable action) throws Exception {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            action.run(em);
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

    @FunctionalInterface
    public interface TransactionRunnable {
        void run(EntityManager em) throws Exception;
    }
}
