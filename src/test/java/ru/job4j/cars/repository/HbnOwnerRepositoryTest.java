package ru.job4j.cars.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.cars.model.Owner;
import ru.job4j.cars.model.User;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class HbnOwnerRepositoryTest {

    @Autowired
    private HbnUserRepository userRepository;

    @Autowired
    private HbnOwnerRepository ownerRepository;

    @AfterEach
    public void clearTables() {
        var owners = ownerRepository.findAllOrderById();
        for (var owner : owners) {
            ownerRepository.delete(owner.getId());
        }
        var users = userRepository.findAllOrderById();
        for (var user : users) {
            userRepository.delete(user.getId());
        }
    }

    private User initUser() {
        var user = new User();
        user.setLogin("user");
        user.setPassword("password");
        userRepository.create(user);
        return user;
    }

    private Owner initOwner() {
        Owner owner = new Owner();
        owner.setName("owner1");
        owner.setUser(initUser());
        ownerRepository.create(owner);
        return owner;
    }

    @Test
    public void whenAddNewOwnerThenFindSameOwner() {
        Owner owner = initOwner();

        Owner result = ownerRepository.findById(owner.getId()).get();
        assertThat(result).usingRecursiveAssertion().isEqualTo(owner);
    }

    @Test
    public void whenUpdateOwnerThenReturnTrue() {
        Owner owner = initOwner();

        owner.setName("owner2");
        boolean result = ownerRepository.update(owner);
        assertThat(result).isTrue();

        Owner updated = ownerRepository.findById(owner.getId()).get();
        assertThat(updated).usingRecursiveAssertion().isEqualTo(owner);
    }

    @Test
    public void whenUpdateUnknownOwnerThenReturnFalse() {
        var owner = new Owner();
        boolean result = ownerRepository.update(owner);
        assertThat(result).isFalse();
    }

    @Test
    public void whenDeleteOwnerThenReturnTrue() {
        Owner owner = initOwner();

        boolean result = ownerRepository.delete(owner.getId());
        assertThat(result).isTrue();

        var optional = ownerRepository.findById(owner.getId());
        assertThat(optional.isEmpty()).isTrue();
    }

    @Test
    public void whenDeleteUnknownOwnerThenReturnFalse() {
        boolean result = ownerRepository.delete(1);
        assertThat(result).isFalse();
    }

    @Test
    public void whenAddOwnersThenGetAllOwners() {
        Owner owner = initOwner();

        Owner owner2 = new Owner();
        owner2.setName("owner2");
        owner2.setUser(owner.getUser());
        ownerRepository.create(owner2);

        var expected = Arrays.asList(owner, owner2);
        List<Owner> actual = ownerRepository.findAllOrderById();
        assertThat(actual).usingRecursiveAssertion().isEqualTo(expected);
    }

}
