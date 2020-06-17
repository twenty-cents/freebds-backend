package com.freebds.backend.common.web.graphicNovel.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BarcodeScanRequest {

    private Long libraryId;
    private String barcode;

    public Long getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(Long libraryId) {
        this.libraryId = libraryId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
