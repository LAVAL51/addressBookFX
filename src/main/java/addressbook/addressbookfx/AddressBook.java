package addressbook.addressbookfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Scanner;

public class AddressBook implements Initializable {

    private final Scanner SCANNER = new Scanner(System.in);
    private Address[] addressBook = new Address[10];
    private Address[] addressBookSearch = new Address[10];
    private final ObservableList<Address> addresses = FXCollections.observableArrayList();

    public void addNewAddressWithSettings(Address[] addressList, String firstname, String lastname, String address) {
        for (int i = 0; i < addressList.length; i++) {
            if (addressList[i] == null) {
                addressList[i] = new Address(firstname, lastname, address);
                break;
            }
        }
    }

    public void searchAddressByKeyWords(Address[] addressList) {
        System.out.println("Please enter your search (you can search by firstname, lastname or address) ");
        String personSearch = SCANNER.nextLine();
        String[] mySearch = personSearch.split(" ");
        boolean result = false;

        for (Address value : addressList) {
            if (value != null) {
                String lastname = value.getLastname();
                String firstname = value.getFirstname();
                String address = value.getAddress();
                for (String search : mySearch) {
                    if (firstname.equals(search) || lastname.equals(search) || address.equals(search)) {
                        addressBook = new Address[10];
                        result = true;
                    }
                }
            }
        }
        isResult(result);
    }

    public void searchAddressByCriteria(Address[] addressList, String search, String criteria) {

        String [] myCriteria = criteria.replace(" ", "").split("");

        int nbLine = addressList.length - this.countNbNullInArray(addressList);

        String[] searchSettings = new String[myCriteria.length * nbLine];
        boolean result = false;

        boolean isActiveFirstname = false;
        boolean isActiveLastname = false;
        boolean isActiveAddress = false;

        for (Address value : addressList) {
            if (value != null) {
                for (String cri: myCriteria) {
                    for (int i = 0; i < searchSettings.length; i++) {
                        if (searchSettings[i] == null) {
                            if (cri.equals("1")) {
                                searchSettings[i] = value.getFirstname();
                                isActiveFirstname = true;
                                break;
                            }
                            if (cri.equals("2")) {
                                searchSettings[i] = value.getLastname();
                                isActiveLastname = true;
                                break;
                            }
                            if (cri.equals("3")) {
                                searchSettings[i] = value.getAddress();
                                isActiveAddress = true;
                                break;
                            }
                        }
                    }
                }
            }
        }

        for (Address address : addressList) {
            if (address != null) {
                for (String searchSetting : searchSettings) {
                    if ((search.equals(address.getAddress()) && isActiveAddress)|| (search.equals(address.getLastname()) && isActiveLastname) || (search.equals(address.getFirstname()) && isActiveFirstname)) {
                        if (searchSetting.equals(search)) {
                            addNewAddressWithSettings(addressBookSearch, address.getLastname(), address.getFirstname(), address.getAddress());
                            initialize(null, null);
                            result = true;
                        }
                    }
                }
            }
        }
        if (!result) {
            initialize(null, null);
        }
    }

    private boolean isValidAddressComparedSearch(Address address, String search) {
        return search.equals(address.getAddress()) || search.equals(address.getLastname()) || search.equals(address.getFirstname());
    }

    private void isResult(Boolean result) {
        if (!result) {
            initialize(null, null);
        }
    }

    private int countNbNullInArray(Address[] addressList) {
        int nbNull = 0;
        for (Address address : addressList) {
            if (address == null) nbNull++;
        }
        return nbNull;
    }

