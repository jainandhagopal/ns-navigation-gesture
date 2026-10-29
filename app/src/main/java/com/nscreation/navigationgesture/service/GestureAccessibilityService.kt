package com.nscreation.navigationgesture.service

import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.core.graphics.toColorInt
import com.nscreation.navigationgesture.modules.gesture.data.repository.GestureRepository
import com.nscreation.navigationgesture.modules.gesture.domain.model.GestureAction
import com.nscreation.navigationgesture.utils.DisplayUtils
import kotlin.math.abs

@Suppress("AccessibilityService")
@SuppressLint("AccessibilityService")
class GestureAccessibilityService : AccessibilityService() {

    private lateinit var windowManager: WindowManager
    private var leftEdgeView: View? = null
    private var rightEdgeView: View? = null
    private var bottomEdgeView: View? = null

    private lateinit var repository: GestureRepository

    companion object {
        var isServiceRunning: Boolean = false
            private set
        const val ACTION_REFRESH_OVERLAYS = "ACTION_REFRESH_OVERLAYS"
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        isServiceRunning = true
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        repository = GestureRepository(this)
        createEdgeOverlays()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_REFRESH_OVERLAYS) {
            if (!::repository.isInitialized) {
                repository = GestureRepository(this)
            }
            removeEdgeOverlays()
            createEdgeOverlays()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        isServiceRunning = false
        removeEdgeOverlays()
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        isServiceRunning = false
        removeEdgeOverlays()
    }

    override fun onAccessibilityEvent(event: android.view.accessibility.AccessibilityEvent?) {
        // Not needed for gesture intercepting
    }

    override fun onInterrupt() {
        removeEdgeOverlays()
    }

