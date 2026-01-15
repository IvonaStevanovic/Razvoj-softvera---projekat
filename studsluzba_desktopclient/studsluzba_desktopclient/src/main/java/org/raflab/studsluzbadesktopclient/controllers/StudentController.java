package org.raflab.studsluzbadesktopclient.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.raflab.studsluzbadesktopclient.MainView;
import org.raflab.studsluzbadesktopclient.coder.CoderFactory;
import org.raflab.studsluzbadesktopclient.coder.CoderType;
import org.raflab.studsluzbadesktopclient.coder.SimpleCode;
import org.raflab.studsluzbadesktopclient.dtos.SrednjaSkolaDTO;
import org.raflab.studsluzbadesktopclient.dtos.StudentPodaciResponse;
import org.raflab.studsluzbadesktopclient.services.NavigationService;
import org.raflab.studsluzbadesktopclient.services.SifarniciService;
import org.raflab.studsluzbadesktopclient.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Component
public class StudentController {

    private final StudentService studentService;
    private final CoderFactory coderFactory;
    private final MainView mainView;
    private final SifarniciService sifarniciService;
    @FXML
    private TextField imeTf;
    @FXML
    private TextField prezimeTf;
    @FXML
    private TextField srednjeImeTf;
    @FXML
    private RadioButton muski;
    @FXML
    private RadioButton zenski;
    @FXML
    private TextField jmbgTf;
    @FXML
    private TableColumn<StudentPodaciResponse, String> jmbgColumn;
    @FXML
    private TableColumn<StudentPodaciResponse, Integer> brojIndeksaColumn;
    @FXML
    private DatePicker datumRodjenjaDp;
    @FXML
    private DatePicker datumAktivacijeDp;
    @FXML
    ComboBox<SimpleCode> mestoRodjenjaCb;
    @FXML
    private TextField emailPrivatniTf;
    @FXML
    private TextField emailFakultetTf;
    @FXML
    TextField brojTelefonaTf;
    @FXML
    TextField adresaTf;
    @FXML
    ComboBox<SimpleCode> mestoStanovanjaCb;
    @FXML
    ComboBox<SimpleCode> drzavaRodjenjaCb;
    @FXML
    ComboBox<SimpleCode> drzavljanstvoCb;
    @FXML
    TextField nacionalnostTf;
    @FXML
    TextField brojLicneKarteTf;
    @FXML
    TextField godinaUpisaTf;
    @FXML
    TextField brojIndeksaTf;
    @FXML
    TextField godinaIndeksaTf;
    @FXML
    TextField uspehSrednjaSkolaTf;
    @FXML
    TextField uspehPrijemniTf;

    @FXML
    ComboBox<SrednjaSkolaDTO> srednjaSkolaCb;

    @FXML Label labelError;

//    @FXML
//    ComboBox<StudProgram> studProgramCb;

//    @FXML
//    ComboBox<VisokoskolskaUstanova> visokoskolskaUstanovaCb;

    // Izmeni konstruktor da izgleda tačno ovako:
    public StudentController(StudentService studentService,
                             CoderFactory coderFactory,
                             MainView mainView,
                             SifarniciService sifarniciService,
                             NavigationService navigationService) { // Dodaj ovo
        this.studentService = studentService;
        this.coderFactory = coderFactory;
        this.mainView = mainView;
        this.sifarniciService = sifarniciService;
        this.navigationService = navigationService; // Dodaj ovo
    }

    // I dodaj final polje na vrh klase:
    private final NavigationService navigationService;

// Obriši @Autowired NavigationService navigationService; koji ti stoji na pola klase

