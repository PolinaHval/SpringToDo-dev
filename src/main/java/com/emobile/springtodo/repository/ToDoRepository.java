package com.emobile.springtodo.repository;

import com.emobile.springtodo.config.HibernateUtil;
import com.emobile.springtodo.model.ToDo;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ToDoRepository {

  public void save(ToDo toDo) {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      session.persist(toDo);
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null) transaction.rollback();
      e.printStackTrace();
    }
  }

  public void delete(long idToDo){
    Transaction tx = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      tx = session.beginTransaction();
      ToDo toDo = session.find(ToDo.class, idToDo);
      if (toDo != null) {
        session.remove(toDo);
      }
      tx.commit();
    } catch (Exception e) {
      if (tx != null) tx.rollback();
      e.printStackTrace();
    }
  }

  public void updateCompleted(long idToDo, boolean completed) {
    Transaction tx = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      tx = session.beginTransaction();
      ToDo toDo = session.find(ToDo.class, idToDo);
      if (toDo != null) {
        toDo.setCompleted(completed);
        session.merge(toDo);
      }
      tx.commit();
    } catch (Exception e) {
      if (tx != null) tx.rollback();
      e.printStackTrace();
    }
  }

  public ToDo getToDoById(long idToDo){
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      return session.find(ToDo.class, idToDo);
    }
  }

  public List<ToDo> findAll(int limit, int offset){
    try(Session session = HibernateUtil.getSessionFactory().openSession()){
      return session.createNativeQuery("SELECT * FROM ToDo", ToDo.class)
          .setFirstResult(offset)
          .setMaxResults(limit)
          .list();
    }
  }
}
