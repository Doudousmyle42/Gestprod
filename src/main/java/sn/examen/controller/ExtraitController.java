package sn.examen.controller;

import sn.examen.dao.CategorieDAO;
import sn.examen.dao.ProduitDAO;
import sn.examen.model.Produit;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

public class ExtraitController {

    @FXML private Label statusLabel;

    private ProduitDAO   prodDAO = new ProduitDAO();
    private CategorieDAO catDAO  = new CategorieDAO();

    // ─── EXPORT PDF ───────────────────────────────────────────────────────────
    @FXML
    public void extrairePDF() {
        String chemin = "liste_produits.pdf";
        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(chemin));
            document.open();

            // Titre
            // On utilise le nom complet pour eviter l'ambiguite avec POI Font
            com.itextpdf.text.Font titreFont = new com.itextpdf.text.Font(
                    com.itextpdf.text.Font.FontFamily.HELVETICA, 18,
                    com.itextpdf.text.Font.BOLD, BaseColor.DARK_GRAY);

            Paragraph titre = new Paragraph("Liste des Produits", titreFont);
            titre.setAlignment(Element.ALIGN_CENTER);
            titre.setSpacingAfter(20);
            document.add(titre);

            // Tableau 5 colonnes
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1f, 3f, 1.5f, 1.5f, 2f});

            // En-tete bleu
            BaseColor bleu = new BaseColor(21, 101, 192);
            com.itextpdf.text.Font headerFont = new com.itextpdf.text.Font(
                    com.itextpdf.text.Font.FontFamily.HELVETICA, 11,
                    com.itextpdf.text.Font.BOLD, BaseColor.WHITE);

            for (String col : new String[]{"ID","Libelle","Quantite","Prix","Categorie"}) {
                PdfPCell cell = new PdfPCell(new Phrase(col, headerFont));
                cell.setBackgroundColor(bleu);
                cell.setPadding(8);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            // Donnees
            com.itextpdf.text.Font dataFont = new com.itextpdf.text.Font(
                    com.itextpdf.text.Font.FontFamily.HELVETICA, 10);

            List<Produit> produits = prodDAO.findAll();
            boolean alt = false;
            for (Produit p : produits) {
                BaseColor bg = alt ? new BaseColor(227, 242, 253) : BaseColor.WHITE;
                String[] vals = {
                        String.valueOf(p.getId()),
                        p.getLibelle(),
                        String.valueOf(p.getQuantite()),
                        String.format("%.2f", p.getPrixUnitaire()),
                        p.getCategorie() != null ? p.getCategorie().getLibelle() : "-"
                };
                for (String v : vals) {
                    PdfPCell cell = new PdfPCell(new Phrase(v, dataFont));
                    cell.setBackgroundColor(bg);
                    cell.setPadding(6);
                    table.addCell(cell);
                }
                alt = !alt;
            }

            document.add(table);
            document.close();
            statusLabel.setText("✅ PDF genere : " + chemin);

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("❌ Erreur PDF : " + e.getMessage());
        }
    }

    // ─── EXPORT EXCEL ─────────────────────────────────────────────────────────
    @FXML
    public void extraireExcel() {
        String chemin = "stats_categories.xlsx";
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Stats");

            // Style en-tete
            CellStyle styleHeader = wb.createCellStyle();
            // On utilise le nom complet pour eviter l'ambiguite avec iText Font
            org.apache.poi.ss.usermodel.Font poiFont = wb.createFont();
            poiFont.setBold(true);
            poiFont.setColor(IndexedColors.WHITE.getIndex());
            styleHeader.setFont(poiFont);
            styleHeader.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
            styleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // En-tete
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Categorie");
            header.createCell(1).setCellValue("Nombre de produits");
            header.getCell(0).setCellStyle(styleHeader);
            header.getCell(1).setCellStyle(styleHeader);

            // Donnees
            Map<String, Long> stats = catDAO.countProduitsParCategorie();
            int rowNum = 1;
            for (Map.Entry<String, Long> entry : stats.entrySet()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(entry.getKey());
                row.createCell(1).setCellValue(entry.getValue());
            }

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);

            try (FileOutputStream fos = new FileOutputStream(chemin)) {
                wb.write(fos);
            }
            statusLabel.setText("✅ Excel genere : " + chemin);

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("❌ Erreur Excel : " + e.getMessage());
        }
    }
}