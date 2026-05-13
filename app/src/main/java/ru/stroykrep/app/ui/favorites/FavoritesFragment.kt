package ru.stroykrep.app.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.launch
import ru.stroykrep.app.R
import ru.stroykrep.app.app
import ru.stroykrep.app.databinding.FragmentFavoritesBinding
import ru.stroykrep.app.ui.common.ProductAdapter
import ru.stroykrep.app.ui.common.SpaceItemDecoration

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val nav = findNavController()
        binding.btnBack.setOnClickListener { nav.navigateUp() }

        val adapter = ProductAdapter { product ->
            nav.navigate(
                R.id.action_global_productFragment,
                bundleOf("productId" to product.id)
            )
        }
        binding.rvFavorites.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            this.adapter = adapter
            addItemDecoration(SpaceItemDecoration(16, spanCount = 2))
        }

        val ctx = requireContext()
        val userId = ctx.app.sessionManager.getCurrentUserId()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                ctx.app.shopRepository.observeFavorites(userId).collect { list ->
                    adapter.submitList(list)
                    binding.txtEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
