module sn.examen {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires itextpdf;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    opens sn.examen to javafx.graphics;
    opens sn.examen.controller to javafx.fxml;
    opens sn.examen.model to org.hibernate.orm.core, javafx.base;
    opens sn.examen.dao to org.hibernate.orm.core;

    exports sn.examen;
    exports sn.examen.controller;
    exports sn.examen.model;
    exports sn.examen.dao;
}