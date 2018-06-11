package org.wordpress.android.ui.posts

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog.Builder
import android.support.v7.app.AppCompatDialogFragment
import android.view.ContextThemeWrapper
import org.wordpress.android.R

/**
 * Basic dialog fragment with support for 1,2 or 3 buttons.
 */
class BasicFragmentDialog : AppCompatDialogFragment() {
    private lateinit var mTag: String
    private lateinit var mTitle: String
    private lateinit var mMessage: String
    private lateinit var mPositiveButtonLabel: String
    private var mNegativeButtonLabel: String? = null
    private var mCancelButtonLabel: String? = null

    interface BasicDialogPositiveClickInterface {
        fun onPositiveClicked(instanceTag: String)
    }

    interface BasicDialogNegativeClickInterface {
        fun onNegativeClicked(instanceTag: String)
    }

    fun initialize(
        tag: String,
        title: String,
        message: String,
        positiveButtonLabel: String,
        negativeButtonLabel: String? = null,
        cancelButtonLabel: String? = null
    ) {
        mTag = tag
        mTitle = title
        mMessage = message
        mPositiveButtonLabel = positiveButtonLabel
        mNegativeButtonLabel = negativeButtonLabel
        mCancelButtonLabel = cancelButtonLabel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.isCancelable = true
        val theme = 0
        setStyle(AppCompatDialogFragment.STYLE_NORMAL, theme)

        if (savedInstanceState != null) {
            mTag = savedInstanceState.getString(STATE_KEY_TAG)
            mTitle = savedInstanceState.getString(STATE_KEY_TITLE)
            mMessage = savedInstanceState.getString(STATE_KEY_MESSAGE)
            mPositiveButtonLabel = savedInstanceState.getString(STATE_KEY_POSITIVE_BUTTON_LABEL)
            mNegativeButtonLabel = savedInstanceState.getString(STATE_KEY_NEGATIVE_BUTTON_LABEL)
            mCancelButtonLabel = savedInstanceState.getString(STATE_KEY_CANCEL_BUTTON_LABEL)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(STATE_KEY_TAG, mTag)
        outState.putString(STATE_KEY_TITLE, mTitle)
        outState.putString(STATE_KEY_MESSAGE, mMessage)
        outState.putString(STATE_KEY_POSITIVE_BUTTON_LABEL, mPositiveButtonLabel)
        outState.putString(STATE_KEY_NEGATIVE_BUTTON_LABEL, mNegativeButtonLabel)
        outState.putString(STATE_KEY_CANCEL_BUTTON_LABEL, mCancelButtonLabel)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = Builder(ContextThemeWrapper(activity, R.style.Calypso_Dialog_Alert))
        builder.setTitle(mTitle)
                .setMessage(mMessage)
                .setPositiveButton(mPositiveButtonLabel) { _, _ ->
                    val activity = activity
                    if (activity != null) {
                        (activity as BasicDialogPositiveClickInterface).onPositiveClicked(mTag)
                    }
                }.setCancelable(true)

        mNegativeButtonLabel?.let {
            builder.setNegativeButton(mNegativeButtonLabel) { _, _ ->
                val activity = activity
                if (activity != null) {
                    (activity as BasicDialogNegativeClickInterface).onNegativeClicked(mTag)
                }
            }
        }

        mCancelButtonLabel?.let {
            builder.setNeutralButton(mCancelButtonLabel) { _, _ ->
                // no-op - dialog is automatically dismissed
            }
        }
        return builder.create()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (activity !is BasicDialogPositiveClickInterface) {
            throw RuntimeException("Hosting activity must implement BasicDialogPositiveClickInterface")
        }
        if (mNegativeButtonLabel != null && activity !is BasicDialogNegativeClickInterface) {
            throw RuntimeException("Hosting activity must implement BasicDialogNegativeClickInterface")
        }
    }

    companion object {
        private const val STATE_KEY_TAG = "state_key_tag"
        private const val STATE_KEY_TITLE = "state_key_title"
        private const val STATE_KEY_MESSAGE = "state_key_message"
        private const val STATE_KEY_POSITIVE_BUTTON_LABEL = "state_key_positive_button_label"
        private const val STATE_KEY_NEGATIVE_BUTTON_LABEL = "state_key_negative_button_label"
        private const val STATE_KEY_CANCEL_BUTTON_LABEL = "state_key_cancel_button_label"
    }
}
