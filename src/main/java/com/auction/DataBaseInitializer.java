package com.auction;

import com.auction.entity.Auction;
import com.auction.entity.Bid;
import com.auction.entity.Role;
import com.auction.entity.User;
import com.auction.repository.AuctionRepository;
import com.auction.repository.BidRepository;
import com.auction.repository.RoleRepository;
import com.auction.repository.UserRepository;
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


    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {

            registerRoles();
            registerUsers();
            registerAuctions();
            registerBids();

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
        User admin1 = new User(1L,"adminAddress","123456789","adminCity","123456789","admin@gmail","admin",passwordEncoder.encode("adminPassword"),"admin",0F,Collections.singleton(roleAdmin));
        User admin2 = new User(2L,"admin2Address","123456789","admin2City","123456789","admin2@gmail","admin2",passwordEncoder.encode("admin2Password"),"admin2",0F,Collections.singleton(roleAdmin));

        // USERS
        User user1 = new User(3L,"carlosAddress","123456789","carlosCity","123456789","carlos@gmail","carlos",passwordEncoder.encode("carlosPassword"),"carlos",0F,Collections.singleton(roleUser));
        User user2 = new User(4L,"dianaAddress","123456789","dianaCity","123456789","diana@gmail","diana",passwordEncoder.encode("dianaPassword"),"diana",0F,Collections.singleton(roleUser));
        User user3 = new User(5L,"sebasAddress","123456789","sebasCity","123456789","sebas@gmail","sebas",passwordEncoder.encode("sebasPassword"),"sebas",0F,Collections.singleton(roleUser));
        User user4 = new User(6L,"jorgeAddress","123456789","jorgeCity","123456789","jorge@gmail","jorge",passwordEncoder.encode("jorgePassword"),"jorge",0F,Collections.singleton(roleUser));
        User user5 = new User(7L,"juanAddress","123456789","juanCity","123456789","juan@gmail","juan",passwordEncoder.encode("juanPassword"),"juan",0F,Collections.singleton(roleUser));
        User user6 = new User(8L,"luisAddress","123456789","luisCity","123456789","luis@gmail","luis",passwordEncoder.encode("luisPassword"),"luis",0F,Collections.singleton(roleUser));
        User user7 = new User(9L,"lenaAddress","123456789","lenaCity","123456789","lena@gmail","lena",passwordEncoder.encode("lenaPassword"),"lena",0F,Collections.singleton(roleUser));
        User user8 = new User(10L,"alexAddress","123456789","alexCity","123456789","alex@gmail","alex",passwordEncoder.encode("alexPassword"),"alex",0F,Collections.singleton(roleUser));
        User user9 = new User(11L,"ivanAddress","123456789","ivanCity","123456789","ivan@gmail","ivan",passwordEncoder.encode("ivanPassword"),"ivan",0F,Collections.singleton(roleUser));
        User user10 = new User(12L,"anaAddress","123456789","anaCity","123456789","ana@gmail","ana",passwordEncoder.encode("anaPassword"),"ana",0F,Collections.singleton(roleUser));
        userRepository.saveAll(List.of(admin1,admin2,user1,user2,user3,user4,user5,user6,user7,user8,user9,user10));
    }

    private void registerAuctions() {
        //                            (id,  product,   description,  initVal, active, highBid, highBidderId,  user)
        Auction auctionCarlos1 = new Auction(1L, "carro1", "description", 45000F,  true,  46000F,   4L,  userRepository.findById(3L).get());
        Auction auctionCarlos2 = new Auction(2L, "carro2", "description", 49000F,  true,  null,   null,  userRepository.findById(3L).get());
        Auction auctionCarlos3 = new Auction(3L, "carro3", "description", 26000F,  true,  null,   null,  userRepository.findById(3L).get());
        Auction auctionDiana1 = new Auction(4L, "juguete1", "description", 5500F,  true,  null,   null,  userRepository.findById(4L).get());
        Auction auctionDiana2 = new Auction(5L, "juguete2", "description", 2300F,  true,  null,   null,  userRepository.findById(4L).get());
        Auction auctionDiana3 = new Auction(6L, "juguete3", "description", 13000F,  true,  null,   null,  userRepository.findById(4L).get());
        Auction auctionSebas1 = new Auction(7L, "ps4", "new play station 4", 1250000F,true,null,   null,  userRepository.findById(5L).get());
        auctionRepository.saveAll(List.of(auctionCarlos1,auctionCarlos2,auctionCarlos3,auctionDiana1,auctionDiana2,auctionDiana3,auctionSebas1));
    }

    private void registerBids() {
        //              (id,   user,                            bidAmount,  auction)
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



        bidRepository.saveAll(List.of(bid1,bid2,bid3,bid4,bid5,bid6,bid7,bid8,bid9,bid10,bid11,bid12,bid13,bid14,bid15,bid16,bid17,bid18,bid19,bid20,
                                    bid21,bid22,bid23,bid24,bid25,bid26,bid27,bid28,bid29,bid30,bid31,bid32,bid33,bid34,bid35,bid36,bid37,bid38,bid39,bid40,
                                    bid41,bid42,bid43,bid44,bid45));
    }



}