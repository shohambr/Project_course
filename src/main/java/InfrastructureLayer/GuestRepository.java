package InfrastructureLayer;

import DomainLayer.Roles.Guest;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class GuestRepository {
    private Map<String, Guest> guests = new HashMap<>();

    public void save(Guest guest) {
        guests.put(guest.getID(), guest);
    }

    public Optional<Guest> findById(String id) {
        return Optional.ofNullable(guests.get(id));
    }

    public Guest getById(String id) {
        return guests.get(id);
    }

    public boolean existsById(String id) {
        return guests.containsKey(id);
    }

    public void delete(Guest guest) {
        guests.remove(guest.getID());
    }

    public void deleteById(String id) {
        guests.remove(id);
    }

    public void update(Guest guest) {
        guests.put(guest.getID(), guest);
    }
}