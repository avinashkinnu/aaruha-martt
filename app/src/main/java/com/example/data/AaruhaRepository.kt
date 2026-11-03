package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.*

class AaruhaRepository {

  private val _currentRole = MutableStateFlow(AppRole.CUSTOMER)
  val currentRole: StateFlow<AppRole> = _currentRole.asStateFlow()

  private val _userProfile = MutableStateFlow(
    CustomerProfile(
      id = "cust_avinash",
      name = "Avinash Kavuri",
      phone = "+91 98480 22334",
      email = "avinashkavuri6@gmail.com",
      addresses = listOf(
        Address(
          id = "addr_1",
          title = "Home",
          fullAddress = "Flat 402, Sai Balaji Residency, Kavuri Hills, Madhapur",
          landmark = "Near D-Mart",
          city = "Hyderabad",
          pincode = "500081",
          isDefault = true
        ),
        Address(
          id = "addr_2",
          title = "Work",
          fullAddress = "Aaruha Services HQ, 3rd Floor, Tech Park, Hitec City",
          landmark = "Opp. Cyber Towers",
          city = "Hyderabad",
          pincode = "500081",
          isDefault = false
        )
      ),
      wishlistProductIds = setOf("prod_1", "prod_5", "prod_11"),
      isDarkMode = false
    )
  )
  val userProfile: StateFlow<CustomerProfile> = _userProfile.asStateFlow()

  private val _categories = MutableStateFlow(
    listOf(
      ProductCategory("cat_fv", "Fruits & Vegetables", "🍎", 0xFFE8F5E9, 24),
      ProductCategory("cat_dairy", "Dairy Products", "🥛", 0xFFE1F5FE, 18),
      ProductCategory("cat_groc", "Groceries", "🌾", 0xFFFFF8E1, 35),
      ProductCategory("cat_snack", "Snacks", "🍿", 0xFFFBE9E7, 28),
      ProductCategory("cat_bev", "Beverages", "🧃", 0xFFEDE7F6, 22),
      ProductCategory("cat_house", "Household Items", "🧼", 0xFFE0F2F1, 20),
      ProductCategory("cat_care", "Personal Care", "🧴", 0xFFFCE4EC, 19),
      ProductCategory("cat_bakery", "Bakery", "🥐", 0xFFFFF3E0, 16),
      ProductCategory("cat_frozen", "Frozen Foods", "🧊", 0xFFE3F2FD, 14),
      ProductCategory("cat_stat", "Stationery", "✏️", 0xFFF3E5F5, 12),
      ProductCategory("cat_pet", "Pet Care", "🐾", 0xFFF1F8E9, 15)
    )
  )
  val categories: StateFlow<List<ProductCategory>> = _categories.asStateFlow()

