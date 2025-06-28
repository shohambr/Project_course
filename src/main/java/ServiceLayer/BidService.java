package ServiceLayer;

import DomainLayer.BidSale;
import DomainLayer.IToken;
import DomainLayer.Product;
import DomainLayer.Store;
import InfrastructureLayer.OrderRepository;
import InfrastructureLayer.ProductRepository;
import InfrastructureLayer.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BidService {

    private final Map<String,BidSale> bids = new ConcurrentHashMap<>();

    private final PaymentService   paymentService;
    private final ShippingService  shippingService;
    private final IToken           tokenService;
    private final StoreRepository  storeRepo;
    private final ProductRepository productRepo;
    private final OrderRepository  orderRepo;

    public BidService(PaymentService   paymentService,
                      ShippingService  shippingService,
                      IToken           tokenService,
                      StoreRepository  storeRepo,
                      ProductRepository productRepo,
                      OrderRepository  orderRepo) {

        this.paymentService = paymentService;
        this.shippingService= shippingService;
        this.tokenService   = tokenService;
        this.storeRepo      = storeRepo;
        this.productRepo    = productRepo;
        this.orderRepo      = orderRepo;
    }

    /*──────────────────── creation ────────────────────*/
    public BidSale start(String storeId,
                         String productId,
                         double startPrice,
                         double minInc,
                         int minutes) {

        BidSale b = new BidSale(storeId, productId, startPrice,
                minInc, LocalDateTime.now().plusMinutes(minutes));
        bids.put(b.getId(), b);
        return b;
    }

    /*──────────────────── bidding ─────────────────────*/
    public void place(String bidId, String bidder, double amount) {

        /* ★ NEW – guests may look but not bid */
        if (bidder == null || bidder.startsWith("Guest")) {
            throw new RuntimeException("You must be logged-in to place a bid.");
        }

        bids.get(bidId).bid(bidder, amount, LocalDateTime.now());
    }

    /*──────────────────── view / housekeeping ─────────*/
    public List<BidSale> open() {
        LocalDateTime now = LocalDateTime.now();

        /* move finished bids to payment stage */
        bids.values().forEach(b -> {
            if (!b.isAwaitingPayment() && now.isAfter(b.getEndAt()))
                b.markAwaitingPayment();
        });

        return new ArrayList<>(bids.values());
    }

    /*──────────────────── payment ─────────────────────*/
    /*──────────────────── payment ─────────────────────*/
    @Transactional
    public void pay(String bidId,
                    String token,
                    String name, String card, String exp, String cvv,
                    String state, String city, String address,
                    String id,    String zip) {

        BidSale b = bids.get(bidId);
        if (b == null)              throw new RuntimeException("bid not found");
        if (!b.isAwaitingPayment()) throw new RuntimeException("not payable");

        String buyer = tokenService.extractUsername(token);
        if (!buyer.equals(b.getWinner())) throw new RuntimeException("not your bid");

        /* 1. charge card */
        paymentService.processPayment(token, name, card, exp, cvv, id);

        /* 2. arrange shipping */
        shippingService.processShipping(token, state, city, address, name, zip);

        /* 3. reserve then sell one unit */
        Store   store   = storeRepo.getById(b.getStoreId());
        Product product = productRepo.getById(b.getProductId());
        if (product == null) throw new RuntimeException("product missing");

        if (!store.reserveProduct(product.getId(), 1))      // ★ NEW
            throw new RuntimeException("out of stock");

        store.sellProduct(product.getId(), 1);              // now succeeds
        product.setQuantity(product.getQuantity() - 1);

        storeRepo.update(store);
        productRepo.save(product);

        /* 4. record order */
        orderRepo.save(new DomainLayer.Order("bid:" + bidId,
                store.getId(), buyer, new Date()));

        /* 5. remove bid from board */
        bids.remove(bidId);
    }

}
