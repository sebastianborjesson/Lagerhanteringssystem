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
import java.util.Properties;


public class Main extends Application {

    BorderPane root = new BorderPane();
    Group buttons = new Group();
    Button home = new Button("Hem");
    Button signOut = new Button("Logga ut");
    Button addProduct = new Button("Lägg till produkt");
    Button changeProduct = new Button("Ändra produkt");
    Button search = new Button("Search");
    private TableView<Produkt> hp = new TableView<>();
    private TableView<Produkt> hp2 = new TableView<>();
    private ObservableList<Produkt> data = FXCollections.observableArrayList();

    //Tre variabler som behövs för databasen
    private static String url;
    private static String username;
    private static String password;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        init("database.properties");
        root.setPadding(new Insets(10, 10, 10, 10));

        root.setTop(topPane());
        root.setLeft(leftPane());
        root.setCenter(middlePane());

        Scene scene = new Scene(root, 900, 600);
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

        Scene loginscen = new Scene(pane, 900, 600);
        primaryStage.setScene(loginscen);
        primaryStage.show();

        loginButton.setOnAction(event -> {
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
            search.getStyleClass().remove("active");
            root.setCenter(middlePane());

        });

        return top;

    }


    public GridPane leftPane() {

        GridPane root1 = new GridPane();
        root.setId("vBox");
        TitledPane product = new TitledPane();
        product.setText("Produkt");

        VBox produktKnappar = new VBox();
        produktKnappar.setStyle("-fx-background-color: gainsboro");

        produktKnappar.getChildren().addAll(addProduct, changeProduct);
        addProduct.setId("menuBtn");
        changeProduct.setId("menuBtn");

        product.setContent(produktKnappar);

        Accordion dragspel = new Accordion();
        dragspel.getPanes().addAll(product);

        VBox knapparUnderTitledPane = new VBox();
        search.setMaxWidth(dragspel.getPrefWidth());
        search.setId("menuBtn");

        knapparUnderTitledPane.getChildren().addAll(search);
        knapparUnderTitledPane.setAlignment(Pos.CENTER);
        knapparUnderTitledPane.setPadding(new Insets(10, 10, 10, 10));

        root1.add(dragspel, 0, 0);
        root1.add(knapparUnderTitledPane, 0, 10);

        root1.setStyle("-fx-background-color: gainsboro");

        addProduct.setOnAction(event -> {
            changeProduct.getStyleClass().remove("active");
            search.getStyleClass().remove("active");
            addProduct.getStyleClass().add("active");
            root.setCenter(laggTillProdukt());
        });

        changeProduct.setOnAction(event -> {
            addProduct.getStyleClass().remove("active");
            search.getStyleClass().remove("active");
            changeProduct.getStyleClass().add("active");
            root.setCenter(andraProdukt());

        });

        search.setOnAction(event -> {
            search.getStyleClass().add("active");
            changeProduct.getStyleClass().remove("active");
            addProduct.getStyleClass().remove("active");
            root.setCenter(sokProdukt());
        });


        return root1;
    }

    public VBox laggTillProdukt() { // Flöde för att lägga till en produkt

        VBox vBox = new VBox();
        vBox.setId("vBox");
        vBox.setAlignment(Pos.TOP_CENTER);
        HashMap<Integer, String> hmKategori = getKategori();

        GridPane pane = new GridPane();
        pane.setPadding(new Insets(20,10,10,100));
        pane.setHgap(20);
        pane.setVgap(20);

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

        Button laggTill = new Button("OK");
        Button avbryt = new Button("Avbryt");
        pane.add(laggTill,4,14);
        pane.add(avbryt,5,14);

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

                    /*
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

                    for(String str: getLagerplats()) {
                        System.out.println(str);
                    }

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

        hp2.setEditable(true);

        TableColumn<Produkt, Long> artikelNummer = new TableColumn<>("Artikelnummer");
        artikelNummer.setMinWidth(150);
        artikelNummer.setCellValueFactory(new PropertyValueFactory<>("artikelNummer"));

        TableColumn<Produkt, String> artikelNamn = new TableColumn<>("Artikelnamn");
        artikelNamn.setMinWidth(150);
        artikelNamn.setCellValueFactory(new PropertyValueFactory<Produkt, String >("artikelNamn"));


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
                            psAntal.setInt(1, hp2.getSelectionModel().getSelectedItem().getAntal());
                            psAntal.setInt(2, hp2.getSelectionModel().getSelectedItem().getArtikelNummer());
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

                        /*
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

        TableColumn<Kategori, String> kategoriNamn = new TableColumn<>("Kategori");
        kategoriNamn.setMinWidth(100);
        kategoriNamn.setCellValueFactory(new PropertyValueFactory<>("namn"));


        try(Connection conn = getConnection()) {                              //DriverManager.getConnection( "jdbc:mysql://localhost/lagerhanteringsystem?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "1234"
            Statement statement = conn.createStatement();
            ResultSet resultProdukter = statement.executeQuery("SELECT produkt.artikelNummer, produkt.artikelNamn, produkt.antal, produkt.lagerPlats FROM produkt ORDER BY artikelNamn ASC;");
            hp2.getColumns().clear();
            hp2.getItems().clear();

            while (resultProdukter.next()) {
                Produkt tmp = new Produkt(resultProdukter.getInt("artikelNummer"), resultProdukter.getString("artikelNamn"),
                        resultProdukter.getInt("antal"), resultProdukter.getString("lagerPlats"));
                //Kategori tmp2 = new Kategori(resultProdukter.getString("namn"));
                data.add(tmp);
                //data.add(tmp2);
                //listView.getItems().addAll(resultProdukter.getString("artikelNamn"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        hp2.setItems(data);
        hp2.getColumns().addAll(artikelNummer, artikelNamn, antal, lagerPlats);

        Button taBort = new Button("Ta bort");

        taBort.setOnAction(event -> {

            try(Connection conn = getConnection()) {


                if(hp2.getSelectionModel().getSelectedItem() == null) {

                    Alert informationAlert = new Alert(Alert.AlertType.ERROR);
                    informationAlert.setTitle("Meddelande!");
                    informationAlert.setHeaderText("Du måste välja en produkt att ta bort!");
                    informationAlert.showAndWait();


                } else {


                    PreparedStatement psTaBort = conn.prepareStatement("DELETE FROM produkt WHERE artikelNummer = ?");
                    psTaBort.setInt(1, hp2.getSelectionModel().getSelectedItem().getArtikelNummer());

                    psTaBort.executeUpdate();

                    PreparedStatement psLagerPlats = conn.prepareStatement("UPDATE lagerplats SET tillganglighet = 0 WHERE namn = ?");
                    psLagerPlats.setString(1, hp2.getSelectionModel().getSelectedItem().getLagerPlats());

                    psLagerPlats.executeUpdate();

                    Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
                    informationAlert.setTitle("Meddelande");
                    informationAlert.setHeaderText("Din valda produkt har tagits bort!");
                    informationAlert.showAndWait();

                    hp2.getItems().remove(hp2.getSelectionModel().getSelectedItem());

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        });

        vBox.getChildren().addAll(rubrik, hp2, taBort);

        return vBox;
    }

    public VBox sokProdukt() {
        VBox vBox = new VBox();
        vBox.setId("vBox");

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
        ChoiceBox kategori = new ChoiceBox(FXCollections.observableArrayList("Bildskärm", "Tangentbord", "Mus", "Stol"));
        kategori.setPrefWidth(150);
        pane.add(kategori, 2, 1);

        Button sok = new Button("Sök");
        pane.add(sok,3,1);

        TableColumn<Produkt, Integer> artikelNummer = new TableColumn<>("Artikelnummer");
        artikelNummer.setMinWidth(150);
        artikelNummer.setCellValueFactory(new PropertyValueFactory<>("artikelNummer"));
        TableColumn<Produkt, String> artikelNamn = new TableColumn<>("Artikelnamn");
        artikelNamn.setMinWidth(150);
        artikelNamn.setCellValueFactory(new PropertyValueFactory<>("artikelNamn"));
        TableColumn<Produkt, Integer> antal = new TableColumn<>("Antal");
        antal.setMinWidth(150);
        antal.setCellValueFactory(new PropertyValueFactory<>("antal"));
        /*
        TableColumn<Kategori, String> kategoriNamn = new TableColumn<>("Kategori");
        kategoriNamn.setMinWidth(100);
        kategoriNamn.setCellValueFactory(new PropertyValueFactory<>("namn"));

         */


        try(Connection conn = getConnection()){     /*DriverManager.getConnection( "jdbc:mysql://localhost/lagerhanteringsystem?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "1234"*/
            Statement statement = conn.createStatement();
            ResultSet resultProdukter = statement.executeQuery("SELECT produkt.artikelNummer, produkt.artikelNamn, kategori.namn, produkt.antal FROM kategori, produkt WHERE produkt.kategoriID=kategori.id ORDER BY artikelNamn ASC;");
            hp.getColumns().clear();
            hp.getItems().clear();

            while (resultProdukter.next()) {
                Produkt tmp = new Produkt(resultProdukter.getInt("artikelNummer"), resultProdukter.getString("artikelNamn"),
                        resultProdukter.getInt("antal"));
               // Kategori tmp2 = new Kategori(resultProdukter.getString("namn"));
                data.add(tmp);
               // data.add(tmp2);
                //listView.getItems().addAll(resultProdukter.getString("artikelNamn"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        hp.setItems(data);
        hp.getColumns().addAll(artikelNummer, artikelNamn, antal);

        vBox.getChildren().addAll(rubrik, pane, hp);

        return vBox;

    }

    public VBox middlePane() {

        VBox vBox = new VBox();
        vBox.setId("vBox");
        Label rubrik = new Label("Välkommen," + "\n" +  "Nils Nilsson!");
        rubrik.setFont(Font.font("Helvetica", FontWeight.BOLD,40));
        Image smiley = new Image("file:images/smiley.png");

        ImageView visaSmiley = new ImageView(smiley);

        vBox.getChildren().addAll(rubrik, new Text(System.lineSeparator()) , visaSmiley);
        vBox.setAlignment(Pos.CENTER);

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
