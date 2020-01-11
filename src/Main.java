import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LongStringConverter;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class Main extends Application {

    BorderPane root = new BorderPane();
    Group buttons = new Group();
    Button home = new Button("Hem");
    Button signOut = new Button("Logga ut");
    Button addProduct = new Button("Lägg till produkt");
    Button changeProduct = new Button("Ändra produkt");
    Button addLagerplats = new Button("Lägg till lagerplats");
    Button removeLagerplats = new Button("Ta bort lagerplats");
    Button addKategori = new Button("Lägg till kategori");
    Button removeKategori = new Button("Ta bort kategori");
    Button search = new Button("Search");
    private TableView<ProduktKategori> tableView1 = new TableView<>(); //Tableview som används för söka produkt flödet
    private TableView<Produkt> tableView2 = new TableView<>(); //Tableview som används för ändra produkt flödet
    private TableView<LagerPlats> tableView3 = new TableView<>(); //Tableview som används för ta bort lagerplats flödet
    private TableView<Kategori> tableView4 = new TableView<>(); //Tableview som används för kategoriflödet
    private ObservableList<ProduktKategori> observableList1 = FXCollections.observableArrayList(); //Observablelist som används för söka produkt flödet
    private ObservableList<Produkt> observableList2 = FXCollections.observableArrayList(); //Observablelist som används för ändra produkt flödet
    private ObservableList<LagerPlats> observableList3 = FXCollections.observableArrayList();
    private ObservableList<Kategori> observableList4 = FXCollections.observableArrayList();

    HashMap<Integer, String> hmKategori;


    //Tre variabler som behövs för databasen
    private static String url;
    private static String username;
    private static String password;
    private static String checkUser;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        init("database.properties");
        root.setPadding(new Insets(10, 10, 10, 10));

        root.setTop(topPane());
        root.setLeft(leftPane());
        //root.setCenter(middlePane());

        Scene scene = new Scene(root, 1000, 600);
        scene.getStylesheets().add("file:styles/theStyle.css");
        primaryStage.setTitle("Lagerhanteringssystem");
        primaryStage.setScene(scene); //Sätter Stagen till scenen jag skapar
        primaryStage.show(); //Visar resultatet

        BorderPane pane = new BorderPane();

        //Nedan följer inloggs koden
        Label systemName = new Label("Lagerhanteringssystem");
        systemName.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));
        systemName.setPadding(new Insets(10));

        VBox inlogg = new VBox();
        inlogg.setAlignment(Pos.CENTER);
        inlogg.setSpacing(20);
        Label username = new Label("Användarnamn");
        username.setFont(new Font("Helvetica", 20));
        TextField usernameTextField = new TextField();
        usernameTextField.setMaxWidth(200);

        Label password = new Label("Lösenord");
        password.setFont(new Font("Helvetica", 20));
        PasswordField passwordField = new PasswordField();
        passwordField.setMaxWidth(200);

        Button loginButton = new Button("Logga in");

        inlogg.getChildren().addAll(username, usernameTextField, password, passwordField, loginButton);
        pane.setTop(systemName);
        pane.setCenter(inlogg);

        Scene loginscen = new Scene(pane, 1000, 600);
        primaryStage.setScene(loginscen);
        primaryStage.show();

        loginButton.setOnAction(event -> {

            try (Connection conn = getConnection()){
                checkUser = usernameTextField.getText();
                String checkPassword = passwordField.getText();

                PreparedStatement preparedStatement = conn.prepareStatement("Select anvandare.namn, anvandare.losenord from anvandare where namn = ? and losenord = ?");
                preparedStatement.setString(1,checkUser);
                preparedStatement.setString(2,checkPassword);
                ResultSet rs = preparedStatement.executeQuery();

                if (rs.next()) {
                    primaryStage.setScene(scene);
                } else {
                    Alert wrongPassword = new Alert(Alert.AlertType.ERROR);
                    wrongPassword.setHeaderText("Fel användarnamn eller lösenord");
                    wrongPassword.setContentText("Testa igen");
                    wrongPassword.showAndWait();
                    usernameTextField.clear();
                    passwordField.clear();
                }

                preparedStatement.close();
            }
            catch (SQLException e) {
                System.out.println("Något gick fel: " + e.getMessage());
            }   root.setCenter(middlePane());

            /* GAMMAL KOD FÖR LOGIN
            String checkUser = usernameTextField.getText().toString();
            String checkPassword = passwordField.getText().toString();
            if (checkUser.equals("Nils") && checkPassword.equals("Nils")) {
                primaryStage.setScene(scene);
            } else {
                Alert wrongPassword = new Alert(Alert.AlertType.ERROR);
                wrongPassword.setHeaderText("Fel användarnamn eller lösenord");
                wrongPassword.setContentText("Testa igen");
                wrongPassword.showAndWait();
                usernameTextField.clear();
                passwordField.clear();
            }

             */

        });

        signOut.setOnAction(event -> {
            usernameTextField.clear();
            passwordField.clear();
            primaryStage.setScene(loginscen);
        });
    }


    public HBox topPane() {

        HBox top = new HBox();
        top.setAlignment(Pos.TOP_LEFT);
        top.setSpacing(10);

        Label systemName = new Label("Lagerhanteringssystem");
        systemName.setPadding(new Insets(0, 200, 0,0));
        systemName.setFont(Font.font("Helvetica", FontWeight.BOLD,30));

        home.setLayoutX(-160);
        buttons.getChildren().addAll(home, signOut);

        Image user = new Image("file:images/user1.png");
        ImageView user1 = new ImageView(user);
        user1.setFitWidth(30);
        user1.setFitHeight(30);

        top.getChildren().addAll(systemName, buttons, user1);

        home.setOnAction(event -> {
            addProduct.getStyleClass().remove("active");
            changeProduct.getStyleClass().remove("active");
            addLagerplats.getStyleClass().remove("active");
            removeLagerplats.getStyleClass().remove("active");
            addKategori.getStyleClass().remove("active");
            removeKategori.getStyleClass().remove("active");
            search.getStyleClass().remove("active");
            root.setCenter(middlePane());

        });

        return top;

    }


    public GridPane leftPane() {

        GridPane root1 = new GridPane();
        root.setId("vBox");

        //Titledpane för produkt
        TitledPane product = new TitledPane();
        product.setText("Produkt");
        VBox produktKnappar = new VBox();
        produktKnappar.setStyle("-fx-background-color: gainsboro");
        produktKnappar.getChildren().addAll(addProduct, changeProduct);
        addProduct.setId("menuBtn");
        changeProduct.setId("menuBtn");
        product.setContent(produktKnappar);

        //Titledpane för lagerplats
        TitledPane lagerPlats = new TitledPane();
        lagerPlats.setText("Lagerplats");
        VBox lagerPlatsKnappar = new VBox();
        lagerPlatsKnappar.setStyle("-fx-background-color: gainsboro");
        lagerPlatsKnappar.getChildren().addAll(addLagerplats, removeLagerplats);
        addLagerplats.setId("menuBtn");
        removeLagerplats.setId("menuBtn");
        lagerPlats.setContent(lagerPlatsKnappar);

        TitledPane kategori = new TitledPane();
        kategori.setText("Kategori");
        VBox kategoriKnappar = new VBox();
        kategoriKnappar.setStyle("-fx-background-color: gainsboro");
        kategoriKnappar.getChildren().addAll(addKategori, removeKategori);
        addKategori.setId("menuBtn");
        removeKategori.setId("menuBtn");
        kategori.setContent(kategoriKnappar);

        Accordion dragspel = new Accordion();
        dragspel.getPanes().addAll(product, lagerPlats, kategori);

        VBox knapparUnderTitledPane = new VBox();
        search.setMaxWidth(dragspel.getPrefWidth());
        search.setId("menuBtn");

        knapparUnderTitledPane.getChildren().addAll(search);
        knapparUnderTitledPane.setAlignment(Pos.CENTER);
        knapparUnderTitledPane.setPadding(new Insets(10, 10, 10, 10));

        root1.add(dragspel, 0, 0);
        root1.add(knapparUnderTitledPane, 0, 1);

        root1.setStyle("-fx-background-color: gainsboro");

        addProduct.setOnAction(event -> {
            changeProduct.getStyleClass().remove("active");
            search.getStyleClass().remove("active");
            addLagerplats.getStyleClass().remove("active");
            addKategori.getStyleClass().remove("active");
            removeLagerplats.getStyleClass().remove("active");
            removeKategori.getStyleClass().remove("active");
            addProduct.getStyleClass().add("active");
            root.setCenter(laggTillProdukt());
        });

        changeProduct.setOnAction(event -> {
            addProduct.getStyleClass().remove("active");
            search.getStyleClass().remove("active");
            addLagerplats.getStyleClass().remove("active");
            addKategori.getStyleClass().remove("active");
            removeLagerplats.getStyleClass().remove("active");
            removeKategori.getStyleClass().remove("active");
            changeProduct.getStyleClass().add("active");
            root.setCenter(andraProdukt());

        });

        addLagerplats.setOnAction(event -> {
            addProduct.getStyleClass().remove("active");
            changeProduct.getStyleClass().remove("active");
            addKategori.getStyleClass().remove("active");
            removeLagerplats.getStyleClass().remove("active");
            removeKategori.getStyleClass().remove("active");
            search.getStyleClass().remove("active");
            addLagerplats.getStyleClass().add("active");
            root.setCenter(laggTillLagerPlats());

        });

        removeLagerplats.setOnAction(event -> {
            addProduct.getStyleClass().remove("active");
            changeProduct.getStyleClass().remove("active");
            addKategori.getStyleClass().remove("active");
            addLagerplats.getStyleClass().remove("active");
            removeKategori.getStyleClass().remove("active");
            search.getStyleClass().remove("active");
            removeLagerplats.getStyleClass().add("active");
            root.setCenter(taBortLagerplats());

        });

        addKategori.setOnAction(event -> {
            addProduct.getStyleClass().remove("active");
            search.getStyleClass().remove("active");
            changeProduct.getStyleClass().remove("active");
            addLagerplats.getStyleClass().remove("active");
            removeLagerplats.getStyleClass().remove("active");
            removeKategori.getStyleClass().remove("active");
            addKategori.getStyleClass().add("active");
            root.setCenter(laggTillKategori());

        });

        removeKategori.setOnAction(event -> {
            addProduct.getStyleClass().remove("active");
            search.getStyleClass().remove("active");
            changeProduct.getStyleClass().remove("active");
            addLagerplats.getStyleClass().remove("active");
            removeLagerplats.getStyleClass().remove("active");
            addKategori.getStyleClass().remove("active");
            removeKategori.getStyleClass().add("active");
            root.setCenter(taBortKategori());

        });

        search.setOnAction(event -> {
            changeProduct.getStyleClass().remove("active");
            addProduct.getStyleClass().remove("active");
            addLagerplats.getStyleClass().remove("active");
            addKategori.getStyleClass().remove("active");
            removeLagerplats.getStyleClass().remove("active");
            removeKategori.getStyleClass().remove("active");
            search.getStyleClass().add("active");
            root.setCenter(sokProdukt());
        });


        return root1;
    }

    public VBox middlePane() {

        VBox vBox = new VBox();
        vBox.setId("vBox");
        Label rubrik = new Label("Välkommen," + "\n" +  checkUser);
        rubrik.setFont(Font.font("Helvetica", FontWeight.BOLD,40));
        Image smiley = new Image("file:images/smiley.png");

        ImageView visaSmiley = new ImageView(smiley);

        vBox.getChildren().addAll(rubrik, new Text(System.lineSeparator()) , visaSmiley);
        vBox.setAlignment(Pos.CENTER);

        return vBox;

    }
    public VBox laggTillProdukt() { // Flöde för att lägga till en produkt

        VBox vBox = new VBox();
        vBox.setId("vBox");
        vBox.setAlignment(Pos.TOP_CENTER);
        hmKategori = getKategori();

        GridPane pane = new GridPane();
        pane.setPadding(new Insets(10,10,10,10));
        pane.setHgap(20);
        pane.setVgap(20);
        pane.setAlignment(Pos.TOP_CENTER);

        Label rubrik = new Label("Lägg till produkt");
        rubrik.setFont(Font.font("Helvetica", FontWeight.BOLD,40));

        Label lblArtikelNmr = new Label("Artikelnummer: ");
        lblArtikelNmr.setPrefWidth(120);
        TextField artikelNmr = new TextField();
        pane.add(lblArtikelNmr, 1, 3);
        pane.add(artikelNmr,2, 3);

        Label lblArtikelNamn = new Label("Artikelnamn: ");
        TextField artikelNamn = new TextField();
        pane.add(lblArtikelNamn, 1, 4);
        pane.add(artikelNamn,2, 4);

        Label lblAntal = new Label("Antal: ");
        TextField antal = new TextField();
        pane.add(lblAntal,1, 5);
        pane.add(antal,2, 5);

        Label lblLagerPlats = new Label("Lagerplats: ");
        ComboBox<String> comboBoxLagerPlats = new ComboBox<>();
        comboBoxLagerPlats.getItems().addAll(getLagerplats());
        comboBoxLagerPlats.setPrefWidth(150);
        pane.add(lblLagerPlats, 1, 7);
        pane.add(comboBoxLagerPlats,2, 7);

        Label lblKategori = new Label("Kategori: ");
        ComboBox<String> comboBoxKategori = new ComboBox<>();
        for (HashMap.Entry<Integer, String> entry : hmKategori.entrySet()){
            String value = entry.getValue();
            comboBoxKategori.getItems().addAll(value);
        }

        comboBoxKategori.getItems().addAll();
        comboBoxKategori.setPrefWidth(150);
        pane.add(lblKategori, 1, 6);
        pane.add(comboBoxKategori, 2, 6);

        Button laggTill = new Button("Lägg till");
        Button avbryt = new Button("Avbryt");
        pane.add(laggTill,1,10);
        pane.add(avbryt,2,10);

        vBox.getChildren().addAll(rubrik,pane);

        avbryt.setOnAction(event -> {
            root.setCenter(middlePane());
            addProduct.getStyleClass().remove("active");
        });

        laggTill.setOnAction(event -> {

            try(Connection conn = getConnection()) {

                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                if (artikelNmr.getText().isEmpty() || artikelNamn.getText().isEmpty() || antal.getText().isEmpty() || comboBoxLagerPlats.getSelectionModel().getSelectedItem().isEmpty()) {
                    errorAlert.setTitle("Meddelande");
                    errorAlert.setHeaderText("Problem");
                    errorAlert.setContentText("Du har inte skrivit in något i alla rutor!");
                    errorAlert.showAndWait();

                } else {

                    PreparedStatement psProduct = conn.prepareStatement("INSERT INTO produkt VALUES (?, ?, ?, ?, ?)");
                    psProduct.setInt(1, Integer.parseInt(artikelNmr.getText()));
                    psProduct.setString(2, artikelNamn.getText());
                    psProduct.setInt(3, Integer.parseInt(antal.getText()));
                    psProduct.setString(4, comboBoxLagerPlats.getSelectionModel().getSelectedItem());
                    for (HashMap.Entry<Integer, String> entry : hmKategori.entrySet()){
                        Integer valueKey = entry.getKey();
                        String value = comboBoxKategori.getSelectionModel().getSelectedItem();
                        if(entry.getValue().equals(value)) {
                            psProduct.setInt(5, valueKey);
                        }
                    }

                    /* GAMMAL KOD FÖR ATT LÄGGA Till PRODUKT SOM FUNKADE EJ
                    if(hmKategori.containsValue(comboBoxKategori.getSelectionModel().getSelectedItem())) {
                        for (HashMap.Entry<Integer, String> entry : hmKategori.entrySet()){
                            int valueKey = entry.getKey();
                            psProduct.setInt(5, valueKey);
                        }

                    }

                     */

                    psProduct.executeUpdate();

                    PreparedStatement psLagerPlats = conn.prepareStatement("UPDATE lagerplats SET tillganglighet = 1 WHERE namn = ?");
                    psLagerPlats.setString(1, comboBoxLagerPlats.getSelectionModel().getSelectedItem());

                    psLagerPlats.executeUpdate();


                    Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
                    informationAlert.setTitle("Meddelande");
                    informationAlert.setHeaderText("Produkt tillagd!");
                    informationAlert.showAndWait();

                    comboBoxLagerPlats.getItems().clear();
                    comboBoxLagerPlats.getItems().addAll(getLagerplats());

                    /* FELSÖKNING för att skriva ut vilka lagerplatser som visas efter insättning
                    for(String str: getLagerplats()) {
                        System.out.println(str);
                    }


                     */
                    artikelNmr.clear();
                    artikelNamn.clear();
                    antal.clear();
                    comboBoxKategori.setValue(null);
                    comboBoxLagerPlats.setValue(null);
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        });

        return vBox;
    }


    public VBox andraProdukt() {
        VBox vBox = new VBox();
        vBox.setId("vBox");

        Label rubrik = new Label("Ändra produkt");
        rubrik.setFont(Font.font("Helvetica", FontWeight.BOLD,40));
        vBox.setAlignment(Pos.TOP_CENTER);

        tableView2.setEditable(true);

        TableColumn<Produkt, Integer> artikelNummer = new TableColumn<>("Artikelnummer");
        artikelNummer.setMinWidth(150);
        artikelNummer.setCellValueFactory(new PropertyValueFactory<>("artikelNummer"));

        TableColumn<Produkt, String> artikelNamn = new TableColumn<>("Artikelnamn");
        artikelNamn.setMinWidth(150);
        artikelNamn.setCellValueFactory(new PropertyValueFactory<>("artikelNamn"));


        TableColumn<Produkt, Integer> antal = new TableColumn<>("Antal");
        antal.setMinWidth(150);
        antal.setCellValueFactory(new PropertyValueFactory<Produkt, Integer>("antal"));
        antal.setCellFactory(TextFieldTableCell.<Produkt, Integer>forTableColumn(new IntegerStringConverter()));
        antal.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Produkt, Integer>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Produkt, Integer> t) {
                        ((Produkt) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setAntal(t.getNewValue());


                        try(Connection conn = getConnection()) {

                            PreparedStatement psAntal = conn.prepareStatement("UPDATE produkt SET antal = ? WHERE artikelNummer = ?");
                            psAntal.setInt(1, tableView2.getSelectionModel().getSelectedItem().getAntal());
                            psAntal.setInt(2, tableView2.getSelectionModel().getSelectedItem().getArtikelNummer());
                            psAntal.executeUpdate();

                        } catch (SQLException e) {
                            e.getMessage();
                        }

                    }
                }
        );

        TableColumn<Produkt, String> lagerPlats = new TableColumn<>("Lagerplats");
        lagerPlats.setMinWidth(150);
        lagerPlats.setCellValueFactory(new PropertyValueFactory<Produkt, String >("lagerPlats"));
        lagerPlats.setCellFactory(TextFieldTableCell.forTableColumn());
        lagerPlats.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Produkt, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Produkt, String> t) {
                        ((Produkt) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setLagerPlats(t.getNewValue());

                        /* EVENTUELL KOD FöR ATT KUNNA ÄNDRA LAGERPLATS
                        try(Connection conn = getConnection()) {

                            PreparedStatement psLagerPlats = conn.prepareStatement("UPDATE lagerplats SET antal = ? WHERE artikelNummer = ?");
                            psAntal.setInt(1, hp2.getSelectionModel().getSelectedItem().getAntal());
                            psAntal.setInt(2, hp2.getSelectionModel().getSelectedItem().getArtikelNummer());
                            psAntal.executeUpdate();

                        } catch (SQLException e) {
                            e.getMessage();
                        }
                        */

                    }
                }
        );

        //Kod för att ta bort extra kolumnen som kommer upp till höger i tableviewn, utrymmet i tableviewn fördelas mellan de olika kolumnerna dvs
        tableView2.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        try(Connection conn = getConnection()) {                              //DriverManager.getConnection( "jdbc:mysql://localhost/lagerhanteringsystem?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "1234"
            Statement statement = conn.createStatement();
            ResultSet resultProdukter = statement.executeQuery("SELECT produkt.artikelNummer, produkt.artikelNamn, produkt.antal, produkt.lagerPlats FROM produkt ORDER BY artikelNamn ASC;");
            tableView2.getColumns().clear();
            tableView2.getItems().clear();

            while (resultProdukter.next()) {
                Produkt tmp = new Produkt(resultProdukter.getInt("artikelNummer"), resultProdukter.getString("artikelNamn"),
                        resultProdukter.getInt("antal"), resultProdukter.getString("lagerPlats"));

                observableList2.add(tmp);

            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        tableView2.setItems(observableList2);
        tableView2.getColumns().addAll(artikelNummer, artikelNamn, antal, lagerPlats);

        Button taBort = new Button("Ta bort");

        taBort.setOnAction(event -> {

            try(Connection conn = getConnection()) {


                if(tableView2.getSelectionModel().getSelectedItem() == null) {

                    Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
                    informationAlert.setTitle("Meddelande!");
                    informationAlert.setHeaderText("Du måste välja en produkt att ta bort!");
                    informationAlert.showAndWait();


                } else {


                    PreparedStatement psTaBort = conn.prepareStatement("DELETE FROM produkt WHERE artikelNummer = ?");
                    psTaBort.setInt(1, tableView2.getSelectionModel().getSelectedItem().getArtikelNummer());

                    psTaBort.executeUpdate();

                    PreparedStatement psLagerPlats = conn.prepareStatement("UPDATE lagerplats SET tillganglighet = 0 WHERE namn = ?");
                    psLagerPlats.setString(1, tableView2.getSelectionModel().getSelectedItem().getLagerPlats());

                    psLagerPlats.executeUpdate();

                    Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
                    informationAlert.setTitle("Meddelande");
                    informationAlert.setHeaderText("Din valda produkt kommer att tas bort!");
                    informationAlert.showAndWait();

                    tableView2.getItems().remove(tableView2.getSelectionModel().getSelectedItem());

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        });

        vBox.getChildren().addAll(rubrik, tableView2, taBort);

        return vBox;
    }

    public VBox laggTillLagerPlats() {
        VBox vBox = new VBox();
        vBox.setId("vBox");
        vBox.setAlignment(Pos.TOP_CENTER);

        GridPane pane = new GridPane();
        pane.setPadding(new Insets(50,10,10,10)); //v1=top, v2=right, v3=bottom, v4=left
        pane.setHgap(20);
        pane.setVgap(25);
        pane.setAlignment(Pos.CENTER);

        Label rubrik = new Label("Lägg till lagerplats");
        rubrik.setFont(Font.font("Helvetica", FontWeight.BOLD,40));

        Label lblLagerPlatsNamn = new Label("Namn:");
        TextField lagerPlatsNamn = new TextField();
        pane.add(lblLagerPlatsNamn, 0, 1);
        pane.add(lagerPlatsNamn, 1, 1);

        Button lpLaggTill = new Button("Lägg till");
        Button lpAvbryt = new Button("Avbryt");

        pane.add(lpLaggTill,0, 2);
        pane.add(lpAvbryt, 1, 2);

        vBox.getChildren().addAll(rubrik, pane);

        lpAvbryt.setOnAction(event -> {
            root.setCenter(middlePane());
            addLagerplats.getStyleClass().remove("active");
        });

        lpLaggTill.setOnAction(event -> {

            try(Connection conn = getConnection()) {

                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                if (lagerPlatsNamn.getText().isEmpty()) {
                    errorAlert.setTitle("Meddelande");
                    errorAlert.setHeaderText("Problem");
                    errorAlert.setContentText("Du måste skriva in ett namn för lagerplatsen!");
                    errorAlert.showAndWait();

                } else {

                    PreparedStatement ps = conn.prepareStatement("INSERT INTO lagerplats VALUES (?, ?)");
                    ps.setString(1, lagerPlatsNamn.getText());
                    ps.setInt(2, 0);

                    ps.executeUpdate();

                    Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
                    informationAlert.setTitle("Meddelande");
                    informationAlert.setHeaderText("Lagerplats tillagd!");
                    informationAlert.showAndWait();

                    lagerPlatsNamn.clear();
                }

            } catch (SQLException e) {
                System.out.println("Något gick fel " + e.getMessage());
            }

        });

        return vBox;
    }


    public VBox taBortLagerplats() {
        VBox vBox = new VBox();
        vBox.setId("vBox");

        Label rubrik = new Label("Ta bort lagerplats");
        rubrik.setFont(Font.font("Helvetica", FontWeight.BOLD,40));
        vBox.setAlignment(Pos.TOP_CENTER);

        TableColumn<LagerPlats, String> lagerPlatsNamn = new TableColumn<>("Namn");
        lagerPlatsNamn.setMinWidth(150);
        lagerPlatsNamn.setCellValueFactory(new PropertyValueFactory<>("namn"));

        TableColumn<LagerPlats, Integer> tillganglighet = new TableColumn<>("Tillgänglighet");
        tillganglighet.setMinWidth(150);
        tillganglighet.setCellValueFactory(new PropertyValueFactory<>("tillganglighet"));

        //Kod för att ta bort extra kolumnen som kommer upp till höger i tableviewn, utrymmet i tableviewn fördelas mellan de olika kolumnerna dvs
        tableView3.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        try(Connection conn = getConnection()) {                              //DriverManager.getConnection( "jdbc:mysql://localhost/lagerhanteringsystem?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "1234"
            Statement statement = conn.createStatement();
            ResultSet resultLagerplats = statement.executeQuery("SELECT lagerplats.namn, lagerplats.tillganglighet FROM lagerplats WHERE lagerplats.tillganglighet = 0 ORDER BY lagerplats.namn ASC;");

            tableView3.getColumns().clear();
            tableView3.getItems().clear();

            while (resultLagerplats.next()) {
                LagerPlats tmp = new LagerPlats(resultLagerplats.getString("namn"), resultLagerplats.getInt("tillganglighet"));

                observableList3.add(tmp);

            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        tableView3.setItems(observableList3);
        tableView3.getColumns().addAll(lagerPlatsNamn, tillganglighet);

        Button taBort = new Button("Ta bort");

        taBort.setOnAction(event -> {

            try(Connection conn = getConnection()) {

                if(tableView3.getSelectionModel().getSelectedItem() == null) {

                    Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
                    informationAlert.setTitle("Meddelande!");
                    informationAlert.setHeaderText("Du måste välja en lagerplats att ta bort!");
                    informationAlert.showAndWait();

                } else {

                    PreparedStatement psTaBort = conn.prepareStatement("DELETE FROM lagerplats WHERE namn = ?");
                    psTaBort.setString(1, tableView3.getSelectionModel().getSelectedItem().getNamn());

                    psTaBort.executeUpdate();

                    Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
                    informationAlert.setTitle("Meddelande");
                    informationAlert.setHeaderText("Din valda lagerplats kommer att tas bort!");
                    informationAlert.showAndWait();

                    tableView3.getItems().remove(tableView3.getSelectionModel().getSelectedItem());

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        });

        vBox.getChildren().addAll(rubrik, tableView3, taBort);
        return vBox;
    }

    public VBox laggTillKategori() {

        hmKategori = getKategori();

        VBox vBox = new VBox();
        vBox.setId("vBox");
        vBox.setAlignment(Pos.TOP_CENTER);

        GridPane pane = new GridPane();
        pane.setPadding(new Insets(50,10,10,10)); //v1=top, v2=right, v3=bottom, v4=left
        pane.setHgap(20);
        pane.setVgap(25);
        pane.setAlignment(Pos.CENTER);

        Label rubrik = new Label("Lägg till kategori");
        rubrik.setFont(Font.font("Helvetica", FontWeight.BOLD,40));

        /*
        Label lblKategoriID = new Label("Kategori ID:");
        TextField kategoriID = new TextField();
        pane.add(lblKategoriID, 0, 1);
        pane.add(kategoriID, 1, 1);
        */

        Label lblKategoriNamn = new Label("Kategori namn:");
        TextField kategoriNamn = new TextField();
        pane.add(lblKategoriNamn, 0, 1);
        pane.add(kategoriNamn, 1, 1);

        Button kgLaggTill = new Button("Lägg till");
        Button kgAvbryt = new Button("Avbryt");

        pane.add(kgLaggTill,0, 2);
        pane.add(kgAvbryt, 1, 2);

        vBox.getChildren().addAll(rubrik, pane);

        kgAvbryt.setOnAction(event -> {
            root.setCenter(middlePane());
            addKategori.getStyleClass().remove("active");
        });

        kgLaggTill.setOnAction(event -> {

            try(Connection conn = getConnection()) {

                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                if (kategoriNamn.getText().isEmpty()) {
                    errorAlert.setTitle("Meddelande");
                    errorAlert.setHeaderText("Problem");
                    errorAlert.setContentText("Du måste fylla i alla fält för att kunna lägga till en kategori!");
                    errorAlert.showAndWait();

                } else if (hmKategori.containsValue(kategoriNamn.getText())){
                    errorAlert.setTitle("Meddelande");
                    errorAlert.setHeaderText("Problem");
                    errorAlert.setContentText("Det kategorinamnet finns redan");
                    errorAlert.showAndWait();
                } else {

                    int id = 1;

                    while(true) {
                        if(!hmKategori.containsKey(id)) {
                            break;
                        } else {
                            id++;
                        }
                    }
                    PreparedStatement ps = conn.prepareStatement("INSERT INTO kategori VALUES (?, ?)");
                    ps.setInt(1, id);
                    ps.setString(2, kategoriNamn.getText());


                    /*
                    for (HashMap.Entry<Integer, String> entry : hmKategori.entrySet()){
                        Integer valueKey = entry.getKey();
                        String value = comboBoxKategori.getSelectionModel().getSelectedItem();
                        if(entry.getValue().equals(value)) {
                        psProduct.setInt(5, valueKey);
                    }
                    }
                    */
                    ps.executeUpdate();
                    hmKategori = getKategori();

                    Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
                    informationAlert.setTitle("Meddelande");
                    informationAlert.setHeaderText("Kategori tillagd!");
                    informationAlert.showAndWait();

                    kategoriNamn.clear();
                }

            } catch (SQLException e) {
                System.out.println("Något gick fel " + e.getMessage());
            }

        });

        return vBox;
    }

    public VBox taBortKategori() {
        VBox vBox = new VBox();
        vBox.setId("vBox");

        Label rubrik = new Label("Ta bort kategori");
        rubrik.setFont(Font.font("Helvetica", FontWeight.BOLD,40));
        vBox.setAlignment(Pos.TOP_CENTER);

        TableColumn<Kategori, Integer> kategoriID = new TableColumn<>("ID");
        kategoriID.setMinWidth(150);
        kategoriID.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Kategori, String> kategoriNamn = new TableColumn<>("Namn");
        kategoriNamn.setMinWidth(150);
        kategoriNamn.setCellValueFactory(new PropertyValueFactory<>("namn"));

        //Kod för att ta bort extra kolumnen som kommer upp till höger i tableviewn, utrymmet i tableviewn fördelas mellan de olika kolumnerna dvs
        tableView4.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        try(Connection conn = getConnection()) {                              //DriverManager.getConnection( "jdbc:mysql://localhost/lagerhanteringsystem?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "1234"
            Statement statement = conn.createStatement();
            ResultSet resultKategori = statement.executeQuery("SELECT DISTINCT kategori.id, kategori.namn FROM kategori WHERE kategori.id NOT IN (SELECT DISTINCT kategoriID FROM produkt)");

            tableView4.getColumns().clear();
            tableView4.getItems().clear();

            while (resultKategori.next()) {
                Kategori tmp = new Kategori(resultKategori.getInt(1), resultKategori.getString(2));

                observableList4.add(tmp);

            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        tableView4.setItems(observableList4);
        tableView4.getColumns().addAll(kategoriID, kategoriNamn);

        Button taBort = new Button("Ta bort");

        taBort.setOnAction(event -> {

            try(Connection conn = getConnection()) {

                if(tableView4.getSelectionModel().getSelectedItem() == null) {

                    Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
                    informationAlert.setTitle("Meddelande!");
                    informationAlert.setHeaderText("Du måste välja en kategori att ta bort!");
                    informationAlert.showAndWait();

                } else {

                    PreparedStatement psTaBort = conn.prepareStatement("DELETE FROM kategori WHERE id = ?");
                    psTaBort.setInt(1, tableView4.getSelectionModel().getSelectedItem().getId());

                    psTaBort.executeUpdate();

                    Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
                    informationAlert.setTitle("Meddelande");
                    informationAlert.setHeaderText("Din valda kategori kommer att tas bort!");
                    informationAlert.showAndWait();

                    tableView4.getItems().remove(tableView4.getSelectionModel().getSelectedItem());

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        });

        vBox.getChildren().addAll(rubrik, tableView4, taBort);
        return vBox;
    }

    public VBox sokProdukt() {
        VBox vBox = new VBox();
        vBox.setId("vBox");
        hmKategori = getKategori();

        GridPane pane = new GridPane();
        pane.setPadding(new Insets(20,10,10,20));
        pane.setHgap(45);
        pane.setVgap(5);
        pane.setId("gridPane");

        Label rubrik = new Label("Sök");
        rubrik.setFont(Font.font("Helvetica", FontWeight.BOLD,40));
        vBox.setAlignment(Pos.TOP_CENTER);

        pane.add(new Label("Artikelnummer: "),0,0);
        TextField textField = new TextField();
        pane.add(textField,0, 1);

        pane.add(new Label("Artikelnamn:"), 1, 0);
        TextField textField1 = new TextField();
        pane.add(textField1,1, 1);

        pane.add(new Label("Kategori:"), 2, 0);

        /*
        ComboBox<String> comboBoxKategori = new ComboBox<>();
        for (HashMap.Entry<Integer, String> entry : hmKategori.entrySet()){
            String value = entry.getValue();
            comboBoxKategori.getItems().addAll(value);
        }
        */
        ChoiceBox<String> choiceBoxKategori = new ChoiceBox<>();
        for (HashMap.Entry<Integer, String> entry : hmKategori.entrySet()){
            String value = entry.getValue();
            choiceBoxKategori.getItems().addAll(value);
        }
        choiceBoxKategori.setPrefWidth(150);
        pane.add(choiceBoxKategori, 2, 1);

        Button sok = new Button("Sök");
        pane.add(sok,3,1);

        TableColumn<ProduktKategori, Integer> artikelNummer = new TableColumn<>("Artikelnummer");
        artikelNummer.setMinWidth(150);
        artikelNummer.setCellValueFactory(new PropertyValueFactory<>("artikelNummer"));
        TableColumn<ProduktKategori, String> artikelNamn = new TableColumn<>("Artikelnamn");
        artikelNamn.setMinWidth(150);
        artikelNamn.setCellValueFactory(new PropertyValueFactory<>("artikelNamn"));
        TableColumn<ProduktKategori, Integer> antal = new TableColumn<>("Antal");
        antal.setMinWidth(150);
        antal.setCellValueFactory(new PropertyValueFactory<>("antal"));
        TableColumn<ProduktKategori, String> kategoriNamn = new TableColumn<>("Kategori");
        kategoriNamn.setMinWidth(100);
        kategoriNamn.setCellValueFactory(new PropertyValueFactory<>("Kategori"));

        /*
        try(Connection conn = getConnection()){
            Statement statement = conn.createStatement();
            ResultSet resultProdukter = statement.executeQuery("SELECT produkt.artikelNummer, produkt.artikelNamn, kategori.namn, produkt.antal FROM kategori, produkt WHERE produkt.kategoriID=kategori.id ORDER BY artikelNamn ASC;");
            tableView1.getColumns().clear();
            tableView1.getItems().clear();

            while (resultProdukter.next()) {
                Produkt tmp = new Produkt(resultProdukter.getInt("artikelNummer"), resultProdukter.getString("artikelNamn"),
                        resultProdukter.getInt("antal"));
               // Kategori tmp2 = new Kategori(resultProdukter.getString("namn"));
                observableList1.add(tmp);
               // data.add(tmp2);
                //listView.getItems().addAll(resultProdukter.getString("artikelNamn"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
         */

        tableView1.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        sok.setOnAction(event ->{

            try (Connection conn = getConnection()) {

                PreparedStatement ps;
                if (!textField.getText().isEmpty()) {
                    ps = conn.prepareStatement("SELECT * FROM produkt_kategori WHERE artikelNummer LIKE ?");
                    ps.setString(1,textField.getText() + "%");
                    ResultSet resultProdukter2 = ps.executeQuery();
                    tableView1.getColumns().clear();
                    tableView1.getItems().clear();
                    while (resultProdukter2.next()) {
                        ProduktKategori tmps = new ProduktKategori(resultProdukter2.getInt("artikelNummer"), resultProdukter2.getString("artikelNamn"),
                                resultProdukter2.getInt("antal"), resultProdukter2.getString("Kategori"));
                        observableList1.add(tmps);
                    }
                }
                else if (!textField1.getText().isEmpty()) {
                    ps = conn.prepareStatement("SELECT * FROM produkt_kategori WHERE artikelNamn LIKE ?");
                    ps.setString(1,textField1.getText() + "%");
                    ResultSet resultProdukter2 = ps.executeQuery();
                    tableView1.getColumns().clear();
                    tableView1.getItems().clear();
                    while (resultProdukter2.next()) {
                        ProduktKategori tmps = new ProduktKategori(resultProdukter2.getInt("artikelNummer"), resultProdukter2.getString("artikelNamn"),
                                resultProdukter2.getInt("antal"), resultProdukter2.getString("Kategori"));
                        observableList1.add(tmps);
                    }
                }
                else if (!choiceBoxKategori.getSelectionModel().getSelectedItem().toString().equals(null)) {
                    ps = conn.prepareStatement("SELECT * FROM produkt_kategori WHERE Kategori LIKE ?");
                    ps.setString(1,choiceBoxKategori.getSelectionModel().getSelectedItem().toString() + "%");
                    ResultSet resultProdukter2 = ps.executeQuery();
                    tableView1.getColumns().clear();
                    tableView1.getItems().clear();
                    while (resultProdukter2.next()) {
                        ProduktKategori tmps = new ProduktKategori(resultProdukter2.getInt("artikelNummer"), resultProdukter2.getString("artikelNamn"),
                                resultProdukter2.getInt("antal"), resultProdukter2.getString("Kategori"));
                        observableList1.add(tmps);
                    }
                }



            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            tableView1.setItems(observableList1);
            tableView1.getColumns().addAll(artikelNummer, artikelNamn, antal, kategoriNamn);
        });
        try(Connection conn = getConnection()) {
            Statement statement = conn.createStatement();
            ResultSet resultProdukter = statement.executeQuery("SELECT * FROM produkt_kategori");
            tableView1.getColumns().clear();
            tableView1.getItems().clear();

            while (resultProdukter.next()) {
                ProduktKategori tmp = new ProduktKategori(resultProdukter.getInt("artikelNummer"), resultProdukter.getString("artikelNamn"),
                        resultProdukter.getInt("antal"), resultProdukter.getString("Kategori"));

                observableList1.addAll(tmp);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        tableView1.setItems(observableList1);
        tableView1.getColumns().addAll(artikelNummer, artikelNamn, antal, kategoriNamn);


        vBox.getChildren().addAll(rubrik, pane, tableView1);

        return vBox;

    }

    private HashMap<Integer, String> getKategori() {

        HashMap<Integer, String> listKategorier = new HashMap<>();

        try(Connection conn = getConnection()) {

            Statement kategoriStatement = conn.createStatement();

            ResultSet resultKategori = kategoriStatement.executeQuery("SELECT * FROM kategori");

            while (resultKategori.next()) {
                listKategorier.put(resultKategori.getInt("id"), resultKategori.getString("namn"));
            }

        } catch (SQLException e) {
            System.out.println("Something went wrong..... " + e.getMessage());
        }

        return listKategorier;
    }

    private ArrayList<String> getLagerplats() {

        ArrayList<String> listLP = new ArrayList<>();

        try(Connection conn = getConnection()) {

            Statement lagerPLatsStatement = conn.createStatement();

            ResultSet resultLP = lagerPLatsStatement.executeQuery("SELECT namn FROM lagerplats WHERE tillganglighet = 0;");

            while (resultLP.next()) {
                listLP.add(resultLP.getString("namn"));
            }

        } catch (SQLException e) {
            System.out.println("Something went wrong..... " + e.getMessage());
        }

        return listLP;
    }

    private static void init(String filename) {
        Properties props = new Properties();

        try(FileInputStream in = new FileInputStream(filename)) {
            props.load(in);
            String driver = props.getProperty("jdbc.driver");
            url = props.getProperty("jdbc.url");
            username = props.getProperty("jdbc.username");
            if(username == null) {
                username = "";
            }
            password = props.getProperty("jdbc.password");
            if(password == null) {
                password = "";
            }
            if(driver != null) {
                Class.forName(driver);
            }

        } catch (IOException ex) {
            System.out.println("Something went wrong...." + ex.getStackTrace().toString());
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Unable to load driver." + cnfe.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }




}