  private val _products = MutableStateFlow(
    listOf(
      // Fruits & Vegetables
      Product(
        id = "prod_1",
        name = "Farm Fresh Shimla Royal Apple",
        categoryId = "cat_fv",
        categoryName = "Fruits & Vegetables",
        brand = "Aaruha Fresh",
        weight = "4 pcs (500-600g)",
        availableWeights = listOf("4 pcs", "8 pcs (1kg)", "12 pcs"),
        mrp = 180.0,
        sellingPrice = 139.0,
        stockQuantity = 45,
        description = "Crisp, sweet, and handpicked Himalayan Royal Apples directly from Himachal orchards. Rich in antioxidants and fiber.",
        emoji = "🍎",
        rating = 4.8,
        reviewCount = 240,
        isBestSeller = true,
        isFlashDeal = true
      ),
      Product(
        id = "prod_2",
        name = "Organic Baby Spinach (Palak)",
        categoryId = "cat_fv",
        categoryName = "Fruits & Vegetables",
        brand = "Aaruha Organics",
        weight = "250 g",
        availableWeights = listOf("250 g", "500 g"),
        mrp = 40.0,
        sellingPrice = 28.0,
        stockQuantity = 30,
        description = "Cleaned and fresh farm spinach bunch. Hydroponically harvested without harsh pesticides.",
        emoji = "🥬",
        rating = 4.7,
        reviewCount = 89,
        isTrending = true
      ),
      Product(
        id = "prod_3",
        name = "Nagpur Sweet Fresh Oranges",
        categoryId = "cat_fv",
        categoryName = "Fruits & Vegetables",
        brand = "Aaruha Fresh",
        weight = "1 kg (approx 5-6 pcs)",
        availableWeights = listOf("1 kg", "2 kg"),
        mrp = 120.0,
        sellingPrice = 89.0,
        stockQuantity = 55,
        description = "Juicy and citrusy rich oranges loaded with Vitamin C. Great for daily morning fresh juice.",
        emoji = "🍊",
        rating = 4.6,
        reviewCount = 112,
        isFlashDeal = true
      ),
      Product(
        id = "prod_4",
        name = "Hybrid Farm Tomatoes",
        categoryId = "cat_fv",
        categoryName = "Fruits & Vegetables",
        brand = "Aaruha Fresh",
        weight = "1 kg",
        availableWeights = listOf("500 g", "1 kg", "2 kg"),
        mrp = 60.0,
        sellingPrice = 38.0,
        stockQuantity = 70,
        description = "Firm, plump, and tangy tomatoes perfect for gravies, salads, and everyday cooking.",
        emoji = "🍅",
        rating = 4.5,
        reviewCount = 310,
        isBestSeller = true
      ),

      // Dairy
      Product(
        id = "prod_5",
        name = "Amul Taaza Homogenised Toned Milk",
        categoryId = "cat_dairy",
        categoryName = "Dairy Products",
        brand = "Amul",
        weight = "500 ml pouch",
        availableWeights = listOf("500 ml", "1 L", "1.5 L"),
        mrp = 28.0,
        sellingPrice = 27.0,
        stockQuantity = 90,
        description = "Fresh toned pasteurized milk with 3.0% fat and 8.5% SNF. Wholesome nutrition for the entire family.",
        emoji = "🥛",
        rating = 4.9,
        reviewCount = 840,
        isBestSeller = true
      ),
      Product(
        id = "prod_6",
        name = "Nandini Fresh Thick Curd",
        categoryId = "cat_dairy",
        categoryName = "Dairy Products",
        brand = "Nandini",
        weight = "500 g pouch",
        availableWeights = listOf("500 g", "1 kg"),
        mrp = 35.0,
        sellingPrice = 32.0,
        stockQuantity = 65,
        description = "Creamy, naturally set probiotic curd made from standardized fresh cow milk.",
        emoji = "🥣",
        rating = 4.8,
        reviewCount = 420
      ),
      Product(
        id = "prod_7",
        name = "Amul Salted Pasteurized Butter",
        categoryId = "cat_dairy",
        categoryName = "Dairy Products",
        brand = "Amul",
        weight = "100 g",
        availableWeights = listOf("100 g", "500 g"),
        mrp = 60.0,
        sellingPrice = 57.0,
        stockQuantity = 40,
        description = "The classic Utterly Butterly Delicious salted butter. Made from fresh pure milk fat.",
        emoji = "🧈",
        rating = 4.9,
        reviewCount = 612,
        isTrending = true
      ),
      Product(
        id = "prod_8",
        name = "Milky Mist Fresh Malai Paneer",
        categoryId = "cat_dairy",
        categoryName = "Dairy Products",
        brand = "Milky Mist",
        weight = "200 g block",
        availableWeights = listOf("200 g", "500 g"),
        mrp = 115.0,
        sellingPrice = 98.0,
        stockQuantity = 38,
        description = "Soft, succulent cottage cheese made with pure pasteurized milk. Absorbs flavors deliciously.",
        emoji = "🧀",
        rating = 4.8,
        reviewCount = 190
      ),

      // Groceries & Staples
      Product(
        id = "prod_9",
        name = "Aashirvaad Shudh Chakki Whole Wheat Atta",
        categoryId = "cat_groc",
        categoryName = "Groceries",
        brand = "Aashirvaad",
        weight = "5 kg",
        availableWeights = listOf("1 kg", "5 kg", "10 kg"),
        mrp = 285.0,
        sellingPrice = 249.0,
        stockQuantity = 50,
        description = "100% pure whole wheat grain flour stone-ground for soft and fluffy rotis.",
        emoji = "🌾",
        rating = 4.9,
        reviewCount = 920,
        isBestSeller = true
      ),
      Product(
        id = "prod_10",
        name = "Fortune Sunlite Refined Sunflower Oil",
        categoryId = "cat_groc",
        categoryName = "Groceries",
        brand = "Fortune",
        weight = "1 L Pouch",
        availableWeights = listOf("1 L", "5 L Can"),
        mrp = 155.0,
        sellingPrice = 132.0,
        stockQuantity = 42,
        description = "Light and healthy cooking oil enriched with Vitamins A and D. Retains natural food flavors.",
        emoji = "🫒",
        rating = 4.7,
        reviewCount = 370
      ),
      Product(
        id = "prod_11",
        name = "India Gate Basmati Rice Premium Feast",
        categoryId = "cat_groc",
        categoryName = "Groceries",
        brand = "India Gate",
        weight = "1 kg",
        availableWeights = listOf("1 kg", "5 kg"),
        mrp = 195.0,
        sellingPrice = 149.0,
        stockQuantity = 35,
        description = "Aged extra-long slender basmati grains with royal aroma. Perfect for biryanis and pulao.",
        emoji = "🍚",
        rating = 4.8,
        reviewCount = 280,
        isFlashDeal = true
      ),
      Product(
        id = "prod_12",
        name = "Tata Salt Iodised Vacuum Evaporated",
        categoryId = "cat_groc",
        categoryName = "Groceries",
        brand = "Tata",
        weight = "1 kg",
        availableWeights = listOf("1 kg"),
        mrp = 30.0,
        sellingPrice = 28.0,
        stockQuantity = 120,
        description = "Desh Ka Namak. Purity and iodine assurance in every single grain.",
        emoji = "🧂",
        rating = 4.9,
        reviewCount = 1100
      ),

      // Snacks
      Product(
        id = "prod_13",
        name = "Lay's India's Magic Masala Potato Chips",
        categoryId = "cat_snack",
        categoryName = "Snacks",
        brand = "Lay's",
        weight = "73 g",
        availableWeights = listOf("50 g", "73 g", "115 g"),
        mrp = 30.0,
        sellingPrice = 25.0,
        stockQuantity = 80,
        description = "Crispy sliced wafer chips seasoned with an explosion of rich traditional Indian spices.",
        emoji = "🥔",
        rating = 4.8,
        reviewCount = 530,
        isTrending = true
      ),
      Product(
        id = "prod_14",
        name = "Haldiram's Nagpur Aloo Bhujia",
        categoryId = "cat_snack",
        categoryName = "Snacks",
        brand = "Haldiram's",
        weight = "200 g",
        availableWeights = listOf("200 g", "400 g"),
        mrp = 65.0,
        sellingPrice = 55.0,
        stockQuantity = 60,
        description = "Crispy potato mint-spiced noodles snack. Irresistible companion with evening chai.",
        emoji = "🥨",
        rating = 4.9,
        reviewCount = 490,
        isBestSeller = true
      ),

      // Beverages
      Product(
        id = "prod_15",
        name = "Coca-Cola Original Refreshing Soft Drink",
        categoryId = "cat_bev",
        categoryName = "Beverages",
        brand = "Coca-Cola",
        weight = "750 ml bottle",
        availableWeights = listOf("250 ml", "750 ml", "1.25 L"),
        mrp = 45.0,
        sellingPrice = 40.0,
        stockQuantity = 65,
        description = "Chilled sparkling carbonated soda with the iconic refreshing taste.",
        emoji = "🥤",
        rating = 4.7,
        reviewCount = 380,
        isFlashDeal = true
      ),
      Product(
        id = "prod_16",
        name = "Red Label Natural Care CTC Tea",
        categoryId = "cat_bev",
        categoryName = "Beverages",
        brand = "Brooke Bond",
        weight = "500 g",
        availableWeights = listOf("250 g", "500 g", "1 kg"),
        mrp = 330.0,
        sellingPrice = 289.0,
        stockQuantity = 45,
        description = "Rich Assam black tea enhanced with 5 ayurvedic herbs: Ashwagandha, Mulethi, Tulsi, Cardamom & Ginger.",
        emoji = "☕",
        rating = 4.8,
        reviewCount = 450
      ),

      // Household & Personal Care
      Product(
        id = "prod_17",
        name = "Surf Excel Matic Front Load Detergent Liquid",
        categoryId = "cat_house",
        categoryName = "Household Items",
        brand = "Surf Excel",
        weight = "1 L Pouch",
        availableWeights = listOf("1 L", "2 L"),
        mrp = 230.0,
        sellingPrice = 195.0,
        stockQuantity = 40,
        description = "Superior stain removal in washing machines without leaving powdery residue.",
        emoji = "🧴",
        rating = 4.8,
        reviewCount = 310
      ),
      Product(
        id = "prod_18",
        name = "Dettol Original Germ Protection Soap",
        categoryId = "cat_care",
        categoryName = "Personal Care",
        brand = "Dettol",
        weight = "125 g (Pack of 3)",
        availableWeights = listOf("125 g x 3", "125 g x 5"),
        mrp = 175.0,
        sellingPrice = 145.0,
        stockQuantity = 50,
        description = "Proven protection against 100 illness-causing germs with moisturizing glycerine.",
        emoji = "🧼",
        rating = 4.9,
        reviewCount = 610,
        isBestSeller = true
      ),

      // Bakery & Frozen
      Product(
        id = "prod_19",
        name = "Britannia 100% Whole Wheat Bread",
        categoryId = "cat_bakery",
        categoryName = "Bakery",
        brand = "Britannia",
        weight = "400 g",
        availableWeights = listOf("400 g"),
        mrp = 55.0,
        sellingPrice = 48.0,
        stockQuantity = 35,
        description = "Zero maida, baked fresh with pure whole wheat flour. Soft, healthy breakfast bread.",
        emoji = "🍞",
        rating = 4.7,
        reviewCount = 290
      ),
      Product(
        id = "prod_20",
        name = "McCain Crispy French Fries",
        categoryId = "cat_frozen",
        categoryName = "Frozen Foods",
        brand = "McCain",
        weight = "450 g",
        availableWeights = listOf("450 g", "1.25 kg"),
        mrp = 150.0,
        sellingPrice = 125.0,
        stockQuantity = 28,
        description = "Ready-to-cook crispy golden fries made from selected Indian potatoes. Deep fry or air fry.",
        emoji = "🍟",
        rating = 4.7,
        reviewCount = 210,
        isFlashDeal = true
      ),

      // Stationery & Pet
      Product(
        id = "prod_21",
        name = "Classmate Pulse Spiral Notebook A4",
        categoryId = "cat_stat",
        categoryName = "Stationery",
        brand = "Classmate",
        weight = "300 pages",
        availableWeights = listOf("180 pages", "300 pages"),
        mrp = 160.0,
        sellingPrice = 135.0,
        stockQuantity = 40,
        description = "Premium ozone-treated elemental chlorine-free bright white paper with sturdy spiral binding.",
        emoji = "📓",
        rating = 4.8,
        reviewCount = 95
      ),
      Product(
        id = "prod_22",
        name = "Pedigree Adult Chicken & Vegetables Dog Food",
        categoryId = "cat_pet",
        categoryName = "Pet Care",
        brand = "Pedigree",
        weight = "1.2 kg",
        availableWeights = listOf("1.2 kg", "3 kg"),
        mrp = 390.0,
        sellingPrice = 339.0,
        stockQuantity = 25,
        description = "Complete and balanced dry dog food with real chicken meat, vegetables, and prebiotics for shiny coat.",
        emoji = "🐕",
        rating = 4.8,
        reviewCount = 140
      )
    )
  )
  val products: StateFlow<List<Product>> = _products.asStateFlow()

