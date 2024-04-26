package ru.job4j.cars.util;

import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.User;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

public class Utils {

    public static List<String> getZonesIds() {
        List<String> zoneIds = new ArrayList<>();
        Collections.addAll(zoneIds, TimeZone.getAvailableIDs());
        return zoneIds;
    }

    public static Post correctTimeZone(Post post, User user) {
        var timezone = user.getTimezone().isEmpty() ? TimeZone.getDefault().getID() : user.getTimezone();
        post.setCreated(post.getCreated()
                .atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(ZoneId.of(timezone)).toLocalDateTime());
        return post;
    }

    public static List<Post> correctTimeZoneList(List<Post> posts, User user) {
        posts.forEach(post -> correctTimeZone(post, user));
        return posts;
    }

}
