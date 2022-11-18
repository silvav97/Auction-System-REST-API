package com.auction.service;

import com.auction.dto.*;
import com.auction.entity.*;
import com.auction.exception.AuctionAlreadyEndedException;
import com.auction.exception.AuctionDoesNotBelongToUserException;
import com.auction.exception.ResourceNotFoundException;
import com.auction.exception.ThereWasNoWinnerException;
import com.auction.repository.*;
import com.auction.security.JwtAuthenticationFilter;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;


import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.auction.util.MyMappers.*;

@Service
public class AuctionServiceImpl implements AuctionService {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private BidServiceImpl bidService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private WinnerBidRepository winnerBidRepository;


    @Override
    public Auction createAuction(AuctionDTO auctionDTO, HttpServletRequest request) {
        User user = jwtAuthenticationFilter.getTheUserFromRequest(request);
        Auction auction = new Auction();
        auction.setProduct(auctionDTO.getProduct());
        auction.setDescription(auctionDTO.getDescription());
        auction.setInitialValue(auctionDTO.getInitialValue());
        auction.setActive(true);
        auction.setUser(user);
        return auctionRepository.save(auction);
    }


    @Override
    public WinnerBid endAuction(Long auctionId, HttpServletRequest request) {
        User user = jwtAuthenticationFilter.getTheUserFromRequest(request);
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new ResourceNotFoundException("Auction","AuctionId",String.valueOf(auctionId)));
        if(!auction.getUser().equals(user))   throw new AuctionDoesNotBelongToUserException();
        if(auction.isActive() == false)   throw new AuctionAlreadyEndedException();

        auction.setActive(false);
        auctionRepository.save(auction);

        if(auction.getHighestBidderId() == null)   throw new ThereWasNoWinnerException();

