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

public class TestManyToMany {

    public static void main(String[] args) throws Exception {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();

            var user = new User(0, "user11111", "password");
            create(user, sf);
            var priceHistories = List.of(
                    new PriceHistory(0, 15600, 15300, LocalDateTime.now()),
                    new PriceHistory(0, 15300, 15100, LocalDateTime.now()));
            var post = new Post();
            post.setDescription("post");
            post.setCreated(LocalDateTime.now());
            post.setUser(user);
            post.setPriceHistories(priceHistories);
            post.setParticipates(List.of(user));
            create(post, sf);

            System.out.println("--------------One To Many----------------------");
            var stored = sf.openSession()
                    .createQuery("from Post where id = :fId", Post.class)
                    .setParameter("fId", post.getId())
                    .getSingleResult();
            stored.getPriceHistories().forEach(System.out::println);

            System.out.println("--------------Many To Many----------------------");
            stored.getParticipates().forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> T create(T model, SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.persist(model);
        session.getTransaction().commit();
        session.close();
        return model;
    }

    public static void update(Post item, SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.update(item);
        session.getTransaction().commit();
        session.close();
    }

    public static void delete(Integer id, SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        Post item = new Post();
        item.setId(id);
        session.delete(item);
        session.getTransaction().commit();
        session.close();
    }

    public static List<Post> findAll(SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        List result = session.createQuery("from Post").list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    public static Post findById(Integer id, SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        Post result = session.get(Post.class, id);
        session.getTransaction().commit();
        session.close();
        return result;
    }
}

