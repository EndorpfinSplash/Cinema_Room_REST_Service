//package cinema;
//
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.LinkedList;
//import java.util.List;
//
//@RestController
//public class SeatsController {
//    Cinema cinema = new Cinema();
//
//    @GetMapping("/seats")
//
//    public Cinema getSeats() {
//        return cinema;
//    }
//}
//
// class Cinema {
//    private int total_rows = 9;
//    private int total_columns = 9;
//
//    private List<Seat> available_seats= new LinkedList<>();
//
//    public Cinema() {
//        for (int i = 1; i <= total_rows; i++) {
//            for (int j = 1; j <= total_columns; j++) {
//                this.available_seats.add(new Seat(i, j));
//            }
//        }
//    }
//
//    public List<Seat> getAvailable_seats() {
//        return available_seats;
//    }
//
//    public int getTotal_rows() {
//        return total_rows;
//    }
//
//    public void setTotal_rows(int total_rows) {
//        this.total_rows = total_rows;
//    }
//
//    public int getTotal_columns() {
//        return total_columns;
//    }
//
//    public void setTotal_columns(int total_columns) {
//        this.total_columns = total_columns;
//    }
//}
//
//class Seat {
//    private int row;
//    private int column;
//
//    public Seat(int row, int column) {
//        this.row = row;
//        this.column = column;
//    }
//
//    public int getRow() {
//        return row;
//    }
//
//    public void setRow(int row) {
//        this.row = row;
//    }
//
//    public int getColumn() {
//        return column;
//    }
//
//    public void setColumn(int column) {
//        this.column = column;
//    }
//}
