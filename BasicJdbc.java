/*
 * The MIT License (MIT)
 * Copyright (c) 2020 Leif Lindbäck
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction,including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so,subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package se.kth.iv1351.jdbcintro;

import java.sql.*;
import java.text.DateFormat;
import java.util.Scanner;

/**
 * A small program that illustrates how to write a simple JDBC program.
 */
public class BasicJdbc {
  private static final String TABLE_NAME = "person";
  private static final String TABLE_NAMES = "works";
  private static final String INSTRUMENTS = "instrument_catalog";
  private static final String NO = "no";
  private static final String INSTR_NAME = "instrument_name";
  private static final String RENT = "lease";
  private static final String ONGOING = "ongoing";
  private PreparedStatement createPersonStmt;
  private PreparedStatement findAllPersonsStmt;
  private PreparedStatement deletePersonStmt;
  private PreparedStatement findAllInstrumentStmt;
  private PreparedStatement rentInstrumentStmt;
  private PreparedStatement checkAllowedToRentStmt;
  private PreparedStatement terminationStmt;
  //private Connection connection;

  private void accessDB() {
    try (Connection connection = createConnection()) {

      //setauto commit false
      connection.setAutoCommit(false);

      //createTable(connection);
      prepareStatements(connection);
      /*createPersonStmt.setString(1, "stina");
      createPersonStmt.setString(2, "0123456789");
      createPersonStmt.setInt(3, 43);
      createPersonStmt.executeUpdate();
      createPersonStmt.setString(1, "olle");
      createPersonStmt.setString(2, "9876543210");
      createPersonStmt.setInt(3, 12);
      createPersonStmt.executeUpdate();*/

      Scanner input = new Scanner(System.in);

      System.out.println("* Enter 1 for list of available instruments"+
                            "\n" +
                         "* Enter 2 to rent an instrument" +
                            "\n" +
                         "* Enter 3 to terminate a rental"

                        );

      int choice = input.nextInt();

      if (choice == 1){
        listAllInstruments(connection);}

       if (choice == 2){
        rentAnInstrument(connection);
      }

       if (choice == 3){

         System.out.println("\n");
         System.out.println("Enter your student assigned ID: ");
         Scanner idReader = new Scanner(System.in);

         int id = idReader.nextInt();

         terminateRental(id, connection);
       }

       //specificInstrument();
      //listAllRows();
      /*deletePersonStmt.setString(1, "stina");
     deletePersonStmt.executeUpdate();*/
     //listAllRows();

    } catch (SQLException | ClassNotFoundException exc) {
      exc.printStackTrace();
      //rollback
    }
  }

