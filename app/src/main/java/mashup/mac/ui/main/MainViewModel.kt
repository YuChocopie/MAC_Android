package mashup.mac.ui.main

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import mashup.data.repository.CounselingRepository
import mashup.mac.base.BaseViewModel
import mashup.mac.ext.EventMutableLiveData
import mashup.mac.ext.postEvent
import mashup.mac.model.Category
import mashup.mac.model.CounselingMapModel
import mashup.mac.ui.counseling.CounselingWriteActivity
import mashup.mac.ui.mypage.MyPageActivity
import mashup.mac.util.log.Dlog

class MainViewModel(
    private val counselingRepository: CounselingRepository
) : BaseViewModel() {

    private val _mapItems = MutableLiveData<List<CounselingMapModel>>()
    val mapItems: LiveData<List<CounselingMapModel>> = _mapItems
    private val _mainListView = MutableLiveData<CounselingWebView>()
    val mainListView: LiveData<CounselingWebView> = _mainListView

    private val eventShowToast = EventMutableLiveData<String>()

    private val kilometers: List<Double> =
        listOf(0.0, 5.0, 10.0, 30.0, 100.0, 500.0, 1000.0, 9999.0, 99999.0)
    var distanceText = MutableLiveData<String>()
    var distanceLevel = 0
    val reset = MutableLiveData<Unit>()

    enum class CounselingWebView { DETAIL, LIST }

    init {
        distanceText.postValue(distanceTextFormat(distanceLevel))
    }

    fun getDistanceMin(): Double = kilometers[distanceLevel]
    fun getDistanceMax(): Double = kilometers[distanceLevel + 1]

    private fun distanceTextFormat(distanceLevel: Int) =
        "${kilometers[distanceLevel].toInt()}~${kilometers[distanceLevel + 1].toInt()}km"

    fun onClickCounselingWrite(context: Context) {
        val intent = Intent(context, CounselingWriteActivity::class.java)
        context.startActivity(intent)
    }

    fun onClickLocationLeft() {
        if (distanceLevel != 0) {
            distanceLevel -= 1
            distanceText.postValue(distanceTextFormat(distanceLevel))
        }
    }

    fun onClickLocationRight() {
        if (distanceLevel < kilometers.size - 2) {
            distanceLevel += 1
            distanceText.postValue(distanceTextFormat(distanceLevel))
        }
    }

    fun onClickMyPage(context: Context) {
        val intent = Intent(context, MyPageActivity::class.java)
        context.startActivity(intent)
    }

    fun onClickList() {
        _mainListView.postValue(CounselingWebView.LIST)
    }

    fun onClickReset() {
        reset.postValue(Unit)
    }

    fun loadData() {
        counselingRepository.getCounselingList(getDistanceMin(), getDistanceMax(),6)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isSuccess()) {
                    val data = it.data
                    Dlog.e("data : $data")
                    _mapItems.postValue(data.map { counseling ->
                        counseling.category?.title
                        CounselingMapModel(
                            id = counseling.id ?: 0,
                            title = counseling.title ?: "",
                            content = counseling.content ?: "",
                            category = Category.getFromTitleCategory(
                                counseling.category?.title ?: ""
                            ),
                            commentCount = counseling.commentCount ?: 0,
                            date = counseling.createdAt ?: "",
                            emotion = counseling.emotion,
                            userId = counseling.userId,
                            select = false,
                            distance = counseling.distance?.toInt() ?: 0
                        )
                    })
                } else {
                    showToast(it.error)
                }
            }) {
                Dlog.e(it.message)
            }.also {
                compositeDisposable.add(it)
            }
    }

    private fun showToast(msg: String?) {
        msg?.let {
            if (it.isNotEmpty()) {
                eventShowToast.postEvent(it)
            }
        }
    }
}