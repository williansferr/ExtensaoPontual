/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Controllers.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import models.Usuario;
import models.Projeto;
import models.UsuarioProjeto;

/**
 *
 * @author Willians
 */
public class UsuarioProjetoJpaController implements Serializable {

    public UsuarioProjetoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

   public UsuarioProjetoJpaController() {
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

    public void create(UsuarioProjeto usuarioProjeto) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario matricula = usuarioProjeto.getMatricula();
            if (matricula != null) {
                matricula = em.getReference(matricula.getClass(), matricula.getMatricula());
                usuarioProjeto.setMatricula(matricula);
            }
            Projeto idProjeto = usuarioProjeto.getIdProjeto();
            if (idProjeto != null) {
                idProjeto = em.getReference(idProjeto.getClass(), idProjeto.getIdProjeto());
                usuarioProjeto.setIdProjeto(idProjeto);
            }
            em.persist(usuarioProjeto);
            if (matricula != null) {
                matricula.getUsuarioProjetoList().add(usuarioProjeto);
                matricula = em.merge(matricula);
            }
            if (idProjeto != null) {
                idProjeto.getUsuarioProjetoList().add(usuarioProjeto);
                idProjeto = em.merge(idProjeto);
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
            Usuario matriculaOld = persistentUsuarioProjeto.getMatricula();
            Usuario matriculaNew = usuarioProjeto.getMatricula();
            Projeto idProjetoOld = persistentUsuarioProjeto.getIdProjeto();
            Projeto idProjetoNew = usuarioProjeto.getIdProjeto();
            if (matriculaNew != null) {
                matriculaNew = em.getReference(matriculaNew.getClass(), matriculaNew.getMatricula());
                usuarioProjeto.setMatricula(matriculaNew);
            }
            if (idProjetoNew != null) {
                idProjetoNew = em.getReference(idProjetoNew.getClass(), idProjetoNew.getIdProjeto());
                usuarioProjeto.setIdProjeto(idProjetoNew);
            }
            usuarioProjeto = em.merge(usuarioProjeto);
            if (matriculaOld != null && !matriculaOld.equals(matriculaNew)) {
                matriculaOld.getUsuarioProjetoList().remove(usuarioProjeto);
                matriculaOld = em.merge(matriculaOld);
            }
            if (matriculaNew != null && !matriculaNew.equals(matriculaOld)) {
                matriculaNew.getUsuarioProjetoList().add(usuarioProjeto);
                matriculaNew = em.merge(matriculaNew);
            }
            if (idProjetoOld != null && !idProjetoOld.equals(idProjetoNew)) {
                idProjetoOld.getUsuarioProjetoList().remove(usuarioProjeto);
                idProjetoOld = em.merge(idProjetoOld);
            }
            if (idProjetoNew != null && !idProjetoNew.equals(idProjetoOld)) {
                idProjetoNew.getUsuarioProjetoList().add(usuarioProjeto);
                idProjetoNew = em.merge(idProjetoNew);
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
            Usuario matricula = usuarioProjeto.getMatricula();
            if (matricula != null) {
                matricula.getUsuarioProjetoList().remove(usuarioProjeto);
                matricula = em.merge(matricula);
            }
            Projeto idProjeto = usuarioProjeto.getIdProjeto();
            if (idProjeto != null) {
                idProjeto.getUsuarioProjetoList().remove(usuarioProjeto);
                idProjeto = em.merge(idProjeto);
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
    
    //RETORNA PROJETOS QUE O DETERMINADO USUARIO ESTÁ CADASTRADO (USUARIO)

    public List<Projeto> getEntityWithParameterUser(Usuario us) {
        EntityManager em = getEntityManager();
        try {

            Query query = em.createNamedQuery("UsuarioProjeto.getProjectUser");
            query.setParameter("matricula", us.getMatricula());
            List<Projeto> lista = query.getResultList();
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
    //RETORNA PROJETOS QUE O DETERMINADO USUARIO ESTÁ CADASTRADO (USUARIO) CONFORME A DATA
    public List<Projeto> getProjectByUser(Usuario us) {
        EntityManager em = getEntityManager();
        try {

            Query query = em.createNamedQuery("UsuarioProjeto.getProjectByUser");
            query.setParameter("matricula", us.getMatricula());
            List<Projeto> lista = query.getResultList();
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
    
    //RETORNA PONTOS QUE O DETERMINADO USUARIO ESTÁ CADASTRADO (USUARIO) CONFORME A DATA
    public List<Projeto> getPontoWithParameterUser(Usuario us, Date data) {
        EntityManager em = getEntityManager();
        try {

            Query query = em.createNamedQuery("UsuarioProjeto.getPontoByUserByPresenteDay");
            query.setParameter("matricula", us.getMatricula());
            query.setParameter("data", data);
            List<Projeto> lista = query.getResultList();
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
    

    //BUSCA TODOS OS USUÁRIO QUE POSSUEM ALGUM PROJETO CADASTRADO
    public List<Usuario> getUserWithProjectAll() {
        EntityManager em = getEntityManager();
        try {

            Query query = em.createNamedQuery("UsuarioProjeto.TodoUsuariosComProjeto");
            List<Usuario> lista = query.getResultList();
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

    //BUSCA TODOS OS ALUNO DE DETERMINADO PROJETO
    public List<Usuario> getUserStudentsByProjeto(Projeto p) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("UsuarioProjeto.StudentsOfProject");
            query.setParameter("idProjeto", p.getIdProjeto());
            List<Usuario> lista = query.getResultList();
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



    //BUSCA TODAS AS ENTIDADES DE USUARIOPROJETO
    public List<UsuarioProjeto> findAll() {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("UsuarioProjeto.findAll");
            List<UsuarioProjeto> lista = query.getResultList();
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
//VERIFICA COM A MATRICULA DO USUÁRIO SE ESSE ESTÁ CADASTRADO EM ALGUM PROJETO

    public boolean isRegistered(Integer id) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("UsuarioProjeto.isRegisteredInSomeProject");
            query.setParameter("valor", id);
            if (query.getResultList().isEmpty()) {
                return false;
            } else {
                return true;
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

// MÉTODO DE VALIDAÇÃO SOBRE A LISTA DE USUÁRIOS (LISTA<USUARIOS>, LISTA<PROJETO>)
    public List<Usuario> getUserNotThereIsThisProject(List<Usuario> list, Projeto p) {
        EntityManager em = getEntityManager();
        List<Usuario> lista = new ArrayList();
        List<Usuario> temp = new ArrayList();

        try {
            Usuario u;
            for (int i = 0; i < list.size(); i++) {
                Query query = em.createNamedQuery("UsuarioProjeto.thereIsThisProject");
                query.setParameter("idProjeto", p.getIdProjeto());
                query.setParameter("matricula", list.get(i).getMatricula());
                temp = query.getResultList();
                if (temp.isEmpty()) {
                    lista.add(list.get(i));
                }
            }
            return lista;

        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

//METODO PARA EXCLUIR TODOS OS REGISTROS EM CASCATA
    public void destroyAllRegistered(Object obj) {
        EntityManager em = getEntityManager();
        try {
            if (obj instanceof Projeto) {
                Projeto p = new Projeto();
                p = (Projeto) obj;
                em.getTransaction().begin();
                Query query = em.createQuery(
                        "DELETE FROM UsuarioProjeto up where up.idProjeto.idProjeto =:valor");
                query.setParameter("valor", p.getIdProjeto());
                query.executeUpdate();
                em.getTransaction().commit();
            } else if (obj instanceof Usuario) {

                Usuario us = new Usuario();
                us = (Usuario) obj;
                System.out.println("us: "+us.getMatricula());
                em.getTransaction().begin();
                Query query = em.createQuery(
                        "DELETE FROM UsuarioProjeto up where up.matricula.matricula =:valor");
                query.setParameter("valor", us.getMatricula());
                query.executeUpdate();
                em.getTransaction().commit();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
//BUSCA REGISTRO DE PROJETO E USUARIO CONFORME OS VALORES PASSADOS (PROJETO/USUARIO)

    public UsuarioProjeto getRegistryByProjectAndUsuario(Projeto p, Usuario us) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("UsuarioProjeto.findRegisteredUsuarioByProject");
            query.setParameter("idProjeto", p.getIdProjeto());
            query.setParameter("matricula", us.getMatricula());
            UsuarioProjeto up = (UsuarioProjeto) query.getSingleResult();
            return up;
        } catch (Exception e) {
            return null;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
//BUSCA REGISTRO DE PROJETO E PROFESSOR CONFORME OS VALORES PASSADOS (PROJETO)

    public UsuarioProjeto getRegistryByProjectAndProfessor(Projeto p) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("UsuarioProjeto.getRegistryByProjectAndProfessor");
            query.setParameter("idProjeto", p.getIdProjeto());
            UsuarioProjeto up = (UsuarioProjeto) query.getSingleResult();
            return up;
        } catch (Exception e) {
            return null;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    //RETORNA O Professor DO PROJETO (PROJETO)
    public List<Usuario> getUserProfessorByProject(Projeto p) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("UsuarioProjeto.buscaUserProfessorByProject");
            query.setParameter("idProjeto", p.getIdProjeto());
            if (query.getResultList().isEmpty()) {
                List<Usuario> list = new ArrayList();
                return list;
            } else {
                List<Usuario> list = query.getResultList();
                return list;
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    //EXCLUI O CADASTRO ENTRE O USUÁRIO E O PROJETO
    public void removeUserOfProject(UsuarioProjeto usuarioProjeto) {
            System.out.println("UsuarioProjeto: "+usuarioProjeto);
        UsuarioProjeto up = getRegistryByProjectAndUsuario(usuarioProjeto.getIdProjeto(), usuarioProjeto.getMatricula());
        try {
            if (up != null) {
                destroy(up.getId());
                System.out.println("Entrou Corretamente");
            }
        } catch (Exception e) {
            System.out.println("Entrou 2 erro!");
            e.printStackTrace();
        }
    }

    //RETORNA OS PROJETOS QUE DETERMINADO PROFESSOR ESTÁ CADASTRADO
    public List<Projeto> getProjectsOfProfessorLoged(Usuario us) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("UsuarioProjeto.getProjectOfProfessor");
            query.setParameter("matricula", us.getMatricula());
            if (query.getResultList().isEmpty()) {
                List<Projeto> list = new ArrayList();
                return list;
            } else {
                List<Projeto> list = query.getResultList();
                return list;
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    //RETORNA TODOS OS PROJETOS CADASTRADOS
    public List<Projeto> getAllProjects() {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("UsuarioProjeto.getAllProjects");
            if (query.getResultList().isEmpty()) {
                List<Projeto> list = new ArrayList();
                return list;
            } else {
                List<Projeto> list = query.getResultList();
                return list;
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
}
