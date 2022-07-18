package com.a90ms.searchbook.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import com.a90ms.searchbook.R
import com.a90ms.searchbook.base.BaseActivity
import com.a90ms.searchbook.databinding.ActivityDetailBinding
import com.a90ms.searchbook.model.Items
import com.a90ms.searchbook.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookDetailActivity : BaseActivity<ActivityDetailBinding>(R.layout.activity_detail) {

    private val viewModel by viewModels<BookDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupData()
        setupLoadingObserver(viewModel)
        setupObserver()
        setupListener()
    }

    private fun setupData() {
        binding.vm = viewModel
    }

    private fun setupObserver() {
        val owner = this
        viewModel.run {
            state.observe(owner) {
                when (it) {
                    is BookDetailState.OnClickBuyLink -> {
                        if (it.url.isNullOrEmpty()) {
                            toast(getString(R.string.empty_link))
                        } else {
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it.url)))
                        }
                    }
                }
            }
        }
    }

    private fun setupListener() {
        binding.mtbDetail.setNavigationOnClickListener {
            finish()
        }
    }

    companion object {
        const val BUNDLE_ITEMS = "items"
        fun createBundle(item: Items) = bundleOf(BUNDLE_ITEMS to item)
    }
}