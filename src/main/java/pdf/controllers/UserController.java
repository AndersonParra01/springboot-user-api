package pdf.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import pdf.models.UserModel;
import pdf.services.PdfService;
import pdf.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

//Excel
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@RestController
@RequestMapping("users")
@CrossOrigin({ "*" })
public class UserController {

    @Autowired
    UserService service;

    @Autowired
    PdfService pdfService;

    @GetMapping("")
    public List<UserModel> getMethodName() {
        return service.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserModel getUserById(@PathVariable("id") Long id) {
        return service.getUserById(id);
    }

    @GetMapping("/generate-user-report/{userId}")
    public ResponseEntity<ByteArrayResource> generateReportUser(@PathVariable Long userId) {

        UserModel user = service.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            String htmlContent = generateHtmlReport(user);
            byte[] pdfBytes = pdfService.generatePdfFromHtml(htmlContent);

            ByteArrayResource resource = new ByteArrayResource(pdfBytes);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=user_report.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(pdfBytes.length)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        }
    }

    public String generateHtmlReport(UserModel user) {
        StringBuilder htmlContent = new StringBuilder();

        htmlContent.append("<html><body>");
        htmlContent.append("<h1>Informe de Usuario</h1>");
        htmlContent.append("<p><strong>ID:</strong> ").append(user.getId()).append("</p>");
        htmlContent.append("<p><strong>Nombres:</strong> ").append(user.getNames()).append("</p>");
        htmlContent.append("<p><strong>Apellidos:</strong> ").append(user.getLastnames()).append("</p>");
        htmlContent.append("<p><strong>Cédula de Identidad:</strong> ").append(user.getIdentityCard()).append("</p>");
        htmlContent.append("<p><strong>Nombre de Usuario:</strong> ").append(user.getUsername()).append("</p>");
        htmlContent.append("</body></html>");

        return htmlContent.toString();
    }

    @GetMapping("/generate-excel/{userId}")
    public ResponseEntity<byte[]> generateExcelForUser(@PathVariable Long userId) throws IOException {
        UserModel user = service.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Usuario");

            String[] headers = { "ID", "Nombres", "Apellidos", "Cédula", "Usuario" };
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }
            Row dataRow = sheet.createRow(1);
            dataRow.createCell(0).setCellValue(user.getId());
            dataRow.createCell(1).setCellValue(user.getNames());
            dataRow.createCell(2).setCellValue(user.getLastnames());
            dataRow.createCell(3).setCellValue(user.getIdentityCard());
            dataRow.createCell(4).setCellValue(user.getUsername());

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            HttpHeaders headersRes = new HttpHeaders();
            headersRes.setContentType(
                    MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headersRes.setContentDispositionFormData("attachment", "usuario_" + userId + ".xlsx");

            return ResponseEntity.ok()
                    .headers(headersRes)
                    .body(outputStream.toByteArray());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/create")
    public UserModel createUser(@RequestBody UserModel user) {
        return service.create(user);
    }

    @PutMapping("/{id}")
    public UserModel updateUserById(@PathVariable Long id, @RequestBody UserModel user) {
        return service.updateById(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        service.deleteByIdUser(id);
    }

}