        WinnerBid winnerBid = registerWinnerAndTransferMoneyFromWinnerToAuctioneer(auction);
        return winnerBid;
    }

    @Transactional
    private WinnerBid registerWinnerAndTransferMoneyFromWinnerToAuctioneer(Auction auction) {
        User winner = userRepository.findById(auction.getHighestBidderId())
                .orElseThrow(() -> new ResourceNotFoundException("User","UserID",String.valueOf(auction.getHighestBidderId())));
        User auctioneer = auction.getUser();
        winner.setCredit(winner.getCredit() - auction.getHighestBid());   // Take money from bidder
        auction.getUser().setCredit(auction.getUser().getCredit() + auction.getHighestBid());  // Deposite Money To Auctioneer

        userRepository.saveAll(List.of(winner,auctioneer));
        WinnerBid winnerBid = new WinnerBid(auction,winner);
        return winnerBidRepository.save(winnerBid);
    }

    // Only Admin role users
    @Override
    public List<AuctionResponseDTO> getAllAuctions(HttpServletRequest request) {
        jwtAuthenticationFilter.getTheUserFromRequest(request);
        List<AuctionResponseDTO> listAuctions = new ArrayList<>();
        for(Auction auction :auctionRepository.findAll()){
            AuctionResponseDTO auctionResponseDTO = mapFromAuctionToAuctionResponseDTO(auction);
            listAuctions.add(auctionResponseDTO);
        }
        return listAuctions;
    }

    // Only Admin role users
    @Override
    public PaginatedAuctionResponseDTO getAllAuctionsWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String sortDireccion, HttpServletRequest request) {
        jwtAuthenticationFilter.getTheUserFromRequest(request);
        Sort sort = sortDireccion.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Auction> auctions = auctionRepository.findAll(pageable);
        return returnPaginatedAuctionResponseDTO(auctions);
    }

    // Only Admin role users
    @Override
    public List<AuctionResponseDTO> getAllAuctionsByUser(Long userId, HttpServletRequest request) {
        jwtAuthenticationFilter.getTheUserFromRequest(request);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User","UserId",String.valueOf(userId)));
        List<AuctionResponseDTO> listAuctions = new ArrayList<>();
        for(Auction auction : auctionRepository.findByUser(user)){
            AuctionResponseDTO auctionResponseDTO = mapFromAuctionToAuctionResponseDTO(auction);
            listAuctions.add(auctionResponseDTO);
        }
        return listAuctions;
    }

    // Only Admin role users
    @Override
    public PaginatedAuctionResponseDTO getAllAuctionsByUserWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String sortDireccion, Long userId, HttpServletRequest request) {
        jwtAuthenticationFilter.getTheUserFromRequest(request);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User","UserId",String.valueOf(userId)));
        Sort sort = sortDireccion.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Auction> auctions = auctionRepository.findAllByUser(user, pageable);
        return returnPaginatedAuctionResponseDTO(auctions);
    }

    @Override
    public List<AuctionResponseDTO> getAllActiveAuctions(HttpServletRequest request) {
        jwtAuthenticationFilter.getTheUserFromRequest(request);
        List<AuctionResponseDTO> listAuctions = new ArrayList<>();
        for(Auction auction : auctionRepository.findByActiveTrue()){
            AuctionResponseDTO auctionResponseDTO = mapFromAuctionToAuctionResponseDTO(auction);
            listAuctions.add(auctionResponseDTO);
        }
        return listAuctions;
    }

    @Override
    public PaginatedAuctionResponseDTO getAllActiveAuctionsWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String sortDireccion, HttpServletRequest request) {
        jwtAuthenticationFilter.getTheUserFromRequest(request);
        Sort sort = sortDireccion.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Auction> auctions = auctionRepository.findAllByActiveTrue(pageable);
        return returnPaginatedAuctionResponseDTO(auctions);
    }

    @Override
    public List<AuctionResponseDTO> getAllMyAuctions(HttpServletRequest request) {
        User user = jwtAuthenticationFilter.getTheUserFromRequest(request);
        List<AuctionResponseDTO> listAuctions = new ArrayList<>();
        for(Auction auction : auctionRepository.findByUser(user)){
            AuctionResponseDTO auctionResponseDTO = mapFromAuctionToAuctionResponseDTO(auction);
            listAuctions.add(auctionResponseDTO);
        }
        return listAuctions;
    }

    @Override
    public PaginatedAuctionResponseDTO getAllMyAuctionsWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String sortDireccion, HttpServletRequest request) {
        User user = jwtAuthenticationFilter.getTheUserFromRequest(request);
        Sort sort = sortDireccion.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Auction> auctions = auctionRepository.findAllByUser(user, pageable);
        return returnPaginatedAuctionResponseDTO(auctions);
    }

    @Override
    public List<BidResponseDTO> getAllBidsFromAuction(Long auctionId, HttpServletRequest request) {
        User user = jwtAuthenticationFilter.getTheUserFromRequest(request);
        Role role = roleRepository.findByName("ROLE_ADMIN").orElseThrow(() -> new ResourceNotFoundException("Role","Name","ROLE_ADMIN"));
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new ResourceNotFoundException("Auction","AuctionId",String.valueOf(auctionId)));

        if(!(user.getRoles().contains(role) || auction.getUser().getUsername().equals(user.getUsername()))) {
            throw new AuctionDoesNotBelongToUserException();
        }

        List<BidResponseDTO> listBids = new ArrayList<>();
        for(Bid bid : bidRepository.findByAuctionId(auctionId)){
            BidResponseDTO bidResponseDTO = mapFromBidToBidResponseDTO(bid);
            listBids.add(bidResponseDTO);
        }
        return listBids;
    }

    @Override
    public PaginatedBidResponseDTO getAllBidsFromAuctionWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String sortDireccion, Long auctionId, HttpServletRequest request) {
        User user = jwtAuthenticationFilter.getTheUserFromRequest(request);
        Role role = roleRepository.findByName("ROLE_ADMIN").orElseThrow(() -> new ResourceNotFoundException("Role","Name","ROLE_ADMIN"));
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new ResourceNotFoundException("Auction","AuctionId",String.valueOf(auctionId)));

        if(!(user.getRoles().contains(role) || auction.getUser().getUsername().equals(user.getUsername()))) {
            throw new AuctionDoesNotBelongToUserException();
        }

        Sort sort = sortDireccion.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        //Page<Bid> bids = bidRepository.findAllByUser(user,pageable);
        Page<Bid> bids = bidRepository.findAllByAuctionId(auctionId,pageable);

        return returnPaginatedBidResponseDTO(bids);

    }

    @Override
    public String exportAuctionsReport(String format,HttpServletRequest request) throws FileNotFoundException, JRException {
        jwtAuthenticationFilter.getTheUserFromRequest(request);
        List<PDFAuctionResponseDTO> listAuctions = new ArrayList<>();
        for(Auction auction :auctionRepository.findAll()){
            PDFAuctionResponseDTO pdfAuctionResponseDTO = mapFromAuctionToPDFAuctionResponseDTO(auction);
            listAuctions.add(pdfAuctionResponseDTO);
        }

        String path = "C://Users//ASUS//Desktop//PROGRAMACION//JAVA//SpringBoot//";
        File file = ResourceUtils.getFile("classpath:auction.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listAuctions);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("gain java", "knowledge");
        parameters.put("aditionalInformation", "hola");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, ds);

        if(format.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "Auctions.html");
        }
        if(format.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, path + "Auctions.pdf");
        }
        return "Path: " + path;
    }

    @Override
    public String exportBidsReport(String format, Long auctionId, HttpServletRequest request) throws FileNotFoundException, JRException {
        User user = jwtAuthenticationFilter.getTheUserFromRequest(request);
        Role role = roleRepository.findByName("ROLE_ADMIN").orElseThrow(() -> new ResourceNotFoundException("Role","Name","ROLE_ADMIN"));
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new ResourceNotFoundException("Auction","AuctionId",String.valueOf(auctionId)));

        if(!(user.getRoles().contains(role) || auction.getUser().getUsername().equals(user.getUsername()))) {
            throw new AuctionDoesNotBelongToUserException();
        }

        List<PDFBidResponseDTO> listBids = new ArrayList<>();
        for(Bid bid : bidRepository.findByAuctionId(auctionId)){
            PDFBidResponseDTO pdfBidResponseDTO = mapFromBidToPDFBidResponseDTO(bid);
            PDFBidResponseDTO.setAuctionId(auctionId);
            PDFBidResponseDTO.setProduct(auction.getProduct());
            PDFBidResponseDTO.setAuctioneerName(auction.getUser().getName());
            PDFBidResponseDTO.setAuctioneerEmail(auction.getUser().getEmail());
            listBids.add(pdfBidResponseDTO);
        }



        String path = "C://Users//ASUS//Desktop//PROGRAMACION//JAVA//SpringBoot//";
        File file = ResourceUtils.getFile("classpath:bid.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listBids);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("gain java", "knowledge");
        parameters.put("aditionalInformation", "hola");
        parameters.put("auctionId", PDFBidResponseDTO.getAuctionId());
        parameters.put("auctioneerName", PDFBidResponseDTO.getAuctioneerName());
        parameters.put("product", PDFBidResponseDTO.getProduct());
        parameters.put("auctioneerEmail", PDFBidResponseDTO.getAuctioneerEmail());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, ds);

        if(format.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, path +user.getUsername().toUpperCase()+"_AuctionID_#"+auctionId+"bids.html");
        }
        if(format.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, path + user.getUsername().toUpperCase()+"_AuctionID_"+auctionId+"bids.pdf");
        }
        return "Path: " + path;
    }


}