    public void sortAddressByOneCriteria(Address[] addressList) {

        System.out.println("You want to sort by \n 1. Firstname \n 2. Lastname \n 3. Address ");
        int choiceSortCriteria = SCANNER.nextInt();

        System.out.println("You want to by \n 1. ascending order \n 2. descending order");
        int sortChoice = SCANNER.nextInt();

        int numberOfElementToSort = addressList.length - this.countNbNullInArray(addressList);
        String[] criteria = new String[numberOfElementToSort];

        for (Address address : addressList) {
            for (int i = 0; i < criteria.length; i++) {
                if (criteria[i] == null) {
                    switch (choiceSortCriteria) {
                        case 1 -> criteria[i] = address.getFirstname();
                        case 2 -> criteria[i] = address.getLastname();
                        case 3 -> criteria[i] = address.getAddress();
                        default -> System.out.println("Invalid input !!");
                    }
                    break;
                }
            }
        }

        String temp;

        for (int i = 0; i < numberOfElementToSort; i++) {
            for (int j = i + 1; j < numberOfElementToSort; j++) {
                assert criteria[i] != null;
                int compare = criteria[i].compareTo(criteria[j]);
                if (sortChoice == 1) {
                    if (compare > 0) {
                        temp = criteria[i];
                        criteria[i] = criteria[j];
                        criteria[j] = temp;
                    }
                } else {
                    if (compare < 0) {
                        temp = criteria[i];
                        criteria[i] = criteria[j];
                        criteria[j] = temp;
                    }
                }
            }
        }
        System.out.println(
                "The names in alphabetical order are: ");
        for (int i = 0; i < numberOfElementToSort; i++) {
            System.out.println(criteria[i]);
        }
    }

    public void removeAddress(Address[] addressList) {
        System.out.println("Please enter information of person you wish to remove");
        String personSearch = SCANNER.nextLine();
        String[] mySearch = personSearch.split(" ");

        String answer = "";

        for (int i = 0; i < addressList.length; i++) {
            if (addressList[i] != null) {
                for (String search : mySearch) {
                    boolean isValidResult = this.isValidAddressComparedSearch(addressList[i], search);
                    if (isValidResult) {
                        System.out.println("The person you want to remove is " + addressList[i] + " ? Y or N");
                        answer = SCANNER.nextLine();
                        if ("Y".equals(answer) || "y".equals(answer)) {
                            for (int x = i; x < addressList.length -1; x++) {
                                addressList[x] = addressList[x+1];
                            }
                            System.out.println("Okay this person as been deleted");
                            break;
                        }
                        System.out.println("We keep searching");
                    }
                }
                if ("Y".equals(answer) || "y".equals(answer)) {
                    break;
                }
            }
            if (i == addressList.length -1) {
                System.out.println("We couldn't find the person we were looking for");
            }
        }
    }

