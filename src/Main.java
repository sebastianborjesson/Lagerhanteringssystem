import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
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




public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        BorderPane root = new BorderPane();
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

        Group buttons = new Group();
        Button home = new Button("Hem");
        home.setLayoutX(-160);
        Button signOut = new Button("Logga ut");
        buttons.getChildren().addAll(home, signOut);

        Image user = new Image("file:images/user1.png");
        ImageView user1 = new ImageView(user);
        user1.setFitWidth(30);
        user1.setFitHeight(30);

        top.getChildren().addAll(systemName, buttons, user1);

        return top;

    }

    public /*VBox*/ GridPane leftPane() { //Om vi väljer att köra med VBOX då är det koden under.

        GridPane root = new GridPane();
        root.setId("vBox");
        TitledPane product = new TitledPane();
        product.setText("Produkt");


        VBox produktKnappar = new VBox();
        produktKnappar.setStyle("-fx-background-color: gainsboro");

        Button addProduct = new Button("Lägg till produkt");
        Button changeProduct = new Button("Ändra produkt");
        produktKnappar.getChildren().addAll(addProduct, changeProduct);
        addProduct.setId("menuBtn");
        changeProduct.setId("menuBtn");

        product.setContent(produktKnappar);

        Accordion dragspel = new Accordion();
        dragspel.getPanes().addAll(product);

        VBox knapparUnderTitledPane = new VBox();
        Button search = new Button("Search");
        search.setMaxWidth(dragspel.getPrefWidth());
        search.setId("menuBtn");

        knapparUnderTitledPane.getChildren().addAll(search);
        knapparUnderTitledPane.setAlignment(Pos.CENTER);
        knapparUnderTitledPane.setPadding(new Insets(10, 10, 10, 10));

        root.add(dragspel, 0, 0);
        root.add(knapparUnderTitledPane, 0, 10);

        root.setStyle("-fx-background-color: gainsboro");


        addProduct.setOnAction(event -> {
            middlePane();
        });

        return root;
    }
        /*
        VBox menuLeft= new VBox();
        menuLeft.setStyle("-fx-background-color: gainsboro");
        menuLeft.setFillWidth(true);

        Button product = new Button("Produkt");
        Button addProduct = new Button("Lägg till produkt");
        Button changeProduct = new Button("Ändra produkt");
        Button search = new Button("Search");

        product.setMinWidth(menuLeft.getPrefWidth());
        addProduct.setMinWidth(menuLeft.getPrefWidth());
        changeProduct.setMinWidth(menuLeft.getPrefWidth());
        search.setMinWidth(menuLeft.getPrefWidth());

        menuLeft.getChildren().addAll(product, addProduct, changeProduct, search);

        return menuLeft;
        */


        public VBox middlePane() {
            /*
            GridPane root = new GridPane();

            Label rubrik = new Label("Lägg till produkt");
            rubrik.setFont(Font.font("Helvetica", FontWeight.BOLD,30));


            Label artikelNummer = new Label("Artikelnummer:");
            Label artikelNamn = new Label("Artikelnamn:");
            Label antal = new Label("Antal:");
            Label kategori = new Label("Kategori:");
            Label lagerPlats = new Label("Lagerplats:");

            root.add(rubrik, 1, 0);
            root.add(artikelNummer, 0, 1);
            root.add(artikelNamn, 0, 2);
            root.add(antal, 0, 3);
            root.add(kategori, 0, 4);
            root.add(lagerPlats, 0, 5);

            root.setPadding(new Insets(10, 10, 10, 10));
            return root;

             */
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
