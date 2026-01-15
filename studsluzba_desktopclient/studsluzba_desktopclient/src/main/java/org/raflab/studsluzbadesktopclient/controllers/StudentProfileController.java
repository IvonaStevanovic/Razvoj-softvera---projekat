package org.raflab.studsluzbadesktopclient.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.raflab.studsluzbadesktopclient.dtos.NepolozeniPredmetDTO;
import org.raflab.studsluzbadesktopclient.dtos.PolozeniPredmetiResponse;
import org.raflab.studsluzbadesktopclient.dtos.StudentPodaciResponse;
import org.raflab.studsluzbadesktopclient.dtos.UplataResponse;
// TODO: Proveri pakete. Pretpostavljam da su ovo response klase koje koristiš umesto DTO.
// Ako se zovu drugačije (npr. StudentResponse), promeni ovde.
 // Ili odgovarajuci response

public class StudentProfileController {

    // --- Header ---
    @FXML private Label headerNameLabel;
    @FXML private Label headerIndexLabel;

    // --- Tab 1: Lični podaci ---
    @FXML private TextField txtIme;
    @FXML private TextField txtPrezime;
    @FXML private TextField txtIndeks; // Verovatno treba dodatan poziv za indeks ili je deo prosirenog response-a
    @FXML private TextField txtJmbg;
    @FXML private TextField txtEmail;
    @FXML private TextField txtSrednjaSkola;

    // --- Tab 2: Ispiti ---
    @FXML private TableView<PolozeniPredmetiResponse> tablePolozeni;
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

    // --- Tab 3: Uplate ---
    @FXML private TableView<UplataResponse> tableUplate;
    @FXML private TableColumn<UplataResponse, Double> colUplataIznos;
    @FXML private TableColumn<UplataResponse, String> colUplataDatum;
    @FXML private TableColumn<UplataResponse, String> colUplataSvrha;

    // --- Tab 4: Tok studija ---
    // Koristimo wildcard <?> dok ne ubaciš UpisGodineResponse klasu u klijent projekat
    @FXML private TableView<?> tableTokStudija;
    @FXML private TableColumn<?, Integer> colGodinaStudija;
    @FXML private TableColumn<?, String> colSkolskaGodina;
    @FXML private TableColumn<?, String> colTipUpisa;
    @FXML private TableColumn<?, String> colDatumUpisa;


    private Long currentStudentId;

    @FXML
    public void initialize() {
        // TODO: Inicijalizacija kolona tabela (setCellValueFactory)
        // Primer: colPredmet.setCellValueFactory(new PropertyValueFactory<>("nazivPredmeta"));
    }

    /**
     * Ovu metodu zovemo iz MainWindowController-a ili Search-a kada otvorimo profil.
     * Ona treba da pozove servise i popuni podatke.
     */
    public void loadStudentData(Long studentId) {
        this.currentStudentId = studentId;
        System.out.println("Učitavanje podataka za studenta ID: " + studentId);

        // TODO: Poziv servisa koji vraća StudentPodaciResponse
        // StudentPodaciResponse student = studentService.getStudentPodaci(studentId);
        // populateBasicInfo(student);

        // TODO: Poziv ka ostalim servisima za tabele (Uplate, Ispiti...)
    }

    private void populateBasicInfo(StudentPodaciResponse student) {
        if (student == null) return;

        headerNameLabel.setText(student.getIme() + " " + student.getPrezime());

        // Napomena: StudentPodaciResponse na serveru nema polje za indeks.
        // Verovatno ces morati da dovuces i StudentIndeksResponse ili da koristis neki 'Aggregate' response.
        // headerIndexLabel.setText(...);
        // txtIndeks.setText(...);

        txtIme.setText(student.getIme());
        txtPrezime.setText(student.getPrezime());
        txtJmbg.setText(student.getJmbg());

        // Error fix: StudentPodaciResponse izgleda nema getEmail metodu
        // txtEmail.setText(student.getEmail());

        txtSrednjaSkola.setText(student.getSrednjaSkola());
    }

    // --- Akcije ---

    @FXML
    public void handleNovaUplata() {
        System.out.println("Otvaranje dijaloga za novu uplatu za studenta: " + currentStudentId);
        // Otvoriti modalni prozor
    }

    @FXML
    public void handleUpisGodine() {
        System.out.println("Otvaranje dijaloga za upis godine...");
    }

    @FXML
    public void handleObnovaGodine() {
        System.out.println("Otvaranje dijaloga za obnovu godine...");
    }
}