    public void loadFile(Address[] addressList, File file) throws Exception {
        try {
            BufferedReader obj = new BufferedReader(new FileReader(file.getPath()));

            String currentLine;
            while ((currentLine = obj.readLine()) != null) {
                String[] words = currentLine.split("\t");
                this.addNewAddressWithSettings(addressList, words[0], words[1], words[2]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onClickLoadButton(ActionEvent event) throws Exception {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                addresses.clear();
                this.addressBook = new Address[10];
                this.loadFile(this.addressBook, file);
                this.initialize(null, null);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @FXML
    private TableView<Address> addressBookTable;

    @FXML
    private TableColumn<Address, String> addressBookTableFirstname;

    @FXML
    private TableColumn<Address, String> addressBookTableLastname;

    @FXML
    private TableColumn<Address, String> addressBookTableAddress;

    public void initialize(URL location, ResourceBundle resource) {
        addressBookTableLastname.setCellValueFactory(new PropertyValueFactory<>("Lastname"));
        addressBookTableFirstname.setCellValueFactory(new PropertyValueFactory<>("Firstname"));
        addressBookTableAddress.setCellValueFactory(new PropertyValueFactory<>("Address"));

        boolean isEmptySearch = true;

        for (Address address : addressBookSearch) {
            addresses.clear();
            if (address != null) {
                setAddresses(addressBookSearch);
                isEmptySearch = false;
                break;
            }
        }

        if(isEmptySearch) {
            setAddresses(addressBook);
        }

        addressBookTable.setItems(addresses);
    }

    private void setAddresses(Address[] addressesBook) {
        for (Address address : addressesBook) {
            if (address != null) {
                addresses.add(new Address(address.getLastname(), address.getFirstname(), address.getAddress()));
            }
        }
    }

    @FXML
    public void onClickAddNeAddress(){

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);

        Label title = new Label("Add new address");
        GridPane.setConstraints(title, 50, 0);
        gridPane.getChildren().add(title);

        Label errorLastname = new Label();
        GridPane.setConstraints(errorLastname, 50, 1);
        gridPane.getChildren().add(errorLastname);
        errorLastname.setVisible(false);

        TextField newLastname = new TextField();
        newLastname.setPromptText("Enter lastname");
        GridPane.setConstraints(newLastname, 50, 2);
        gridPane.getChildren().add(newLastname);

        Label errorFirstname = new Label();
        GridPane.setConstraints(errorFirstname, 50, 3);
        gridPane.getChildren().add(errorFirstname);
        errorFirstname.setVisible(false);

        TextField newFirstname = new TextField();
        newFirstname.setPromptText("Enter firstname");
        GridPane.setConstraints(newFirstname, 50, 4);
        gridPane.getChildren().add(newFirstname);

        Label errorAddress = new Label();
        GridPane.setConstraints(errorAddress, 50, 5);
        gridPane.getChildren().add(errorAddress);
        errorAddress.setVisible(false);

        TextField newAddress = new TextField();
        newAddress.setPromptText("Enter address");
        GridPane.setConstraints(newAddress, 50, 6);
        gridPane.getChildren().add(newAddress);

        Button sendNewAddress = new Button("Send");
        GridPane.setConstraints(sendNewAddress, 50, 10);
        gridPane.getChildren().add(sendNewAddress);

        Scene scene = new Scene(gridPane, 643, 439);

        Stage newWindow = new Stage();

        sendNewAddress.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                boolean verifLastname = true;
                boolean verifFirstname = true;
                boolean verifAddress = true;

                if (newLastname.getText().trim().isEmpty()){
                    errorLastname.setText("Lastname is required");
                    errorLastname.setStyle("-fx-font-weight: bold;");
                    errorLastname.setTextFill(Color.RED);
                    errorLastname.setVisible(true);
                    verifLastname = false;
                } else {
                    errorLastname.setVisible(false);
                }

                if (newFirstname.getText().trim().isEmpty()){
                    errorFirstname.setText("Firstname is required");
                    errorFirstname.setStyle("-fx-font-weight: bold;");
                    errorFirstname.setTextFill(Color.RED);
                    errorFirstname.setVisible(true);
                    verifFirstname = false;
                } else {
                    errorFirstname.setVisible(false);
                }

                if (newAddress.getText().trim().isEmpty()){
                    errorAddress.setText("Address is required");
                    errorAddress.setStyle("-fx-font-weight: bold;");
                    errorAddress.setTextFill(Color.RED);
                    errorAddress.setVisible(true);
                    verifAddress = false;
                } else {
                    errorAddress.setVisible(false);
                }

                if (verifLastname && verifFirstname && verifAddress) {
                    addNewAddressWithSettings(addressBook, newLastname.getText(), newFirstname.getText(), newAddress.getText());
                    initialize(null, null);
                    newWindow.close();
                }
            }
        });

        newWindow.setTitle("Add new address");
        newWindow.setScene(scene);
        newWindow.show();
    }

    @FXML
    private TextField search;

    private boolean isCheckedLastname = false;
    private boolean isCheckedFirstname = false;
    private boolean isCheckedAddress = false;

    @FXML
    public void onMouseClickedLastname() {
        this.isCheckedLastname = !this.isCheckedLastname;
    }

    @FXML
    public void onMouseClickedFirstname() {
        this.isCheckedFirstname = !this.isCheckedFirstname;
    }

    @FXML
    public void onMouseClickedAddress() {
        this.isCheckedAddress = !this.isCheckedAddress;
    }

    @FXML
    public void onTextChanged() {
        String criteria = "";

        if (this.isCheckedFirstname) criteria += "1";
        if (this.isCheckedLastname) criteria += "2";
        if (this.isCheckedAddress) criteria += "3";

        if (!criteria.equals("")) {
            addressBookSearch = new Address[10];
            searchAddressByCriteria(addressBook, search.getText(), criteria);
        }
    }
}
