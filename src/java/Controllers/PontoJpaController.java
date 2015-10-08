/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Controllers.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.Calendar;
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
import models.Projeto;
import models.Usuario;
import models.UsuarioProjeto;

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
        System.out.println("ponto: " + ponto);
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsuarioProjeto idUsuarioProjeto = ponto.getIdUsuarioProjeto();
            if (idUsuarioProjeto != null) {
                idUsuarioProjeto = em.getReference(idUsuarioProjeto.getClass(), idUsuarioProjeto.getId());
                ponto.setIdUsuarioProjeto(idUsuarioProjeto);
            }
            em.persist(ponto);
            if (idUsuarioProjeto != null) {
                idUsuarioProjeto.getPontoList().add(ponto);
                idUsuarioProjeto = em.merge(idUsuarioProjeto);
            }
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
            Ponto persistentPonto = em.find(Ponto.class, ponto.getIdPonto());
            UsuarioProjeto idUsuarioProjetoOld = persistentPonto.getIdUsuarioProjeto();
            UsuarioProjeto idUsuarioProjetoNew = ponto.getIdUsuarioProjeto();
            if (idUsuarioProjetoNew != null) {
                idUsuarioProjetoNew = em.getReference(idUsuarioProjetoNew.getClass(), idUsuarioProjetoNew.getId());
                ponto.setIdUsuarioProjeto(idUsuarioProjetoNew);
            }
            ponto = em.merge(ponto);
            if (idUsuarioProjetoOld != null && !idUsuarioProjetoOld.equals(idUsuarioProjetoNew)) {
                idUsuarioProjetoOld.getPontoList().remove(ponto);
                idUsuarioProjetoOld = em.merge(idUsuarioProjetoOld);
            }
            if (idUsuarioProjetoNew != null && !idUsuarioProjetoNew.equals(idUsuarioProjetoOld)) {
                idUsuarioProjetoNew.getPontoList().add(ponto);
                idUsuarioProjetoNew = em.merge(idUsuarioProjetoNew);
            }
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
            UsuarioProjeto idUsuarioProjeto = ponto.getIdUsuarioProjeto();
            if (idUsuarioProjeto != null) {
                idUsuarioProjeto.getPontoList().remove(ponto);
                idUsuarioProjeto = em.merge(idUsuarioProjeto);
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

    //BUSCA TODOS PONTOS SEM RESTRIÇÕES

    public List<Ponto> findAll() {

        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("Ponto.findAll");
            List<Ponto> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }
//BUSCA PONTOS DO MES CORRENTE PASSADOS COMO PARAMETRO (MES/USUARIO)

    public List<Ponto> findByMonth(Integer mes, Usuario us) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("Ponto.findByMonth");
            query.setParameter("mes", mes);
            query.setParameter("matricula", us.getMatricula());
            List<Ponto> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }

    //BUSCA PONTOS DO MES E ANO COM HORA_ENTRADA OU HORA_SAIDA NULL, PASSADOS COMO PARAMETRO 
    //(DIA_INICIAL, DIA_FINAL, USUARIO, PROJETO)

    public List<Ponto> findByDataHourNull(Calendar dataInicial, Calendar dataFinal, Integer us, Integer p) {
        Date ini = dataInicial.getTime();
        Date fim = dataFinal.getTime();
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("Ponto.findByDateByUserAndProject");
            query.setParameter("dataInicial", ini);
            query.setParameter("dataFinal", fim);
            query.setParameter("matricula", us);
            query.setParameter("idProjeto", p);
            List<Ponto> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }
    //BUSCA PONTOS DO MES E ANO USUARIO E PROJETO (DATA_INICIAL, DATA_FINAL, MATRICULA, ID_PROJETO)
    public List<Ponto> findByData(Calendar dataInicial, Calendar dataFinal, Integer us, Integer p) {
        Date ini = dataInicial.getTime();
        Date fim = dataFinal.getTime();
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("Ponto.findByDateByUserAndProject");
            query.setParameter("dataInicial", ini);
            query.setParameter("dataFinal", fim);
            query.setParameter("matricula", us);
            query.setParameter("idProjeto", p);
            List<Ponto> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }
    
//BUSCA A LISTA DE PONTOS SOBRE DETERMINADA DATA E USUÁRIO (DATA, USUARIO)

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
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }
//BUSCA USUÁRIO CONFORME A MATRICULA (MATRICULA)

    public List<Ponto> findByMatricula(Integer matricula) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("Ponto.findByMatricula");
            query.setParameter("matricula", matricula);
            List<Ponto> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }
    
    public String findData(int matricula, Projeto p) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("Ponto.findAmountTotalOfHours");
            query.setParameter("matricula", matricula);
            query.setParameter("projeto", p.getIdProjeto());
           String dateList = (String) query.getSingleResult();
            return dateList;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }
    

}
