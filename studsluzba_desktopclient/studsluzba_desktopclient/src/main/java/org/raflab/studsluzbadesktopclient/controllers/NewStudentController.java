package org.raflab.studsluzbadesktopclient.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.raflab.studsluzbadesktopclient.dtos.SrednjaSkolaResponse;
import org.raflab.studsluzbadesktopclient.dtos.StudentPodaciRequest;
import org.raflab.studsluzbadesktopclient.dtos.StudentPodaciResponse;
import org.raflab.studsluzbadesktopclient.services.SifarniciService;
import org.raflab.studsluzbadesktopclient.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewStudentController {

    private final StudentService studentService;
    private final SifarniciService sifarniciService;

    // Polja forme
    @FXML private TextField imeTf, prezimeTf, srednjeImeTf, jmbgTf, brojLicneKarteTf;
    @FXML private DatePicker datumRodjenjaDp;
    @FXML private TextField mestoRodjenjaTf, drzavaRodjenjaTf, drzavljanstvoTf, nacionalnostTf;
    @FXML private TextField adresaTf, mestoPrebivalistaTf, mestoStanovanjaTf;
    @FXML private TextField emailFakultetTf, emailPrivatniTf, brojTelefonaTf;

    // Pol
    @FXML private RadioButton muski, zenski;
    @FXML private ToggleGroup polGroup;

    // Škola i Uspeh
    @FXML private ComboBox<SrednjaSkolaResponse> srednjaSkolaCb;
    @FXML private TextField uspehSrednjaSkolaTf, uspehPrijemniTf;

    // Indeks
    @FXML private TextField brojIndeksaTf, godinaUpisaTf, studProgramOznakaTf;

    @FXML private Label msgLabel;

    @Autowired
    public NewStudentController(StudentService studentService, SifarniciService sifarniciService) {
        this.studentService = studentService;
        this.sifarniciService = sifarniciService;
    }

    @FXML
    public void initialize() {
        try {
            if (srednjaSkolaCb != null) {
                srednjaSkolaCb.setItems(FXCollections.observableArrayList(sifarniciService.getSrednjeSkole()));
                // Ovo popravlja ispis "SrednjaSkolaResponse(id=1...)" u "Gimnazija"
                srednjaSkolaCb.setCellFactory(lv -> new ListCell<>() {
                    @Override protected void updateItem(SrednjaSkolaResponse item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(empty ? null : item.getNaziv());
                    }
                });
                srednjaSkolaCb.setButtonCell(new ListCell<>() {
                    @Override protected void updateItem(SrednjaSkolaResponse item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(empty ? null : item.getNaziv());
                    }
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    public void handleSave(ActionEvent event) {
        try {
            StudentPodaciRequest req = collectData();
            StudentPodaciResponse response = studentService.createStudent(req);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Uspeh");
            alert.setHeaderText(null);
            alert.setContentText("Student " + response.getIme() + " " + response.getPrezime() + " je uspešno kreiran.");
            alert.showAndWait();

            clearForm();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška");
            alert.setHeaderText("Neuspešno čuvanje");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private StudentPodaciRequest collectData() {
        StudentPodaciRequest req = new StudentPodaciRequest();

        // Validacija
        if (getText(imeTf).isEmpty() || getText(prezimeTf).isEmpty() || getText(jmbgTf).isEmpty()) {
            throw new RuntimeException("Popunite obavezna polja!");
        }

        // Lični podaci
        req.setIme(getText(imeTf));
        req.setPrezime(getText(prezimeTf));
        req.setSrednjeIme(getText(srednjeImeTf));
        req.setJmbg(getText(jmbgTf));
        req.setDatumRodjenja(datumRodjenjaDp.getValue());
        req.setMestoRodjenja(getText(mestoRodjenjaTf));
        req.setDrzavaRodjenja(getText(drzavaRodjenjaTf));
        req.setDrzavljanstvo(getText(drzavljanstvoTf));
        req.setNacionalnost(getText(nacionalnostTf));
        req.setBrojLicneKarte(getText(brojLicneKarteTf));

        // Pol
        if (muski != null && muski.isSelected()) req.setPol('M');
        else if (zenski != null && zenski.isSelected()) req.setPol('Z');
        else req.setPol('M'); // Default

        // Kontakt
        req.setAdresa(getText(adresaTf));
        req.setMestoPrebivalista(getText(mestoPrebivalistaTf));
        req.setMestoStanovanja(getText(mestoStanovanjaTf));
        req.setAdresaStanovanja(getText(mestoStanovanjaTf)); // Ako imaš i ovo polje
        req.setEmailFakultet(getText(emailFakultetTf));
        req.setEmailPrivatni(getText(emailPrivatniTf));
        req.setBrojTelefonaMobilni(getText(brojTelefonaTf));

        // Škola
        if (srednjaSkolaCb != null && srednjaSkolaCb.getValue() != null) {
            req.setSrednjaSkolaId(srednjaSkolaCb.getValue().getId());
        } else {
            // Obavezno setuj neki ID ili baci grešku ako je škola obavezna
            req.setSrednjaSkolaId(1L);
        }

        // Uspeh (hendlovanje praznih polja)
        try {
            String uspehSkola = getText(uspehSrednjaSkolaTf);
            if(!uspehSkola.isEmpty()) req.setUspehSrednjaSkola(Double.parseDouble(uspehSkola));

            String uspehPrijemni = getText(uspehPrijemniTf);
            if(!uspehPrijemni.isEmpty()) req.setUspehPrijemni(Double.parseDouble(uspehPrijemni));

            req.setBrojIndeksa(Integer.parseInt(getText(brojIndeksaTf)));
            req.setGodinaUpisa(Integer.parseInt(getText(godinaUpisaTf)));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Brojevi (indeks, godina, uspeh) moraju biti validni brojevi!");
        }

        req.setStudProgramOznaka(getText(studProgramOznakaTf));

        return req;
    }

    private String getText(TextField tf) {
        return (tf != null && tf.getText() != null) ? tf.getText().trim() : "";
    }

    private void clearForm() {
        // Tekstualna polja
        if(imeTf != null) imeTf.clear();
        if(prezimeTf != null) prezimeTf.clear();
        if(srednjeImeTf != null) srednjeImeTf.clear();
        if(jmbgTf != null) jmbgTf.clear();
        if(datumRodjenjaDp != null) datumRodjenjaDp.setValue(null);

        if(mestoRodjenjaTf != null) mestoRodjenjaTf.clear();
        if(drzavaRodjenjaTf != null) drzavaRodjenjaTf.clear();
        if(drzavljanstvoTf != null) drzavljanstvoTf.clear();
        if(nacionalnostTf != null) nacionalnostTf.clear();
        if(brojLicneKarteTf != null) brojLicneKarteTf.clear();

        if(adresaTf != null) adresaTf.clear();
        if(mestoPrebivalistaTf != null) mestoPrebivalistaTf.clear();
        if(mestoStanovanjaTf != null) mestoStanovanjaTf.clear();
        if(emailFakultetTf != null) emailFakultetTf.clear();
        if(emailPrivatniTf != null) emailPrivatniTf.clear();
        if(brojTelefonaTf != null) brojTelefonaTf.clear();

        // Škola i uspeh
        if(srednjaSkolaCb != null) srednjaSkolaCb.getSelectionModel().clearSelection();
        if(uspehSrednjaSkolaTf != null) uspehSrednjaSkolaTf.clear();
        if(uspehPrijemniTf != null) uspehPrijemniTf.clear();

        // Indeks
        if(brojIndeksaTf != null) brojIndeksaTf.clear();
        if(godinaUpisaTf != null) godinaUpisaTf.clear();
        if(studProgramOznakaTf != null) studProgramOznakaTf.clear();

        // Radio buttoni za pol
        if(muski != null) muski.setSelected(false);
        if(zenski != null) zenski.setSelected(false);
    }
}