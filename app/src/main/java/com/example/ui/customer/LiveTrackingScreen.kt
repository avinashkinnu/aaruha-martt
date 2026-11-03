package com.example.ui.customer

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Order
import com.example.model.OrderStatus
import com.example.ui.theme.*

@Composable
fun LiveTrackingScreen(
  order: Order?,
  onCancelOrder: (String) -> Unit,
  onOpenSupportChat: () -> Unit,
  onShopMoreClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current

  if (order == null) {
    Box(
      modifier = modifier
        .fillMaxSize()
        .padding(24.dp)
        .testTag("no_active_orders_view"),
      contentAlignment = Alignment.Center
    ) {
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
          modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant),
          contentAlignment = Alignment.Center
        ) {
          Icon(Icons.Default.Moped, contentDescription = null, tint = AaruhaGreenPrimary, modifier = Modifier.size(40.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("No Active Deliveries", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          "You don't have any ongoing deliveries right now.",
          fontSize = 13.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onShopMoreClick) {
          Text("Order Fresh Groceries")
        }
      }
    }
  } else {
    LazyColumn(
      modifier = modifier
        .fillMaxSize()
        .testTag("live_tracking_screen"),
      contentPadding = PaddingValues(bottom = 90.dp)
    ) {
      // Top Live Tracking Banner & ETA
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
          shape = RoundedCornerShape(16.dp)
        ) {
          Column(
            modifier = Modifier.padding(18.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(AaruhaYellowAccent)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "LIVE ORDER TRACKING",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = AaruhaYellowAccent,
                  letterSpacing = 0.5.sp
                )
              }
              Text(
                text = "Order #${order.id}",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
              )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
              text = if (order.status == OrderStatus.DELIVERED) "Order Delivered!" else "Arriving in ${order.etaMinutes} mins ⚡",
              color = Color.White,
              fontSize = 24.sp,
              fontWeight = FontWeight.ExtraBold
            )

            Text(
              text = order.status.subtitle,
              color = Color.White.copy(alpha = 0.9f),
              fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Delivery OTP Pill
            Row(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White.copy(alpha = 0.2f))
                .padding(horizontal = 10.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Delivery OTP: ",
                color = Color.White,
                fontSize = 12.sp
              )
              Text(
                text = order.deliveryOtp,
                color = AaruhaYellowAccent,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp,
                letterSpacing = 2.sp
              )
            }
          }
        }
      }

      // Interactive Simulated Map with Route & Rider GPS
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          border = CardDefaults.outlinedCardBorder(),
          shape = RoundedCornerShape(16.dp)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Navigation, contentDescription = null, tint = AaruhaGreenPrimary, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Google Maps Live Route", fontWeight = FontWeight.Bold, fontSize = 13.sp)
              }
              Text(
                text = "1.2 km away",
                color = AaruhaGreenPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
              )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Canvas Map Route View
            LiveMapRouteCanvas(progress = order.trackingProgress)

            Spacer(modifier = Modifier.height(8.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = "🏪 Aaruha Dark Store (Madhapur)",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Text(
                text = "🏠 Your Doorstep",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
              )
            }
          }
        }
      }

      // Order Status Progression Timeline
      item {
        Spacer(modifier = Modifier.height(14.dp))
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          border = CardDefaults.outlinedCardBorder(),
          shape = RoundedCornerShape(16.dp)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text("Order Progress", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(14.dp))

            val stages = listOf(
              OrderStatus.PLACED,
              OrderStatus.CONFIRMED,
              OrderStatus.PACKED,
              OrderStatus.PICKED_UP,
              OrderStatus.OUT_FOR_DELIVERY,
              OrderStatus.DELIVERED
            )

            stages.forEachIndexed { index, st ->
              val isCompleted = order.status.stepIndex >= st.stepIndex
              val isCurrent = order.status == st

              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
              ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  Box(
                    modifier = Modifier
                      .size(24.dp)
                      .clip(CircleShape)
                      .background(
                        when {
                          isCompleted -> SuccessGreen
                          isCurrent -> AaruhaYellowAccent
                          else -> Color.LightGray.copy(alpha = 0.5f)
                        }
                      ),
                    contentAlignment = Alignment.Center
                  ) {
                    if (isCompleted) {
                      Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                    } else if (isCurrent) {
                      Box(
                        modifier = Modifier
                          .size(8.dp)
                          .clip(CircleShape)
                          .background(Color.Black)
                      )
                    }
                  }
                  if (index < stages.size - 1) {
                    Box(
                      modifier = Modifier
                        .width(2.dp)
                        .height(26.dp)
                        .background(if (order.status.stepIndex > st.stepIndex) SuccessGreen else Color.LightGray.copy(alpha = 0.4f))
                    )
                  }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                  Text(
                    text = st.title,
                    fontSize = 13.sp,
                    fontWeight = if (isCurrent || isCompleted) FontWeight.Bold else FontWeight.Normal,
                    color = if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                  )
                  Text(
                    text = st.subtitle,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }
            }
          }
        }
      }

      // Delivery Partner Details Card & Call / WhatsApp buttons
      item {
        Spacer(modifier = Modifier.height(14.dp))
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          border = CardDefaults.outlinedCardBorder(),
          shape = RoundedCornerShape(16.dp)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text("Delivery Partner", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(10.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(46.dp)
                  .clip(CircleShape)
                  .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
              ) {
                Text(text = "🛵", fontSize = 24.sp)
              }
              Spacer(modifier = Modifier.width(12.dp))
              Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = order.deliveryPartnerName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "★ 4.9",
                    color = Color(0xFFD97706),
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                  )
                }
                Text(
                  text = "Hero Electric • TS 09 EA 4582",
                  fontSize = 11.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                  text = "Vaccinated & temperature verified",
                  fontSize = 10.sp,
                  color = SuccessGreen
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons: Call, WhatsApp, Support Chat
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              // Call Partner
              Button(
                onClick = {
                  val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${order.deliveryPartnerPhone}"))
                  context.startActivity(intent)
                },
                modifier = Modifier
                  .weight(1f)
                  .testTag("call_partner_btn"),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
              ) {
                Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Call", fontSize = 12.sp)
              }

              // WhatsApp Partner
              Button(
                onClick = {
                  val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://wa.me/919845012345?text=Hello%20Ramesh,%20checking%20my%20Aaruha%20Groceries%20Order%20${order.id}")
                  )
                  context.startActivity(intent)
                },
                modifier = Modifier
                  .weight(1f)
                  .testTag("whatsapp_partner_btn"),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366))
              ) {
                Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("WhatsApp", fontSize = 12.sp)
              }

              // Support Chat
              OutlinedButton(
                onClick = onOpenSupportChat,
                modifier = Modifier
                  .weight(1f)
                  .testTag("help_support_btn"),
                shape = RoundedCornerShape(8.dp)
              ) {
                Icon(Icons.Default.SupportAgent, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Help", fontSize = 12.sp)
              }
            }
          }
        }
      }

      // Order Items Breakdown Card
      item {
        Spacer(modifier = Modifier.height(14.dp))
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          border = CardDefaults.outlinedCardBorder(),
          shape = RoundedCornerShape(16.dp)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text("Items in this delivery (${order.items.size})", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            order.items.forEach { itm ->
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(itm.emoji, fontSize = 18.sp)
                  Spacer(modifier = Modifier.width(8.dp))
                  Column {
                    Text(itm.productName, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    Text("${itm.weight} × ${itm.quantity}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                  }
                }
                Text("₹${(itm.price * itm.quantity).toInt()}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
              }
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Total Paid (${order.paymentMethod})", fontWeight = FontWeight.Bold, fontSize = 13.sp)
              Text("₹${order.finalAmount.toInt()}", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
            }
          }
        }
      }
    }
  }
}

