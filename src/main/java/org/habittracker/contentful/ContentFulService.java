package org.habittracker.contentful;

import com.contentful.java.cda.CDAClient;
import jakarta.enterprise.context.ApplicationScoped;
import org.habittracker.contentful.beans.BattleGround;
import org.habittracker.contentful.beans.Location;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The ContentFulService class provides functionality to interact with the Contentful API,
 * allowing the retrieval of locations, battlegrounds, and other entry types. It includes
 * caching mechanisms to reduce API calls and improve performance. The entries are fetched
 * and cached on a per-class basis and are automatically refreshed if the cache is older than
 * a set time period.
 *
 * This service supports the following functionality:
 * - Fetching a specific location by ID.
 * - Fetching a specific battleground by ID.
 * - Retrieving all entries of a specific class type from Contentful.
 *
 * The service uses a {@link CDAClient} instance to communicate with the Contentful API
 * and transform the response into Java objects. The cache is implemented as a Map where
 * the keys are class types and the values are collections of respective entries.
 */
@ApplicationScoped
public class ContentFulService {

    private static final String DELIVERY = "yQ5KqmZ8BJ6QH0HoING5oEEbzEqIYEYHpkqsGo0iJEE";
    private static final String SPACE = "4adypw3mrwwn";

    private final CDAClient client;

    private final Map<Class, Collection> cache = new HashMap<>();

    private LocalDateTime lastDelivery;

    public ContentFulService() {
        this.client = CDAClient.builder()
                .setSpace(SPACE)
                .setToken(DELIVERY)
                .build();
    }

    public Optional<Location> getLocationById(String id) {
        Collection<Location> locations = getAllItems(Location.class);
        return locations.stream().filter(location -> location.getId().equals(id))
                .findFirst();
    }

    public Optional<BattleGround> getBattleGroundById(String id) {
        Collection<BattleGround> battleGrounds = getAllItems(BattleGround.class);
        return battleGrounds.stream().filter(battleGround -> battleGround.getId().equals(id))
                .findFirst();
    }


    public <T> Collection<T> getAllItems(Class<T> clazz) {
        if (!cache.containsKey(clazz) || (lastDelivery == null || lastDelivery.plusMinutes(5).isBefore(LocalDateTime.now()))) {
            Collection<T> results = client
                    .observeAndTransform(clazz)
                    .include(3)
                    .all()
                    .blockingSingle();
            cache.put(clazz, results);
            lastDelivery = LocalDateTime.now();
            return results;
        } else {
            return cache.get(clazz);
        }
    }

    public static void main(String[] args) {
        ContentFulService contentFulService = new ContentFulService();
        Collection<Location> allItems = contentFulService.getAllItems(Location.class);
        System.out.println(allItems);
    }
}
