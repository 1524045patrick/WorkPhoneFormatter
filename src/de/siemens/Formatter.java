package de.siemens;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;


class Formatter
{
    private PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
    private AccessConnector accessConnector;
    private ArrayList<String> formatted_phoneNumber = new ArrayList<>();

    ArrayList<String> getFormatted_phoneNumber() { return formatted_phoneNumber; }

    Formatter(AccessConnector accessConnector) {this.accessConnector = accessConnector;}

    void generatePhonenumbers()
    {
        if (accessConnector.getISO_Codes().size() != accessConnector.getPhoneNumbers().size()) {
            System.err.println("ISO_Codes oder Telefonnummern fehlen!");
        } else {
            int length = accessConnector.getISO_Codes().size();
            Phonenumber.PhoneNumber phoneNumber;
            for (int i = 0; i < length; i++) {
                try {
                    if (!accessConnector.getPhoneNumbers().get(i).equals(null)) {
                        phoneNumber = phoneNumberUtil.parse(accessConnector.getPhoneNumbers().get(i), accessConnector.getISO_Codes().get(i));
                        String formatted = phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
                        formatted_phoneNumber.add(formatted);
                    }
                } catch (NullPointerException nullpointer) {
                    formatted_phoneNumber.add("");
                } catch (NumberParseException e) {
                    try {
                        PrintWriter printWriter = new PrintWriter("FehlgeschlageneFormatierungen.txt");
                        printWriter.println("Telefonnummer mit der ID: " + (i + 1) + " konnte nicht formatiert werden");
                        printWriter.close();
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }

                }
            }
        }

    }
}