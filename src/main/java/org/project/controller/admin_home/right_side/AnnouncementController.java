package org.project.controller.admin_home.right_side;

import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;
import org.project.controller.ServicesInterface;
import org.project.controller.messages.Message;
import org.project.controller.messages.MessageType;
import org.project.model.dao.users.Users;
import org.project.model.dao.users.UsersDAOImpl;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AnnouncementController implements Initializable {
    @FXML
    private TextArea msgTxtField;
    @FXML
    private JFXToggleButton italicButton;
    @FXML
    private JFXToggleButton boldButton;
    @FXML
    private JFXComboBox sizeComboBox;
    @FXML
    private JFXComboBox fontComboBox;
    @FXML
    private JFXColorPicker fontColorPicker;

    private String colorPicked;
    private String fontFamily = "Arial";
    private int sizePicked;
    private boolean italic;
    private boolean bold;
    private ServicesInterface servicesInterface;
    private UsersDAOImpl usersDAO;
    private ArrayList<Users> onlineUserslist = new ArrayList<>();

    public void setServicesInterface(ServicesInterface servicesInterface) {
        this.servicesInterface = servicesInterface;
    }

    public void setUsersDAO(UsersDAOImpl usersDAO) {
        this.usersDAO = usersDAO;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        fontComboBox.getItems().addAll(Font.getFontNames());
        updateFontComboBoxcell();
        fontColorPicker.setValue(Color.BLACK);
        colorPicked = toRGBCode(fontColorPicker.getValue());
        fontColorPicker.valueProperty().addListener((obs, oldVal, newVal) -> {
                    Color color = (Color) newVal;
                    System.out.println("Color : " + toRGBCode(color));
                    colorPicked = toRGBCode(color);
                    setTextFieldStyle();
                }
        );

        sizeComboBox.getItems().addAll(IntStream.rangeClosed(8, 28).boxed().collect(Collectors.toList()));
        sizeComboBox.setValue(14);
        sizePicked = 14;
        sizeComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
                    int size = (int) newVal;
                    System.out.println("Size : " + size);
                    sizePicked = size;
                    setTextFieldStyle();
                }
        );
        fontComboBox.setValue(fontFamily);
        fontComboBox.valueProperty().addListener((observableValue, o, t1) -> {
            String fontName = t1.toString();
            System.out.println(fontName);
            this.fontFamily = fontName;
            setTextFieldStyle();
        });
        boldButton.setOnAction((ActionEvent e) -> {
            if (bold) {
                bold = false;
            } else {
                bold = true;
            }
            System.out.println("Now bold is " + bold);
            setTextFieldStyle();
        });
        italicButton.setOnAction((ActionEvent e) -> {
            if (italic) {
                italic = false;
            } else {
                italic = true;
            }
            System.out.println("Now italic is " + italic);
            setTextFieldStyle();
        });
        setTextFieldStyle();


    }

    public String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    public FontWeight getFontWeight() {
        if (bold) {
            return FontWeight.BOLD;
        } else {
            return FontWeight.LIGHT;
        }
    }

    public FontPosture getFontPosture() {
        if (italic) {
            return FontPosture.ITALIC;
        } else {
            return FontPosture.REGULAR;
        }
    }

    public void setTextFieldStyle() {
        // System.out.println("s;geod");
        String str = msgTxtField.getText().toString();
        msgTxtField.setText("");
        msgTxtField.setStyle("-fx-font-family: \"" + fontFamily + "\"; " + "-fx-text-fill: " + colorPicked + ";" + "-fx-font-size: " + sizePicked + ";" + " -fx-font-weight:" + getFontWeight().name() + ";" + " -fx-font-style:" + getFontPosture().name());
        msgTxtField.setText(str);
    }

    public void updateFontComboBoxcell() {
        fontComboBox.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                final ListCell<String> cell = new ListCell<String>() {
                    {
                        super.setPrefWidth(100);
                    }

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item);
                            this.setFont(new Font(item, 15));

                        } else {
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        });
    }

    public void sendMessage(ActionEvent event) {
        Message newMsg = new Message();
        newMsg.setType(MessageType.SERVER);
        newMsg.setFontFamily(fontFamily);
        newMsg.setTextFill(colorPicked);
        newMsg.setFontSize(sizePicked);
        newMsg.setFontWeight(getFontWeight().name());
        newMsg.setFontPosture(getFontPosture().name());
        newMsg.setMsg(msgTxtField.getText());
        System.out.println(colorPicked);
        msgTxtField.setText("");
        onlineUserslist = getOnlineUsersList();
        if (onlineUserslist.size() > 0) {
            try {
                System.out.println("onlineUserslist" + onlineUserslist);
                System.out.println("newMsgs" + newMsg.getMsg());
                servicesInterface.sendMessageFromAdminToOnlineUsers(newMsg, onlineUserslist);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    ArrayList<Users> getOnlineUsersList() {
        return usersDAO.getAllOnlineUsers();
    }
}
