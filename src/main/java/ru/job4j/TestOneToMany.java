package ru.job4j;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.PriceHistory;
import ru.job4j.cars.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class TestOneToMany {

    public static void main(String[] args) throws Exception {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();

        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();

            var user = new User(0, "user1", "password");
            create(user, sf);
            var priceHistories = List.of(
                    new PriceHistory(0, 15600, 15300, LocalDateTime.now()),
                    new PriceHistory(0, 15300, 15100, LocalDateTime.now()));
            var post = new Post(0, "post", LocalDateTime.now(), user, priceHistories);

            create(post, sf);
            var stored = sf.openSession()
                    .createQuery("from Post where id = :fId", Post.class)
                    .setParameter("fId", post.getId())
                    .getSingleResult();
            stored.getPriceHistories().forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> void create(T model, SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.persist(model);
        session.getTransaction().commit();
        session.close();
    }

    public static <T> List<T> findAll(Class<T> cl, SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        List<T> list = session.createQuery("from " + cl.getName(), cl).list();
        session.getTransaction().commit();
        session.close();
        return list;
    }
}

