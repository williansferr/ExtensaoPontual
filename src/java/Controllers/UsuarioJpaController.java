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
import models.Usuario;

/**
 *
 * @author Willians
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

     public UsuarioJpaController() {
    }

    public EntityManager getEntityManager() {
        try {
            if (emf == null) {
                emf = Persistence.createEntityManagerFactory("ExtensaoPontualPU");
            }

            return emf.createEntityManager();
        } catch (Exception e) {
            return null;
        }

    }

    public void create(Usuario usuario) {
        if (usuario.getUsuarioProjetoList() == null) {
            usuario.setUsuarioProjetoList(new ArrayList<UsuarioProjeto>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<UsuarioProjeto> attachedUsuarioProjetoList = new ArrayList<UsuarioProjeto>();
            for (UsuarioProjeto usuarioProjetoListUsuarioProjetoToAttach : usuario.getUsuarioProjetoList()) {
                usuarioProjetoListUsuarioProjetoToAttach = em.getReference(usuarioProjetoListUsuarioProjetoToAttach.getClass(), usuarioProjetoListUsuarioProjetoToAttach.getId());
                attachedUsuarioProjetoList.add(usuarioProjetoListUsuarioProjetoToAttach);
            }
            usuario.setUsuarioProjetoList(attachedUsuarioProjetoList);
            em.persist(usuario);
            for (UsuarioProjeto usuarioProjetoListUsuarioProjeto : usuario.getUsuarioProjetoList()) {
                Usuario oldMatriculaOfUsuarioProjetoListUsuarioProjeto = usuarioProjetoListUsuarioProjeto.getMatricula();
                usuarioProjetoListUsuarioProjeto.setMatricula(usuario);
                usuarioProjetoListUsuarioProjeto = em.merge(usuarioProjetoListUsuarioProjeto);
                if (oldMatriculaOfUsuarioProjetoListUsuarioProjeto != null) {
                    oldMatriculaOfUsuarioProjetoListUsuarioProjeto.getUsuarioProjetoList().remove(usuarioProjetoListUsuarioProjeto);
                    oldMatriculaOfUsuarioProjetoListUsuarioProjeto = em.merge(oldMatriculaOfUsuarioProjetoListUsuarioProjeto);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getMatricula());
            List<UsuarioProjeto> usuarioProjetoListOld = persistentUsuario.getUsuarioProjetoList();
            List<UsuarioProjeto> usuarioProjetoListNew = usuario.getUsuarioProjetoList();
            List<String> illegalOrphanMessages = null;
            for (UsuarioProjeto usuarioProjetoListOldUsuarioProjeto : usuarioProjetoListOld) {
                if (!usuarioProjetoListNew.contains(usuarioProjetoListOldUsuarioProjeto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UsuarioProjeto " + usuarioProjetoListOldUsuarioProjeto + " since its matricula field is not nullable.");
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
            usuario.setUsuarioProjetoList(usuarioProjetoListNew);
            usuario = em.merge(usuario);
            for (UsuarioProjeto usuarioProjetoListNewUsuarioProjeto : usuarioProjetoListNew) {
                if (!usuarioProjetoListOld.contains(usuarioProjetoListNewUsuarioProjeto)) {
                    Usuario oldMatriculaOfUsuarioProjetoListNewUsuarioProjeto = usuarioProjetoListNewUsuarioProjeto.getMatricula();
                    usuarioProjetoListNewUsuarioProjeto.setMatricula(usuario);
                    usuarioProjetoListNewUsuarioProjeto = em.merge(usuarioProjetoListNewUsuarioProjeto);
                    if (oldMatriculaOfUsuarioProjetoListNewUsuarioProjeto != null && !oldMatriculaOfUsuarioProjetoListNewUsuarioProjeto.equals(usuario)) {
                        oldMatriculaOfUsuarioProjetoListNewUsuarioProjeto.getUsuarioProjetoList().remove(usuarioProjetoListNewUsuarioProjeto);
                        oldMatriculaOfUsuarioProjetoListNewUsuarioProjeto = em.merge(oldMatriculaOfUsuarioProjetoListNewUsuarioProjeto);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuario.getMatricula();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getMatricula();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<UsuarioProjeto> usuarioProjetoListOrphanCheck = usuario.getUsuarioProjetoList();
            for (UsuarioProjeto usuarioProjetoListOrphanCheckUsuarioProjeto : usuarioProjetoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the UsuarioProjeto " + usuarioProjetoListOrphanCheckUsuarioProjeto + " in its usuarioProjetoList field has a non-nullable matricula field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<Usuario> getUsuarioAllProfessor() {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("Usuario.findAllUserProfessor");
            List<Usuario> qlista = query.getResultList();
            return qlista;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> selectAll() {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("Usuario.findAll");
            List<Usuario> qlista = query.getResultList();

            return qlista;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public List<Usuario> selectAllWithAdmin() {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("Usuario.findAllWithAdmin");
            List<Usuario> qlista = query.getResultList();

            return qlista;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public Usuario findByMatricula(Integer matricula) {
        EntityManager em = getEntityManager();
        Usuario usuario = new Usuario();
        try {
            usuario = em.getReference(Usuario.class, matricula);
            return usuario;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
//BUSCA TODOS OS PROFESSORES DA TABELA USUARIO, SEM EXCEÇÃO

    public List<Usuario> getUserProfessor() {
        EntityManager em = getEntityManager();
        List<Usuario> qlista = new ArrayList<>();
        try {
            Query query = em.createNamedQuery("Usuario.findUsersProfessor");
            qlista = query.getResultList();
            return qlista;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (em != null) {
                em.close();
            }
        }

    }
    
    //BUSCA EMAIL ESPECIFICO
    
    public Usuario getFindEmail(String email){
        EntityManager em = getEntityManager();
        try{
            Query query = em.createNamedQuery("Usuario.findEmail");
            query.setParameter("email", email);
            Usuario us = (Usuario)query.getSingleResult();
            return us;
       }catch(Exception e){
            return null;
        }finally {
            if (em != null) {
                em.close();
            }
        }
    }

//BUSCA TODOS ALUNOS E VOLUNTÁRIOS DA TABELA USUARIO SEM EXEÇÃO

    public List<Usuario> getAllUserStudentsAndVolunteers() {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("Usuario.findAllUserStudentsAndVolunteers");
            List<Usuario> qlista = query.getResultList();
            return qlista;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public List<String> findAllEmails(){
       EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("Usuario.findAllEmails");
            List<String> listEmails = query.getResultList();
            return listEmails;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
}
