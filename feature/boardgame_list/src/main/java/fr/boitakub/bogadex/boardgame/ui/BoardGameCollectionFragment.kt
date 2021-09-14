package fr.boitakub.bogadex.boardgame.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import fr.boitakub.boardgame_list.R
import fr.boitakub.bogadex.boardgame.BoardGameCollectionRepository
import fr.boitakub.bogadex.boardgame.ui.BoardGameCollectionListAdapter.Companion.SPAN_COUNT_ONE
import fr.boitakub.bogadex.boardgame.ui.BoardGameCollectionListAdapter.Companion.SPAN_COUNT_THREE
import fr.boitakub.bogadex.boardgame.ui.BoardGameCollectionViewModel.Companion.provideFactory
import fr.boitakub.bogadex.boardgame.usecase.ListCollection
import fr.boitakub.bogadex.boardgame.usecase.ListCollectionItemOwned
import fr.boitakub.bogadex.boardgame.usecase.ListCollectionItemWanted
import fr.boitakub.common.databinding.CommonListFragmentBinding
import javax.inject.Inject

@AndroidEntryPoint
class BoardGameCollectionFragment :
    Fragment(),
    fr.boitakub.clean_architecture.View<BoardGameCollectionViewModel> {

    @Inject
    lateinit var repository: BoardGameCollectionRepository

    @Inject
    lateinit var imageLoaderViewModelFactory: BoardGameCollectionViewModel.BoardGameCollectionViewModelFactory

    lateinit var binding: CommonListFragmentBinding
    override val presenter: BoardGameCollectionViewModel by viewModels {
        provideFactory(
            imageLoaderViewModelFactory,
            getCollection(arguments?.getString("filter"))
        )
    }
    private lateinit var adapter: BoardGameCollectionListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CommonListFragmentBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        adapter =
            BoardGameCollectionListAdapter((binding.recyclerView.layoutManager as GridLayoutManager))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = adapter

        presenter.gameList.observe(
            viewLifecycleOwner,
            {
                adapter.setItems(it)
            }
        )

        presenter.errorMessage.observe(
            viewLifecycleOwner,
            {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        )

        presenter.loading.observe(
            viewLifecycleOwner,
            Observer {
                if (it) {
                    binding.pbLoading.visibility = View.VISIBLE
                } else {
                    binding.pbLoading.visibility = View.GONE
                }
            }
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_switch_layout) {
            switchLayout()
            switchIcon(item)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun switchLayout() {
        if ((binding.recyclerView.layoutManager as GridLayoutManager).spanCount == SPAN_COUNT_ONE) {
            (binding.recyclerView.layoutManager as GridLayoutManager).spanCount = SPAN_COUNT_THREE
        } else {
            (binding.recyclerView.layoutManager as GridLayoutManager).spanCount = SPAN_COUNT_ONE
        }
        adapter.notifyItemRangeChanged(0, adapter.itemCount)
    }

    private fun getCollection(string: String?): ListCollection {
        return when (string) {
            "collection" -> ListCollectionItemOwned(repository)
            "wishlist" -> ListCollectionItemWanted(repository)
            else -> { // Note the block
                ListCollection(repository)
            }
        }
    }

    private fun switchIcon(item: MenuItem) {
        if ((binding.recyclerView.layoutManager as GridLayoutManager).spanCount == SPAN_COUNT_THREE) {
            item.icon = resources.getDrawable(R.drawable.ic_span_3)
        } else {
            item.icon = resources.getDrawable(R.drawable.ic_span_1)
        }
    }
}
