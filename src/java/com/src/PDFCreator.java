package com.src;

import Common.src.com.Config.AppConfig;
import Common.src.com.Config.Configurator;
import com.bean.BillItem;
import com.bean.Itemisation;
import com.bean.RatedCdr;
import java.io.*;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.BadElementException;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class PDFCreator {

    AppConfig config = Configurator.getAppConfig();
    public final String RESOURCE = config.getPdfGetResource();
    private static int noBorder = Rectangle.NO_BORDER;
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font smallFont = new Font(Font.FontFamily.TIMES_ROMAN, 10,
            Font.NORMAL);
    private static Font redFont = new Font(Font.FontFamily.COURIER, 28,
            Font.BOLD, BaseColor.RED);
    private static Font blueFont = new Font(Font.FontFamily.COURIER, 28, Font.BOLD, BaseColor.BLUE);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);
    private static Font whiteFont = new Font(Font.FontFamily.TIMES_ROMAN, 11,
            Font.BOLD, BaseColor.WHITE);

    public PDFCreator(ArrayList<Itemisation> itemisations) throws Exception {

        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        //PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\Neha\\Documents\\NetBeansProjects\\JavaiText\\src\\javaitext\\AddBigTable.pdf"));
        for (Itemisation itemisation : itemisations) {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(config.getPdfCreateDirectory() + "/" + itemisation.getAccountNumber() + ".pdf"));
            writer.setBoxSize("art", new Rectangle(100, 100, 559, 788));
            HeaderFooter event = new HeaderFooter();
            writer.setPageEvent(event);
            document.open();
            addImage(document);
            document.setMarginMirroring(true);
            PdfPTable table = new PdfPTable(7); // table with 7 columns
            table.setWidthPercentage(100f);
            table.setHorizontalAlignment(100);
            table.setSpacingBefore(5);
            table.setHeaderRows(1);
            PdfPCell cell;
            cell = new PdfPCell(new Paragraph("Call Report", whiteFont));
            cell.setColspan(1);
            cell.setGrayFill(0.5f);
            cell.setBorder(0);
            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(itemisation.getAccountNumber(), whiteFont));
            cell.setColspan(6);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setTop(1);
            cell.setPaddingLeft(10.0f);
            cell.setGrayFill(0.5f);
            cell.setBorder(0);
            table.addCell(cell);
            table.setSpacingBefore(10);
            document.add(table);
            document.add(Chunk.NEWLINE);




            String[] headerStrings1 = new String[]{"Destination", "", "", "", "", "No. of Calls", "Cost"};
            for (String header : headerStrings1) {
                PdfPCell headerCell1 = new PdfPCell(new Paragraph(header, smallFont));
                headerCell1.setBorder(Rectangle.BOTTOM);
                headerCell1.setBorderWidth(1);
                headerCell1.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                table.addCell(headerCell1);
                table.setSpacingAfter(10f);
            }

            document.add(Chunk.NEWLINE);
            
            for (int r = 0; r < 4; r++) {
                cell = new PdfPCell(new Paragraph("Destination", smallFont));
                cell.setBorder(0);
                cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph("", smallFont));
                cell.setBorder(0);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph("", smallFont));
                cell.setBorder(0);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph("", smallFont));
                cell.setBorder(0);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph("", smallFont));
                cell.setBorder(0);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph("Calls", smallFont));
                cell.setBorder(0);
                cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph("Cost", smallFont));
                cell.setBorder(0);
                cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                table.addCell(cell);
            }
            document.add(Chunk.NEWLINE);

            String[] headerStrings2 = new String[]{"Total", "", "", "", "", "No. of Calls", "Cost"};
            for (String header : headerStrings2) {
                PdfPCell headerCell2 = new PdfPCell(new Paragraph(header, smallFont));

                headerCell2.setBorder(Rectangle.TOP);
                headerCell2.setBorderWidth(1);
                headerCell2.setHorizontalAlignment((Element.ALIGN_JUSTIFIED));
                table.addCell(headerCell2);
                table.setSpacingAfter(10f);

            }
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);

            cell = new PdfPCell(new Paragraph(new Phrase(new Chunk(""))));
            cell.setRowspan(1);
            cell.setPaddingBottom(1);
            cell.setBorder(0);
            cell.setColspan(7);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("Service Charges", whiteFont)); // 2nd section
            cell.setRowspan(1);
            cell.setColspan(7);
            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            //cell.setPaddingLeft(10.0f);
            cell.setGrayFill(0.5f);
            cell.setBorder(0);
            table.addCell(cell);
            table.setSpacingBefore(10);
            String[] headerStrings3 = new String[]{"UserID", "", "Description", "", "", "", "Cost"};
            for (String header : headerStrings3) {
                PdfPCell headerCell3 = new PdfPCell(new Paragraph(header, smallFont));
                headerCell3.setBorder(Rectangle.BOTTOM);
                headerCell3.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                headerCell3.setBorderWidth(1);
                table.addCell(headerCell3);
                table.setSpacingAfter(10f);

            }
            document.add(Chunk.NEWLINE);

            for(BillItem bItem : itemisation.getBillItems()) {
                cell = new PdfPCell(new Paragraph(bItem.getAccountNumber(), smallFont));
                cell.setBorder(0);
                cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph("", smallFont));
                cell.setBorder(0);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(bItem.getAssetName(), smallFont));
                cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                cell.setBorder(0);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph("", smallFont));
                cell.setBorder(0);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph("", smallFont));
                cell.setBorder(0);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph("", smallFont));
                cell.setBorder(0);
                ;
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(bItem.getRetalGross(), smallFont));
                cell.setBorder(0);
                cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);

                table.addCell(cell);
            }

            document.add(Chunk.NEWLINE);

            String[] headerStrings4 = new String[]{"", "", "", "", "", "Total", "Cost"};
            for (String header : headerStrings4) {
                PdfPCell headerCell4 = new PdfPCell(new Paragraph(header, smallFont));

                headerCell4.setBorder(Rectangle.TOP);
                headerCell4.setBorderWidth(1);
                headerCell4.setHorizontalAlignment((Element.ALIGN_JUSTIFIED));
                table.addCell(headerCell4);
                table.setSpacingAfter(10f);

            }
            // Itemisation Table
            cell = new PdfPCell(new Paragraph("Call Itemisation", whiteFont)); // 2nd section
            cell.setRowspan(1);
            cell.setColspan(7);
            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            //cell.setPaddingLeft(10.0f);
            cell.setGrayFill(0.5f);
            cell.setBorder(0);
            table.addCell(cell);
            table.setSpacingBefore(10);

            String[] headerStrings = new String[]{"UserID", "CallDate", "CallTime", "Duration", "Telephone Number", "Time Band", "Cost"};
            for (String header : headerStrings) {
                PdfPCell headerCell = new PdfPCell(new Paragraph(header, smallFont));
                headerCell.setBorder(Rectangle.BOTTOM);
                headerCell.setBorderWidth(1);
                table.addCell(headerCell);
                table.setSpacingAfter(10f);
                //table.addCell(new Phrase(Chunk.NEWLINE)); 
            }
            document.add(Chunk.NEWLINE);

            //ResultSet r = s.executeQuery(" SELECT UserID,CallDate,CallTime,Duration,Telephone Number,Time Band,Cost FROM LARGE_TABLE);
            //while(r.next())
            for (RatedCdr rCdr : itemisation.getRatedCdrs().get(itemisation.getAccountNumber())) {
                //PdfPCell cell = new PdfPCell(new Paragraph(String.valueOf(row)));
                cell = new PdfPCell(new Paragraph(rCdr.getUser(), smallFont));
                //cell.setHorizontalAlignment(100);
                cell.setBorder(0);
                table.addCell(cell);
                //cell = new PdfPCell(new Paragraph("UserID"));
                //cell.setBorder(0);
                //table.addCell(cell);
                cell = new PdfPCell(new Paragraph(rCdr.getStartTimestamp().toString(), smallFont));
                cell.setBorder(0);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(rCdr.getStartTimestamp().toString(), smallFont));
                cell.setBorder(0);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(String.valueOf(rCdr.getDuration()), smallFont));
                cell.setBorder(0);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(rCdr.getDestination(), smallFont));
                cell.setBorder(0);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(rCdr.getTimeBand(), smallFont));
                cell.setBorder(0);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(rCdr.getTimeBand(), smallFont));
                cell.setBorder(0);
                table.addCell(cell);
                //table.addCell(cell);
                //table.addCell(cell);
            }

            document.add(table);
            document.close();
        }
    }

    /** Inner class to add a header and a footer. */
    static class HeaderFooter extends PdfPageEventHelper {

        public void onEndPage(PdfWriter writer, Document document) {
            Rectangle rect = writer.getBoxSize("art");
            switch (writer.getPageNumber() % 2) {
                case 0:
                    ColumnText.showTextAligned(writer.getDirectContent(),
                            Element.ALIGN_RIGHT, new Phrase("even header"),
                            rect.getRight(), rect.getTop(), 0);
                    break;
                case 1:
                    ColumnText.showTextAligned(writer.getDirectContent(),
                            Element.ALIGN_CENTER, new Phrase("Invoice Number :"),
                            rect.getRight(), rect.getTop(), 0);
                    break;
            }
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_CENTER, new Phrase(String.format("page %d", writer.getPageNumber())),
                    (rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 18, 0);
        }
    }

    public void addImage(Document document) throws BadElementException,
            MalformedURLException, IOException, DocumentException {
        Image image1 = Image.getInstance(String.format(RESOURCE, "logo"));
        image1.scaleAbsolute(50, 50);
        document.add(image1);
    }
}
