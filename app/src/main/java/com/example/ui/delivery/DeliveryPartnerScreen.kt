package com.example.ui.delivery

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DeliveryPartner
import com.example.model.Order
import com.example.model.OrderStatus
import com.example.ui.theme.*

@Composable
fun DeliveryPartnerScreen(
  rider: DeliveryPartner,
  orders: List<Order>,
  onUpdateStatus: (orderId: String, nextStatus: OrderStatus, progress: Float) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var inputOtp by remember { mutableStateOf("") }
  var showOtpError by remember { mutableStateOf(false) }

  // Assigned active order
  val activeOrder = orders.find { it.status != OrderStatus.DELIVERED && it.status != OrderStatus.CANCELLED }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("delivery_partner_screen"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Rider Header
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(16.dp)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(AaruhaYellowAccent),
              contentAlignment = Alignment.Center
            ) {
              Text("🛵", fontSize = 28.sp)
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = rider.name,
                  color = Color.White,
                  fontSize = 18.sp,
                  fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "★ ${rider.rating}",
                  color = AaruhaYellowAccent,
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp
                )
              }
              Text(
                text = "${rider.vehicleType} (${rider.vehicleNumber})",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 12.sp
              )
              Text(
                text = "Partner ID: ${rider.id} • Active Duty",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 11.sp
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Today's Earnings & Deliveries Pill
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(10.dp))
              .background(Color.White.copy(alpha = 0.15f))
              .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text("Today's Payout", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
              Text("₹${rider.todayEarnings.toInt()}", color = AaruhaYellowAccent, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
              Text("Deliveries Completed", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
              Text("${rider.totalDeliveries} Lifetime", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
          }
        }
      }
    }

    // Active Assigned Order Card
    if (activeOrder == null) {
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          border = CardDefaults.outlinedCardBorder(),
          shape = RoundedCornerShape(14.dp)
        ) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(32.dp),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text("🎯", fontSize = 36.sp)
              Spacer(modifier = Modifier.height(8.dp))
              Text("No Pending Deliveries", fontWeight = FontWeight.Bold)
              Text("Stay near Madhapur Dark Store for automatic dispatch assignment.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
          }
        }
      }
    } else {
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          border = CardDefaults.outlinedCardBorder(),
          shape = RoundedCornerShape(16.dp)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text("Assigned Delivery #${activeOrder.id}", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                Text("ETA: ${activeOrder.etaMinutes} mins • Distance: 1.2 km", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
              }
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(AaruhaYellowContainer)
                  .padding(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Text(
                  text = activeOrder.status.title,
                  color = Color(0xFF92400E),
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Destination address
            Card(
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(10.dp)) {
                Text("Deliver To: Avinash Kavuri", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text(activeOrder.deliveryAddress.fullAddress, fontSize = 12.sp)
                Text("Notes: ${activeOrder.customerNotes}", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Communication buttons (Call / WhatsApp Customer)
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Button(
                onClick = {
                  val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+919848022334"))
                  context.startActivity(intent)
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
              ) {
                Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Call Customer", fontSize = 11.sp)
              }

              Button(
                onClick = {
                  val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://wa.me/919848022334?text=Hello%20Avinash,%20this%20is%20Ramesh%20from%20Aaruha%20Groceries.%20I%20am%20arriving%20with%20your%20order!")
                  )
                  context.startActivity(intent)
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366))
              ) {
                Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("WhatsApp", fontSize = 11.sp)
              }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(14.dp))

            // Step Action Buttons for Delivery Workflow
            Text("Update Delivery Status:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(8.dp))

            when (activeOrder.status) {
              OrderStatus.PLACED, OrderStatus.CONFIRMED -> {
                Button(
                  onClick = { onUpdateStatus(activeOrder.id, OrderStatus.PACKED, 0.35f) },
                  modifier = Modifier.fillMaxWidth(),
                  shape = RoundedCornerShape(8.dp)
                ) {
                  Text("Step 1: Reached Dark Store")
                }
              }
              OrderStatus.PACKED -> {
                Button(
                  onClick = { onUpdateStatus(activeOrder.id, OrderStatus.PICKED_UP, 0.50f) },
                  modifier = Modifier.fillMaxWidth(),
                  shape = RoundedCornerShape(8.dp),
                  colors = ButtonDefaults.buttonColors(containerColor = AaruhaGreenPrimary)
                ) {
                  Text("Step 2: Collect & Pick Up Package")
                }
              }
              OrderStatus.PICKED_UP -> {
                Button(
                  onClick = { onUpdateStatus(activeOrder.id, OrderStatus.OUT_FOR_DELIVERY, 0.75f) },
                  modifier = Modifier.fillMaxWidth(),
                  shape = RoundedCornerShape(8.dp),
                  colors = ButtonDefaults.buttonColors(containerColor = AaruhaGreenLight)
                ) {
                  Text("Step 3: Start Ride (Out For Delivery)")
                }
              }
              OrderStatus.OUT_FOR_DELIVERY -> {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                  Text(
                    text = "Customer will provide 4-digit Delivery OTP (Hint: ${activeOrder.deliveryOtp})",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                  OutlinedTextField(
                    value = inputOtp,
                    onValueChange = {
                      inputOtp = it
                      showOtpError = false
                    },
                    label = { Text("Enter Customer OTP") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    isError = showOtpError,
                    singleLine = true
                  )
                  if (showOtpError) {
                    Text("Incorrect OTP. Please ask customer for the 4-digit code.", color = DiscountRed, fontSize = 11.sp)
                  }
                  Button(
                    onClick = {
                      if (inputOtp.trim() == activeOrder.deliveryOtp) {
                        onUpdateStatus(activeOrder.id, OrderStatus.DELIVERED, 1.0f)
                        Toast.makeText(context, "Order Delivered Successfully! ₹45 credited.", Toast.LENGTH_LONG).show()
                        inputOtp = ""
                      } else {
                        showOtpError = true
                      }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
                  ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Verify OTP & Complete Delivery")
                  }
                }
              }
              else -> {}
            }
          }
        }
      }
    }
  }
}
