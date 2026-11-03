package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.*
import com.example.ui.admin.AdminDashboardScreen
import com.example.ui.components.AaruhaTopBar
import com.example.ui.components.FloatingCartPreviewBar
import com.example.ui.components.RoleSwitcherDialog
import com.example.ui.customer.*
import com.example.ui.delivery.DeliveryPartnerScreen
import com.example.ui.employee.EmployeeDashboardScreen
import com.example.ui.theme.*

@Composable
fun AaruhaApp(
  viewModel: AaruhaViewModel = viewModel()
) {
  val currentRole by viewModel.currentRole.collectAsStateWithLifecycle()
  val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
  val categories by viewModel.categories.collectAsStateWithLifecycle()
  val products by viewModel.products.collectAsStateWithLifecycle()
  val filteredProducts by viewModel.filteredProducts.collectAsStateWithLifecycle()
  val cartItems by viewModel.cart.collectAsStateWithLifecycle()
  val appliedCoupon by viewModel.appliedCoupon.collectAsStateWithLifecycle()
  val orders by viewModel.orders.collectAsStateWithLifecycle()
  val activeTrackingOrder by viewModel.activeTrackingOrder.collectAsStateWithLifecycle()
  val employees by viewModel.employees.collectAsStateWithLifecycle()
  val deliveryPartners by viewModel.deliveryPartners.collectAsStateWithLifecycle()
  val monthlyRevenue by viewModel.monthlyRevenue.collectAsStateWithLifecycle()
  val reviews by viewModel.reviews.collectAsStateWithLifecycle()
  val supportChat by viewModel.supportChat.collectAsStateWithLifecycle()

  val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
  val selectedCategoryId by viewModel.selectedCategoryId.collectAsStateWithLifecycle()
  val selectedProduct by viewModel.selectedProduct.collectAsStateWithLifecycle()
  val customerTab by viewModel.currentCustomerTab.collectAsStateWithLifecycle()

  var showRoleSwitcher by remember { mutableStateOf(false) }
  var showCartSheet by remember { mutableStateOf(false) }
  var showAddressPicker by remember { mutableStateOf(false) }

  // Handle system back button properly
  BackHandler(enabled = showCartSheet || customerTab != 0 || selectedCategoryId != null || selectedProduct != null) {
    when {
      showCartSheet -> showCartSheet = false
      selectedProduct != null -> viewModel.selectProduct(null)
      selectedCategoryId != null -> viewModel.selectCategory(null)
      customerTab != 0 -> viewModel.setCustomerTab(0)
    }
  }

  val totalCartAmount = cartItems.sumOf { it.product.sellingPrice * it.quantity }
  val totalCartCount = cartItems.sumOf { it.quantity }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      if (currentRole == AppRole.CUSTOMER) {
        val defaultAddress = userProfile.addresses.firstOrNull { it.isDefault } ?: userProfile.addresses.firstOrNull()
        AaruhaTopBar(
          currentRole = currentRole,
          selectedAddress = defaultAddress,
          onSwitchRoleClick = { showRoleSwitcher = true },
          onAddressClick = { showAddressPicker = true },
          onSearchClick = { viewModel.setCustomerTab(0) },
          onWishlistClick = { viewModel.setCustomerTab(3) },
          wishlistCount = userProfile.wishlistProductIds.size
        )
      } else {
        // Module Portal Top Bar
        Surface(
          color = MaterialTheme.colorScheme.primary,
          shadowElevation = 4.dp
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .statusBarsPadding()
              .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(36.dp)
                  .clip(CircleShape)
                  .background(AaruhaYellowAccent),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = when (currentRole) {
                    AppRole.ADMIN -> Icons.Default.AdminPanelSettings
                    AppRole.EMPLOYEE -> Icons.Default.Storefront
                    AppRole.DELIVERY_PARTNER -> Icons.Default.DirectionsBike
                    else -> Icons.Default.ShoppingBag
                  },
                  contentDescription = null,
                  tint = Color.Black,
                  modifier = Modifier.size(20.dp)
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = "AARUHA GROCERIES",
                  color = AaruhaYellowAccent,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.ExtraBold,
                  letterSpacing = 0.5.sp
                )
                Text(
                  text = currentRole.title,
                  color = Color.White,
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }

            AssistChip(
              onClick = { showRoleSwitcher = true },
              label = {
                Text("Switch Role", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
              },
              colors = AssistChipDefaults.assistChipColors(containerColor = AaruhaYellowAccent)
            )
          }
        }
      }
    },
    bottomBar = {
      if (currentRole == AppRole.CUSTOMER && !showCartSheet) {
        Column {
          // Floating Cart Bar (appears when items > 0)
          FloatingCartPreviewBar(
            itemCount = totalCartCount,
            totalAmount = totalCartAmount,
            onClick = { showCartSheet = true }
          )

          // Material 3 Navigation Bar
          NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
          ) {
            NavigationBarItem(
              selected = customerTab == 0,
              onClick = { viewModel.setCustomerTab(0) },
              icon = {
                Icon(
                  imageVector = if (customerTab == 0) Icons.Filled.Storefront else Icons.Outlined.Storefront,
                  contentDescription = "Home"
                )
              },
              label = { Text("Home", fontSize = 11.sp) },
              modifier = Modifier.testTag("nav_tab_home")
            )
            NavigationBarItem(
              selected = customerTab == 1,
              onClick = { viewModel.setCustomerTab(1) },
              icon = {
                Icon(
                  imageVector = if (customerTab == 1) Icons.Filled.GridView else Icons.Outlined.GridView,
                  contentDescription = "Categories"
                )
              },
              label = { Text("Categories", fontSize = 11.sp) },
              modifier = Modifier.testTag("nav_tab_categories")
            )
            NavigationBarItem(
              selected = customerTab == 2,
              onClick = { viewModel.setCustomerTab(2) },
              icon = {
                BadgedBox(badge = {
                  if (activeTrackingOrder != null && activeTrackingOrder?.status != OrderStatus.DELIVERED) {
                    Badge(containerColor = AaruhaYellowAccent, contentColor = Color.Black) {
                      Text("LIVE")
                    }
                  }
                }) {
                  Icon(
                    imageVector = if (customerTab == 2) Icons.Filled.Moped else Icons.Outlined.Moped,
                    contentDescription = "Track"
                  )
                }
              },
              label = { Text("Live Track", fontSize = 11.sp) },
              modifier = Modifier.testTag("nav_tab_track")
            )
            NavigationBarItem(
              selected = customerTab == 3,
              onClick = { viewModel.setCustomerTab(3) },
              icon = {
                Icon(
                  imageVector = if (customerTab == 3) Icons.Filled.Person else Icons.Outlined.Person,
                  contentDescription = "Account"
                )
              },
              label = { Text("Account", fontSize = 11.sp) },
              modifier = Modifier.testTag("nav_tab_account")
            )
          }
        }
      }
    }
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
      when (currentRole) {
        AppRole.CUSTOMER -> {
          if (showCartSheet) {
            CartAndCheckoutScreen(
              cartItems = cartItems,
              appliedCoupon = appliedCoupon,
              availableCoupons = viewModel.coupons,
              addresses = userProfile.addresses,
              onIncrease = { viewModel.addToCart(it.product, it.selectedWeight) },
              onDecrease = { viewModel.decreaseCartItem(it.product.id, it.selectedWeight) },
              onRemove = { viewModel.removeCartItem(it.product.id, it.selectedWeight) },
              onApplyCoupon = { viewModel.applyCoupon(it) },
              onRemoveCoupon = { viewModel.removeCoupon() },
              onSelectAddress = { viewModel.setDefaultAddress(it) },
              onAddAddressClick = { showAddressPicker = true },
              onPlaceOrder = { method, addr, notes ->
                val ord = viewModel.placeOrder(method, addr, notes)
                showCartSheet = false
                ord
              },
              onShopMoreClick = { showCartSheet = false }
            )
          } else {
            when (customerTab) {
              0 -> {
                CustomerHomeScreen(
                  categories = categories,
                  products = filteredProducts,
                  cartItems = cartItems,
                  wishlistIds = userProfile.wishlistProductIds,
                  searchQuery = searchQuery,
                  selectedCategoryId = selectedCategoryId,
                  onSearchChange = { viewModel.setSearchQuery(it) },
                  onCategorySelect = { viewModel.selectCategory(it) },
                  onProductClick = { viewModel.selectProduct(it) },
                  onAddToCart = { viewModel.addToCart(it) },
                  onIncrease = { viewModel.addToCart(it) },
                  onDecrease = { viewModel.decreaseCartItem(it.id, it.weight) },
                  onWishlistToggle = { viewModel.toggleWishlist(it) },
                  onViewAllCategoriesClick = { viewModel.setCustomerTab(1) }
                )
              }
              1 -> {
                CategoriesScreen(
                  categories = categories,
                  products = products,
                  cartItems = cartItems,
                  wishlistIds = userProfile.wishlistProductIds,
                  selectedCategoryId = selectedCategoryId,
                  onSelectCategory = { viewModel.selectCategory(it) },
                  onProductClick = { viewModel.selectProduct(it) },
                  onAddToCart = { viewModel.addToCart(it) },
                  onIncrease = { viewModel.addToCart(it) },
                  onDecrease = { viewModel.decreaseCartItem(it.id, it.weight) },
                  onWishlistToggle = { viewModel.toggleWishlist(it) }
                )
              }
              2 -> {
                LiveTrackingScreen(
                  order = activeTrackingOrder,
                  onCancelOrder = { viewModel.cancelOrder(it) },
                  onOpenSupportChat = { viewModel.setCustomerTab(3) },
                  onShopMoreClick = { viewModel.setCustomerTab(0) }
                )
              }
              3 -> {
                CustomerProfileScreen(
                  profile = userProfile,
                  orders = orders,
                  products = products,
                  chatMessages = supportChat,
                  onToggleDarkMode = { viewModel.toggleDarkMode() },
                  onReorder = {
                    viewModel.reorder(it)
                    showCartSheet = true
                  },
                  onAddAddress = { viewModel.addAddress(it) },
                  onSetDefaultAddress = { viewModel.setDefaultAddress(it) },
                  onTrackOrder = {
                    viewModel.setActiveTrackingOrder(it)
                    viewModel.setCustomerTab(2)
                  },
                  onSendChatMessage = { viewModel.sendChatMessage(it) }
                )
              }
            }
          }
        }

        AppRole.ADMIN -> {
          AdminDashboardScreen(
            orders = orders,
            employees = employees,
            deliveryPartners = deliveryPartners,
            products = products,
            categories = categories,
            monthlyRevenue = monthlyRevenue,
            onAddEmployee = { viewModel.addEmployee(it) },
            onToggleEmployeeStatus = { viewModel.toggleEmployeeStatus(it) },
            onSaveProduct = { viewModel.saveProduct(it) },
            onDeleteProduct = { viewModel.deleteProduct(it) },
            onUpdateStock = { id, delta -> viewModel.updateStock(id, delta) }
          )
        }

        AppRole.EMPLOYEE -> {
          EmployeeDashboardScreen(
            products = products,
            orders = orders,
            onUpdateStock = { id, delta -> viewModel.updateStock(id, delta) },
            onOrderPackCompleted = { ordId ->
              viewModel.updateDeliveryPartnerStatus(ordId, OrderStatus.PACKED, 0.4f)
            }
          )
        }

        AppRole.DELIVERY_PARTNER -> {
          val activeRider = deliveryPartners.first()
          DeliveryPartnerScreen(
            rider = activeRider,
            orders = orders,
            onUpdateStatus = { ordId, st, prog ->
              viewModel.updateDeliveryPartnerStatus(ordId, st, prog)
            }
          )
        }
      }
    }
  }

  // Product Detail Bottom Sheet
  selectedProduct?.let { product ->
    ProductDetailSheet(
      product = product,
      cartItems = cartItems,
      reviews = reviews,
      similarProducts = products.filter { it.categoryId == product.categoryId && it.id != product.id },
      isWishlisted = userProfile.wishlistProductIds.contains(product.id),
      onDismiss = { viewModel.selectProduct(null) },
      onAddToCart = { prod, wt -> viewModel.addToCart(prod, wt) },
      onIncrease = { prod -> viewModel.addToCart(prod) },
      onDecrease = { prod -> viewModel.decreaseCartItem(prod.id, prod.weight) },
      onWishlistToggle = { viewModel.toggleWishlist(product.id) },
      onAddReview = { rating, comment -> viewModel.addReview(product.id, rating, comment) }
    )
  }

  // Role Switcher Modal
  if (showRoleSwitcher) {
    RoleSwitcherDialog(
      currentRole = currentRole,
      onRoleSelected = { role -> viewModel.setRole(role) },
      onDismiss = { showRoleSwitcher = false }
    )
  }

  // Address Selection Dialog
  if (showAddressPicker) {
    AlertDialog(
      onDismissRequest = { showAddressPicker = false },
      title = { Text("Select Delivery Location") },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          userProfile.addresses.forEach { addr ->
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .clickable {
                  viewModel.setDefaultAddress(addr.id)
                  showAddressPicker = false
                },
              colors = CardDefaults.cardColors(
                containerColor = if (addr.isDefault) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
              )
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = AaruhaGreenPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                  Text(addr.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                  Text(addr.fullAddress, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
              }
            }
          }
        }
      },
      confirmButton = {
        TextButton(onClick = { showAddressPicker = false }) { Text("Close") }
      }
    )
  }
}