@Composable
private fun LiveMapRouteCanvas(progress: Float) {
  val infiniteTransition = rememberInfiniteTransition(label = "map_radar")
  val pulseRadius by infiniteTransition.animateFloat(
    initialValue = 10f,
    targetValue = 26f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "pulse"
  )

  Canvas(
    modifier = Modifier
      .fillMaxWidth()
      .height(140.dp)
      .clip(RoundedCornerShape(12.dp))
      .background(Color(0xFFE2E8F0))
  ) {
    val startX = 40.dp.toPx()
    val startY = size.height * 0.75f
    val endX = size.width - 40.dp.toPx()
    val endY = size.height * 0.25f

    // Draw Map Grid Roads
    drawLine(
      color = Color.White,
      start = Offset(0f, size.height * 0.5f),
      end = Offset(size.width, size.height * 0.5f),
      strokeWidth = 14f
    )
    drawLine(
      color = Color.White,
      start = Offset(size.width * 0.5f, 0f),
      end = Offset(size.width * 0.5f, size.height),
      strokeWidth = 14f
    )

    // Route curve from store to customer
    val path = androidx.compose.ui.graphics.Path().apply {
      moveTo(startX, startY)
      cubicTo(
        startX + (endX - startX) * 0.3f, startY - 40f,
        startX + (endX - startX) * 0.7f, endY + 40f,
        endX, endY
      )
    }

    // Planned Route Line
    drawPath(
      path = path,
      color = Color(0xFF10B981),
      style = Stroke(
        width = 8f,
        cap = StrokeCap.Round,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(16f, 10f))
      )
    )

    // Dark Store Pin (Start)
    drawCircle(
      color = Color(0xFF0C733F),
      radius = 12f,
      center = Offset(startX, startY)
    )
    drawCircle(
      color = Color.White,
      radius = 5f,
      center = Offset(startX, startY)
    )

    // Customer Home Pin (End)
    drawCircle(
      color = Color(0xFFDC2626),
      radius = 14f,
      center = Offset(endX, endY)
    )
    drawCircle(
      color = Color.White,
      radius = 6f,
      center = Offset(endX, endY)
    )

    // Current Rider Position along progress
    val riderX = startX + (endX - startX) * progress.coerceIn(0f, 1f)
    val riderY = startY + (endY - startY) * progress.coerceIn(0f, 1f)

    // Radar pulse wave
    drawCircle(
      color = Color(0xFF0C733F).copy(alpha = 0.35f),
      radius = pulseRadius,
      center = Offset(riderX, riderY)
    )

    // Rider Scooter Dot
    drawCircle(
      color = Color(0xFFFFB800),
      radius = 16f,
      center = Offset(riderX, riderY)
    )
    drawCircle(
      color = Color.Black,
      radius = 7f,
      center = Offset(riderX, riderY)
    )
  }
}
