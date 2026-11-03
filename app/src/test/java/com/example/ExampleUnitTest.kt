package com.example

import com.example.data.AaruhaRepository
import com.example.model.Address
import com.example.model.OrderStatus
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun testAaruhaRepositoryCartAndOrder() {
    val repo = AaruhaRepository()
    val product = repo.products.value.first()

    // Test add to cart
    val initialCartSize = repo.cart.value.size
    repo.addToCart(product, "1 kg")
    assertTrue(repo.cart.value.any { it.product.id == product.id && it.selectedWeight == "1 kg" })

    // Test coupon application
    val validCoupon = repo.applyCoupon("AARUHA50")
    assertTrue(validCoupon)
    assertEquals("AARUHA50", repo.appliedCoupon.value?.code)

    // Test order placement
    val dummyAddr = Address("t1", "Home", "Plot 12", "Nearby", "Hyderabad", "500081", true)
    val order = repo.placeOrder("UPI / Google Pay", dummyAddr, "Ring bell")
    assertNotNull(order)
    assertTrue(order.id.startsWith("AG-"))
    assertEquals(OrderStatus.CONFIRMED, order.status)
    assertEquals(0, repo.cart.value.size) // Cart cleared after checkout
  }
}

