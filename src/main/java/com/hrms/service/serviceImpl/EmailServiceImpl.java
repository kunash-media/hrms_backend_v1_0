package com.hrms.service.serviceImpl;
import com.hrms.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    // ── Logo path inside src/main/resources ──────────────────────────────────
    private static final String LOGO_PATH = "static/images/logo-siec.jpeg";

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // ── Send OTP Email ────────────────────────────────────────────────────────

    @Override
    public void sendOtpEmail(String toEmail, String subject, String otp) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            // multipart = true  →  required for inline attachments (logo)
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(buildOtpEmailTemplate(otp), true);   // true = HTML

            // Attach SIEC logo as inline CID image
            addLogoIfExists(helper);

            mailSender.send(mimeMessage);
            logger.info("OTP email sent successfully to: {}", toEmail);

        } catch (MessagingException e) {
            logger.error("Failed to send OTP email to {}: {}", toEmail, e.getMessage());
            throw new RuntimeException("Failed to send OTP email: " + e.getMessage());
        }
    }

    // ── Attach logo as inline image (safe — won't crash if file missing) ─────

    private void addLogoIfExists(MimeMessageHelper helper) {
        try {
            ClassPathResource logoResource = new ClassPathResource(LOGO_PATH);
            if (logoResource.exists() && logoResource.isReadable()) {
                helper.addInline("siec-logo", logoResource);   // CID = "siec-logo"
                logger.info("SIEC logo attached successfully from: {}", LOGO_PATH);
            } else {
                logger.warn("Logo not found at '{}' — email will be sent without logo.", LOGO_PATH);
            }
        } catch (Exception e) {
            logger.warn("Could not attach logo to email: {} — email will be sent without logo.", e.getMessage());
        }
    }

    // ── HTML Email Template ───────────────────────────────────────────────────

    private String buildOtpEmailTemplate(String otp) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<title>Password Reset OTP – SIEC HRMS</title>" +
                "<style>" +
                "  body { margin:0; padding:0; font-family:'Segoe UI',Arial,sans-serif;" +
                "         background-color:#f0f4f8; }" +
                "  .wrapper { max-width:600px; margin:40px auto; background:#ffffff;" +
                "             border-radius:12px; overflow:hidden;" +
                "             box-shadow:0 4px 20px rgba(0,0,0,0.10); }" +
                "  .header { background:#ffffff; text-align:center;" +
                "             padding:30px 30px 22px;" +
                "             border-bottom:3px solid #4caf50; }" +
                "  .header img { max-height:80px; max-width:260px; object-fit:contain; }" +
                "  .header .fallback { font-size:22px; font-weight:700; color:#1a6b3c; display:none; }" +
                "  .body { padding:38px 40px 28px; color:#333333; }" +
                "  .body p { font-size:15px; line-height:1.75; margin:0 0 16px; }" +
                "  .otp-box { text-align:center; margin:28px 0; }" +
                "  .otp-code { display:inline-block; font-size:38px; font-weight:800;" +
                "              color:#1a6b3c; background:#f0faf2;" +
                "              border:3px solid #4caf50;" +
                "              border-radius:10px; padding:16px 44px;" +
                "              letter-spacing:14px;" +
                "              font-family:'Courier New',monospace; }" +
                "  .hint { color:#888; font-size:13px; margin-top:10px; }" +
                "  .notice { background:#fff8e1; border-left:4px solid #f59e0b;" +
                "            padding:14px 18px; border-radius:6px; margin:22px 0; }" +
                "  .notice ul { margin:6px 0 0 0; padding-left:18px;" +
                "               font-size:14px; color:#555; line-height:1.9; }" +
                "  .footer { background:#f8fafc; text-align:center;" +
                "            padding:20px; font-size:12px; color:#999;" +
                "            border-top:1px solid #e5e7eb; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='wrapper'>" +

                // ── HEADER: white bg + SIEC logo via CID ──────────────────────
                "  <div class='header'>" +
                "    <img src='cid:siec-logo' alt='SIEC Logo'" +
                "         onerror=\"this.style.display='none';" +
                "                  document.querySelector('.fallback').style.display='block';\" />" +
                "    <div class='fallback'>SIEC HRMS</div>" +
                "  </div>" +

                // ── BODY ──────────────────────────────────────────────────────
                "  <div class='body'>" +
                "    <p>Hello,</p>" +
                "    <p>We received a request to reset your <strong>SIEC Employee</strong> portal password." +
                "       Please use the OTP below to proceed.</p>" +
                "    <p>If you did not request this, please ignore this email or" +
                "       contact HR immediately.</p>" +

                "    <div class='otp-box'>" +
                "      <div class='otp-code'>" + otp + "</div>" +
                "      <div class='hint'>Select and copy the OTP above</div>" +
                "    </div>" +

                "    <div class='notice'>" +
                "      <strong>&#9888; Important:</strong>" +
                "      <ul>" +
                "        <li>This OTP is valid for <strong>5 minutes</strong> only.</li>" +
                "        <li>Do <strong>not</strong> share this code with anyone.</li>" +
                "        <li>If you did not request a password reset, contact HR immediately.</li>" +
                "      </ul>" +
                "    </div>" +

                "    <p>Regards,<br><strong>SIEC Support Team</strong><br>" +
                "    <span style='color:#888;font-size:13px;'>" +
                "    Sustainable Integrated Environment Consultant</span></p>" +
                "  </div>" +

                // ── FOOTER ────────────────────────────────────────────────────
                "  <div class='footer'>" +
                "    <p>&copy; 2026 SIEC – Sustainable Integrated Environment Consultant." +
                "       All rights reserved.</p>" +
                "    <p>This is an automated email — please do not reply.</p>" +
                "  </div>" +

                "</div>" +
                "</body>" +
                "</html>";
    }
}

