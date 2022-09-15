package addressbook.addressbookfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.Scanner;

public class TextFile {
    private final String ACCESS_PATH = "./src/fr/iut/laval/partie1/myTextFile/";
    private final Scanner SCANNER = new Scanner(System.in);

    private final AddressBook ADDRESSBOOK = new AddressBook();

    public void loadFile(Address[] addressList, File file) throws Exception {
        try {
            File myList = new File(file.getPath() + file.getName() + ".txt");

            BufferedReader obj = new BufferedReader(new FileReader(myList));

            String currentLine;
            while ((currentLine = obj.readLine()) != null) {
                String[] words = currentLine.split("\t");
                ADDRESSBOOK.addNewAddressWithSettings(addressList, words[0], words[1], words[2]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveFile(Address[] addressList) throws IOException {
        System.out.println("Choose a name for the recording file ");
        String fileName = SCANNER.nextLine();

        try {
            File myList = new File(ACCESS_PATH + fileName +  ".txt");
            if (myList.createNewFile()) {
                FileWriter fileWriter = new FileWriter(myList);
                for (Address address : addressList) {
                    if (address != null) {
                        fileWriter.write(address.getLastname() + "\t" + address.getFirstname() + "\t" + address.getAddress() + "\n");
                    }
                }
                fileWriter.close();
                System.out.println("The address book has been saved. Backup name : " + fileName);
            } else {
                System.out.println("The name chosen for the backup is already taken. ");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
