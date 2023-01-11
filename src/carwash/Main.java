package carwash;

import java.sql.*; //Importing java.sql package
import java.util.Scanner;

public class Main {

    public static void createDatabase() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "");//Establishing connection
            PreparedStatement preparedStatement = connection.prepareStatement("create database if not exists carwash");
            preparedStatement.executeUpdate();

            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/carwash", "root", "");

            preparedStatement = connection.prepareStatement("create table if not exists records(id int auto_increment primary key, car_number varchar(10), driver varchar(125),  date_booked date)");
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement("create table if not exists users(username varchar(50) primary key, password varchar(50), user_type varchar(10))");
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error while connecting to the database: " + e.getMessage()); //Message if something goes wrong while conneting to the database
        }
    }

    public static String menu() {
        String selection = "";
        Scanner sc = new Scanner(System.in);
        System.out.println("#### Welcome. Please select an option below: ####");
        System.out.println("1. Add Record");
        System.out.println("2. Edit Record");
        System.out.println("3. Delete Record");
        System.out.println("4. View Records");
        System.out.println("5. Exit");
        selection = sc.nextLine();
        return selection;
    }

    public static void update(String record, String value, int id) {
        try {
            String query = "update records set " + record + "=? where id=?";
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/carwash", "root", "");
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            //Updating Value
            preparedStatement.setString(1, value);
            preparedStatement.setInt(2, id);

            //Executing Query
            preparedStatement.executeUpdate();

            System.out.println("Record updated Successfully.");
        } catch (SQLException e) {
            System.out.println("\nERROR: " + e.getMessage());
        }
    }

    public static void add(String car, String driver, String dt) {
        try {
            String query = "insert into records(car_number, driver, date_booked) values(?, ?, ?)";
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/carwash", "root", "");
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            //Updating Value
            preparedStatement.setString(1, car);
            preparedStatement.setString(2, driver);
            preparedStatement.setDate(2, Date.valueOf(dt));

            //Executing Query
            preparedStatement.executeUpdate();

            System.out.println("New record added successfully.");
        } catch (SQLException e) {
            System.out.println("\nERROR: " + e.getMessage());
        }
    }

    public static void delete(int id) {
        try {
            String query = "delete from records where id=?";
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/carwash", "root", "");
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            //Updating Value
            preparedStatement.setInt(1, id);

            //Executing Query
            preparedStatement.executeUpdate();

            System.out.println("Record successfully deleted.");
        } catch (SQLException e) {
            System.out.println("\nERROR: " + e.getMessage());
        }
    }

    public static void display() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/carwash", "root", "");//Establishing connection
            //Using SQL SELECT Query
            PreparedStatement preparedStatement = connection.prepareStatement("select * from records");

            //Creating Java ResultSet object
            ResultSet resultSet = preparedStatement.executeQuery();

            //Getting Results
            System.out.println("ID\tCAR\tDRIVER\tDATE");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String car = resultSet.getString("car_number");
                String driver = resultSet.getString("driver");
                String dt = resultSet.getString("date_booked");

                //Printing Results
                System.out.println(id + "\t" + car + "\t" + driver + "\t" + dt);
            }
            System.out.println("\n");
        } catch (SQLException e) {
            System.out.println("\nERROR: " + e.getMessage());
        }
    }

    public static void displayCustomer(String driver) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/carwash", "root", "");//Establishing connection
            //Using SQL SELECT Query
            PreparedStatement preparedStatement = connection.prepareStatement("select * from records where driver=?");

            preparedStatement.setString(1, driver);
            //Creating Java ResultSet object
            ResultSet resultSet = preparedStatement.executeQuery();

            //Getting Results
            System.out.println("ID\tCAR\tDATE");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String car = resultSet.getString("car_number");
                String dt = resultSet.getString("date_booked");

                //Printing Results
                System.out.println(id + "\t" + car + "\t" + dt);
            }
            System.out.println("\n");
        } catch (SQLException e) {
            System.out.println("\nERROR: " + e.getMessage());
        }
    }

    public static String login() {
        try {
            Scanner sc = new Scanner(System.in);
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/carwash", "root", "");//Establishing connection
            //Using SQL SELECT Query
            PreparedStatement preparedStatement = connection.prepareStatement("select * from users where username=? and password=?");

            System.out.println("Username: ");
            String user = sc.nextLine();

            System.out.println("Password: ");
            String pass = sc.nextLine();

            preparedStatement.setString(1, user);
            preparedStatement.setString(2, pass);
            //Creating Java ResultSet object
            ResultSet resultSet = preparedStatement.executeQuery();

            //Getting Results
            int results = 0;
            while (resultSet.next()) {
                results += 1;
            }
            return user;
        } catch (SQLException e) {
            System.out.println("\nERROR: Invalid username and/or password!!!");
            return null;
        }
    }

    public static void admin() {

        while (true) {
            String res = menu();
            Scanner sc = new Scanner(System.in);
            switch (res) {
                case "1":
                {
                    System.out.println("Car Number: ");
                    String car_num = sc.nextLine();

                    System.out.println("Driver username: ");
                    String driver = sc.nextLine();

                    System.out.println("Date: ");
                    String dt = sc.nextLine();

                    add(car_num, driver, dt);
                    break;
                }
                case "2":
                {
                    System.out.println("Enter record number to update: ");
                    int id = sc.nextInt();

                    System.out.println("What wolud you like to updat?");
                    System.out.println("1. Car Number");
                    System.out.println("2. Drive username");
                    System.out.println("3. Date");
                    String up = sc.nextLine();
                    String upd = "";
                    if (up == "1") {
                        upd = "car_number";
                    } else if (up == "2") {
                        upd = "driver";
                    } else if (up == "3") {
                        upd = "date_booked";
                    }

                    if (upd != "") {
                        System.out.println("Enter new value: ");
                        String vl = sc.nextLine();
                        update(upd, vl, id);
                    } else {
                        System.out.println("Invalid input!!!!");
                    }
                    break;
                }
                case "3":
                {
                    System.out.println("Enter record id: ");
                    int rec = sc.nextInt();
                    delete(rec);
                    break;
                }
                case "4":
                {
                    display();
                    break;
                }
                    
                case "5":
                {
                    System.out.println("Thank you for using our service.");
                    System.exit(0);
                    break;
                }
                default: {
                    System.out.println("\nInvalid input!!!!\n");
                    break;
                }
                    
            }
        }
    }

    public static void forward(String username) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/carwash", "root", "");//Establishing connection
            //Using SQL SELECT Query
            PreparedStatement preparedStatement = connection.prepareStatement("select * from users where username=?");

            preparedStatement.setString(1, username);
            //Creating Java ResultSet object
            ResultSet resultSet = preparedStatement.executeQuery();

            //Getting Results
            String tp = "";
            while (resultSet.next()) {
                tp = resultSet.getString("user_type");
            }

            if (tp == "customer") {
                displayCustomer(username);
            } else {
                
                admin();
            }
        } catch (SQLException e) {
            System.out.println("\nERROR: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        createDatabase();
        String username = login();

        while (username == null) {
            username = login();
        }
        forward(username);
//        display();
    }
}
