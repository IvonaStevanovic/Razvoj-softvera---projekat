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

import java.util.ArrayList;
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
        if (predmetPolozioCol != null) {
            // Mora biti "nazivPredmeta" jer se tako zove varijabla u klijentskom DTO-u
            predmetPolozioCol.setCellValueFactory(new PropertyValueFactory<>("nazivPredmeta"));
        }
        if (ocenaCol != null) {
            ocenaCol.setCellValueFactory(new PropertyValueFactory<>("ocena"));
        }
        if (datumPolaganjaCol != null) {
            datumPolaganjaCol.setCellValueFactory(new PropertyValueFactory<>("datumPolaganja"));
        }
        if (espbPolozioCol != null) {
            espbPolozioCol.setCellValueFactory(new PropertyValueFactory<>("espb"));
        }
        if (predmetNepolozenCol != null) {
            predmetNepolozenCol.setCellValueFactory(new PropertyValueFactory<>("nazivPredmeta"));
        }
        if (espbNepolozenCol != null) {
            espbNepolozenCol.setCellValueFactory(new PropertyValueFactory<>("espb"));
        }
    }
    public void prikaziStudenta(StudentPodaciResponse student) {
        if (student == null) return;

        // --- 1. ZAKLJUČAVANJE POLJA (Read-only režim) ---
        // Proveravamo svaki element pre nego što mu pristupimo
        if (imeTf != null) imeTf.setEditable(false);
        if (prezimeTf != null) prezimeTf.setEditable(false);
        if (srednjeImeTf != null) srednjeImeTf.setEditable(false);
        if (jmbgTf != null) jmbgTf.setEditable(false);
        if (adresaTf != null) adresaTf.setEditable(false);
        if (nacionalnostTf != null) nacionalnostTf.setEditable(false);
        if (brojLicneKarteTf != null) brojLicneKarteTf.setEditable(false);
        if (brojIndeksaTf != null) brojIndeksaTf.setEditable(false);
        if (godinaUpisaTf != null) godinaUpisaTf.setEditable(false);

        // Onemogućavanje izbora za interaktivne elemente
        if (datumRodjenjaDp != null) datumRodjenjaDp.setDisable(true);
        if (muski != null) muski.setDisable(true);
        if (zenski != null) zenski.setDisable(true);
        if (mestoRodjenjaCb != null) mestoRodjenjaCb.setDisable(true);
        if (drzavaRodjenjaCb != null) drzavaRodjenjaCb.setDisable(true);
        if (drzavljanstvoCb != null) drzavljanstvoCb.setDisable(true);

        // --- 2. POPUNJAVANJE PODATAKA IZ OBJEKTA ---
        if (imeTf != null) imeTf.setText(student.getIme());
        if (prezimeTf != null) prezimeTf.setText(student.getPrezime());
        if (jmbgTf != null) jmbgTf.setText(student.getJmbg());

        if (srednjeImeTf != null) {
            srednjeImeTf.setText(student.getSrednjeIme() != null ? student.getSrednjeIme() : "");
        }

        if (adresaTf != null) {
            adresaTf.setText(student.getAdresa() != null ? student.getAdresa() : "");
        }

        if (nacionalnostTf != null) {
            nacionalnostTf.setText(student.getNacionalnost() != null ? student.getNacionalnost() : "");
        }

        if (brojIndeksaTf != null) {
            brojIndeksaTf.setText(String.valueOf(student.getBrojIndeksa()));
        }

        if (godinaUpisaTf != null) {
            godinaUpisaTf.setText(String.valueOf(student.getGodinaUpisa()));
        }

        // Datum rođenja
        if (datumRodjenjaDp != null && student.getDatumRodjenja() != null) {
            datumRodjenjaDp.setValue(student.getDatumRodjenja());
        }

        // Pol (Radio Buttons)
        if (student.getPol() != null) {
            if ("M".equalsIgnoreCase(student.getPol()) || "MUSKI".equalsIgnoreCase(student.getPol())) {
                if (muski != null) muski.setSelected(true);
            } else if ("Ž".equalsIgnoreCase(student.getPol()) || "ZENSKI".equalsIgnoreCase(student.getPol())) {
                if (zenski != null) zenski.setSelected(true);
            }
        }

        // --- 3. REGISTRACIJA PREČICA (Ctrl + [) ---
        // Ovo osigurava da prečica za nazad radi čim se podaci učitaju
        Platform.runLater(() -> {
            if (korenskiVBox != null && korenskiVBox.getScene() != null) {
                korenskiVBox.getScene().getAccelerators().put(
                        new KeyCodeCombination(KeyCode.OPEN_BRACKET, KeyCombination.CONTROL_ANY),
                        () -> handleBack(null)
                );
            }
        });


        ucitajDetaljeStudenta(student.getId());
    }

    private void ucitajDetaljeStudenta(Long studentId) {
        try {
            List<PolozeniPredmetiResponse> polozeni = studentService.getPolozeniIspiti(studentId);
            List<NepolozeniPredmetDTO> nepolozeni = studentService.getNepolozeniIspiti(Math.toIntExact(studentId));
            List<UplataResponse> uplate = studentService.getUplate(studentId);

            polozeniTable.setItems(FXCollections.observableArrayList(polozeni));
            nepolozeniTable.setItems(FXCollections.observableArrayList(nepolozeni));
            uplateTable.setItems(FXCollections.observableArrayList(uplate));

            // --- PRORAČUN STATUSA (Sekcija 1 Specifikacije) ---
            int ukupnoEspb = 0;
            double sumaOcena = 0;
            int brojPolozenih = 0;

            for (PolozeniPredmetiResponse p : polozeni) {
                // Koristimo tvoje polje 'espb' iz DTO-a
                ukupnoEspb += (p.getEspb() != null) ? p.getEspb() : 0;

                if (p.getOcena() != null && p.getOcena() > 5) {
                    sumaOcena += p.getOcena();
                    brojPolozenih++;
                }
            }

            ukupnoEspbLabel.setText(String.valueOf(ukupnoEspb));
            prosekLabel.setText(brojPolozenih > 0 ? String.format("%.2f", sumaOcena / brojPolozenih) : "0.00");

        } catch (Exception e) {
            labelError.setText("Greška pri učitavanju detalja!");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBack(ActionEvent event) {
        // Pozivamo MainWindow da nas vrati nazad kroz istoriju
        if (MainWindowController.getInstance() != null) {
            MainWindowController.getInstance().goBack();
        }
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
    public void popuniFormuIzBaze(StudentPodaciResponse student) {
        if (student == null) return;

        // --- POPUNJAVANJE I ZAKLJUČAVANJE TEKSTUALNIH POLJA ---
        postaviTekstIZakljucaj(imeTf, student.getIme());
        postaviTekstIZakljucaj(prezimeTf, student.getPrezime());
        postaviTekstIZakljucaj(jmbgTf, student.getJmbg());
        postaviTekstIZakljucaj(brojIndeksaTf, String.valueOf(student.getBrojIndeksa()));
        postaviTekstIZakljucaj(godinaUpisaTf, String.valueOf(student.getGodinaUpisa()));

        // Polja koja su pravila grešku (sada su sigurna)
        postaviTekstIZakljucaj(srednjeImeTf, student.getSrednjeIme());
        postaviTekstIZakljucaj(adresaTf, student.getAdresa());
        postaviTekstIZakljucaj(nacionalnostTf, student.getNacionalnost());
        postaviTekstIZakljucaj(brojLicneKarteTf, student.getBrojLicneKarte());

        // --- DATUM ---
        if (datumRodjenjaDp != null) {
            datumRodjenjaDp.setValue(student.getDatumRodjenja());
            datumRodjenjaDp.setDisable(true); // Onemogućava izmjenu datuma
        }

        // --- POL (Radio Buttons) ---
        if (student.getPol() != null) {
            String polStr = String.valueOf(student.getPol()); // Jer je u modelu Character
            if (polStr.equalsIgnoreCase("M")) {
                if (muski != null) { muski.setSelected(true); muski.setDisable(true); }
                if (zenski != null) zenski.setDisable(true);
            } else {
                if (zenski != null) { zenski.setSelected(true); zenski.setDisable(true); }
                if (muski != null) muski.setDisable(true);
            }
        }
    }

    // Pomoćna metoda da ne pišeš isti kod 20 puta
    private void postaviTekstIZakljucaj(TextField tf, String vrednost) {
        if (tf != null) {
            tf.setText(vrednost != null ? vrednost : "");
            tf.setEditable(false); // Onemogućava kucanje
            tf.setStyle("-fx-background-color: #eeeeee;"); // Opciono: daje sivu boju kao znak da je zaključano
        }
    }
    private void postaviPoljaNaReadOnly() {
        imeTf.setEditable(false);
        prezimeTf.setEditable(false);
        srednjeImeTf.setEditable(false);
        jmbgTf.setEditable(false);
        nacionalnostTf.setEditable(false);
        brojLicneKarteTf.setEditable(false);
        datumRodjenjaDp.setDisable(true); // Disable se koristi za DatePicker da bi se sprečio pop-up kalendar
        muski.setDisable(true);
        zenski.setDisable(true);
        mestoRodjenjaCb.setDisable(true);
        drzavaRodjenjaCb.setDisable(true);
        drzavljanstvoCb.setDisable(true);
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
        // Prečica Ctrl + [
        if (event.isControlDown() && event.getCode() == KeyCode.OPEN_BRACKET) {
            handleBack(null);
            event.consume();
        }
        // Prečica ESC (za svaki slučaj)
        else if (event.getCode() == KeyCode.ESCAPE) {
            handleBack(null);
            event.consume();
        }
    }
    public void loadStudentData(Long studentId) {
        try {
            // 1. Preuzimanje osnovnih podataka o studentu
            StudentPodaciResponse student = studentService.getStudentById(studentId);

            // 2. Provera da li je student pronađen pre daljeg rada
            if (student == null) {
                System.err.println("Student sa ID " + studentId + " nije pronađen!");
                return;
            }

            // Popunjavanje forme osnovnim podacima
            popuniFormuIzBaze(student);

            // 3. Preuzimanje podataka koji se vežu za interni ID (položeni i uplate)
            List<PolozeniPredmetiResponse> polozeni = studentService.getPolozeniIspiti(studentId);
            List<UplataResponse> uplate = studentService.getUplate(studentId);

            // 4. RAD SA POLOŽENIM ISPITIMA
            if (polozeniTable != null) {
                if (polozeni != null && !polozeni.isEmpty()) {
                    // Postavljamo listu i odmah računamo prosek i ESPB
                    polozeniTable.setItems(FXCollections.observableArrayList(polozeni));
                    obracunajAkademskiStatus(polozeni);
                } else {
                    polozeniTable.setItems(FXCollections.emptyObservableList());
                    if (ukupnoEspbLabel != null) ukupnoEspbLabel.setText("0");
                    if (prosekLabel != null) prosekLabel.setText("0.00");
                }
            }

            // 5. RAD SA NEPOLOŽENIM PREDMETIMA (Ispravljena logika)
            // Koristimo brojIndeksa jer server to traži na @GetMapping("/{brojIndeksa}/nepolozeni")
            if (nepolozeniTable != null) {
                if (student.getBrojIndeksa() > 0) {
                    List<NepolozeniPredmetDTO> nepolozeni = studentService.getNepolozeniIspiti(student.getBrojIndeksa());

                    if (nepolozeni != null && !nepolozeni.isEmpty()) {
                        // Postavljamo listu nepoloženih
                        nepolozeniTable.setItems(FXCollections.observableArrayList(nepolozeni));
                    } else {
                        nepolozeniTable.setItems(FXCollections.emptyObservableList());
                    }
                } else {
                    nepolozeniTable.setItems(FXCollections.emptyObservableList());
                }
            }

            // 6. RAD SA UPLATAMA
            if (uplateTable != null) {
                if (uplate != null && !uplate.isEmpty()) {
                    uplateTable.setItems(FXCollections.observableArrayList(uplate));
                } else {
                    uplateTable.setItems(FXCollections.emptyObservableList());
                }
            }

            // 7. Fokusiranje komponente radi prečica
            Platform.runLater(() -> {
                if (korenskiVBox != null) korenskiVBox.requestFocus();
            });

        } catch (Exception e) {
            System.err.println("GRESKA u loadStudentData: " + e.getMessage());
            e.printStackTrace();
            if (labelError != null) labelError.setText("Greška pri osvežavanju podataka.");
        }
    }

    private void obracunajAkademskiStatus(List<PolozeniPredmetiResponse> polozeni) {
        int ukupnoEspb = 0;
        double sumaOcena = 0;
        int brojPolozenih = 0;

        if (polozeni != null) {
            for (PolozeniPredmetiResponse p : polozeni) {
                // Provera ocene: bodovi i prosek se računaju samo ako je ispit položen
                if (p.getOcena() != null && p.getOcena() > 5) {
                    // Sabiramo ESPB poene predmeta [cite: 9, 92]
                    ukupnoEspb += (p.getEspb() != null) ? p.getEspb() : 0;
                    sumaOcena += p.getOcena();
                    brojPolozenih++;
                }
            }
        }

        // Ažuriranje labela na UI-ju
        if (ukupnoEspbLabel != null) {
            ukupnoEspbLabel.setText(String.valueOf(ukupnoEspb));
        }

        if (prosekLabel != null) {
            prosekLabel.setText(brojPolozenih > 0 ? String.format("%.2f", sumaOcena / brojPolozenih) : "0.00");
        }
    }

}