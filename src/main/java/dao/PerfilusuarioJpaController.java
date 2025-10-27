/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import dto.Perfilusuario;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import dto.Usuarios;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author ABNER
 */
public class PerfilusuarioJpaController implements Serializable {

    public PerfilusuarioJpaController() {
    }
    public PerfilusuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("com.mycompany_VotacionDigital_war_1.0-SNAPSHOTPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Perfilusuario perfilusuario) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios iduser = perfilusuario.getIduser();
            if (iduser != null) {
                iduser = em.getReference(iduser.getClass(), iduser.getIduser());
                perfilusuario.setIduser(iduser);
            }
            em.persist(perfilusuario);
            if (iduser != null) {
                iduser.getPerfilusuarioList().add(perfilusuario);
                iduser = em.merge(iduser);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Perfilusuario perfilusuario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Perfilusuario persistentPerfilusuario = em.find(Perfilusuario.class, perfilusuario.getIdperfil());
            Usuarios iduserOld = persistentPerfilusuario.getIduser();
            Usuarios iduserNew = perfilusuario.getIduser();
            if (iduserNew != null) {
                iduserNew = em.getReference(iduserNew.getClass(), iduserNew.getIduser());
                perfilusuario.setIduser(iduserNew);
            }
            perfilusuario = em.merge(perfilusuario);
            if (iduserOld != null && !iduserOld.equals(iduserNew)) {
                iduserOld.getPerfilusuarioList().remove(perfilusuario);
                iduserOld = em.merge(iduserOld);
            }
            if (iduserNew != null && !iduserNew.equals(iduserOld)) {
                iduserNew.getPerfilusuarioList().add(perfilusuario);
                iduserNew = em.merge(iduserNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = perfilusuario.getIdperfil();
                if (findPerfilusuario(id) == null) {
                    throw new NonexistentEntityException("The perfilusuario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Perfilusuario perfilusuario;
            try {
                perfilusuario = em.getReference(Perfilusuario.class, id);
                perfilusuario.getIdperfil();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The perfilusuario with id " + id + " no longer exists.", enfe);
            }
            Usuarios iduser = perfilusuario.getIduser();
            if (iduser != null) {
                iduser.getPerfilusuarioList().remove(perfilusuario);
                iduser = em.merge(iduser);
            }
            em.remove(perfilusuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Perfilusuario> findPerfilusuarioEntities() {
        return findPerfilusuarioEntities(true, -1, -1);
    }

    public List<Perfilusuario> findPerfilusuarioEntities(int maxResults, int firstResult) {
        return findPerfilusuarioEntities(false, maxResults, firstResult);
    }

    private List<Perfilusuario> findPerfilusuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Perfilusuario.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Perfilusuario findPerfilusuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Perfilusuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getPerfilusuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Perfilusuario> rt = cq.from(Perfilusuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    
    public Perfilusuario findPerfilByCodUni(String coduni) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("Perfilusuario.findByCoduni");
            q.setParameter("coduni", coduni);
            
            Perfilusuario perfUsuario = (Perfilusuario) q.getSingleResult();
            return perfUsuario;
            
        } catch(Exception e) {
            return null;
        }
    }
    
    
    public static void main(String[] args) {
        
        PerfilusuarioJpaController perUSU = new PerfilusuarioJpaController();
        
        Perfilusuario usuario = perUSU.findPerfilByCodUni("0333212012");
        
        if(usuario != null){
            System.out.println(usuario.getNombre());
        } else{
            System.out.println("Error: Usuario no encontrado");
        }
        
        
        
        
    }
    
    
    
}
