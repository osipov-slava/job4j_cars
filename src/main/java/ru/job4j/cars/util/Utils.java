package ru.job4j.cars.util;

import java.time.LocalDateTime;
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

    public static LocalDateTime correctTimeZone(LocalDateTime localDateTime, String userTimezone) {
        var timezone = userTimezone == null ? TimeZone.getDefault().getID() : userTimezone;
        return localDateTime
                .atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(ZoneId.of(timezone)).toLocalDateTime();
    }

}
