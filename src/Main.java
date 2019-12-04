import javafx.application.Application;
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

    public /*VBox*/ GridPane leftPane() { //Om vi väljer att köra med VBOX då är det koden under.


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
       

        public VBox laggTillProdukt() {

            VBox vBox = new VBox();

            vBox.setId("vBox");

            GridPane pane = new GridPane();
            pane.setPadding(new Insets(20,10,10,100));
            pane.setHgap(20);
            pane.setVgap(20);

            Label rubrik = new Label("Lägg till produkt");
            rubrik.setFont(Font.font("Helvetica", FontWeight.BOLD,40));
            vBox.setAlignment(Pos.TOP_CENTER);

            pane.add(new Label("Artikelnummer: "),1,3);
            pane.add(new TextField(),2, 3);

            pane.add(new Label("Artikelnamn:"), 1, 4);
            pane.add(new TextField(),2, 4);

            pane.add(new Label("Antal:"), 1, 5);
            pane.add(new TextField(),2, 5);

            pane.add(new Label("Kategori:"), 1, 6);
            pane.add(new TextField(),2, 6);

            pane.add(new Label("Lagerplats:"), 1, 7);
            pane.add(new TextField(),2, 7);


            Button laggTill = new Button("OK");
            Button avbryt = new Button("Avbryt");
            pane.add(laggTill,4,14);
            pane.add(avbryt,5,14);


            vBox.getChildren().addAll(rubrik,pane);

            avbryt.setOnAction(event -> {
                root.setCenter(middlePane());
                addProduct.getStyleClass().remove("active");
            });

            return vBox;
        }

        public VBox andraProdukt() {
            VBox vBox = new VBox();

            vBox.setId("vBox");

            Label test = new Label("Nu får ni se");

            vBox.getChildren().add(test);

            return vBox;
        }

        public VBox sokProdukt() {
            VBox vBox = new VBox();

            vBox.setId("vBox");

            Label sokRuta = new Label("Här kan man söka");

            vBox.getChildren().add(sokRuta);

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
