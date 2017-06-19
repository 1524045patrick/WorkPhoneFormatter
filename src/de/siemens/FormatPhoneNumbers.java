package de.siemens;

import java.util.Scanner;

public class FormatPhoneNumbers
{

    private static void Intro()
    {
        System.out.println("Willkommen im wunderhübschen Telefonnummernformatierer!");
        System.out.println();
        System.out.println("ZU BEACHTEN: \nBitte immer Groß/Kleinschreibung beachten und ohne unnötige Leerzeichen, " +
                "sonst funktioniert die Formatierung nicht!");

    }

    private static String scanTable()
    {
        System.out.println("Bitte trage den Namen der Tabelle ein die bearbeitet werden soll");
        Scanner scanner = new Scanner(System.in);
        String tableName = scanner.nextLine();
        System.out.println("Der Tabellenname ist: " + tableName);
        System.out.println("Soll der Tabellenname übernommen werden? (y/n)");
        String save = scanner.nextLine();
        if (save.equals("y") || save.equals("Y") || save.equals("Ja") || save.equals("ja"))
            System.out.println("Tabellenname gespeichert");
        else
            scanTable();
        return tableName;
    }

    private static String scanPhoneNumberColumn()
    {
        String column;

        System.out.println("Bitte trage den Namen der Spalte ein in der die Telefonnummern sind");
        Scanner scanner = new Scanner(System.in);
        column = scanner.nextLine();
        System.out.println("Der Spaltenname ist: " + column);
        System.out.println("Soll der Spaltenname übernommen werden? (y/n)");
        String save = scanner.nextLine();
        if (save.equals("y") || save.equals("Y") || save.equals("Ja") || save.equals("ja"))
            System.out.println("Spaltenname gespeichert");
        else
            scanPhoneNumberColumn();

        return column;
    }

    private static String scanISO()
    {
        String iso_code;

        System.out.println("Bitte trage den Namen der Spalte ein in der die ISO_Codes sind");
        Scanner scanner = new Scanner(System.in);
        iso_code = scanner.nextLine();
        System.out.println("Der Spaltenname ist: " + iso_code);
        System.out.println("Soll der Spaltenname übernommen werden? (y/n)");
        String save = scanner.nextLine();
        if (save.equals("y") || save.equals("Y") || save.equals("Ja") || save.equals("ja"))
            System.out.println("Spaltenname gespeichert");
        else
            scanISO();

        return iso_code;
    }

    private static String scanID()
    {
        String ID;

        System.out.println("Bitte trage den Namen der Spalte ein in der die IDs sind");
        Scanner scanner = new Scanner(System.in);
        ID = scanner.nextLine();
        System.out.println("Der Spaltenname ist: " + ID);
        System.out.println("Soll der Spaltenname übernommen werden? (y/n)");
        String save = scanner.nextLine();
        if (save.equals("y") || save.equals("Y") || save.equals("Ja") || save.equals("ja"))
            System.out.println("Spaltenname gespeichert");
        else
            scanID();

        return ID;
    }

    public static void main(String[] args)
    {
        FormatPhoneNumbers.Intro();
        AccessConnector accessConnector = new AccessConnector();
        accessConnector.connectDatabase();

        System.out.println("Vor der Formatierung bitte die Spalten mit den IDs/Telefonnummern und ISO_Codes in folgende Spaltennamen umbennen");
        System.out.println("Telefonnummerspalte = phonenumber");
        System.out.println("ISO_Codespalte = ISO");
        System.out.println("ID_Spalte = ID");
        String tableName = FormatPhoneNumbers.scanTable();

        //String ISO = FormatPhoneNumbers.scanISO();
        String ISO = "ISO";
        accessConnector.executeQueryISO_Codes(ISO, tableName);

        //String phoneNumber = FormatPhoneNumbers.scanPhoneNumberColumn();
        String phoneNumber = "phonenumber";
        accessConnector.executeQueryPhoneNumber(phoneNumber);


        String ID = "ID";
                //FormatPhoneNumbers.scanID();
        accessConnector.executeQueryID(ID);

        Formatter formatter = new Formatter(accessConnector);

        formatter.generatePhonenumbers();

        accessConnector.import_phonenumbers(formatter);

        accessConnector.updatePhonenumberColumn();
        accessConnector.closeDatabase();

        System.out.println("Telefonnummern wurden erfolgreich formatiert");
    }

}
