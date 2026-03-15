package sn.examen.dao;

import sn.examen.model.Produit;
import jakarta.persistence.*;
import java.util.*;

public class ProduitDAO {

    private EntityManagerFactory emf = JPAUtil.getEMF();

    // Lire tous les produits
    public List<Produit> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT p FROM Produit p JOIN FETCH p.categorie ORDER BY p.libelle",
                    Produit.class).getResultList();
        } finally { em.close(); }
    }

    // Recherche par libelle (barre de recherche)
    public List<Produit> findByLibelle(String libelle) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Produit p JOIN FETCH p.categorie " +
                                    "WHERE LOWER(p.libelle) LIKE LOWER(:l) ORDER BY p.libelle",
                            Produit.class)
                    .setParameter("l", "%" + libelle + "%")
                    .getResultList();
        } finally { em.close(); }
    }

    // Produits dont la quantite est inferieure a 5 (alerte stock)
    public List<Produit> findLowStock() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT p FROM Produit p WHERE p.quantite < 5",
                    Produit.class).getResultList();
        } finally { em.close(); }
    }

    // Creer un produit
    public void save(Produit p) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(p);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback(); throw e;
        } finally { em.close(); }
    }

    // Modifier un produit
    public void update(Produit p) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(p);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback(); throw e;
        } finally { em.close(); }
    }

    // Supprimer un produit
    public void delete(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Produit p = em.find(Produit.class, id);
            if (p != null) em.remove(p);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback(); throw e;
        } finally { em.close(); }
    }

    // Pour le BarChart : nombre de produits ajoutes par mois en 2024
    public Map<Integer, Long> countByMonthIn2024() {
        EntityManager em = emf.createEntityManager();
        try {
            // FUNCTION('EXTRACT', ...) est la facon Hibernate de faire EXTRACT
            List<Object[]> results = em.createQuery(
                            "SELECT MONTH(p.dateAjout), COUNT(p) FROM Produit p " +
                                    "WHERE YEAR(p.dateAjout) = 2024 GROUP BY MONTH(p.dateAjout)")
                    .getResultList();
            Map<Integer, Long> map = new TreeMap<>();
            for (Object[] row : results) {
                map.put(((Number) row[0]).intValue(), (Long) row[1]);
            }
            return map;
        } finally { em.close(); }
    }
}

