package com.example.ui.customer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Address
import com.example.model.CartItem
import com.example.model.Coupon
import com.example.model.Order
import com.example.ui.theme.*

@Composable
fun CartAndCheckoutScreen(
  cartItems: List<CartItem>,
  appliedCoupon: Coupon?,
  availableCoupons: List<Coupon>,
  addresses: List<Address>,
  onIncrease: (CartItem) -> Unit,
  onDecrease: (CartItem) -> Unit,
  onRemove: (CartItem) -> Unit,
  onApplyCoupon: (String) -> Boolean,
  onRemoveCoupon: () -> Unit,
  onSelectAddress: (String) -> Unit,
  onAddAddressClick: () -> Unit,
  onPlaceOrder: (paymentMethod: String, address: Address, notes: String) -> Order,
  onShopMoreClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedPaymentMethod by remember { mutableStateOf("UPI (Google Pay / PhonePe)") }
  var deliveryNotes by remember { mutableStateOf("Leave near door, ring bell once") }
  var couponInputText by remember { mutableStateOf("") }
  var couponMessage by remember { mutableStateOf<String?>(null) }
  var isOrderPlacing by remember { mutableStateOf(false) }

  val selectedAddress = addresses.firstOrNull { it.isDefault } ?: addresses.firstOrNull() ?: Address(
    id = "addr_default",
    title = "Home",
    fullAddress = "Flat 402, Sai Balaji Residency, Kavuri Hills",
    landmark = "Near D-Mart",
    city = "Hyderabad",
    pincode = "500081",
    isDefault = true
  )

  val itemsTotal = cartItems.sumOf { it.product.sellingPrice * it.quantity }
  val discount = if (appliedCoupon != null && itemsTotal >= appliedCoupon.minOrderValue) {
    val calc = (itemsTotal * appliedCoupon.discountPercent) / 100.0
    minOf(calc, appliedCoupon.maxDiscount)
  } else 0.0

  val deliveryFee = if (itemsTotal > 199.0 || appliedCoupon?.code == "FREEDEL") 0.0 else 25.0
  val finalTotal = maxOf(0.0, itemsTotal - discount + deliveryFee)

  if (cartItems.isEmpty()) {
    Box(
      modifier = modifier
        .fillMaxSize()
        .padding(24.dp)
        .testTag("empty_cart_view"),
      contentAlignment = Alignment.Center
    ) {
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
          modifier = Modifier
            .size(100.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant),
          contentAlignment = Alignment.Center
        ) {
          Text(text = "🛒", fontSize = 50.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
          text = "Your Cart is Empty",
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "Add fresh groceries, fruits, milk & snacks to get them delivered in 10 minutes!",
          fontSize = 13.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
          onClick = onShopMoreClick,
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
          modifier = Modifier.testTag("empty_cart_browse_btn")
        ) {
          Text("Browse Groceries", fontWeight = FontWeight.Bold)
        }
      }
    }
  } else {
    LazyColumn(
      modifier = modifier
        .fillMaxSize()
        .testTag("cart_checkout_screen"),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // 10-Minute Delivery Guarantee Header
      item {
        Card(
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(AaruhaYellowAccent),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.Bolt, contentDescription = null, tint = Color.Black)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = "Instant Delivery in 8-10 Mins",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
              )
              Text(
                text = "Dispatching directly from Kavuri Hills Dark Store",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
              )
            }
          }
        }
      }

      // Cart Items List
      item {
        Text(
          text = "Review Items (${cartItems.size})",
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold
        )
      }

      items(cartItems) { item ->
        Card(
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          border = CardDefaults.outlinedCardBorder(),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.weight(1f)
            ) {
              Box(
                modifier = Modifier
                  .size(52.dp)
                  .clip(RoundedCornerShape(8.dp))
                  .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
              ) {
                Text(text = item.product.emoji, fontSize = 28.sp)
              }
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Text(
                  text = item.product.name,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.SemiBold,
                  maxLines = 1
                )
                Text(
                  text = item.selectedWeight,
                  fontSize = 11.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                  text = "₹${(item.product.sellingPrice * item.quantity).toInt()}",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.primary
                )
              }
            }

            // Stepper
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              IconButton(
                onClick = { onDecrease(item) },
                modifier = Modifier.size(24.dp)
              ) {
                Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(14.dp))
              }
              Text(
                text = item.quantity.toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                modifier = Modifier.padding(horizontal = 6.dp)
              )
              IconButton(
                onClick = { onIncrease(item) },
                modifier = Modifier.size(24.dp)
              ) {
                Icon(Icons.Default.Add, contentDescription = "Increase", tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(14.dp))
              }
            }
          }
        }
      }

      // Coupons Section
      item {
        Card(
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          border = CardDefaults.outlinedCardBorder(),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocalOffer, contentDescription = null, tint = AaruhaGreenPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Coupons & Offers", fontWeight = FontWeight.Bold, fontSize = 14.sp)
              }
              if (appliedCoupon != null) {
                TextButton(onClick = onRemoveCoupon) {
                  Text("Remove", color = DiscountRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
              }
            }

            if (appliedCoupon != null) {
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(8.dp))
                  .background(SuccessGreenBg)
                  .padding(10.dp)
              ) {
                Text(
                  text = "✓ '${appliedCoupon.code}' applied! You save ₹${discount.toInt()}",
                  color = SuccessGreen,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            } else {
              Spacer(modifier = Modifier.height(8.dp))
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
              ) {
                OutlinedTextField(
                  value = couponInputText,
                  onValueChange = { couponInputText = it },
                  placeholder = { Text("Enter coupon code", fontSize = 12.sp) },
                  modifier = Modifier.weight(1f),
                  singleLine = true,
                  shape = RoundedCornerShape(8.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                  onClick = {
                    val success = onApplyCoupon(couponInputText)
                    couponMessage = if (success) "Coupon applied successfully!" else "Invalid coupon"
                  },
                  shape = RoundedCornerShape(8.dp)
                ) {
                  Text("Apply")
                }
              }
              if (couponMessage != null) {
                Text(
                  text = couponMessage ?: "",
                  fontSize = 11.sp,
                  color = if (appliedCoupon != null) SuccessGreen else DiscountRed,
                  modifier = Modifier.padding(top = 4.dp)
                )
              }
            }

            // Quick coupon chips
            Spacer(modifier = Modifier.height(10.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              items(availableCoupons) { cp ->
                OutlinedButton(
                  onClick = { onApplyCoupon(cp.code) },
                  shape = RoundedCornerShape(8.dp),
                  contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                ) {
                  Text("${cp.code} (${cp.title})", fontSize = 11.sp)
                }
              }
            }
          }
        }
      }

      // Delivery Address Selector
      item {
        Card(
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          border = CardDefaults.outlinedCardBorder(),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = AaruhaGreenPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Delivery Address", fontWeight = FontWeight.Bold, fontSize = 14.sp)
              }
              TextButton(onClick = onAddAddressClick) {
                Text("+ New Address", fontSize = 12.sp)
              }
            }

            Text(
              text = "${selectedAddress.title} • ${selectedAddress.fullAddress}",
              fontSize = 13.sp,
              fontWeight = FontWeight.Medium,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = "Landmark: ${selectedAddress.landmark}, ${selectedAddress.city} - ${selectedAddress.pincode}",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
              value = deliveryNotes,
              onValueChange = { deliveryNotes = it },
              placeholder = { Text("Delivery instructions (e.g. Leave at door)", fontSize = 11.sp) },
              modifier = Modifier.fillMaxWidth(),
              singleLine = true,
              shape = RoundedCornerShape(8.dp)
            )
          }
        }
      }

      // Payment Method Options
      item {
        Card(
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          border = CardDefaults.outlinedCardBorder(),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text("Select Payment Method", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))

            val methods = listOf(
              "UPI (Google Pay / PhonePe / Paytm)" to "Instant 1-click payment",
              "Razorpay / Credit & Debit Cards" to "Visa, Mastercard, RuPay & NetBanking",
              "Cash on Delivery (COD)" to "Pay cash or scan QR upon delivery"
            )

            methods.forEach { (method, desc) ->
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(8.dp))
                  .clickable { selectedPaymentMethod = method }
                  .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                RadioButton(
                  selected = selectedPaymentMethod == method,
                  onClick = { selectedPaymentMethod = method }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                  Text(text = method, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                  Text(text = desc, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
              }
            }
          }
        }
      }

      // Bill Summary Details
      item {
        Card(
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Bill Details", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
              Text("Item Total", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text("₹${itemsTotal.toInt()}", fontSize = 13.sp)
            }
            if (discount > 0) {
              Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Coupon Discount", fontSize = 13.sp, color = SuccessGreen)
                Text("-₹${discount.toInt()}", fontSize = 13.sp, color = SuccessGreen, fontWeight = FontWeight.Bold)
              }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
              Text("Delivery Partner Fee", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              if (deliveryFee == 0.0) {
                Text("FREE", fontSize = 13.sp, color = SuccessGreen, fontWeight = FontWeight.Bold)
              } else {
                Text("₹${deliveryFee.toInt()}", fontSize = 13.sp)
              }
            }
            HorizontalDivider()
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text("To Pay", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text("Includes all taxes", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
              Text(
                text = "₹${finalTotal.toInt()}",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
              )
            }
          }
        }
      }

      // Place Order Button
      item {
        Button(
          onClick = {
            isOrderPlacing = true
            onPlaceOrder(selectedPaymentMethod, selectedAddress, deliveryNotes)
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .testTag("place_order_button"),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text("₹${finalTotal.toInt()}", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
              Text("TOTAL", fontSize = 10.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text("Place Order ⚡", fontWeight = FontWeight.Bold, fontSize = 15.sp)
              Spacer(modifier = Modifier.width(6.dp))
              Icon(Icons.Default.ArrowForward, contentDescription = null)
            }
          }
        }
      }
    }
  }
}
