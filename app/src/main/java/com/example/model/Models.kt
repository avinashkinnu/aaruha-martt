package com.example.model

enum class AppRole(val title: String, val badge: String) {
  CUSTOMER("Customer View", "Shop & Track"),
  ADMIN("Admin Portal", "System Control"),
  EMPLOYEE("Employee Portal", "Store & Stock"),
  DELIVERY_PARTNER("Delivery Partner", "Rider App")
}

data class CustomerProfile(
  val id: String = "cust_101",
  val name: String = "Avinash K",
  val phone: String = "+91 98765 43210",
  val email: String = "avinash@aaruhaservices.in",
  val addresses: List<Address> = emptyList(),
  val wishlistProductIds: Set<String> = emptySet(),
  val isDarkMode: Boolean = false
)

data class Address(
  val id: String,
  val title: String, // "Home", "Work", "Apartment"
  val fullAddress: String,
  val landmark: String,
  val city: String,
  val pincode: String,
  val isDefault: Boolean = false
)

data class ProductCategory(
  val id: String,
  val name: String,
  val iconEmoji: String,
  val colorHex: Long,
  val itemCount: Int
)

data class Product(
  val id: String,
  val name: String,
  val categoryId: String,
  val categoryName: String,
  val brand: String,
  val weight: String,
  val availableWeights: List<String> = listOf("500 g", "1 kg", "2 kg"),
  val mrp: Double,
  val sellingPrice: Double,
  val offerDiscountPercent: Int = (((mrp - sellingPrice) / mrp) * 100).toInt(),
  val stockQuantity: Int,
  val description: String,
  val emoji: String,
  val rating: Double = 4.8,
  val reviewCount: Int = 128,
  val isFlashDeal: Boolean = false,
  val isBestSeller: Boolean = false,
  val isTrending: Boolean = false
)

data class CartItem(
  val product: Product,
  val quantity: Int,
  val selectedWeight: String
)

enum class OrderStatus(val title: String, val subtitle: String, val stepIndex: Int) {
  PLACED("Order Placed", "Your order has been received", 0),
  CONFIRMED("Confirmed", "Store accepted & preparing", 1),
  PACKED("Packed", "Safely packaged & quality checked", 2),
  PICKED_UP("Picked Up", "Rider collected your package", 3),
  OUT_FOR_DELIVERY("Out For Delivery", "Rider is heading to your door", 4),
  DELIVERED("Delivered", "Delivered at your doorstep", 5),
  CANCELLED("Cancelled", "Order was cancelled", -1)
}

data class Order(
  val id: String,
  val date: String,
  val items: List<CartItemSummary>,
  val itemsTotal: Double,
  val deliveryFee: Double,
  val discountAmount: Double,
  val couponApplied: String?,
  val finalAmount: Double,
  val paymentMethod: String,
  val paymentStatus: String, // "Paid", "Pending COD", "Refunded"
  val status: OrderStatus,
  val deliveryPartnerId: String? = "del_01",
  val deliveryPartnerName: String = "Ramesh Kumar",
  val deliveryPartnerPhone: String = "+91 98450 12345",
  val deliveryAddress: Address,
  val etaMinutes: Int = 8,
  val trackingProgress: Float = 0.65f, // 0 to 1 for map animation
  val deliveryOtp: String = "5821",
  val customerNotes: String = "Leave near door, ring bell once."
)

data class CartItemSummary(
  val productId: String,
  val productName: String,
  val price: Double,
  val quantity: Int,
  val weight: String,
  val emoji: String
)

data class DeliveryPartner(
  val id: String,
  val name: String,
  val phone: String,
  val aadhaarNumber: String,
  val vehicleType: String,
  val vehicleNumber: String,
  val drivingLicense: String,
  val joiningDate: String,
  val rating: Double,
  val isActive: Boolean,
  val currentAssignedOrderId: String?,
  val totalDeliveries: Int,
  val todayEarnings: Double
)

data class Employee(
  val id: String,
  val name: String,
  val phone: String,
  val email: String,
  val aadhaarNumber: String,
  val education: String,
  val qualification: String,
  val address: String,
  val joiningDate: String,
  val salary: Double,
  val designation: String,
  val role: String,
  val status: String, // "Active", "On Leave", "Offline"
  val loginTime: String,
  val logoutTime: String,
  val workingHoursToday: Double
)

data class Review(
  val id: String,
  val productId: String,
  val customerName: String,
  val rating: Int,
  val comment: String,
  val date: String
)

data class Coupon(
  val code: String,
  val title: String,
  val discountPercent: Int,
  val maxDiscount: Double,
  val minOrderValue: Double,
  val description: String
)

data class MonthlyRevenueRecord(
  val month: String,
  val ordersCount: Int,
  val revenue: Double,
  val expenses: Double,
  val deliveryCosts: Double,
  val salaries: Double,
  val profit: Double
)

data class ChatMessage(
  val id: String,
  val isFromUser: Boolean,
  val message: String,
  val timestamp: String
)
