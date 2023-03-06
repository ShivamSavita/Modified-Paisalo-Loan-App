package com.artifex.utils;

/**
 * Created by @elage on 6/2/15.
 */
public interface DigitalizedEventCallback {

    String ERROR_OUTSIDE_VERTICAL = "ERROR_OUTSIDE_VERTICAL";
    String ERROR_OUTSIDE_HORIZONTAL = "ERROR_OUTSIDE_HORIZONTAL";

    void longPressOnPdfPosition(int page, float viewX, float viewY, float pdfX, float pdfY);

    void doubleTapOnPdfPosition(int page, float viewX, float viewY, float pdfX, float pdfY);

    void singleTapOnPdfPosition(int page, float viewX, float viewY, float pdfX, float pdfY);

    void error(String message);
}
