package org.raflab.studsluzbadesktopclient.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.raflab.studsluzbadesktopclient.MainView;
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
    private StudentService studentService;
    @Autowired
    private NavigationService navigationService;
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
        colIndeks.setCellValueFactory(cellData -> {
            StudentPodaciResponse s = cellData.getValue();
            if (s.getBrojIndeksa() != 0 && s.getGodinaUpisa() != 0) {
                return new SimpleStringProperty(s.getBrojIndeksa() + "/" + s.getGodinaUpisa());
            }
            return new SimpleStringProperty("");
        });

        colIme.setCellValueFactory(new PropertyValueFactory<>("ime"));
        colPrezime.setCellValueFactory(new PropertyValueFactory<>("prezime"));

        colEmail.setCellValueFactory(cellData -> {
            StudentPodaciResponse s = cellData.getValue();
            String email = (s != null) ? s.getEmailFakultet() : "";
            return new SimpleStringProperty(email != null ? email : "");
        });

        colJmbg.setCellValueFactory(cellData -> {
            StudentPodaciResponse s = cellData.getValue();
            String jmbg = (s != null) ? s.getJmbg() : "";
            return new SimpleStringProperty(jmbg != null ? jmbg : "");
        });

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

        SrednjaSkolaResponse selektovana = comboSrednjaSkola.getValue();
        String skolaNaziv = (selektovana != null) ? selektovana.getNaziv() : null;

        if (studentService == null) {
            System.err.println("StudentService nije inject-ovan!");
            return;
        }

        List<StudentPodaciResponse> rezultati;

        if (indeksUnos != null && !indeksUnos.trim().isEmpty()) {
            System.out.println("Vrsim pretragu po indeksu: " + indeksUnos);
            rezultati = studentService.searchStudents(null, null, indeksUnos, null);

            if (indeksUnos.contains("/") && !rezultati.isEmpty()) {
                try {
                    int unesenaGodina = Integer.parseInt(indeksUnos.split("/")[1].trim());
                    rezultati.get(0).setGodinaUpisa(unesenaGodina);
                } catch (Exception e) { }
            }
        }
        else if (skolaNaziv != null) {
            System.out.println("Vrsim pretragu po srednjoj skoli: " + skolaNaziv);
            rezultati = studentService.getStudentiPoSrednjojSkoli(skolaNaziv);
        }
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
                // --- KLJUČNA IZMENA: Šaljemo i ID indeksa koji smo kliknuli ---
                // Ovo rešava problem učitavanja pogrešnog (aktivnog) indeksa
                controller.loadStudentData(student.getId(), student.getStudentIndeksId());
            }

            // 4. Navigacija
            navigationService.navigateTo(profilView);

        } catch (Exception e) {
            System.err.println("Greska pri otvaranju profila: " + e.getMessage());
            e.printStackTrace();
        }
    }
}