  private val _cart = MutableStateFlow<List<CartItem>>(
    listOf(
      CartItem(
        product = _products.value.first { it.id == "prod_1" },
        quantity = 2,
        selectedWeight = "4 pcs"
      ),
      CartItem(
        product = _products.value.first { it.id == "prod_5" },
        quantity = 1,
        selectedWeight = "500 ml pouch"
      )
    )
  )
  val cart: StateFlow<List<CartItem>> = _cart.asStateFlow()

  val coupons = listOf(
    Coupon("AARUHA50", "Flat 50% Off", 50, 100.0, 199.0, "Save 50% up to ₹100 on orders above ₹199"),
    Coupon("FREEDEL", "Zero Delivery Fee", 100, 30.0, 149.0, "Free delivery on orders above ₹149"),
    Coupon("SUPER100", "Flat ₹100 Off", 20, 100.0, 499.0, "Flat ₹100 instant discount on orders above ₹499")
  )

  private val _appliedCoupon = MutableStateFlow<Coupon?>(coupons.first())
  val appliedCoupon: StateFlow<Coupon?> = _appliedCoupon.asStateFlow()

  private val _orders = MutableStateFlow<List<Order>>(
    listOf(
      Order(
        id = "AG-84920",
        date = "Today, 10:42 AM",
        items = listOf(
          CartItemSummary("prod_1", "Farm Fresh Shimla Royal Apple", 139.0, 2, "4 pcs", "🍎"),
          CartItemSummary("prod_5", "Amul Taaza Toned Milk", 27.0, 1, "500 ml", "🥛"),
          CartItemSummary("prod_13", "Lay's India's Magic Masala", 25.0, 2, "73 g", "🥔")
        ),
        itemsTotal = 355.0,
        deliveryFee = 0.0,
        discountAmount = 100.0,
        couponApplied = "AARUHA50",
        finalAmount = 255.0,
        paymentMethod = "UPI / Google Pay",
        paymentStatus = "Paid",
        status = OrderStatus.OUT_FOR_DELIVERY,
        deliveryPartnerId = "dp_01",
        deliveryPartnerName = "Ramesh Kumar",
        deliveryPartnerPhone = "+91 98450 12345",
        deliveryAddress = Address("addr_1", "Home", "Flat 402, Sai Balaji Residency, Madhapur", "Near D-Mart", "Hyderabad", "500081", true),
        etaMinutes = 7,
        trackingProgress = 0.70f,
        deliveryOtp = "4912"
      ),
      Order(
        id = "AG-73210",
        date = "Yesterday, 04:15 PM",
        items = listOf(
          CartItemSummary("prod_9", "Aashirvaad Chakki Atta", 249.0, 1, "5 kg", "🌾"),
          CartItemSummary("prod_10", "Fortune Sunflower Oil", 132.0, 1, "1 L", "🫒"),
          CartItemSummary("prod_12", "Tata Salt Iodised", 28.0, 1, "1 kg", "🧂")
        ),
        itemsTotal = 409.0,
        deliveryFee = 25.0,
        discountAmount = 50.0,
        couponApplied = "INSTANT50",
        finalAmount = 384.0,
        paymentMethod = "Razorpay / Card",
        paymentStatus = "Paid",
        status = OrderStatus.DELIVERED,
        deliveryPartnerId = "dp_02",
        deliveryPartnerName = "Suresh Varma",
        deliveryPartnerPhone = "+91 97000 88221",
        deliveryAddress = Address("addr_1", "Home", "Flat 402, Sai Balaji Residency, Madhapur", "Near D-Mart", "Hyderabad", "500081", true),
        etaMinutes = 0,
        trackingProgress = 1.0f,
        deliveryOtp = "8120"
      )
    )
  )
  val orders: StateFlow<List<Order>> = _orders.asStateFlow()

