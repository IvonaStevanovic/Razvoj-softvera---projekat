package org.raflab.studsluzbadesktopclient.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import org.raflab.studsluzbadesktopclient.MainView;
import org.raflab.studsluzbadesktopclient.coder.CoderFactory;
import org.raflab.studsluzbadesktopclient.coder.CoderType;
import org.raflab.studsluzbadesktopclient.coder.SimpleCode;
import org.raflab.studsluzbadesktopclient.dtos.*;
import org.raflab.studsluzbadesktopclient.services.NavigationService;
import org.raflab.studsluzbadesktopclient.services.SifarniciService;
import org.raflab.studsluzbadesktopclient.services.StudentService;
import org.springframework.stereotype.Component;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.raflab.studsluzbadesktopclient.dtos.*;
import java.util.List;

@Component
public class StudentController {

    private final StudentService studentService;
    private final CoderFactory coderFactory;
    private final MainView mainView;
    private final SifarniciService sifarniciService;
    private final NavigationService navigationService;

    // Osnovni podaci
    @FXML private TextField imeTf, prezimeTf, srednjeImeTf, jmbgTf,
             nacionalnostTf, brojLicneKarteTf,
            godinaUpisaTf, brojIndeksaTf, godinaIndeksaTf, uspehSrednjaSkolaTf, uspehPrijemniTf;
    @FXML private RadioButton muski, zenski;
    @FXML private DatePicker datumRodjenjaDp, datumAktivacijeDp;
    @FXML private ComboBox<SimpleCode> mestoRodjenjaCb, mestoStanovanjaCb, drzavaRodjenjaCb, drzavljanstvoCb;
    @FXML private ComboBox<SrednjaSkolaDTO> srednjaSkolaCb;
    @FXML private Label labelError;
    @FXML private TextField adresaTf;
    @FXML private TextField emailPrivatniTf;
    @FXML private TextField emailFakultetTf;
    @FXML private TextField brojTelefonaTf;
    @FXML private VBox korenskiVBox;
    @FXML private TableView<PolozeniPredmetiResponse> polozeniTable;
    @FXML private TableColumn<PolozeniPredmetiResponse, String> predmetPolozioCol;
    @FXML private TableColumn<PolozeniPredmetiResponse, Integer> ocenaCol;
    @FXML private TableColumn<PolozeniPredmetiResponse, String> datumPolaganjaCol;
    @FXML private TableColumn<PolozeniPredmetiResponse, Integer> espbPolozioCol;
    @FXML private TabPane profilTabPane;
    @FXML private TableView<NepolozeniPredmetDTO> nepolozeniTable;
    @FXML private TableColumn<NepolozeniPredmetDTO, String> predmetNepolozenCol;
    @FXML private TableColumn<NepolozeniPredmetDTO, Integer> espbNepolozenCol;

    // Tabela za Finansije
    @FXML private TableView<UplataResponse> uplateTable;
    @FXML private TableColumn<UplataResponse, String> datumUplateCol;
    @FXML private TableColumn<UplataResponse, Double> iznosCol;
    @FXML private TableColumn<UplataResponse, String> svrhaCol;

    // Akademski status (Labele)
    @FXML private Label ukupnoEspbLabel;
    @FXML private Label prosekLabel;

    public StudentController(StudentService studentService, CoderFactory coderFactory,
                             MainView mainView, SifarniciService sifarniciService,
                             NavigationService navigationService) {
        this.studentService = studentService;
        this.coderFactory = coderFactory;
        this.mainView = mainView;
        this.sifarniciService = sifarniciService;
        this.navigationService = navigationService;
    }

