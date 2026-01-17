package com.hoctap.learningsupportapi.service.summary;

import java.io.ByteArrayOutputStream;

public interface TomTatExportService {

    ByteArrayOutputStream exportToPdf(Integer maTomTat);

    ByteArrayOutputStream exportToDocx(Integer maTomTat);
}