  private val _activeOrderIdForTracking = MutableStateFlow<String?>("AG-84920")
  val activeOrderIdForTracking: StateFlow<String?> = _activeOrderIdForTracking.asStateFlow()

  private val _employees = MutableStateFlow(
    listOf(
      Employee(
        id = "EMP-101",
        name = "Rajesh Patel",
        phone = "+91 98234 11223",
        email = "rajesh.patel@aaruhaservices.in",
        aadhaarNumber = "XXXX-XXXX-4589",
        education = "MBA in Supply Chain Management",
        qualification = "Certified Warehouse Specialist",
        address = "Flat 201, Green Meadows, Gachibowli, Hyderabad",
        joiningDate = "15 Jan 2024",
        salary = 48000.0,
        designation = "Store Operations Manager",
        role = "Manager",
        status = "Active",
        loginTime = "08:15 AM",
        logoutTime = "Pending",
        workingHoursToday = 5.5
      ),
      Employee(
        id = "EMP-102",
        name = "Priya Sharma",
        phone = "+91 97112 44556",
        email = "priya.sharma@aaruhaservices.in",
        aadhaarNumber = "XXXX-XXXX-8921",
        education = "B.Sc. Food Technology",
        qualification = "Quality & Inventory Assessor",
        address = "Plot 54, Jubilee Hills Enclave, Hyderabad",
        joiningDate = "10 Mar 2024",
        salary = 34000.0,
        designation = "Lead Inventory Specialist",
        role = "Inventory",
        status = "Active",
        loginTime = "08:45 AM",
        logoutTime = "Pending",
        workingHoursToday = 5.0
      ),
      Employee(
        id = "EMP-103",
        name = "Ananya Roy",
        phone = "+91 96554 77889",
        email = "ananya.roy@aaruhaservices.in",
        aadhaarNumber = "XXXX-XXXX-3341",
        education = "Diploma in Logistics",
        qualification = "Fast Pack & Dispatch Specialist",
        address = "H.No 12-4, Madhapur Village, Hyderabad",
        joiningDate = "01 Jun 2024",
        salary = 26000.0,
        designation = "Order Packing Associate",
        role = "Fulfillment",
        status = "On Leave",
        loginTime = "--",
        logoutTime = "--",
        workingHoursToday = 0.0
      )
    )
  )
  val employees: StateFlow<List<Employee>> = _employees.asStateFlow()

