package org.raflab.studsluzbadesktopclient.controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
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

    // FXML Elementi - Forma
    @FXML private TextField imeTf, prezimeTf, srednjeImeTf, jmbgTf, nacionalnostTf, brojLicneKarteTf, adresaTf, emailPrivatniTf, emailFakultetTf, brojTelefonaTf, godinaUpisaTf, brojIndeksaTf, godinaIndeksaTf, uspehSrednjaSkolaTf, uspehPrijemniTf;
    @FXML private RadioButton muski, zenski;
    @FXML private DatePicker datumRodjenjaDp, datumAktivacijeDp;
    @FXML private TextField mestoRodjenjaTf, drzavaRodjenjaTf, drzavljanstvoTf, mestoStanovanjaTf;
    @FXML private ComboBox<SrednjaSkolaResponse> srednjaSkolaCb;
    @FXML private Label labelError, ukupnoEspbLabel, prosekLabel;
    @FXML private VBox korenskiVBox;
    @FXML private TabPane profilTabPane;

    @FXML private Label ukupnoUplateLabel;

    // Tabele - Ispiti i Uplate
    @FXML private TableView<PolozeniPredmetiResponse> polozeniTable;

    // Tabela sada koristi NepolozeniPredmetResponse
    @FXML private TableView<NepolozeniPredmetResponse> nepolozeniTable;
    @FXML private TableView<UplataResponse> uplateTable;

    // Kolone - Ispiti
    @FXML private TableColumn<PolozeniPredmetiResponse, String> predmetPolozioCol, datumPolaganjaCol;
    @FXML private TableColumn<PolozeniPredmetiResponse, Integer> ocenaCol, espbPolozioCol;

    // Kolone sada koriste NepolozeniPredmetResponse
    @FXML private TableColumn<NepolozeniPredmetResponse, String> predmetNepolozenCol;
    @FXML private TableColumn<NepolozeniPredmetResponse, Integer> espbNepolozenCol;

    // Kolone - Finansije
    @FXML private TableColumn<UplataResponse, String> datumUplateCol, svrhaCol;
    @FXML private TableColumn<UplataResponse, Double> iznosCol;

    // Tabele za Tok studija (Upis i Obnova)
    @FXML private TableView<UpisGodineResponse> upisGodineTable;
    @FXML private TableColumn<UpisGodineResponse, Integer> colUpisGodina;
    @FXML private TableColumn<UpisGodineResponse, String> colUpisSkolska;
    @FXML private TableColumn<UpisGodineResponse, String> colUpisDatum;
    @FXML private TableColumn<UpisGodineResponse, String> colUpisNapomena;

    @FXML private TableView<ObnovaGodineResponse> obnovaGodineTable;
    @FXML private TableColumn<ObnovaGodineResponse, Integer> colObnovaGodina;
    @FXML private TableColumn<ObnovaGodineResponse, String> colObnovaDatum;
    @FXML private TableColumn<ObnovaGodineResponse, String> colObnovaPredmeti;
    @FXML private TableColumn<ObnovaGodineResponse, String> colObnovaNapomena;

    private Long currentStudentId;
    private Long currentIndeksId;

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
        loadStudentData(studentId, null);
    }

    public void loadStudentData(Long studentId, Long selectedIndeksId) {
        this.currentStudentId = studentId;
        try {
            StudentPodaciResponse student = studentService.getStudentById(studentId);
            if (student == null) return;

            this.currentIndeksId = (selectedIndeksId != null) ? selectedIndeksId : student.getStudentIndeksId();

            popuniFormuIzBaze(student);

            // 1. Ispiti
            if (this.currentIndeksId != null) {
                List<PolozeniPredmetiResponse> polozeni = studentService.getPolozeniIspiti(this.currentIndeksId);
                if (polozeniTable != null) {
                    polozeniTable.setItems(FXCollections.observableArrayList(polozeni != null ? polozeni : FXCollections.emptyObservableList()));
                    obracunajAkademskiStatus(polozeni);
                }
            } else {
                if (polozeniTable != null) polozeniTable.setItems(FXCollections.emptyObservableList());
            }

            if (student.getBrojIndeksa() > 0 && nepolozeniTable != null) {
                List<NepolozeniPredmetResponse> nepolozeni = studentService.getNepolozeniIspiti(student.getBrojIndeksa());
                nepolozeniTable.setItems(FXCollections.observableArrayList(nepolozeni != null ? nepolozeni : FXCollections.emptyObservableList()));
            }

            // 2. Uplate
            List<UplataResponse> uplate = studentService.getUplate(studentId);
            if (uplateTable != null) {
                uplateTable.setItems(FXCollections.observableArrayList(uplate != null ? uplate : FXCollections.emptyObservableList()));
                if (ukupnoUplateLabel != null) {
                    double ukupno = 0.0;
                    if (uplate != null) {
                        for (UplataResponse u : uplate) {
                            if (u.getIznosRsd() != null) ukupno += u.getIznosRsd();
                        }
                    }
                    ukupnoUplateLabel.setText(String.format("%,.2f RSD", ukupno));
                }
            }

            // 3. Tok studija
            if (this.currentIndeksId != null) {
                List<UpisGodineResponse> upisi = studentService.getUpisaneGodine(this.currentIndeksId);
                if (upisGodineTable != null) {
                    upisGodineTable.setItems(FXCollections.observableArrayList(upisi != null ? upisi : FXCollections.emptyObservableList()));
                }

                List<ObnovaGodineResponse> obnove = studentService.getObnovljeneGodine(this.currentIndeksId);
                if (obnovaGodineTable != null) {
                    obnovaGodineTable.setItems(FXCollections.observableArrayList(obnove != null ? obnove : FXCollections.emptyObservableList()));
                }
            } else {
                if (upisGodineTable != null) upisGodineTable.setItems(FXCollections.emptyObservableList());
                if (obnovaGodineTable != null) obnovaGodineTable.setItems(FXCollections.emptyObservableList());
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

    @FXML
    public void handleUpisGodine(ActionEvent event) {
        if (currentStudentId == null || currentIndeksId == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Podaci o studentu nisu učitani.");
            alert.showAndWait();
            return;
        }

        Dialog<UpisGodineRequest> dialog = new Dialog<>();
        dialog.setTitle("Upis godine");
        dialog.setHeaderText("Unesite podatke za upis naredne godine");

        ButtonType upisiButtonType = new ButtonType("Upiši", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(upisiButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField godinaStudijaTf = new TextField();
        godinaStudijaTf.setPromptText("npr. 2");

        int sledecaGodina = 1;
        if (upisGodineTable != null && !upisGodineTable.getItems().isEmpty()) {
            sledecaGodina = upisGodineTable.getItems().stream()
                    .mapToInt(UpisGodineResponse::getGodinaStudija)
                    .max().orElse(0) + 1;
        }
        godinaStudijaTf.setText(String.valueOf(sledecaGodina));

        DatePicker datumDp = new DatePicker(LocalDate.now());

        TextArea napomenaTa = new TextArea();
        napomenaTa.setPromptText("Napomena (opciono)");
        napomenaTa.setPrefRowCount(2);

        // --- IZBOR PREDMETA (CHECKBOX LISTA) ---
        VBox predmetiBox = new VBox(5);
        ScrollPane scrollPane = new ScrollPane(predmetiBox);
        scrollPane.setPrefHeight(150);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color:transparent;");

        List<CheckBox> checkBoxes = new java.util.ArrayList<>();

        if (nepolozeniTable != null && !nepolozeniTable.getItems().isEmpty()) {
            for (NepolozeniPredmetResponse np : nepolozeniTable.getItems()) {
                CheckBox cb = new CheckBox(np.getNazivPredmeta() + " (" + np.getEspb() + " ESPB)");
                cb.setUserData(np.getPredmetId());
                checkBoxes.add(cb);
                predmetiBox.getChildren().add(cb);
            }
        } else {
            predmetiBox.getChildren().add(new Label("Nema predmeta za prenos."));
        }

        grid.add(new Label("Godina studija:"), 0, 0);
        grid.add(godinaStudijaTf, 1, 0);
        grid.add(new Label("Datum upisa:"), 0, 1);
        grid.add(datumDp, 1, 1);
        grid.add(new Label("Napomena:"), 0, 2);
        grid.add(napomenaTa, 1, 2);
        grid.add(new Label("Prenos predmeta:"), 0, 3);
        grid.add(scrollPane, 1, 3);

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(godinaStudijaTf::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == upisiButtonType) {
                try {
                    UpisGodineRequest req = new UpisGodineRequest();
                    req.setGodinaStudija(Integer.parseInt(godinaStudijaTf.getText()));
                    req.setDatum(datumDp.getValue());
                    req.setNapomena(napomenaTa.getText());
                    req.setStudentIndeksId(currentIndeksId);

                    java.util.Set<Long> odabraniIds = new java.util.HashSet<>();
                    for (CheckBox cb : checkBoxes) {
                        if (cb.isSelected() && cb.getUserData() != null) {
                            odabraniIds.add((Long) cb.getUserData());
                        }
                    }
                    req.setPrenetiPredmetiIds(odabraniIds);

                    return req;
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(req -> {
            if (req != null) {
                try {
                    studentService.upisiGodinu(currentIndeksId, req);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Uspeh");
                    alert.setContentText("Uspešno ste upisali godinu!");
                    alert.showAndWait();
                    loadStudentData(currentStudentId, currentIndeksId);
                } catch (RuntimeException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Greška");
                    alert.setHeaderText("Greška pri upisu");
                    String msg = e.getMessage();
                    if(msg.contains("message\":\"")) {
                        int start = msg.indexOf("message\":\"") + 10;
                        int end = msg.indexOf("\"", start);
                        if (end > start) msg = msg.substring(start, end);
                    }
                    alert.setContentText(msg);
                    alert.showAndWait();
                }
            }
        });
    }

    // --- METODA ZA OBNOVU GODINE (SA BLOKADOM PREKORAČENJA ESPB) ---
    @FXML
    public void handleObnovaGodine(ActionEvent event) {
        if (currentStudentId == null || currentIndeksId == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Podaci o studentu nisu učitani.");
            alert.showAndWait();
            return;
        }

        // 1. Priprema podataka
        int trenutnaGodina = 1;
        if (upisGodineTable != null && !upisGodineTable.getItems().isEmpty()) {
            trenutnaGodina = upisGodineTable.getItems().stream()
                    .mapToInt(UpisGodineResponse::getGodinaStudija)
                    .max().orElse(1);
        }

        int zauzetiEspb = 0;
        if (nepolozeniTable != null) {
            for (NepolozeniPredmetResponse np : nepolozeniTable.getItems()) {
                zauzetiEspb += np.getEspb();
            }
        }
        final int fiksniZauzetiEspb = zauzetiEspb;

        Dialog<ObnovaGodineRequest> dialog = new Dialog<>();
        dialog.setTitle("Obnova godine");
        dialog.setHeaderText("Obnova " + trenutnaGodina + ". godine");

        ButtonType potvrdiDugme = new ButtonType("Obnovi", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(potvrdiDugme, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField godinaTf = new TextField(String.valueOf(trenutnaGodina));
        godinaTf.setEditable(false);

        DatePicker datumDp = new DatePicker(LocalDate.now());
        TextArea napomenaTa = new TextArea();
        napomenaTa.setPromptText("Napomena");
        napomenaTa.setPrefRowCount(2);

        Label lblEspbInfo = new Label("Preneti ESPB: " + fiksniZauzetiEspb + " / 60");
        lblEspbInfo.setStyle("-fx-font-weight: bold;");

        VBox predmetiBox = new VBox(5);
        ScrollPane scrollPane = new ScrollPane(predmetiBox);
        scrollPane.setPrefHeight(150);
        scrollPane.setFitToWidth(true);

        List<CheckBox> checkBoxes = new java.util.ArrayList<>();

        StudentPodaciResponse studentPodaci = studentService.getStudentById(currentStudentId);
        if (studentPodaci != null && studentPodaci.getStudijskiProgramId() != null) {
            List<PredmetResponse> sviPredmeti = studentService.getPredmetiByProgram(studentPodaci.getStudijskiProgramId());

            int narednaGodina = trenutnaGodina + 1;
            for (PredmetResponse p : sviPredmeti) {
                // Ako predmet ima polje godina, ovde filtriramo
                // if (p.getGodina() == narednaGodina) {
                CheckBox cb = new CheckBox(p.getNaziv() + " (" + p.getEspb() + " ESPB)");
                cb.setUserData(p);

                cb.selectedProperty().addListener((obs, oldVal, newVal) -> {
                    int trenutnoOdabrano = 0;
                    for(CheckBox box : checkBoxes) {
                        if(box.isSelected()) {
                            PredmetResponse pr = (PredmetResponse) box.getUserData();
                            trenutnoOdabrano += pr.getEspb();
                        }
                    }
                    int total = fiksniZauzetiEspb + trenutnoOdabrano;
                    lblEspbInfo.setText("Ukupno ESPB: " + total + " / 60");
                    if(total > 60) lblEspbInfo.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                    else lblEspbInfo.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");
                });

                checkBoxes.add(cb);
                predmetiBox.getChildren().add(cb);
                // }
            }
        } else {
            predmetiBox.getChildren().add(new Label("Nije moguće učitati predmete (fali ID programa)."));
        }

        grid.add(new Label("Godina koju obnavljate:"), 0, 0);
        grid.add(godinaTf, 1, 0);
        grid.add(new Label("Datum:"), 0, 1);
        grid.add(datumDp, 1, 1);
        grid.add(new Label("Napomena:"), 0, 2);
        grid.add(napomenaTa, 1, 2);
        grid.add(new Label("Dodaj predmete iz naredne godine:"), 0, 3);
        grid.add(scrollPane, 1, 3);
        grid.add(lblEspbInfo, 1, 4);

        dialog.getDialogPane().setContent(grid);

        // --- NOVO: BLOKADA DUGMETA AKO JE ESPB PREKORAČEN ---
        // Dodajemo filter na dugme da sprečimo zatvaranje dijaloga ako je ESPB > 60
        Button btnPotvrdi = (Button) dialog.getDialogPane().lookupButton(potvrdiDugme);
        btnPotvrdi.addEventFilter(ActionEvent.ACTION, ae -> {
            int odabranoEspb = 0;
            for(CheckBox cb : checkBoxes) {
                if(cb.isSelected()) {
                    PredmetResponse pr = (PredmetResponse) cb.getUserData();
                    odabranoEspb += pr.getEspb();
                }
            }
            int total = fiksniZauzetiEspb + odabranoEspb;

            if (total > 60) {
                ae.consume(); // PREKIDA PROCES (NE ZATVARA DIJALOG)
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Upozorenje");
                alert.setHeaderText("Prekoračeni ESPB bodovi");
                alert.setContentText("Ukupan zbir ESPB bodova ne sme biti veći od 60!\nTrenutno izabrano: " + total);
                alert.showAndWait();
            }
        });
        // ----------------------------------------------------

        dialog.setResultConverter(btn -> {
            if (btn == potvrdiDugme) {
                ObnovaGodineRequest req = new ObnovaGodineRequest();
                req.setGodinaStudija(Integer.parseInt(godinaTf.getText()));
                req.setDatum(datumDp.getValue());
                req.setNapomena(napomenaTa.getText());
                req.setStudentIndeksId(currentIndeksId);

                java.util.Set<Long> ids = new java.util.HashSet<>();
                for(CheckBox cb : checkBoxes) {
                    if(cb.isSelected()) {
                        PredmetResponse pr = (PredmetResponse) cb.getUserData();
                        ids.add(pr.getId());
                    }
                }
                req.setPredmetIds(ids);
                return req;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(req -> {
            try {
                studentService.obnovaGodine(currentIndeksId, req);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Uspeh");
                alert.setContentText("Godina uspešno obnovljena!");
                alert.showAndWait();
                loadStudentData(currentStudentId, currentIndeksId);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Greška");
                String msg = e.getMessage();
                if(msg.contains("message\":\"")) {
                    int start = msg.indexOf("message\":\"") + 10;
                    int end = msg.indexOf("\"", start);
                    if (end > start) msg = msg.substring(start, end);
                }
                alert.setContentText(msg);
                alert.showAndWait();
            }
        });
    }

    @FXML
    public void handleNovaUplata(ActionEvent event) {
        if (currentStudentId == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Podaci o studentu nisu učitani.");
            alert.showAndWait();
            return;
        }

        Dialog<UplataRequest> dialog = new Dialog<>();
        dialog.setTitle("Nova uplata");
        dialog.setHeaderText("Unesite podatke o uplati");

        ButtonType saveButtonType = new ButtonType("Sačuvaj", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

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

        dialog.showAndWait().ifPresent(request -> {
            if (request != null) {
                boolean success = studentService.dodajUplatu(request);
                if (success) {
                    loadStudentData(currentStudentId, currentIndeksId);
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

        // --- UPIS GODINE ---
        if (colUpisGodina != null) {
            colUpisGodina.setCellValueFactory(new PropertyValueFactory<>("godinaStudija"));
            colUpisSkolska.setCellValueFactory(new PropertyValueFactory<>("skolskaGodina"));
            colUpisNapomena.setCellValueFactory(new PropertyValueFactory<>("napomena"));
            colUpisDatum.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getDatumUpisa() != null ? cellData.getValue().getDatumUpisa().toString() : ""));
        }

        // --- OBNOVA GODINE ---
        if (colObnovaGodina != null) {
            colObnovaGodina.setCellValueFactory(new PropertyValueFactory<>("godinaStudija"));
            colObnovaNapomena.setCellValueFactory(new PropertyValueFactory<>("napomena"));
            colObnovaDatum.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getDatum() != null ? cellData.getValue().getDatum().toString() : ""));

            colObnovaPredmeti.setCellValueFactory(cellData -> {
                if (cellData.getValue().getPredmetiNazivi() != null && !cellData.getValue().getPredmetiNazivi().isEmpty()) {
                    return new SimpleStringProperty(String.join(", ", cellData.getValue().getPredmetiNazivi()));
                }
                return new SimpleStringProperty("");
            });
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
            // Ako koristiš RadioButtone:
            if (muski != null && zenski != null) {
                if (student.getPol().toString().equalsIgnoreCase("M")) {
                    muski.setSelected(true);
                } else if (student.getPol().toString().equalsIgnoreCase("Z")) {
                    zenski.setSelected(true);
                }
            }}
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
    public void handleUverenje(ActionEvent event) {
        if (currentIndeksId == null) {
            // Ovde možeš dodati Alert da korisnik mora da selektuje studenta
            System.out.println("Nije izabran student!");
            return;
        }

        try {
            String url = "http://localhost:8090/api/student/izvestaj/uverenje-o-ispitima/" + currentIndeksId;

            // --- FIX ZA HEADLESS EXCEPTION ---
            // Umesto java.awt.Desktop, koristimo sistemsku komandu koja radi uvek
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win")) {
                // Windows
                new ProcessBuilder("cmd", "/c", "start", url).start();
            } else if (os.contains("mac")) {
                // Mac
                new ProcessBuilder("open", url).start();
            } else {
                // Linux
                new ProcessBuilder("xdg-open", url).start();
            }
            // ----------------------------------

        } catch (Exception e) {
            System.err.println("Neuspešno otvaranje pretraživača: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    public void handleUverenjeOStudiranju(ActionEvent event) {
        if (currentIndeksId == null) return;

        try {
            String url = "http://localhost:8090/api/student/izvestaj/uverenje-o-studiranju/" + currentIndeksId;

            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win")) {
                // --- PROMENA JE OVDE ---
                // Dodali smo "msedge" u komandu.
                // Ovo kaže: cmd -> start -> Microsoft Edge -> URL
                new ProcessBuilder("cmd", "/c", "start", "msedge", url).start();

            } else if (os.contains("mac")) {
                new ProcessBuilder("open", "-a", "Microsoft Edge", url).start();
            } else {
                new ProcessBuilder("xdg-open", url).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}