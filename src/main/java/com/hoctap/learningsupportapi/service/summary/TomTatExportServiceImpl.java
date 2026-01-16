package com.hoctap.learningsupportapi.service.summary;

import com.hoctap.learningsupportapi.model.dto.summary.TomTatExportDTO;
import com.hoctap.learningsupportapi.model.entity.TomTatBaiHoc;
import com.hoctap.learningsupportapi.repository.MonHocCaNhanRepository;
import com.hoctap.learningsupportapi.repository.TomTatBaiHocRepository;
import com.hoctap.learningsupportapi.repository.TomTatTuKhoaRepository;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.ConverterProperties;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;


import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TomTatExportServiceImpl implements TomTatExportService {

    private final TomTatBaiHocRepository tomTatRepo;
    private final TomTatTuKhoaRepository tuKhoaRepo;
    private final MonHocCaNhanRepository monHocRepo;

    private TomTatExportDTO loadData(Integer maTomTat) {

        TomTatBaiHoc entity = tomTatRepo.findById(maTomTat)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tóm tắt"));

        String tenMon = "";

        if (entity.getMaMonHoc() != null) {
            tenMon = monHocRepo.findById(entity.getMaMonHoc())
                    .map(m -> m.getTenMonHoc())
                    .orElse("");
        }

        List<String> tuKhoa = tuKhoaRepo.findByMaTomTat(maTomTat)
                .stream()
                .map(k -> k.getTuKhoa())
                .toList();

        return TomTatExportDTO.builder()
                .tieuDe(entity.getTieuDe())
                .noiDung(entity.getNoiDungTomTat())
                .tenMonHoc(tenMon)
                .soTu(entity.getSoTu())
                .soTrang(entity.getSoTrang())
                .ngayTao(entity.getNgayTao())
                .tuKhoa(tuKhoa)
                .build();
    }

    // ================= PDF =================

    @Override
    public ByteArrayOutputStream exportToPdf(Integer maTomTat) {

        TomTatExportDTO dto = loadData(maTomTat);

        try {
            // ===== 1. Convert Markdown → HTML =====
            Parser parser = Parser.builder().build();
            Node document = parser.parse(dto.getNoiDung());
            HtmlRenderer renderer = HtmlRenderer.builder().build();

            String markdownHtml = renderer.render(document);

            // ===== 2. Tạo HTML đầy đủ với template =====
            String fullHtml = """
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        font-size: 12px;
                        line-height: 1.6;
                        margin: 40px;
                    }

                    h1, h2, h3 {
                        color: #2c3e50;
                    }

                    .header {
                        text-align: center;
                        margin-bottom: 20px;
                    }

                    .info {
                        margin-bottom: 20px;
                    }

                    .keywords {
                        font-style: italic;
                        margin-bottom: 20px;
                    }

                    .content {
                        margin-top: 20px;
                    }

                    ul, ol {
                        margin-left: 20px;
                    }

                </style>
            </head>
            <body>

                <div class="header">
                    <h2>%s</h2>
                </div>

                <div class="info">
                    <p><b>Môn học:</b> %s</p>
                    <p><b>Số từ:</b> %s</p>
                    <p><b>Số trang:</b> %s</p>
                    <p><b>Ngày tạo:</b> %s</p>
                </div>

                <div class="keywords">
                    <b>Từ khóa:</b> %s
                </div>

                <hr/>

                <div class="content">
                    %s
                </div>

            </body>
            </html>
            """.formatted(
                    dto.getTieuDe(),
                    dto.getTenMonHoc(),
                    dto.getSoTu(),
                    dto.getSoTrang(),
                    dto.getNgayTao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                    String.join(", ", dto.getTuKhoa()),
                    markdownHtml
            );

            // ===== 3. Render HTML → PDF =====
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            ConverterProperties props = new ConverterProperties();
            props.setCharset("UTF-8");

            HtmlConverter.convertToPdf(fullHtml, out, props);

            return out;

        } catch (Exception e) {
            throw new RuntimeException("Lỗi export PDF bằng iText", e);
        }
    }
    
    // ================= DOCX =================

    @Override
    public ByteArrayOutputStream exportToDocx(Integer maTomTat) {

        TomTatExportDTO dto = loadData(maTomTat);

        try (XWPFDocument doc = new XWPFDocument()) {

            XWPFParagraph title = doc.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);

            XWPFRun runTitle = title.createRun();
            runTitle.setBold(true);
            runTitle.setFontSize(16);
            runTitle.setText(dto.getTieuDe());

            XWPFParagraph info = doc.createParagraph();
            XWPFRun run = info.createRun();

            run.setText("Môn học: " + dto.getTenMonHoc());
            run.addBreak();

            run.setText("Số từ: " + dto.getSoTu());
            run.addBreak();

            run.setText("Số trang: " + dto.getSoTrang());
            run.addBreak();

            run.setText("Ngày tạo: " +
                    dto.getNgayTao()
                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            run.addBreak();

            run.setText("Từ khóa: " + String.join(", ", dto.getTuKhoa()));
            run.addBreak();
            run.addBreak();

            XWPFParagraph content = doc.createParagraph();
            XWPFRun runContent = content.createRun();

            MarkdownDocxRenderer.renderMarkdownToDocx(dto.getNoiDung(), doc);


            ByteArrayOutputStream out = new ByteArrayOutputStream();
            doc.write(out);
            return out;

        } catch (IOException e) {
            throw new RuntimeException("Lỗi export DOCX", e);
        }
    }

    public class MarkdownDocxRenderer {

        public static void renderMarkdownToDocx(String markdown, XWPFDocument doc) {

            String[] lines = markdown.split("\n");

            for (String line : lines) {

                XWPFParagraph p = doc.createParagraph();
                XWPFRun run = p.createRun();

                // Header ##
                if (line.startsWith("## ")) {
                    run.setBold(true);
                    run.setFontSize(14);
                    run.setText(line.replace("## ", ""));
                }

                // Header ###
                else if (line.startsWith("### ")) {
                    run.setBold(true);
                    run.setFontSize(13);
                    run.setText(line.replace("### ", ""));
                }

                // List
                else if (line.matches("^\\d+\\. .*")) {
                    run.setText(line);
                }

                // Bold **
                else if (line.contains("**")) {

                    String[] parts = line.split("\\*\\*");

                    boolean bold = false;

                    for (String part : parts) {
                        XWPFRun r = p.createRun();
                        r.setBold(bold);
                        r.setText(part);
                        bold = !bold;
                    }

                } else {
                    run.setText(line);
                }
            }
        }
    }

}
