/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Controllers.exceptions.IllegalOrphanException;
import Controllers.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import models.UsuarioProjeto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import models.Projeto;

/**
 *
 * @author Willians
 */
public class ProjetoJpaController implements Serializable {

    public ProjetoJpaController() {
    }

    
    public ProjetoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    
    
    public EntityManager getEntityManager() {
        try {
            if (emf == null) {
                emf = Persistence.createEntityManagerFactory("PrimeFacesSipowPU");
            }

            return emf.createEntityManager();
        } catch (Exception e) {
            return null;
        }

    }

    public void create(Projeto projeto) {
        if (projeto.getUsuarioProjetoList() == null) {
            projeto.setUsuarioProjetoList(new ArrayList<UsuarioProjeto>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<UsuarioProjeto> attachedUsuarioProjetoList = new ArrayList<UsuarioProjeto>();
            for (UsuarioProjeto usuarioProjetoListUsuarioProjetoToAttach : projeto.getUsuarioProjetoList()) {
                usuarioProjetoListUsuarioProjetoToAttach = em.getReference(usuarioProjetoListUsuarioProjetoToAttach.getClass(), usuarioProjetoListUsuarioProjetoToAttach.getId());
                attachedUsuarioProjetoList.add(usuarioProjetoListUsuarioProjetoToAttach);
            }
            projeto.setUsuarioProjetoList(attachedUsuarioProjetoList);
            em.persist(projeto);
            for (UsuarioProjeto usuarioProjetoListUsuarioProjeto : projeto.getUsuarioProjetoList()) {
                Projeto oldIdProjetoOfUsuarioProjetoListUsuarioProjeto = usuarioProjetoListUsuarioProjeto.getIdProjeto();
                usuarioProjetoListUsuarioProjeto.setIdProjeto(projeto);
                usuarioProjetoListUsuarioProjeto = em.merge(usuarioProjetoListUsuarioProjeto);
                if (oldIdProjetoOfUsuarioProjetoListUsuarioProjeto != null) {
                    oldIdProjetoOfUsuarioProjetoListUsuarioProjeto.getUsuarioProjetoList().remove(usuarioProjetoListUsuarioProjeto);
                    oldIdProjetoOfUsuarioProjetoListUsuarioProjeto = em.merge(oldIdProjetoOfUsuarioProjetoListUsuarioProjeto);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Projeto projeto) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Projeto persistentProjeto = em.find(Projeto.class, projeto.getIdProjeto());
            List<UsuarioProjeto> usuarioProjetoListOld = persistentProjeto.getUsuarioProjetoList();
            List<UsuarioProjeto> usuarioProjetoListNew = projeto.getUsuarioProjetoList();
            List<String> illegalOrphanMessages = null;
            for (UsuarioProjeto usuarioProjetoListOldUsuarioProjeto : usuarioProjetoListOld) {
                if (!usuarioProjetoListNew.contains(usuarioProjetoListOldUsuarioProjeto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UsuarioProjeto " + usuarioProjetoListOldUsuarioProjeto + " since its idProjeto field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<UsuarioProjeto> attachedUsuarioProjetoListNew = new ArrayList<UsuarioProjeto>();
            for (UsuarioProjeto usuarioProjetoListNewUsuarioProjetoToAttach : usuarioProjetoListNew) {
                usuarioProjetoListNewUsuarioProjetoToAttach = em.getReference(usuarioProjetoListNewUsuarioProjetoToAttach.getClass(), usuarioProjetoListNewUsuarioProjetoToAttach.getId());
                attachedUsuarioProjetoListNew.add(usuarioProjetoListNewUsuarioProjetoToAttach);
            }
            usuarioProjetoListNew = attachedUsuarioProjetoListNew;
            projeto.setUsuarioProjetoList(usuarioProjetoListNew);
            projeto = em.merge(projeto);
            for (UsuarioProjeto usuarioProjetoListNewUsuarioProjeto : usuarioProjetoListNew) {
                if (!usuarioProjetoListOld.contains(usuarioProjetoListNewUsuarioProjeto)) {
                    Projeto oldIdProjetoOfUsuarioProjetoListNewUsuarioProjeto = usuarioProjetoListNewUsuarioProjeto.getIdProjeto();
                    usuarioProjetoListNewUsuarioProjeto.setIdProjeto(projeto);
                    usuarioProjetoListNewUsuarioProjeto = em.merge(usuarioProjetoListNewUsuarioProjeto);
                    if (oldIdProjetoOfUsuarioProjetoListNewUsuarioProjeto != null && !oldIdProjetoOfUsuarioProjetoListNewUsuarioProjeto.equals(projeto)) {
                        oldIdProjetoOfUsuarioProjetoListNewUsuarioProjeto.getUsuarioProjetoList().remove(usuarioProjetoListNewUsuarioProjeto);
                        oldIdProjetoOfUsuarioProjetoListNewUsuarioProjeto = em.merge(oldIdProjetoOfUsuarioProjetoListNewUsuarioProjeto);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = projeto.getIdProjeto();
                if (findProjeto(id) == null) {
                    throw new NonexistentEntityException("The projeto with id " + id + " no longer exists.");
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
            Projeto projeto;
            try {
                projeto = em.getReference(Projeto.class, id);
                projeto.getIdProjeto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The projeto with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<UsuarioProjeto> usuarioProjetoListOrphanCheck = projeto.getUsuarioProjetoList();
            for (UsuarioProjeto usuarioProjetoListOrphanCheckUsuarioProjeto : usuarioProjetoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Projeto (" + projeto + ") cannot be destroyed since the UsuarioProjeto " + usuarioProjetoListOrphanCheckUsuarioProjeto + " in its usuarioProjetoList field has a non-nullable idProjeto field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(projeto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Projeto> findProjetoEntities() {
        return findProjetoEntities(true, -1, -1);
    }

    public List<Projeto> findProjetoEntities(int maxResults, int firstResult) {
        return findProjetoEntities(false, maxResults, firstResult);
    }

    private List<Projeto> findProjetoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Projeto.class));
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

    public Projeto findProjeto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Projeto.class, id);
        } finally {
            em.close();
        }
    }

    public int getProjetoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Projeto> rt = cq.from(Projeto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<Projeto> selectAll() {
        EntityManager em = getEntityManager();
        try{
        Query query = em.createQuery("SELECT p FROM Projeto p ORDER BY p.nome");
        List<Projeto> qlista = query.getResultList();
        return qlista;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    
}
