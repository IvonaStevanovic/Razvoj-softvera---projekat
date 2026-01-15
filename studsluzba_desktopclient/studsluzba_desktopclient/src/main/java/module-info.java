module studsluzba.desktopclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.beans;
    requires spring.core;
    requires spring.web;
    requires spring.webflux;
    requires spring.data.commons;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;

    requires static lombok;
    requires reactor.core;
    requires jakarta.validation; // ili java.validation ako koristiš stariju verziju
    requires java.sql;
    requires jasperreports;
    requires org.jetbrains.annotations;
    requires org.reactivestreams;

    // Glavni paket aplikacije
    opens org.raflab.studsluzbadesktopclient to spring.core, spring.beans, spring.context;

    // Konfiguracija (WebClient i ostali Bean-ovi)
    opens org.raflab.studsluzbadesktopclient.config to spring.core, spring.beans, spring.context;

    // Servisi
    opens org.raflab.studsluzbadesktopclient.services to spring.core, spring.beans, spring.context;

    // Kontroleri (Moraju biti otvoreni i za FXML i za Spring)
    opens org.raflab.studsluzbadesktopclient.controllers to javafx.fxml, spring.core, spring.beans, spring.context;



    // Coder paket (Gde ti je pucao CoderFactory)
    opens org.raflab.studsluzbadesktopclient.coder to spring.core, spring.beans, spring.context;
    opens org.raflab.studsluzbadesktopclient.dtos to javafx.base, com.fasterxml.jackson.databind, spring.core;
    // Exportovanje glavnog paketa
    exports org.raflab.studsluzbadesktopclient;

}