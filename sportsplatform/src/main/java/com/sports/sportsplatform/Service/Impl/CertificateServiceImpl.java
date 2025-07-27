package com.sports.sportsplatform.Service.Impl;


import com.sports.sportsplatform.Model.Certificate;
import com.sports.sportsplatform.Repository.CertificateRepository;
import com.sports.sportsplatform.Service.CertificateService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository certificateRepository;

    public CertificateServiceImpl(CertificateRepository certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    @Override
    public List<Certificate> getAllCertificates() {
        return certificateRepository.findAll();
    }

    @Override
    public Certificate getCertificateById(Long id) {
        return certificateRepository.findById(id).orElse(null);
    }

    @Override
    public void saveCertificate(Certificate certificate) {
        certificateRepository.save(certificate);
    }

    @Override
    public void deleteCertificate(Long id) {
        certificateRepository.deleteById(id);
    }

    public void verifyCertificate(Long id, boolean verified) {
        Certificate certificate = certificateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Certificate not found"));

        certificate.setVerified(verified);
        certificateRepository.save(certificate);
    }


}

