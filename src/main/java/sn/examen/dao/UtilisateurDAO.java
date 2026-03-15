package sn.examen.dao;

import sn.examen.model.Utilisateur;
import jakarta.persistence.*;
import java.util.List;

public class UtilisateurDAO {

    private EntityManagerFactory emf = JPAUtil.getEMF();

    // Verifier login/password (pour la connexion)
    public Utilisateur findByLoginPassword(String login, String password) {
        EntityManager em = emf.createEntityManager();
        try {
            // JPQL : SELECT u FROM Utilisateur u WHERE ...
            return em.createQuery(
                            "SELECT u FROM Utilisateur u WHERE u.login = :l AND u.password = :p",
                            Utilisateur.class)
                    .setParameter("l", login)
                    .setParameter("p", password)
                    .getSingleResult(); // Leve NoResultException si pas trouve
        } catch (NoResultException e) {
            return null; // Retourne null si utilisateur non trouve
        } finally {
            em.close();
        }
    }

    // Verifier si un login existe deja
    public boolean loginExists(String login) {
        EntityManager em = emf.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(u) FROM Utilisateur u WHERE u.login = :l", Long.class)
                    .setParameter("l", login)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    // Enregistrer un nouvel utilisateur
    public void save(Utilisateur Utilisateur) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(Utilisateur);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}