    @FXML
    public void initialize(){
        drzavaRodjenjaCb.setItems(FXCollections.observableArrayList(coderFactory.getSimpleCoder(CoderType.DRZAVA).getCodes()));
        drzavaRodjenjaCb.setValue(new SimpleCode("Serbia"));

        drzavljanstvoCb.setItems(FXCollections.observableArrayList(coderFactory.getSimpleCoder(CoderType.DRZAVA).getCodes()));
        drzavljanstvoCb.setValue(new SimpleCode("Serbia"));

        mestoRodjenjaCb.setItems(FXCollections.observableArrayList(coderFactory.getSimpleCoder(CoderType.MESTO).getCodes()));
        mestoRodjenjaCb.setValue(new SimpleCode("Beograd"));

        mestoStanovanjaCb.setItems(FXCollections.observableArrayList(coderFactory.getSimpleCoder(CoderType.MESTO).getCodes()));
        mestoStanovanjaCb.setValue(new SimpleCode("Beograd"));
        try {
            List<SrednjaSkolaDTO> srednjeSkole = sifarniciService.getSrednjeSkole();
            srednjaSkolaCb.setItems(FXCollections.observableArrayList(srednjeSkole));
        }catch (Exception e){
            labelError.setText(e.getMessage());
        }
    }
    public void prikaziStudenta(StudentPodaciResponse student) {
        imeTf.setText(student.getIme());
        prezimeTf.setText(student.getPrezime());
        jmbgTf.setText(student.getJmbg());

    }
    public void handleOpenModalSrednjeSkole(ActionEvent ae) {
        mainView.openModal("addSrednjaSkola");
    }

    @FXML
    void handleBack(ActionEvent event) {
        navigationService.goBack(); // OVO te zapravo vraća fizički na ekran pretrage
    }
    public void updateSrednjeSkole() {
        try{
            List<SrednjaSkolaDTO> srednjeSkole = sifarniciService.getSrednjeSkole();
            srednjaSkolaCb.setItems(FXCollections.observableArrayList(srednjeSkole));
        }catch (Exception e){
            labelError.setText(e.getMessage());
        }
    }

    public void handleOpenModalVisokoskolskeUstanove(ActionEvent ae) {
        //mainViewManager.openModal("addVisaUstanovaForStudent");
    }

    public void handleSaveStudent(ActionEvent event) {
        StudentPodaciResponse studentDTO = new StudentPodaciResponse();

        studentDTO.setIme(imeTf.getText());
        studentDTO.setPrezime(prezimeTf.getText());
        studentDTO.setSrednjeIme(srednjeImeTf.getText());
        studentDTO.setPol(muski.isSelected() ? "M" : "Z");
        studentDTO.setAdresa(adresaTf.getText());
        studentDTO.setJmbg(jmbgTf.getText());
        studentDTO.setDatumRodjenja(LocalDate.from(datumRodjenjaDp.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        studentDTO.setMestoRodjenja(mestoRodjenjaCb.getValue().getCode());
        studentDTO.setEmailPrivatni(emailPrivatniTf.getText());
        studentDTO.setEmailFakultet(emailFakultetTf.getText());
        studentDTO.setBrojTelefonaMobilni(brojTelefonaTf.getText());
        studentDTO.setMestoStanovanja(mestoStanovanjaCb.getValue().getCode());

        studentDTO.setDrzavaRodjenja(drzavaRodjenjaCb.getValue().getCode());
        studentDTO.setDrzavljanstvo(drzavljanstvoCb.getValue().getCode());
        studentDTO.setNacionalnost(nacionalnostTf.getText());

        studentService.saveStudent(studentDTO);
        resetForm();
    }




    private void resetForm() {
        imeTf.clear();
        prezimeTf.clear();
        srednjeImeTf.clear();

        muski.setSelected(false);
        zenski.setSelected(false);

        jmbgTf.clear();
        datumRodjenjaDp.setValue(null);
        datumAktivacijeDp.setValue(null);

        mestoRodjenjaCb.setValue(null);
        emailPrivatniTf.clear();
        emailFakultetTf.clear();
        brojTelefonaTf.clear();
        adresaTf.clear();

        mestoStanovanjaCb.setValue(null);
        drzavaRodjenjaCb.setValue(null);
        drzavljanstvoCb.setValue(null);

        nacionalnostTf.clear();
        brojLicneKarteTf.clear();
        godinaUpisaTf.clear();
        brojIndeksaTf.clear();
        godinaIndeksaTf.clear();
        uspehSrednjaSkolaTf.clear();
        uspehPrijemniTf.clear();
    }
}
