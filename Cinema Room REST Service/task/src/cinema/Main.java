package cinema;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}

class TicketException extends RuntimeException {

    private static final long serialVersionUID = -7806029002430564887L;
    private String message;

    public TicketException() {
    }

    public TicketException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

class PasswordException extends RuntimeException {

    private static final long serialVersionUID = -7806029002430564887L;
    private String message;

    public PasswordException() {
    }

    public PasswordException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

class TicketExceptionResponse {
    private String error;

    public TicketExceptionResponse() {
    }

    public TicketExceptionResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}

@ControllerAdvice
class PasswordControllerAdvice {

    @ExceptionHandler(PasswordException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public TicketExceptionResponse handlePasswordException(PasswordException se) {
        return new TicketExceptionResponse(se.getMessage());
    }
}

@ControllerAdvice
class TicketControllerAdvice {

    @ExceptionHandler(TicketException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public TicketExceptionResponse handleTicketException(TicketException se) {
        return new TicketExceptionResponse(se.getMessage());
    }
}



@RestController
class SeatsController {
    final static String PASSWORD = "super_secret";
    Cinema cinema = new Cinema();
    Map<UUID, Seat> cinemaMap = new ConcurrentHashMap<>();

    @GetMapping("/seats")
    public Cinema getSeats() {
        return cinema;
    }

//    @PostMapping("/purchase")
//    public Seat purchaseSeat(@RequestBody CustomerRequest customerRequest) {
//        if (
//                customerRequest.getRow() > cinema.getTotal_rows() || customerRequest.getRow() < 1
//                        ||
//                        customerRequest.getColumn() > cinema.getTotal_columns() || customerRequest.getColumn() < 1
//        ) {
//            throw new TicketException("The number of a row or a column is out of bounds!");
//        }
//
//        for (Seat seat : this.cinema.getAvailable_seats()) {
//            if (seat.getColumn() == customerRequest.getColumn() && seat.getRow() == customerRequest.getRow()) {
//                this.cinema.getAvailable_seats().remove(seat);
//                return seat;
//            }
//        }
//        throw new TicketException("The ticket has been already purchased!");
//    }

    @PostMapping("/purchase")
    public Response purchaseSeat(@RequestBody CustomerRequest customerRequest) {
        if (
                customerRequest.getRow() > cinema.getTotal_rows() || customerRequest.getRow() < 1
                        ||
                        customerRequest.getColumn() > cinema.getTotal_columns() || customerRequest.getColumn() < 1
        ) {
            throw new TicketException("The number of a row or a column is out of bounds!");
        }

        for (Seat seat : this.cinema.getAvailable_seats()) {
            if (seat.getColumn() == customerRequest.getColumn() && seat.getRow() == customerRequest.getRow()) {
                this.cinema.getAvailable_seats().remove(seat);
                UUID token = UUID.randomUUID();
                cinemaMap.put(token, seat);
                return new Response(token, seat);
            }
        }
        throw new TicketException("The ticket has been already purchased!");
    }

    @PostMapping("/return")
    public ReturnResponse returnTicket(@RequestBody ReturnRequest returnRequest) {
        if (cinemaMap.containsKey(returnRequest.getToken())) {
            Seat removedSeat = cinemaMap.remove(returnRequest.getToken());
            this.cinema.getAvailable_seats().add(removedSeat);
            return new ReturnResponse(removedSeat);
        } else {
            throw new TicketException("Wrong token!");
        }
    }

    @PostMapping("/stats")
    public Statistic getStats(@RequestParam(required = false) String password) {
        if (PASSWORD.equals(password)) {
            int income = 0;
            for (Seat seat : cinemaMap.values()) {
                income = income + seat.getPrice();
            }
            return new Statistic(
                    income,
                    cinema.getAvailable_seats().size(),
                    cinema.getTotal_columns() * cinema.getTotal_rows() - cinema.getAvailable_seats().size());
        } else {
            throw new PasswordException("The password is wrong!");
        }
    }

}

class Statistic {
    private int current_income;
    private int number_of_available_seats;
    private int number_of_purchased_tickets;

    public Statistic(int current_income, int number_of_available_seats, int number_of_purchased_tickets) {
        this.current_income = current_income;
        this.number_of_available_seats = number_of_available_seats;
        this.number_of_purchased_tickets = number_of_purchased_tickets;
    }

    public int getCurrent_income() {
        return current_income;
    }

    public void setCurrent_income(int current_income) {
        this.current_income = current_income;
    }

    public int getNumber_of_available_seats() {
        return number_of_available_seats;
    }

    public void setNumber_of_available_seats(int number_of_available_seats) {
        this.number_of_available_seats = number_of_available_seats;
    }

    public int getNumber_of_purchased_tickets() {
        return number_of_purchased_tickets;
    }

    public void setNumber_of_purchased_tickets(int number_of_purchased_tickets) {
        this.number_of_purchased_tickets = number_of_purchased_tickets;
    }
}


class ReturnRequest {
    private UUID token;

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }
}

class ReturnResponse {
    private Seat returned_ticket;

    public ReturnResponse(Seat returned_ticket) {
        this.returned_ticket = returned_ticket;
    }

    public Seat getReturned_ticket() {
        return returned_ticket;
    }

    public void setReturned_ticket(Seat returned_ticket) {
        this.returned_ticket = returned_ticket;
    }
}

class CustomerRequest {
    private int row;
    private int column;

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }
}


class Cinema {
    private int total_rows = 9;
    private int total_columns = 9;

    private List<Seat> available_seats = new LinkedList<>();

    public Cinema() {
        for (int i = 1; i <= total_rows; i++) {
            for (int j = 1; j <= total_columns; j++) {
                this.available_seats.add(new Seat(i, j));
            }
        }
    }

    public List<Seat> getAvailable_seats() {
        return available_seats;
    }

    public int getTotal_rows() {
        return total_rows;
    }

    public void setTotal_rows(int total_rows) {
        this.total_rows = total_rows;
    }

    public int getTotal_columns() {
        return total_columns;
    }

    public void setTotal_columns(int total_columns) {
        this.total_columns = total_columns;
    }
}

class Seat {
    private int row;
    private int column;
    private int price;

    public Seat(int row, int column) {
        this.row = row;
        this.column = column;
        if (row < 5 && row > 0) {
            this.price = 10;
        } else this.price = 8;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    static class TicketPurchasedException extends RuntimeException {
        public TicketPurchasedException(String cause) {
            super(cause);
        }
    }
}

class Response {
    private UUID token;
    private Seat ticket;

    public Response(UUID token, Seat ticket) {
        this.token = token;
        this.ticket = ticket;
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public Seat getTicket() {
        return ticket;
    }

    public void setTicket(Seat ticket) {
        this.ticket = ticket;
    }
}