module addressbook.addressbookfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens addressbook.addressbookfx to javafx.fxml;
    exports addressbook.addressbookfx;
}