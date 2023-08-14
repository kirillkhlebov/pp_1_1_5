package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
        } catch (javax.persistence.PersistenceException e) {
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
        } catch (javax.persistence.PersistenceException e) {
            System.out.println("There is no Users table in database");
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = Util.getSession()) {
            Transaction transaction = session.beginTransaction();
            Query query = session.createSQLQuery("INSERT INTO users (`name`, `last_name`, `age`) VALUES (?, ?, ?);");
            query.setParameter(1, name);
            query.setParameter(2, lastName);
            query.setParameter(3, age);
            query.executeUpdate();
            transaction.commit();
            System.out.printf("User with name - %s is added to database%n", name);
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = Util.getSession()) {
            Transaction transaction = session.beginTransaction();
            Query query = session.createSQLQuery("DELETE FROM users WHERE `id` = ?;");
            query.setParameter(1, id);
            query.executeUpdate();
            transaction.commit();
            System.out.printf(String.format("User with id = %d is deleted", id));
        } catch (HibernateException e) {
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
        try (Session session = Util.getSession()) {
            Transaction transaction = session.beginTransaction();
            session.createSQLQuery("DELETE FROM users;").executeUpdate();
            transaction.commit();
            System.out.println("All rows from Users table are removed");
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }
}
