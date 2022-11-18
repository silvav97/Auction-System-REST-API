package com.auction.service;

import net.sf.jasperreports.engine.JRException;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;

public interface ReportService {

    // Reports with Jasper
    void exportAuctionsReport(String path, String format, HttpServletRequest request) throws FileNotFoundException, JRException;
    void exportBidsReport(String path, String format, Long auctionId, HttpServletRequest request) throws FileNotFoundException, JRException;


}
