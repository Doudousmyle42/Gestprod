package sn.examen.controller;

import sn.examen.dao.CategorieDAO;
import sn.examen.dao.ProduitDAO;
import sn.examen.model.Produit;

// ── iText PDF (imports COMPLETS pour eviter l'ambiguite) ──
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

// iText Font avec alias pour eviter le conflit avec POI Font
import com.itextpdf.text.Font  ;   // ← on garde ce nom, mais on renomme POI Font

// ── Apache POI Excel (imports COMPLETS) ──
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

// ── JavaFX ──
import javafx.fxml.FXML;
import javafx.scene.control.Label;

// ── Java standard ──
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;