    @FXML
    public void initialize() {
        setupCoders();
        setupTableColumns();
        updateSrednjeSkole();

        // Čekamo da se TabPane zakači za scenu da bismo dodali prečice
        if (profilTabPane != null) {
            profilTabPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
                if (newScene != null) {
                    newScene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                        // CTRL + [ (Prečica iz specifikacije)
                        if (event.isControlDown() && event.getCode() == KeyCode.OPEN_BRACKET) {
                            handleBack(null);
                            event.consume();
                        }
                        // ESC (Dodatna prečica radi sigurnosti)
                        else if (event.getCode() == KeyCode.ESCAPE) {
                            handleBack(null);
                            event.consume();
                        }
                    });
                }
            });
        }
    }

    private void setupCoders() {

        if (drzavaRodjenjaCb != null)
            drzavaRodjenjaCb.setItems(FXCollections.observableArrayList(coderFactory.getSimpleCoder(CoderType.DRZAVA).getCodes()));

        if (drzavljanstvoCb != null)
            drzavljanstvoCb.setItems(FXCollections.observableArrayList(coderFactory.getSimpleCoder(CoderType.DRZAVA).getCodes()));

        if (mestoRodjenjaCb != null)
            mestoRodjenjaCb.setItems(FXCollections.observableArrayList(coderFactory.getSimpleCoder(CoderType.MESTO).getCodes()));

        if (mestoStanovanjaCb != null) // Ovo je bacalo grešku
            mestoStanovanjaCb.setItems(FXCollections.observableArrayList(coderFactory.getSimpleCoder(CoderType.MESTO).getCodes()));
    }

    private void setupTableColumns() {
        // Kolone za položene
        predmetPolozioCol.setCellValueFactory(new PropertyValueFactory<>("nazivPredmeta"));
        ocenaCol.setCellValueFactory(new PropertyValueFactory<>("ocena"));
        datumPolaganjaCol.setCellValueFactory(new PropertyValueFactory<>("datumPolaganja"));
        espbPolozioCol.setCellValueFactory(new PropertyValueFactory<>("espb"));

        // Kolone za nepoložene
        predmetNepolozenCol.setCellValueFactory(new PropertyValueFactory<>("nazivPredmeta"));
        espbNepolozenCol.setCellValueFactory(new PropertyValueFactory<>("espb"));

        // Kolone za uplate
        datumUplateCol.setCellValueFactory(new PropertyValueFactory<>("datumUplate"));
        iznosCol.setCellValueFactory(new PropertyValueFactory<>("iznos"));
        svrhaCol.setCellValueFactory(new PropertyValueFactory<>("svrhaUplate"));
    }

    public void prikaziStudenta(StudentPodaciResponse student) {
        if (student == null) return;

        // 1. Popunjavanje osnovnih polja (provera null za svako polje da ne pukne)
        if (imeTf != null) imeTf.setText(student.getIme());
        if (prezimeTf != null) prezimeTf.setText(student.getPrezime());
        if (srednjeImeTf != null) srednjeImeTf.setText(student.getSrednjeIme() != null ? student.getSrednjeIme() : "");
        if (jmbgTf != null) jmbgTf.setText(student.getJmbg());
        if (adresaTf != null) adresaTf.setText(student.getAdresa());
        if (brojIndeksaTf != null) brojIndeksaTf.setText(String.valueOf(student.getBrojIndeksa()));
        if (godinaUpisaTf != null) godinaUpisaTf.setText(String.valueOf(student.getGodinaUpisa()));

        // 2. Podešavanje Radio Button-a za pol
        if (muski != null && zenski != null) {
            if ("MUSKI".equalsIgnoreCase(student.getPol())) muski.setSelected(true);
            else if ("ZENSKI".equalsIgnoreCase(student.getPol())) zenski.setSelected(true);
        }

        // 3. Podešavanje datuma
        if (datumRodjenjaDp != null) datumRodjenjaDp.setValue(student.getDatumRodjenja());

        // 4. NASILNO DODAVANJE PREČICA DIREKTNO NA SCENU (Ovo rešava tvoj problem)
        Platform.runLater(() -> {
            if (korenskiVBox != null && korenskiVBox.getScene() != null) {
                Scene scene = korenskiVBox.getScene();

                // Čistimo stare prečice da se ne dupliraju
                scene.getAccelerators().clear();

                // CTRL + [ (Specifikacija)
                scene.getAccelerators().put(
                        new KeyCodeCombination(KeyCode.OPEN_BRACKET, KeyCombination.CONTROL_ANY),
                        () -> handleBack(null)
                );

                // CTRL + B (Rezervna prečica ako zagrada ne radi na tastaturi)
                scene.getAccelerators().put(
                        new KeyCodeCombination(KeyCode.B, KeyCombination.CONTROL_ANY),
                        () -> handleBack(null)
                );

                // ESCAPE (Najsigurnije za testiranje)
                scene.getAccelerators().put(
                        new KeyCodeCombination(KeyCode.ESCAPE),
                        () -> handleBack(null)
                );

                korenskiVBox.requestFocus();
                System.out.println("DEBUG: Prečice registrovane na sceni.");
            }
        });

        // 5. Učitavanje tabela (ispiti i uplate) i proračun proseka/ESPB
        ucitajDetaljeStudenta(student.getId());
    }

    private void ucitajDetaljeStudenta(Long studentId) {
        try {
            // Dohvatanje podataka sa servisa
            List<PolozeniPredmetiResponse> polozeni = studentService.getPolozeniIspiti(studentId);
            List<NepolozeniPredmetDTO> nepolozeni = studentService.getNepolozeniIspiti(studentId);
            List<UplataResponse> uplate = studentService.getUplate(studentId);

            // Punjenje tabela
            polozeniTable.setItems(FXCollections.observableArrayList(polozeni));
            nepolozeniTable.setItems(FXCollections.observableArrayList(nepolozeni));
            uplateTable.setItems(FXCollections.observableArrayList(uplate));

            // Računanje proseka i ESPB (Sekcija 1 specifikacije)
            int ukupnoEspb = 0;
            double sumaOcena = 0;
            int brojPolozenih = 0;

            for (PolozeniPredmetiResponse p : polozeni) {
                ukupnoEspb += p.getEspb();
                if (p.getOcena() > 5) {
                    sumaOcena += p.getOcena();
                    brojPolozenih++;
                }
            }

            ukupnoEspbLabel.setText(String.valueOf(ukupnoEspb));
            prosekLabel.setText(brojPolozenih > 0 ? String.format("%.2f", sumaOcena / brojPolozenih) : "0.00");

        } catch (Exception e) {
            labelError.setText("Greška pri učitavanju detalja: " + e.getMessage());
        }
    }

    @FXML
    void handleBack(ActionEvent event) {
        navigationService.goBack();
    }

    @FXML
    void handleUpisGodine(ActionEvent event) {
        // TODO: Otvoriti modal za upis godine
    }

    @FXML
    void handleObnovaGodine(ActionEvent event) {
        // TODO: Otvoriti modal za obnovu godine
    }

    public void updateSrednjeSkole() {
        try {
            srednjaSkolaCb.setItems(FXCollections.observableArrayList(sifarniciService.getSrednjeSkole()));
        } catch (Exception e) {
            labelError.setText(e.getMessage());
        }
    }

    // Tvoje postojeće metode za save i reset...
    public void handleSaveStudent(ActionEvent event) {
        // ... tvoj kod za čuvanje ...
    }

    public void handleOpenModalSrednjeSkole(ActionEvent ae) {
        mainView.openModal("addSrednjaSkola");
    }
    @FXML
    void handleUverenje(ActionEvent event) {
        try {
            // Uzimamo ID studenta koji je trenutno prikazan
            // Pretpostavka je da imamo negde sačuvan trenutni studentId ili ga čitamo iz polja
            // Za testiranje koristimo ispis u konzolu, a u sledećem koraku vezujemo JasperReports
            System.out.println("Generisanje uverenja o studiranju...");

            // Poziv servisa za generisanje izveštaja (implementiraćemo ga u ReportsControlleru)
            // reportsService.generateUverenje(trenutniStudent.getId());

            labelError.setText("Uverenje uspešno generisano.");
            labelError.setStyle("-fx-text-fill: green;");
        } catch (Exception e) {
            labelError.setText("Greška pri generisanju uverenja: " + e.getMessage());
            labelError.setStyle("-fx-text-fill: red;");
        }
    }
    @FXML
    public void handleKeyPressed(KeyEvent event) {
        // Provera za CTRL + [ ILI CTRL + B
        if (event.isControlDown() && (event.getCode() == KeyCode.OPEN_BRACKET || event.getCode() == KeyCode.B)) {
            handleBack(null);
            event.consume();
        }
        // ESCAPE uvek radi najbolje za brzo zatvaranje
        else if (event.getCode() == KeyCode.ESCAPE) {
            handleBack(null);
            event.consume();
        }
    }
}