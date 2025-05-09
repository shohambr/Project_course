package DomainLayer;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Bid {
    private final LocalDateTime bidTime;
    private final BigDecimal minPrice;
    private final BigDecimal increment;
    private BigDecimal lastBid;
    private String highestBidder;

    public Bid(LocalDateTime bidTime, BigDecimal minPrice, BigDecimal increment) {
        if (bidTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Bid time must be in the future");
        }
        if (minPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Minimum price must be positive");
        }
        if (increment.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Increment must be positive");
        }
        this.bidTime = bidTime;
        this.minPrice = minPrice;
        this.increment = increment;
        this.lastBid = minPrice;
        this.highestBidder = null;
    }

    public void placeBid(BigDecimal amount, String bidder) {
        if (LocalDateTime.now().isAfter(bidTime)) {
            throw new IllegalStateException("Bidding time has expired");
        }
        if (amount.compareTo(lastBid) <= 0) {
            throw new IllegalArgumentException("Bid must be higher than the current highest bid");
        }
        BigDecimal difference = amount.subtract(lastBid);
        if (difference.remainder(increment).compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalArgumentException("Bid must be in increments of " + increment);
        }
        lastBid = amount;
        highestBidder = bidder;
    }

    public LocalDateTime getBidTime() {
        return bidTime;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public BigDecimal getIncrement() {
        return increment;
    }

    public BigDecimal getLastBid() {
        return lastBid;
    }

    public String getHighestBidder() {
        return highestBidder;
    }
}