  private val _deliveryPartners = MutableStateFlow(
    listOf(
      DeliveryPartner(
        id = "dp_01",
        name = "Ramesh Kumar",
        phone = "+91 98450 12345",
        aadhaarNumber = "XXXX-XXXX-7721",
        vehicleType = "Electric Scooter (Ather 450X)",
        vehicleNumber = "TS 09 EA 4582",
        drivingLicense = "DL-092021004812",
        joiningDate = "12 Feb 2024",
        rating = 4.9,
        isActive = true,
        currentAssignedOrderId = "AG-84920",
        totalDeliveries = 1420,
        todayEarnings = 1280.0
      ),
      DeliveryPartner(
        id = "dp_02",
        name = "Suresh Varma",
        phone = "+91 97000 88221",
        aadhaarNumber = "XXXX-XXXX-1944",
        vehicleType = "Honda Activa 6G",
        vehicleNumber = "AP 10 BF 7890",
        drivingLicense = "DL-102022009841",
        joiningDate = "05 Apr 2024",
        rating = 4.8,
        isActive = true,
        currentAssignedOrderId = null,
        totalDeliveries = 980,
        todayEarnings = 890.0
      ),
      DeliveryPartner(
        id = "dp_03",
        name = "Vikram Singh",
        phone = "+91 99123 55667",
        aadhaarNumber = "XXXX-XXXX-6288",
        vehicleType = "TVS iQube Electric",
        vehicleNumber = "TS 07 HG 2134",
        drivingLicense = "DL-072023001928",
        joiningDate = "20 May 2024",
        rating = 4.95,
        isActive = false,
        currentAssignedOrderId = null,
        totalDeliveries = 1890,
        todayEarnings = 0.0
      )
    )
  )
  val deliveryPartners: StateFlow<List<DeliveryPartner>> = _deliveryPartners.asStateFlow()

