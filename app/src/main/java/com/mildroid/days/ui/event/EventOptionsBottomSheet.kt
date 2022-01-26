package com.mildroid.days.ui.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.divider.MaterialDivider
import com.mildroid.days.R
import com.mildroid.days.databinding.BottomSheetEventOptionsBinding
import com.mildroid.days.utils.*

class EventOptionsBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetEventOptionsBinding? = null
    private val binding: BottomSheetEventOptionsBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetEventOptionsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.eventOptionsOperationArea.run {
            addView(
                verticalLayout(R.string.edit_title, R.drawable.ic_paper_24dp) {

                }
            )

            addView(
                verticalLayout(R.string.edit_date, R.drawable.ic_calendar_24dp) {

                }
            )

            addView(
                verticalLayout(R.string.change_image, R.drawable.ic_image_24dp) {

                }
            )

            addView(
                MaterialDivider(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT

                    ).also {
                        it.setMargins(dp32, dp(8), dp32, dp(8))
                    }
                }
            )

            addView(
                verticalLayout(R.string.archive, R.drawable.ic_folder_24dp) {

                }
            )

            addView(
                verticalLayout(
                    R.string.delete,
                    R.drawable.ic_delete_24dp,
                    R.color.red
                ) {

                }
            )
        }
    }

    override fun getTheme() = R.style.RoundedBottomSheetDialog

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}