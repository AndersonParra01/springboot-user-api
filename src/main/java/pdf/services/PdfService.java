package pdf.services;

import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.springframework.stereotype.Service;


@Service
public class PdfService {
    public byte[] generatePdfFromHtml(String htmlContent) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        renderer.createPDF(out, true);
        out.close();
        return out.toByteArray();
    }
}
