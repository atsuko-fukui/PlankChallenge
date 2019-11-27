package com.example.muumuu.plankchallenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.airbnb.epoxy.EpoxyRecyclerView
import com.example.muumuu.plankchallenge.model.Record
import com.example.muumuu.plankchallenge.viewmodel.RecordViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RecordFragment : Fragment() {

    private val recyclerView: EpoxyRecyclerView
        get() = view!!.findViewById(R.id.recycler_view)

    private val viewModel = RecordViewModel()
    private val disposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_record, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = context ?: return
        disposable.addAll(
            viewModel.fetchAllRecord(context)
                .subscribeOn(Schedulers.io())
                .subscribe(),

            viewModel.observeAllRecord()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateRecordList)
        )
    }

    private fun updateRecordList(records: List<Record>) {
        recyclerView.withModels {
            records.forEachIndexed { index, record ->
                RecordItemViewModel_()
                    .id(index)
                    .recordDate(record.date)
                    .addTo(this)
            }
        }
    }
}