  private val _monthlyRevenue = MutableStateFlow(
    listOf(
      MonthlyRevenueRecord("May 2026", 1240, 682000.0, 480000.0, 74000.0, 108000.0, 202000.0),
      MonthlyRevenueRecord("Jun 2026", 1480, 814000.0, 560000.0, 88000.0, 108000.0, 254000.0),
      MonthlyRevenueRecord("Jul 2026", 1720, 946000.0, 640000.0, 103000.0, 108000.0, 306000.0),
      MonthlyRevenueRecord("Aug 2026", 1950, 1072500.0, 720000.0, 117000.0, 108000.0, 352500.0),
      MonthlyRevenueRecord("Sep 2026", 2180, 1199000.0, 800000.0, 130000.0, 108000.0, 399000.0),
      MonthlyRevenueRecord("Oct 2026", 2450, 1347500.0, 890000.0, 147000.0, 108000.0, 457500.0),
      MonthlyRevenueRecord("Nov 2026 (MTD)", 1320, 726000.0, 490000.0, 79000.0, 108000.0, 236000.0)
    )
  )
  val monthlyRevenue: StateFlow<List<MonthlyRevenueRecord>> = _monthlyRevenue.asStateFlow()

  private val _reviews = MutableStateFlow(
    listOf(
      Review("rev_1", "prod_1", "Pooja Reddy", 5, "Delivered within 8 minutes! Apples are super crisp and very sweet.", "2 hours ago"),
      Review("rev_2", "prod_1", "Karthik M", 5, "Zepto and Blinkit speed, but freshness is even better here.", "Yesterday"),
      Review("rev_3", "prod_5", "Deepak Rao", 5, "Cold milk pouch delivered right on time before morning coffee.", "Yesterday"),
      Review("rev_4", "prod_9", "Sunita Nair", 5, "Genuine Aashirvaad atta, packed very cleanly without any spillage.", "3 days ago")
    )
  )
  val reviews: StateFlow<List<Review>> = _reviews.asStateFlow()

