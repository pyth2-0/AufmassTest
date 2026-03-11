package com.aufmass.app.ui.components

import android.text.InputType
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.EditText
import android.widget.LinearLayout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun StylusEditText(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    inputType: Int = InputType.TYPE_CLASS_TEXT,
    imeAction: Int = android.view.inputmethod.EditorInfo.IME_ACTION_NEXT,
    allowComma: Boolean = false,
    onImeNext: (() -> Unit)? = null,
    onImeDone: (() -> Unit)? = null,
    onViewCreated: ((EditText) -> Unit)? = null,
    onFocus: ((Boolean) -> Unit)? = null
) {
    var currentText by remember { mutableStateOf(value) }
    val context = LocalContext.current

    AndroidView(
        factory = { ctx ->
            EditText(ctx).apply {
                hint = label
                setText(value)
                currentText = value
                this.inputType = if (allowComma) {
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_NUMBER_FLAG_DECIMAL
                } else inputType
                if (allowComma) {
                    filters = arrayOf(InputFilter { source, start, end, _, _, _ ->
                        val sb = StringBuilder()
                        for (i in start until end) {
                            val c = source[i]
                            if (c.isDigit() || c == ',' || c == '.') {
                                sb.append(c)
                            }
                        }
                        if (sb.isNotEmpty() && sb.toString() != source.subSequence(start, end).toString()) {
                            sb.toString().replace(".", ",")
                        } else if (sb.isEmpty()) {
                            ""
                        } else {
                            null
                        }
                    })
                }
                imeOptions = imeAction
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 8, 0, 8)
                }
                setOnEditorActionListener { v, actionId, _ ->
                    when (actionId) {
                        android.view.inputmethod.EditorInfo.IME_ACTION_NEXT -> {
                            onImeNext?.invoke()
                            true
                        }
                        android.view.inputmethod.EditorInfo.IME_ACTION_DONE -> {
                            v.clearFocus()
                            onImeDone?.invoke()
                            true
                        }
                        else -> false
                    }
                }
                addTextChangedListener(object : TextWatcher {
                    private var isUpdating = false
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                    override fun afterTextChanged(s: Editable?) {
                        if (isUpdating) return
                        isUpdating = true
                        val input = s?.toString() ?: ""
                        val transformed = if (allowComma) {
                            input.filter { it.isDigit() || it == ',' }
                        } else {
                            input
                        }
                        if (transformed != input) {
                            val selection = selectionStart
                            setText(transformed)
                            setSelection(minOf(selection, transformed.length))
                        }
                        currentText = transformed
                        onValueChange(currentText)
                        isUpdating = false
                    }
                })
                onViewCreated?.invoke(this)
                onFocus?.let { callback ->
                    setOnFocusChangeListener { _, hasFocus ->
                        callback(hasFocus)
                    }
                }
                applyDarkModeStyle(context)
            }
        },
        update = { editText ->
            if (editText.text.toString() != value) {
                editText.setText(value)
                editText.setSelection(value.length)
                currentText = value
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp)
    )
}

private fun EditText.applyDarkModeStyle(context: android.content.Context) {
    val typedArray = context.theme.obtainStyledAttributes(
        intArrayOf(
            android.R.attr.colorBackground,
            android.R.attr.textColor,
            android.R.attr.textColorHint
        )
    )
    try {
        val bgColor = typedArray.getColor(0, android.graphics.Color.WHITE)
        val textColor = typedArray.getColor(1, android.graphics.Color.BLACK)
        val hintColor = typedArray.getColor(2, android.graphics.Color.GRAY)
        setBackgroundColor(bgColor)
        setTextColor(textColor)
        setHintTextColor(hintColor)
    } finally {
        typedArray.recycle()
    }
}
