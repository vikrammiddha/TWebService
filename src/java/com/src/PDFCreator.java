package com.src;

import Common.src.com.Config.AppConfig;
import Common.src.com.Config.Configurator;
import Common.src.com.Exception.ResilientException;
import com.bean.BillItem;
import com.bean.CallReport;
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
import ewsconnect.EWSConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;
import org.apache.log4j.Logger;

public class PDFCreator {

    AppConfig config = Configurator.getAppConfig();
    public static Logger LOGGER = Logger.getLogger(EWSConnection.class);
    public final String RESOURCE = config.getPdfGetResource();
    private static Font smallFont = new Font(Font.FontFamily.TIMES_ROMAN, 10,
            Font.NORMAL);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
    private static Font smallBoldFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
    private static Font whiteFont = new Font(Font.FontFamily.TIMES_ROMAN, 11,
            Font.BOLD, BaseColor.WHITE);
    //Total Call Cost
    public static Double totalCost = 0.0;
    //Total Number of Calls
    public static int totalCalls = 0;

	public boolean valid = false;
    public PDFCreator(ArrayList<Itemisation> itemisations, String runId) throws Exception {

        
        //PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\Neha\\Documents\\NetBeansProjects\\JavaiText\\src\\javaitext\\AddBigTable.pdf"));
        for (Itemisation itemisation : itemisations) {
            Document document = new Document(PageSize.A4, 10, 10, 10, 10);
            LOGGER.info("Creating PDF for account :" + itemisation.getAccountNumber() + "at : " + config.getReportsDirectory() + "/" + itemisation.getAccountNumber() + "_" + runId + ".pdf");
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(config.getReportsDirectory() + "/" + itemisation.getAccountNumber() + "_" + runId + ".pdf"));
            LOGGER.info("Created PDF for account :" + itemisation.getAccountNumber() + "at : " + config.getReportsDirectory() + "/" + itemisation.getAccountNumber() + "_" + runId + ".pdf");
            writer.setBoxSize("art", new Rectangle(10, 10, 559, 788));
            HeaderFooter event = new HeaderFooter();
            writer.setPageEvent(event);
            document.open();
            addMetaData(document, itemisation);
            createTable1(document, itemisation);
            createTable2(document, itemisation);
            document.add(Chunk.NEXTPAGE);
            createTable3(document, itemisation);
            document.close();
        }
		this.valid = true;
    }

    private static void addMetaData(Document document, Itemisation itemisation) {
        document.addTitle("Itemisation Report");
        document.addSubject("Report for : " + itemisation.getAccountNumber());
        document.addKeywords(itemisation.getEmailAddress());
        document.addAuthor("MP");
        document.addCreator("MakePositve Ltd.");
    }

    private static void addTitlePage(Document document)
            throws DocumentException {
        PdfPTable table8 = new PdfPTable(3);
        table8.setWidthPercentage(100f);
        table8.setHorizontalAlignment(100);
        PdfPCell cell8;
        try {
            cell8 = new PdfPCell(new Paragraph(" ", smallFont));
            cell8.setBorder(0);
            cell8.setHorizontalAlignment(Element.ALIGN_BOTTOM);
            cell8.setHorizontalAlignment(Element.ALIGN_RIGHT);

            table8.addCell(cell8);
            cell8 = new PdfPCell(new Paragraph("Invoice Number:" + "EUI-001924", smallFont));
            cell8.setBorder(0);
            cell8.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table8.addCell(cell8);

            cell8 = new PdfPCell(new Paragraph("Resilient Networks PLC ", smallFont));
            cell8.setBorder(0);
            cell8.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table8.addCell(cell8);

            cell8 = new PdfPCell(new Paragraph(" ", smallFont));
            cell8.setBorder(0);
            cell8.setHorizontalAlignment(Element.ALIGN_TOP);
            cell8.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table8.addCell(cell8);
            cell8 = new PdfPCell(new Paragraph("Date (And Tax Point):" + "31-07-2011", smallFont));
            cell8.setBorder(0);
            cell8.setHorizontalAlignment(Element.ALIGN_TOP);
            cell8.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table8.addCell(cell8);

            cell8 = new PdfPCell(new Paragraph("25 - 27 Shaftesbury Avenue", smallFont));
            cell8.setBorder(0);
            cell8.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table8.addCell(cell8);

            cell8 = new PdfPCell(new Paragraph("", smallFont));
            cell8.setBorder(0);
            cell8.setHorizontalAlignment(Element.ALIGN_TOP);
            cell8.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table8.addCell(cell8);
            cell8 = new PdfPCell(new Paragraph("Billing Period (Ending): " + "31-07-2011", smallFont));
            cell8.setBorder(0);
            cell8.setHorizontalAlignment(Element.ALIGN_TOP);
            cell8.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table8.addCell(cell8);

            cell8 = new PdfPCell(new Paragraph("London W1D 7EQ", smallFont));
            cell8.setBorder(0);
            cell8.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table8.addCell(cell8);

            document.add(table8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Start a new page
        //document.newPage();
    }

    public static void createTable1(Document document, Itemisation itemisation) throws DocumentException {
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
        cell = new PdfPCell(new Phrase(itemisation.getAccountName(), whiteFont));
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
            PdfPCell headerCell1 = new PdfPCell(new Paragraph(header, smallBold));
            headerCell1.setBorder(Rectangle.BOTTOM);
            headerCell1.setBorderWidth(1);
            headerCell1.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            table.addCell(headerCell1);
            table.setSpacingAfter(10f);
        }

        document.add(Chunk.NEWLINE);
        totalCost = 0.0;
        totalCalls = 0;
        for (CallReport cR : itemisation.getSummary()) {
            cell = new PdfPCell(new Paragraph(cR.getZoneDestination(), smallBoldFont));
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
            cell = new PdfPCell(new Paragraph(String.valueOf(cR.getCount()), smallBoldFont));
            cell.setBorder(0);
            //Sum up the total calls
            totalCalls += cR.getCount();
            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph(String.valueOf(cR.getRetailPrice()), smallBoldFont));
            cell.setBorder(0);
            //Sum up the total value
            totalCost += cR.getRetailPrice();
            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            table.addCell(cell);
        }
        document.add(Chunk.NEWLINE);

        String[] headerStrings2 = new String[]{"Total", "", "", "", "", String.valueOf(totalCalls), String.valueOf(totalCost)};
        for (String header : headerStrings2) {
            PdfPCell headerCell2 = new PdfPCell(new Paragraph(header, smallBold));

            headerCell2.setBorder(Rectangle.TOP);
            headerCell2.setBorderWidth(1);
            headerCell2.setHorizontalAlignment((Element.ALIGN_JUSTIFIED));
            table.addCell(headerCell2);
            table.setSpacingAfter(10f);

        }
        document.add(table);
        document.add(Chunk.NEWLINE);
    }

    public static void createTable2(Document document, Itemisation itemisation) throws DocumentException {

        PdfPTable table1 = new PdfPTable(7);
        table1.setWidthPercentage(100f);
        table1.setHorizontalAlignment(100);
        table1.setSpacingBefore(5);
        table1.setHeaderRows(1);
        PdfPCell cell2;
        cell2 = new PdfPCell(new Paragraph("Service Charges", whiteFont)); // 2nd section
        cell2.setRowspan(1);
        cell2.setColspan(7);
        cell2.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        //cell.setPaddingLeft(10.0f);
        cell2.setGrayFill(0.5f);
        cell2.setBorder(0);
        table1.addCell(cell2);
        table1.setSpacingBefore(10);

        String[] headerStrings3 = new String[]{"UserID", "", "Description", "", "", "", "Cost"};
        for (String header : headerStrings3) {
            PdfPCell headerCell3 = new PdfPCell(new Paragraph(header, smallBold));
            headerCell3.setBorder(Rectangle.BOTTOM);
            headerCell3.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            headerCell3.setBorderWidth(1);
            table1.addCell(headerCell3);
            table1.setSpacingAfter(10f);

        }
        document.add(Chunk.NEWLINE);
        totalCost = 0.0;
        for (BillItem bItem : itemisation.getBillItems()) {
            cell2 = new PdfPCell(new Paragraph(bItem.getIdentifier(), smallBoldFont));
            cell2.setBorder(0);
            cell2.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            table1.addCell(cell2);
            cell2 = new PdfPCell(new Paragraph("", smallFont));
            cell2.setBorder(0);
            table1.addCell(cell2);
            cell2 = new PdfPCell(new Paragraph(bItem.getAssetName(), smallBoldFont));
            cell2.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            cell2.setBorder(0);
            table1.addCell(cell2);
            cell2 = new PdfPCell(new Paragraph("", smallFont));
            cell2.setBorder(0);
            table1.addCell(cell2);
            cell2 = new PdfPCell(new Paragraph("", smallFont));
            cell2.setBorder(0);
            table1.addCell(cell2);
            cell2 = new PdfPCell(new Paragraph("", smallFont));
            cell2.setBorder(0);
            table1.addCell(cell2);
            cell2 = new PdfPCell(new Paragraph(bItem.getRetalGross(), smallBoldFont));
            totalCost += Double.valueOf(bItem.getRetalGross());
            cell2.setBorder(0);
            cell2.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);

            table1.addCell(cell2);
        }

        document.add(Chunk.NEWLINE);

        String[] headerStrings4 = new String[]{"", "", "", "", "", "Total", String.valueOf(totalCost)};
        for (String header : headerStrings4) {
            PdfPCell headerCell4 = new PdfPCell(new Paragraph(header, smallBold));

            headerCell4.setBorder(Rectangle.TOP);
            headerCell4.setBorderWidth(1);
            headerCell4.setHorizontalAlignment((Element.ALIGN_JUSTIFIED));
            table1.addCell(headerCell4);
            table1.setSpacingAfter(10f);

        }
        document.add(table1);
        document.add(Chunk.NEWLINE);
    }

    public static void createTable3(Document document, Itemisation itemisation) throws DocumentException {
        PdfPTable table2 = new PdfPTable(7);
        table2.setWidthPercentage(100f);
        table2.setHorizontalAlignment(100);
        table2.setSpacingBefore(5);
        table2.setHeaderRows(1);
        PdfPCell cell3;     // Itemisation Table
        cell3 = new PdfPCell(new Paragraph("Call Itemisation", whiteFont)); // 2nd section

        cell3.setRowspan(1);
        cell3.setColspan(7);
        cell3.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        //cell.setPaddingLeft(10.0f);
        cell3.setGrayFill(0.5f);
        cell3.setBorder(0);
        table2.addCell(cell3);
        table2.setSpacingBefore(10);

        String[] headerStrings = new String[]{"UserID", "CallDate", "CallTime", "Duration", "Telephone Number", "Time Band", "Cost"};
        for (String header : headerStrings) {
            PdfPCell headerCell = new PdfPCell(new Paragraph(header, smallBold));
            headerCell.setBorder(Rectangle.BOTTOM);
            headerCell.setBorderWidth(1);
            table2.addCell(headerCell);
            table2.setSpacingAfter(10f);
            //table.addCell(new Phrase(Chunk.NEWLINE)); 
        }
        document.add(Chunk.NEWLINE);

        //ResultSet r = s.executeQuery(" SELECT UserID,CallDate,CallTime,Duration,Telephone Number,Time Band,Cost FROM LARGE_TABLE);
        //while(r.next())
        totalCost = 0.0;
        for (RatedCdr rCdr : itemisation.getRatedCdrs()) {
            //PdfPCell cell = new PdfPCell(new Paragraph(String.valueOf(row)));
            cell3 = new PdfPCell(new Paragraph(rCdr.getUser(), smallFont));
            //cell.setHorizontalAlignment(100);
            cell3.setBorder(0);
            table2.addCell(cell3);
            //cell = new PdfPCell(new Paragraph("UserID"));
            //cell.setBorder(0);
            //table.addCell(cell);
            cell3 = new PdfPCell(new Paragraph(rCdr.getStartTimestamp().toString(), smallFont));
            cell3.setBorder(0);
            table2.addCell(cell3);
            cell3 = new PdfPCell(new Paragraph(rCdr.getStartTimestamp().toString(), smallFont));
            cell3.setBorder(0);
            table2.addCell(cell3);
            cell3 = new PdfPCell(new Paragraph(String.valueOf(rCdr.getDuration()), smallFont));
            cell3.setBorder(0);
            table2.addCell(cell3);
            cell3 = new PdfPCell(new Paragraph(rCdr.getDestination(), smallFont));
            cell3.setBorder(0);
            table2.addCell(cell3);
            cell3 = new PdfPCell(new Paragraph(rCdr.getTimeBand(), smallFont));
            cell3.setBorder(0);
            table2.addCell(cell3);
            cell3 = new PdfPCell(new Paragraph(String.valueOf(rCdr.getRetailPrice()), smallFont));
            totalCost += rCdr.getRetailPrice();
            cell3.setBorder(0);
            table2.addCell(cell3);
        }

        String[] headerStrings5 = new String[]{"", "", "", "", "", "Total", String.valueOf(totalCost)};
        for (String header : headerStrings5) {
            PdfPCell headerCell4 = new PdfPCell(new Paragraph(header, smallBold));

            headerCell4.setBorder(Rectangle.TOP);
            headerCell4.setBorderWidth(1);
            headerCell4.setHorizontalAlignment((Element.ALIGN_JUSTIFIED));
            table2.addCell(headerCell4);
            table2.setSpacingAfter(10f);

        }
        document.add(table2);

    }

    static class HeaderFooter extends PdfPageEventHelper {
        public static Logger LOGGER = Logger.getLogger(EWSConnection.class);
        @Override
        public void onStartPage(PdfWriter writer, Document document) {
            try {
                Rectangle rect = writer.getBoxSize("art");
                AppConfig config = Configurator.getAppConfig();
                String RESOURCE = config.getPdfGetResource();
                try {
                    addImage(document, RESOURCE);
                    addTitlePage(document);
                    document.add(Chunk.NEWLINE);
                } catch (DocumentException ex) {
                    LOGGER.error("Exception while adding Header/Footer on PDF. Cause :" + ex.getMessage());;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ColumnText.showTextAligned(writer.getDirectContent(),
                        Element.ALIGN_RIGHT, new Phrase(String.format("page %d", writer.getPageNumber()), smallFont),
                        rect.getRight(), rect.getTop() + 20, 0);
                // (rect.getLeft() + rect.getRight()) / 2, rect.getTop() + 18, 0);
            } catch (ResilientException ex) {
                LOGGER.error("Exception while creating PDF. Cause :" + ex.getMessage());
            }
        }

        private static void addImage(Document document, String Resource) throws BadElementException,
                MalformedURLException, IOException, DocumentException {
            Image image1 = Image.getInstance(String.format(Resource, "logo"));
            image1.setAlignment(Element.ALIGN_LEFT);
            image1.scaleAbsoluteHeight(15);
            image1.scaleAbsoluteWidth(15);
            image1.scalePercent(20);
            document.add(image1);
        }
    }
}