  private val _supportChat = MutableStateFlow(
    listOf(
      ChatMessage("msg_1", false, "Welcome to Aaruha Groceries 24x7 Customer Care! How may we assist your delivery today?", "10:43 AM")
    )
  )
  val supportChat: StateFlow<List<ChatMessage>> = _supportChat.asStateFlow()

  // Actions
  fun setRole(role: AppRole) {
    _currentRole.value = role
  }

  fun toggleDarkMode() {
    _userProfile.update { it.copy(isDarkMode = !it.isDarkMode) }
  }

  fun toggleWishlist(productId: String) {
    _userProfile.update { current ->
      val updated = if (current.wishlistProductIds.contains(productId)) {
        current.wishlistProductIds - productId
      } else {
        current.wishlistProductIds + productId
      }
      current.copy(wishlistProductIds = updated)
    }
  }

  fun addToCart(product: Product, weight: String = product.weight) {
    _cart.update { current ->
      val existing = current.find { it.product.id == product.id && it.selectedWeight == weight }
      if (existing != null) {
        current.map {
          if (it.product.id == product.id && it.selectedWeight == weight) it.copy(quantity = it.quantity + 1)
          else it
        }
      } else {
        current + CartItem(product = product, quantity = 1, selectedWeight = weight)
      }
    }
  }

  fun decreaseCartItem(productId: String, weight: String) {
    _cart.update { current ->
      val existing = current.find { it.product.id == productId && it.selectedWeight == weight } ?: return@update current
      if (existing.quantity > 1) {
        current.map {
          if (it.product.id == productId && it.selectedWeight == weight) it.copy(quantity = it.quantity - 1)
          else it
        }
      } else {
        current.filterNot { it.product.id == productId && it.selectedWeight == weight }
      }
    }
  }

  fun removeCartItem(productId: String, weight: String) {
    _cart.update { current ->
      current.filterNot { it.product.id == productId && it.selectedWeight == weight }
    }
  }

  fun clearCart() {
    _cart.value = emptyList()
  }

  fun applyCoupon(code: String): Boolean {
    val found = coupons.find { it.code.equals(code.trim(), ignoreCase = true) }
    return if (found != null) {
      _appliedCoupon.value = found
      true
    } else {
      false
    }
  }

  fun removeCoupon() {
    _appliedCoupon.value = null
  }

  fun addAddress(address: Address) {
    _userProfile.update { current ->
      val list = if (address.isDefault) {
        current.addresses.map { it.copy(isDefault = false) } + address
      } else {
        current.addresses + address
      }
      current.copy(addresses = list)
    }
  }

  fun setDefaultAddress(id: String) {
    _userProfile.update { current ->
      val list = current.addresses.map { it.copy(isDefault = (it.id == id)) }
      current.copy(addresses = list)
    }
  }

  fun placeOrder(paymentMethod: String, address: Address, notes: String): Order {
    val cartItems = _cart.value
    val itemsTotal = cartItems.sumOf { it.product.sellingPrice * it.quantity }
    val coupon = _appliedCoupon.value
    val discount = if (coupon != null && itemsTotal >= coupon.minOrderValue) {
      val calculated = (itemsTotal * coupon.discountPercent) / 100.0
      minOf(calculated, coupon.maxDiscount)
    } else 0.0

    val deliveryFee = if (itemsTotal > 199.0 || coupon?.code == "FREEDEL") 0.0 else 25.0
    val finalTotal = maxOf(0.0, itemsTotal - discount + deliveryFee)

    val randomNum = (10000..99999).random()
    val otp = (1000..9999).random().toString()
    val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
    val dateStr = sdf.format(Date())

    val newOrder = Order(
      id = "AG-$randomNum",
      date = "Today, $dateStr",
      items = cartItems.map {
        CartItemSummary(
          productId = it.product.id,
          productName = it.product.name,
          price = it.product.sellingPrice,
          quantity = it.quantity,
          weight = it.selectedWeight,
          emoji = it.product.emoji
        )
      },
      itemsTotal = itemsTotal,
      deliveryFee = deliveryFee,
      discountAmount = discount,
      couponApplied = coupon?.code,
      finalAmount = finalTotal,
      paymentMethod = paymentMethod,
      paymentStatus = if (paymentMethod.contains("Cash")) "Pending COD" else "Paid",
      status = OrderStatus.CONFIRMED,
      deliveryPartnerId = "dp_01",
      deliveryPartnerName = "Ramesh Kumar",
      deliveryPartnerPhone = "+91 98450 12345",
      deliveryAddress = address,
      etaMinutes = 9,
      trackingProgress = 0.25f,
      deliveryOtp = otp,
      customerNotes = notes.ifBlank { "Ring bell once" }
    )

    _orders.update { listOf(newOrder) + it }
    _activeOrderIdForTracking.value = newOrder.id
    _cart.value = emptyList()
    return newOrder
  }

