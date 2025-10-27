/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import dto.Perfilusuario;
import dto.Usuarios;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author ABNER
 */
public class UsuariosJpaController implements Serializable {

    public UsuariosJpaController() {
        
    }
    public UsuariosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("com.mycompany_VotacionDigital_war_1.0-SNAPSHOTPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuarios usuarios) {
        if (usuarios.getPerfilusuarioList() == null) {
            usuarios.setPerfilusuarioList(new ArrayList<Perfilusuario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Perfilusuario> attachedPerfilusuarioList = new ArrayList<Perfilusuario>();
            for (Perfilusuario perfilusuarioListPerfilusuarioToAttach : usuarios.getPerfilusuarioList()) {
                perfilusuarioListPerfilusuarioToAttach = em.getReference(perfilusuarioListPerfilusuarioToAttach.getClass(), perfilusuarioListPerfilusuarioToAttach.getIdperfil());
                attachedPerfilusuarioList.add(perfilusuarioListPerfilusuarioToAttach);
            }
            usuarios.setPerfilusuarioList(attachedPerfilusuarioList);
            em.persist(usuarios);
            for (Perfilusuario perfilusuarioListPerfilusuario : usuarios.getPerfilusuarioList()) {
                Usuarios oldIduserOfPerfilusuarioListPerfilusuario = perfilusuarioListPerfilusuario.getIduser();
                perfilusuarioListPerfilusuario.setIduser(usuarios);
                perfilusuarioListPerfilusuario = em.merge(perfilusuarioListPerfilusuario);
                if (oldIduserOfPerfilusuarioListPerfilusuario != null) {
                    oldIduserOfPerfilusuarioListPerfilusuario.getPerfilusuarioList().remove(perfilusuarioListPerfilusuario);
                    oldIduserOfPerfilusuarioListPerfilusuario = em.merge(oldIduserOfPerfilusuarioListPerfilusuario);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuarios usuarios) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios persistentUsuarios = em.find(Usuarios.class, usuarios.getIduser());
            List<Perfilusuario> perfilusuarioListOld = persistentUsuarios.getPerfilusuarioList();
            List<Perfilusuario> perfilusuarioListNew = usuarios.getPerfilusuarioList();
            List<String> illegalOrphanMessages = null;
            for (Perfilusuario perfilusuarioListOldPerfilusuario : perfilusuarioListOld) {
                if (!perfilusuarioListNew.contains(perfilusuarioListOldPerfilusuario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Perfilusuario " + perfilusuarioListOldPerfilusuario + " since its iduser field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Perfilusuario> attachedPerfilusuarioListNew = new ArrayList<Perfilusuario>();
            for (Perfilusuario perfilusuarioListNewPerfilusuarioToAttach : perfilusuarioListNew) {
                perfilusuarioListNewPerfilusuarioToAttach = em.getReference(perfilusuarioListNewPerfilusuarioToAttach.getClass(), perfilusuarioListNewPerfilusuarioToAttach.getIdperfil());
                attachedPerfilusuarioListNew.add(perfilusuarioListNewPerfilusuarioToAttach);
            }
            perfilusuarioListNew = attachedPerfilusuarioListNew;
            usuarios.setPerfilusuarioList(perfilusuarioListNew);
            usuarios = em.merge(usuarios);
            for (Perfilusuario perfilusuarioListNewPerfilusuario : perfilusuarioListNew) {
                if (!perfilusuarioListOld.contains(perfilusuarioListNewPerfilusuario)) {
                    Usuarios oldIduserOfPerfilusuarioListNewPerfilusuario = perfilusuarioListNewPerfilusuario.getIduser();
                    perfilusuarioListNewPerfilusuario.setIduser(usuarios);
                    perfilusuarioListNewPerfilusuario = em.merge(perfilusuarioListNewPerfilusuario);
                    if (oldIduserOfPerfilusuarioListNewPerfilusuario != null && !oldIduserOfPerfilusuarioListNewPerfilusuario.equals(usuarios)) {
                        oldIduserOfPerfilusuarioListNewPerfilusuario.getPerfilusuarioList().remove(perfilusuarioListNewPerfilusuario);
                        oldIduserOfPerfilusuarioListNewPerfilusuario = em.merge(oldIduserOfPerfilusuarioListNewPerfilusuario);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuarios.getIduser();
                if (findUsuarios(id) == null) {
                    throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios usuarios;
            try {
                usuarios = em.getReference(Usuarios.class, id);
                usuarios.getIduser();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Perfilusuario> perfilusuarioListOrphanCheck = usuarios.getPerfilusuarioList();
            for (Perfilusuario perfilusuarioListOrphanCheckPerfilusuario : perfilusuarioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuarios (" + usuarios + ") cannot be destroyed since the Perfilusuario " + perfilusuarioListOrphanCheckPerfilusuario + " in its perfilusuarioList field has a non-nullable iduser field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usuarios);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuarios> findUsuariosEntities() {
        return findUsuariosEntities(true, -1, -1);
    }

    public List<Usuarios> findUsuariosEntities(int maxResults, int firstResult) {
        return findUsuariosEntities(false, maxResults, firstResult);
    }

    private List<Usuarios> findUsuariosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuarios.class));
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

    public Usuarios findUsuarios(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuarios.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuariosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuarios> rt = cq.from(Usuarios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    
     
    public Usuarios validarUsuario(String coduni, String passuni) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("Usuarios.validar");
            q.setParameter("coduni", coduni);
            q.setParameter("passuni", passuni);
            Usuarios user = (Usuarios)q.getSingleResult();
            return user;
            
        } catch(Exception e) {
            return null;
        }
    }
    
    
    public static void main(String[] args) {
        
        UsuariosJpaController usuDAO = new UsuariosJpaController();
        
        Usuarios usuario = usuDAO.validarUsuario("0333212012", "stringHr2003//");
        
        if(usuario != null){
            System.out.println("ok");
        } else{
            System.out.println("usuario no encontrado");
        }
    }
    
    
}
