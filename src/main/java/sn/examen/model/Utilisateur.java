package sn.examen.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")  // Attention : 'user' est un mot reserve en SQL !
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nom;
    private String prenom;
    private String telephone;
    private String email;

    @Column(unique = true, nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;

    public Utilisateur() {
    }

    // Getters et Setters
    public int getId()              { return id; }
    public void setId(int id)       { this.id = id; }
    public String getNom()          { return nom; }
    public void setNom(String n)    { this.nom = n; }
    public String getPrenom()       { return prenom; }
    public void setPrenom(String p) { this.prenom = p; }
    public String getTelephone()    { return telephone; }
    public void setTelephone(String t) { this.telephone = t; }
    public String getEmail()        { return email; }
    public void setEmail(String e)  { this.email = e; }
    public String getLogin()        { return login; }
    public void setLogin(String l)  { this.login = l; }
    public String getPassword()     { return password; }
    public void setPassword(String p){ this.password = p; }
}

