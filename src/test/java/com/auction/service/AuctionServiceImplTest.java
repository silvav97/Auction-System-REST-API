package com.auction.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.when;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.auction.dto.AuctionDTO;
import com.auction.dto.AuctionResponseDTO;
import com.auction.dto.PaginatedAuctionResponseDTO;
import com.auction.entity.Auction;
import com.auction.entity.Role;
import com.auction.entity.User;
import com.auction.entity.WinnerBid;
import com.auction.exception.AuctionAlreadyEndedException;
import com.auction.exception.AuctionDoesNotBelongToUserException;
import com.auction.exception.ResourceNotFoundException;
import com.auction.exception.ThereWasNoWinnerException;
import com.auction.repository.AuctionRepository;
import com.auction.repository.RoleRepository;
import com.auction.repository.UserRepository;
import com.auction.repository.WinnerBidRepository;
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
class AuctionServiceImplTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private AuctionRepository auctionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private WinnerBidRepository winnerBidRepository;

    @InjectMocks
    private AuctionServiceImpl auctionService;

    private Role roleAdmin;
    private Role roleUser;
    private User user;
    private Auction auction;
    private MockHttpServletRequest request;

    @BeforeEach
    void setup() {
        roleAdmin = new Role(1L,"ROLE_ADMIN");
        roleUser = new Role(2L,"ROLE_USER");
        user = new User(1L,"sebasAddress","123456789","sebasCity","123456789","sebas@gmail.com","sebas",passwordEncoder.encode("sebasPassword"),"sebas",2000000F, Collections.singleton(roleUser));
        auction = new Auction(1L, "PS4",  "play station 4",    1250000F,  true, 1590000F,   2L, user);
        request = new MockHttpServletRequest();
    }


    @Test
    void createAuctionTest() {
        // Given
        AuctionDTO auctionDTO = new AuctionDTO(auction.getProduct(),auction.getDescription(),auction.getInitialValue());

        // When
        when(jwtAuthenticationFilter.getTheUserFromRequest(request)).thenReturn(user);
        when(auctionRepository.save(any(Auction.class))).thenReturn(auction);

        // Then
        Auction savedAuction = auctionService.createAuction(auctionDTO, request);
        assertThat(savedAuction).isEqualTo(auction);
    }

    @Nested
    class endAuctionRelatedMethods {

        @Test
        void endAuctionTest() {
            // Given
            WinnerBid winnerBid = new WinnerBid(1L, auction, user);

            // When
            when(jwtAuthenticationFilter.getTheUserFromRequest(request)).thenReturn(user);
            when(auctionRepository.findById(anyLong())).thenReturn(Optional.of(auction));
            when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
            when(winnerBidRepository.save(any(WinnerBid.class))).thenReturn(winnerBid);

            // Then
            WinnerBid savedWinnerBid = auctionService.endAuction(1L,request);
            assertThat(savedWinnerBid).isEqualTo(winnerBid);
        }

        @Test
        void endUnexistingAuctionTest() {
            // Given

            // When
            when(jwtAuthenticationFilter.getTheUserFromRequest(request)).thenReturn(user);
            when(auctionRepository.findById(anyLong())).thenReturn(Optional.empty());

            // Then
            assertThrows(ResourceNotFoundException.class, () -> auctionService.endAuction(1L,request));
            verify(auctionRepository,never()).save(any(Auction.class));
        }

        @Test
        void endAuctionNotBeingTheOwnerTest() {
            // Given
            User user2 = new User(2L,"juanAddress","123456789","juanCity","123456789","juan@gmail.com","juan",passwordEncoder.encode("juanPassword"),"juan",2000000F, Collections.singleton(roleUser));

            // When
            when(jwtAuthenticationFilter.getTheUserFromRequest(request)).thenReturn(user2);
            when(auctionRepository.findById(anyLong())).thenReturn(Optional.of(auction));

            // Then
            assertThrows(AuctionDoesNotBelongToUserException.class, () -> auctionService.endAuction(1L,request));
            verify(auctionRepository,never()).save(any(Auction.class));
        }

        @Test
        void endAuctionTheAuctionIsAlreadyNonActiveTest() {
            // Given
            Auction NonActiveAuction = new Auction(1L, "PS4",  "play station 4",    1250000F,  false, 1590000F,   2L, user);

            // When
            when(jwtAuthenticationFilter.getTheUserFromRequest(request)).thenReturn(user);
            when(auctionRepository.findById(anyLong())).thenReturn(Optional.of(NonActiveAuction));

            // Then
            assertThrows(AuctionAlreadyEndedException.class, () -> auctionService.endAuction(1L,request));
            verify(auctionRepository,never()).save(any(Auction.class));
        }

        @Test
        void endAuctionWhenThereWasNotAnyBidderTest() {
            // Given
            Auction auction2 = new Auction(1L, "PS4",  "play station 4",    1250000F,  true, null,   null, user);

            // When
            when(jwtAuthenticationFilter.getTheUserFromRequest(request)).thenReturn(user);
            when(auctionRepository.findById(anyLong())).thenReturn(Optional.of(auction2));

            // Then
            assertThrows(ThereWasNoWinnerException.class, () -> auctionService.endAuction(1L,request));
        }

        @Test
        void endAuctionWhenUserWinnerNotFoundTest() {
            // Given

            // When
            when(jwtAuthenticationFilter.getTheUserFromRequest(request)).thenReturn(user);
            when(auctionRepository.findById(anyLong())).thenReturn(Optional.of(auction));
            when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

            // Then
            assertThrows(ResourceNotFoundException.class, () -> auctionService.endAuction(1L,request));
        }

    }

    @Nested
    class getAllMyAuctionsRelated {

        @Test
        void getAllMyAuctionsTest() {
            // Given
            Auction auction2 = new Auction(2L, "PS5",  "play station 5",    1250000F,  false, 1590000F,   3L, user);

            // When
            when(jwtAuthenticationFilter.getTheUserFromRequest(request)).thenReturn(user);
            when(auctionRepository.findByUser(any(User.class))).thenReturn(List.of(auction,auction2));

            // Then
            List<AuctionResponseDTO> auctions = auctionService.getAllMyAuctions(request);
            assertThat(auctions.size()).isEqualTo(2);
        }

        @Test
        void getAllMyAuctionsWithPaginationAndSortingASCTest() {
            // Given
            Auction auction2 = new Auction(2L, "PS5",  "play station 5",    1250000F,  false, 1590000F,   3L, user);
            Page<Auction> pageOfAuctions = new PageImpl<>(List.of(auction,auction2), PageRequest.of(10, 10), List.of(auction).size());

            // When
            when(jwtAuthenticationFilter.getTheUserFromRequest(request)).thenReturn(user);
            when(auctionRepository.findAllByUser(any(User.class),any(Pageable.class))).thenReturn(pageOfAuctions);

            // Then
            PaginatedAuctionResponseDTO auctions = auctionService.getAllMyAuctionsWithPaginationAndSorting(1,1,"id","ASC",request);
            assertThat(auctions.getContent().size()).isEqualTo(2);
        }

        @Test
        void getAllMyAuctionsWithPaginationAndSortingDESCTest() {
            // Given
            Auction auction2 = new Auction(2L, "PS5",  "play station 5",    1250000F,  false, 1590000F,   3L, user);
            Page<Auction> pageOfAuctions = new PageImpl<>(List.of(auction,auction2), PageRequest.of(10, 10), List.of(auction).size());

            // When
            when(jwtAuthenticationFilter.getTheUserFromRequest(request)).thenReturn(user);
            when(auctionRepository.findAllByUser(any(User.class),any(Pageable.class))).thenReturn(pageOfAuctions);

            // Then
            PaginatedAuctionResponseDTO auctions = auctionService.getAllMyAuctionsWithPaginationAndSorting(1,1,"id","DESC",request);
            assertThat(auctions.getContent().size()).isEqualTo(2);
        }
    }

    @Nested
    class getAllAuctions {
        @Test
        void getAllAuctionsTest() {
            // Given
            Auction auction2 = new Auction(2L, "PS5",  "play station 5",    1250000F,  false, 1590000F,   3L, user);

            // When
            when(auctionRepository.findAll()).thenReturn(List.of(auction,auction2));

            // Then
            List<AuctionResponseDTO> auctions = auctionService.getAllAuctions(request);
            assertThat(auctions.size()).isEqualTo(2);

        }

        @Test
        void getAllAuctionsWithPaginationAndSortingASCTest() {
            // Given
            Auction auction2 = new Auction(2L, "PS5",  "play station 5",    1250000F,  false, 1590000F,   3L, user);
            Page<Auction> pageOfAuctions = new PageImpl<>(List.of(auction,auction2), PageRequest.of(10, 10), List.of(auction).size());

            // When
            when(auctionRepository.findAll(any(Pageable.class))).thenReturn(pageOfAuctions);

            // Then
            PaginatedAuctionResponseDTO auctions = auctionService.getAllAuctionsWithPaginationAndSorting(1,1,"id","ASC",request);
            assertThat(auctions.getContent().size()).isEqualTo(2);
        }

        @Test
        void getAllAuctionsWithPaginationAndSortingDESCTest() {
            // Given
            Auction auction2 = new Auction(2L, "PS5",  "play station 5",    1250000F,  false, 1590000F,   3L, user);
            Page<Auction> pageOfAuctions = new PageImpl<>(List.of(auction,auction2), PageRequest.of(10, 10), List.of(auction).size());

            // When
            when(auctionRepository.findAll(any(Pageable.class))).thenReturn(pageOfAuctions);

            // Then
            PaginatedAuctionResponseDTO auctions = auctionService.getAllAuctionsWithPaginationAndSorting(1,1,"id","DESC",request);
            assertThat(auctions.getContent().size()).isEqualTo(2);
        }

    }

    @Nested
    class getAllAuctionsByUserRelated {
        @Test
        void getAllAuctionsByUserTest() {
            // Given
            Auction auction2 = new Auction(2L, "PS5",  "play station 5",    1250000F,  false, 1590000F,   3L, user);

            // When
            when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
            when(auctionRepository.findByUser(any(User.class))).thenReturn(List.of(auction,auction2));

            // Then
            List<AuctionResponseDTO> auctions = auctionService.getAllAuctionsByUser(1L,request);
            assertThat(auctions.size()).isEqualTo(2);

        }

        @Test
        void getAllAuctionsByUserWithUnExistingUserIDTest() {
            // Given

            // When
            when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

            // Then
            assertThrows(ResourceNotFoundException.class, () -> auctionService.getAllAuctionsByUser(1L,request));
        }

        @Test
        void getAllAuctionsByUserWithPaginationAndSortingASCTest() {
            // Given
            Auction auction2 = new Auction(2L, "PS5",  "play station 5",    1250000F,  false, 1590000F,   3L, user);
            Page<Auction> pageOfAuctions = new PageImpl<>(List.of(auction,auction2), PageRequest.of(10, 10), List.of(auction).size());

            // When
            when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
            when(auctionRepository.findAllByUser(any(User.class),any(Pageable.class))).thenReturn(pageOfAuctions);

            // Then
            PaginatedAuctionResponseDTO auctions = auctionService.getAllAuctionsByUserWithPaginationAndSorting(1,1,"id","ASC",1L,request);
            assertThat(auctions.getContent().size()).isEqualTo(2);
        }

        @Test
        void getAllAuctionsByUserWithPaginationAndSortingDESCTest() {
            // Given
            Auction auction2 = new Auction(2L, "PS5",  "play station 5",    1250000F,  false, 1590000F,   3L, user);
            Page<Auction> pageOfAuctions = new PageImpl<>(List.of(auction,auction2), PageRequest.of(10, 10), List.of(auction).size());

            // When
            when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
            when(auctionRepository.findAllByUser(any(User.class),any(Pageable.class))).thenReturn(pageOfAuctions);

            // Then
            PaginatedAuctionResponseDTO auctions = auctionService.getAllAuctionsByUserWithPaginationAndSorting(1,1,"id","DESC",1L,request);
            assertThat(auctions.getContent().size()).isEqualTo(2);
        }

        @Test
        void getAllAuctionsByUserWithPaginationAndSortingWithUnexistingUserIdTest() {
            // Given

            // When
            when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

            // Then
            assertThrows(ResourceNotFoundException.class, () -> auctionService.getAllAuctionsByUserWithPaginationAndSorting(1,1,"id","DESC",1L,request));
        }
    }

    @Nested
    class getAllActiveAuctions {

        @Test
        void getAllActiveAuctionsTest() {
            // Given
            Auction auction2 = new Auction(2L, "PS5",  "play station 5",    1250000F,  true, 1590000F,   3L, user);

            // When
            when(auctionRepository.findByActiveTrue()).thenReturn(List.of(auction,auction2));

            // Then
            List<AuctionResponseDTO> auctions = auctionService.getAllActiveAuctions(request);
            assertThat(auctions.size()).isEqualTo(2);
        }

        @Test
        void getAllActiveAuctionsWithPaginationAndSortingASCTest() {
            // Given
            Auction auction2 = new Auction(2L, "PS5",  "play station 5",    1250000F,  false, 1590000F,   3L, user);
            Page<Auction> pageOfAuctions = new PageImpl<>( List.of(auction,auction2), PageRequest.of(10, 10),  List.of(auction,auction2).size());

            // When
            when(auctionRepository.findAllByActiveTrue(any(Pageable.class))).thenReturn(pageOfAuctions);

            // Then
            PaginatedAuctionResponseDTO auctions = auctionService.getAllActiveAuctionsWithPaginationAndSorting(1,1,"id","ASC",request);
            assertThat(auctions.getContent().size()).isEqualTo(2);
        }

        @Test
        void getAllActiveAuctionsWithPaginationAndSortingDESCTest() {
            // Given
            Auction auction2 = new Auction(2L, "PS5",  "play station 5",    1250000F,  false, 1590000F,   3L, user);
            Page<Auction> pageOfAuctions = new PageImpl<>( List.of(auction,auction2), PageRequest.of(10, 10),  List.of(auction,auction2).size());

            // When
            when(auctionRepository.findAllByActiveTrue(any(Pageable.class))).thenReturn(pageOfAuctions);

            // Then
            PaginatedAuctionResponseDTO auctions = auctionService.getAllActiveAuctionsWithPaginationAndSorting(1,1,"id","DESC",request);
            assertThat(auctions.getContent().size()).isEqualTo(2);
        }
    }

    @Nested
    class getAuctionByAuctionIdRelated {

        @Test
        void getAuctionByAuctionIdBeingAdminButNotAuctionsOwnerTest() {
            // Given
            User user2 = new User(2L,"alanAddress","123456789","alanCity","123456789","alan@gmail.com","alan",passwordEncoder.encode("alanPassword"),"alan",2000000F, Collections.singleton(roleAdmin));

            // When
            when(jwtAuthenticationFilter.getTheUserFromRequest(request)).thenReturn(user2);
            when(roleRepository.findByName(anyString())).thenReturn(Optional.of(roleAdmin));
            when(auctionRepository.findById(anyLong())).thenReturn(Optional.of(auction));

            // Then
            AuctionResponseDTO obtainedAuction = auctionService.getAuctionByAuctionId(1L,request);
            assertThat(obtainedAuction.getAuctioneer()).isEqualTo(auction.getUser().getName());
        }

        @Test
        void getAuctionByAuctionIdBeingAuctionsOwnerButNotAdminTest() {
            // Given

            // When
            when(jwtAuthenticationFilter.getTheUserFromRequest(request)).thenReturn(user);
            when(roleRepository.findByName(anyString())).thenReturn(Optional.of(roleAdmin));
            when(auctionRepository.findById(anyLong())).thenReturn(Optional.of(auction));

            // Then
            AuctionResponseDTO obtainedAuction = auctionService.getAuctionByAuctionId(1L,request);
            assertThat(obtainedAuction.getAuctioneer()).isEqualTo(auction.getUser().getName());
        }

        @Test
        void getAuctionByAuctionIdWithUnexistingRoleNameTest() {
            // Given
            User user2 = new User(2L,"alanAddress","123456789","alanCity","123456789","alan@gmail.com","alan",passwordEncoder.encode("alanPassword"),"alan",2000000F, Collections.singleton(roleAdmin));

            // When
            when(jwtAuthenticationFilter.getTheUserFromRequest(request)).thenReturn(user2);
            when(roleRepository.findByName(anyString())).thenReturn(Optional.empty());

            // Then
            assertThrows(ResourceNotFoundException.class, () -> auctionService.getAuctionByAuctionId(1L,request));
        }

        @Test
        void getAuctionByAuctionIdWithUnexistingAuctionIDTest() {
            // Given
            User user2 = new User(2L,"alanAddress","123456789","alanCity","123456789","alan@gmail.com","alan",passwordEncoder.encode("alanPassword"),"alan",2000000F, Collections.singleton(roleAdmin));

            // When
            when(jwtAuthenticationFilter.getTheUserFromRequest(request)).thenReturn(user2);
            when(roleRepository.findByName(anyString())).thenReturn(Optional.of(roleAdmin));
            when(auctionRepository.findById(anyLong())).thenReturn(Optional.empty());

            // Then
            assertThrows(ResourceNotFoundException.class, () -> auctionService.getAuctionByAuctionId(1L,request));
        }

        @Test
        void getAuctionByAuctionIdBeingNeitherAdminNorAuctionsOwnerTest() {
            // Given
            User user2 = new User(2L,"alanAddress","123456789","alanCity","123456789","alan@gmail.com","alan",passwordEncoder.encode("alanPassword"),"alan",2000000F, Collections.singleton(roleUser));

            // When
            when(jwtAuthenticationFilter.getTheUserFromRequest(request)).thenReturn(user2);
            when(roleRepository.findByName(anyString())).thenReturn(Optional.of(roleAdmin));
            when(auctionRepository.findById(anyLong())).thenReturn(Optional.of(auction));

            // Then
            assertThrows(AuctionDoesNotBelongToUserException.class, () -> auctionService.getAuctionByAuctionId(1L,request));
        }
    }

}