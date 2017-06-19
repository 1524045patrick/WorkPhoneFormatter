package de.siemens;

import java.sql.*;
import java.util.ArrayList;

class AccessConnector
{
    private Connection connection;
    private ArrayList<String> phoneNumbers = new ArrayList<>();
    private ArrayList<String> ISO_Codes = new ArrayList<>();
    private ArrayList<Integer> id = new ArrayList<>();
    private String idColumnName;
    private String actualColumn;
    private String phonenumberColumn;
    private String table;
    private Statement query;
    private ResultSet resultSet;

    ArrayList<String> getPhoneNumbers() { return phoneNumbers; }

    private void setPhoneNumbers(ArrayList<String> phoneNumbers) {this.phoneNumbers = phoneNumbers; }

    ArrayList<String> getISO_Codes() { return ISO_Codes; }


    /**
     * Erstellt die Verbindung zu einer festgelegten Access-Datenbank
     */
    void connectDatabase()
    {
        //String databaseURL = "C:/Users/de5bo010/Desktop/Lead Management Suite - Kopie.accdb";
        String databaseURL = "C:/Users/Patri/Documents/db_orhan.accdb";
        try {
            this.connection = DriverManager.getConnection(("jdbc:ucanaccess://" + databaseURL));
        } catch (SQLException e) {
            System.out.println("Datenbank konnte nicht gefunden werden");
            System.err.println(e.getMessage() + "\nErrorCode: " + e.getErrorCode());
        }

    }

    /**
     * Stellt eine SQL-Anfrage an die verbundene Access-Datenbank um sich die Telefonnummern
     * aus der Access-Datenbank zu nehmen und zu speichern
     *
     * @param columnName Spaltenname der Access-Tabelle in welcher die Telefonnummern enthält
     */
    void executeQueryPhoneNumber(String columnName)
    {
        this.actualColumn = columnName;
        this.phonenumberColumn = columnName;
        try {
            this.query = this.connection.createStatement();
            /**
             * TODO Tabellennamen / Spaltennamen Variabel machen!
             */
            this.resultSet = query.executeQuery("SELECT xxx_ish2017_leaddaten.phonenumber " +
                    "FROM xxx_ish2017_leaddaten");
            appendItems(this.phoneNumbers, this.resultSet);
        } catch (SQLException e) {
            System.err.println(e.getMessage() + "\nErrorCode: " + e.getErrorCode());
        }
    }

    /**
     * Funktionsfähig, holt die aus der angegebenen Spalte + Tabelle die ISO_Codes heraus und speichert diese
     *
     * @param columnName Spaltenname der Access-Tabelle in welcher die ISO_Codes enthalten sind
     */
    void executeQueryISO_Codes(String columnName, String tableName)
    {
        this.actualColumn = columnName;
        this.table = tableName;
        try {
            this.query = this.connection.createStatement();
            ResultSet resultSet = this.query.executeQuery("SELECT " + columnName + " FROM " + this.table);
            appendItems(this.ISO_Codes, resultSet);
        } catch (SQLException e) {
            System.err.println(e.getMessage() + "\nErrorcode: " + e.getErrorCode());
        }
    }

    void executeQueryID(String columnName)
    {
        this.idColumnName = columnName;
        this.actualColumn = columnName;
        try {
            this.query = this.connection.createStatement();
            ResultSet result = this.query.executeQuery("SELECT " + columnName + " FROM " + this.table);
            appendID(this.id, result);
        } catch (SQLException e) {
            System.err.println(e.getMessage() + "\nErrorcode: " + e.getErrorCode());
        }
    }


    /**
     * Funktionsfähig
     *
     * @param list Arraylist welcher die Ergebnisse der Query hinzugefügt werden sollen (Telefonnummern / ISO_Codes)
     */
    private void appendItems(ArrayList<String> list, ResultSet resultSet)
    {

        try {
            while (resultSet.next()) {
                try {
                        list.add(resultSet.getString(this.actualColumn));
                } catch (NullPointerException nullpointer) {
                    System.out.println("Why is this being ignored?");
                    list.add("");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * //TODO ArrayList<T> Variabler Datentyp für AppendItems Funktion
     *
     * @param list List mit inhaltenen Werten
     */
    private void appendID(ArrayList<Integer> list, ResultSet resultSet)
    {
        try {
            while (resultSet.next()) {
                list.add(resultSet.getInt(this.actualColumn));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage() + "\nErrorCode: " + e.getErrorCode());
        }
    }

    /**
     * imporiert die nach kanonischem Format formatierten Telefonnnummern
     *
     * @param formatter dieser Formatter hat im Programm die Telefonnummern nach vorgebenem Format formatiert
     */
    void import_phonenumbers(Formatter formatter) { setPhoneNumbers(formatter.getFormatted_phoneNumber()); }

    /**
     * //TODO Threads fehlen unbedingt nachholen!
     */
    void updatePhonenumberColumn()
    {
        int i = 0;
        try {
            String query = "SELECT " + this.phonenumberColumn + " FROM " + this.table +
                    " WHERE (([" + this.idColumnName + "] IS NOT NULL AND [" +
                    this.phonenumberColumn + "] IS NOT NULL) OR ([" + this.idColumnName + "] IS NOT NULL AND [" + this.phonenumberColumn + "] IS NULL))";
            Statement queryE = connection.createStatement();
            this.resultSet = queryE.executeQuery(query);

            String updateQuery = "UPDATE [" + this.table + "] SET [" + this.phonenumberColumn +
                    "] =? " + "WHERE [" + this.idColumnName + "] =?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);

            while (this.resultSet.next()) {
                preparedStatement.setString(1, this.phoneNumbers.get(i));
                preparedStatement.setString(2, Integer.toString(this.id.get(i)));
                preparedStatement.executeUpdate();
                i++;
            }

            preparedStatement.close();

        } catch (SQLException e) {
            System.err.println(e.getMessage() + "\nErrorcode: " + e.getErrorCode());
        }
    }

    void closeDatabase()
    {
        try {
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
