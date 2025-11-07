package smartcity.util;

import smartcity.dao.JpaUtil;
import smartcity.model.*;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Random;


/**
 * Générateur de données
 * ---------------------
 * Utilitaire de génération massive de données de test pour Smart City.
 * Création automatique de capteurs + mesures avec optimisation batch.
 *
 */
public class DataGenerator {
    public static void generateData() {
        // Vérifier que les tables existent (Hibernate les créera automatiquement)
        JpaUtil.ensureTablesExist();
        
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            
            System.out.println("🔧 Génération des données...");

            // Création de plusieurs capteurs
            String[] types = {"Pollution", "Temperature", "Humidite", "Bruit"};
            String[] zones = {"Centre-Ville", "Nord", "Sud", "Est", "Ouest"};
            Random rnd = new Random();
            
            for (String type : types) {
                for (String zone : zones) {
                    if (rnd.nextBoolean()) { // On ne crée pas tous les capteurs possibles
                        Sensor s = new Sensor("Capteur_" + type + "_" + zone, type, zone, true);
                        em.persist(s);
                        
                        // Génération de mesures pour ce capteur
                        int nMeasures = 100 + rnd.nextInt(400); // Entre 100 et 500 mesures par capteur
                        for (int i = 0; i < nMeasures; i++) {
                            Measurement m = new Measurement(
                                LocalDateTime.now().minusMinutes(rnd.nextInt(10000)),
                                10 + rnd.nextDouble() * 90,
                                s
                            );
                            em.persist(m);
                            if (i % 50 == 0) { 
                                em.flush();
                                em.clear();
                            }
                        }
                    }
                }
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
    
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        generateData();
        System.out.println("✅ Données générées en " + (System.currentTimeMillis() - start) + " ms");
    }
}
