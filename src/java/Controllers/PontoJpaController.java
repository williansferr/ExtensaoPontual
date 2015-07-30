/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Controllers.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import models.Ponto;

/**
 *
 * @author Willians
 */
public class PontoJpaController implements Serializable {

    public PontoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public PontoJpaController() {
    }

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

    public void create(Ponto ponto) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(ponto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ponto ponto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ponto = em.merge(ponto);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ponto.getIdPonto();
                if (findPonto(id) == null) {
                    throw new NonexistentEntityException("The ponto with id " + id + " no longer exists.");
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
            Ponto ponto;
            try {
                ponto = em.getReference(Ponto.class, id);
                ponto.getIdPonto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ponto with id " + id + " no longer exists.", enfe);
            }
            em.remove(ponto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ponto> findPontoEntities() {
        return findPontoEntities(true, -1, -1);
    }

    public List<Ponto> findPontoEntities(int maxResults, int firstResult) {
        return findPontoEntities(false, maxResults, firstResult);
    }

    private List<Ponto> findPontoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ponto.class));
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

    public Ponto findPonto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ponto.class, id);
        } finally {
            em.close();
        }
    }

    public int getPontoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ponto> rt = cq.from(Ponto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Ponto> findAll() {

        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("Ponto.findAll");
            List<Ponto> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Ponto> findByData(Date data) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("Ponto.findByData");
            query.setParameter("data", data);
            List<Ponto> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Ponto> findByUsuarioAndData(Integer matricula, Date data) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("Ponto.findByUsuarioAndData");
            query.setParameter("matricula", matricula);
            query.setParameter("data", data);
            List<Ponto> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Ponto> findByMatricula(Integer matricula) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("Ponto.findByMatricula");
            query.setParameter("matricula", matricula);
            List<Ponto> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
