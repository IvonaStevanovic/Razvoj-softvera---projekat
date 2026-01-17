package org.raflab.studsluzbadesktopclient.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.raflab.studsluzbadesktopclient.dtos.NepolozeniPredmetDTO;
import org.raflab.studsluzbadesktopclient.dtos.PolozeniPredmetiResponse;
import org.raflab.studsluzbadesktopclient.dtos.StudentPodaciResponse;
import org.raflab.studsluzbadesktopclient.dtos.UplataResponse;
import org.raflab.studsluzbadesktopclient.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public class StudentProfileController {

    @Autowired
    private StudentService studentService;

    // --- Header ---
    @FXML private Label headerNameLabel;
    @FXML private Label headerIndexLabel;

    // --- Tab 1: Podaci ---
    @FXML private TextField txtIme;
    @FXML private TextField txtPrezime;
    @FXML private TextField txtIndeks;
    @FXML private TextField txtJmbg;
    @FXML private TextField txtEmail;
    @FXML private TextField txtSrednjaSkola;
    @FXML private TextField brojLicneKarteTf;
    @FXML private TextField mestoRodjenjaTf;
    @FXML private TextField drzavaRodjenjaTf;
    @FXML private TextField drzavljanstvoTf;
    @FXML private TextField nacionalnostTf;
    @FXML private TextField brojIndeksaTf;
    @FXML private TextField godinaUpisaTf;

    // --- Tab 2: Ispiti ---
    @FXML private TableView<PolozeniPredmetiResponse> tablePolozeni; // U FXML je polozeniTable, pazi na ID
    @FXML private TableColumn<PolozeniPredmetiResponse, String> colPredmet;
    @FXML private TableColumn<PolozeniPredmetiResponse, Integer> colOcena;
    @FXML private TableColumn<PolozeniPredmetiResponse, String> colDatum;
    @FXML private TableColumn<PolozeniPredmetiResponse, Integer> colEspb;

    @FXML private Label lblProsek;
    @FXML private Label lblEspb;

    @FXML private TableView<NepolozeniPredmetDTO> tableNepolozeni;
    @FXML private TableColumn<NepolozeniPredmetDTO, String> colNepolozeniPredmet;
    @FXML private TableColumn<NepolozeniPredmetDTO, String> colNastavnik;
    @FXML private TableColumn<NepolozeniPredmetDTO, String> colStatus;

    // --- Tab 3: Finansije (Uplate) ---
    @FXML private TableView<UplataResponse> tableUplate;
    @FXML private TableColumn<UplataResponse, Double> colUplataIznos;
    @FXML private TableColumn<UplataResponse, String> colUplataDatum;
    @FXML private TableColumn<UplataResponse, String> colUplataSvrha;

    // --- Tab 4: Tok studija ---
    @FXML private TableView<?> tableTokStudija;

    private Long currentStudentId;

    @FXML
    public void initialize() {
        // 1. Inicijalizacija kolona za Finansije
        if(colUplataIznos != null) colUplataIznos.setCellValueFactory(new PropertyValueFactory<>("iznosEur"));
        if(colUplataSvrha != null) colUplataSvrha.setCellValueFactory(new PropertyValueFactory<>("svrhaUplate"));

        if(colUplataDatum != null) {
            colUplataDatum.setCellValueFactory(cellData -> {
                if (cellData.getValue().getDatumUplate() != null) {
                    return new SimpleStringProperty(cellData.getValue().getDatumUplate().toString());
                } else {
                    return new SimpleStringProperty("");
                }
            });
        }

        // 2. Inicijalizacija kolona za Položene ispite (POPRAVLJENO)
        if(colPredmet != null) {
            colPredmet.setCellValueFactory(new PropertyValueFactory<>("nazivPredmeta"));
            colOcena.setCellValueFactory(new PropertyValueFactory<>("ocena"));
            colEspb.setCellValueFactory(new PropertyValueFactory<>("espb"));

            // --- POPRAVKA: getDatum() -> getDatumPolaganja() ---
            colDatum.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getDatumPolaganja() != null ? cellData.getValue().getDatumPolaganja().toString() : ""));
        }
    }

    public void loadStudentData(Long studentId) {
        this.currentStudentId = studentId;

        if (studentService == null) return;

        try {
            // A) Učitaj osnovne podatke
            StudentPodaciResponse student = studentService.getStudentById(studentId);
            populateBasicInfo(student);

            // B) Učitaj UPLATE
            List<UplataResponse> uplate = studentService.getUplate(studentId);
            if (tableUplate != null) {
                tableUplate.setItems(FXCollections.observableArrayList(uplate));
            }

            // C) Učitaj položene ispite
            List<PolozeniPredmetiResponse> polozeni = studentService.getPolozeniIspiti(studentId);
            if (tablePolozeni != null) {
                tablePolozeni.setItems(FXCollections.observableArrayList(polozeni));
                calculateStats(polozeni);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateBasicInfo(StudentPodaciResponse student) {
        if (student == null) return;
        if(headerNameLabel != null) headerNameLabel.setText(student.getIme() + " " + student.getPrezime());
        if(txtIme != null) txtIme.setText(student.getIme());
        if(txtPrezime != null) txtPrezime.setText(student.getPrezime());
        if(txtJmbg != null) txtJmbg.setText(student.getJmbg());
        if(txtEmail != null) txtEmail.setText(student.getEmailFakultet());
        if(txtSrednjaSkola != null) txtSrednjaSkola.setText(student.getSrednjaSkola());
        // Ostala polja
        if(brojLicneKarteTf != null) brojLicneKarteTf.setText(student.getBrojLicneKarte());
        if(mestoRodjenjaTf != null) mestoRodjenjaTf.setText(student.getMestoRodjenja());
        if(drzavaRodjenjaTf != null) drzavaRodjenjaTf.setText(student.getDrzavaRodjenja());
        if(drzavljanstvoTf != null) drzavljanstvoTf.setText(student.getDrzavljanstvo());
        if(nacionalnostTf != null) nacionalnostTf.setText(student.getNacionalnost());
    }

    private void calculateStats(List<PolozeniPredmetiResponse> polozeni) {
        if (polozeni == null || polozeni.isEmpty()) {
            if(lblProsek != null) lblProsek.setText("0.00");
            if(lblEspb != null) lblEspb.setText("0");
            return;
        }
        double sum = 0;
        int espb = 0;
        int count = 0;
        for (PolozeniPredmetiResponse p : polozeni) {
            if (p.getOcena() != null && p.getOcena() > 5) {
                sum += p.getOcena();
                if(p.getEspb() != null) espb += p.getEspb();
                count++;
            }
        }
        double avg = count > 0 ? sum / count : 0.0;
        if(lblProsek != null) lblProsek.setText(String.format("%.2f", avg));
        if(lblEspb != null) lblEspb.setText(String.valueOf(espb));
    }

    @FXML public void handleNovaUplata() {}
    @FXML public void handleUpisGodine() {}
    @FXML public void handleObnovaGodine() {}
}