package ru.job4j.cars.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.dto.UserDto;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.PostRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SimplePostService implements PostService {

    private final PostRepository postRepository;

    @Override
    public Post create(Post post, UserDto userDto) {
        var user = new User();
        user.setId(userDto.getId());
        post.setUser(user);
        return postRepository.create(post);
    }

//    @Override
//    public boolean update(int id, Post post, List<Integer> categoryIds) {
//        categoryIds.forEach(c -> post.getCategories().add(new Category(c, null)));
//        return postRepository.update(post);
//    }

//    @Override
//    public boolean done(int id, UserDto userDto) {
//        return postRepository.done(id, userDto);
//    }
//
//    @Override
//    public boolean deleteById(int id, UserDto userDto) {
//        return postRepository.deleteById(id, userDto);
//    }

    @Override
    public Optional<Post> findById(int id) {
        return postRepository.findById(id);
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAllOrderById();
    }

}
