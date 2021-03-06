package pl.kognitywistyka.app.user;

import pl.kognitywistyka.app.user.User;

import javax.persistence.*;

/**
 * Entity class representing admins.
 * Extends User. DB table user, single-table inheritance discriminator value "pokropski".
 * No additional attributes.
 */
@Entity
@DiscriminatorValue("pokropski")
public class Pokropski extends User {

    public Pokropski() {}

    public Pokropski(String id, String firstName, String lastName, String password) {
        setId(id);
        setFirstName(firstName);
        setLastName(lastName);
        setPassword(password);
    }

    @Override
    public String toString() {
        return getId();
    }

}
