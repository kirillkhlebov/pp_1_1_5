package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        try (Session session = Util.getSession()) {
            Transaction transaction = session.beginTransaction();
            session.createSQLQuery("CREATE TABLE users (\n" +
                    "  `id` BIGINT NOT NULL AUTO_INCREMENT,\n" +
                    "  `name` VARCHAR(45) NULL,\n" +
                    "  `last_name` VARCHAR(45) NULL,\n" +
                    "  `age` TINYINT NULL,\n" +
                    "  PRIMARY KEY (`id`));").executeUpdate();
            transaction.commit();
            System.out.println("Users table is created");
        } catch (PersistenceException e) {
            System.out.println("Users table is already exist");
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = Util.getSession()) {
            Transaction transaction = session.beginTransaction();
            session.createSQLQuery("DROP TABLE users;").executeUpdate();
            transaction.commit();
            System.out.println("Users table is deleted");
        } catch (PersistenceException e) {
            System.out.println("There is no Users table in database");
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;
        try (Session session = Util.getSession()) {
            transaction = session.beginTransaction();
            session.persist(new User(name, lastName, age));
            transaction.commit();
            System.out.printf("User with name - %s is added to database%n", name);
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        try (Session session = Util.getSession()) {
            transaction = session.beginTransaction();
            session.createQuery("delete User where id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            transaction.commit();
            System.out.printf(String.format("User with id = %d is deleted", id));
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> usersList = new ArrayList<>();
        try (Session session = Util.getSession()) {
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("SELECT a from User a", User.class);
            usersList = query.getResultList();
            transaction.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return usersList;
    }

    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;
        try (Session session = Util.getSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery("DELETE FROM users;").executeUpdate();
            transaction.commit();
            System.out.println("All rows from Users table are removed");
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
