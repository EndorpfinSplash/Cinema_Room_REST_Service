package cinema;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.*;

import java.util.*;
import java.util.concurrent.*;

@RestController
public class Controller {
    final Map<Long, String> users = new ConcurrentHashMap<>(Map.of(
            2234L, "Kate",
            234234234L, "Alice",
            2134L, "Jessica",
            3456L, "Olivia",
            98684L, "Emma",
            85L, "Liam",
            8495L, "James",
            48438L, "Henry"
    ));

    // add your code below this line
    @DeleteMapping("/users/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeUser(@PathVariable String id) {
        if (this.users.containsKey(Long.valueOf(id))) {
            this.users.remove(Long.valueOf(id));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/test/{id}")
    public void testMethod(@PathVariable String id) {
        if ("200".equals(id)) {
            return;
        }
        if ("300".equals(id)) {
            throw new ResponseStatusException(HttpStatus.MULTIPLE_CHOICES);
        }
        if ("400".equals(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if ("500".equals(id)) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

