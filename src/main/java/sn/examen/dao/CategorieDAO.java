package sn.examen.dao;

import sn.examen.model.Categorie;
import jakarta.persistence.*;
import java.util.*;

public class CategorieDAO {

    private EntityManagerFactory emf = JPAUtil.getEMF();

    // Lire toutes les categories
    public List<Categorie> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT c FROM Categorie c ORDER BY c.libelle",
                    Categorie.class).getResultList();
        } finally { em.close(); }
    }

    // Creer une categorie
    public void save(Categorie c) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(c);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback(); throw e;
        } finally { em.close(); }
    }

    // Modifier une categorie
    public void update(Categorie c) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(c); // merge = mettre a jour
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback(); throw e;
        } finally { em.close(); }
    }

    // Supprimer une categorie par ID
    public void delete(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Categorie c = em.find(Categorie.class, id);
            if (c != null) em.remove(c);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback(); throw e;
        } finally { em.close(); }
    }

    // Pour le PieChart : nombre de produits par categorie
    public Map<String, Long> countProduitsParCategorie() {
        EntityManager em = emf.createEntityManager();
        try {
            List<Object[]> results = em.createQuery(
                            "SELECT c.libelle, COUNT(p) FROM Produit p " +
                                    "JOIN p.categorie c GROUP BY c.libelle")
                    .getResultList();
            Map<String, Long> map = new LinkedHashMap<>();
            for (Object[] row : results) {
                map.put((String) row[0], (Long) row[1]);
            }
            return map;
        } finally { em.close(); }
    }
}

