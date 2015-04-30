package ru.trylogic.querydsl.example;

import com.mysema.query.jpa.impl.JPAQuery;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.trylogic.querydsl.example.QUser.user;

public class Test {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("unit");

        EntityManager entityManager = emf.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(new User("Smith"));
        entityManager.persist(new User("Gates"));
        entityManager.persist(new User("Orlov"));
        entityManager.persist(new User("Smirnov"));
        entityManager.persist(new User("Orlov"));

        entityManager.flush();
        transaction.commit();
        
        JPAQuery query = new JPAQuery(entityManager);

        List<String> uniqueUserNames = query.from(user)
                .where(user.name.like("%ov"))
                .groupBy(user.name)
                .list(user.name);

        System.out.println("Unique names:");
        for (String uniqueUserName : uniqueUserNames) {
            System.out.println(uniqueUserName);
        }

        entityManager.close();
        emf.close();

    }
}
