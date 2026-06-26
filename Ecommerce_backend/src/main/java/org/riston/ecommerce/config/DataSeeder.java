package org.riston.ecommerce.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.riston.ecommerce.domain.*;
import org.riston.ecommerce.model.*;
import org.riston.ecommerce.repository.*;
import org.riston.ecommerce.service.impl.EmbeddingIngestionServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final AddressRepository addressRepository;
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final CategoryRepository categoryRepository;
    private final CouponRepository couponRepository;
    private final DealRepository dealRepository;
    private final HomeCategoryRepository homeCategoryRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final PaymentOrderRepository paymentOrderRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final SellerReportRepository sellerReportRepository;
    private final SellerRepository sellerRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final VerificationCodeRepository verificationCodeRepository;
    private final WishlistRepository wishlistRepository;
    private final EmbeddingIngestionServiceImpl embeddingIngestionServiceImpl;
    private final JdbcTemplate jdbcTemplate;

    private final Faker faker = new Faker();
    private final Random random = new Random();

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            log.info("Starting full database seed process using Datafaker...");

            // 1. Independent Core Tables
            List<User> users = seedUsers();
            List<Seller> sellers = seedSellers();
            List<Category> categories = seedCategories();
            seedCoupons();

            // 2. Base Relations
            seedUserAddresses(users);
            seedVerificationCodes(users, sellers);

            // 3. Products
            List<Product> products = seedProducts(categories, sellers);

            // 4. Shopping Interactions
            seedCartsAndItems(users, products);
            seedWishlists(users, products);
            seedReviews(users, products);

            // 5. Marketing Features
            List<HomeCategory> homeCategories = seedHomeCategories(categories);
            seedDeals(homeCategories);

            seedSellerReports(sellers);

            log.info("Database has been completely populated with clean, context-relevant mock data!");
        } else {
            log.info("Core data detected. Seeding skipped.");
        }
        if (vectorStoreIsEmpty()) {
            log.info("Starting embedding ingestion...");
            embeddingIngestionServiceImpl.ingestProducts(productRepository.findAll());
            log.info("Embeddings ingested into pgvector!");
        }
        else {
            log.info("Vector store already has data — skipping ingestion.");
        }
    }

    private boolean vectorStoreIsEmpty() {
        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM document_chunks", Long.class);
        return count == null || count == 0;
    }

    private List<User> seedUsers() {
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setFullName(faker.name().fullName());
            user.setEmail(faker.internet().emailAddress());
            user.setMobile(faker.phoneNumber().cellPhone());
            user.setPassword("raw_password_placeholder");
            user.setRole(USER_ROLE.ROLE_CUSTOMER);
            userList.add(userRepository.save(user));
        }
        log.info("Seeded Users");
        return userList;
    }

    private List<Seller> seedSellers() {
        List<Seller> sellerList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Seller seller = new Seller();
            seller.setSellerName(faker.company().name());
            seller.setEmail(faker.internet().emailAddress());
            seller.setMobile(faker.phoneNumber().cellPhone());
            seller.setPassword("seller_password");
            seller.setGSTIN(faker.regexify("[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}"));
            seller.setRole(USER_ROLE.ROLE_SELLER);
            seller.setEmailVerified(true);
            seller.setAccountStatus(AccountStatus.ACTIVE);

            BusinessDetails business = seller.getBusinessDetails();
            business.setBusinessName(seller.getSellerName() + " Ltd");
            business.setBusinessEmail(seller.getEmail());
            business.setBusinessMobile(seller.getMobile());
            business.setBusinessAddress(faker.address().fullAddress());
            business.setLogo(faker.internet().image());
            business.setBanner(faker.internet().image());

            BankDetails bank = seller.getBankDetails();
            bank.setAccountNumber(faker.regexify("[0-9]{9,18}"));
            bank.setAccountHolderName(seller.getSellerName());
            bank.setIfscCode(faker.regexify("SBIN0[0-9]{6}"));

            Address pickup = seller.getPickupAddress();
            pickup.setName(seller.getSellerName() + " Warehouse");
            pickup.setLocality(faker.address().secondaryAddress());
            pickup.setAddress(faker.address().streetAddress());
            pickup.setCity(faker.address().city());
            pickup.setState(faker.address().state());
            pickup.setPinCode(faker.address().zipCode());
            pickup.setMobileNumber(seller.getMobile());

            sellerList.add(sellerRepository.save(seller));
        }
        log.info("Seeded Sellers");
        return sellerList;
    }

    private List<Category> seedCategories() {
        List<Category> catList = new ArrayList<>();
        String[] structure = {"Electronics", "Clothing", "Home Decor"};

        for (String parentName : structure) {
            Category level1 = new Category();
            level1.setName(parentName);
            level1.setCategoryId(parentName.toLowerCase() + "_l1");
            level1.setLevel(1);
            level1.setParentCategory(null);
            Category savedL1 = categoryRepository.save(level1);
            catList.add(savedL1);

            Category level2 = new Category();
            level2.setName("Premium " + parentName);
            level2.setCategoryId(parentName.toLowerCase() + "_l2");
            level2.setLevel(2);
            level2.setParentCategory(savedL1);
            catList.add(categoryRepository.save(level2));
        }
        log.info("Seeded Categories");
        return catList;
    }

    private void seedCoupons() {
        String[] promoCodes = {"WELCOME10", "MEGA25", "FESTIVE50"};
        String[] percentages = {"10", "25", "50"};

        for (int i = 0; i < promoCodes.length; i++) {
            Coupon coupon = new Coupon();
            coupon.setCode(promoCodes[i]);
            coupon.setDiscountPercentage(percentages[i]);
            coupon.setValidityStartDate(LocalDate.now());
            coupon.setValidityEndDate(LocalDate.now().plusMonths(3));
            coupon.setMinimumOrderValue(499.00);
            coupon.setIsActive(true);
            couponRepository.save(coupon);
        }
        log.info("Seeded Coupons");
    }

    private void seedUserAddresses(List<User> users) {
        for (User u : users) {
            Address address = new Address();
            address.setName(u.getFullName() + " Home");
            address.setLocality(faker.address().secondaryAddress());
            address.setAddress(faker.address().streetAddress());
            address.setCity(faker.address().city());
            address.setState(faker.address().state());
            address.setPinCode(faker.address().zipCode());
            address.setMobileNumber(u.getMobile() != null ? u.getMobile() : faker.phoneNumber().cellPhone());
            Address savedAddress = addressRepository.save(address);
            u.getAddresses().add(savedAddress);
            userRepository.save(u);
        }
        log.info("Seeded User Addresses");
    }

    private void seedVerificationCodes(List<User> users, List<Seller> sellers) {
        for (User u : users) {
            VerificationCode vc = new VerificationCode();
            vc.setOtp(faker.number().digits(6));
            vc.setEmail(u.getEmail());
            vc.setUser(u);
            vc.setRole(u.getRole());
            vc.setExpiryDate(LocalDateTime.now().plusMinutes(15));
            verificationCodeRepository.save(vc);
        }
        for (Seller s : sellers) {
            VerificationCode vc = new VerificationCode();
            vc.setOtp(faker.number().digits(6));
            vc.setEmail(s.getEmail());
            vc.setSeller(s);
            vc.setRole(s.getRole());
            vc.setExpiryDate(LocalDateTime.now().plusMinutes(15));
            verificationCodeRepository.save(vc);
        }
        log.info("Seeded Verification Codes");
    }

    private List<Product> seedProducts(List<Category> categories, List<Seller> sellers) {
        List<Product> productList = new ArrayList<>();

        // category name → list of {title, description, color, priceRange}
        Map<String, List<String[]>> categoryProducts = new LinkedHashMap<>();

        categoryProducts.put("Electronics", List.of(
                new String[]{"Wireless Bluetooth Headphones", "Over-ear wireless headphones with 30hr battery, active noise cancellation and deep bass sound.", "Black", "1200", "2500"},
                new String[]{"Smart LED TV 32 inch", "Full HD 32-inch smart TV with built-in WiFi, Netflix, YouTube and HDMI ports.", "Black", "8000", "15000"},
                new String[]{"USB-C Fast Charger 65W", "GaN 65W fast charger compatible with laptops, smartphones and tablets via USB-C.", "White", "800", "2000"},
                new String[]{"Portable Power Bank 20000mAh", "Slim power bank with dual USB-A and USB-C ports, LED battery indicator and fast charging.", "Grey", "999", "2500"},
                new String[]{"Mechanical Keyboard TKL RGB", "Tenkeyless mechanical keyboard with RGB backlight, tactile blue switches and aluminium frame.", "Black", "2000", "5000"}
        ));

        categoryProducts.put("Premium Electronics", List.of(
                new String[]{"4K OLED Monitor 27 inch", "27-inch 4K OLED monitor with 144Hz refresh rate, HDR support and USB-C connectivity.", "Silver", "25000", "50000"},
                new String[]{"Noise Cancelling Earbuds Pro", "Premium ANC earbuds with spatial audio, 8hr playtime and wireless charging case.", "White", "5000", "12000"},
                new String[]{"Smart Watch Series X", "Advanced smartwatch with AMOLED display, GPS, heart rate monitor, SpO2 sensor and 7-day battery.", "Black", "8000", "18000"},
                new String[]{"Gaming Mouse 16000 DPI", "Ergonomic gaming mouse with 16000 DPI optical sensor, 6 programmable buttons and RGB lighting.", "Black", "1500", "4000"},
                new String[]{"1080p Webcam with Autofocus", "USB plug-and-play webcam with autofocus, dual noise-cancelling microphones and 90-degree FOV.", "Black", "2000", "5000"}
        ));

        categoryProducts.put("Clothing", List.of(
                new String[]{"Men's Slim Fit Chinos", "Comfortable slim fit chinos in stretch cotton blend, ideal for office and casual wear.", "Beige", "500", "1200"},
                new String[]{"Women's Floral Kurta", "Elegant floral printed kurta in breathable cotton fabric, perfect for daily and festive wear.", "Blue", "400", "900"},
                new String[]{"Unisex Fleece Hoodie", "Cozy fleece hoodie with kangaroo pocket and drawstring hood, available in multiple colours.", "Grey", "600", "1500"},
                new String[]{"Men's Formal Shirt", "Wrinkle-resistant formal shirt with regular fit, button-down collar and chest pocket.", "White", "500", "1200"},
                new String[]{"Women's Denim Jacket", "Classic relaxed fit denim jacket with button closure and two front pockets, suitable for all seasons.", "Blue", "800", "2000"}
        ));

        categoryProducts.put("Premium Clothing", List.of(
                new String[]{"Men's Wool Blazer", "Tailored single-breasted wool blazer with two-button closure and inner lining, ideal for formal occasions.", "Navy", "3000", "7000"},
                new String[]{"Women's Pure Silk Saree", "Handwoven pure silk saree with intricate zari border and blouse piece, perfect for weddings.", "Red", "5000", "12000"},
                new String[]{"Unisex Genuine Leather Jacket", "Genuine leather biker jacket with quilted lining, silver zips and multiple pockets.", "Black", "4000", "9000"},
                new String[]{"Men's Jogger Pants", "Lightweight jogger pants with elastic waistband and zip pockets, great for gym and casual outings.", "Black", "600", "1500"},
                new String[]{"Women's Embroidered Anarkali Suit", "Heavily embroidered Anarkali suit with dupatta and palazzo pants, ideal for festive occasions.", "Green", "2500", "6000"}
        ));

        categoryProducts.put("Home Decor", List.of(
                new String[]{"Ceramic Table Lamp", "Elegant handcrafted ceramic table lamp with warm white LED bulb, perfect for bedroom and living room.", "White", "800", "2000"},
                new String[]{"Wooden Photo Frame Set of 3", "Set of 3 rustic wooden photo frames in 4x6, 5x7 and 8x10 sizes with glass cover.", "Brown", "400", "900"},
                new String[]{"Floating Wall Shelves Set", "Set of 3 floating wall shelves made from solid pine wood with hidden brackets, easy to install.", "Walnut", "700", "1800"},
                new String[]{"Decorative Embroidered Throw Pillows", "Set of 2 decorative throw pillows with geometric embroidered covers, machine washable and fade-resistant.", "Mustard", "500", "1200"},
                new String[]{"Scented Soy Candle Gift Set", "Set of 4 hand-poured soy wax candles in lavender, vanilla, rose and sandalwood, 40hr burn time each.", "Cream", "600", "1500"}
        ));

        categoryProducts.put("Premium Home Decor", List.of(
                new String[]{"Handwoven Jute Area Rug", "Eco-friendly handwoven jute rug with anti-slip latex backing, ideal for living rooms and hallways.", "Natural", "1500", "4000"},
                new String[]{"White Marble Serving Tray", "Premium white marble serving tray with polished brass handles, perfect for hosting and display.", "White", "2000", "5000"},
                new String[]{"Boho Rattan Hanging Chair", "Hand-woven rattan hanging egg chair with padded cushion, suitable for indoor and covered outdoor use.", "Brown", "5000", "12000"},
                new String[]{"Brass Diya Set of 6", "Set of 6 handcrafted brass diyas with intricate floral detailing, ideal for Diwali and festive decoration.", "Gold", "500", "1200"},
                new String[]{"Large Macrame Wall Hanging", "Hand-knotted large macrame wall hanging in natural cotton rope with wooden dowel, 90cm wide.", "Cream", "800", "2000"}
        ));

        for (Category category : categories) {
            List<String[]> products = categoryProducts.getOrDefault(category.getName(), List.of());

            for (String[] productData : products) {
                String title = productData[0];
                String description = productData[1];
                String color = productData[2];
                int minPrice = Integer.parseInt(productData[3]);
                int maxPrice = Integer.parseInt(productData[4]);

                int mrp = minPrice + random.nextInt(maxPrice - minPrice);
                int discount = random.nextInt(30) + 5; // 5–35%
                int sellingPrice = (int) (mrp * (1 - discount / 100.0));

                Product product = new Product();
                product.setTitle(title);
                product.setDescription(description);
                product.setColor(color);
                product.setSizes("S,M,L,XL");
                product.setMrpPrice(mrp);
                product.setSellingPrice(sellingPrice);
                product.setDiscountPercent(discount);
                product.setQuantity(random.nextInt(100) + 10);
                product.setNumRatings(random.nextInt(500));
                product.setCreatedAt(LocalDateTime.now());
                product.setImages(Arrays.asList(faker.internet().image(), faker.internet().image()));
                product.setCategory(category);
                product.setSeller(sellers.get(random.nextInt(sellers.size())));

                productList.add(productRepository.save(product));
            }
        }

        log.info("Seeded Products");
        return productList;
    }

    private void seedCartsAndItems(List<User> users, List<Product> products) {
        for (User u : users) {
            Cart cart = new Cart();
            cart.setUser(u);
            cart = cartRepository.save(cart);

            int itemsToGenerate = random.nextInt(3) + 1;
            double cartTotalSelling = 0;
            int cartTotalMrp = 0;
            int cartTotalItemsCount = 0;

            for (int i = 0; i < itemsToGenerate; i++) {
                Product randomProd = products.get(random.nextInt(products.size()));

                CartItem item = new CartItem();
                item.setCart(cart);
                item.setProduct(randomProd);
                item.setSize("M");
                item.setQuantity(random.nextInt(2) + 1);
                item.setMrpPrice(randomProd.getMrpPrice());
                item.setSellingPrice(randomProd.getSellingPrice());
                item.setUserId(u.getId());
                cartItemRepository.save(item);

                cartTotalSelling += (item.getSellingPrice() * item.getQuantity());
                cartTotalMrp += (item.getMrpPrice() * item.getQuantity());
                cartTotalItemsCount += item.getQuantity();
            }

            cart.setTotalSellingPrice(cartTotalSelling);
            cart.setTotalMrpPrice(cartTotalMrp);
            cart.setTotalItem(cartTotalItemsCount);
            cart.setDiscount(cartTotalMrp - (int) cartTotalSelling);
            cartRepository.save(cart);
        }
        log.info("Seeded Carts and Cart Items");
    }

    private void seedWishlists(List<User> users, List<Product> products) {
        for (User u : users) {
            Wishlist wishlist = new Wishlist();
            wishlist.setUser(u);

            Set<Product> wishlistProducts = new HashSet<>();
            wishlistProducts.add(products.get(random.nextInt(products.size())));
            wishlistProducts.add(products.get(random.nextInt(products.size())));
            wishlist.setProducts(wishlistProducts);

            wishlistRepository.save(wishlist);
        }
        log.info("Seeded Wishlists");
    }

    private void seedReviews(List<User> users, List<Product> products) {
        String[] reviewTexts = {
                "Absolutely love this product! Great quality and fast delivery.",
                "Good value for money. Exactly as described.",
                "Very happy with the purchase. Will buy again.",
                "Quality is decent for the price. Packaging was good.",
                "Exceeded my expectations. Highly recommend!",
                "Product is okay but delivery was delayed.",
                "Great build quality. Looks exactly like the pictures.",
                "Perfect gift. The recipient loved it!",
                "Works as expected. No complaints.",
                "Solid product. Customer support was helpful too."
        };

        for (Product p : products) {
            int reviewsCount = random.nextInt(4) + 1;
            for (int i = 0; i < reviewsCount; i++) {
                Review review = new Review();
                review.setReviewText(reviewTexts[random.nextInt(reviewTexts.length)]);
                review.setRating(random.nextInt(2) + 4.0);
                review.setProduct(p);
                review.setUser(users.get(random.nextInt(users.size())));
                review.setProductImages(Collections.singletonList(faker.internet().image()));
                review.setCreatedAt(LocalDateTime.now());
                reviewRepository.save(review);
            }
        }
        log.info("Seeded Reviews");
    }

    private List<HomeCategory> seedHomeCategories(List<Category> categories) {
        List<HomeCategory> homeCatList = new ArrayList<>();
        HomeCategorySection[] sections = HomeCategorySection.values();

        for (Category c : categories) {
            HomeCategory hc = new HomeCategory();
            hc.setName(c.getName());
            hc.setCategoryId(c.getCategoryId());
            hc.setImage(faker.internet().image());
            hc.setSection(sections[random.nextInt(sections.length)]);
            homeCatList.add(homeCategoryRepository.save(hc));
        }
        log.info("Seeded Home Categories");
        return homeCatList;
    }

    private void seedDeals(List<HomeCategory> homeCategories) {
        for (int i = 0; i < Math.min(3, homeCategories.size()); i++) {
            Deal deal = new Deal();
            deal.setDiscount(random.nextInt(20) + 10);
            deal.setHomeCategory(homeCategories.get(i));
            dealRepository.save(deal);
        }
        log.info("Seeded Active System Deals");
    }

    private void seedSellerReports(List<Seller> sellers) {
        for (Seller s : sellers) {
            SellerReport report = new SellerReport();
            report.setSeller(s);
            report.setTotalOrders(5L);
            report.setTotalSales(2500L);
            report.setTotalEarnings(2200L);
            report.setNetEarnings(2100L);
            report.setTotalTax(100L);
            report.setTotalRefunds(0L);
            report.setCanceledOrders(0L);
            sellerReportRepository.save(report);
        }
        log.info("Seeded Seller Statistics Reports");
    }
}