package ru.job4j;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.cars.model.*;
import ru.job4j.cars.repository.*;

import java.time.LocalDateTime;
import java.util.List;

public class TestPost {

    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            var crudRepository = new CrudRepository(sf);
            var userRepository = new HbnUserRepository(crudRepository);
            var ownerRepository = new HbnOwnerRepository(crudRepository);
            var postRepository = new HbnPostRepository(crudRepository, sf);

            var user = new User();
            user.setLogin("user46");
            user.setPassword("password");
            userRepository.create(user);

            var owner = new Owner();
            owner.setUser(user);
            owner.setName("John");
            ownerRepository.create(owner);

            var post = initFirstPost(user, owner, postRepository);

            var post2 = initSecondPost(user, owner, postRepository);

            testConnectedObjects(postRepository, post);
            testConnectedObjects(postRepository, post2);

            var posts = postRepository.findAllOrderById();
            posts.forEach(p -> System.out.println(p.getDescription()));

            testFindAllForLastDay(postRepository);
            testFindAllCarModelLike(postRepository, "Toyota");
            testFindAllWithFile(postRepository);
            testPostsWithSubscribersByPosts(postRepository, List.of(post, post2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Post initFirstPost(User user, Owner owner, PostRepository postRepository) {
        var engine = new Engine();
        engine.setName("v4 120HP");

        var car1 = new Car();
        car1.setName("Toyota Corolla");
        car1.setEngine(engine);
        car1.setOwner(owner);

        var post = new Post();
        post.setDescription("post");
        post.setCreated(LocalDateTime.now().minusDays(1).minusMinutes(2));
        post.setUser(user);
        post.setCar(car1);
        post.setSubscribers(List.of(user));
        return postRepository.create(post);
    }

    private static Post initSecondPost(User user, Owner owner, PostRepository postRepository) {
        var engine2 = new Engine();
        engine2.setName("v4 180HP");

        var car2 = new Car();
        car2.setName("Audi A4");
        car2.setEngine(engine2);
        car2.setOwner(owner);

        var post2 = new Post();
        post2.setDescription("post2");
        post2.setCreated(LocalDateTime.now().minusDays(1).plusHours(2));
        post2.setUser(user);
        post2.setCar(car2);
        var files = List.of(
                new File(0, "directory/", "filename1"),
                new File(0, "directory/", "filename2"));
        post2.setFiles(files);
        return postRepository.create(post2);
    }

    private static void testConnectedObjects(PostRepository postRepository, Post post) {
        var id = post.getId();
        var optional = postRepository.findById(id);
        var stored = optional.get();
        System.out.println("============Post: " + post.getDescription());

        System.out.println("----User--Many To One----------------------");
        System.out.println(stored.getUser());

        System.out.println("----Car--Many To One----------------------");
        System.out.println(stored.getCar().getName());

        System.out.println("----Files--One To Many----------------------");
        stored.getFiles().forEach(System.out::println);
    }

    private static void testFindAllForLastDay(PostRepository postRepository) {
        System.out.println("----------------FindAllForLastDay");
        var posts = postRepository.findAllForLastDay();
        posts.forEach(post -> System.out.println(post.getCreated()));
    }

    private static void testFindAllCarModelLike(PostRepository postRepository, String key) {
        System.out.println("----------------FindAllCarModelLike");
        var posts = postRepository.findAllCarModelLike(key);
        posts.forEach(post -> System.out.println(post.getCar().getName()));
    }

    private static void testFindAllWithFile(PostRepository postRepository) {
        System.out.println("----------------FindAllWithFile");
        var posts = postRepository.findAllWithFile();
        posts.forEach(post -> System.out.println(post.getFiles()));
    }

    private static void testPostsWithSubscribersByPosts(HbnPostRepository postRepository, List<Post> posts) {
        System.out.println("----------------Join Subscriptions");
        var result = postRepository.findPostsWithSubscribersByPosts(posts);
        result.forEach(post -> System.out.println(post.getSubscribers()));
    }
}