  private Connection createConnection() throws SQLException, ClassNotFoundException {
    //Class.forName("org.postgresql.Driver");
    //return DriverManager.getConnection("jdbc:postgresql://localhost:5432/simplejdbc",
      //"postgres", "postgres");
    Class.forName("com.mysql.cj.jdbc.Driver");
    return DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/soundgood_music_school?serverTimezone=UTC",
    "root", "Thisismadb");
  }

  /*private void createTable(Connection connection) {
    try (Statement stmt = connection.createStatement()) {
      if (!tableExists(connection)) {
        stmt.executeUpdate(
                "create table " + TABLE_NAME + " (name varchar(32) primary key, phone varchar(12), age int)");

       // stmt.executeUpdate(
           //     "create table " + TABLE_NAMES + " (name varchar(32) primary key, phone varchar(12), age int)");
      }


    } catch (SQLException sqle) {
      sqle.printStackTrace();
    }
  }*/

  /*private boolean tableExists(Connection connection) throws SQLException {
    DatabaseMetaData metaData = connection.getMetaData();
    ResultSet tableMetaData = metaData.getTables(null, null, null, null);
    while (tableMetaData.next()) {
      String tableName = tableMetaData.getString(3);
      if (tableName.equalsIgnoreCase(TABLE_NAME)) {
        return true;
      }
    }
    return false;
  }*/

  private void rentAnInstrument(Connection connection) throws SQLException {

    listAllInstruments(connection);

    Scanner reader = new Scanner(System.in);

    System.out.println("What Instrument would you like to rent? Select the number ");

    int usersWish = reader.nextInt();

    System.out.println(" ");
    System.out.println("Number " + usersWish + " has been added to your rental cart");

    System.out.println(" ");

    System.out.println("Enter your StudentID: ");

    int studentID = reader.nextInt();

    System.out.println(" ");

    System.out.println("Enter todays date: ");

    int dateOfRenting = reader.nextInt();

    renting(usersWish, studentID, dateOfRenting, ONGOING, connection);


  }


  private void homeScreen(){

    System.out.println(" ----------------- IT WAS A PLEASURE HELPING YOU HELPING YOU ----------------- ");
  }

  private void updatedLog (int studentID){

    System.out.println(" ");
    System.out.println("****************** Your updated rental log ******************");
    System.out.println("----------------------------------------------------------");

    try (ResultSet leases = checkAllowedToRentStmt.executeQuery()) {

      while (leases.next()) {

        if (Integer.parseInt(leases.getString(2)) == studentID) {

          System.out.println(
                  "instrument_id: " + leases.getString(1) + " | " +
                          "student_id: " + leases.getString(2) + " | " +
                          "date of renting: " + leases.getString(3) + " | "
                          + "status of the rental: " + leases.getString(4));

        }
      }


    } catch (SQLException throwables) {
      throwables.printStackTrace();
      //rollback
    }
  }

  private void terminateRental(int studentID, Connection connection) throws SQLException {

    System.out.println(" ");
    System.out.println("****************** Your updated rental log ******************");
    System.out.println("----------------------------------------------------------");

    try (ResultSet leases = checkAllowedToRentStmt.executeQuery()) {

      while (leases.next()) {

        if (Integer.parseInt(leases.getString(2)) == studentID) {

          System.out.println(
                  "instrument_id: " + leases.getString(1) + " | " +
                          "student_id: " + leases.getString(2) + " | " +
                          "date of renting: " + leases.getString(3) + " | "
                          + "status of the rental: " + leases.getString(4));

        }
      }

    } catch (SQLException throwables) {
      //rollback
      throwables.printStackTrace();
    }

    System.out.println(" ");
    System.out.println("Enter the instrumentID of the rental session you want to terminate: ");
    Scanner scan = new Scanner(System.in);
    int selection = scan.nextInt();

    provenTermination(selection, studentID, connection);


    System.out.println(" ");
    System.out.println("To verify that your rental with ID = " + selection + " has been terminated, press 4 to see your updated rental log");

    Scanner reader = new Scanner(System.in);
    int verify = reader.nextInt();

    if (verify == 4){
      updatedLog(studentID);
    } else homeScreen();


  }

  private void provenTermination(int selection, int studentID, Connection connection) throws SQLException {

    String endRental = "terminated";

    try (ResultSet leases2 = checkAllowedToRentStmt.executeQuery()) {

      while (leases2.next()) {

        if (Integer.parseInt(leases2.getString(1)) == selection) {

          //terminationStmt.executeUpdate();
         // terminationStmt.setInt(1, selection);
         // terminationStmt.setInt(2, studentID);
          terminationStmt.setString(1, endRental);
          terminationStmt.setInt(2, selection);
          terminationStmt.executeUpdate();
          connection.commit();

         // java.sql.Date.valueOf("2013-09-04")


          System.out.println(" ");
          System.out.println("The selected rental instance shown below has now been terminated");
          System.out.println(
                  "instrument_id: " + leases2.getString(1) + " | " +
                          "student_id: " + leases2.getString(2) + " | " +
                          "date of renting: " + leases2.getString(3) + " | " +
                  "status of rental: " + leases2.getString(4)
          );

        }
      }
    } catch (SQLException throwables2) {
      throwables2.printStackTrace();
      //rollback
      connection.rollback();
    }
  }

   //terminationStmt.setString(3, "terminated");*/


  private void renting (int instrumentID, int studentID, int dateOfRenting, String ONGOING, Connection connection) throws SQLException {

    try(ResultSet leases = checkAllowedToRentStmt.executeQuery()) {

       int i = 0;

      while (leases.next() && i <= 2){

        if(Integer.parseInt(leases.getString(2)) == studentID){


          i++;
        }
      }

      System.out.println(" ");


      if(i <= 1){
        rentInstrumentStmt.setString(1, String.valueOf(instrumentID));
        rentInstrumentStmt.setString(2, String.valueOf(studentID));
        rentInstrumentStmt.setString(3, String.valueOf(dateOfRenting));
        rentInstrumentStmt.setString(4, ONGOING);
        rentInstrumentStmt.executeUpdate();
        connection.commit();

      } else System.out.println("Unfortunately you have reached the limit of 2 rentals");


    } catch (SQLException throwables) {
      throwables.printStackTrace();
      //rollback
      connection.rollback();
    }




   /* rentInstrumentStmt.setString(1, String.valueOf(instrumentID));
    rentInstrumentStmt.setString(2, String.valueOf(studentID));
    rentInstrumentStmt.setString(3, String.valueOf(dateOfRenting));
    rentInstrumentStmt.executeUpdate();*/
  }




  private void listAllInstruments(Connection connection) throws SQLException {
    try(ResultSet instruments = findAllInstrumentStmt.executeQuery()) {


      System.out.println(" ");
      while (instruments.next()){

        System.out.println(
                instruments.getString(1) + " | " + "name: " + instruments.getString(4) + " |   " +
                "brand: " + instruments.getString(5) + " |   " +
                "price: " + instruments.getString(3) + " | " +
                        "rented: " + instruments.getString(7));
      }
      connection.commit();
    } catch (SQLException throwables) {
      //rollback
      connection.rollback();
      throwables.printStackTrace();
    }

  }

  /*private void listAllRows() {
    try (ResultSet persons = findAllPersonsStmt.executeQuery()) {
      while (persons.next()) {
        System.out.println(
            "name: " + persons.getString(1) + ", phone: " + persons.getString(2) + ", age: " + persons.getInt(3));
      }
    } catch (SQLException sqle) {
      sqle.printStackTrace();
    }
  }*/

  private void prepareStatements(Connection connection) throws SQLException {
    createPersonStmt = connection.prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?, ?, ?)");
    findAllPersonsStmt = connection.prepareStatement("SELECT * from " + TABLE_NAME);
    deletePersonStmt = connection.prepareStatement("DELETE FROM " + TABLE_NAME + " WHERE name = ?");
    findAllInstrumentStmt = connection.prepareStatement("SELECT * from " + INSTRUMENTS);
    rentInstrumentStmt = connection.prepareStatement("INSERT INTO " + RENT + " VALUES (?, ?, ?, ?)");
    checkAllowedToRentStmt = connection.prepareStatement("SELECT * from " + RENT);
    terminationStmt = connection.prepareStatement("UPDATE Lease SET rental_status =? WHERE instrument_id =?");
    //customProofStmt = connection.prepareStatement("INSERT INTO " + RENT + "WHERE instrument_id = ")

  }

  public static void main(String[] args) {
    new BasicJdbc().accessDB();

  }
}
