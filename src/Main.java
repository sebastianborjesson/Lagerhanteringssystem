import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.sql.*;


public class Main extends Application {

    BorderPane root = new BorderPane();
    Group buttons = new Group();
    Button home = new Button("Hem");
    Button signOut = new Button("Logga ut");
    Button addProduct = new Button("Lägg till produkt");
    Button changeProduct = new Button("Ändra produkt");
    Button search = new Button("Search");


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        root.setPadding(new Insets(10, 10, 10, 10));

        root.setTop(topPane());
        root.setLeft(leftPane());
        root.setCenter(middlePane());


        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add("file:styles/theStyle.css");
        primaryStage.setTitle("Lagerhanteringssystem");
        primaryStage.setScene(scene); //Sätter Stagen till scenen jag skapar
        primaryStage.show(); //Visar resultatet

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

            GridPane pane = new GridPane();
            pane.setPadding(new Insets(20,10,10,100));
            pane.setHgap(20);
            pane.setVgap(20);

            Label rubrik = new Label("Lägg till produkt");
            rubrik.setFont(Font.font("Helvetica", FontWeight.BOLD,40));
            vBox.setAlignment(Pos.TOP_CENTER);

            Label artikelnummer = new Label("Artikelnummer: ");
            artikelnummer.setPrefWidth(120);
            TextField textField = new TextField();
            pane.add(textField,2, 3);
            pane.add(artikelnummer, 1, 3);

            pane.add(new Label("Artikelnamn:"), 1, 4);
            TextField textField1 = new TextField();
            pane.add(textField1,2, 4);

            pane.add(new Label("Antal:"), 1, 5);
            TextField textField2 = new TextField();
            pane.add(textField2,2, 5);

            pane.add(new Label("Kategori:"), 1, 6);
            ChoiceBox kategori = new ChoiceBox(FXCollections.observableArrayList("Bildskärm", "Tangentbord", "Mus", "Stol"));
            kategori.setPrefWidth(150);
            pane.add(kategori, 2, 6);


            pane.add(new Label("Lagerplats:"), 1, 7);
            TextField textField4 = new TextField();
            pane.add(textField4,2, 7);


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


                Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
                informationAlert.setTitle("Meddelande");
                informationAlert.setHeaderText("Produkt tillagd!");
                informationAlert.showAndWait();

                textField.clear();
                textField1.clear();
                textField2.clear();
                textField4.clear();

            });

            return vBox;
        }

        public VBox andraProdukt() {
            VBox vBox = new VBox();

            vBox.setId("vBox");

            Label rubrik = new Label("Ändra produkt");
            rubrik.setFont(Font.font("Helvetica", FontWeight.BOLD,40));
            vBox.setAlignment(Pos.TOP_CENTER);

            GridPane pane = new GridPane();
            pane.setPadding(new Insets(20,10,10,50));
            pane.setHgap(20);
            pane.setVgap(20);

            Text artikelnr1 = new Text("Artikelnummer");
            artikelnr1.setFont(Font.font("Helvetica", FontWeight.BOLD,14));
            pane.add(artikelnr1, 2, 2);
            Text artikelnamn1 = new Text("Artikelnamn");
            artikelnamn1.setFont(Font.font("Helvetica", FontWeight.BOLD,14));
            pane.add(artikelnamn1, 3, 2);
            Text kategori = new Text("Kategori");
            kategori.setFont(Font.font("Helvetica", FontWeight.BOLD,14));
            pane.add(kategori, 4, 2);


            Label artikelnr2 = new Label("202402");
            artikelnr2.setFont(Font.font("Helvetica", 12));
            pane.add(artikelnr2, 2, 4);
            Label artikelnamn2 = new Label("Logitech G402 Hyperion Fury");
            artikelnamn2.setFont(Font.font("Helvetica", 12));
            pane.add(artikelnamn2, 3, 4);
            Label kategori2 = new Label("Mus");
            kategori2.setFont(Font.font("Helvetica", 12));
            pane.add(kategori2, 4, 4);

            Button andra = new Button("Ändra");
            andra.setMaxSize(100, 10);
            pane.add(andra, 6, 4);
            Button taBort = new Button("Ta bort");
            taBort.setMaxSize(100, 10);
            pane.add(taBort, 7, 4);



            vBox.getChildren().addAll(rubrik, pane);

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

            ListView<String> listView = new ListView<>();
            try(Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost/lagerHanteringSystem?useSSL=false", "root", "1234")) {
                Statement statement = conn.createStatement();
                ResultSet resultProdukter = statement.executeQuery("SELECT * FROM produkt");

                while (resultProdukter.next()) {
                    System.out.println(resultProdukter.getString("artikelNamn"));
                    listView.getItems().addAll(resultProdukter.getString("artikelNamn"));
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

            vBox.getChildren().addAll(rubrik, pane, listView);

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


}
