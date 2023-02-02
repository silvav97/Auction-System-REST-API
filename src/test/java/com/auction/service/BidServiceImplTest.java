package com.auction.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.when;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.auction.dto.BidResponseDTO;
import com.auction.dto.PaginatedBidResponseDTO;
import com.auction.entity.Auction;
import com.auction.entity.Bid;
import com.auction.entity.Role;
import com.auction.entity.User;
import com.auction.exception.AuctionDoesNotBelongToUserException;
import com.auction.exception.BidDoesNotBelongToUserException;
import com.auction.exception.ResourceNotFoundException;
import com.auction.repository.*;
import com.auction.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class BidServiceImplTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private BidRepository bidRepository;

    @Mock
    private AuctionRepository auctionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private BidServiceImpl bidService;

    private Role roleAdmin, roleUser;
    private User user, user2, admin;
    private Auction auction;
    private Bid bid, bid2;
    private MockHttpServletRequest request;

    @BeforeEach
    void setup() {
        roleAdmin = new Role(1L,"ROLE_ADMIN");
        roleUser = new Role(2L,"ROLE_USER");
        user = new User(1L,"sebasAddress","123456789","sebasCity","123456789","sebas@gmail.com","sebas",passwordEncoder.encode("sebasPassword"),"sebas",2000000F, Collections.singleton(roleUser));
        user2 = new User(2L,"juanAddress","123456789","juanCity","123456789","juan@gmail.com","juan",passwordEncoder.encode("juanPassword"),"juan",2000000F, Collections.singleton(roleUser));
        admin = new User(3L,"adminAddress","123456789","adminCity","123456789","admin@gmail.com","admin",passwordEncoder.encode("adminPassword"),"admin",2000000F, Collections.singleton(roleAdmin));
        auction = new Auction(1L, "PS4",  "play station 4",    1250000F,  true, 1590000F,   2L, user);
        bid = new Bid(1L, user2, 1610500F,auction);
        bid2 = new Bid(1L, user2, 1620500F,auction);

        request = new MockHttpServletRequest();
    }


    @Nested
    class getAllBidsRelated {

        @Test
        void getAllBidsTest() {
            // Given

            // When
            when(bidRepository.findAll()).thenReturn(List.of(bid,bid2));

            // Then
            List<BidResponseDTO> bids = bidService.getAllBids(request);
            assertThat(bids.size()).isEqualTo(2);

        }

        @Test
        void getAllBidsWithPaginationAndSortingASCTest() {
            // Given
            Bid bid2 = new Bid(1L, user2, 1620500F,auction);
            Page<Bid> pageOfBids = new PageImpl<>(List.of(bid,bid2), PageRequest.of(10,10), List.of(bid).size());

            // When
            when(bidRepository.findAll(any(Pageable.class))).thenReturn(pageOfBids);

            // Then
            PaginatedBidResponseDTO bids = bidService.getAllBidsWithPaginationAndSorting(1,1,"id","ASC",request);
            assertThat(bids.getContent().size()).isEqualTo(2);
        }

        @Test
        void getAllBidsWithPaginationAndSortingDESCTest() {
            // Given
            Page<Bid> pageOfBids = new PageImpl<>(List.of(bid,bid2), PageRequest.of(10,10), List.of(bid).size());

            // When
            when(bidRepository.findAll(any(Pageable.class))).thenReturn(pageOfBids);

            // Then
            PaginatedBidResponseDTO bids = bidService.getAllBidsWithPaginationAndSorting(1,1,"id","DESC",request);
            assertThat(bids.getContent().size()).isEqualTo(2);
        }
    }

    @Nested
    class getAllBidsByUserRelated {

        @Test
        void getAllBidsByUserTest() {
            // Given

            // When
            when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
            when(bidRepository.findByUser(any(User.class))).thenReturn(List.of(bid, bid2));

            // Then
            List<BidResponseDTO> bids = bidService.getAllBidsByUser(anyLong(), request);
            assertThat(bids.size()).isEqualTo(2);
        }

        @Test
        void getAllBidsByUserWithUnExistingUserIDTest() {
            // Given

            // When
            when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

            // Then
            assertThrows(ResourceNotFoundException.class, () -> bidService.getAllBidsByUser(anyLong(), request));
        }

        @Test
        void getAllBidsByUserWithPaginationAndSortingASCTest() {
            // Given
            Page<Bid> pageOfBids = new PageImpl<>(List.of(bid, bid2), PageRequest.of(10, 10), List.of(bid).size());

            // When
            when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
            when(bidRepository.findAllByUser(any(User.class), any(Pageable.class))).thenReturn(pageOfBids);

            // Then
            PaginatedBidResponseDTO bids = bidService.getAllBidsByUserWithPaginationAndSorting(1, 1, "id", "ASC", anyLong(), request);
            assertThat(bids.getContent().size()).isEqualTo(2);
        }

        @Test
        void getAllBidsByUserWithPaginationAndSortingDESCTest() {
            // Given
            Page<Bid> pageOfBids = new PageImpl<>(List.of(bid, bid2), PageRequest.of(10, 10), List.of(bid).size());

            // When
            when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
            when(bidRepository.findAllByUser(any(User.class), any(Pageable.class))).thenReturn(pageOfBids);

            // Then
            PaginatedBidResponseDTO bids = bidService.getAllBidsByUserWithPaginationAndSorting(1, 1, "id", "DESC", anyLong(), request);
            assertThat(bids.getContent().size()).isEqualTo(2);
        }

        @Test
        void getAllBidsByUserWithPaginationAndSortingWithUnexistingUserIdTest() {
            // Given

            // When
            when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

            // Then
            assertThrows(ResourceNotFoundException.class, () -> bidService.getAllBidsByUserWithPaginationAndSorting(1, 1, "id", "DESC", 1L, request));
        }

    }

    @Nested
    class getAllBidsFromAuctionRelatedMethods {

        @Test
        void getAllBidsFromAuctionBeingAdminButNotAuctionsOwnerTest() {
            // Given

            // When
            when(jwtAuthenticationFilter.getTheUserFromRequest(request)).thenReturn(admin);
            when(roleRepository.findByName(anyString())).thenReturn(Optional.of(roleAdmin));
            when(auctionRepository.findById(anyLong())).thenReturn(Optional.of(auction));
            when(bidRepository.findByAuctionId(anyLong())).thenReturn(List.of(bid,bid2));

            // Then
            List<BidResponseDTO> listBids = bidService.getAllBidsFromAuction(1L, request);
            assertThat(listBids.size()).isEqualTo(2);
        }

        @Test
        void getAllBidsFromAuctionBeingAuctionsOwnerButNotAdminTest() {
            // Given

            // When
            when(jwtAuthenticationFilter.getTheUserFromRequest(request)).thenReturn(user);
            when(roleRepository.findByName(anyString())).thenReturn(Optional.of(roleAdmin));
            when(auctionRepository.findById(anyLong())).thenReturn(Optional.of(auction));
            when(bidRepository.findByAuctionId(anyLong())).thenReturn(List.of(bid,bid2));

            // Then
            List<BidResponseDTO> listBids = bidService.getAllBidsFromAuction(1L, request);
            assertThat(listBids.size()).isEqualTo(2);
            assertThat(auction.getUser().getName()).isEqualTo(user.getName());
        }

        @Test
        void getAllBidsFromAuctionWithUnexistingRoleNameTest() {
            // Given

            // When
            when(jwtAuthenticationFilter.getTheUserFromRequest(request)).thenReturn(user);
            when(roleRepository.findByName(anyString())).thenReturn(Optional.empty());

            // Then
            assertThrows(ResourceNotFoundException.class, () -> bidService.getAllBidsFromAuction(1L, request));
        }

        @Test
        void getAllBidsFromAuctionWithUnexistingAuctionIDTest() {
            // Given

            // When
            when(jwtAuthenticationFilter.getTheUserFromRequest(request)).thenReturn(user);
            when(roleRepository.findByName(anyString())).thenReturn(Optional.of(roleAdmin));
            when(auctionRepository.findById(anyLong())).thenReturn(Optional.empty());

            // Then
            assertThrows(ResourceNotFoundException.class, () -> bidService.getAllBidsFromAuction(1L, request));
        }

        @Test
        void getAllBidsFromAuctionBeingNeitherAdminNorAuctionsOwnerTest() {
            // Given

            // When
            when(jwtAuthenticationFilter.getTheUserFromRequest(request)).thenReturn(user2);
            when(roleRepository.findByName(anyString())).thenReturn(Optional.of(roleAdmin));
            when(auctionRepository.findById(anyLong())).thenReturn(Optional.of(auction));

            // Then
            assertThrows(AuctionDoesNotBelongToUserException.class, () -> bidService.getAllBidsFromAuction(1L, request));
        }
    }

    @Nested
    class getAllBidsFromAuctionWithPaginationAndSortingRelatedMethods {

        @Test
        void getAllBidsFromAuctionWithPaginationAndSortingBeingAdminButNotAuctionsOwnerTest() {
            // Given
            Page<Bid> pageOfBids = new PageImpl<>(List.of(bid, bid2), PageRequest.of(10, 10), List.of(bid).size());

            // When
            when(jwtAuthenticationFilter.getTheUserFromRequest(request)).thenReturn(admin);
            when(roleRepository.findByName(anyString())).thenReturn(Optional.of(roleAdmin));
            when(auctionRepository.findById(anyLong())).thenReturn(Optional.of(auction));
            when(bidRepository.findAllByAuctionId(anyLong(), any(Pageable.class))).thenReturn(pageOfBids);

            // Then
            PaginatedBidResponseDTO bids = bidService.getAllBidsFromAuctionWithPaginationAndSorting(1, 1, "id", "ASC", anyLong(), request);
            assertThat(bids.getContent().size()).isEqualTo(2);
        }

        @Test
        void getAllBidsFromAuctionWithPaginationAndSortingBeingAuctionsOwnerButNotAdminTest() {
            // Given
            Page<Bid> pageOfBids = new PageImpl<>(List.of(bid, bid2), PageRequest.of(10, 10), List.of(bid).size());

            // When
            when(jwtAuthenticationFilter.getTheUserFromRequest(request)).thenReturn(user);
            when(roleRepository.findByName(anyString())).thenReturn(Optional.of(roleAdmin));
            when(auctionRepository.findById(anyLong())).thenReturn(Optional.of(auction));
            when(bidRepository.findAllByAuctionId(anyLong(), any(Pageable.class))).thenReturn(pageOfBids);

            // Then
            PaginatedBidResponseDTO bids = bidService.getAllBidsFromAuctionWithPaginationAndSorting(1, 1, "id", "ASC", anyLong(), request);
            assertThat(bids.getContent().size()).isEqualTo(2);
            assertThat(auction.getUser().getName()).isEqualTo(user.getName());
        }

        @Test
        void getAllBidsFromAuctionWithPaginationAndSortingWithUnexistingRoleNameTest() {
            // Given

            // When
            when(jwtAuthenticationFilter.getTheUserFromRequest(request)).thenReturn(user);
            when(roleRepository.findByName(anyString())).thenReturn(Optional.empty());

            // Then
            assertThrows(ResourceNotFoundException.class, () -> bidService.getAllBidsFromAuctionWithPaginationAndSorting(1, 1, "id", "ASC", anyLong(), request));
        }

        @Test
        void getAllBidsFromAuctionWithPaginationAndSortingWithUnexistingAuctionIDTest() {
            // Given

            // When
            when(jwtAuthenticationFilter.getTheUserFromRequest(request)).thenReturn(user);
            when(roleRepository.findByName(anyString())).thenReturn(Optional.of(roleAdmin));
            when(auctionRepository.findById(anyLong())).thenReturn(Optional.empty());

            // Then
            assertThrows(ResourceNotFoundException.class, () -> bidService.getAllBidsFromAuctionWithPaginationAndSorting(1, 1, "id", "ASC", anyLong(), request));
        }

        @Test
        void getAllBidsFromAuctionWithPaginationAndSortingBeingNeitherAdminNorAuctionsOwnerTest() {
            // Given

            // When
            when(jwtAuthenticationFilter.getTheUserFromRequest(request)).thenReturn(user2);
            when(roleRepository.findByName(anyString())).thenReturn(Optional.of(roleAdmin));
            when(auctionRepository.findById(anyLong())).thenReturn(Optional.of(auction));

            // Then
            assertThrows(AuctionDoesNotBelongToUserException.class, () -> bidService.getAllBidsFromAuctionWithPaginationAndSorting(1, 1, "id", "ASC", anyLong(), request));
        }
    }

    @Nested
    class getBidByBidId {

        @Test
        void getBidByBidIdBeingAdminButNotBidsOwnerTest() {
            // Given

            // When
            when(jwtAuthenticationFilter.getTheUserFromRequest(request)).thenReturn(admin);
            when(roleRepository.findByName(anyString())).thenReturn(Optional.of(roleAdmin));
            when(bidRepository.findById(anyLong())).thenReturn(Optional.of(bid));

            // Then
            BidResponseDTO obtainedBid = bidService.getBidByBidId(1L, request);
            assertThat(obtainedBid.getBidderName()).isEqualTo(bid.getUser().getName());
        }

        @Test
        void getBidByBidIdBeingBidsOwnerButNotAdminTest() {
            // Given

            // When
            when(jwtAuthenticationFilter.getTheUserFromRequest(request)).thenReturn(user2);
            when(roleRepository.findByName(anyString())).thenReturn(Optional.of(roleAdmin));
            when(bidRepository.findById(anyLong())).thenReturn(Optional.of(bid));

            // Then
            BidResponseDTO obtainedBid = bidService.getBidByBidId(1L, request);
            assertThat(obtainedBid.getBidderName()).isEqualTo(bid.getUser().getName());
        }

        @Test
        void getBidByBidIdWithUnexistingRoleNameTest() {
            // Given

            // When
            when(jwtAuthenticationFilter.getTheUserFromRequest(request)).thenReturn(user);
            when(roleRepository.findByName(anyString())).thenReturn(Optional.empty());

            // Then
            assertThrows(ResourceNotFoundException.class, () -> bidService.getBidByBidId(1L, request));
        }

        @Test
        void getBidByBidIdWithUnexistingBidIDTest() {
            // Given

            // When
            when(jwtAuthenticationFilter.getTheUserFromRequest(request)).thenReturn(user);
            when(roleRepository.findByName(anyString())).thenReturn(Optional.of(roleAdmin));
            when(bidRepository.findById(anyLong())).thenReturn(Optional.empty());

            // Then
            assertThrows(ResourceNotFoundException.class, () -> bidService.getBidByBidId(1L, request));
        }

        @Test
        void getBidByBidIdBeingNeitherAdminNorBidsOwnerTest() {
            // Given

            // When
            when(jwtAuthenticationFilter.getTheUserFromRequest(request)).thenReturn(user);
            when(roleRepository.findByName(anyString())).thenReturn(Optional.of(roleAdmin));
            when(bidRepository.findById(anyLong())).thenReturn(Optional.of(bid));

            // Then
            assertThrows(BidDoesNotBelongToUserException.class, () -> bidService.getBidByBidId(1L, request));
        }
    }

    @Nested
    class getAllMyBidsRelatedMethods {

        @Test
        void getAllMyBidsTest() {
            // Given

            // When
            when(jwtAuthenticationFilter.getTheUserFromRequest(request)).thenReturn(user2);
            when(bidRepository.findByUser(any(User.class))).thenReturn(List.of(bid,bid2));

            // Then
            List<BidResponseDTO> bids = bidService.getAllMyBids(request);
            assertThat(bids.size()).isEqualTo(2);
        }

        @Test
        void getAllMyBidsWithPaginationAndSortingTest() {
            // Given
            Page<Bid> pageOfBids = new PageImpl<>(List.of(bid, bid2), PageRequest.of(10, 10), List.of(bid).size());

            // When
            when(jwtAuthenticationFilter.getTheUserFromRequest(request)).thenReturn(user);
            when(bidRepository.findAllByUser(any(User.class), any(Pageable.class))).thenReturn(pageOfBids);

            // Then
            PaginatedBidResponseDTO bids = bidService.getAllMyBidsWithPaginationAndSorting(1, 1, "id", "ASC", request);
            assertThat(bids.getContent().size()).isEqualTo(2);
            assertThat(auction.getUser().getName()).isEqualTo(user.getName());
        }
    }




}