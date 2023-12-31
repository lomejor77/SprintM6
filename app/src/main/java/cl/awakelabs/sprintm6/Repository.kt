package cl.awakelabs.sprintm6

import android.util.Log
import androidx.lifecycle.LiveData
import cl.awakelabs.sprintm6.data.local.PhoneDAO
import cl.awakelabs.sprintm6.data.local.PhoneEntity
import cl.awakelabs.sprintm6.data.remote.Phone
import cl.awakelabs.sprintm6.data.remote.PhoneAPI

class Repository(private val phoneAPI: PhoneAPI, private val phoneDAO: PhoneDAO) {

    fun obtainPhonesEntity(): LiveData<List<PhoneEntity>> = phoneDAO.getPhones()
    fun obtainIdPhone(id: String): LiveData<PhoneEntity> = phoneDAO.getPhoneDetail(id)
    suspend fun getPhones(){
        val response = phoneAPI.getDatas()
        if (response.isSuccessful) {
            val resp = response.body()
            resp?.let { phone ->
                val phonesEntity = phone.map { it.transform() }
                phoneDAO.insertPhones(phonesEntity)
            }
        }else{
            Log.e("repositorio", response.errorBody().toString())
        }

    }
   // fun obtainIdPhone(id: String): LiveData<PhoneEntity> = phoneDAO.getPhoneDetail(id)
    fun Phone.transform(): PhoneEntity = PhoneEntity(this.id, this.name, this.price, this.image)
}
