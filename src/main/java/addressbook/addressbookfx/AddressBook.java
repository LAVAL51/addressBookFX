package addressbook.addressbookfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class AddressBook implements Initializable {

    // Statement of variables that I will use

    private final Scanner SCANNER = new Scanner(System.in);
    private Address[] addressBook = new Address[10];
    private Address[] addressBookSearch = new Address[10];
    public final ObservableList<Address> addresses = FXCollections.observableArrayList();
    private boolean isSearch = false;

    // For method search
    private boolean isCheckedLastname = false;
    private boolean isCheckedFirstname = false;
    private boolean isCheckedAddress = false;


    // Statement of FXML variables that I will use

    // Menu
    @FXML
    private Menu fileMenu;
    @FXML
    private MenuItem homeMenu;
    @FXML
    private MenuItem loadAddressBookTable;
    @FXML
    private Menu addressBookMenu;
    @FXML
    private MenuItem showAddresses;
    @FXML
    private MenuItem addNewAddress;

    // Panes
    @FXML
    private Pane content;
    @FXML
    private Pane homePage;
    @FXML
    private Pane AddressBook;
    @FXML
    private Pane AddAddressBook;

    // Variables for method initialize
    @FXML
    private TableView<Address> addressBookTable = new TableView<Address>();
    @FXML
    private TableColumn<Address, String> addressBookTableFirstname = new TableColumn<Address, String>();
    @FXML
    private TableColumn<Address, String> addressBookTableLastname = new TableColumn<Address, String>();
    @FXML
    private TableColumn<Address, String> addressBookTableAddress = new TableColumn<Address, String>();
    @FXML
    private TableColumn<Address, Button> deleteButton = new TableColumn<Address, Button>();


    // For method add new address
    @FXML
    private Button sendNewAddress;
    @FXML
    private TextField newLastname;
    @FXML
    private TextField newFirstname;
    @FXML
    private TextField newAddress;
    @FXML
    private Label errorLastname;
    @FXML
    private Label errorFirstname;
    @FXML
    private Label errorAddress;
    @FXML
    private Label requiredFields;
    @FXML
    private Label requiredStar;
    @FXML
    private Label newAddressRegistered;

    // For method search
    @FXML
    private TextField search;

    //
    // We use the default constructor that is a fact I don't declared constructor.
    //

    // Methods

    public void addNewAddressWithSettings(Address[] addressList, String firstname, String lastname, String address) {
        for (int i = 0; i < addressList.length; i++) {
            if (addressList[i] == null) {
                addressList[i] = new Address(firstname, lastname, address, new Button("Delete"));
                break;
            }
        }
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
                            break;
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

    private void setAddresses(Address[] addressesBook) {
        for (Address address : addressesBook) {
            if (address != null) {
                addresses.add(new Address(address.getLastname(), address.getFirstname(), address.getAddress(), new Button("Delete")));
            }
        }
    }

    public void initialize(URL location, ResourceBundle resource) {
        addressBookTableLastname.setCellValueFactory(new PropertyValueFactory<>("Lastname"));
        addressBookTableFirstname.setCellValueFactory(new PropertyValueFactory<>("Firstname"));
        addressBookTableAddress.setCellValueFactory(new PropertyValueFactory<>("Address"));
        deleteButton.setCellValueFactory(new PropertyValueFactory<>("Delete"));

        if(this.isSearch) {
            for (Address address : addressBookSearch) {
                if (address != null) {
                    setAddresses(addressBookSearch);
                    break;
                }
            }
            this.isSearch = false;
        }  else {
            setAddresses(addressBook);
        }

        addressBookTable.setItems(addresses);
    }

    private void changePane(){
        if (AddressBook.isVisible()) AddressBook.setVisible(false);
        if (AddAddressBook.isVisible()) AddAddressBook.setVisible(false);
        if (homePage.isVisible()) homePage.setVisible(false);
    }

    // FXML Methods

    @FXML
    public void onClickLoadButton() throws Exception {
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
    public void onClickAddNeAddress(){

        sendNewAddress.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                newAddressRegistered.setVisible(false);
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
                    newAddressRegistered.setVisible(true);
                    newLastname.setText("");
                    newFirstname.setText("");
                    newAddress.setText("");
                }
            }
        });
    }

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
        addressBookSearch = new Address[10];

        if (this.isCheckedFirstname) criteria += "1";
        if (this.isCheckedLastname) criteria += "2";
        if (this.isCheckedAddress) criteria += "3";

        addresses.clear();

        if (!criteria.equals("")) {
            this.isSearch = true;
            searchAddressByCriteria(addressBook, search.getText(), criteria);
        } else {
            initialize(null, null);
        }
    }

    @FXML
    public void changeToShowAddressBook(ActionEvent event) {
        this.changePane();
        AddressBook.setVisible(true);
    }

    public void changeToAddAddressBook(ActionEvent event) {
        this.changePane();
        AddAddressBook.setVisible(true);
    }

    @FXML
    public void ChangeToHomePage(ActionEvent event) {
        this.changePane();
        homePage.setVisible(true);
    }
}
