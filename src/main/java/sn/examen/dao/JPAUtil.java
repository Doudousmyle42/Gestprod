package sn.examen.dao;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {

    // Instance unique (Singleton Pattern)
    private static EntityManagerFactory emf;

    // Methode pour obtenir la factory
    public static EntityManagerFactory getEMF() {
        if (emf == null) {
            // 'examPU' doit correspondre au nom dans persistence.xml
            emf = Persistence.createEntityManagerFactory("examPU");
        }
        return emf;
    }

    // Fermer la connexion proprement
    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
