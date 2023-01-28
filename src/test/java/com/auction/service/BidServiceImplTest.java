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

    private Role roleAdmin;
    private Role roleUser;
    private User user, user2;
    private Auction auction;
    private Bid bid;
    private MockHttpServletRequest request;

    @BeforeEach
    void setup() {
        roleAdmin = new Role(1L,"ROLE_ADMIN");
        roleUser = new Role(2L,"ROLE_USER");
        user = new User(1L,"sebasAddress","123456789","sebasCity","123456789","sebas@gmail.com","sebas",passwordEncoder.encode("sebasPassword"),"sebas",2000000F, Collections.singleton(roleUser));
        user2 = new User(2L,"juanAddress","123456789","juanCity","123456789","juan@gmail.com","juan",passwordEncoder.encode("juanPassword"),"juan",2000000F, Collections.singleton(roleUser));
        auction = new Auction(1L, "PS4",  "play station 4",    1250000F,  true, 1590000F,   2L, user);
        bid = new Bid(1L, user2, 1610500F,auction);
        request = new MockHttpServletRequest();
    }


    @Nested
    class getAllBidsRelated {

        @Test
        void getAllBidsTest() {
            // Given
            Bid bid2 = new Bid(1L, user2, 1620500F,auction);

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
            Bid bid2 = new Bid(1L, user2, 1620500F,auction);
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
            Bid bid2 = new Bid(1L, user2, 1620500F,auction);

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
            assertThrows(ResourceNotFoundException.class, () -> bidService.getAllBidsByUser(anyLong(),request));
        }

        @Test
        void getAllBidsByUserWithPaginationAndSortingASCTest() {
            // Given
            Bid bid2 = new Bid(1L, user2, 1620500F,auction);
            Page<Bid> pageOfBids = new PageImpl<>(List.of(bid,bid2), PageRequest.of(10,10), List.of(bid).size());

            // When
            when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
            when(bidRepository.findAllByUser(any(User.class), any(Pageable.class))).thenReturn(pageOfBids);

            // Then
            PaginatedBidResponseDTO bids = bidService.getAllBidsByUserWithPaginationAndSorting(1,1,"id","ASC",anyLong(), request);
            assertThat(bids.getContent().size()).isEqualTo(2);
        }
    }




}