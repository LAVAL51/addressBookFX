package addressbook.addressbookfx;

import java.util.Scanner;

public class Address {
    // Settings
    private String lastname;
    private  String firstname;
    private String address;
    private final Scanner SCANNER = new Scanner(System.in);

    //Constructors

    public Address() {
        this.lastname = "";
        this.firstname = "";
        this.address = "";
    }

    public Address(String lastname, String firstname, String address) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.address = address;
    }

    // methods

    public Address newAddress() {

        Address address;

        address = new Address();
        System.out.println("New address :");
        System.out.println("Lastname : ");
        address.setLastname(SCANNER.nextLine());
        System.out.println("Firstname : ");
        address.setFirstname(SCANNER.nextLine());
        System.out.println("Address : ");
        address.setAddress(SCANNER.nextLine());

        return address;
    }

    // Getters

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getAddress() {
        return address;
    }

    // Setters

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String toString(){
        return "Lastname : " + this.lastname + " fristname : " + this.firstname + " address : " + address;
    }
}
