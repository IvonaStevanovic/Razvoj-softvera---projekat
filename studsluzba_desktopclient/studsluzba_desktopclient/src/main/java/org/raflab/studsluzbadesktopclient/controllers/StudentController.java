package org.raflab.studsluzbadesktopclient.controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane; // Dodato
import javafx.scene.layout.VBox;
import org.raflab.studsluzbadesktopclient.coder.CoderFactory;
import org.raflab.studsluzbadesktopclient.dtos.*;
import org.raflab.studsluzbadesktopclient.services.NavigationService;
import org.raflab.studsluzbadesktopclient.services.SifarniciService;
import org.raflab.studsluzbadesktopclient.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class StudentController {

    private final StudentService studentService;
    private final CoderFactory coderFactory;
    private final SifarniciService sifarniciService;
    private final NavigationService navigationService;

    // FXML Elementi
    @FXML private TextField imeTf, prezimeTf, srednjeImeTf, jmbgTf, nacionalnostTf, brojLicneKarteTf, adresaTf, emailPrivatniTf, emailFakultetTf, brojTelefonaTf, godinaUpisaTf, brojIndeksaTf, godinaIndeksaTf, uspehSrednjaSkolaTf, uspehPrijemniTf;
    @FXML private RadioButton muski, zenski;
    @FXML private DatePicker datumRodjenjaDp, datumAktivacijeDp;
    @FXML private TextField mestoRodjenjaTf, drzavaRodjenjaTf, drzavljanstvoTf, mestoStanovanjaTf;
    @FXML private ComboBox<SrednjaSkolaResponse> srednjaSkolaCb;
    @FXML private Label labelError, ukupnoEspbLabel, prosekLabel;
    @FXML private VBox korenskiVBox;
    @FXML private TabPane profilTabPane;

    // NOVO: Labela za ukupan zbir
    @FXML private Label ukupnoUplateLabel;

    // Tabele
    @FXML private TableView<PolozeniPredmetiResponse> polozeniTable;
    @FXML private TableView<NepolozeniPredmetDTO> nepolozeniTable;
    @FXML private TableView<UplataResponse> uplateTable;

    // Kolone - Ispiti
    @FXML private TableColumn<PolozeniPredmetiResponse, String> predmetPolozioCol, datumPolaganjaCol;
    @FXML private TableColumn<PolozeniPredmetiResponse, Integer> ocenaCol, espbPolozioCol;
    @FXML private TableColumn<NepolozeniPredmetDTO, String> predmetNepolozenCol;
    @FXML private TableColumn<NepolozeniPredmetDTO, Integer> espbNepolozenCol;

    // Kolone - Finansije
    @FXML private TableColumn<UplataResponse, String> datumUplateCol, svrhaCol;
    @FXML private TableColumn<UplataResponse, Double> iznosCol;

    // Čuvamo ID trenutnog studenta za novu uplatu
    private Long currentStudentId;

    @Autowired
    public StudentController(StudentService studentService, CoderFactory coderFactory,
                             SifarniciService sifarniciService,
                             NavigationService navigationService) {
        this.studentService = studentService;
        this.coderFactory = coderFactory;
        this.sifarniciService = sifarniciService;
        this.navigationService = navigationService;
    }

    @FXML
    public void initialize() {
        setupTableColumns();
        updateSrednjeSkole();

        if (profilTabPane != null) {
            profilTabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
                navigationService.recordTabChange(profilTabPane, oldTab);
            });
        }
    }

    @FXML
    public void handleBack(ActionEvent event) {
        navigationService.goBack();
    }

    @FXML
    public void handleForward(ActionEvent event) {
        navigationService.goForward();
    }

    public void loadStudentData(Long studentId) {
        this.currentStudentId = studentId; // Bitno za dodavanje uplate
        try {
            StudentPodaciResponse student = studentService.getStudentById(studentId);
            if (student == null) return;

            popuniFormuIzBaze(student);

            List<PolozeniPredmetiResponse> polozeni = studentService.getPolozeniIspiti(studentId);
            List<UplataResponse> uplate = studentService.getUplate(studentId);

            if (polozeniTable != null) {
                polozeniTable.setItems(FXCollections.observableArrayList(polozeni != null ? polozeni : FXCollections.emptyObservableList()));
                obracunajAkademskiStatus(polozeni);
            }

            if (student.getBrojIndeksa() > 0 && nepolozeniTable != null) {
                List<NepolozeniPredmetDTO> nepolozeni = studentService.getNepolozeniIspiti(student.getBrojIndeksa());
                nepolozeniTable.setItems(FXCollections.observableArrayList(nepolozeni != null ? nepolozeni : FXCollections.emptyObservableList()));
            }

            if (uplateTable != null) {
                uplateTable.setItems(FXCollections.observableArrayList(uplate != null ? uplate : FXCollections.emptyObservableList()));

                // Računanje zbira
                if (ukupnoUplateLabel != null) {
                    double ukupno = 0.0;
                    if (uplate != null) {
                        for (UplataResponse u : uplate) {
                            if (u.getIznosRsd() != null) {
                                ukupno += u.getIznosRsd();
                            }
                        }
                    }
                    ukupnoUplateLabel.setText(String.format("%,.2f RSD", ukupno));
                }
            }

            Platform.runLater(() -> {
                if (korenskiVBox != null) {
                    korenskiVBox.setFocusTraversable(true);
                    korenskiVBox.requestFocus();
                }
            });

        } catch (Exception e) {
            if (labelError != null) labelError.setText("Greška pri osvežavanju podataka.");
            e.printStackTrace();
        }
    }

    // --- NOVA METODA ZA DODAVANJE UPLATE ---
    @FXML
    public void handleNovaUplata(ActionEvent event) {
        if (currentStudentId == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Podaci o studentu nisu učitani.");
            alert.showAndWait();
            return;
        }

        // 1. Kreiranje Dijaloga
        Dialog<UplataRequest> dialog = new Dialog<>();
        dialog.setTitle("Nova uplata");
        dialog.setHeaderText("Unesite podatke o uplati");

        // Dugmad
        ButtonType saveButtonType = new ButtonType("Sačuvaj", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // 2. Polja za unos
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField iznosTf = new TextField();
        iznosTf.setPromptText("npr. 5000");

        TextArea svrhaTa = new TextArea();
        svrhaTa.setPromptText("npr. Školarina");
        svrhaTa.setPrefRowCount(3);

        DatePicker datumDp = new DatePicker(LocalDate.now());

        grid.add(new Label("Iznos (RSD):"), 0, 0);
        grid.add(iznosTf, 1, 0);
        grid.add(new Label("Svrha:"), 0, 1);
        grid.add(svrhaTa, 1, 1);
        grid.add(new Label("Datum:"), 0, 2);
        grid.add(datumDp, 1, 2);

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(iznosTf::requestFocus);

        // 3. Konverzija rezultata
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    UplataRequest req = new UplataRequest();
                    req.setStudentId(currentStudentId);
                    req.setIznosRsd(Double.parseDouble(iznosTf.getText()));
                    req.setNapomena(svrhaTa.getText());
                    req.setDatumUplate(datumDp.getValue());
                    req.setIznosEur(0.0);
                    return req;
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });

        // 4. Obrada rezultata
        dialog.showAndWait().ifPresent(request -> {
            if (request != null) {
                boolean success = studentService.dodajUplatu(request);
                if (success) {
                    loadStudentData(currentStudentId); // Osveži tabelu i zbir
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Uspeh");
                    alert.setContentText("Uplata je uspešno sačuvana!");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Greška");
                    alert.setContentText("Došlo je do greške prilikom čuvanja uplate.");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Neispravan unos iznosa!");
                alert.showAndWait();
            }
        });
    }

    private void obracunajAkademskiStatus(List<PolozeniPredmetiResponse> polozeni) {
        int ukupnoEspb = 0;
        double sumaOcena = 0;
        int brojPolozenih = 0;

        if (polozeni != null) {
            for (PolozeniPredmetiResponse p : polozeni) {
                if (p.getOcena() != null && p.getOcena() > 5) {
                    ukupnoEspb += (p.getEspb() != null) ? p.getEspb() : 0;
                    sumaOcena += p.getOcena();
                    brojPolozenih++;
                }
            }
        }
        if (ukupnoEspbLabel != null) ukupnoEspbLabel.setText(String.valueOf(ukupnoEspb));
        if (prosekLabel != null) prosekLabel.setText(brojPolozenih > 0 ? String.format("%.2f", sumaOcena / brojPolozenih) : "0.00");
    }

    private void setupTableColumns() {
        // --- ISPITI ---
        if (predmetPolozioCol != null) predmetPolozioCol.setCellValueFactory(new PropertyValueFactory<>("nazivPredmeta"));
        if (ocenaCol != null) ocenaCol.setCellValueFactory(new PropertyValueFactory<>("ocena"));
        if (datumPolaganjaCol != null) datumPolaganjaCol.setCellValueFactory(new PropertyValueFactory<>("datumPolaganja"));
        if (espbPolozioCol != null) espbPolozioCol.setCellValueFactory(new PropertyValueFactory<>("espb"));

        if (predmetNepolozenCol != null) predmetNepolozenCol.setCellValueFactory(new PropertyValueFactory<>("nazivPredmeta"));
        if (espbNepolozenCol != null) espbNepolozenCol.setCellValueFactory(new PropertyValueFactory<>("espb"));

        // --- FINANSIJE ---
        if (datumUplateCol != null) {
            datumUplateCol.setCellValueFactory(cellData -> {
                LocalDate datum = cellData.getValue().getDatumUplate();
                if (datum != null) {
                    return new SimpleStringProperty(datum.format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")));
                }
                return new SimpleStringProperty("");
            });
        }

        if (iznosCol != null) {
            iznosCol.setCellValueFactory(new PropertyValueFactory<>("iznosRsd"));
            iznosCol.setCellFactory(column -> new TableCell<UplataResponse, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(String.format("%,.2f RSD", item));
                    }
                }
            });
        }

        if (svrhaCol != null) {
            svrhaCol.setCellValueFactory(new PropertyValueFactory<>("svrhaUplate"));
        }
    }

    public void popuniFormuIzBaze(StudentPodaciResponse student) {
        postaviTekstIZakljucaj(imeTf, student.getIme());
        postaviTekstIZakljucaj(prezimeTf, student.getPrezime());
        postaviTekstIZakljucaj(srednjeImeTf, student.getSrednjeIme());
        postaviTekstIZakljucaj(jmbgTf, student.getJmbg());
        postaviTekstIZakljucaj(brojIndeksaTf, String.valueOf(student.getBrojIndeksa()));
        postaviTekstIZakljucaj(godinaUpisaTf, String.valueOf(student.getGodinaUpisa()));
        postaviTekstIZakljucaj(adresaTf, student.getAdresa());
        postaviTekstIZakljucaj(nacionalnostTf, student.getNacionalnost());
        postaviTekstIZakljucaj(brojLicneKarteTf, student.getBrojLicneKarte());
        postaviTekstIZakljucaj(mestoRodjenjaTf, student.getMestoRodjenja());
        postaviTekstIZakljucaj(drzavaRodjenjaTf, student.getDrzavaRodjenja());
        postaviTekstIZakljucaj(drzavljanstvoTf, student.getDrzavljanstvo());

        if (datumRodjenjaDp != null) {
            datumRodjenjaDp.setValue(student.getDatumRodjenja());
            datumRodjenjaDp.setDisable(true);
            datumRodjenjaDp.setStyle("-fx-opacity: 1; -fx-background-color: #eeeeee;");
        }

        if (student.getPol() != null) {
            boolean jeMusko = String.valueOf(student.getPol()).equalsIgnoreCase("M");
            if (muski != null) { muski.setSelected(jeMusko); muski.setDisable(true); }
            if (zenski != null) { zenski.setSelected(!jeMusko); zenski.setDisable(true); }
        }
    }

    private void postaviTekstIZakljucaj(TextField tf, String vrednost) {
        if (tf != null) {
            tf.setText(vrednost != null ? vrednost : "");
            tf.setEditable(false);
            tf.setStyle("-fx-background-color: #eeeeee;");
        }
    }

    public void updateSrednjeSkole() {
        try {
            if (srednjaSkolaCb != null) {
                srednjaSkolaCb.setItems(FXCollections.observableArrayList(sifarniciService.getSrednjeSkole()));
            }
        } catch (Exception e) {
            if (labelError != null) labelError.setText(e.getMessage());
        }
    }

    @FXML
    public void handleUverenje(ActionEvent event) {
        System.out.println("Uverenje generisanje pokrenuto...");
    }

    @FXML
    public void handleKeyPressed(KeyEvent event) {
        if (event.isControlDown()) {
            if (event.getCode().toString().equals("OPEN_BRACKET")) {
                navigationService.goBack();
            } else if (event.getCode().toString().equals("CLOSE_BRACKET")) {
                navigationService.goForward();
            }
        }
    }

    @FXML
    void handleUpisGodine(ActionEvent event) {
        System.out.println("Upis godine kliknut.");
    }

    @FXML
    void handleObnovaGodine(ActionEvent event) {
        System.out.println("Obnova godine kliknuta.");
    }
}