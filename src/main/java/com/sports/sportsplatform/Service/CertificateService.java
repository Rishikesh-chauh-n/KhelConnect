package com.sports.sportsplatform.Service;




import com.sports.sportsplatform.Model.Certificate;

import java.util.List;

public interface CertificateService {
    List<Certificate> getAllCertificates();
    Certificate getCertificateById(Long id);
    void saveCertificate(Certificate certificate);
    void deleteCertificate(Long id);

    void verifyCertificate(Long id, boolean verified);
}
