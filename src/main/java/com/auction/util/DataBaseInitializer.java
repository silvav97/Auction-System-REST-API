package com.auction.util;

import com.auction.entity.*;
import com.auction.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;


@Configuration
public class DataBaseInitializer {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private WinnerBidRepository winnerBidRepository;


    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {

            registerRoles();
            registerUsers();
            registerAuctions();
            registerBids();
            registerWinnerBids();

        };
    }


    private void registerRoles() {
        Role role1 = new Role(1L,"ROLE_ADMIN");
        Role role2 = new Role(2L,"ROLE_USER");
        roleRepository.saveAll(List.of(role1,role2));
    }

    private void registerUsers() {
        Role roleAdmin = roleRepository.findByName("ROLE_ADMIN").get();
        Role roleUser = roleRepository.findByName("ROLE_USER").get();

        // ADMINS2
        //                   (id,   address,      cellPhone,  city,    documentNumber,  email,      name,           password,                   username, credit,  roles)
        User user1 = new User(1L,"adminAddress","123456789","adminCity","123456789","admin@gmail.com","admin",passwordEncoder.encode("adminPassword"),"admin",2000000F,Collections.singleton(roleAdmin));
        User user2 = new User(2L,"admin2Address","123456789","admin2City","123456789","admin2@gmail.com","admin2",passwordEncoder.encode("admin2Password"),"admin2",2000000F,Collections.singleton(roleAdmin));

        // USERS
        User user3 = new User(3L,"carlosAddress","123456789","carlosCity","123456789","carlos@gmail.com","carlos",passwordEncoder.encode("carlosPassword"),"carlos",2000000F,Collections.singleton(roleUser));
        User user4 = new User(4L,"dianaAddress","123456789","dianaCity","123456789","diana@gmail.com","diana",passwordEncoder.encode("dianaPassword"),"diana",2000000F,Collections.singleton(roleUser));
        User user5 = new User(5L,"sebasAddress","123456789","sebasCity","123456789","sebas@gmail.com","sebas",passwordEncoder.encode("sebasPassword"),"sebas",2000000F,Collections.singleton(roleUser));
        User user6 = new User(6L,"jorgeAddress","123456789","jorgeCity","123456789","jorge@gmail.com","jorge",passwordEncoder.encode("jorgePassword"),"jorge",2000000F,Collections.singleton(roleUser));
        User user7 = new User(7L,"juanAddress","123456789","juanCity","123456789","juan@gmail.com","juan",passwordEncoder.encode("juanPassword"),"juan",2000000F,Collections.singleton(roleUser));
        User user8 = new User(8L,"luisAddress","123456789","luisCity","123456789","luis@gmail.com","luis",passwordEncoder.encode("luisPassword"),"luis",2000000F,Collections.singleton(roleUser));
        User user9 = new User(9L,"lenaAddress","123456789","lenaCity","123456789","lena@gmail.com","lena",passwordEncoder.encode("lenaPassword"),"lena",2000000F,Collections.singleton(roleUser));
        User user10 = new User(10L,"alexAddress","123456789","alexCity","123456789","alex@gmail.com","alex",passwordEncoder.encode("alexPassword"),"alex",2000000F,Collections.singleton(roleUser));
        User user11 = new User(11L,"ivanAddress","123456789","ivanCity","123456789","ivan@gmail.com","ivan",passwordEncoder.encode("ivanPassword"),"ivan",2000000F,Collections.singleton(roleUser));
        User user12 = new User(12L,"anaAddress","123456789","anaCity","123456789","ana@gmail.com","ana",passwordEncoder.encode("anaPassword"),"ana",2000000F,Collections.singleton(roleUser));
        userRepository.saveAll(List.of(user1,user2,user3,user4,user5,user6,user7,user8,user9,user10,user11,user12));
    }

    private void registerAuctions() {
        //                                  (id,  product,  description, initVal,    active, highBid,highBidderId,  user)
        Auction auctionCarlos1 = new Auction(1L, "carro1", "description", 45000F,      true,  60000F,   8L,  userRepository.findById(3L).get()); // has bids
        Auction auctionCarlos2 = new Auction(2L, "carro2", "description", 45000F,      true,  60000F,   8L,  userRepository.findById(3L).get()); // has bids
        Auction auctionCarlos3 = new Auction(3L, "carro3", "description", 26000F,      true,  null,   null,  userRepository.findById(3L).get());
        Auction auctionDiana1 = new Auction(4L, "juguete1", "description", 5500F,      true,  41000F,   9L,  userRepository.findById(4L).get()); // has bids
        Auction auctionDiana2 = new Auction(5L, "juguete2", "description", 2300F,      true,  null,   null,  userRepository.findById(4L).get());
        Auction auctionDiana3 = new Auction(6L, "juguete3", "description", 13000F,     true,  null,   null,  userRepository.findById(4L).get());

        Auction Sebas1 = new Auction(7L, "PS4",  "play station 4",    1250000F,  false, 1590000F,   3L,  userRepository.findById(5L).get());
        Auction Sebas2 = new Auction(8L, "televisor", "new Plasma Tv", 980000F,  false, 1051000F,   3L,  userRepository.findById(5L).get());
        Auction Sebas3 = new Auction(9L, "computer", "new Computer",   980000F,  false, 1051000F,   3L,  userRepository.findById(5L).get());
        Auction Sebas4 = new Auction(10L, "tv", "new Extra Plasma Tv", 980000F,  false, 1051000F,   3L,  userRepository.findById(5L).get());
        Auction Sebas5 = new Auction(11L, "tv", "new Extra Plasma Tv", 980000F,  false, 1051000F,   4L,  userRepository.findById(5L).get());
        Auction Sebas6 = new Auction(12L, "tv", "new Extra Plasma Tv", 980000F,  false, 1051000F,   4L,  userRepository.findById(5L).get());
        Auction Sebas7 = new Auction(13L, "tv", "new Extra Plasma Tv", 980000F,  false, 1051000F,   12L,  userRepository.findById(5L).get());
        Auction Sebas8 = new Auction(14L, "tv", "new Extra Plasma Tv", 980000F,  false, 1051000F,   10L,  userRepository.findById(5L).get());
        Auction Sebas9 = new Auction(15L, "tv", "new Extra Plasma Tv", 980000F,  false, 1051000F,   11L,  userRepository.findById(5L).get());
        Auction Sebas10 = new Auction(16L, "tv", "new Extra Plasma Tv", 980000F, false, 1051000F,   12L,  userRepository.findById(5L).get());
        Auction Sebas11 = new Auction(17L, "tv", "new Extra Plasma Tv", 980000F, true,  null,   null,  userRepository.findById(5L).get());
        Auction Sebas12 = new Auction(18L, "tv", "new Extra Plasma Tv", 980000F, true,  null,   null,  userRepository.findById(5L).get());
        Auction Sebas13 = new Auction(19L, "tv", "new Extra Plasma Tv", 980000F, true,  null,   null,  userRepository.findById(5L).get());
        Auction Sebas14 = new Auction(20L, "tv", "new Extra Plasma Tv", 980000F, true,  null,   null,  userRepository.findById(5L).get());
        Auction Sebas15 = new Auction(21L, "tv", "new Extra Plasma Tv", 980000F, true,  null,   null,  userRepository.findById(5L).get());
        Auction Sebas16 = new Auction(22L, "tv", "new Extra Plasma Tv", 980000F, true,  null,   null,  userRepository.findById(5L).get());
        Auction Sebas17 = new Auction(23L, "tv", "new Extra Plasma Tv", 980000F, true,  null,   null,  userRepository.findById(5L).get());
        Auction Sebas18 = new Auction(24L, "tv", "new Extra Plasma Tv", 980000F, true,  null,   null,  userRepository.findById(5L).get());
        Auction Sebas19 = new Auction(25L, "tv", "new Extra Plasma Tv", 980000F, true,  null,   null,  userRepository.findById(5L).get());
        Auction Sebas20 = new Auction(26L, "tv", "new Extra Plasma Tv", 980000F, true,  null,   null,  userRepository.findById(5L).get());
        Auction Sebas21 = new Auction(27L, "tv", "new Extra Plasma Tv", 980000F, true,  null,   null,  userRepository.findById(5L).get());
        Auction Sebas22 = new Auction(28L, "tv", "new Extra Plasma Tv", 980000F, true,  null,   null,  userRepository.findById(5L).get());
        Auction Sebas23 = new Auction(29L, "tv", "new Extra Plasma Tv", 980000F, true,  null,   null,  userRepository.findById(5L).get());
        Auction Sebas24 = new Auction(30L, "tv", "new Extra Plasma Tv", 980000F, true,  null,   null,  userRepository.findById(5L).get());
        Auction Sebas25 = new Auction(31L, "tv", "new Extra Plasma Tv", 980000F, true,  null,   null,  userRepository.findById(5L).get());
        Auction Sebas26 = new Auction(32L, "tv", "new Extra Plasma Tv", 980000F, true,  null,   null,  userRepository.findById(5L).get());
        Auction Sebas27 = new Auction(33L, "tv", "new Extra Plasma Tv", 980000F, true,  null,   null,  userRepository.findById(5L).get());
        Auction Sebas28 = new Auction(34L, "tv", "new Extra Plasma Tv", 980000F, true,  null,   null,  userRepository.findById(5L).get());
        Auction Sebas29 = new Auction(35L, "tv", "new Extra Plasma Tv", 980000F, true,  null,   null,  userRepository.findById(5L).get());
        Auction Sebas30 = new Auction(36L, "tv", "new Extra Plasma Tv", 980000F, true,  null,   null,  userRepository.findById(5L).get());

        auctionRepository.saveAll(List.of(auctionCarlos1,auctionCarlos2,auctionCarlos3,auctionDiana1,auctionDiana2,auctionDiana3,
                Sebas1,Sebas2,Sebas3,Sebas4,Sebas5,Sebas6,Sebas7,Sebas8,Sebas9,Sebas10,Sebas11,Sebas12,Sebas13,Sebas14,Sebas15,
                Sebas16,Sebas17,Sebas18,Sebas19,Sebas20,Sebas21,Sebas22,Sebas23,Sebas24,Sebas25,Sebas26,Sebas27,Sebas28,Sebas29,Sebas30));
    }

    private void registerBids() {
        //                 (id,   user,                            bidAmount,  auction)
        // BID FOR AUCTION 1 WHOSE AUCTIONEER IS CARLOS WITH USERID : 3
        Bid bid1 = new Bid(1L, userRepository.findById(4L).get(),  46000F,     auctionRepository.findById(1L).get());
        Bid bid2 = new Bid(2L, userRepository.findById(5L).get(),  47000F,     auctionRepository.findById(1L).get());
        Bid bid3 = new Bid(3L, userRepository.findById(6L).get(),  48000F,     auctionRepository.findById(1L).get());
        Bid bid4 = new Bid(4L, userRepository.findById(7L).get(),  49000F,     auctionRepository.findById(1L).get());
        Bid bid5 = new Bid(5L, userRepository.findById(8L).get(),  50000F,     auctionRepository.findById(1L).get());
        Bid bid6 = new Bid(6L, userRepository.findById(4L).get(),  51000F,     auctionRepository.findById(1L).get());
        Bid bid7 = new Bid(7L, userRepository.findById(5L).get(),  52000F,     auctionRepository.findById(1L).get());
        Bid bid8 = new Bid(8L, userRepository.findById(6L).get(),  53000F,     auctionRepository.findById(1L).get());
        Bid bid9 = new Bid(9L, userRepository.findById(7L).get(),  54000F,     auctionRepository.findById(1L).get());
        Bid bid10 = new Bid(10L, userRepository.findById(8L).get(),  55000F,     auctionRepository.findById(1L).get());
        Bid bid11 = new Bid(11L, userRepository.findById(4L).get(),  56000F,     auctionRepository.findById(1L).get());
        Bid bid12 = new Bid(12L, userRepository.findById(5L).get(),  57000F,     auctionRepository.findById(1L).get());
        Bid bid13 = new Bid(13L, userRepository.findById(6L).get(),  58000F,     auctionRepository.findById(1L).get());
        Bid bid14 = new Bid(14L, userRepository.findById(7L).get(),  59000F,     auctionRepository.findById(1L).get());
        Bid bid15 = new Bid(15L, userRepository.findById(8L).get(),  60000F,     auctionRepository.findById(1L).get());

        // BID FOR AUCTION 2 WHOSE AUCTIONEER IS CARLOS WITH USERID : 3
        Bid bid16 = new Bid(16L, userRepository.findById(4L).get(),  46000F,     auctionRepository.findById(2L).get());
        Bid bid17 = new Bid(17L, userRepository.findById(5L).get(),  47000F,     auctionRepository.findById(2L).get());
        Bid bid18 = new Bid(18L, userRepository.findById(6L).get(),  48000F,     auctionRepository.findById(2L).get());
        Bid bid19 = new Bid(19L, userRepository.findById(7L).get(),  49000F,     auctionRepository.findById(2L).get());
        Bid bid20 = new Bid(20L, userRepository.findById(8L).get(),  50000F,     auctionRepository.findById(2L).get());
        Bid bid21 = new Bid(21L, userRepository.findById(4L).get(),  51000F,     auctionRepository.findById(2L).get());
        Bid bid22 = new Bid(22L, userRepository.findById(5L).get(),  52000F,     auctionRepository.findById(2L).get());
        Bid bid23 = new Bid(23L, userRepository.findById(6L).get(),  53000F,     auctionRepository.findById(2L).get());
        Bid bid24 = new Bid(24L, userRepository.findById(7L).get(),  54000F,     auctionRepository.findById(2L).get());
        Bid bid25 = new Bid(25L, userRepository.findById(8L).get(),  55000F,     auctionRepository.findById(2L).get());
        Bid bid26 = new Bid(26L, userRepository.findById(4L).get(),  56000F,     auctionRepository.findById(2L).get());
        Bid bid27 = new Bid(27L, userRepository.findById(5L).get(),  57000F,     auctionRepository.findById(2L).get());
        Bid bid28 = new Bid(28L, userRepository.findById(6L).get(),  58000F,     auctionRepository.findById(2L).get());
        Bid bid29 = new Bid(29L, userRepository.findById(7L).get(),  59000F,     auctionRepository.findById(2L).get());
        Bid bid30 = new Bid(30L, userRepository.findById(8L).get(),  60000F,     auctionRepository.findById(2L).get());

        // BID FOR AUCTION 4 WHOSE AUCTIONEER IS DIANA WITH USERID : 4
        Bid bid31 = new Bid(31L, userRepository.findById(3L).get(),  6000F,     auctionRepository.findById(4L).get());
        Bid bid32 = new Bid(32L, userRepository.findById(5L).get(),  7000F,     auctionRepository.findById(4L).get());
        Bid bid33 = new Bid(33L, userRepository.findById(6L).get(),  8000F,     auctionRepository.findById(4L).get());
        Bid bid34 = new Bid(34L, userRepository.findById(7L).get(),  9000F,     auctionRepository.findById(4L).get());
        Bid bid35 = new Bid(35L, userRepository.findById(8L).get(),  10000F,     auctionRepository.findById(4L).get());
        Bid bid36 = new Bid(36L, userRepository.findById(3L).get(),  11000F,     auctionRepository.findById(4L).get());
        Bid bid37 = new Bid(37L, userRepository.findById(5L).get(),  12000F,     auctionRepository.findById(4L).get());
        Bid bid38 = new Bid(38L, userRepository.findById(6L).get(),  13000F,     auctionRepository.findById(4L).get());
        Bid bid39 = new Bid(39L, userRepository.findById(7L).get(),  14000F,     auctionRepository.findById(4L).get());
        Bid bid40 = new Bid(40L, userRepository.findById(8L).get(),  15000F,     auctionRepository.findById(4L).get());
        Bid bid41 = new Bid(41L, userRepository.findById(3L).get(),  16000F,     auctionRepository.findById(4L).get());
        Bid bid42 = new Bid(42L, userRepository.findById(5L).get(),  17000F,     auctionRepository.findById(4L).get());
        Bid bid43 = new Bid(43L, userRepository.findById(6L).get(),  18000F,     auctionRepository.findById(4L).get());
        Bid bid44 = new Bid(44L, userRepository.findById(7L).get(),  19000F,     auctionRepository.findById(4L).get());
        Bid bid45 = new Bid(45L, userRepository.findById(8L).get(),  20000F,     auctionRepository.findById(4L).get());
        Bid bid46 = new Bid(46L, userRepository.findById(9L).get(),  25000F,     auctionRepository.findById(4L).get());
        Bid bid47 = new Bid(47L, userRepository.findById(10L).get(),  35000F,     auctionRepository.findById(4L).get());
        Bid bid48 = new Bid(48L, userRepository.findById(9L).get(),  40000F,     auctionRepository.findById(4L).get());
        Bid bid49 = new Bid(49L, userRepository.findById(10L).get(),  35000F,     auctionRepository.findById(4L).get());
        Bid bid50 = new Bid(50L, userRepository.findById(9L).get(),  41000F,     auctionRepository.findById(4L).get());


        // BID FOR AUCTION 7 WHOSE AUCTIONEER IS SEBAS WITH USERID : 5    THIS AUCTION ARE NOT ACTIVE (THEY ALREADY HAD A WINNER)
        Bid bid51 = new Bid(51L, userRepository.findById(3L).get(),  1500000F,     auctionRepository.findById(7L).get());
        Bid bid52 = new Bid(52L, userRepository.findById(4L).get(),  1510000F,     auctionRepository.findById(7L).get());
        Bid bid53 = new Bid(53L, userRepository.findById(6L).get(),  1520000F,     auctionRepository.findById(7L).get());
        Bid bid54 = new Bid(54L, userRepository.findById(7L).get(),  1530000F,     auctionRepository.findById(7L).get());
        Bid bid55 = new Bid(55L, userRepository.findById(8L).get(),  1540000F,     auctionRepository.findById(7L).get());
        Bid bid56 = new Bid(56L, userRepository.findById(9L).get(),  1550000F,     auctionRepository.findById(7L).get());
        Bid bid57 = new Bid(57L, userRepository.findById(10L).get(),  1560000F,     auctionRepository.findById(7L).get());
        Bid bid58 = new Bid(58L, userRepository.findById(11L).get(),  1570000F,     auctionRepository.findById(7L).get());
        Bid bid59 = new Bid(59L, userRepository.findById(12L).get(),  1580000F,     auctionRepository.findById(7L).get());
        Bid bid60 = new Bid(60L, userRepository.findById(3L).get(),  1590000F,     auctionRepository.findById(7L).get());

        // BID FOR AUCTION 8-16 WHOSE AUCTIONEER IS SEBAS WITH USERID : 5    THIS AUCTION ARE NOT ACTIVE (THEY ALREADY HAD A WINNER)
        Bid bid61 = new Bid(61L, userRepository.findById(3L).get(),  1051000F,     auctionRepository.findById(8L).get());
        Bid bid62 = new Bid(62L, userRepository.findById(3L).get(),  1051000F,     auctionRepository.findById(9L).get());
        Bid bid63 = new Bid(63L, userRepository.findById(3L).get(),  1051000F,     auctionRepository.findById(10L).get());
        Bid bid64 = new Bid(64L, userRepository.findById(4L).get(),  1051000F,     auctionRepository.findById(11L).get());
        Bid bid65 = new Bid(65L, userRepository.findById(4L).get(),  1051000F,     auctionRepository.findById(12L).get());
        Bid bid66 = new Bid(66L, userRepository.findById(12L).get(),  1051000F,     auctionRepository.findById(13L).get());
        Bid bid67 = new Bid(67L, userRepository.findById(10L).get(),  1051000F,     auctionRepository.findById(14L).get());
        Bid bid68 = new Bid(68L, userRepository.findById(11L).get(),  1051000F,     auctionRepository.findById(15L).get());
        Bid bid69 = new Bid(69L, userRepository.findById(12L).get(),  1051000F,     auctionRepository.findById(16L).get());

        bidRepository.saveAll(List.of(bid1, bid2, bid3, bid4, bid5, bid6, bid7, bid8, bid9, bid10,bid11,bid12,bid13,bid14,bid15,bid16,bid17,bid18,bid19,bid20,
                                      bid21,bid22,bid23,bid24,bid25,bid26,bid27,bid28,bid29,bid30,bid31,bid32,bid33,bid34,bid35,bid36,bid37,bid38,bid39,bid40,
                                      bid41,bid42,bid43,bid44,bid45,bid46,bid47,bid48,bid49,bid50,bid51,bid52,bid53,bid54,bid55,bid56,bid57,bid58,bid59,bid60,
                                      bid61,bid62,bid63,bid64,bid65,bid66,bid67,bid68,bid69));
    }

    private void registerWinnerBids() {
        //                                 (id,   auction,                            user )
        WinnerBid winnerBid1 = new WinnerBid(1L, auctionRepository.findById(7L).get(), userRepository.findById(3L).get());
        WinnerBid winnerBid2 = new WinnerBid(2L, auctionRepository.findById(8L).get(), userRepository.findById(3L).get());
        WinnerBid winnerBid3 = new WinnerBid(3L, auctionRepository.findById(9L).get(), userRepository.findById(3L).get());
        WinnerBid winnerBid4 = new WinnerBid(4L, auctionRepository.findById(10L).get(), userRepository.findById(3L).get());
        WinnerBid winnerBid5 = new WinnerBid(5L, auctionRepository.findById(11L).get(), userRepository.findById(4L).get());
        WinnerBid winnerBid6 = new WinnerBid(6L, auctionRepository.findById(12L).get(), userRepository.findById(4L).get());
        WinnerBid winnerBid7 = new WinnerBid(7L, auctionRepository.findById(13L).get(), userRepository.findById(12L).get());
        WinnerBid winnerBid8 = new WinnerBid(8L, auctionRepository.findById(14L).get(), userRepository.findById(10L).get());
        WinnerBid winnerBid9 = new WinnerBid(9L, auctionRepository.findById(15L).get(), userRepository.findById(11L).get());
        WinnerBid winnerBid10 = new WinnerBid(10L, auctionRepository.findById(16L).get(), userRepository.findById(12L).get());

        winnerBidRepository.saveAll(List.of(winnerBid1,winnerBid2,winnerBid3,winnerBid4,winnerBid5,
                                            winnerBid6,winnerBid7,winnerBid8,winnerBid9,winnerBid10));
    }



}