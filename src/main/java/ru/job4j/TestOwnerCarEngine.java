package ru.job4j;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.cars.model.*;
import ru.job4j.cars.repository.*;

import java.time.LocalDateTime;

public class TestOwnerCarEngine {

    public static void main(String[] args) throws Exception {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try (SessionFactory sf = new MetadataSources(registry)
                .buildMetadata().buildSessionFactory()) {
            var userRepository = new HbnUserRepository(new CrudRepository(sf));
            var carRepository = new HbnCarRepository(new CrudRepository(sf));
            var ownerRepository = new HbnOwnerRepository(new CrudRepository(sf));
            var engineRepository = new HbnEngineRepository(new CrudRepository(sf));
            var historyOwnerRepository = new HbnHistoryOwnerRepository(new CrudRepository(sf));

            var user = new User();
            testUser(user, userRepository);

            var engine = new Engine();
            testEngine(engine, engineRepository);

            var owner = new Owner();
            testOwner(owner, user, ownerRepository);

            var car = new Car();
            createCar(car, engine, carRepository);

            var historyOwner = new HistoryOwner(0, owner, car, LocalDateTime.now().minusYears(1), LocalDateTime.now());
            testHistoryOwner(historyOwner, historyOwnerRepository);

            testCar(car, carRepository);

            historyOwnerRepository.delete(historyOwner.getId());
            ownerRepository.delete(owner.getId());
            carRepository.delete(car.getId());
            engineRepository.delete(engine.getId());
            userRepository.delete(user.getId());
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    private static void testUser(User user, UserRepository userRepository) {
        System.out.println("----------------Users");
        user.setLogin("admin48");
        user.setPassword("admin");
        userRepository.create(user);
        userRepository.findAllOrderById().forEach(System.out::println);
    }

    private static void testEngine(Engine engine, HbnEngineRepository engineRepository) {
        System.out.println("----------------Engine");
        engine.setName("engine v6");
        engineRepository.create(engine);
        engineRepository.findAllOrderById().forEach(System.out::println);
        engine.setName("engine v8");
        engineRepository.update(engine);
        System.out.println("Update: " + engineRepository.findById(engine.getId()));
    }

    private static void testOwner(Owner owner, User user, HbnOwnerRepository ownerRepository) {
        System.out.println("----------------Owner");
        owner.setUser(user);
        owner.setName("John Smith");
        ownerRepository.create(owner);
        ownerRepository.findAllOrderById().forEach(System.out::println);
        owner.setName("Lisa White");
        ownerRepository.update(owner);
        System.out.println("Update: " + ownerRepository.findById(owner.getId()));
    }

    private static void createCar(Car car, Engine engine, HbnCarRepository carRepository) {
        System.out.println("----------------Car & HistoryOwner");
        car.setName("Toyota Camry");
        car.setEngine(engine);
        carRepository.create(car);
    }

    private static void testCar(Car car, HbnCarRepository carRepository) {
        var cars = carRepository.findAllOrderById();
        cars.forEach(System.out::println);
        car.setName("Toyota Highlander");
        carRepository.update(car);
        System.out.println("Update: " + carRepository.findById(car.getId()));
    }

    private static void testHistoryOwner(HistoryOwner historyOwner, HbnHistoryOwnerRepository historyOwnerRepository) {
        historyOwnerRepository.create(historyOwner);
        historyOwnerRepository.findAllOrderById().forEach(System.out::println);
        historyOwner.setStartAt(LocalDateTime.now());
        historyOwnerRepository.update(historyOwner);
        System.out.println("Update: " + historyOwnerRepository.findById(historyOwner.getId()));
    }
}

