package com.mytrip.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Trip entity representing a single planned trip.
 *
 * NOTE on validation strategy (see Final_Angelica_Instructions.md):
 *  - Blank/required-field checks (tripName, ownerEmail, startLocation, destination)
 *    are enforced via Jakarta Bean Validation annotations. These are only triggered
 *    when validation is explicitly invoked, e.g. through the controller's @Valid.
 *  - Numeric setters below throw IllegalArgumentException immediately for values
 *    that can never be valid (negative distance, negative fuel price), since these
 *    make direct calculation impossible/nonsensical regardless of validation layer.
 *  - vehicleMpg of zero is allowed to be *set* (so partial/in-progress objects don't
 *    blow up), but calculateFuelCost() throws if mpg <= 0 at calculation time.
 */
@Entity
@Table(name = "TRIPS")
public class Trip {

    private static final BigDecimal AVG_SPEED_MPH = new BigDecimal("55");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "tripName is required")
    private String tripName;

    @NotBlank(message = "ownerEmail is required")
    private String ownerEmail;

    @NotBlank(message = "startLocation is required")
    private String startLocation;

    // Optional field - no validation annotation
    private String waypoint;

    @NotBlank(message = "destination is required")
    private String destination;

    @NotNull(message = "distanceMiles is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "distanceMiles must be greater than zero")
    private BigDecimal distanceMiles;

    @NotNull(message = "vehicleMpg is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "vehicleMpg must be greater than zero")
    private BigDecimal vehicleMpg;

    @NotNull(message = "fuelPrice is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "fuelPrice cannot be negative")
    private BigDecimal fuelPrice;

    @NotNull(message = "budget is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "budget cannot be negative")
    private BigDecimal budget;

    @NotNull(message = "lodgingCost is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "lodgingCost cannot be negative")
    private BigDecimal lodgingCost;

    @NotNull(message = "activityCost is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "activityCost cannot be negative")
    private BigDecimal activityCost;

    public Trip() {
    }

    // --- id ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // --- tripName ---
    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    // --- ownerEmail ---
    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    // --- startLocation ---
    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    // --- waypoint (optional) ---
    public String getWaypoint() {
        return waypoint;
    }

    public void setWaypoint(String waypoint) {
        this.waypoint = waypoint;
    }

    // --- destination ---
    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    // --- distanceMiles ---
    public BigDecimal getDistanceMiles() {
        return distanceMiles;
    }

    public void setDistanceMiles(BigDecimal distanceMiles) {
        if (distanceMiles != null && distanceMiles.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("distanceMiles cannot be negative");
        }
        this.distanceMiles = distanceMiles;
    }

    // --- vehicleMpg ---
    public BigDecimal getVehicleMpg() {
        return vehicleMpg;
    }

    public void setVehicleMpg(BigDecimal vehicleMpg) {
        if (vehicleMpg != null && vehicleMpg.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("vehicleMpg cannot be negative");
        }
        this.vehicleMpg = vehicleMpg;
    }

    // --- fuelPrice ---
    public BigDecimal getFuelPrice() {
        return fuelPrice;
    }

    public void setFuelPrice(BigDecimal fuelPrice) {
        if (fuelPrice != null && fuelPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("fuelPrice cannot be negative");
        }
        this.fuelPrice = fuelPrice;
    }

    // --- budget ---
    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        if (budget != null && budget.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("budget cannot be negative");
        }
        this.budget = budget;
    }

    // --- lodgingCost ---
    public BigDecimal getLodgingCost() {
        return lodgingCost;
    }

    public void setLodgingCost(BigDecimal lodgingCost) {
        if (lodgingCost != null && lodgingCost.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("lodgingCost cannot be negative");
        }
        this.lodgingCost = lodgingCost;
    }

    // --- activityCost ---
    public BigDecimal getActivityCost() {
        return activityCost;
    }

    public void setActivityCost(BigDecimal activityCost) {
        if (activityCost != null && activityCost.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("activityCost cannot be negative");
        }
        this.activityCost = activityCost;
    }

    // ---------------------------------------------------------------
    // Calculated values
    // ---------------------------------------------------------------

    /**
     * Gallons required = distanceMiles / vehicleMpg.
     * vehicleMpg must be greater than zero.
     */
    public BigDecimal calculateGallonsRequired() {
        if (vehicleMpg == null || vehicleMpg.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("vehicleMpg must be greater than zero");
        }
        if (distanceMiles == null) {
            throw new IllegalArgumentException("distanceMiles must be set");
        }
        return distanceMiles.divide(vehicleMpg, 4, RoundingMode.HALF_UP);
    }

    /**
     * Fuel cost = gallons required x fuel price per gallon.
     */
    public BigDecimal calculateFuelCost() {
        if (fuelPrice == null) {
            throw new IllegalArgumentException("fuelPrice must be set");
        }
        BigDecimal gallonsRequired = calculateGallonsRequired();
        return gallonsRequired.multiply(fuelPrice).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Total estimated trip cost = fuel cost + lodging cost + activity cost.
     */
    public BigDecimal calculateTotalCost() {
        if (lodgingCost == null || activityCost == null) {
            throw new IllegalArgumentException("lodgingCost and activityCost must be set");
        }
        return calculateFuelCost()
                .add(lodgingCost)
                .add(activityCost)
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Budget difference = traveler budget - total estimated cost.
     */
    public BigDecimal calculateBudgetDifference() {
        if (budget == null) {
            throw new IllegalArgumentException("budget must be set");
        }
        return budget.subtract(calculateTotalCost()).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Under budget when budget difference >= 0.
     */
    public boolean isUnderBudget() {
        return calculateBudgetDifference().compareTo(BigDecimal.ZERO) >= 0;
    }

    /**
     * Estimated drive time in hours = distance / average speed (55 mph).
     */
    public BigDecimal calculateEstimatedDriveTimeHours() {
        if (distanceMiles == null) {
            throw new IllegalArgumentException("distanceMiles must be set");
        }
        return distanceMiles.divide(AVG_SPEED_MPH, 4, RoundingMode.HALF_UP);
    }

    /**
     * Estimated minutes = round((distance / 55) * 60).
     */
    public long calculateEstimatedMinutes() {
        BigDecimal hours = calculateEstimatedDriveTimeHours();
        return hours.multiply(new BigDecimal("60")).setScale(0, RoundingMode.HALF_UP).longValue();
    }
}
