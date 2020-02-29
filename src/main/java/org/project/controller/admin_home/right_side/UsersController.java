package org.project.controller.admin_home.right_side;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import org.project.exceptions.UserAlreadyExistException;
import org.project.model.connection.MysqlConnection;
import org.project.model.dao.users.Users;
import org.project.model.dao.users.UsersDAOImpl;

import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class UsersController implements Initializable {


    @FXML
    private Label valErrorlbl;

    @FXML
    private TextField phoneTxt;

    @FXML
    private TextField nameTxt;

    @FXML
    private TextField emailTxt;

    @FXML
    private TextField passwordTxt;

    @FXML
    private TableColumn<Users, String> phoneCol;

    @FXML
    private TableColumn<Users, Integer> id;

    @FXML
    private TableColumn<Users, String> useCol;

    @FXML
    private TableColumn<Users, String> nameCol;

    @FXML
    private TableColumn<Users, String> emailCol;

    @FXML
    private TableColumn<Users, String> passwordCol;

    @FXML
    private TableColumn<Users, String> countryCol;

    @FXML
    private ChoiceBox choicebox;
    @FXML
    private TableView<Users> tableView;

    String originPhone;

    List<Users> list = null;
    ObservableList<Users> listUsers = FXCollections.observableArrayList();
    ObservableList<Users> selectedIndexes = FXCollections.observableArrayList();
    ObservableList<String> AllCountries = FXCollections.observableArrayList();
    //    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
//    static final String DB_URL = "jdbc:mysql://localhost/first";
//    private Connection conn = null;
    MysqlConnection mysqlConnection = MysqlConnection.getInstance();
    UsersDAOImpl usersDAOImpl;

    {
        try {
            usersDAOImpl = new UsersDAOImpl(mysqlConnection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void Update(ActionEvent event) {

        selectedIndexes = tableView.getSelectionModel().getSelectedItems();
        System.out.println("phone" + selectedIndexes.get(0).getPhoneNumber());
        System.out.println("name" + selectedIndexes.get(0).getName());
        System.out.println("email" + selectedIndexes.get(0).getEmail());
        System.out.println("password" + selectedIndexes.get(0).getPassword());
        System.out.println("id" + selectedIndexes.get(0).getId());

        if(!usersDAOImpl.isUserExist(selectedIndexes.get(0).getPhoneNumber())) {

            usersDAOImpl.updateUser(selectedIndexes.get(0));

            System.out.println("phone" + selectedIndexes.get(0).getPhoneNumber());
            System.out.println("name" + selectedIndexes.get(0).getName());
            System.out.println("email" + selectedIndexes.get(0).getEmail());
            System.out.println("password" + selectedIndexes.get(0).getPassword());
            System.out.println("id" + selectedIndexes.get(0).getId());

            System.out.println("phone cell" + phoneCol.getText());
            System.out.println("You clicked me!");
//        label.setText("Hello World!");
        }else {
            Alert alertError = new Alert(Alert.AlertType.ERROR);
            alertError.setTitle("User error");
            alertError.setHeaderText("Error Alert ");
            alertError.setContentText("User is already in database");
            alertError.showAndWait();

        }

    }

    @FXML
    private void Insert(ActionEvent event) {
        Users user = new Users();

        if(!usersDAOImpl.isUserExist(phoneTxt.getText())) {

            if (validate(nameTxt.getText(), phoneTxt.getText(), emailTxt.getText(), passwordTxt.getText())) {


                user.setName(nameTxt.getText());
                user.setPhoneNumber(phoneTxt.getText());
                user.setEmail(emailTxt.getText());
                user.setPassword(passwordTxt.getText());
                System.out.println("country"+(choicebox.getSelectionModel().getSelectedItem().toString()));
                user.setCountry(choicebox.getSelectionModel().getSelectedItem().toString());

                try {
                    usersDAOImpl.register(user);


                } catch (UserAlreadyExistException ex) {
                    Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
                }
                valErrorlbl.setText("");
                System.out.println("userid "+user.getId());
               //  Users users=listUsers.sorted().get(listUsers.size()-1);
               // System.out.println("before user id "+ users.getId());


               Users users1=listUsers.get(listUsers.size()-1);
               System.out.println("before user id "+ users1.getId());

                user.setId(users1.getId()+1);

                listUsers=  usersDAOImpl.getUsers();

                tableView.setItems(listUsers);

                nameTxt.setText("");
                phoneTxt.setText("");
                emailTxt.setText("");
                passwordTxt.setText("");
                choicebox.setValue("Egypt");


            } else {




                valErrorlbl.setText("Data Evaluated the validation");
                valErrorlbl.setStyle("-fx-border: 0px 0px 2px 0px ; -fx-border-color: #f60");
            }

        }else{

            Alert alertError = new Alert(Alert.AlertType.ERROR);
            alertError.setTitle("User error");
            alertError.setHeaderText("Error Alert ");
            alertError.setContentText("User is already in database");
            alertError.showAndWait();
        }


    }

    @FXML
    private void delete(ActionEvent event) {
        Users user = tableView.getSelectionModel().getSelectedItem();
        usersDAOImpl.deleteUSer(user);


        tableView.getItems().removeAll(tableView.getSelectionModel().getSelectedItems());

        System.out.println("deleted");
    }


    public boolean validate(String nameTxtval,String phoneTxtval,String emailTxtval,String passwordTxtval) {

        if (phoneTxtval.matches("^01[0125]{1}(\\-)?[^0\\D]{1}\\d{7}$") && nameTxtval.matches("^[a-zA-Z_-][ a-zA-Z0-9_-]{6,14}$") && emailTxtval.matches("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+\\b(com|net|eg)\\b$") && passwordTxtval.matches("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9\\\\\\\\s]).{6,}")) {
            return true;

        } else {

            return false;
        }

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tableView.setEditable(true);
        phoneCol.setCellFactory(TextFieldTableCell.forTableColumn());
        // nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        emailCol.setCellFactory(TextFieldTableCell.forTableColumn());
        passwordCol.setCellFactory(TextFieldTableCell.forTableColumn());
        useCol.setCellFactory(TextFieldTableCell.forTableColumn());
        countryCol.setCellFactory(TextFieldTableCell.forTableColumn());
        //tableView.getColumns().add( dateOfBirthColumn);

        phoneCol.setCellValueFactory(
                new PropertyValueFactory<Users, String>("phoneNumber")
        );
//        nameCol.setCellValueFactory(
//                new PropertyValueFactory<Person, String>("test")
//        );
        emailCol.setCellValueFactory(
                new PropertyValueFactory<Users, String>("Email")
        );
        passwordCol.setCellValueFactory(
                new PropertyValueFactory<Users, String>("Password")
        );
        useCol.setCellValueFactory(
                new PropertyValueFactory<Users, String>("name")
        );
        countryCol.setCellValueFactory(
                new PropertyValueFactory<Users, String>("country")
        );


        //  tableView.getSelectionModel().get
        phoneCol.setOnEditCommit((TableColumn.CellEditEvent<Users, String> event) -> {
            TablePosition<Users, String> pos = event.getTablePosition();

            String newFullName = event.getNewValue();
            String olavalue=event.getOldValue();
            System.out.println("old value"+olavalue);
            System.out.println("new val" + newFullName);

            int row = pos.getRow();
            pos.getColumn();
            pos.getRow();
            Users person = event.getTableView().getItems().get(row);
            if(!usersDAOImpl.isUserExist(newFullName)) {
                if (newFullName.matches("^01[0125]{1}(\\-)?[^0\\D]{1}\\d{7}$")) {




                    person.setPhoneNumber(newFullName);
                    // usersDAOImpl.updateSinglePropertyUser(person,"phone_number");
                    selectedIndexes = tableView.getSelectionModel().getSelectedItems();
                    selectedIndexes.get(0).setPhoneNumber(newFullName);
                    usersDAOImpl.updateUser(selectedIndexes.get(0));
                    System.out.println("pass" + person.getPassword());
                    System.out.println("phone" + person.getPhoneNumber());
                    System.out.println("email" + person.getEmail());
                    System.out.println("use" + person.getName());
                } else {


                    Alert alertError = new Alert(Alert.AlertType.ERROR);
                    alertError.setTitle("User error");
                    alertError.setHeaderText("Error Alert ");
                    alertError.setContentText("phone pattern violated");
                    alertError.showAndWait();
                    tableView.getItems().set(tableView.getSelectionModel().getSelectedIndex(), person);
                }
            }else {


                TableCell<Users,String> item = new TableCell<>();
//Set the i-th item
                tableView.getItems().set(tableView.getSelectionModel().getSelectedIndex(), person);
              //  tableView.getSelectionModel().getSelectedItem().setPhoneNumber(olavalue);


                Alert alertError = new Alert(Alert.AlertType.ERROR);
                alertError.setTitle("User error");
                alertError.setHeaderText("Error Alert ");
                alertError.setContentText("User Already Exist");
                alertError.showAndWait();

                tableView.getItems().set(tableView.getSelectionModel().getSelectedIndex(), person);

            }



        });

        useCol.setOnEditCommit((TableColumn.CellEditEvent<Users, String> event) -> {
            TablePosition<Users, String> pos = event.getTablePosition();

            String newFullName = event.getNewValue();
            System.out.println("new val" + newFullName);

            int row = pos.getRow();
            Users person = event.getTableView().getItems().get(row);

           if( newFullName.matches("^[a-zA-Z_-][ a-zA-Z0-9_-]{6,14}$")) {

               selectedIndexes = tableView.getSelectionModel().getSelectedItems();
               selectedIndexes.get(0).setName(newFullName);
               usersDAOImpl.updateUser(selectedIndexes.get(0));


               System.out.println("pass" + person.getPassword());
               // System.out.println("phone" + person.getPhone());
               System.out.println("email" + person.getEmail());
               // System.out.println("use" + person.getUse());
           }else {
               Alert alertError = new Alert(Alert.AlertType.ERROR);
               alertError.setTitle("User error");
               alertError.setHeaderText("Error Alert ");
               alertError.setContentText("name pattern violated");
               alertError.showAndWait();
               tableView.getItems().set(tableView.getSelectionModel().getSelectedIndex(), person);


           }

        });



        emailCol.setOnEditCommit((TableColumn.CellEditEvent<Users, String> event) -> {
            TablePosition<Users, String> pos = event.getTablePosition();

            String newFullName = event.getNewValue();
            System.out.println("new val" + newFullName);

            int row = pos.getRow();
            Users person = event.getTableView().getItems().get(row);
           if( newFullName.matches("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+\\b(com|net|eg)\\b$")){

               person.setEmail(newFullName);
               selectedIndexes = tableView.getSelectionModel().getSelectedItems();
               selectedIndexes.get(0).setEmail(newFullName);
               usersDAOImpl.updateUser(selectedIndexes.get(0));


               System.out.println("pass" + person.getPassword());
               // System.out.println("phone" + person.getPhone());
               System.out.println("email" + person.getEmail());
               //System.out.println("use" + person.getUse());

           }else{

               Alert alertError = new Alert(Alert.AlertType.ERROR);
               alertError.setTitle("User error");
               alertError.setHeaderText("Error Alert ");
               alertError.setContentText("Email pattern violated");
               alertError.showAndWait();
               tableView.getItems().set(tableView.getSelectionModel().getSelectedIndex(), person);


           }



        });



        passwordCol.setOnEditCommit((TableColumn.CellEditEvent<Users, String> event) -> {
            TablePosition<Users, String> pos = event.getTablePosition();

            String newFullName = event.getNewValue();
            System.out.println("new val" + newFullName);
            int row = pos.getRow();
            Users person = event.getTableView().getItems().get(row);
            if(newFullName.matches("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9\\\\\\\\s]).{6,}")){


                person.setPassword(newFullName);

                selectedIndexes = tableView.getSelectionModel().getSelectedItems();
                selectedIndexes.get(0).setPassword(newFullName);

                System.out.println("id when update "+selectedIndexes.get(0).getId()+"gen "+selectedIndexes.get(0).getGender());
                usersDAOImpl.updateUser(selectedIndexes.get(0));

                System.out.println("pass" + person.getPassword());
                // System.out.println("phone" + person.getPhone());
                System.out.println("email" + person.getEmail());
                //System.out.println("use" + person.getUse());


            }else {

                Alert alertError = new Alert(Alert.AlertType.ERROR);
                alertError.setTitle("User error");
                alertError.setHeaderText("Error Alert ");
                alertError.setContentText("Password pattern violated");

                alertError.showAndWait();
                tableView.getItems().set(tableView.getSelectionModel().getSelectedIndex(), person);

            }

        });

        countryCol.setOnEditCommit((TableColumn.CellEditEvent<Users, String> event) -> {
            TablePosition<Users, String> pos = event.getTablePosition();

            String newFullName = event.getNewValue();
            System.out.println("new val" + newFullName);
            int row = pos.getRow();
            Users person = event.getTableView().getItems().get(row);

            boolean iterator=AllCountries.contains(newFullName);
            System.out.println("iter"+iterator);


            if(iterator){


                person.setCountry(newFullName);

                selectedIndexes = tableView.getSelectionModel().getSelectedItems();
                selectedIndexes.get(0).setCountry(newFullName);
                usersDAOImpl.updateUser(selectedIndexes.get(0));
                System.out.println("updated1"+selectedIndexes.get(0).getId());
                System.out.println("updated1"+person.getId());

                System.out.println("pass" + person.getPassword());
                // System.out.println("phone" + person.getPhone());
                System.out.println("email" + person.getEmail());
                //System.out.println("use" + person.getUse());


            }else {

                Alert alertError = new Alert(Alert.AlertType.ERROR);
                alertError.setTitle("User error");
                alertError.setHeaderText("Error Alert ");
                alertError.setContentText("Country doesn't exist");

                alertError.showAndWait();
                tableView.getItems().set(tableView.getSelectionModel().getSelectedIndex(), person);

            }

        });










        List<String> collect = Arrays.asList(Locale.getAvailableLocales()).stream().map(Locale::getDisplayCountry).filter(s -> !s.isEmpty()).sorted().collect(Collectors.toList());
        boolean iterator=collect.contains("Egypt");
        System.out.println("iter"+iterator);
        AllCountries = FXCollections.observableArrayList(collect);

        System.out.println(collect);
        choicebox.setItems(AllCountries);
        choicebox.setValue("Egypt");

        // ChoiceBox c = new ChoiceBox(FXCollections.observableArrayList(st));

        // add a listener
        choicebox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

            // if the item of the list is changed
            public void changed(ObservableValue ov, Number value, Number new_value) {

                // set the text for the label to the selected item
                choicebox.setValue(new_value.intValue());
                System.out.println(new_value.intValue());
                System.out.println("choice"+choicebox.getSelectionModel().getSelectedItem());

                //l1.setText(st[new_value.intValue()] + " selected");
            }
        });

        System.out.println("choice"+choicebox.getSelectionModel().getSelectedItem());





        listUsers=  usersDAOImpl.getUsers();

        tableView.setItems(listUsers);




    }
}