  fun reorder(orderId: String) {
    val order = _orders.value.find { it.id == orderId } ?: return
    order.items.forEach { summary ->
      val product = _products.value.find { it.id == summary.productId }
      if (product != null) {
        addToCart(product, summary.weight)
      }
    }
  }

  fun cancelOrder(orderId: String) {
    _orders.update { current ->
      current.map {
        if (it.id == orderId) it.copy(status = OrderStatus.CANCELLED) else it
      }
    }
  }

  fun setActiveTrackingOrder(orderId: String) {
    _activeOrderIdForTracking.value = orderId
  }

  // Delivery Partner Actions
  fun updateDeliveryPartnerStatus(orderId: String, nextStatus: OrderStatus, progress: Float) {
    _orders.update { current ->
      current.map {
        if (it.id == orderId) it.copy(status = nextStatus, trackingProgress = progress, etaMinutes = if (nextStatus == OrderStatus.DELIVERED) 0 else maxOf(1, it.etaMinutes - 2))
        else it
      }
    }
  }

  // Admin & Employee Product Management
  fun saveProduct(product: Product) {
    _products.update { current ->
      val exists = current.any { it.id == product.id }
      if (exists) {
        current.map { if (it.id == product.id) product else it }
      } else {
        listOf(product) + current
      }
    }
  }

  fun deleteProduct(productId: String) {
    _products.update { current -> current.filterNot { it.id == productId } }
  }

  fun updateStock(productId: String, delta: Int) {
    _products.update { current ->
      current.map {
        if (it.id == productId) it.copy(stockQuantity = maxOf(0, it.stockQuantity + delta))
        else it
      }
    }
  }

  // Admin Employee Management
  fun addEmployee(emp: Employee) {
    _employees.update { listOf(emp) + it }
  }

  fun toggleEmployeeStatus(employeeId: String) {
    _employees.update { current ->
      current.map {
        if (it.id == employeeId) {
          val newStatus = if (it.status == "Active") "On Leave" else "Active"
          it.copy(status = newStatus)
        } else it
      }
    }
  }

  fun addReview(productId: String, rating: Int, comment: String) {
    val newReview = Review(
      id = "rev_${System.currentTimeMillis()}",
      productId = productId,
      customerName = _userProfile.value.name,
      rating = rating,
      comment = comment,
      date = "Just now"
    )
    _reviews.update { listOf(newReview) + it }
  }

  fun sendChatMessage(msg: String) {
    val userMsg = ChatMessage(
      id = "msg_${System.currentTimeMillis()}",
      isFromUser = true,
      message = msg,
      timestamp = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
    )
    _supportChat.update { it + userMsg }

    // Instant realistic Aaruha support reply
    val replyText = when {
      msg.contains("order", true) || msg.contains("track", true) || msg.contains("where", true) ->
        "Your order AG-84920 is on the way! Rider Ramesh is about 6-8 minutes away. You can view the live GPS route right in the Live Tracking tab."
      msg.contains("refund", true) || msg.contains("cancel", true) ->
        "Any refund or cancellation requested on Aaruha Groceries is credited back to your original payment method / UPI within 15 minutes."
      msg.contains("quality", true) || msg.contains("fresh", true) ->
        "All our fruits, vegetables, and dairy items undergo 3-stage quality checks. If anything is less than perfect, we replace it instantly with no questions asked!"
      msg.contains("delivery", true) || msg.contains("time", true) ->
        "We deliver in under 10 minutes from our nearest dark store located within 2 km of your location!"
      else ->
        "Thank you for contacting Aaruha Groceries! Our customer support team is always here to make your experience ultra-fast and delighting. Let us know if you need anything else!"
    }

    val botMsg = ChatMessage(
      id = "msg_${System.currentTimeMillis() + 1}",
      isFromUser = false,
      message = replyText,
      timestamp = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
    )
    _supportChat.update { it + botMsg }
  }
}
