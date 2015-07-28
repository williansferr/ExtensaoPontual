/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Controllers.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import models.Projeto;
import models.Usuario;
import models.UsuarioProjeto;

/**
 *
 * @author Willians
 */
public class Usuario_ProjetoJpaController implements Serializable {

    public Usuario_ProjetoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public Usuario_ProjetoJpaController() {
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
    
    
    public List<Projeto> getEntityWithParameterUser(Integer v){
         EntityManager em = getEntityManager();
        try{
            
        Query query = em.createNamedQuery("UsuarioProjeto.getProjectUser");
        query.setParameter("valor", v);
        List<Projeto> lista = query.getResultList();
        return lista;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    
    public List<Usuario> getEntityWithParameterProject(Integer p){
         EntityManager em = getEntityManager();
        try{
            
        Query query = em.createNamedQuery("UsuarioProjeto.getUserProject");
        query.setParameter("valor", p);
        List<Usuario> lista = query.getResultList();
        return lista;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
     public List<Usuario> getUserWithProjectAll(){
        EntityManager em = getEntityManager();
        try{
            
        Query query = em.createNamedQuery("UsuarioProjeto.TodoUsuariosComProjeto");
        List<Usuario> lista = query.getResultList();
        return lista;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
        
    }
    
    
     public List<UsuarioProjeto> findAll() {

        EntityManager em = getEntityManager();
        try{
        Query query = em.createNamedQuery("UsuarioProjeto.findAll");
        List<UsuarioProjeto> lista = query.getResultList();
        return lista;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    

    public void create(UsuarioProjeto usuarioProjeto) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Projeto projeto = usuarioProjeto.getProjeto();
            if (projeto != null) {
                projeto = em.getReference(projeto.getClass(), projeto.getIdProjeto());
                usuarioProjeto.setProjeto(projeto);
            }
            em.persist(usuarioProjeto);
            if (projeto != null) {
                projeto.getUsuarioProjetoList().add(usuarioProjeto);
                projeto = em.merge(projeto);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UsuarioProjeto usuarioProjeto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsuarioProjeto persistentUsuarioProjeto = em.find(UsuarioProjeto.class, usuarioProjeto.getId());
            Projeto projetoOld = persistentUsuarioProjeto.getProjeto();
            Projeto projetoNew = usuarioProjeto.getProjeto();
            if (projetoNew != null) {
                projetoNew = em.getReference(projetoNew.getClass(), projetoNew.getIdProjeto());
                usuarioProjeto.setProjeto(projetoNew);
            }
            usuarioProjeto = em.merge(usuarioProjeto);
            if (projetoOld != null && !projetoOld.equals(projetoNew)) {
                projetoOld.getUsuarioProjetoList().remove(usuarioProjeto);
                projetoOld = em.merge(projetoOld);
            }
            if (projetoNew != null && !projetoNew.equals(projetoOld)) {
                projetoNew.getUsuarioProjetoList().add(usuarioProjeto);
                projetoNew = em.merge(projetoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuarioProjeto.getId();
                if (findUsuarioProjeto(id) == null) {
                    throw new NonexistentEntityException("The usuarioProjeto with id " + id + " no longer exists.");
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
            UsuarioProjeto usuarioProjeto;
            try {
                usuarioProjeto = em.getReference(UsuarioProjeto.class, id);
                usuarioProjeto.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarioProjeto with id " + id + " no longer exists.", enfe);
            }
            Projeto projeto = usuarioProjeto.getProjeto();
            if (projeto != null) {
                projeto.getUsuarioProjetoList().remove(usuarioProjeto);
                projeto = em.merge(projeto);
            }
            em.remove(usuarioProjeto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UsuarioProjeto> findUsuarioProjetoEntities() {
        return findUsuarioProjetoEntities(true, -1, -1);
    }

    public List<UsuarioProjeto> findUsuarioProjetoEntities(int maxResults, int firstResult) {
        return findUsuarioProjetoEntities(false, maxResults, firstResult);
    }

    private List<UsuarioProjeto> findUsuarioProjetoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UsuarioProjeto.class));
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

    public UsuarioProjeto findUsuarioProjeto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UsuarioProjeto.class, id);
        } finally {
            em.close();
        }
    }
    


    public int getUsuarioProjetoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UsuarioProjeto> rt = cq.from(UsuarioProjeto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    
}
