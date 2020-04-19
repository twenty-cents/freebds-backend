package com.freebds.backend.common.web.requests;

public class LibraryGraphicNovelFormatRequest {

    private Long id;
    private int format;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }
}
