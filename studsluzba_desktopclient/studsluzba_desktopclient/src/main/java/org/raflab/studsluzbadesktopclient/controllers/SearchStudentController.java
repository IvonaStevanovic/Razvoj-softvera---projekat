package org.raflab.studsluzbadesktopclient.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.raflab.studsluzbadesktopclient.MainView;
import org.raflab.studsluzbadesktopclient.dtos.SrednjaSkolaDTO;
import org.raflab.studsluzbadesktopclient.dtos.SrednjaSkolaResponse;
import org.raflab.studsluzbadesktopclient.dtos.StudentPodaciResponse;
import org.raflab.studsluzbadesktopclient.services.NavigationService;
import org.raflab.studsluzbadesktopclient.services.SifarniciService;
import org.raflab.studsluzbadesktopclient.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class SearchStudentController {

    @FXML private TextField filterIndeks;
    @FXML private TextField filterIme;
    @FXML private TextField filterPrezime;

    @FXML private TableView<StudentPodaciResponse> studentsTable;
    @FXML private TableColumn<StudentPodaciResponse, String> colIndeks;
    @FXML private TableColumn<StudentPodaciResponse, String> colIme;
    @FXML private TableColumn<StudentPodaciResponse, String> colPrezime;
    @FXML private TableColumn<StudentPodaciResponse, String> colEmail;
    @FXML private TableColumn<StudentPodaciResponse, String> colSrednjaSkola;
    @FXML private TableColumn<StudentPodaciResponse, String> colJmbg;
    @FXML private ComboBox<SrednjaSkolaResponse> comboSrednjaSkola;
    @Autowired
    private StudentService studentService; // Inject-ovan servis
    @Autowired
    private NavigationService navigationService; // Dodato za istoriju
    @Autowired
    private SifarniciService sifarniciService;
    @Autowired
    private MainView mainView;
    @FXML
    public void initialize() {
        setupTableColumns();
        setupRowFactory();
        loadSrednjeSkole();
    }

    private void setupTableColumns() {
        // 1. Kolona za INDEKS (Format: Broj / Godina)
        colIndeks.setCellValueFactory(cellData -> {
            StudentPodaciResponse s = cellData.getValue();
            // Provera na null je OBAVEZNA pre poređenja sa nulom (0)
            if (s.getBrojIndeksa() != 0 && s.getGodinaUpisa() != 0) {
                return new SimpleStringProperty(s.getBrojIndeksa() + "/" + s.getGodinaUpisa());
            }
            return new SimpleStringProperty("");
        });

        // 2. Standardne kolone (Property imena moraju biti IDENTIČNA poljima u StudentPodaciResponse)
        colIme.setCellValueFactory(new PropertyValueFactory<>("ime"));
        colPrezime.setCellValueFactory(new PropertyValueFactory<>("prezime"));

        // 3. Email (Sigurna provera bez obzira na vrstu pretrage)
        colEmail.setCellValueFactory(cellData -> {
            StudentPodaciResponse s = cellData.getValue();
            // Proveri da li se getter na tvom Response-u zove tačno getEmailFakultet()
            String email = (s != null) ? s.getEmailFakultet() : "";
            return new SimpleStringProperty(email != null ? email : "");
        });

        // 4. JMBG
        colJmbg.setCellValueFactory(cellData -> {
            StudentPodaciResponse s = cellData.getValue();
            String jmbg = (s != null) ? s.getJmbg() : "";
            return new SimpleStringProperty(jmbg != null ? jmbg : "");
        });

        // 5. Srednja škola (Ovo je važno za tvoju novu pretragu)
        colSrednjaSkola.setCellValueFactory(new PropertyValueFactory<>("srednjaSkola"));
    }

    private void setupRowFactory() {
        studentsTable.setRowFactory(tv -> {
            TableRow<StudentPodaciResponse> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    StudentPodaciResponse rowData = row.getItem();
                    openProfile(rowData);
                }
            });
            return row;
        });
    }

    private void loadSrednjeSkole() {
        try {
            List<SrednjaSkolaResponse> škole = sifarniciService.getSrednjeSkole();
            comboSrednjaSkola.setItems(FXCollections.observableArrayList(škole));

            // Podesi da se u padajućem meniju vidi naziv
            comboSrednjaSkola.setCellFactory(lv -> new ListCell<SrednjaSkolaResponse>() {
                @Override
                protected void updateItem(SrednjaSkolaResponse item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? "" : item.getNaziv());
                }
            });
            comboSrednjaSkola.setButtonCell(comboSrednjaSkola.getCellFactory().call(null));

        } catch (Exception e) {
            System.err.println("Greška: " + e.getMessage());
        }
    }

    public void handleSearch() {
        String indeksUnos = filterIndeks.getText();
        String ime = filterIme.getText();
        String prezime = filterPrezime.getText();

        // Uzimamo selektovanu vrednost
        SrednjaSkolaResponse selektovana = comboSrednjaSkola.getValue();
        String skolaNaziv = (selektovana != null) ? selektovana.getNaziv() : null;

        if (studentService == null) {
            System.err.println("StudentService nije inject-ovan!");
            return;
        }

        List<StudentPodaciResponse> rezultati;

        // 1. PRIORITET: Pretraga po indeksu (ako je unet)
        if (indeksUnos != null && !indeksUnos.trim().isEmpty()) {
            System.out.println("Vrsim pretragu po indeksu: " + indeksUnos);
            rezultati = studentService.searchStudents(null, null, indeksUnos, null);

            // Fix za prikaz godine ako je format Broj/Godina
            if (indeksUnos.contains("/") && !rezultati.isEmpty()) {
                try {
                    int unesenaGodina = Integer.parseInt(indeksUnos.split("/")[1].trim());
                    rezultati.get(0).setGodinaUpisa(unesenaGodina);
                } catch (Exception e) { }
            }
        }
        // 2. NOVO: Pretraga po srednjoj školi (ako je izabrana, a indeks nije unet)
        else if (skolaNaziv != null) {
            System.out.println("Vrsim pretragu po srednjoj skoli: " + skolaNaziv);
            // Koristimo namenski endpoint sa servera
            rezultati = studentService.getStudentiPoSrednjojSkoli(skolaNaziv);
        }
        // 3. Standardna pretraga po imenu i prezimenu
        else {
            System.out.println("Vrsim standardnu pretragu: " + ime + " " + prezime);
            rezultati = studentService.searchStudents(ime, prezime, null, null);
        }

        studentsTable.setItems(FXCollections.observableArrayList(rezultati));
    }
    @FXML
    public void handleClear() {
        filterIndeks.clear();
        filterIme.clear();
        filterPrezime.clear();
        comboSrednjaSkola.getSelectionModel().clearSelection();
        if (comboSrednjaSkola != null) comboSrednjaSkola.getSelectionModel().clearSelection();
        studentsTable.setItems(FXCollections.emptyObservableList());
    }

    private void openProfile(StudentPodaciResponse student) {
        try {
            // 1. Učitaj vizuelni dio
            Parent profilView = mainView.loadPane("studentPodaciTabPane");

            // 2. Uzmi kontroler preko nove metode iz MainView
            StudentController controller = (StudentController) mainView.getController();

            // 3. Napuni ga podacima
            if (controller != null) {
                controller.loadStudentData(student.getId());
            }

            // 4. OVO SPASAVA ISTORIJU: navigateTo gura trenutnu pretragu u Stack
            navigationService.navigateTo(profilView);

        } catch (Exception e) {
            System.err.println("Greska pri otvaranju profila: " + e.getMessage());
            e.printStackTrace();
        }
    }
}