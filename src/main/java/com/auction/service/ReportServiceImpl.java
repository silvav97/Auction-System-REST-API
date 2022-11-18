package com.auction.service;

import com.auction.dto.PDFAuctionResponseDTO;
import com.auction.dto.PDFBidResponseDTO;
import com.auction.entity.Auction;
import com.auction.entity.Bid;
import com.auction.entity.Role;
import com.auction.entity.User;
import com.auction.exception.AuctionDoesNotBelongToUserException;
import com.auction.exception.FormatFileNotAvailableException;
import com.auction.exception.ResourceNotFoundException;
import com.auction.repository.AuctionRepository;
import com.auction.repository.BidRepository;
import com.auction.repository.RoleRepository;
import com.auction.security.JwtAuthenticationFilter;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.auction.util.MyMappers.mapFromAuctionToPDFAuctionResponseDTO;
import static com.auction.util.MyMappers.mapFromBidToPDFBidResponseDTO;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BidRepository bidRepository;


    @Override
    public void exportAuctionsReport(String path, String format, HttpServletRequest request) throws FileNotFoundException, JRException {
        jwtAuthenticationFilter.getTheUserFromRequest(request);
        if(!(format.equalsIgnoreCase("pdf")||format.equalsIgnoreCase("html"))) {throw new FormatFileNotAvailableException(format);}

        List<Auction> auctions = auctionRepository.findAll();
        List<PDFAuctionResponseDTO> listAuctions =  auctions.stream().map(auction -> mapFromAuctionToPDFAuctionResponseDTO(auction)).collect(Collectors.toList());
        Map<String, Object> parameters = new HashMap<>();
        String fileName = path +"Auctions";
        String classpath = "classpath:auction.jrxml";

        generateReport(classpath, listAuctions, parameters, path, format, fileName);
    }

    @Override
    public void exportBidsReport(String path, String format, Long auctionId, HttpServletRequest request) throws JRException, FileNotFoundException {
        User user = jwtAuthenticationFilter.getTheUserFromRequest(request);
        Role role = roleRepository.findByName("ROLE_ADMIN").orElseThrow(() -> new ResourceNotFoundException("Role","Name","ROLE_ADMIN"));
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new ResourceNotFoundException("Auction","AuctionId",String.valueOf(auctionId)));
        if(!(user.getRoles().contains(role) || auction.getUser().getUsername().equals(user.getUsername()))) {throw new AuctionDoesNotBelongToUserException();}
        if(!(format.equalsIgnoreCase("pdf")||format.equalsIgnoreCase("html"))) {throw new FormatFileNotAvailableException(format);}

        List<Bid> bids = bidRepository.findByAuctionId(auctionId);
        List<PDFBidResponseDTO> listBids =  bids.stream().map(bid -> mapFromBidToPDFBidResponseDTO(bid)).collect(Collectors.toList());
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("auctionId", auctionId);
        parameters.put("auctioneerName", auction.getUser().getName());
        parameters.put("product", auction.getProduct());
        parameters.put("auctioneerEmail", auction.getUser().getEmail());
        String fileName = path +user.getUsername().toUpperCase()+"_AuctionID_#"+auctionId+"bids";
        String classpath = "classpath:bid.jrxml";

        generateReport(classpath, listBids, parameters, path, format, fileName);
    }

    private void generateReport(String classpath, List<?> dataSource, Map<String, Object> parameters, String path, String format, String fileName) throws FileNotFoundException, JRException {
        File file = ResourceUtils.getFile(classpath);
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(dataSource);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, ds);
        if(format.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, fileName+".html");
        }
        if(format.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, fileName+".pdf");
        }
    }


}
