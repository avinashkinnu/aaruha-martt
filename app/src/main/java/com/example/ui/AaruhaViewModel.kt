package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AaruhaRepository
import com.example.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AaruhaViewModel(
  private val repository: AaruhaRepository = AaruhaRepository()
) : ViewModel() {

  val currentRole = repository.currentRole
  val userProfile = repository.userProfile
  val categories = repository.categories
  val products = repository.products
  val cart = repository.cart
  val coupons = repository.coupons
  val appliedCoupon = repository.appliedCoupon
  val orders = repository.orders
  val employees = repository.employees
  val deliveryPartners = repository.deliveryPartners
  val monthlyRevenue = repository.monthlyRevenue
  val reviews = repository.reviews
  val supportChat = repository.supportChat

  private val _searchQuery = MutableStateFlow("")
  val searchQuery = _searchQuery.asStateFlow()

  private val _selectedCategoryId = MutableStateFlow<String?>(null)
  val selectedCategoryId = _selectedCategoryId.asStateFlow()

  private val _selectedProduct = MutableStateFlow<Product?>(null)
  val selectedProduct = _selectedProduct.asStateFlow()

  private val _currentCustomerTab = MutableStateFlow(0) // 0: Home, 1: Categories, 2: Orders/Tracking, 3: Profile/Support
  val currentCustomerTab = _currentCustomerTab.asStateFlow()

  // Filtered products flow based on category and search query
  val filteredProducts: StateFlow<List<Product>> = combine(
    products,
    searchQuery,
    selectedCategoryId
  ) { list, query, catId ->
    var result = list
    if (catId != null) {
      result = result.filter { it.categoryId == catId }
    }
    if (query.isNotBlank()) {
      result = result.filter {
        it.name.contains(query, ignoreCase = true) ||
            it.categoryName.contains(query, ignoreCase = true) ||
            it.brand.contains(query, ignoreCase = true)
      }
    }
    result
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val activeTrackingOrder: StateFlow<Order?> = combine(
    orders,
    repository.activeOrderIdForTracking
  ) { list, activeId ->
    list.find { it.id == activeId } ?: list.firstOrNull { it.status != OrderStatus.DELIVERED && it.status != OrderStatus.CANCELLED } ?: list.firstOrNull()
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

  // Quick stats for Admin
  val totalRevenue = monthlyRevenue.map { list -> list.sumOf { it.revenue } }
  val totalProfit = monthlyRevenue.map { list -> list.sumOf { it.profit } }

  fun setRole(role: AppRole) {
    repository.setRole(role)
  }

  fun setCustomerTab(index: Int) {
    _currentCustomerTab.value = index
  }

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun selectCategory(catId: String?) {
    _selectedCategoryId.value = catId
  }

  fun selectProduct(product: Product?) {
    _selectedProduct.value = product
  }

  fun toggleDarkMode() {
    repository.toggleDarkMode()
  }

  fun toggleWishlist(productId: String) {
    repository.toggleWishlist(productId)
  }

  fun addToCart(product: Product, weight: String = product.weight) {
    repository.addToCart(product, weight)
  }

  fun decreaseCartItem(productId: String, weight: String) {
    repository.decreaseCartItem(productId, weight)
  }

  fun removeCartItem(productId: String, weight: String) {
    repository.removeCartItem(productId, weight)
  }

  fun clearCart() {
    repository.clearCart()
  }

  fun applyCoupon(code: String): Boolean {
    return repository.applyCoupon(code)
  }

  fun removeCoupon() {
    repository.removeCoupon()
  }

  fun addAddress(address: Address) {
    repository.addAddress(address)
  }

  fun setDefaultAddress(id: String) {
    repository.setDefaultAddress(id)
  }

  fun placeOrder(paymentMethod: String, address: Address, notes: String): Order {
    val order = repository.placeOrder(paymentMethod, address, notes)
    _currentCustomerTab.value = 2 // Switch to Orders & Live Tracking tab
    return order
  }

  fun reorder(orderId: String) {
    repository.reorder(orderId)
  }

  fun cancelOrder(orderId: String) {
    repository.cancelOrder(orderId)
  }

  fun setActiveTrackingOrder(orderId: String) {
    repository.setActiveTrackingOrder(orderId)
  }

  // Delivery Partner Actions
  fun updateDeliveryPartnerStatus(orderId: String, status: OrderStatus, progress: Float) {
    repository.updateDeliveryPartnerStatus(orderId, status, progress)
  }

  // Admin & Employee Product Management
  fun saveProduct(product: Product) {
    repository.saveProduct(product)
  }

  fun deleteProduct(productId: String) {
    repository.deleteProduct(productId)
  }

  fun updateStock(productId: String, delta: Int) {
    repository.updateStock(productId, delta)
  }

  // Admin Employee Management
  fun addEmployee(emp: Employee) {
    repository.addEmployee(emp)
  }

  fun toggleEmployeeStatus(employeeId: String) {
    repository.toggleEmployeeStatus(employeeId)
  }

  fun addReview(productId: String, rating: Int, comment: String) {
    repository.addReview(productId, rating, comment)
  }

  fun sendChatMessage(msg: String) {
    repository.sendChatMessage(msg)
  }
}