    @SuppressLint("InflateParams")
    private fun createEdgeOverlays() {
        if (leftEdgeView != null || rightEdgeView != null || bottomEdgeView != null) return

        val edgeWidthPx = DisplayUtils.dpToPx(this, 12)
        val bottomHeightPx = DisplayUtils.dpToPx(this, 18)

        // Left Edge (Active if leftAction != NONE)
        if (repository.getLeftAction() != GestureAction.NONE) {
            leftEdgeView = FrameLayout(this).apply {
                setBackgroundColor(Color.TRANSPARENT)
                if (repository.isLeftBarVisible()) {
                    val leftIndicator = View(this@GestureAccessibilityService).apply {
                        background = GradientDrawable().apply {
                            shape = GradientDrawable.RECTANGLE
                            setColor("#60FFFFFF".toColorInt())
                            cornerRadius = DisplayUtils.dpToPx(this@GestureAccessibilityService, 2).toFloat()
                        }
                    }
                    val barWidth = DisplayUtils.dpToPx(this@GestureAccessibilityService, 4)
                    val barHeight = DisplayUtils.dpToPx(this@GestureAccessibilityService, 40)
                    val barParams = FrameLayout.LayoutParams(barWidth, barHeight).apply {
                        gravity = Gravity.START or Gravity.CENTER_VERTICAL
                        leftMargin = DisplayUtils.dpToPx(this@GestureAccessibilityService, 2)
                    }
                    addView(leftIndicator, barParams)
                }

                setOnTouchListener(object : View.OnTouchListener {
                    private var startX = 0f
                    private var startY = 0f
                    private var triggered = false

                    @SuppressLint("ClickableViewAccessibility")
                    override fun onTouch(v: View, event: MotionEvent): Boolean {
                        when (event.action) {
                            MotionEvent.ACTION_DOWN -> {
                                startX = event.rawX
                                startY = event.rawY
                                triggered = false
                                return true
                            }
                            MotionEvent.ACTION_MOVE -> {
                                if (!triggered && event.rawX - startX > DisplayUtils.dpToPx(this@GestureAccessibilityService, 25) && abs(event.rawY - startY) < DisplayUtils.dpToPx(this@GestureAccessibilityService, 150)) {
                                    triggered = true
                                    performAction(repository.getLeftAction())
                                    vibrate()
                                    return true
                                }
                            }
                        }
                        return false
                    }
                })
            }

            val leftParams = WindowManager.LayoutParams(
                edgeWidthPx,
                WindowManager.LayoutParams.MATCH_PARENT,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
                else
                    @Suppress("DEPRECATION")
                    WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT
            ).apply {
                gravity = Gravity.START or Gravity.TOP
            }

            try {
                windowManager.addView(leftEdgeView, leftParams)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Right Edge (Active if rightAction != NONE)
        if (repository.getRightAction() != GestureAction.NONE) {
            rightEdgeView = FrameLayout(this).apply {
                setBackgroundColor(Color.TRANSPARENT)
                if (repository.isRightBarVisible()) {
                    val rightIndicator = View(this@GestureAccessibilityService).apply {
                        background = GradientDrawable().apply {
                            shape = GradientDrawable.RECTANGLE
                            setColor("#60FFFFFF".toColorInt())
                            cornerRadius = DisplayUtils.dpToPx(this@GestureAccessibilityService, 2).toFloat()
                        }
                    }
                    val barWidth = DisplayUtils.dpToPx(this@GestureAccessibilityService, 4)
                    val barHeight = DisplayUtils.dpToPx(this@GestureAccessibilityService, 40)
                    val barParams = FrameLayout.LayoutParams(barWidth, barHeight).apply {
                        gravity = Gravity.END or Gravity.CENTER_VERTICAL
                        rightMargin = DisplayUtils.dpToPx(this@GestureAccessibilityService, 2)
                    }
                    addView(rightIndicator, barParams)
                }

                setOnTouchListener(object : View.OnTouchListener {
                    private var startX = 0f
                    private var startY = 0f
                    private var triggered = false

                    @SuppressLint("ClickableViewAccessibility")
                    override fun onTouch(v: View, event: MotionEvent): Boolean {
                        when (event.action) {
                            MotionEvent.ACTION_DOWN -> {
                                startX = event.rawX
                                startY = event.rawY
                                triggered = false
                                return true
                            }
                            MotionEvent.ACTION_MOVE -> {
                                if (!triggered && startX - event.rawX > DisplayUtils.dpToPx(this@GestureAccessibilityService, 25) && abs(event.rawY - startY) < DisplayUtils.dpToPx(this@GestureAccessibilityService, 150)) {
                                    triggered = true
                                    performAction(repository.getRightAction())
                                    vibrate()
                                    return true
                                }
                            }
                        }
                        return false
                    }
                })
            }

            val rightParams = WindowManager.LayoutParams(
                edgeWidthPx,
                WindowManager.LayoutParams.MATCH_PARENT,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
                else
                    @Suppress("DEPRECATION")
                    WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT
            ).apply {
                gravity = Gravity.END or Gravity.TOP
            }

            try {
                windowManager.addView(rightEdgeView, rightParams)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Bottom Edge (Active if bottomAction != NONE || bottomHoldAction != NONE)
        if (repository.getBottomAction() != GestureAction.NONE || repository.getBottomHoldAction() != GestureAction.NONE) {
            createBottomOverlay(bottomHeightPx)
        }
    }

    @SuppressLint("InflateParams")
    private fun createBottomOverlay(bottomHeightPx: Int) {
        bottomEdgeView = FrameLayout(this).apply {
            setBackgroundColor(Color.TRANSPARENT)
            if (repository.isBottomBarVisible()) {
                val bottomIndicator = View(this@GestureAccessibilityService).apply {
                    background = GradientDrawable().apply {
                        shape = GradientDrawable.RECTANGLE
                        setColor("#80FFFFFF".toColorInt())
                        cornerRadius = DisplayUtils.dpToPx(this@GestureAccessibilityService, 3).toFloat()
                    }
                }
                val indicatorWidth = DisplayUtils.dpToPx(this@GestureAccessibilityService, 80)
                val indicatorHeight = DisplayUtils.dpToPx(this@GestureAccessibilityService, 4)
                val indicatorParams = FrameLayout.LayoutParams(indicatorWidth, indicatorHeight).apply {
                    gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                    bottomMargin = DisplayUtils.dpToPx(this@GestureAccessibilityService, 4)
                }
                addView(bottomIndicator, indicatorParams)
            }

            setOnTouchListener(object : View.OnTouchListener {
                private var startX = 0f
                private var startY = 0f
                private var triggered = false
                private val holdHandler = Handler(Looper.getMainLooper())
                private var holdRunnable: Runnable? = null

                @SuppressLint("ClickableViewAccessibility")
                override fun onTouch(v: View, event: MotionEvent): Boolean {
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            startX = event.rawX
                            startY = event.rawY
                            triggered = false

                            if (repository.getBottomHoldAction() != GestureAction.NONE) {
                                holdRunnable = Runnable {
                                    if (!triggered) {
                                        triggered = true
                                        performAction(repository.getBottomHoldAction())
                                        vibrate()
                                    }
                                }
                                holdHandler.postDelayed(holdRunnable!!, 400)
                            }
                            return true
                        }
                        MotionEvent.ACTION_MOVE -> {
                            return true
                        }
                        MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                            holdRunnable?.let { holdHandler.removeCallbacks(it) }
                            if (!triggered && repository.getBottomAction() != GestureAction.NONE) {
                                if (startY - event.rawY > DisplayUtils.dpToPx(this@GestureAccessibilityService, 25)) {
                                    triggered = true
                                    performAction(repository.getBottomAction())
                                    vibrate()
                                }
                            }
                            return true
                        }
                    }
                    return false
                }
            })
        }

        val bottomParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            bottomHeightPx,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
            else
                @Suppress("DEPRECATION")
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        }

        try {
            windowManager.addView(bottomEdgeView, bottomParams)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun removeEdgeOverlays() {
        try {
            leftEdgeView?.let { windowManager.removeView(it) }
            rightEdgeView?.let { windowManager.removeView(it) }
            bottomEdgeView?.let { windowManager.removeView(it) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        leftEdgeView = null
        rightEdgeView = null
        bottomEdgeView = null
    }

    private fun performAction(action: GestureAction) {
        when (action) {
            GestureAction.BACK -> performGlobalAction(GLOBAL_ACTION_BACK)
            GestureAction.HOME -> performGlobalAction(GLOBAL_ACTION_HOME)
            GestureAction.RECENTS -> performGlobalAction(GLOBAL_ACTION_RECENTS)
            GestureAction.NOTIFICATIONS -> performGlobalAction(GLOBAL_ACTION_NOTIFICATIONS)
            GestureAction.QUICK_SETTINGS -> performGlobalAction(GLOBAL_ACTION_QUICK_SETTINGS)
            GestureAction.SPLIT_SCREEN -> performGlobalAction(GLOBAL_ACTION_TOGGLE_SPLIT_SCREEN)
            GestureAction.NONE -> {}
        }
    }

    private fun vibrate() {
        if (!repository.isHapticEnabled()) return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager
                val vibrator = vibratorManager.defaultVibrator
                vibrator.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(30)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
